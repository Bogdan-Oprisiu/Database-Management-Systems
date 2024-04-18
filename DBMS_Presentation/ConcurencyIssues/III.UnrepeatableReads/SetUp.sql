-- SetUp script
CREATE TABLE account (
    id INT PRIMARY KEY,
    balance INT
);

INSERT INTO account (id, balance) VALUES (1, 1000);

SELECT * FROM account;
