CREATE TABLE _user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE operating_system (
    id SERIAL PRIMARY KEY,
    os_name VARCHAR(100) NOT NULL,
    os_version INT NOT NULL,
    os_version_name VARCHAR(100)
);

CREATE TABLE computer_manufacturer (
    id SERIAL PRIMARY KEY,
    manufacturer_name VARCHAR(100) NOT NULL,
    manufacturer_email VARCHAR(100) NOT NULL,
    manufacturer_phone INT NOT NULL
);

CREATE TABLE computer_category (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    category_description VARCHAR(255) NOT NULL
);

CREATE TABLE computer (
    id SERIAL PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    base_price INT NOT NULL,
    os_id INT REFERENCES operating_system(id),
    manufacturer_id INT REFERENCES computer_manufacturer(id),
    category_id INT REFERENCES computer_category(id),
    CONSTRAINT fk_os_id FOREIGN KEY (os_id) REFERENCES operating_system(id),
    CONSTRAINT fk_manufacturer_id FOREIGN KEY (manufacturer_id) REFERENCES computer_manufacturer(id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES computer_category(id)
);

CREATE TABLE _order (
    id SERIAL PRIMARY KEY,
    order_date DATE,
    delivery_address Address,
    user_id INT REFERENCES _user(id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES _user(id)
);

CREATE TABLE computer_order (
    id SERIAL PRIMARY KEY,
    computer_id INT,
    order_id INT,
    CONSTRAINT fk_computer_id FOREIGN KEY (computer_id) REFERENCES computer(id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES _order(id)
);
