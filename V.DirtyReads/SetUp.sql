-- SetUp script
CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    salary INT
);

-- Insert initial data
INSERT INTO employees (id, name, salary) VALUES (1, 'John', 50000);
INSERT INTO employees (id, name, salary) VALUES (2, 'Alice', 60000);
