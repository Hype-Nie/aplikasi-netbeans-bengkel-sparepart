package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

public class ReportPanelView extends javax.swing.JPanel {

    private DefaultTableModel serviceModel;
    private DefaultTableModel purchaseModel;
    private DefaultTableModel saleModel;

    public ReportPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        stylePrimaryButton(loadReportButton);
        styleSecondaryButton(exportServiceCsvButton);
        styleSecondaryButton(exportPurchaseCsvButton);
        styleSecondaryButton(exportSaleCsvButton);

        totalServiceLabel.setForeground(UiTheme.PRIMARY_DARK);
        totalPurchaseLabel.setForeground(UiTheme.PRIMARY_DARK);
        totalSaleLabel.setForeground(UiTheme.PRIMARY_DARK);
        profitLabel.setForeground(UiTheme.SUCCESS);

        // Set default dates: first day of month -> today
        java.time.LocalDate today = java.time.LocalDate.now();
        dateFromChooser.setDate(java.sql.Date.valueOf(today.withDayOfMonth(1)));
        dateToChooser.setDate(java.sql.Date.valueOf(today));
        dateFromChooser.setDateFormatString("yyyy-MM-dd");
        dateToChooser.setDateFormatString("yyyy-MM-dd");

        serviceModel = new DefaultTableModel(
                new String[]{"No Servis", "Tanggal", "Pelanggan", "Kendaraan", "Mekanik", "Status", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        purchaseModel = new DefaultTableModel(
                new String[]{"No Pembelian", "Tanggal", "Supplier", "Metode Bayar", "Status", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        saleModel = new DefaultTableModel(
                new String[]{"No Penjualan", "Tanggal", "Pelanggan", "Metode Bayar", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        serviceTable.setModel(serviceModel);
        purchaseTable.setModel(purchaseModel);
        saleTable.setModel(saleModel);
        UiTheme.styleTable(serviceTable);
        UiTheme.styleTable(purchaseTable);
        UiTheme.styleTable(saleTable);
    }

    private void styleTextField(javax.swing.JTextField f) {
        f.setFont(UiTheme.FONT_BODY); f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
    private void stylePrimaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(java.awt.Color.WHITE); b.setBackground(UiTheme.PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16)); b.setFocusPainted(false);
        b.setContentAreaFilled(false); b.setOpaque(true);
    }
    private void styleSecondaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16)); b.setFocusPainted(false);
        b.setContentAreaFilled(false); b.setOpaque(true);
    }

    // --- Listeners ---
    public void addLoadReportListener(ActionListener l) { loadReportButton.addActionListener(l); }
    public void addExportServiceCsvListener(ActionListener l) { exportServiceCsvButton.addActionListener(l); }
    public void addExportPurchaseCsvListener(ActionListener l) { exportPurchaseCsvButton.addActionListener(l); }
    public void addExportSaleCsvListener(ActionListener l) { exportSaleCsvButton.addActionListener(l); }

    // --- Getters ---
    public String getDateFrom() { 
        java.util.Date date = dateFromChooser.getDate();
        return date != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) : ""; 
    }
    public String getDateTo() { 
        java.util.Date date = dateToChooser.getDate();
        return date != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) : ""; 
    }

    // --- Setters ---
    public void setServiceRows(List<Object[]> rows) {
        serviceModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] display = new Object[row.length];
            System.arraycopy(row, 0, display, 0, row.length);
            if (display[6] instanceof BigDecimal) {
                display[6] = MoneyUtil.format((BigDecimal) display[6]);
            }
            serviceModel.addRow(display);
        }
    }

    public void setPurchaseRows(List<Object[]> rows) {
        purchaseModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] display = new Object[row.length];
            System.arraycopy(row, 0, display, 0, row.length);
            if (display[5] instanceof BigDecimal) {
                display[5] = MoneyUtil.format((BigDecimal) display[5]);
            }
            purchaseModel.addRow(display);
        }
    }

    public void setSaleRows(List<Object[]> rows) {
        saleModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] display = new Object[row.length];
            System.arraycopy(row, 0, display, 0, row.length);
            if (display[4] instanceof BigDecimal) {
                display[4] = MoneyUtil.format((BigDecimal) display[4]);
            }
            saleModel.addRow(display);
        }
    }

    public void setSummary(BigDecimal totalService, BigDecimal totalPurchase, BigDecimal totalSale) {
        totalServiceLabel.setText("Pemasukan Servis: " + MoneyUtil.format(totalService));
        totalPurchaseLabel.setText("Pengeluaran Pembelian: " + MoneyUtil.format(totalPurchase));
        totalSaleLabel.setText("Pemasukan Penjualan: " + MoneyUtil.format(totalSale));
        BigDecimal profit = totalService.add(totalSale).subtract(totalPurchase);
        profitLabel.setText("Laba Kotor: " + MoneyUtil.format(profit));
    }

    public DefaultTableModel getServiceModel() { return serviceModel; }
    public DefaultTableModel getPurchaseModel() { return purchaseModel; }
    public DefaultTableModel getSaleModel() { return saleModel; }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gbc;

        filterPanel = new javax.swing.JPanel();
        filterTitleLabel = new javax.swing.JLabel();
        filterFieldsPanel = new javax.swing.JPanel();
        dateFromLabel = new javax.swing.JLabel();
        dateFromChooser = new com.toedter.calendar.JDateChooser();
        dateToLabel = new javax.swing.JLabel();
        dateToChooser = new com.toedter.calendar.JDateChooser();
        loadReportButton = new javax.swing.JButton();
        summaryPanel = new javax.swing.JPanel();
        totalServiceLabel = new javax.swing.JLabel();
        totalPurchaseLabel = new javax.swing.JLabel();
        totalSaleLabel = new javax.swing.JLabel();
        profitLabel = new javax.swing.JLabel();
        tabPane = new javax.swing.JTabbedPane();
        serviceTabPanel = new javax.swing.JPanel();
        serviceToolbar = new javax.swing.JPanel();
        exportServiceCsvButton = new javax.swing.JButton();
        serviceScrollPane = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        purchaseTabPanel = new javax.swing.JPanel();
        purchaseToolbar = new javax.swing.JPanel();
        exportPurchaseCsvButton = new javax.swing.JButton();
        purchaseScrollPane = new javax.swing.JScrollPane();
        purchaseTable = new javax.swing.JTable();
        saleTabPanel = new javax.swing.JPanel();
        saleToolbar = new javax.swing.JPanel();
        exportSaleCsvButton = new javax.swing.JButton();
        saleScrollPane = new javax.swing.JScrollPane();
        saleTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout(14, 14));

        // --- Filter panel ---
        filterPanel.setBackground(new java.awt.Color(255, 255, 255));
        filterPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)),
                javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        filterPanel.setLayout(new java.awt.BorderLayout(10, 10));

        filterTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        filterTitleLabel.setText("Laporan Transaksi");
        filterPanel.add(filterTitleLabel, java.awt.BorderLayout.NORTH);

        filterFieldsPanel.setOpaque(false);
        filterFieldsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 4));
        dateFromLabel.setText("Dari Tanggal:");
        filterFieldsPanel.add(dateFromLabel);
        dateFromChooser.setPreferredSize(new java.awt.Dimension(140, 26));
        filterFieldsPanel.add(dateFromChooser);
        dateToLabel.setText("Sampai Tanggal:");
        filterFieldsPanel.add(dateToLabel);
        dateToChooser.setPreferredSize(new java.awt.Dimension(140, 26));
        filterFieldsPanel.add(dateToChooser);
        loadReportButton.setText("Tampilkan Laporan");
        filterFieldsPanel.add(loadReportButton);
        filterPanel.add(filterFieldsPanel, java.awt.BorderLayout.CENTER);

        // Summary row
        summaryPanel.setOpaque(false);
        summaryPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 4));
        totalServiceLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        totalServiceLabel.setText("Pemasukan Servis: Rp 0");
        summaryPanel.add(totalServiceLabel);
        totalPurchaseLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        totalPurchaseLabel.setText("Pengeluaran Pembelian: Rp 0");
        summaryPanel.add(totalPurchaseLabel);
        totalSaleLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        totalSaleLabel.setText("Pemasukan Penjualan: Rp 0");
        summaryPanel.add(totalSaleLabel);
        profitLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        profitLabel.setText("Laba Kotor: Rp 0");
        summaryPanel.add(profitLabel);
        filterPanel.add(summaryPanel, java.awt.BorderLayout.SOUTH);

        add(filterPanel, java.awt.BorderLayout.NORTH);

        // --- Tabbed pane ---
        tabPane.setFont(new java.awt.Font("Segoe UI", 0, 14));

        // Service tab
        serviceTabPanel.setBackground(new java.awt.Color(255, 255, 255));
        serviceTabPanel.setLayout(new java.awt.BorderLayout(8, 8));
        serviceToolbar.setOpaque(false);
        serviceToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));
        exportServiceCsvButton.setText("Export CSV Servis");
        serviceToolbar.add(exportServiceCsvButton);
        serviceTabPanel.add(serviceToolbar, java.awt.BorderLayout.NORTH);
        serviceScrollPane.setViewportView(serviceTable);
        serviceTabPanel.add(serviceScrollPane, java.awt.BorderLayout.CENTER);
        tabPane.addTab("Laporan Servis", serviceTabPanel);

        // Purchase tab
        purchaseTabPanel.setBackground(new java.awt.Color(255, 255, 255));
        purchaseTabPanel.setLayout(new java.awt.BorderLayout(8, 8));
        purchaseToolbar.setOpaque(false);
        purchaseToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));
        exportPurchaseCsvButton.setText("Export CSV Pembelian");
        purchaseToolbar.add(exportPurchaseCsvButton);
        purchaseTabPanel.add(purchaseToolbar, java.awt.BorderLayout.NORTH);
        purchaseScrollPane.setViewportView(purchaseTable);
        purchaseTabPanel.add(purchaseScrollPane, java.awt.BorderLayout.CENTER);
        tabPane.addTab("Laporan Pembelian", purchaseTabPanel);

        // Sale tab
        saleTabPanel.setBackground(new java.awt.Color(255, 255, 255));
        saleTabPanel.setLayout(new java.awt.BorderLayout(8, 8));
        saleToolbar.setOpaque(false);
        saleToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));
        exportSaleCsvButton.setText("Export CSV Penjualan");
        saleToolbar.add(exportSaleCsvButton);
        saleTabPanel.add(saleToolbar, java.awt.BorderLayout.NORTH);
        saleScrollPane.setViewportView(saleTable);
        saleTabPanel.add(saleScrollPane, java.awt.BorderLayout.CENTER);
        tabPane.addTab("Laporan Penjualan", saleTabPanel);

        add(tabPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFromChooser;
    private javax.swing.JLabel dateFromLabel;
    private com.toedter.calendar.JDateChooser dateToChooser;
    private javax.swing.JLabel dateToLabel;
    private javax.swing.JButton exportPurchaseCsvButton;
    private javax.swing.JButton exportServiceCsvButton;
    private javax.swing.JPanel filterFieldsPanel;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel filterTitleLabel;
    private javax.swing.JButton loadReportButton;
    private javax.swing.JLabel profitLabel;
    private javax.swing.JScrollPane purchaseScrollPane;
    private javax.swing.JTable purchaseTable;
    private javax.swing.JPanel purchaseTabPanel;
    private javax.swing.JPanel purchaseToolbar;
    private javax.swing.JScrollPane serviceScrollPane;
    private javax.swing.JTable serviceTable;
    private javax.swing.JPanel serviceTabPanel;
    private javax.swing.JPanel serviceToolbar;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JLabel totalPurchaseLabel;
    private javax.swing.JLabel totalSaleLabel;
    private javax.swing.JLabel totalServiceLabel;
    private javax.swing.JPanel saleTabPanel;
    private javax.swing.JPanel saleToolbar;
    private javax.swing.JButton exportSaleCsvButton;
    private javax.swing.JScrollPane saleScrollPane;
    private javax.swing.JTable saleTable;
    // End of variables declaration//GEN-END:variables
}
