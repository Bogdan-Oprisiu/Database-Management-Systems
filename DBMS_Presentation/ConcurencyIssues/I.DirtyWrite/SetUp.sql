USE CONCURENCY_ISSUES;

CREATE TABLE Person (
    PersonID INT PRIMARY KEY,
    FirstName VARCHAR(50),
    LastName VARCHAR(50)
);

INSERT INTO Person (PersonID, FirstName, LastName) VALUES
(1, 'Aaron', 'Hotchner'),
(2, 'Aaron', 'Reid'),
(3, 'Aaron', 'Morgan'),
(4, 'Emily', 'Prentiss');

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DROP TABLE Person;