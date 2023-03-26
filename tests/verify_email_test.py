from .utils import client, register, verify


def test_verify():
    register(client=client, body={
        "email": "test4@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test4@edu.hse.ru", "verification_code": "0000"})


def test_verify_not_found():
    verify(client=client,
           body={"email": "test5@edu.hse.ru", "verification_code": "0000"},
           expected_code=404, expected_json={"detail": "User not found"})


def test_verify_incorrect_code():
    register(client=client, body={
        "email": "test6@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client,
           body={"email": "test6@edu.hse.ru", "verification_code": "1111"},
           expected_code=400,
           expected_json={"detail": "Incorrect verification code"
    })


def test_verify_already_verified():
    register(client=client, body={
        "email": "test7@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test7@edu.hse.ru", "verification_code": "0000"})
    verify(client=client,
           body={"email": "test7@edu.hse.ru", "verification_code": "0000"},
           expected_code=400,
           expected_json={"detail": "Email is already verified"
    })
