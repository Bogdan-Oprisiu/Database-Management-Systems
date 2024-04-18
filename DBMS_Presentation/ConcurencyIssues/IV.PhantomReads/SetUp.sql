-- SetUp script
CREATE TABLE orders (
    id INT PRIMARY KEY,
    product_id INT,
    quantity INT
);

-- Insert initial data
INSERT INTO orders (id, product_id, quantity) VALUES (1, 1, 5);
INSERT INTO orders (id, product_id, quantity) VALUES (2, 2, 3);
