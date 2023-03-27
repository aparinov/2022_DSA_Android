from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import log
import logging

import models
import schemas
from database import get_db

router = APIRouter(prefix='/users')


@router.get("", response_model=schemas.UserInfoResponse, response_model_exclude_none=True)
def get_user_info(email: str, db: Session = Depends(get_db)):
    logging.info({"request": {"email", email}})

    profile = db.query(models.Profile).filter(
        models.Profile.email == email).first()
    if not profile:
        logging.warning(f"Couldn't find profile for {email}")
        raise HTTPException(status_code=404, detail="Profile not found")
    profile_info = db.query(models.ProfileInfo).filter(
        models.ProfileInfo.email == email).first()
    if not profile_info:
        logging.error(
            f"Couldn't find profile info for {email}, but found profile")
        raise HTTPException(status_code=404, detail="Profile info not found")
    tag_ids = [entry.tag_id for entry in db.query(
        models.ProfileTag).filter(models.ProfileTag.email == email).all()]
    tags = []
    for tag_id in tag_ids:
        tag = db.query(models.Tag).filter(models.Tag.id == tag_id).first()
        if tag is None:
            logging.error("Invalid tag")
            raise HTTPException(status_code=400, detail="Invalid tag")
        tags.append(tag.tag)

    response = schemas.UserInfoResponse(
        is_student=profile_info.student,
        first_name=profile.first_name,
        last_name=profile.last_name,
        middle_name=profile.middle_name,
        contacts=profile.email,
        nickname=profile_info.nickname,
        degree=profile_info.degree,
        faculty=profile_info.faculty,
        program=profile_info.program,
        year_of_study=profile_info.year_of_study,
        bio=profile_info.bio,
        tags=tags,
    )

    logging.info({"response": response.json()})
    return response


@router.post("")
def edit_user_info(email: str, request: schemas.EditUserInfoRequest, db: Session = Depends(get_db)):
    logging.info({"request": {"email": email, "body": request}})

    logging.info(email)
    profile = db.query(models.Profile).filter(models.Profile.email == email)
    if profile.count() == 0:
        logging.warning(f"Couldn't find profile for {email}")
        raise HTTPException(status_code=404, detail="Profile not found")
    profile_info = db.query(models.ProfileInfo).filter(
        models.ProfileInfo.email == email)
    if profile_info.count() == 0:
        logging.error(
            f"Couldn't find profile info for {email}, but found profile")
        raise HTTPException(status_code=404, detail="Profile info not found")

    def update_profile(column, value):
        if value is not None:
            profile.update({column: value})

    def update_profile_info(column, value):
        if value is not None:
            profile_info.update({column: value})

    update_profile(models.Profile.first_name, request.first_name)
    update_profile(models.Profile.last_name, request.last_name)
    update_profile(models.Profile.middle_name, request.middle_name)
    update_profile_info(models.ProfileInfo.nickname, request.nickname)
    update_profile_info(models.ProfileInfo.degree, request.degree)
    update_profile_info(models.ProfileInfo.faculty, request.faculty)
    update_profile_info(models.ProfileInfo.program, request.program)
    update_profile_info(models.ProfileInfo.year_of_study,
                        request.year_of_study)
    update_profile_info(models.ProfileInfo.bio, request.bio)

    if request.tags:
        db.query(models.ProfileTag).filter(models.ProfileTag.email == email).delete()
        for tag in request.tags:
            entry = db.query(models.Tag).filter(models.Tag.tag == tag).first()
            if entry is None:
                logging.error(f"Invalid tag {tag}")
                raise HTTPException(status_code=400, detail=f"Invalid tag {tag}")
            db.add(models.ProfileTag(email=email, tag_id=entry.id))

    db.commit()

    logging.info({"response": "OK"})
    return Response(status_code=200)
