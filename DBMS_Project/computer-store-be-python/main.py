import hashlib

from fastapi import FastAPI, HTTPException, Depends
from sqlalchemy.orm import Session

from database import get_db
from models import User
from schemas import UserCreate, UserAuthenticate

app = FastAPI()


@app.post("/register/")
async def register_user(user: UserCreate, db: Session = Depends(get_db)):
    hashed_password = hashlib.sha256(user.password.encode()).hexdigest()
    db_user = User(email=user.email, first_name=user.first_name, last_name=user.last_name, password=hashed_password)
    db.add(db_user)
    db.commit()
    return {"message": "User registered successfully"}


@app.post("/login/")
async def login_user(user: UserAuthenticate, db: Session = Depends(get_db)):
    hashed_password = hashlib.sha256(user.password.encode()).hexdigest()
    db_user = db.query(User).filter(User.email == user.email, User.password == hashed_password).first()
    if db_user:
        return {"message": "Login successful"}
    else:
        raise HTTPException(status_code=401, detail="Invalid email or password")
