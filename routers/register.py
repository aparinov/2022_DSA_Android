from fastapi import APIRouter, HTTPException, Response
from fastapi.params import Depends
from sqlalchemy.orm import Session
import secrets
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import ssl
import log
import logging

import models
import schemas
import settings as config
from database import get_db

router = APIRouter(prefix='/register')


# Route to register a user and send a verification email
@router.post("")
def register(form: schemas.RegisterRequest, db: Session = Depends(get_db)):
    logging.info({"request": form.json()})

    settings = config.get_settings()

    if not (form.email.endswith("@edu.hse.ru") or form.email.endswith("@hse.ru")):
        logging.warning("Invalid email address")
        raise HTTPException(status_code=400, detail="Invalid email address")

    # Generate verification code
    if settings.USE_FAKE_VERIFICATION_CODE:
        logging.info("Use fake verification code")
        verification_code = '0000'
    else:
        logging.info("Use actual verification code")
        verification_code = secrets.token_hex(16)

    # Add user data to registration_forms table
    registration_form = models.RegistrationForm(
        email=form.email,
        first_name=form.first_name,
        last_name=form.last_name,
        middle_name=form.middle_name,
        # TODO: store hashed passwords.
        password=form.password,
        verification_code=verification_code
    )

    existing_form = db.query(models.RegistrationForm).filter(models.RegistrationForm.email == registration_form.email)
    if existing_form.count() > 0:
        if existing_form.first().verified:
            logging.warning("Profile with this email already exists")
            raise HTTPException(
                status_code=400, detail="Profile with this email already exists")
        logging.info("Update registration form")
        existing_form.update({
            models.RegistrationForm.first_name: registration_form.first_name,
            models.RegistrationForm.last_name: registration_form.last_name,
            models.RegistrationForm.middle_name: registration_form.middle_name,
            models.RegistrationForm.password: registration_form.password,
            models.RegistrationForm.verified: False,
            models.RegistrationForm.verification_code: verification_code
        })
    else:
        logging.info("Add registration form")
        db.add(registration_form)

    if not settings.USE_FAKE_VERIFICATION_CODE:
        # Send verification email
        sender_email = settings.GMAIL_ADDRESS
        receiver_email = form.email
        password = settings.GMAIL_PASSWORD

        message = MIMEMultipart("alternative")
        message["Subject"] = "Verification Code"
        message["From"] = sender_email
        message["To"] = receiver_email

        text = f"Your verification code is: {verification_code}"

        # Add message to the email
        message.attach(MIMEText(text, "plain"))

        try:
            # Create a secure SSL context and send the email
            context = ssl.create_default_context()
            with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
                server.login(sender_email, password)
                server.sendmail(sender_email, receiver_email,
                                message.as_string())
        except:
            logging.error("Failed to send an email")
            raise HTTPException(
                status_code=400, detail="Failed to send an email")

    db.commit()
    logging.info({"response": "OK"})
    return Response(status_code=200)
