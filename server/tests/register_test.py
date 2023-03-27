from .utils import client, register, verify


def test_register():
    register(client=client, body={
        "email": "test@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })


def test_register_non_verified_form():
    register(client=client, body={
        "email": "test1@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    register(client=client, body={
        "email": "test1@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })


def test_invalid_email():
    register(client=client, body={
        "email": "test2@smth.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    }, expected_code=400, expected_json={"detail": "Invalid email address"})


def test_register_already_registered_profile():
    register(client=client, body={
        "email": "test3@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={"email": "test3@edu.hse.ru", "verification_code": "0000"})

    register(client=client, body={
        "email": "test3@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    }, expected_code=400, expected_json={"detail": "Profile with this email already exists"})
