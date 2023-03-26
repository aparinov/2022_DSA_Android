from .utils import client, register, verify, create_project, apply, decide


def _create_project(client, email, title)-> int:
    register(client=client, body={
        "email": email,
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": email, "verification_code": "0000"})
    return create_project(
        client=client,
        body={
            "author_email": email,
            "title": title,
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
    )


def _apply(client, email, id)-> int:
    register(client=client, body={
        "email": email,
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": email, "verification_code": "0000"})
    return apply(
        client=client,
        body={
            "project_id": id,
            "email": email,
        }
    )


def test_decide_accepted():
    id = _create_project(client, "test27@edu.hse.ru", "Название проекта 10")
    application_id = _apply(client, "test28@edu.hse.ru", id)
    decide(client=client, body={"application_id": application_id, "decision": "ACCEPTED"})


def test_decide_rejected():
    id = _create_project(client, "test29@edu.hse.ru", "Название проекта 11")
    application_id = _apply(client, "test30@edu.hse.ru", id)
    decide(client=client, body={"application_id": application_id, "decision": "REJECTED"})
    
