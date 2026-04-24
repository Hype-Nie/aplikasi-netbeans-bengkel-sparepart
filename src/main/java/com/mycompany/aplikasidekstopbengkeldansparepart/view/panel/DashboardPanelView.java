package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.DashboardSummary;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

public class DashboardPanelView extends javax.swing.JPanel {

    private DefaultTableModel recentServiceModel;
    private DefaultTableModel lowStockModel;

    public DashboardPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // Style stat cards
        styleCard(card1, UiTheme.PRIMARY); styleCard(card2, UiTheme.SUCCESS);
        styleCard(card3, UiTheme.WARNING); styleCard(card4, UiTheme.PRIMARY_DARK);
        activeCustomersValue.setForeground(UiTheme.PRIMARY);
        totalStockValue.setForeground(UiTheme.SUCCESS);
        todayServicesValue.setForeground(UiTheme.WARNING);
        monthlyPurchasesValue.setForeground(UiTheme.PRIMARY_DARK);
        card1Title.setForeground(UiTheme.TEXT_MUTED); card2Title.setForeground(UiTheme.TEXT_MUTED);
        card3Title.setForeground(UiTheme.TEXT_MUTED); card4Title.setForeground(UiTheme.TEXT_MUTED);

        styleSecondaryButton(refreshButton);
        todayRevenueLabel.setForeground(UiTheme.PRIMARY_DARK);
        revenueText.setFont(UiTheme.FONT_BODY); revenueText.setForeground(UiTheme.TEXT_PRIMARY);
        note1.setFont(UiTheme.FONT_BODY); note1.setForeground(UiTheme.TEXT_PRIMARY);
        note2.setFont(UiTheme.FONT_BODY); note2.setForeground(UiTheme.TEXT_PRIMARY);
        note3.setFont(UiTheme.FONT_BODY); note3.setForeground(UiTheme.TEXT_PRIMARY);

        recentServiceModel = new DefaultTableModel(
                new String[]{"No Servis", "Pelanggan", "Kendaraan", "Status", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        lowStockModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Stok"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        recentServiceTable.setModel(recentServiceModel); UiTheme.styleTable(recentServiceTable);
        lowStockTable.setModel(lowStockModel); UiTheme.styleTable(lowStockTable);
    }

    private void styleCard(javax.swing.JPanel card, java.awt.Color accent) {
        card.setBackground(UiTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
    }

    private void styleSecondaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setOpaque(true);
    }

    public void setSummary(DashboardSummary summary) {
        activeCustomersValue.setText(String.valueOf(summary.getActiveCustomers()));
        totalStockValue.setText(String.valueOf(summary.getTotalStock()));
        todayServicesValue.setText(String.valueOf(summary.getTodayServices()));
        monthlyPurchasesValue.setText(String.valueOf(summary.getMonthlyPurchases()));
        todayRevenueLabel.setText(MoneyUtil.format(summary.getTodayRevenue()));
    }

    public void setRecentServiceRows(java.util.List<Object[]> rows) {
        recentServiceModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] displayRow = {row[0], row[1], row[2], row[3],
                    row[4] instanceof java.math.BigDecimal ? MoneyUtil.format((java.math.BigDecimal) row[4]) : row[4]};
            recentServiceModel.addRow(displayRow);
        }
    }

    public void setLowStockRows(java.util.List<Object[]> rows) {
        lowStockModel.setRowCount(0);
        for (Object[] row : rows) lowStockModel.addRow(row);
    }

    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topContainer = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        card1Title = new javax.swing.JLabel();
        activeCustomersValue = new javax.swing.JLabel();
        card2 = new javax.swing.JPanel();
        card2Title = new javax.swing.JLabel();
        totalStockValue = new javax.swing.JLabel();
        card3 = new javax.swing.JPanel();
        card3Title = new javax.swing.JLabel();
        todayServicesValue = new javax.swing.JLabel();
        card4 = new javax.swing.JPanel();
        card4Title = new javax.swing.JLabel();
        monthlyPurchasesValue = new javax.swing.JLabel();
        refreshPanel = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        mainSplitPane = new javax.swing.JSplitPane();
        recentServicePanel = new javax.swing.JPanel();
        recentServiceTitle = new javax.swing.JLabel();
        recentServiceScrollPane = new javax.swing.JScrollPane();
        recentServiceTable = new javax.swing.JTable();
        rightContainer = new javax.swing.JPanel();
        lowStockPanel = new javax.swing.JPanel();
        lowStockTitle = new javax.swing.JLabel();
        lowStockScrollPane = new javax.swing.JScrollPane();
        lowStockTable = new javax.swing.JTable();
        summaryPanel = new javax.swing.JPanel();
        summaryTitle = new javax.swing.JLabel();
        summaryBody = new javax.swing.JPanel();
        note1 = new javax.swing.JLabel();
        note2 = new javax.swing.JLabel();
        note3 = new javax.swing.JLabel();
        revenueRow = new javax.swing.JPanel();
        revenueText = new javax.swing.JLabel();
        todayRevenueLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout(14, 14));

        topContainer.setOpaque(false);
        topContainer.setLayout(new java.awt.BorderLayout());
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new java.awt.GridLayout(1, 4, 12, 12));

        card1.setBackground(new java.awt.Color(255, 255, 255));
        card1.setLayout(new java.awt.BorderLayout(8, 8));
        card1Title.setFont(new java.awt.Font("Segoe UI", 0, 12));
        card1Title.setText("Pelanggan Aktif");
        card1.add(card1Title, java.awt.BorderLayout.NORTH);
        activeCustomersValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        activeCustomersValue.setText("0");
        card1.add(activeCustomersValue, java.awt.BorderLayout.CENTER);
        statsPanel.add(card1);

        card2.setBackground(new java.awt.Color(255, 255, 255));
        card2.setLayout(new java.awt.BorderLayout(8, 8));
        card2Title.setFont(new java.awt.Font("Segoe UI", 0, 12));
        card2Title.setText("Stok Sparepart");
        card2.add(card2Title, java.awt.BorderLayout.NORTH);
        totalStockValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        totalStockValue.setText("0");
        card2.add(totalStockValue, java.awt.BorderLayout.CENTER);
        statsPanel.add(card2);

        card3.setBackground(new java.awt.Color(255, 255, 255));
        card3.setLayout(new java.awt.BorderLayout(8, 8));
        card3Title.setFont(new java.awt.Font("Segoe UI", 0, 12));
        card3Title.setText("Servis Hari Ini");
        card3.add(card3Title, java.awt.BorderLayout.NORTH);
        todayServicesValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        todayServicesValue.setText("0");
        card3.add(todayServicesValue, java.awt.BorderLayout.CENTER);
        statsPanel.add(card3);

        card4.setBackground(new java.awt.Color(255, 255, 255));
        card4.setLayout(new java.awt.BorderLayout(8, 8));
        card4Title.setFont(new java.awt.Font("Segoe UI", 0, 12));
        card4Title.setText("Pembelian Bulan Ini");
        card4.add(card4Title, java.awt.BorderLayout.NORTH);
        monthlyPurchasesValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        monthlyPurchasesValue.setText("0");
        card4.add(monthlyPurchasesValue, java.awt.BorderLayout.CENTER);
        statsPanel.add(card4);

        topContainer.add(statsPanel, java.awt.BorderLayout.CENTER);
        refreshPanel.setOpaque(false);
        refreshPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        refreshButton.setText("Refresh Dashboard");
        refreshPanel.add(refreshButton);
        topContainer.add(refreshPanel, java.awt.BorderLayout.SOUTH);
        add(topContainer, java.awt.BorderLayout.NORTH);

        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.68);
        mainSplitPane.setBorder(null);

        recentServicePanel.setBackground(new java.awt.Color(255, 255, 255));
        recentServicePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        recentServicePanel.setLayout(new java.awt.BorderLayout(10, 10));
        recentServiceTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        recentServiceTitle.setText("Aktivitas Servis Terbaru");
        recentServicePanel.add(recentServiceTitle, java.awt.BorderLayout.NORTH);
        recentServiceScrollPane.setViewportView(recentServiceTable);
        recentServicePanel.add(recentServiceScrollPane, java.awt.BorderLayout.CENTER);
        mainSplitPane.setLeftComponent(recentServicePanel);

        rightContainer.setOpaque(false);
        rightContainer.setLayout(new java.awt.BorderLayout(12, 12));
        lowStockPanel.setBackground(new java.awt.Color(255, 255, 255));
        lowStockPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        lowStockPanel.setLayout(new java.awt.BorderLayout(10, 10));
        lowStockTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lowStockTitle.setText("Sparepart Stok Menipis");
        lowStockPanel.add(lowStockTitle, java.awt.BorderLayout.NORTH);
        lowStockScrollPane.setViewportView(lowStockTable);
        lowStockPanel.add(lowStockScrollPane, java.awt.BorderLayout.CENTER);
        rightContainer.add(lowStockPanel, java.awt.BorderLayout.CENTER);

        summaryPanel.setBackground(new java.awt.Color(255, 255, 255));
        summaryPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        summaryPanel.setPreferredSize(new java.awt.Dimension(360, 170));
        summaryPanel.setLayout(new java.awt.BorderLayout(10, 10));
        summaryTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        summaryTitle.setText("Ringkasan Hari Ini");
        summaryPanel.add(summaryTitle, java.awt.BorderLayout.NORTH);
        summaryBody.setOpaque(false);
        summaryBody.setLayout(new java.awt.GridLayout(4, 1, 8, 8));
        note1.setText("- Total transaksi servis dihitung otomatis dari database"); summaryBody.add(note1);
        note2.setText("- Total pembelian bulan ini dihitung otomatis"); summaryBody.add(note2);
        note3.setText("- Stok sparepart ikut berubah saat transaksi"); summaryBody.add(note3);
        revenueRow.setOpaque(false); revenueRow.setLayout(new java.awt.BorderLayout());
        revenueText.setText("Estimasi omzet hari ini:"); revenueRow.add(revenueText, java.awt.BorderLayout.WEST);
        todayRevenueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        todayRevenueLabel.setText("Rp 0"); revenueRow.add(todayRevenueLabel, java.awt.BorderLayout.EAST);
        summaryBody.add(revenueRow);
        summaryPanel.add(summaryBody, java.awt.BorderLayout.CENTER);
        rightContainer.add(summaryPanel, java.awt.BorderLayout.SOUTH);
        mainSplitPane.setRightComponent(rightContainer);

        add(mainSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activeCustomersValue;
    private javax.swing.JPanel card1;
    private javax.swing.JLabel card1Title;
    private javax.swing.JPanel card2;
    private javax.swing.JLabel card2Title;
    private javax.swing.JPanel card3;
    private javax.swing.JLabel card3Title;
    private javax.swing.JPanel card4;
    private javax.swing.JLabel card4Title;
    private javax.swing.JPanel lowStockPanel;
    private javax.swing.JScrollPane lowStockScrollPane;
    private javax.swing.JTable lowStockTable;
    private javax.swing.JLabel lowStockTitle;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JLabel monthlyPurchasesValue;
    private javax.swing.JLabel note1;
    private javax.swing.JLabel note2;
    private javax.swing.JLabel note3;
    private javax.swing.JPanel recentServicePanel;
    private javax.swing.JScrollPane recentServiceScrollPane;
    private javax.swing.JTable recentServiceTable;
    private javax.swing.JLabel recentServiceTitle;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel refreshPanel;
    private javax.swing.JPanel revenueRow;
    private javax.swing.JLabel revenueText;
    private javax.swing.JPanel rightContainer;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel summaryBody;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JLabel summaryTitle;
    private javax.swing.JLabel todayRevenueLabel;
    private javax.swing.JLabel todayServicesValue;
    private javax.swing.JPanel topContainer;
    private javax.swing.JLabel totalStockValue;
    // End of variables declaration//GEN-END:variables
}
