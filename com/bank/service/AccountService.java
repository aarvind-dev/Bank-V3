package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Transaction;

import java.util.List;
import java.util.Optional;

public class AccountService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public boolean createAccount(Account account) {
        if(accountDAO.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            return false;
        }
        return accountDAO.createAccount(account);
    }

    public Optional<Account> getAccount(String accountNumber) {
        return accountDAO.findByAccountNumber(accountNumber);
    }

    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) return false;

        Optional<Account> accountOpt = accountDAO.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            Account acc = accountOpt.get();
            double newBalance = acc.getBalance() + amount;

            // Update balance
            boolean updated = accountDAO.updateBalance(accountNumber, newBalance);

            if (updated) {
                // Log transaction
                Transaction tx = new Transaction(accountNumber, "DEPOSIT", amount, newBalance, "Cash Deposit");
                transactionDAO.createTransaction(tx);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) return false;

        Optional<Account> accountOpt = accountDAO.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            Account acc = accountOpt.get();
            if (acc.getBalance() >= amount) {
                double newBalance = acc.getBalance() - amount;

                if (accountDAO.updateBalance(accountNumber, newBalance)) {
                    Transaction tx = new Transaction(accountNumber, "WITHDRAW", amount, newBalance, "Cash Withdrawal");
                    transactionDAO.createTransaction(tx);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean transfer(String fromAccountNum, String toAccountNum, double amount) {
        if (amount <= 0 || fromAccountNum.equals(toAccountNum)) return false;

        Optional<Account> fromOpt = accountDAO.findByAccountNumber(fromAccountNum);
        Optional<Account> toOpt = accountDAO.findByAccountNumber(toAccountNum);

        if (fromOpt.isPresent() && toOpt.isPresent()) {
            Account from = fromOpt.get();
            Account to = toOpt.get();

            if (from.getBalance() >= amount) {
                // Deduct from sender
                double newFromBalance = from.getBalance() - amount;
                accountDAO.updateBalance(fromAccountNum, newFromBalance);
                transactionDAO.createTransaction(new Transaction(fromAccountNum, "TRANSFER_OUT", amount, newFromBalance, "Transfer to " + toAccountNum));

                // Add to receiver
                double newToBalance = to.getBalance() + amount;
                accountDAO.updateBalance(toAccountNum, newToBalance);
                transactionDAO.createTransaction(new Transaction(toAccountNum, "TRANSFER_IN", amount, newToBalance, "Transfer from " + fromAccountNum));

                return true;
            }
        }
        return false;
    }

    public List<Transaction> getTransactionHistory(String accountNumber, int limit) {
        return transactionDAO.findByAccountNumber(accountNumber, limit);
    }
}