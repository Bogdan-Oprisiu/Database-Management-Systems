-- Create the operating_system table
CREATE TABLE operating_system
(
    id              SERIAL PRIMARY KEY,
    os_name         VARCHAR(100) NOT NULL,
    os_version      INT          NOT NULL,
    os_version_name VARCHAR(100)
);

-- Insert initial data into the operating_system table
INSERT INTO operating_system (os_name, os_version, os_version_name) VALUES
                                                                        ('Windows', 10, 'Windows 10'),
                                                                        ('macOS', 11, 'Big Sur'),
                                                                        ('Ubuntu', 20, 'Ubuntu 20.04 LTS');

-- Create the computer_manufacturer table
CREATE TABLE computer_manufacturer
(
    id                 SERIAL PRIMARY KEY,
    manufacturer_name  VARCHAR(100) NOT NULL,
    manufacturer_email VARCHAR(100) NOT NULL,
    manufacturer_phone INT          NOT NULL
);

-- Insert initial data into the computer_manufacturer table
INSERT INTO computer_manufacturer (manufacturer_name, manufacturer_email, manufacturer_phone) VALUES
                                                                                                  ('Dell', 'info@dell.com', 123456789),
                                                                                                  ('HP', 'info@hp.com', 987654321),
                                                                                                  ('Apple', 'info@apple.com', 999888777);

-- Create the computer_category table
CREATE TABLE computer_category
(
    id                   SERIAL PRIMARY KEY,
    category_name        VARCHAR(100) NOT NULL,
    category_description VARCHAR(255) NOT NULL
);

-- Insert initial data into the computer_category table
INSERT INTO computer_category (category_name, category_description) VALUES
                                                                        ('Laptop', 'Portable computers'),
                                                                        ('Desktop', 'Stationary computers'),
                                                                        ('Tablet', 'Handheld touch-screen devices');

-- Create the computer table
CREATE TABLE computer
(
    id              SERIAL PRIMARY KEY,
    model_name      VARCHAR(100) NOT NULL,
    base_price      INT          NOT NULL,
    os_id           INT REFERENCES operating_system (id),
    manufacturer_id INT REFERENCES computer_manufacturer (id),
    category_id     INT REFERENCES computer_category (id),
    CONSTRAINT fk_os_id FOREIGN KEY (os_id) REFERENCES operating_system (id),
    CONSTRAINT fk_manufacturer_id FOREIGN KEY (manufacturer_id) REFERENCES computer_manufacturer (id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES computer_category (id)
);

-- Insert initial data into the computer table
INSERT INTO computer (model_name, base_price, os_id, manufacturer_id, category_id) VALUES
                                                                                       ('Dell XPS 13', 1200, 1, 1, 1),
                                                                                       ('MacBook Pro', 2000, 2, 3, 1),
                                                                                       ('HP Pavilion', 800, 1, 2, 2);
