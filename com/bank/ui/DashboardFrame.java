package com.bank.ui;

import com.bank.factory.DAOFactory;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.service.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

/**
 * Uses services created through Factory Method pattern
 */
public class DashboardFrame extends JFrame {
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 73, 94);
    private Color accentColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);

    private final DAOFactory daoFactory;
    private final User currentUser;
    private final AccountService accountService;

    public DashboardFrame(DAOFactory daoFactory, User user) {
        this.daoFactory = daoFactory;
        this.currentUser = user;

        // Create service using factory - NO direct database dependency!
        this.accountService = new AccountService(
                daoFactory.getAccountDAO(),
                daoFactory.getTransactionDAO()
        );

        initComponents();
    }

    private void initComponents() {
        setTitle("Bank Management System - " + currentUser.getFullName());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Center with operation buttons
        mainPanel.add(createOperationsPanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(1000, 80));

        JLabel titleLabel = new JLabel("🏦 Bank Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(primaryColor);
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullName() + " ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(dangerColor);
        logoutBtn.setForeground(Color.WHITE);

        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);


        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(daoFactory).setVisible(true);
        });

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createOperationsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        String[] operations = {"Create Account", "Deposit", "Withdraw",
                "Transfer", "View Account", "History"};
        String[] icons = {"➕", "💰", "💸", "🔄", "👁", "📊"};
        Color[] colors = {accentColor, primaryColor, dangerColor,
                new Color(155, 89, 182), new Color(230, 126, 34), secondaryColor};

        int row = 0, col = 0;
        for (int i = 0; i < operations.length; i++) {
            JButton btn = createOperationButton(operations[i], icons[i], colors[i], i);
            gbc.gridx = col;
            gbc.gridy = row;
            panel.add(btn, gbc);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        return panel;
    }

    private JButton createOperationButton(String text, String icon, Color color, int operation) {

        JButton btn = new JButton("<html><center><font size='5'>" + icon + "</font><br>" + text + "</center></html>");

        btn.setPreferredSize(new Dimension(200, 100));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);

        try {
            btn.putClientProperty("JButton.buttonType", "square");
        } catch (Exception ignored) {}
        // ---------------------------------

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(darkenColor(color));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        btn.addActionListener(e -> handleOperation(operation));

        return btn;
    }

    private Color darkenColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], Math.max(0f, hsb[2] - 0.1f));
    }

    private void handleOperation(int operation) {
        switch (operation) {
            case 0: showCreateAccount(); break;
            case 1: showDeposit(); break;
            case 2: showWithdraw(); break;
            case 3: showTransfer(); break;
            case 4: showViewAccount(); break;
            case 5: showHistory(); break;
        }
    }

    private void showCreateAccount() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField(currentUser.getFullName());
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField depositField = new JTextField("0");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Savings", "Current", "Fixed Deposit"});

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Initial Deposit:"));
        panel.add(depositField);
        panel.add(new JLabel("Account Type:"));
        panel.add(typeBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Account",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String accountNumber = "ACC" + System.currentTimeMillis();

                Account account = new Account();
                account.setAccountNumber(accountNumber);
                account.setUserId(currentUser.getUserId());
                account.setName(nameField.getText());
                account.setEmail(emailField.getText());
                account.setPhone(phoneField.getText());
                account.setBalance(Double.parseDouble(depositField.getText()));
                account.setAccountType((String) typeBox.getSelectedItem());

                if (accountService.createAccount(account)) {
                    JOptionPane.showMessageDialog(this,
                            "Account created!\nAccount Number: " + accountNumber,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create account!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void showDeposit() {
        String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (accountNumber == null || accountNumber.trim().isEmpty()) return;

        String amountStr = JOptionPane.showInputDialog(this, "Enter Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(amountStr);
            if (accountService.deposit(accountNumber, amount)) {
                Optional<Account> account = accountService.getAccount(accountNumber);
                JOptionPane.showMessageDialog(this,
                        "Deposit successful!\nNew Balance: ₹" +
                                String.format("%.2f", account.get().getBalance()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showWithdraw() {
        String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (accountNumber == null || accountNumber.trim().isEmpty()) return;

        String amountStr = JOptionPane.showInputDialog(this, "Enter Amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(amountStr);
            if (accountService.withdraw(accountNumber, amount)) {
                Optional<Account> account = accountService.getAccount(accountNumber);
                JOptionPane.showMessageDialog(this,
                        "Withdrawal successful!\nNew Balance: ₹" +
                                String.format("%.2f", account.get().getBalance()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal failed! (Insufficient balance or account not found)");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showTransfer() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JTextField amountField = new JTextField();

        panel.add(new JLabel("From Account:"));
        panel.add(fromField);
        panel.add(new JLabel("To Account:"));
        panel.add(toField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer Money",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (accountService.transfer(fromField.getText(), toField.getText(), amount)) {
                    JOptionPane.showMessageDialog(this,
                            "Transfer successful!\nAmount: ₹" + String.format("%.2f", amount),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Transfer failed!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void showViewAccount() {
        String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (accountNumber == null || accountNumber.trim().isEmpty()) return;

        Optional<Account> accountOpt = accountService.getAccount(accountNumber);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            String details = String.format(
                    "Account Number: %s\n" +
                            "Name: %s\n" +
                            "Email: %s\n" +
                            "Phone: %s\n" +
                            "Type: %s\n" +
                            "Balance: ₹%.2f",
                    account.getAccountNumber(),
                    account.getName(),
                    account.getEmail(),
                    account.getPhone(),
                    account.getAccountType(),
                    account.getBalance()
            );
            JOptionPane.showMessageDialog(this, details, "Account Details",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Account not found!");
        }
    }

    private void showHistory() {
        String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (accountNumber == null || accountNumber.trim().isEmpty()) return;

        List<Transaction> transactions = accountService.getTransactionHistory(accountNumber, 20);

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found!");
            return;
        }

        StringBuilder history = new StringBuilder();
        history.append(String.format("%-15s %-15s %-12s %-12s\n",
                "Type", "Amount", "Balance", "Date"));
        history.append("─".repeat(60)).append("\n");

        for (Transaction t : transactions) {
            history.append(String.format("%-15s ₹%-14.2f ₹%-11.2f %s\n",
                    t.getTransactionType(),
                    t.getAmount(),
                    t.getBalanceAfter(),
                    t.getTransactionDate()
            ));
        }

        JTextArea textArea = new JTextArea(history.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Transaction History",
                JOptionPane.INFORMATION_MESSAGE);
    }
}