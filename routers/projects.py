from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy import exc
from sqlalchemy.orm import Session
import log
import logging
import datetime
import typing

import models
import schemas
from database import get_db

router = APIRouter(prefix='/projects')


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


def _parse_date(date: str) -> datetime.datetime:
    if date is None:
        return None
    parts = date.split(".")
    assert len(parts) == 3
    return datetime.datetime(year=int(parts[2]), month=int(parts[1]), day=int(parts[0]))


def _serialize_date(date: datetime.datetime) -> str:
    if date is None:
        return None
    result = ""
    if date.day < 10:
        result += f"0{date.day}"
    else:
        result += str(date.day)
    result += "."
    if date.month < 10:
        result += f"0{date.month}"
    else:
        result += str(date.month)
    result += "."
    result += str(date.year)
    return result


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


def _get_tag_ids(db: Session, tags: typing.List[str] | None) -> typing.List[int]:
    tag_ids = []
    if tags:
        for tag in tags:
            entry = db.query(models.Tag).filter(models.Tag.tag == tag).first()
            if entry is None:
                logging.warning(f"Invalid tag: {tag}")
                raise HTTPException(status_code=400, detail="Invalid tag")
            tag_ids.append(entry.id)
    return list(set(tag_ids))


@router.post("")
def create_project(request: schemas.CreateProjectRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    author = db.query(models.Profile).filter(
        models.Profile.email == request.author_email).first()
    if author is None:
        logging.error("Couldn't find author of project")
        raise HTTPException(status_code=404, detail="Profile is not found")

    tag_ids = _get_tag_ids(db, request.tags)

    project = models.Project(
        author_email=request.author_email,
        title=request.title,
        title_lower=request.title.lower(),
        description=request.description,
        max_number_of_students=request.max_number_of_students,
        recruiting_status=request.recruiting_status,
        project_status=request.project_status,
        applications_deadline=_parse_date(request.applications_deadline),
        planned_start_of_work=_parse_date(request.planned_start_of_work),
        planned_finish_of_work=_parse_date(request.planned_finish_of_work),
    )
    try:
        db.add(project)
        db.flush()
        db.refresh(project)
        for tag_id in tag_ids:
            db.add(models.ProjectTags(project_id=project.id, tag_id=tag_id))
        db.commit()
    except exc.IntegrityError:
        logging.warning("Couldn't add project to database")
        raise HTTPException(
            status_code=409, detail="Project with this title already exists")

    response = {"id": project.id}
    logging.info({"response": response})
    return response


@router.get("/{id}", response_model=schemas.GetProjectResponse, response_model_exclude_none=True)
def get_project_by_id(id: int, db: Session = Depends(get_db)):
    logging.info({"request": {"id": id}})

    project = db.query(models.Project).filter(models.Project.id == id).first()
    if project is None:
        logging.warning("Project is not found")
        raise HTTPException(status_code=404, detail="Project is not found")

    author = db.query(models.Profile).filter(
        models.Profile.email == project.author_email).first()
    if author is None:
        logging.error("Couldn't find author of project")
        raise HTTPException(status_code=404, detail="Profile is not found")

    author: str = " ".join([author.last_name, author.first_name,
                            author.middle_name if author.middle_name else ""])
    author = author.strip()
    tags = _get_tags_by_project(db, project.id)
    response = schemas.GetProjectResponse(
        id=project.id,
        author=author,
        author_email=project.author_email,
        title=project.title,
        description=project.description,
        max_number_of_students=project.max_number_of_students,
        current_number_of_students=project.current_number_of_students,
        recruiting_status=project.recruiting_status,
        project_status=project.project_status,
        applications_deadline=_serialize_date(project.applications_deadline),
        planned_start_of_work=_serialize_date(project.planned_start_of_work),
        planned_finish_of_work=_serialize_date(project.planned_finish_of_work),
        tags=tags,
    )

    logging.info({"response": response.json()})
    return response


@router.get("", response_model=schemas.SearchProjectsResponse, response_model_exclude_none=True)
def get_project_by_email(email: str, db: Session = Depends(get_db)):
    logging.info({"request": {"email": email}})

    projects = db.query(models.Project).filter(
        models.Project.author_email == email).all()
    response = schemas.SearchProjectsResponse()
    for project in projects:
        tags = _get_tags_by_project(db, project.id)
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


@router.put("")
def put_project(request: schemas.PutProjectRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    project = db.query(models.Project).filter(models.Project.id == request.id)
    if project.count() == 0:
        logging.warning("Project is not found")
        raise HTTPException(status_code=404, detail="Project is not found")

    def update_project(column, value):
        if value is not None:
            project.update({column: value})
            if column == models.Project.title:
                project.update({models.Project.title_lower: value.lower()})

    update_project(models.Project.title, request.title)
    update_project(models.Project.description, request.description)
    update_project(models.Project.max_number_of_students,
                   request.max_number_of_students)
    update_project(models.Project.recruiting_status, request.recruiting_status)
    update_project(models.Project.project_status, request.project_status)
    update_project(models.Project.applications_deadline,
                   _parse_date(request.applications_deadline))
    update_project(models.Project.planned_start_of_work,
                   _parse_date(request.planned_start_of_work))
    update_project(models.Project.planned_finish_of_work,
                   _parse_date(request.planned_finish_of_work))

    if request.tags:
        tag_ids = _get_tag_ids(db, request.tags)
        db.query(models.ProjectTags).filter(models.ProjectTags.project_id == request.id).delete()
        for tag_id in tag_ids:
            db.add(models.ProjectTags(project_id=request.id, tag_id=tag_id))

    db.commit()

    logging.info({"response": "OK"})
    return Response(status_code=200)


@router.delete("")
def delete_project(id: int, db: Session = Depends(get_db)):
    logging.info({"request": {"id": id}})

    project = db.query(models.Project).filter(models.Project.id == id).first()
    if project is None:
        logging.warning("Project is not found")
        raise HTTPException(status_code=404, detail="Project is not found")

    db.delete(project)
    db.commit()

    logging.info({"response": "OK"})
    return Response(status_code=200)
