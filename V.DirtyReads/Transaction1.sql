-- Transaction 1
BEGIN TRANSACTION;

-- Read all employees
SELECT * FROM employees;

-- Introduce a delay
WAITFOR DELAY '00:00:05';

-- Read all employees
SELECT * FROM employees;

-- Commit the transaction
COMMIT;
