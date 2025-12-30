package com.bank.factory;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.UserDAO;
import com.bank.dao.impl.MemoryAccountDAO;
import com.bank.dao.impl.MemoryTransactionDAO;
import com.bank.dao.MemoryUserDAO;
import com.bank.model.User;

/**
 * In-memory implementation of DAOFactory
 * Creates DAO instances that store data in HashMaps
 *
 * Data is stored in memory (RAM) only - not persisted to disk
 * Perfect for testing and development
 *
 * WARNING: All data is lost when application closes!
 */
public class MemoryDAOFactory extends DAOFactory {

    // Singleton DAO instances to share data across the application
    private static MemoryUserDAO userDAO;
    private static MemoryAccountDAO accountDAO;
    private static MemoryTransactionDAO transactionDAO;

    /**
     * Constructor - Initialize DAOs once
     */
    public MemoryDAOFactory() {
        if (userDAO == null) {
            userDAO = new MemoryUserDAO();
            accountDAO = new MemoryAccountDAO();
            transactionDAO = new MemoryTransactionDAO();
        }
    }

    @Override
    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    @Override
    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public void initialize() {
        System.out.println("\n========================================");
        System.out.println("Initializing MEMORY Data Source");
        System.out.println("========================================");

        System.out.println("→ Initializing in-memory data structures...");
        System.out.println("  ✓ Users HashMap initialized");
        System.out.println("  ✓ Accounts HashMap initialized");
        System.out.println("  ✓ Transactions HashMap initialized");

        // Create default admin user
        System.out.println("→ Creating default admin user...");
        createDefaultAdmin();

        System.out.println("========================================");
        System.out.println("✓ MEMORY initialization completed!");
        System.out.println("⚠ WARNING: Data stored in memory only!");
        System.out.println("⚠ All data will be lost when app closes!");
        System.out.println("========================================\n");
    }

    @Override
    public void cleanup() {
        System.out.println("✓ Memory storage cleanup - clearing all data");

        // Clear all data structures
        if (userDAO != null) {
            userDAO.clearAll();
        }
        if (accountDAO != null) {
            accountDAO.clearAll();
        }
        if (transactionDAO != null) {
            transactionDAO.clearAll();
        }
    }

    @Override
    public String getFactoryName() {
        return "Memory Factory (In-Memory HashMap)";
    }

    /**
     * Create default admin user in memory
     */
    private void createDefaultAdmin() {
        try {
            // Check if admin already exists
            if (userDAO.findByUsername("admin").isPresent()) {
                System.out.println("  ✓ Admin user already exists");
                return;
            }

            // Create admin user
            User admin = new User();
            admin.setUserId(1); // For memory storage, we set ID manually
            admin.setUsername("admin");
            admin.setFullName("System Administrator");
            admin.setUserType("ADMIN");

            if (userDAO.createUser(admin, "admin123")) {
                System.out.println("  ✓ Default admin user created (username: admin, password: admin123)");
            }
        } catch (Exception e) {
            System.err.println("  ! Warning: Could not create default admin: " + e.getMessage());
        }
    }
}