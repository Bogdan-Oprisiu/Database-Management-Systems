from pydantic import BaseModel


class UserCreate(BaseModel):
    email: str
    first_name: str
    last_name: str
    password: str


class UserAuthenticate(BaseModel):
    email: str
    password: str
