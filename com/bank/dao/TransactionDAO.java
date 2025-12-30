package com.bank.dao;

import com.bank.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    boolean createTransaction(Transaction transaction);
    List<Transaction> findByAccountNumber(String accountNumber, int limit);
}