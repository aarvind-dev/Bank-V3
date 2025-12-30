package com.bank.dao;

import com.bank.model.Account;
import java.util.Optional;
import java.util.List;

public interface AccountDAO {
    boolean createAccount(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(int userId);
    boolean updateBalance(String accountNumber, double newBalance);
}