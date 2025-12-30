package com.bank.dao.impl;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileTransactionDAO implements TransactionDAO {
    private static final String FILE_PATH = "data/transactions.csv";

    public FileTransactionDAO() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public boolean createTransaction(Transaction t) {
        int nextId = (int) (System.currentTimeMillis() / 1000); // Simple ID generation
        t.setTransactionId(nextId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.format("%d,%s,%s,%.2f,%.2f,%s,%s",
                    t.getTransactionId(), t.getAccountNumber(), t.getTransactionType(),
                    t.getAmount(), t.getBalanceAfter(), t.getTransactionDate().toString(), t.getDescription());
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber, int limit) {
        List<Transaction> all = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[1].equals(accountNumber)) {
                    Transaction t = new Transaction();
                    t.setTransactionId(Integer.parseInt(parts[0]));
                    t.setAccountNumber(parts[1]);
                    t.setTransactionType(parts[2]);
                    t.setAmount(Double.parseDouble(parts[3]));
                    t.setBalanceAfter(Double.parseDouble(parts[4]));
                    t.setTransactionDate(Timestamp.valueOf(parts[5]));
                    t.setDescription(parts[6]);
                    all.add(t);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }

        Collections.reverse(all); // Show newest first
        return all.stream().limit(limit).collect(Collectors.toList());
    }
}