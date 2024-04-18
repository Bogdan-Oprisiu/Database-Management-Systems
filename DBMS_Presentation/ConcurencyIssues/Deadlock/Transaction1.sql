USE CONCURENCY_ISSUES;

-- Transaction 1
BEGIN TRANSACTION;

-- Update product A
UPDATE inventory SET quantity = quantity - 5 WHERE id = 1;
WAITFOR DELAY '00:00:05'; -- Introduce delay to simulate work

-- Update product B
UPDATE inventory SET quantity = quantity + 5 WHERE id = 2;

COMMIT;
