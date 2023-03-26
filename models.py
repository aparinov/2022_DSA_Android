from datetime import datetime

from sqlalchemy import Column, Integer, String, Text, Boolean, DateTime, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship, backref

Base = declarative_base()


class RegistrationForm(Base):
    __tablename__ = 'registration_forms'
    email = Column(String, primary_key=True)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    middle_name = Column(String)
    password = Column(String, nullable=False)
    verified = Column(Boolean, nullable=False, default=False)
    verification_code = Column(String, nullable=False)


class ProfileInfo(Base):
    __tablename__ = 'profile_infos'
    email = Column(Text, primary_key=True)
    student = Column(Boolean, default=True)
    nickname = Column(Text)
    degree = Column(Text)
    faculty = Column(Text)
    program = Column(Text)
    year_of_study = Column(Integer)
    bio = Column(Text)


class Profile(Base):
    __tablename__ = 'profiles'
    email = Column(String, primary_key=True)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    middle_name = Column(String)
    password = Column(String, nullable=False)


class Project(Base):
    __tablename__ = 'projects'
    id = Column(Integer, primary_key=True)
    author_email = Column(String, ForeignKey('profiles.email'), nullable=False)
    title = Column(String, nullable=False, unique=True)
    title_lower = Column(String, nullable=False)
    description = Column(Text, nullable=False)
    max_number_of_students = Column(Integer, nullable=False)
    current_number_of_students = Column(Integer, nullable=False, default=0)
    recruiting_status = Column(String, nullable=False, default='NOT_STARTED')
    project_status = Column(String, nullable=False, default='NOT_STARTED')
    applications_deadline = Column(DateTime)
    planned_start_of_work = Column(DateTime)
    planned_finish_of_work = Column(DateTime)

    applications = relationship("Application", cascade="all, delete-orphan")
    participants = relationship("ProjectParticipant", cascade="all, delete-orphan")


class Application(Base):
    __tablename__ = 'applications'
    id = Column(Integer, primary_key=True)
    project_id = Column(Integer, ForeignKey('projects.id'), nullable=False)
    student_email = Column(String, ForeignKey('profiles.email'), nullable=False)
    created_at = Column(DateTime, nullable=False, default=datetime.utcnow())
    updated_at = Column(DateTime, nullable=False, default=datetime.utcnow())
    status = Column(String, default='CREATED')


class ProjectParticipant(Base):
    __tablename__ = 'project_participants'
    id = Column(Integer, primary_key=True)
    project_id = Column(Integer, ForeignKey('projects.id'), nullable=False)
    student_email = Column(String, ForeignKey('profiles.email'), nullable=False)
    applied_at = Column(DateTime, nullable=False, default=datetime.utcnow())


class Tag(Base):
    __tablename__ = 'tags'
    id = Column(Integer, primary_key=True)
    tag = Column(String, nullable=False, unique=True)


class ProjectTags(Base):
    __tablename__ = 'project_tags'
    project_id = Column(Integer, primary_key=True)
    tag_id = Column(Integer, primary_key=True)


class ProfileTag(Base):
    __tablename__ = 'profile_tags'
    email = Column(String, primary_key=True)
    tag_id = Column(Integer, primary_key=True)
