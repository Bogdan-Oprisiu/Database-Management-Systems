-- Transaction 2
BEGIN TRANSACTION;

-- Read the balance
SELECT * FROM account WHERE id = 1;

-- Update the balance
UPDATE account SET balance = 1200 WHERE id = 1;
SELECT * FROM account WHERE id = 1;

-- Commit the transaction
COMMIT;
