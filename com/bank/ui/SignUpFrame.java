package com.bank.ui;

import com.bank.factory.DAOFactory;
import com.bank.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Decoupled from data source through Factory Method pattern
 */
public class SignUpFrame extends JFrame {
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private Color secondaryColor = new Color(52, 73, 94);

    private final DAOFactory daoFactory;
    private final UserService userService;

    public SignUpFrame(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.userService = new UserService(daoFactory.getUserDAO());

        initComponents();
    }

    private void initComponents() {
        setTitle("Bank Management System - Sign Up");
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(accentColor);
        headerPanel.setPreferredSize(new Dimension(550, 100));
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField fullNameField = createTextField();
        JTextField usernameField = createTextField();
        JPasswordField passwordField = createPasswordField();
        JPasswordField confirmPasswordField = createPasswordField();
        JTextField emailField = createTextField();
        JTextField phoneField = createTextField();

        addFormField(formPanel, gbc, 0, "Full Name:", fullNameField);
        addFormField(formPanel, gbc, 1, "Username:", usernameField);
        addFormField(formPanel, gbc, 2, "Password:", passwordField);
        addFormField(formPanel, gbc, 3, "Confirm Password:", confirmPasswordField);
        addFormField(formPanel, gbc, 4, "Email:", emailField);
        addFormField(formPanel, gbc, 5, "Phone:", phoneField);

        // Buttons
        JButton signUpBtn = createStyledButton("Create Account", accentColor);
        JButton backBtn = createStyledButton("Back to Login", secondaryColor);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel, gbc);

        // Sign up action
        signUpBtn.addActionListener(e -> performSignUp(
                fullNameField.getText(),
                usernameField.getText(),
                new String(passwordField.getPassword()),
                new String(confirmPasswordField.getPassword()),
                emailField.getText(),
                phoneField.getText()
        ));

        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(daoFactory).setVisible(true);
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void performSignUp(String fullName, String username, String password,
                               String confirmPassword, String email, String phone) {
        // Validation
        if (fullName.trim().isEmpty() || username.trim().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all required fields (Name, Username, Password)!");
            return;
        }

        if (username.trim().length() < 4) {
            JOptionPane.showMessageDialog(this, "Username must be at least 4 characters!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        // Register user via service
        boolean success = userService.registerUser(username, password, fullName, email, phone);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now login with your credentials.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new LoginFrame(daoFactory).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username already exists! Please choose another.");
        }
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row,
                              String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
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

        btn.setPreferredSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}