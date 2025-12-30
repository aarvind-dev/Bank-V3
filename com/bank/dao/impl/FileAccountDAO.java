package com.bank.dao.impl;

import com.bank.dao.AccountDAO;
import com.bank.model.Account;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileAccountDAO implements AccountDAO {
    private static final String FILE_PATH = "data/accounts.csv";

    public FileAccountDAO() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createAccount(Account account) {
        if (findByAccountNumber(account.getAccountNumber()).isPresent()) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.format("%s,%d,%s,%s,%s,%.2f,%s,%s",
                    account.getAccountNumber(),
                    account.getUserId(),
                    account.getName(),
                    account.getEmail(),
                    account.getPhone(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getCreatedDate() != null ? account.getCreatedDate().toString() : new Timestamp(System.currentTimeMillis()).toString()
            );
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        List<Account> allAccounts = readAllAccounts();
        return allAccounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    @Override
    public List<Account> findByUserId(int userId) {
        List<Account> allAccounts = readAllAccounts();
        return allAccounts.stream()
                .filter(a -> a.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateBalance(String accountNumber, double newBalance) {
        List<Account> allAccounts = readAllAccounts();
        boolean found = false;

        for (Account acc : allAccounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                acc.setBalance(newBalance);
                found = true;
                break;
            }
        }

        if (found) {
            return writeAllAccounts(allAccounts);
        }
        return false;
    }

    private List<Account> readAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Account acc = new Account();
                    acc.setAccountNumber(parts[0]);
                    acc.setUserId(Integer.parseInt(parts[1]));
                    acc.setName(parts[2]);
                    acc.setEmail(parts[3]);
                    acc.setPhone(parts[4]);
                    acc.setBalance(Double.parseDouble(parts[5]));
                    acc.setAccountType(parts[6]);
                    try {
                        acc.setCreatedDate(Timestamp.valueOf(parts[7]));
                    } catch (Exception e) {
                        acc.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    }
                    accounts.add(acc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    private boolean writeAllAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Account account : accounts) {
                String line = String.format("%s,%d,%s,%s,%s,%.2f,%s,%s",
                        account.getAccountNumber(),
                        account.getUserId(),
                        account.getName(),
                        account.getEmail(),
                        account.getPhone(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getCreatedDate().toString()
                );
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}