-- Transaction 2
BEGIN TRANSACTION;

-- Update Alice's salary
UPDATE employees SET salary = 100000 WHERE id = 2;

-- Commit the transaction
COMMIT;
