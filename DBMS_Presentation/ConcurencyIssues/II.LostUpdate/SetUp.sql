USE CONCURENCY_ISSUES;

CREATE TABLE BankAccounts(
    AccountId		INT IDENTITY(1,1),
    BalanceAmount   INT
);
 
insert into BankAccounts (
    BalanceAmount
)
SELECT 1500;

SELECT * FROM BankAccounts

DROP TABLE BankAccounts ;