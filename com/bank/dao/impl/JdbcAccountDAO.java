package com.bank.dao.impl;

import com.bank.dao.AccountDAO;
import com.bank.factory.JdbcDAOFactory;
import com.bank.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAccountDAO implements AccountDAO {

    @Override
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, user_id, name, email, phone, balance, account_type, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setInt(2, account.getUserId());
            stmt.setString(3, account.getName());
            stmt.setString(4, account.getEmail());
            stmt.setString(5, account.getPhone());
            stmt.setDouble(6, account.getBalance());
            stmt.setString(7, account.getAccountType());
            stmt.setTimestamp(8, account.getCreatedDate() != null ? account.getCreatedDate() : new java.sql.Timestamp(System.currentTimeMillis()));

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = JdbcDAOFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws Exception {
        Account account = new Account();
        account.setAccountNumber(rs.getString("account_number"));
        account.setUserId(rs.getInt("user_id"));
        account.setName(rs.getString("name"));
        account.setEmail(rs.getString("email"));
        account.setPhone(rs.getString("phone"));
        account.setBalance(rs.getDouble("balance"));
        account.setAccountType(rs.getString("account_type"));
        account.setCreatedDate(rs.getTimestamp("created_date"));
        return account;
    }
}