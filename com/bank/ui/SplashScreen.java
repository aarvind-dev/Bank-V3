package com.bank.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Splash screen shown during application initialization
 */
public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public SplashScreen() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(41, 128, 185));

        content.setOpaque(true);

        content.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 3));

        // Title
        JLabel titleLabel = new JLabel("🏦 Bank Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(30, 20, 20, 20));

        // Status
        statusLabel = new JLabel("Initializing...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(400, 25));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(41, 128, 185));
        centerPanel.setOpaque(true); // Ensure opaque

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        centerPanel.add(statusLabel, gbc);
        gbc.gridy = 1;
        centerPanel.add(progressBar, gbc);

        content.add(titleLabel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);

        setContentPane(content);
        setSize(500, 250);
        setLocationRelativeTo(null);
    }

    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    public void close() {
        setVisible(false);
        dispose();
    }
}