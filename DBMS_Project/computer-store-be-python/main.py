from fastapi import FastAPI, Depends

from authentication import schemas
from authentication.authentication_controller import *
from db import models
from db.database import get_db

app = FastAPI()


@app.post("/register/")
async def register_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    await register_user(user, db)


@app.post("/login/")
async def login_user(user: schemas.UserAuthenticate, db: Session = Depends(get_db)):
    await login_user(user, db)


@app.post("/operating_system/add")
def create_operating_system(os: schemas.OperatingSystem, db: Session = Depends(get_db)):
    db_os = models.OperatingSystem(**os.dict())
    db.add(db_os)
    db.commit()
    db.refresh(db_os)
    return db_os


@app.post("/computer_manufacturer/")
def create_computer_manufacturer(manufacturer: schemas.ComputerManufacturer, db: Session = Depends(get_db)):
    db_manufacturer = models.ComputerManufacturer(**manufacturer.dict())
    db.add(db_manufacturer)
    db.commit()
    db.refresh(db_manufacturer)
    return db_manufacturer


@app.post("/computer_category/")
def create_computer_category(category: schemas.ComputerCategory, db: Session = Depends(get_db)):
    db_category = models.ComputerCategory(**category.dict())
    db.add(db_category)
    db.commit()
    db.refresh(db_category)
    return db_category


@app.post("/computer/")
def create_computer(computer: schemas.Computer, db: Session = Depends(get_db)):
    db_computer = models.Computer(**computer.dict())
    db.add(db_computer)
    db.commit()
    db.refresh(db_computer)
    return db_computer
