package com.bank.dao;

import com.bank.model.User;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryUserDAO implements UserDAO {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, String> credentials = new HashMap<>(); // username -> password
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public boolean createUser(User user, String password) {
        if (findByUsername(user.getUsername()).isPresent()) return false;

        int id = idGenerator.getAndIncrement();
        user.setUserId(id);
        users.put(id, user);
        credentials.put(user.getUsername(), password);
        return true;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (credentials.containsKey(username) && credentials.get(username).equals(password)) {
            return findByUsername(username);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUser(User user) {
        if (users.containsKey(user.getUserId())) {
            users.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {
        Optional<User> user = findById(userId);
        if (user.isPresent()) {
            credentials.put(user.get().getUsername(), newPassword);
            return true;
        }
        return false;
    }

    public void clearAll() {
        users.clear();
        credentials.clear();
    }
}