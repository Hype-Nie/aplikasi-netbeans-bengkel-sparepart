package com.mycompany.aplikasidekstopbengkeldansparepart.view;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

public class LoginView extends javax.swing.JFrame {

    public LoginView() {
        initComponents();
        customInit();
        getRootPane().setDefaultButton(loginButton);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Validasi", JOptionPane.WARNING_MESSAGE);
    }

    private void customInit() {
        // Apply UiTheme styling that can't be set in the designer
        brandingPanel.setBackground(new java.awt.Color(10, 87, 166));
        brandTitle.setForeground(java.awt.Color.WHITE);
        brandSubtitle.setForeground(new java.awt.Color(225, 239, 255));
        contentPanel.setBackground(java.awt.Color.WHITE);
        cardPanel.setBackground(java.awt.Color.WHITE);
        cardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)),
                javax.swing.BorderFactory.createEmptyBorder(30, 28, 30, 28)));
        formTitle.setForeground(new java.awt.Color(22, 28, 36));
        formSubTitle.setForeground(new java.awt.Color(94, 109, 124));
        demoInfo.setForeground(new java.awt.Color(94, 109, 124));

        usernameField.setFont(UiTheme.FONT_BODY);
        usernameField.setMargin(UiTheme.FIELD_INSETS);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        passwordField.setFont(UiTheme.FONT_BODY);
        passwordField.setMargin(UiTheme.FIELD_INSETS);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        loginButton.setFont(UiTheme.FONT_BODY);
        loginButton.setForeground(java.awt.Color.WHITE);
        loginButton.setBackground(UiTheme.PRIMARY);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        brandingPanel = new javax.swing.JPanel();
        brandTitle = new javax.swing.JLabel();
        brandSubtitle = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        formTitle = new javax.swing.JLabel();
        formSubTitle = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        demoInfo = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login Admin - Aplikasi Bengkel dan Sparepart");
        setMinimumSize(new java.awt.Dimension(820, 500));

        brandingPanel.setPreferredSize(new java.awt.Dimension(360, 500));
        brandingPanel.setLayout(new java.awt.GridBagLayout());

        brandTitle.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        brandTitle.setText("BengkelPro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        brandingPanel.add(brandTitle, gridBagConstraints);

        brandSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        brandSubtitle.setText("Aplikasi Desktop Bengkel & Sparepart");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        brandingPanel.add(brandSubtitle, gridBagConstraints);

        getContentPane().add(brandingPanel, java.awt.BorderLayout.WEST);

        contentPanel.setLayout(new java.awt.GridBagLayout());

        cardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        cardPanel.setPreferredSize(new java.awt.Dimension(380, 360));
        cardPanel.setLayout(new java.awt.GridBagLayout());

        formTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        formTitle.setText("Login Admin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        cardPanel.add(formTitle, gridBagConstraints);

        formSubTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        formSubTitle.setText("Masuk dengan akun admin dari database");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        cardPanel.add(formSubTitle, gridBagConstraints);

        usernameLabel.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        cardPanel.add(usernameLabel, gridBagConstraints);

        usernameField.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        cardPanel.add(usernameField, gridBagConstraints);

        passwordLabel.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        cardPanel.add(passwordLabel, gridBagConstraints);

        passwordField.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 18, 0);
        cardPanel.add(passwordField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        cardPanel.add(demoInfo, gridBagConstraints);

        loginButton.setText("Masuk ke Dashboard");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        cardPanel.add(loginButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contentPanel.add(cardPanel, gridBagConstraints);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel brandSubtitle;
    private javax.swing.JLabel brandTitle;
    private javax.swing.JPanel brandingPanel;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel demoInfo;
    private javax.swing.JLabel formSubTitle;
    private javax.swing.JLabel formTitle;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
