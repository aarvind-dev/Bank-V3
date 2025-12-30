package com.bank.dao.impl;

import com.bank.dao.TransactionDAO;
import com.bank.factory.JdbcDAOFactory;
import com.bank.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDAO implements TransactionDAO {

    @Override
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_number, type, amount, balance_after, description, transaction_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, transaction.getAccountNumber());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setDouble(4, transaction.getBalanceAfter());
            stmt.setString(5, transaction.getDescription());
            stmt.setTimestamp(6, transaction.getTransactionDate());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC LIMIT ?";

        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction tx = new Transaction();
                    tx.setTransactionId(rs.getInt("transaction_id"));
                    tx.setAccountNumber(rs.getString("account_number"));
                    tx.setTransactionType(rs.getString("type"));
                    tx.setAmount(rs.getDouble("amount"));
                    tx.setBalanceAfter(rs.getDouble("balance_after"));
                    tx.setDescription(rs.getString("description"));
                    tx.setTransactionDate(rs.getTimestamp("transaction_date"));
                    transactions.add(tx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}