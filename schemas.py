from typing import List, Optional
from pydantic import BaseModel


# Request body schema for POST /register.
class RegisterRequest(BaseModel):
    email: str
    first_name: str
    last_name: str
    middle_name: Optional[str]
    password: str


# Request body schema for POST /verify-email.
class VerifyEmailRequest(BaseModel):
    email: str
    verification_code: str


# Request body schema for POST /login.
class LoginRequest(BaseModel):
    email: str
    password: str


# Response body schema for GET /users?email=<email>.
class UserInfoResponse(BaseModel):
    is_student: bool
    first_name: str
    last_name: str
    middle_name: Optional[str]
    # By default, contacts = email
    contacts: str
    nickname: Optional[str]
    degree: Optional[str]
    faculty: Optional[str]
    program: Optional[str]
    year_of_study: Optional[int]
    bio: Optional[str]
    tags: List[str]


# Request body schema for POST /users?email=<email>.
class EditUserInfoRequest(BaseModel):
    first_name: Optional[str]
    last_name: Optional[str]
    middle_name: Optional[str]
    contacts: Optional[str]
    nickname: Optional[str]
    degree: Optional[str]
    faculty: Optional[str]
    program: Optional[str]
    year_of_study: Optional[int]
    bio: Optional[str]
    tags: Optional[List[str]]


class SearchProjectsRequest(BaseModel):
    search_string: Optional[str]
    project_status: Optional[str]
    recruiting_status: Optional[str]


class ProjectInfo(BaseModel):
    id: int
    title: str
    description: str
    status_string: str
    tags: List[str]


class SearchProjectsResponse(BaseModel):
    projects: List[ProjectInfo] = []


class CreateProjectRequest(BaseModel):
    author_email: str
    title: str
    description: str
    max_number_of_students: int
    recruiting_status: Optional[str]
    project_status: Optional[str]
    applications_deadline: Optional[str]
    planned_start_of_work: Optional[str]
    planned_finish_of_work: Optional[str]
    tags: Optional[List[str]]


class GetProjectResponse(BaseModel):
    id: int
    author: str
    author_email: str
    title: str
    description: str
    max_number_of_students: int
    current_number_of_students: int
    recruiting_status: str
    project_status: str
    applications_deadline: Optional[str]
    planned_start_of_work: Optional[str]
    planned_finish_of_work: Optional[str]
    tags: List[str]


class PutProjectRequest(BaseModel):
    id: int
    title: Optional[str]
    description: Optional[str]
    max_number_of_students: Optional[int]
    recruiting_status: Optional[str]
    project_status: Optional[str]
    applications_deadline: Optional[str]
    planned_start_of_work: Optional[str]
    planned_finish_of_work: Optional[str]
    tags: Optional[List[str]]


class GetTagsResponse(BaseModel):
    tags: List[str]


class GetRecommendationsResponse(BaseModel):
    projects: List[ProjectInfo] = []


class CreateApplicationRequest(BaseModel):
    email: str
    project_id: int


class CreateApplicationResponse(BaseModel):
    application_id: int


class DecideApplicationRequest(BaseModel):
    application_id: int
    decision: str
