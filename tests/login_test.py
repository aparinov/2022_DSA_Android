from .utils import client, register, verify, login


def test_login():
    register(client=client, body={
        "email": "test8@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test8@edu.hse.ru", "verification_code": "0000"})
    login(client=client, body={"email": "test8@edu.hse.ru", "password": "test_password"})


def test_login_incorrect_password():
    register(client=client, body={
        "email": "test9@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test9@edu.hse.ru", "verification_code": "0000"})
    login(
        client=client,
        body={"email": "test9@edu.hse.ru", "password": "password"},
        expected_code=400,
        expected_json={"detail": "Incorrect password"}
    )


def test_login_not_found():
    login(
        client=client,
        body={"email": "test10@edu.hse.ru", "password": "password"},
        expected_code=404,
        expected_json={"detail": "Profile not found"}
    )
