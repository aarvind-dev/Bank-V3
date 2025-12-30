package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;

import java.util.Optional;

/**
 * Service layer for user operations
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Register a new user
     */
    public boolean registerUser(String username, String password, String fullName,
                                String email, String phone) {
        // Validation
        if (username == null || username.trim().length() < 4) {
            return false;
        }

        if (password == null || password.length() < 6) {
            return false;
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        // Check if username already exists
        if (userDAO.findByUsername(username).isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserType("CUSTOMER");

        return userDAO.createUser(user, password);
    }

    /**
     * Authenticate a user
     */
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        return userDAO.authenticate(username, password);
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(int userId) {
        return userDAO.findById(userId);
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    /**
     * Change user password
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return false;
        }

        Optional<User> userOpt = userDAO.findById(userId);
        if (!userOpt.isPresent()) {
            return false;
        }

        User user = userOpt.get();

        // Verify old password
        if (userDAO.authenticate(user.getUsername(), oldPassword).isPresent()) {
            return userDAO.changePassword(userId, newPassword);
        }

        return false;
    }
}