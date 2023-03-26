from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import log
import logging

import models
import schemas
from database import get_db

router = APIRouter(prefix='/login')


@router.post("")
def login(request: schemas.LoginRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    profile = db.query(models.Profile).filter(models.Profile.email == request.email).first()
    if not profile:
        logging.warning("Profile not found")
        raise HTTPException(status_code=404, detail="Profile not found")
    if profile.password != request.password:
        logging.warning("Incorrect password")
        raise HTTPException(status_code=400, detail="Incorrect password")
    logging.info({"response": "OK"})
    return Response(status_code=200)
