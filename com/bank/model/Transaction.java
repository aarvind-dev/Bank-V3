package com.bank.model;

import java.sql.Timestamp;

/**
 * Domain model representing a financial transaction
 */
public class Transaction {
    private int transactionId;
    private String accountNumber;
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    private double amount;
    private double balanceAfter;
    private Timestamp transactionDate;
    private String description;

    public Transaction() {}

    public Transaction(String accountNumber, String transactionType, double amount, double balanceAfter, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.transactionDate = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}