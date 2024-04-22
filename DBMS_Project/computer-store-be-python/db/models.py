from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class Address:
    def __init__(self, number, street, city, state, postal_code):
        self.number = number
        self.street = street
        self.city = city
        self.state = state
        self.postal_code = postal_code


class User(Base):
    __tablename__ = "_user"

    id = Column(Integer, primary_key=True)
    email = Column(String, unique=True, index=True)
    first_name = Column(String)
    last_name = Column(String)
    password = Column(String)