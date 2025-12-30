package com.bank;

import com.bank.factory.DAOFactory;
import com.bank.ui.LoginFrame;
import com.bank.ui.SplashScreen;

import javax.swing.*;

/**
 * using Factory Method pattern
 */
public class BankApplication {

    private static DAOFactory daoFactory;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);

            // Initialize in background thread
            new Thread(() -> {
                try {
                    splash.updateStatus("Initializing data layer...");
                    Thread.sleep(500);

                    // Get DAO Factory - EASY TO SWAP DATA SOURCES HERE!
                    // Just change DAOFactory.JDBC to DAOFactory.FILE or DAOFactory.MEMORY
                    daoFactory = DAOFactory.getDAOFactory(DAOFactory.JDBC);

                    splash.updateStatus("Setting up database...");
                    daoFactory.initialize();
                    Thread.sleep(500);

                    splash.updateStatus("Loading application...");
                    Thread.sleep(500);

                    // Close splash and show login
                    SwingUtilities.invokeLater(() -> {
                        splash.close();
                        new LoginFrame(daoFactory).setVisible(true);
                    });

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        splash.close();
                        JOptionPane.showMessageDialog(null,
                                "Failed to initialize application:\n" + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    });
                }
            }).start();
        });

        // Cleanup on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (daoFactory != null) {
                daoFactory.cleanup();
            }
        }));
    }

    public static DAOFactory getDAOFactory() {
        return daoFactory;
    }
}