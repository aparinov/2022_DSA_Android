from .utils import client, register, verify, create_project, search_projects


def test_search_by_substring():
    register(client=client, body={
        "email": "test25@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test25@edu.hse.ru", "verification_code": "0000"})

    titles = ["Один", "Два.", "Один? два!",
              "Один, два, три", "Два или три?", "Три."]
    project_ids = []
    for title in titles:
        project_id = create_project(
            client=client,
            body={
                "author_email": "test25@edu.hse.ru",
                "title": title,
                "description": "Описание проекта.",
                "max_number_of_students": 1,
                "tags": ["C"],
            }
        )
        project_ids.append(project_id)

    queries = ["Один", "ДВА", "тРи,"]
    expected_results = [
        [project_ids[0], project_ids[2], project_ids[3]],
        [project_ids[1], project_ids[2], project_ids[3], project_ids[4]],
        [project_ids[3], project_ids[4], project_ids[5]],
    ]

    for query, expected_result in zip(queries, expected_results):
        response = client.post(
            "/search-projects", json={"search_string": query})
        assert response.status_code == 200
        assert [project["id"]
                for project in response.json()["projects"]] == expected_result


def test_search_by_statuses():
    register(client=client, body={
        "email": "test26@edu.hse.ru",
        "first_name": "Тест",
        "last_name": "Тестов",
        "password": "test_password"
    })
    verify(client=client, body={
           "email": "test26@edu.hse.ru", "verification_code": "0000"})

    project_ids = []
    counter = 1
    for recruiting_status in ["NOT_STARTED", "IN_PROGRESS", "FINISHED"]:
        for project_status in ["IN_PROGRESS", "FINISHED"]:
            project_id = create_project(
                client=client,
                body={
                    "author_email": "test26@edu.hse.ru",
                    "title": f"Название проекта для поиска {counter}",
                    "description": "Описание проекта.",
                    "max_number_of_students": 1,
                    "recruiting_status": recruiting_status,
                    "project_statue": project_status,
                    "tags": ["C"],
                }
            )
            counter += 1
            project_ids.append(project_id)

    queries = [
        {"search_string": "поиска", "recruiting_status": "NOT_STARTED"},
        {"search_string": "поиска", "project_status": "IN_PROGRESS"},
        {"search_string": "поиска", "recruiting_status": "FINISHED", "project_status": "FINISHED"},
    ]
    expected_results = [
        [project_ids[0], project_ids[1]],
        [project_ids[0], project_ids[2], project_ids[4]],
        [project_ids[5]],
    ]

    for query, expected_result in zip(queries, expected_results):
        response = client.post("/search-projects", json=query)
        assert response.status_code == 200
        for project in response.json()["projects"]:
            assert project["id"] in expected_result
