package com.bank.dao.impl;

import com.bank.dao.AccountDAO;
import com.bank.model.Account;
import java.util.*;
import java.util.stream.Collectors;

public class MemoryAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public boolean createAccount(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) return false;
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public List<Account> findByUserId(int userId) {
        return accounts.values().stream()
                .filter(a -> a.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateBalance(String accountNumber, double newBalance) {
        if (accounts.containsKey(accountNumber)) {
            accounts.get(accountNumber).setBalance(newBalance);
            return true;
        }
        return false;
    }

    public void clearAll() { accounts.clear(); }
}