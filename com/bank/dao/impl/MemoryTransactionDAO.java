package com.bank.dao.impl;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemoryTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    @Override
    public boolean createTransaction(Transaction transaction) {
        transaction.setTransactionId(idGen.getAndIncrement());
        transactions.add(transaction);
        return true;
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber, int limit) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate())) // Newest first
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void clearAll() { transactions.clear(); }
}