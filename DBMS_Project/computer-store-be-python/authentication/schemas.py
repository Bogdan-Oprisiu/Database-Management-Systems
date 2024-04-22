from pydantic import BaseModel


class UserCreate(BaseModel):
    email: str
    first_name: str
    last_name: str
    password: str


class UserAuthenticate(BaseModel):
    email: str
    password: str


class OperatingSystem(BaseModel):
    os_name: str
    os_version: int
    os_version_name: str


class ComputerManufacturer(BaseModel):
    manufacturer_name: str
    manufacturer_email: str
    manufacturer_phone: int


class ComputerCategory(BaseModel):
    category_name: str
    category_description: str


class Computer(BaseModel):
    model_name: str
    base_price: int
    os_id: int
    manufacturer_id: int
    category_id: int
