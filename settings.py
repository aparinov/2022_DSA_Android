from functools import lru_cache

from pydantic import (
    BaseSettings
)


class Settings(BaseSettings):
    DATABASE_URL: str
    GMAIL_ADDRESS: str
    GMAIL_PASSWORD: str
    USE_FAKE_VERIFICATION_CODE: bool

    class Config:
        env_file = ".env"


@lru_cache()
def get_settings():
    return Settings()
