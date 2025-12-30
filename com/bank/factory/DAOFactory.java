package com.bank.factory;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.UserDAO;

public abstract class DAOFactory {

    // Factory type constants
    public static final int JDBC = 1;
    public static final int FILE = 2;
    public static final int MEMORY = 3;


    public static DAOFactory getDAOFactory(int factoryType) {
        switch (factoryType) {
            case JDBC:
                return new JdbcDAOFactory();
            case FILE:
                return new FileDAOFactory();
            case MEMORY:
                return new MemoryDAOFactory();
            default:
                throw new IllegalArgumentException("Invalid factory type: " + factoryType);
        }
    }

    public abstract AccountDAO getAccountDAO();

    public abstract TransactionDAO getTransactionDAO();

    public abstract UserDAO getUserDAO();

    public abstract void initialize();

    public abstract void cleanup();

    public abstract String getFactoryName();
}