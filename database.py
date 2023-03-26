from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import settings

engine = create_engine(settings.get_settings().DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


# Dependency to get a database session
def get_db():
    try:
        db = SessionLocal()
        yield db
    finally:
        db.close()
