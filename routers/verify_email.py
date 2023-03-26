from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import log
import logging

import models
import schemas
from database import get_db

router = APIRouter(prefix='/verify-email')


# Route to verify a user's email
@router.post("")
def verify_email(request: schemas.VerifyEmailRequest, db: Session = Depends(get_db)):
    logging.info({"request": request.json()})

    registration_form = db.query(models.RegistrationForm).filter(
        models.RegistrationForm.email == request.email).first()
    if registration_form is None:
        logging.warning("User not found")
        raise HTTPException(status_code=404, detail="User not found")
    if registration_form.verified:
        logging.warning("Email is already verified")
        raise HTTPException(status_code=400, detail="Email is already verified")
    if registration_form.verification_code != request.verification_code:
        logging.warning("Incorrect varification code")
        raise HTTPException(
            status_code=400, detail="Incorrect verification code")

    # Update the user's verified status in the database
    registration_form.verified = True

    # Create profile_info and profile.
    profile_info = models.ProfileInfo(
        email=registration_form.email,
        student=True
    )
    db.add(profile_info)

    profile = models.Profile(
        email=registration_form.email,
        first_name=registration_form.first_name,
        last_name=registration_form.last_name,
        middle_name=registration_form.middle_name,
        password=registration_form.password
    )
    db.add(profile)
    db.commit()

    logging.info({"response": "OK"})
    return Response(status_code=200)
