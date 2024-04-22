import hashlib

from fastapi import HTTPException
from sqlalchemy.orm import Session

from db.models import User
from authentication.schemas import UserAuthenticate, UserCreate


def register_user(user: UserCreate, db: Session):
    hashed_password = hashlib.sha256(user.password.encode()).hexdigest()
    db_user = User(email=user.email, first_name=user.first_name, last_name=user.last_name, password=hashed_password)
    db.add(db_user)
    db.commit()
    if db_user.email and db_user.first_name and db_user.last_name and db_user.password:
        return {"message": "User registered successfully"}
    else:
        raise HTTPException(status_code=401, detail="Invalid credentials")


def login_user(user: UserAuthenticate, db: Session):
    hashed_password = hashlib.sha256(user.password.encode()).hexdigest()
    db_user = db.query(User).filter(User.email == user.email, User.password == hashed_password).first()
    if db_user:
        return {"message": "Login successful"}
    else:
        raise HTTPException(status_code=401, detail="Invalid email or password")
