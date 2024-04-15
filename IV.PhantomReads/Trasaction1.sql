-- Transaction 1
BEGIN TRANSACTION;

-- Read all orders with a quantity greater than or equal to 4
SELECT * FROM orders WHERE quantity >= 4;

-- Introduce a delay
WAITFOR DELAY '00:00:05';

-- Read all orders with a quantity greater than or equal to 4 again
SELECT * FROM orders WHERE quantity >= 4;

-- Commit the transaction
COMMIT;
