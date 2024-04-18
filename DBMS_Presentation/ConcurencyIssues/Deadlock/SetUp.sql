USE CONCURENCY_ISSUES;

-- SetUp script
CREATE TABLE inventory (
    id INT PRIMARY KEY,
    product_name VARCHAR(50),
    quantity INT
);

-- Insert initial data
INSERT INTO inventory (id, product_name, quantity) VALUES (1, 'Product A', 10);
INSERT INTO inventory (id, product_name, quantity) VALUES (2, 'Product B', 20);
