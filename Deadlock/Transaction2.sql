USE CONCURENCY_ISSUES;

-- Transaction 2
BEGIN TRANSACTION;

-- Update product B
UPDATE inventory SET quantity = quantity - 3 WHERE id = 2;
WAITFOR DELAY '00:00:05'; -- Introduce delay to simulate work

-- Update product A
UPDATE inventory SET quantity = quantity + 3 WHERE id = 1;

COMMIT;
