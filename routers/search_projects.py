from fastapi import APIRouter, HTTPException
from fastapi.params import Depends
from sqlalchemy import func
from sqlalchemy.orm import Session
import log
import typing
import logging

import models
import schemas
from database import get_db

router = APIRouter(prefix='/search-projects')


def _tokenize(text: str) -> typing.List[str]:
    delimiters = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"
    for delimiter in delimiters:
        text = text.replace(delimiter, " ")
    return [t.lower() for t in text.split()]


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


@router.post("")
def search_projects(request: schemas.SearchProjectsRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    if (request.search_string is None
            and request.recruiting_status is None
            and request.project_status is None):
        logging.warning("One of search filters should be presented")
        raise HTTPException(status_code=400, detail="All filters are empty")

    query = db.query(models.Project)
    if request.recruiting_status:
        query = query.filter(
            models.Project.recruiting_status == request.recruiting_status)
    if request.project_status:
        query = query.filter(
            models.Project.project_status == request.project_status)
    if request.search_string:
        for term in _tokenize(request.search_string):
            query = query.filter(func.lower(
                models.Project.title_lower).contains(term))

    found_projects = query.limit(10).all()
    response = schemas.SearchProjectsResponse()
    for project in found_projects:
        project_tags = db.query(models.ProjectTags).filter(
            models.ProjectTags.project_id == project.id).all()
        tags = []
        for project_tag in project_tags:
            tag = db.query(models.Tag).filter(models.Tag.id == project_tag.tag_id).first()
            if tag is None:
                raise HTTPException(status_code=400, detail="Incorrect tag")
            tags.append(tag.tag)
        response.projects.append(
            schemas.ProjectInfo(
                id=project.id,
                title=project.title,
                description=project.description,
                status_string=_make_status_string(project),
                tags=tags,
            )
        )

    logging.info({"response": response.json()})
    return response
