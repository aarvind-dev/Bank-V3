package com.bank.dao;

import com.bank.model.User;
import java.util.Optional;

public interface UserDAO {
    boolean createUser(User user, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findById(int userId);
    Optional<User> authenticate(String username, String password);
    boolean updateUser(User user);
    boolean changePassword(int userId, String newPassword);
}