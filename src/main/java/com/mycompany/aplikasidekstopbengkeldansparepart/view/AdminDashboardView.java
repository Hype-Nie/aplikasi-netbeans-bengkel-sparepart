package com.mycompany.aplikasidekstopbengkeldansparepart.view;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.CustomerPanelView;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.DashboardPanelView;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.PurchaseTransactionPanelView;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.ServiceTransactionPanelView;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.SparepartPanelView;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.SupplierPanelView;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;

public class AdminDashboardView extends javax.swing.JFrame {

    private final java.util.Map<String, javax.swing.JButton> navButtons = new java.util.LinkedHashMap<>();

    // Panel instances created by this view, exposed via getters for controllers
    private final DashboardPanelView dashboardPanel = new DashboardPanelView();
    private final CustomerPanelView customerPanel = new CustomerPanelView();
    private final SparepartPanelView sparepartPanel = new SparepartPanelView();
    private final SupplierPanelView supplierPanel = new SupplierPanelView();
    private final ServiceTransactionPanelView servicePanel = new ServiceTransactionPanelView();
    private final PurchaseTransactionPanelView purchasePanel = new PurchaseTransactionPanelView();

    private static final String KEY_DASHBOARD = "Dashboard";
    private static final String KEY_PELANGGAN = "Pelanggan";
    private static final String KEY_SPAREPART = "Sparepart";
    private static final String KEY_SUPPLIER = "Supplier";
    private static final String KEY_SERVIS = "Servis";
    private static final String KEY_PEMBELIAN = "Pembelian";

    /**
     * Constructor used by the AdminDashboardController.
     * @param adminName the full name of the logged-in admin
     */
    public AdminDashboardView(String adminName) {
        initComponents();
        customInit();
        adminLabel.setText("Admin: " + adminName);
        wireNavigation();
    }

    /** No-arg constructor (needed for NetBeans designer preview). */
    public AdminDashboardView() {
        initComponents();
        customInit();
        wireNavigation();
    }

    // --- Panel getters used by AdminDashboardController ---
    public DashboardPanelView getDashboardPanel() { return dashboardPanel; }
    public CustomerPanelView getCustomerPanel() { return customerPanel; }
    public SparepartPanelView getSparepartPanel() { return sparepartPanel; }
    public SupplierPanelView getSupplierPanel() { return supplierPanel; }
    public ServiceTransactionPanelView getServicePanel() { return servicePanel; }
    public PurchaseTransactionPanelView getPurchasePanel() { return purchasePanel; }

    // --- Listener ---
    public void addLogoutListener(ActionListener l) { logoutButton.addActionListener(l); }

    // --- Internal helpers ---

    private void customInit() {
        sidebarPanel.setBackground(UiTheme.SIDEBAR_BG);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 14));
        appTitle.setForeground(UiTheme.PRIMARY);
        appTitle.setBorder(BorderFactory.createEmptyBorder(0, 8, 14, 0));
        appTitle.setAlignmentX(0.0f);

        styleNavButton(dashboardBtn); styleNavButton(customerBtn); styleNavButton(sparepartBtn);
        styleNavButton(supplierBtn); styleNavButton(serviceBtn); styleNavButton(purchaseBtn);

        navButtons.put(KEY_DASHBOARD, dashboardBtn);
        navButtons.put(KEY_PELANGGAN, customerBtn);
        navButtons.put(KEY_SPAREPART, sparepartBtn);
        navButtons.put(KEY_SUPPLIER, supplierBtn);
        navButtons.put(KEY_SERVIS, serviceBtn);
        navButtons.put(KEY_PEMBELIAN, purchaseBtn);

        topBarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        topBarPanel.setBackground(UiTheme.SURFACE);
        titleLabel.setForeground(UiTheme.TEXT_PRIMARY);
        adminLabel.setFont(UiTheme.FONT_BODY); adminLabel.setForeground(UiTheme.TEXT_MUTED);

        styleSecondaryButton(logoutButton);
        contentPanel.setBackground(UiTheme.BACKGROUND);
    }

    private void wireNavigation() {
        // Add all panels to the CardLayout content area
        contentPanel.add(dashboardPanel, KEY_DASHBOARD);
        contentPanel.add(customerPanel, KEY_PELANGGAN);
        contentPanel.add(sparepartPanel, KEY_SPAREPART);
        contentPanel.add(supplierPanel, KEY_SUPPLIER);
        contentPanel.add(servicePanel, KEY_SERVIS);
        contentPanel.add(purchasePanel, KEY_PEMBELIAN);

        // Wire sidebar buttons to show the corresponding panel
        dashboardBtn.addActionListener(e -> showPanel(KEY_DASHBOARD, "Dashboard Admin"));
        customerBtn.addActionListener(e -> showPanel(KEY_PELANGGAN, "Kelola Data Pelanggan"));
        sparepartBtn.addActionListener(e -> showPanel(KEY_SPAREPART, "Kelola Data Sparepart"));
        supplierBtn.addActionListener(e -> showPanel(KEY_SUPPLIER, "Kelola Data Supplier"));
        serviceBtn.addActionListener(e -> showPanel(KEY_SERVIS, "Transaksi Servis Masuk"));
        purchaseBtn.addActionListener(e -> showPanel(KEY_PEMBELIAN, "Transaksi Pembelian Supplier"));

        // Show dashboard by default
        showPanel(KEY_DASHBOARD, "Dashboard Admin");
    }

    private void showPanel(String key, String title) {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, key);
        titleLabel.setText(title);
        for (java.util.Map.Entry<String, javax.swing.JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().setBackground(UiTheme.PRIMARY);
                entry.getValue().setForeground(java.awt.Color.WHITE);
            } else {
                entry.getValue().setBackground(UiTheme.SIDEBAR_BUTTON);
                entry.getValue().setForeground(UiTheme.TEXT_PRIMARY);
            }
        }
    }

    private void styleNavButton(javax.swing.JButton btn) {
        btn.setFont(UiTheme.FONT_BODY);
        btn.setForeground(UiTheme.TEXT_PRIMARY);
        btn.setBackground(UiTheme.SIDEBAR_BUTTON);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setAlignmentX(0.0f);
        btn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 44));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    }

    private void styleSecondaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel = new javax.swing.JPanel();
        appTitle = new javax.swing.JLabel();
        dashboardBtn = new javax.swing.JButton();
        customerBtn = new javax.swing.JButton();
        sparepartBtn = new javax.swing.JButton();
        supplierBtn = new javax.swing.JButton();
        serviceBtn = new javax.swing.JButton();
        purchaseBtn = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        topBarPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        rightTopPanel = new javax.swing.JPanel();
        adminLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aplikasi Desktop Bengkel dan Sparepart");
        setMinimumSize(new java.awt.Dimension(1120, 700));
        getContentPane().setLayout(new java.awt.BorderLayout());

        sidebarPanel.setPreferredSize(new java.awt.Dimension(260, 700));
        sidebarPanel.setLayout(new javax.swing.BoxLayout(sidebarPanel, javax.swing.BoxLayout.Y_AXIS));

        appTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        appTitle.setText("BengkelPro");
        sidebarPanel.add(appTitle);

        dashboardBtn.setText("Dashboard Admin");
        sidebarPanel.add(dashboardBtn);

        customerBtn.setText("Kelola Data Pelanggan");
        sidebarPanel.add(customerBtn);

        sparepartBtn.setText("Kelola Data Sparepart");
        sidebarPanel.add(sparepartBtn);

        supplierBtn.setText("Kelola Data Supplier");
        sidebarPanel.add(supplierBtn);

        serviceBtn.setText("Transaksi Servis Masuk");
        sidebarPanel.add(serviceBtn);

        purchaseBtn.setText("Transaksi Pembelian Supplier");
        sidebarPanel.add(purchaseBtn);

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.WEST);

        mainPanel.setLayout(new java.awt.BorderLayout());

        topBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        topBarPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        titleLabel.setText("Dashboard Admin");
        topBarPanel.add(titleLabel, java.awt.BorderLayout.WEST);

        rightTopPanel.setOpaque(false);
        rightTopPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        adminLabel.setText("Admin: -");
        rightTopPanel.add(adminLabel);

        logoutButton.setText("Logout");
        rightTopPanel.add(logoutButton);

        topBarPanel.add(rightTopPanel, java.awt.BorderLayout.EAST);

        mainPanel.add(topBarPanel, java.awt.BorderLayout.NORTH);

        contentPanel.setLayout(new java.awt.CardLayout());
        mainPanel.add(contentPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1280, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminLabel;
    private javax.swing.JLabel appTitle;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton customerBtn;
    private javax.swing.JButton dashboardBtn;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton purchaseBtn;
    private javax.swing.JPanel rightTopPanel;
    private javax.swing.JButton serviceBtn;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JButton sparepartBtn;
    private javax.swing.JButton supplierBtn;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel topBarPanel;
    // End of variables declaration//GEN-END:variables
}
