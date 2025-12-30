package com.bank.factory;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.UserDAO;
import com.bank.dao.impl.FileAccountDAO;
import com.bank.dao.impl.FileTransactionDAO;
import com.bank.dao.impl.FileUserDAO;

import java.io.File;

public class FileDAOFactory extends DAOFactory {

    private static final String DATA_DIR = "data";

    @Override
    public UserDAO getUserDAO() {
        return new FileUserDAO();
    }

    @Override
    public AccountDAO getAccountDAO() {
        return new FileAccountDAO();
    }

    @Override
    public TransactionDAO getTransactionDAO() {
        return new FileTransactionDAO();
    }

    @Override
    public void initialize() {
        // Create data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("Created data directory: " + dir.getAbsolutePath());
            }
        }
    }

    @Override
    public void cleanup() {
        System.out.println("File Data Source cleanup completed.");
    }

    @Override
    public String getFactoryName() {
        return "File System Factory (CSV)";
    }
}