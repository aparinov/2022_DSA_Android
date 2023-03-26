from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import log
import logging

import models
import schemas
from database import get_db

router = APIRouter(prefix='/tags')


@router.get("")
def get_tags(db: Session = Depends(get_db)):
    logging.info({"request": {}})

    response = schemas.GetTagsResponse(
        tags=[entry.tag for entry in db.query(models.Tag).all()])

    logging.info({"response": response.json()})
    return response
