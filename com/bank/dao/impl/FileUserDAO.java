package com.bank.dao.impl;

import com.bank.dao.UserDAO;
import com.bank.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserDAO implements UserDAO {
    private static final String FILE_PATH = "data/users.csv";

    public FileUserDAO() {
        checkFileExists();
    }

    private void checkFileExists() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Create default admin
                createUser(new User(0, "admin", "System Admin", "admin@bank.com", "0000000000", "ADMIN"), "admin123");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createUser(User user, String password) {
        if (findByUsername(user.getUsername()).isPresent()) return false;

        List<User> users = readAllUsers();
        int newId = users.isEmpty() ? 1 : users.get(users.size() - 1).getUserId() + 1;
        user.setUserId(newId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Format: id,username,password,fullname,email,phone,type
            String line = String.format("%d,%s,%s,%s,%s,%s,%s",
                    user.getUserId(), user.getUsername(), password,
                    user.getFullName(), user.getEmail(), user.getPhone(), user.getUserType());
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return readAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public Optional<User> findById(int userId) {
        return readAllUsers().stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst();
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    if (parts[1].equalsIgnoreCase(username) && parts[2].equals(password)) {
                        return Optional.of(new User(
                                Integer.parseInt(parts[0]), parts[1], parts[3], parts[4], parts[5], parts[6]
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUser(User user) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && Integer.parseInt(parts[0]) == user.getUserId()) {
                    // Update this line, keep password (parts[2])
                    line = String.format("%d,%s,%s,%s,%s,%s,%s",
                            user.getUserId(), user.getUsername(), parts[2],
                            user.getFullName(), user.getEmail(), user.getPhone(), user.getUserType());
                    found = true;
                }
                lines.add(line);
            }
        } catch (IOException e) { return false; }

        if (found) rewriteFile(lines);
        return found;
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && Integer.parseInt(parts[0]) == userId) {
                    // Update password part
                    parts[2] = newPassword;
                    line = String.join(",", parts);
                    found = true;
                }
                lines.add(line);
            }
        } catch (IOException e) { return false; }

        if (found) rewriteFile(lines);
        return found;
    }

    private List<User> readAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    users.add(new User(
                            Integer.parseInt(parts[0]), parts[1], parts[3], parts[4], parts[5], parts[6]
                    ));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    private void rewriteFile(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}