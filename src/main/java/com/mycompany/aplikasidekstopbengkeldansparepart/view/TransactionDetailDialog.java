package com.mycompany.aplikasidekstopbengkeldansparepart.view;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.ReceiptPrinter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Reusable modal dialog that displays full transaction details
 * (header info + items table + total) with a "Cetak Struk" print button.
 */
public class TransactionDetailDialog extends JDialog {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private TransactionDetailDialog(Frame parent, String title) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 450));
        setLocationRelativeTo(parent);
    }

    // ==================== SALE ====================

    public static void showSaleDetail(Frame parent, SaleTransaction tx, List<SaleItem> items, String customerName) {
        TransactionDetailDialog dialog = new TransactionDetailDialog(parent, "Detail Penjualan - " + tx.getSaleNo());

        JPanel content = createContentPanel();

        // Header info
        JPanel info = createInfoPanel();
        addInfoRow(info, "No. Penjualan", tx.getSaleNo());
        addInfoRow(info, "Tanggal", tx.getSaleDate().format(DATE_FMT));
        addInfoRow(info, "Pelanggan", customerName == null || customerName.isBlank() ? "Umum (Walk-in)" : customerName);
        addInfoRow(info, "Metode Bayar", tx.getPaymentMethod());
        content.add(info, BorderLayout.NORTH);

        // Items table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Kode Part", "Nama Part", "Qty", "Harga Satuan", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (SaleItem item : items) {
            model.addRow(new Object[]{
                item.getPartCode(), item.getPartName(), item.getQty(),
                MoneyUtil.format(item.getUnitPrice()), MoneyUtil.format(item.getSubtotal())
            });
        }
        JTable table = new JTable(model);
        UiTheme.styleTable(table);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer: total + print button
        content.add(createFooter(tx.getTotal(), () ->
                ReceiptPrinter.printSaleReceipt(tx, items, customerName)), BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setVisible(true);
    }

    // ==================== SERVICE ====================

    public static void showServiceDetail(Frame parent, ServiceTransaction tx, List<ServiceItem> items, String customerName) {
        TransactionDetailDialog dialog = new TransactionDetailDialog(parent, "Detail Servis - " + tx.getServiceNo());

        JPanel content = createContentPanel();

        // Header info
        JPanel info = createInfoPanel();
        addInfoRow(info, "No. Servis", tx.getServiceNo());
        addInfoRow(info, "Tanggal", tx.getServiceDate().format(DATE_FMT));
        addInfoRow(info, "Pelanggan", customerName == null || customerName.isBlank() ? "-" : customerName);
        addInfoRow(info, "Kendaraan", tx.getVehicle());
        addInfoRow(info, "Keluhan", tx.getComplaint());
        addInfoRow(info, "Mekanik", tx.getMechanic());
        addInfoRow(info, "Status", tx.getStatus());
        content.add(info, BorderLayout.NORTH);

        // Items table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Jenis", "Deskripsi", "Qty", "Harga", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (ServiceItem item : items) {
            model.addRow(new Object[]{
                item.getItemType(), item.getDescription(), item.getQty(),
                MoneyUtil.format(item.getPrice()), MoneyUtil.format(item.getSubtotal())
            });
        }
        JTable table = new JTable(model);
        UiTheme.styleTable(table);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer
        content.add(createFooter(tx.getTotal(), () ->
                ReceiptPrinter.printServiceReceipt(tx, items, customerName)), BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setVisible(true);
    }

    // ==================== PURCHASE ====================

    public static void showPurchaseDetail(Frame parent, PurchaseTransaction tx, List<PurchaseItem> items, String supplierName) {
        TransactionDetailDialog dialog = new TransactionDetailDialog(parent, "Detail Pembelian - " + tx.getPurchaseNo());

        JPanel content = createContentPanel();

        // Header info
        JPanel info = createInfoPanel();
        addInfoRow(info, "No. Pembelian", tx.getPurchaseNo());
        addInfoRow(info, "Tanggal", tx.getPurchaseDate().format(DATE_FMT));
        addInfoRow(info, "Supplier", supplierName == null || supplierName.isBlank() ? "-" : supplierName);
        addInfoRow(info, "Metode Bayar", tx.getPaymentMethod());
        addInfoRow(info, "Status", tx.getStatus());
        content.add(info, BorderLayout.NORTH);

        // Items table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Kode Part", "Nama Part", "Qty", "Harga Satuan", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (PurchaseItem item : items) {
            model.addRow(new Object[]{
                item.getPartCode(), item.getPartName(), item.getQty(),
                MoneyUtil.format(item.getUnitPrice()), MoneyUtil.format(item.getSubtotal())
            });
        }
        JTable table = new JTable(model);
        UiTheme.styleTable(table);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer
        content.add(createFooter(tx.getTotal(), () ->
                ReceiptPrinter.printPurchaseReceipt(tx, items, supplierName)), BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setVisible(true);
    }

    // ==================== UI HELPERS ====================

    private static JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        return panel;
    }

    private static JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
                BorderFactory.createEmptyBorder(0, 0, 12, 0)));
        return panel;
    }

    private static int infoRow = 0;

    private static void addInfoRow(JPanel panel, String label, String value) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Calculate row from component count
        int row = panel.getComponentCount() / 2;

        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(UiTheme.TEXT_MUTED);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(labelComp, gbc);

        JLabel valueComp = new JLabel(value == null ? "-" : value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComp.setForeground(UiTheme.TEXT_PRIMARY);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueComp, gbc);
    }

    private static JPanel createFooter(BigDecimal total, Runnable printAction) {
        JPanel footer = new JPanel(new BorderLayout(10, 8));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 0, 0, 0)));

        // Total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        totalPanel.setOpaque(false);
        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPanel.add(totalLabel);
        JLabel totalValue = new JLabel(MoneyUtil.format(total));
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalValue.setForeground(UiTheme.PRIMARY_DARK);
        totalPanel.add(totalValue);
        footer.add(totalPanel, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton printBtn = UiTheme.createPrimaryButton("Cetak Struk");
        printBtn.addActionListener(e -> printAction.run());
        btnPanel.add(printBtn);

        JButton closeBtn = UiTheme.createSecondaryButton("Tutup");
        closeBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(closeBtn);
            if (window != null) window.dispose();
        });
        btnPanel.add(closeBtn);

        footer.add(btnPanel, BorderLayout.SOUTH);
        return footer;
    }
}
