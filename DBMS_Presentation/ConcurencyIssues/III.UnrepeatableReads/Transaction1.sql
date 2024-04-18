-- Transaction 1
BEGIN TRANSACTION;

-- Read the balance
SELECT * FROM account WHERE id = 1;

-- Update the balance with a delay
UPDATE account SET balance = 800 WHERE id = 1;
SELECT * FROM account WHERE id = 1;

-- Introduce a delay
WAITFOR DELAY '00:00:05';

-- Commit the transaction
COMMIT;
