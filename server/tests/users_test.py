from .utils import client, register, verify, get_user_info, edit_user_info


def test_get_user_info():
    register(client=client, body={
        "email": "test11@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test11@edu.hse.ru", "verification_code": "0000"})
    get_user_info(
        client=client,
        email="test11@edu.hse.ru",
        expected_code=200,
        expected_json={
            "is_student": True,
            "first_name": "Тест",
            "last_name": "Тестов",
            "contacts": "test11@edu.hse.ru",
            "tags": [],
        },
    )


def test_get_user_info_not_found():
    get_user_info(
        client=client,
        email="test12@edu.hse.ru",
        expected_code=404,
        expected_json={
            "detail": "Profile not found"
        },
    )


def test_edit_user_info():
    register(client=client, body={
        "email": "test13@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test13@edu.hse.ru", "verification_code": "0000"})
    edit_user_info(
        client=client,
        email="test13@edu.hse.ru",
        body={
            "middle_name": "Middlename",
            "bio": "Bio.",
            "faculty": "Faculty",
            "program": "Educational program",
            "degree": "Bachelor",
            "year_of_study": 3,
            "tags": ["C++", "Веб"],
        }
    )
    get_user_info(
        client=client,
        email="test13@edu.hse.ru",
        expected_code=200,
        expected_json={
            "is_student": True,
            "first_name": "Тест",
            "last_name": "Тестов",
            "contacts": "test13@edu.hse.ru",
            "middle_name": "Middlename",
            "bio": "Bio.",
            "faculty": "Faculty",
            "program": "Educational program",
            "degree": "Bachelor",
            "year_of_study": 3,
            "tags": ["C++", "Веб"],
        },
    )
    edit_user_info(
        client=client,
        email="test13@edu.hse.ru",
        body={
            "tags": ["Android"],
        }
    )
    get_user_info(
        client=client,
        email="test13@edu.hse.ru",
        expected_code=200,
        expected_json={
            "is_student": True,
            "first_name": "Тест",
            "last_name": "Тестов",
            "contacts": "test13@edu.hse.ru",
            "middle_name": "Middlename",
            "bio": "Bio.",
            "faculty": "Faculty",
            "program": "Educational program",
            "degree": "Bachelor",
            "year_of_study": 3,
            "tags": ["Android"],
        },
    )


def test_edit_user_info_not_found():
    edit_user_info(
        client=client,
        email="test14@edu.hse.ru",
        body={"middle_name": "Middlename"},
        expected_code=404,
        expected_json={"detail": "Profile not found"},
    )
