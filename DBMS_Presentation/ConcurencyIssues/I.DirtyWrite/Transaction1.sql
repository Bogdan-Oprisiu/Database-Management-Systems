SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

SELECT 
    COUNT(DISTINCT LastName) DistinctLastNameBeforeBeginTran
FROM Person
WHERE FirstName = 'Aaron';

BEGIN TRANSACTION;
 
UPDATE Person
SET LastName = 'Hotchner'
WHERE FirstName = 'Aaron';
 
SELECT 
    COUNT(DISTINCT LastName) DistinctLastNameInTransaction
FROM Person
WHERE FirstName = 'Aaron';
 
WAITFOR DELAY '00:00:05.000';
 
ROLLBACK TRANSACTION;
 
SELECT 
    COUNT(DISTINCT LastName) DistinctLastNameAfterRollback
FROM Person
WHERE FirstName = 'Aaron';