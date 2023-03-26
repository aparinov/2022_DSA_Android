from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from main import app
from database import get_db

SQLALCHEMY_DATABASE_URL = "postgresql://user1:password@localhost/testdb"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL
)
TestingSessionLocal = sessionmaker(
    autocommit=False, autoflush=False, bind=engine)


def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()


app.dependency_overrides[get_db] = override_get_db

client = TestClient(app)


def register(client: TestClient,
             body: dict,
             expected_code: int = 200,
             expected_json: dict = None):
    response = client.post("/register", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def verify(client: TestClient,
           body: dict,
           expected_code: int = 200,
           expected_json: dict = None):
    response = client.post("/verify-email", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def login(client: TestClient,
           body: dict,
           expected_code: int = 200,
           expected_json: dict = None):
    response = client.post("/login", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def get_user_info(client: TestClient,
                  email: str,
                  expected_code: int = 200,
                  expected_json: dict = None):
    response = client.get("/users", params={"email": email})
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def edit_user_info(client: TestClient,
                   email: str,
                   body: dict,
                   expected_code: int = 200,
                   expected_json: dict = None):
    response = client.post("/users", params={"email": email}, json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def create_project(client: TestClient,
                   body: dict,
                   expected_code: int = 200,
                   expected_json: dict = None)-> int:
    response = client.post("/projects", json=body)
    assert response.status_code == expected_code
    json = response.json()
    if expected_json:
        assert json == expected_json
    return json.get("id")


def get_project(client: TestClient,
                id: int,
                expected_code: int = 200,
                expected_json: dict = None):
    response = client.get(f"/projects/{id}")
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def edit_project(client: TestClient,
                 body: dict,
                 expected_code: int = 200,
                 expected_json: dict = None):
    response = client.put("/projects", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def delete_project(client: TestClient,
                   id: int,
                   expected_code: int = 200,
                   expected_json: dict = None):
    response = client.delete("/projects", params={"id": id})
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def get_projects_by_email(client: TestClient,
                          email: str,
                          expected_code: int = 200,
                          expected_json: dict = None):
    response = client.get("/projects", params={"email": email})
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def search_projects(client: TestClient,
                    body: dict,
                    expected_code: int = 200,
                    expected_json: dict = None):
    response = client.post("/search-projects", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json


def apply(client: TestClient,
          body: dict,
          expected_code: int = 200,
          expected_json: dict = None) -> int:
    response = client.post("/applications/apply", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json
    return response.json().get("application_id")


def decide(client: TestClient,
           body: dict,
           expected_code: int = 200,
           expected_json: dict = None) -> int:
    response = client.post("/applications/decide", json=body)
    assert response.status_code == expected_code
    if expected_json:
        assert response.json() == expected_json
