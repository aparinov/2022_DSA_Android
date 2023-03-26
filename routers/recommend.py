from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import log
import logging
import collections
import typing

import models
import schemas
from database import get_db

router = APIRouter(prefix='/recommend')


def _make_status_string(project: models.Project) -> str:
    status_string = ""
    if project.recruiting_status == "NOT_STARTED":
        status_string += "Набор не начат, "
    if project.recruiting_status == "IN_PROGRESS":
        status_string += "Набор идет, "
    if project.recruiting_status == "COMPLETED":
        status_string += "Набор закончен, "
    if project.project_status == "NOT_STARTED":
        status_string += "работа не начата"
    if project.project_status == "IN_PROGRESS":
        status_string += "работа идет"
    if project.project_status == "COMPLETED":
        status_string += "завершен"
    return status_string


def _get_tags_by_project(db: Session, project_id: int) -> typing.List[str]:
    tag_ids = [entry.tag_id for entry in db.query(models.ProjectTags).filter(
        models.ProjectTags.project_id == project_id).all()]
    tags = []
    for tag_id in tag_ids:
        tag = db.query(models.Tag).filter(models.Tag.id == tag_id).first()
        if tag is None:
            logging.error("Invalid tag id")
            raise HTTPException(status_code=400, detail="Invalid tag id")
        tags.append(tag.tag)
    return tags


@router.get("")
def recommend(email: str, db: Session = Depends(get_db)):
    logging.info({"request": {"email": email}})

    # Получаем предпочтения студента.
    profile_tags = [entry.tag_id for entry in db.query(models.ProfileTag).filter(
        models.ProfileTag.email == email).all()]
    if not profile_tags:
        return schemas.GetRecommendationsResponse()
    # Получаем все записи о проектах, которые содаржат хотя бы одно предпочтение студента.
    project_tags = db.query(models.ProjectTags).filter(
        models.ProjectTags.tag_id.in_(profile_tags)).all()

    # Массив уникальных идентификаторов проектов.
    project_ids = set(
        [project_tag.project_id for project_tag in project_tags])
    # Отсеиваем проекты, в которых студент является автором и которые завершили набор.
    filtered_project_ids = []
    for project_id in project_ids:
        project = db.query(models.Project).filter(
            models.Project.id == project_id).first()
        if project is None:
            raise HTTPException(status_code=400, detail=f"Couldn't find project by project id {project_id}")
        if project.author_email != email and project.recruiting_status in ["NOT_STARTED", "IN_PROGRESS"]:
            filtered_project_ids.append(project_id)
    project_ids = filtered_project_ids

    # Вычисляем размер пересечения тегов проекта и студента для каждого проекта.
    matched_tags = collections.defaultdict(int)
    for tag in project_tags:
        matched_tags[tag.project_id] += 1

    # Получаем количество тегов для каждого проекта.
    project_tags_numbers = {}
    for project_id in project_ids:
        project_tags_numbers[project_id] = db.query(models.ProjectTags).filter(
            models.ProjectTags.project_id == project_id).count()

    # Вычисляем релевантность каждого проекта согласно intersection over union.
    relevances = {}
    for project_id in project_ids:
        a = len(profile_tags)
        b = project_tags_numbers[project_id]
        c = matched_tags[project_id]
        relevances[project_id] = c / (a + b - c)

    relevances = [(project_id, relevance)
                  for project_id, relevance in relevances.items()]
    relevances.sort(key=lambda x: (-x[1], x[0]))
    relevances = relevances[:10]

    logging.info({"relevances": relevances})

    response = schemas.GetRecommendationsResponse()
    for project_id, _ in relevances:
        project = db.query(models.Project).filter(models.Project.id == project_id).first()
        if project is None:
            raise HTTPException(status_code=500, detail="Couldn't find project")
        response.projects.append(schemas.ProjectInfo(
            id=project_id,
            title=project.title,
            description=project.description,
            status_string=_make_status_string(project),
            tags=_get_tags_by_project(db, project.id),
        ))
    logging.info({"response": response.json()})
    return response
