package com.bank.ui;

import com.bank.factory.DAOFactory;
import com.bank.model.User;
import com.bank.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

/**
 * Uses services which are created via Factory Method pattern
 */
public class LoginFrame extends JFrame {
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private Color secondaryColor = new Color(52, 73, 94);

    private final DAOFactory daoFactory;
    private final UserService userService;

    public LoginFrame(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.userService = new UserService(daoFactory.getUserDAO());

        initComponents();
    }

    private void initComponents() {
        setTitle("Bank Management System - Login");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(500, 120));
        JLabel titleLabel = new JLabel("🏦 Bank System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Login Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Login to Your Account");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTitle.setForeground(secondaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(loginTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 35));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 35));
        formPanel.add(passwordField, gbc);

        // Buttons
        JButton loginBtn = createStyledButton("Login", primaryColor);
        JButton signupBtn = createStyledButton("Sign Up", accentColor);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        formPanel.add(buttonPanel, gbc);

        // Info label
        JLabel infoLabel = new JLabel("<html><center>New user? Click Sign Up to create account<br>Default Admin: admin / admin123</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(infoLabel, gbc);

        // Login action
        loginBtn.addActionListener(e -> performLogin(usernameField.getText(),
                new String(passwordField.getPassword())));

        // Sign up action
        signupBtn.addActionListener(e -> {
            dispose();
            new SignUpFrame(daoFactory).setVisible(true);
        });

        // Enter key to login
        passwordField.addActionListener(e -> loginBtn.doClick());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void performLogin(String username, String password) {
        if (username.trim().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        Optional<User> userOpt = userService.authenticate(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + user.getFullName() + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new DashboardFrame(daoFactory, user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password!",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        try {
            btn.putClientProperty("JButton.buttonType", "square");
        } catch (Exception ignored) {}

        btn.setPreferredSize(new Dimension(150, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}