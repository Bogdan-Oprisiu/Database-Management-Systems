USE CONCURENCY_ISSUES;

CREATE TABLE Unrepeatable_Reads (
	id INT PRIMARY KEY,
	field INT
);

INSERT INTO Unrepeatable_Reads (id, field) VALUES
(1, 1),
(2, 69);

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

SELECT * FROM Unrepeatable_Reads;

DROP TABLE Unrepeatable_Reads;