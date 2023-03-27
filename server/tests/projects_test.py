from .utils import client, register, verify, create_project, get_project, edit_project, delete_project, get_projects_by_email


def test_create_project():
    register(client=client, body={
        "email": "test15@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test15@edu.hse.ru", "verification_code": "0000"})
    id_1 = create_project(
        client=client,
        body={
            "author_email": "test15@edu.hse.ru",
            "title": "Название проекта",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
            "planned_start_of_work": "31.07.2023",
        },
    )
    id_2 = create_project(
        client=client,
        body={
            "author_email": "test15@edu.hse.ru",
            "title": "Название проекта 1",
            "description": "Описание проекта.",
            "max_number_of_students": 2,
            "applications_deadline": "01.12.2000",
            "tags": ["Теория языков программирования", "JavaScript"],
        },
    )
    get_project(
        client=client,
        id=id_1,
        expected_code=200,
        expected_json={
            "id": id_1,
            "author": "Тестов Тест",
            "author_email": "test15@edu.hse.ru",
            "title": "Название проекта",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
            "current_number_of_students": 0,
            "project_status": "NOT_STARTED",
            "recruiting_status": "NOT_STARTED",
            "planned_start_of_work": "31.07.2023",
            "tags": [],
        }
    )
    get_project(
        client=client,
        id=id_2,
        expected_code=200,
        expected_json={
            "id": id_2,
            "author": "Тестов Тест",
            "author_email": "test15@edu.hse.ru",
            "title": "Название проекта 1",
            "description": "Описание проекта.",
            "max_number_of_students": 2,
            "current_number_of_students": 0,
            "project_status": "NOT_STARTED",
            "recruiting_status": "NOT_STARTED",
            "applications_deadline": "01.12.2000",
            "tags": ["JavaScript", "Теория языков программирования"],
        }
    )


def test_create_project_conflict():
    register(client=client, body={
        "email": "test16@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test16@edu.hse.ru", "verification_code": "0000"})
    create_project(
        client=client,
        body={
            "author_email": "test16@edu.hse.ru",
            "title": "Название проекта 2",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
    )
    create_project(
        client=client,
        body={
            "author_email": "test16@edu.hse.ru",
            "title": "Название проекта 2",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
            "tags": ["Теория языков программирования", "JavaScript"],
        },
        expected_code=409,
        expected_json={"detail": "Project with this title already exists"},
    )


def test_create_project_incorrect_profile():
    create_project(
        client=client,
        body={
            "author_email": "test17@edu.hse.ru",
            "title": "Название проекта 3",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
        expected_code=404,
        expected_json={"detail": "Profile is not found"},
    )


def test_get_project_not_fount():
    get_project(
        client=client,
        id=-1,
        expected_code=404,
        expected_json={
            "detail": "Project is not found",
        }
    )


def test_edit_project():
    register(client=client, body={
        "email": "test18@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test18@edu.hse.ru", "verification_code": "0000"})
    id_1 = create_project(
        client=client,
        body={
            "author_email": "test18@edu.hse.ru",
            "title": "Название проекта 4",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
            "planned_finish_of_work": "10.10.2024",
            "tags": ["Теория языков программирования", "JavaScript"],
        },
    )
    edit_project(
        client=client,
        body={
            "id": id_1,
            "title": "Название проекта 5",
            "description": "Другое описание проекта",
            "max_number_of_students": 2,
            "project_status": "IN_PROGRESS",
            "planned_finish_of_work": "01.01.2024",
            "tags": ["Java"],
        }
    )
    get_project(
        client=client,
        id=id_1,
        expected_code=200,
        expected_json={
            "id": id_1,
            "author": "Тестов Тест",
            "author_email": "test18@edu.hse.ru",
            "title": "Название проекта 5",
            "description": "Другое описание проекта",
            "max_number_of_students": 2,
            "current_number_of_students": 0,
            "project_status": "IN_PROGRESS",
            "recruiting_status": "NOT_STARTED",
            "planned_finish_of_work": "01.01.2024",
            "tags": ["Java"],
        }
    )


def test_delete_project():
    register(client=client, body={
        "email": "test19@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test19@edu.hse.ru", "verification_code": "0000"})
    id_1 = create_project(
        client=client,
        body={
            "author_email": "test19@edu.hse.ru",
            "title": "Название проекта 6",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
    )
    delete_project(client=client, id=id_1)


def test_get_projects_by_email():
    register(client=client, body={
        "email": "test20@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test20@edu.hse.ru", "verification_code": "0000"})
    id_1 = create_project(
        client=client,
        body={
            "author_email": "test20@edu.hse.ru",
            "title": "Название проекта 7",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
    )
    id_2 = create_project(
        client=client,
        body={
            "author_email": "test20@edu.hse.ru",
            "title": "Название проекта 8",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
            "tags": ["Теория языков программирования", "JavaScript"],
        },
    )
    id_3 = create_project(
        client=client,
        body={
            "author_email": "test20@edu.hse.ru",
            "title": "Название проекта 9",
            "description": "Описание проекта.",
            "max_number_of_students": 1,
        },
    )
    get_projects_by_email(
        client=client,
        email="test20@edu.hse.ru",
        expected_code=200,
        expected_json={
            "projects": [
                {
                    "id": id_1,
                    "title": "Название проекта 7",
                    "description": "Описание проекта.",
                    "status_string": "Набор не начат, работа не начата",
                    "tags": [],
                },
                {
                    "id": id_2,
                    "title": "Название проекта 8",
                    "description": "Описание проекта.",
                    "status_string": "Набор не начат, работа не начата",
                    "tags": ["JavaScript", "Теория языков программирования"],
                },
                {
                    "id": id_3,
                    "title": "Название проекта 9",
                    "description": "Описание проекта.",
                    "status_string": "Набор не начат, работа не начата",
                    "tags": [],
                },
            ]
        }
    )
