package com.bank.model;

import java.sql.Timestamp;

/**
 * Domain model representing a bank account
 */
public class Account {
    private String accountNumber;
    private int userId;
    private String name;
    private String email;
    private String phone;
    private double balance;
    private String accountType;
    private Timestamp createdDate;

    // Constructors
    public Account() {}

    public Account(String accountNumber, int userId, String name, String email,
                   String phone, double balance, String accountType, Timestamp createdDate) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.accountType = accountType;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}