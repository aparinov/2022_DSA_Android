from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError
import log
import logging
import datetime

import models
import schemas
from database import get_db

router = APIRouter(prefix='/applications')


@router.post("/apply")
def apply(request: schemas.CreateApplicationRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    project = db.query(models.Project).filter(
        models.Project.id == request.project_id).first()
    if project is None:
        logging.warning("Project is not found")
        raise HTTPException(status_code=404, detail="Project is not found")
    if project.current_number_of_students >= project.max_number_of_students:
        logging.warning("Recruiting is finished")
        raise HTTPException(status_code=400, detail="Recruiting is finished")
    if db.query(models.ProjectParticipant).filter(
            models.ProjectParticipant.project_id == request.project_id).filter(
            models.ProjectParticipant.student_email == request.email).count() > 0:
        logging.warning("The student is already participating in this project")
        raise HTTPException(
            status_code=400, detail="Student is already participating in this project")

    try:
        application = models.Application(
            project_id=request.project_id, student_email=request.email)
        db.add(application)
        db.commit()
        db.refresh(application)
    except IntegrityError:
        logging.warning("Duplicate application")
        raise HTTPException(
            status_code=400, detail="The student has already applied for this project")

    response = schemas.CreateApplicationResponse(application_id=application.id)
    logging.info({"response": response.json()})
    return response


@router.post("/decide")
def decide(request: schemas.DecideApplicationRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    if request.decision not in ["ACCEPTED", "REJECTED"]:
        logging.warning("Incorrect application decision")
        raise HTTPException(status_code=400, detail="Incorrect decision")

    application = db.query(models.Application).filter(
        models.Application.id == request.application_id).first()
    if application is None:
        logging.warning("Application is not found")
        raise HTTPException(status_code=404, detail="Application is not found")
    if application.status != "CREATED":
        logging.warning("Decision on this application has already been made")
        raise HTTPException(
            status_code=400, detail="Decision on this application has already been made")

    application.status = request.decision
    application.updated_at = datetime.datetime.now()
    if application.status == "ACCEPTED":
        project = db.query(models.Project).filter(models.Project.id == application.project_id).first()
        if project.current_number_of_students >= project.max_number_of_students:
            raise HTTPException(status_code=400, detail="Recruiting is finished")
        project.current_number_of_students += 1
        db.add(models.ProjectParticipant(project_id=project.id, student_email=application.student_email))
    db.commit()
