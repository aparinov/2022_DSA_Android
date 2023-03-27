import typing

from .utils import client, register, verify, edit_user_info, create_project, edit_project


def _prepare() -> typing.List[int]:
    # Автор и 3 студанта.
    emails = ["test21@edu.hse.ru", "test22@edu.hse.ru", "test23@edu.hse.ru", "test24@edu.hse.ru"]
    # Теги профилей (проставляем только студентам).
    profile_tags = [
        ["C"],
        ["C"],
        ["C", "C++"],
        ["C", "C++", "IOS"],
    ]
    # Регистрируем профили с нужными тегами.
    for email, tags in zip(emails, profile_tags):
        register(client=client, body={
            "email": email,
            "first_name": "Тест",
            "last_name": "Тестов",
            "password": "test_password"
        })
        verify(client=client, body={
               "email": email, "verification_code": "0000"})
        if tags:
            edit_user_info(client=client, email=email, body={"tags": tags})

    # Теги проектов.
    project_tags = [
        ["C"],
        ["C", "C++"],
        ["C++"],
        ["C++", "IOS"],
        ["IOS"],
        ["C", "C++", "IOS"],
        ["C", "C++", "IOS"],
    ]
    counter = 1
    project_ids = []
    # Создаем проекты с нужными тегами.
    for tags in project_tags:
        project_id = create_project(
            client=client,
            body={
                "author_email": emails[0],
                "title": f"Рекомендация {counter}",
                "description": "Описание проекта.",
                "max_number_of_students": 1,
                "tags": tags,
            }
        )
        counter += 1
        project_ids.append(project_id)
    # Завершаем набор в последнем проекте.
    edit_project(client=client, body={"id": project_ids[-1], "recruiting_status": "FINISHED"})
    return project_ids, emails


def test_recomment():
    '''
    project 1: C
    project 2: C, C++
    project 3: C++
    project 4: C++, IOS
    project 5: IOS
    project 6: C, C++, IOS
    project 7 (набор закончен): C, C++, IOS

    profile 1 (author): C
    profile 2: C
    profile 3: C, C++
    profile 4: C, C++, IOS
    '''
    project_ids, emails = _prepare()

    def _get_project_info(index: int):
        tags_by_index = [
            ["C"], ["C++", "C"], ["C++"], ["C++", "IOS"], ["IOS"], ["C++", "C", "IOS"],
        ]
        return {
                "id": project_ids[index],
                "title": f"Рекомендация {index+1}",
                "description": "Описание проекта.",
                "status_string": "Набор не начат, работа не начата",
                "tags": tags_by_index[index],
            }

    expected_results = [
        # Первый профиль является автором для всех проектов.
        [],
        # 1.0, 0.5, 0.33
        [
            _get_project_info(0),
            _get_project_info(1),
            _get_project_info(5),
        ],
        # 1.0, 0.66, 0.5, 0.5, 0.33
        [
            _get_project_info(1),
            _get_project_info(5),
            _get_project_info(0),
            _get_project_info(2),
            _get_project_info(3),
        ],
        # 1.0, 0.66, 0.66, 0.33, 0.33, 0.33
        [
            _get_project_info(5),
            _get_project_info(1),
            _get_project_info(3),
            _get_project_info(0),
            _get_project_info(2),
            _get_project_info(4),
        ],
    ]
    for email, expected_result in zip(emails, expected_results):
        response = client.get("/recommend", params={"email": email})
        assert response.status_code == 200
        assert response.json() == {"projects": expected_result}
