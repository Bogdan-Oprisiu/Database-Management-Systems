-- Transaction 2
BEGIN TRANSACTION;

-- Insert a new order with a quantity of 6
INSERT INTO orders (id, product_id, quantity) VALUES (3, 1, 6);

-- Commit the transaction
COMMIT;
