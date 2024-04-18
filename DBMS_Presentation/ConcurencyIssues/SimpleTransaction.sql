USE CONCURENCY_ISSUES;

-- Create the table
CREATE TABLE YourTable (
    ID INT PRIMARY KEY,
    Column1 VARCHAR(50)
);

INSERT INTO YourTable (ID, Column1) VALUES (1, 'Inserted Value');

SELECT * FROM YourTable;

-- Begin the transaction
BEGIN TRANSACTION;

-- Perform some operations within the transaction
UPDATE YourTable
SET Column1 = 'New Value';

-- Commit the transaction
COMMIT TRANSACTION;

SELECT * FROM YourTable;

-- Drop the table
DROP TABLE YourTable;
