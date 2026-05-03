package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.AutoCompleteComboBox;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * View panel for direct spare part counter sales (Penjualan Sparepart).
 */
public class SaleTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;
    private DefaultTableModel historyModel;
    private AutoCompleteComboBox.Controller customerCodeController;
    private AutoCompleteComboBox.Controller sparepartCodeController;
    private final Map<String, Sparepart> sparepartsByCode = new HashMap<>();

    // Header fields
    private JTextField saleNoField;
    private JTextField saleDateField;
    private JComboBox<String> customerCodeCombo;
    private JTextField paymentMethodField;

    // Item input fields
    private JComboBox<String> sparepartCodeCombo;
    private JTextField partNameField;
    private JTextField itemQtyField;
    private JTextField unitPriceField;
    private JLabel itemSubtotalValue;

    // Buttons
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton newButton;
    private JButton saveButton;

    // Tables
    private JTable detailTable;
    private JTable historyTable;

    // Footer
    private JLabel totalValue;

    public SaleTransactionPanelView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel headerTitle = new JLabel("Informasi Penjualan Sparepart");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(headerTitle, BorderLayout.NORTH);

        JPanel headerFields = new JPanel(new GridBagLayout());
        headerFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        saleNoField = new JTextField(14);
        saleNoField.setEditable(false);
        saleDateField = new JTextField(14);
        saleDateField.setText(DateUtil.todayText());
        customerCodeCombo = new JComboBox<>();
        paymentMethodField = new JTextField(14);
        paymentMethodField.setText("Tunai");

        styleTextField(saleNoField);
        styleTextField(saleDateField);
        styleComboBox(customerCodeCombo);
        styleTextField(paymentMethodField);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        headerFields.add(new JLabel("No. Penjualan"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        headerFields.add(saleNoField, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        headerFields.add(new JLabel("Tanggal (yyyy-MM-dd)"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        headerFields.add(saleDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        headerFields.add(new JLabel("Pelanggan (opsional)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        headerFields.add(customerCodeCombo, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        headerFields.add(new JLabel("Metode Bayar"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        headerFields.add(paymentMethodField, gbc);

        headerPanel.add(headerFields, BorderLayout.CENTER);

        // ===== DETAIL PANEL =====
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JPanel itemInputPanel = new JPanel(new GridBagLayout());
        itemInputPanel.setOpaque(false);

        JLabel detailTitle = new JLabel("Detail Item Penjualan");
        detailTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4, 8, 4);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        itemInputPanel.add(detailTitle, gbc);

        sparepartCodeCombo = new JComboBox<>();
        partNameField = new JTextField(20);
        partNameField.setEditable(false);
        itemQtyField = new JTextField(6);
        itemQtyField.setText("1");
        unitPriceField = new JTextField(10);
        unitPriceField.setEditable(false);
        itemSubtotalValue = new JLabel("Rp 0");

        styleComboBox(sparepartCodeCombo);
        styleTextField(partNameField);
        styleTextField(itemQtyField);
        styleTextField(unitPriceField);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        itemInputPanel.add(new JLabel("Sparepart"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL;
        itemInputPanel.add(sparepartCodeCombo, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        itemInputPanel.add(new JLabel("Nama"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        itemInputPanel.add(partNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        itemInputPanel.add(new JLabel("Qty"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        itemInputPanel.add(itemQtyField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        itemInputPanel.add(new JLabel("Harga Jual"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        itemInputPanel.add(unitPriceField, gbc);

        addItemButton = new JButton("Tambah Item");
        removeItemButton = new JButton("Hapus Item");
        styleSecondaryButton(addItemButton);
        styleSecondaryButton(removeItemButton);

        gbc.gridx = 2; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        itemInputPanel.add(addItemButton, gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        itemInputPanel.add(removeItemButton, gbc);

        JLabel subtotalLabel = new JLabel("Subtotal");
        gbc.gridx = 2; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        itemInputPanel.add(subtotalLabel, gbc);
        itemSubtotalValue.setForeground(UiTheme.PRIMARY_DARK);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        itemInputPanel.add(itemSubtotalValue, gbc);

        detailPanel.add(itemInputPanel, BorderLayout.NORTH);

        detailModel = new DefaultTableModel(
                new String[]{"SparepartID", "Kode Part", "Nama Part", "Qty", "Harga Jual", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        detailTable = new JTable(detailModel);
        UiTheme.styleTable(detailTable);
        // Hide sparepart_id column
        detailTable.getColumnModel().getColumn(0).setMinWidth(0);
        detailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        detailTable.getColumnModel().getColumn(0).setWidth(0);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // ===== FOOTER PANEL =====
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        totalPanel.setOpaque(false);
        JLabel totalLabel = new JLabel("Total Penjualan:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalPanel.add(totalLabel);
        totalValue = new JLabel("Rp 0");
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalValue.setForeground(UiTheme.PRIMARY_DARK);
        totalPanel.add(totalValue);
        footerPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionPanel.setOpaque(false);
        newButton = new JButton("Transaksi Baru");
        saveButton = new JButton("Simpan Transaksi");
        styleSecondaryButton(newButton);
        stylePrimaryButton(saveButton);
        actionPanel.add(newButton);
        actionPanel.add(saveButton);
        footerPanel.add(actionPanel, BorderLayout.SOUTH);

        // ===== HISTORY PANEL =====
        JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel historyTitle = new JLabel("Riwayat Penjualan Sparepart");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyPanel.add(historyTitle, BorderLayout.NORTH);

        historyModel = new DefaultTableModel(
                new String[]{"No Penjualan", "Tanggal", "Pelanggan", "Metode Bayar", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        historyTable = new JTable(historyModel);
        UiTheme.styleTable(historyTable);
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // ===== TABBED PANE =====
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel mainInputPanel = new JPanel(new BorderLayout(14, 14));
        mainInputPanel.setOpaque(false);
        mainInputPanel.add(headerPanel, BorderLayout.NORTH);
        mainInputPanel.add(detailPanel, BorderLayout.CENTER);
        mainInputPanel.add(footerPanel, BorderLayout.SOUTH);

        tabPane.addTab("Input Penjualan Baru", mainInputPanel);
        tabPane.addTab("Riwayat Penjualan", historyPanel);

        add(tabPane, BorderLayout.CENTER);

        // ===== WIRE INTERNAL EVENTS =====
        customerCodeController = AutoCompleteComboBox.attach(customerCodeCombo);
        sparepartCodeController = AutoCompleteComboBox.attach(sparepartCodeCombo);

        sparepartCodeCombo.addActionListener(e -> applySelectedPartFields());
        itemQtyField.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateItemSubtotalPreview());
    }

    // ===== PUBLIC LISTENER METHODS =====
    public void addAddItemListener(ActionListener l) { addItemButton.addActionListener(l); }
    public void addRemoveItemListener(ActionListener l) { removeItemButton.addActionListener(l); }
    public void addSaveListener(ActionListener l) { saveButton.addActionListener(l); }
    public void addNewListener(ActionListener l) { newButton.addActionListener(l); }

    // ===== PUBLIC GETTER METHODS =====
    public String getSaleNo() { return saleNoField.getText().trim(); }
    public String getSaleDateText() { return saleDateField.getText().trim(); }
    public String getCustomerCode() {
        String sel = customerCodeController.getSelectedText();
        return extractCode(sel);
    }
    public String getPaymentMethod() { return paymentMethodField.getText().trim(); }

    public String getPartCode() { return extractCode(sparepartCodeController.getSelectedText()); }
    public String getPartName() { return partNameField.getText().trim(); }
    public int getItemQty() {
        try { return Integer.parseInt(itemQtyField.getText().trim()); }
        catch (Exception e) { return 0; }
    }
    public BigDecimal getUnitPrice() { return MoneyUtil.parse(unitPriceField.getText()); }

    /**
     * Returns the sparepart ID for the currently selected part, or -1 if none.
     */
    public int getSelectedSparepartId() {
        String code = getPartCode();
        Sparepart sp = sparepartsByCode.get(code);
        return sp != null ? sp.getId() : -1;
    }

    // ===== PUBLIC SETTER METHODS =====
    public void setSaleNo(String saleNo) {
        saleNoField.setText(saleNo == null ? "" : saleNo);
    }

    public void setCustomerOptions(List<Customer> customers) {
        List<String> options = new ArrayList<>();
        options.add(""); // empty = walk-in
        for (Customer c : customers) {
            options.add(formatOption(c.getCustomerCode(), c.getName()));
        }
        customerCodeController.setItems(options);
    }

    public void setSparepartOptions(List<Sparepart> spareparts) {
        sparepartsByCode.clear();
        List<String> options = new ArrayList<>();
        for (Sparepart sp : spareparts) {
            sparepartsByCode.put(sp.getPartCode(), sp);
            options.add(formatOption(sp.getPartCode(), sp.getName()));
        }
        sparepartCodeController.setItems(options);
    }

    public void clearItemInput() {
        sparepartCodeController.setSelectedItem("");
        partNameField.setText("");
        itemQtyField.setText("1");
        unitPriceField.setText("");
        updateItemSubtotalPreview();
        sparepartCodeCombo.requestFocus();
    }

    public void clearTransactionForm() {
        saleNoField.setText("");
        saleDateField.setText(DateUtil.todayText());
        customerCodeController.setSelectedItem("");
        paymentMethodField.setText("Tunai");
        detailModel.setRowCount(0);
        setTotalValue(BigDecimal.ZERO);
        clearItemInput();
        saleNoField.requestFocus();
    }

    public void addDetailRow(SaleItem item) {
        detailModel.addRow(new Object[]{
            item.getSparepartId(),
            item.getPartCode(),
            item.getPartName(),
            item.getQty(),
            MoneyUtil.format(item.getUnitPrice()),
            MoneyUtil.format(item.getSubtotal())
        });
    }

    public void removeSelectedDetailRow() {
        int sel = detailTable.getSelectedRow();
        if (sel >= 0) detailModel.removeRow(sel);
    }

    public int getDetailCount() { return detailModel.getRowCount(); }

    public List<SaleItem> getDetailItems() {
        List<SaleItem> items = new ArrayList<>();
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            int sparepartId = (int) detailModel.getValueAt(i, 0);
            String partCode = String.valueOf(detailModel.getValueAt(i, 1));
            String partName = String.valueOf(detailModel.getValueAt(i, 2));
            int qty = Integer.parseInt(String.valueOf(detailModel.getValueAt(i, 3)));
            BigDecimal unitPrice = MoneyUtil.parse(String.valueOf(detailModel.getValueAt(i, 4)));
            items.add(new SaleItem(sparepartId, partCode, partName, qty, unitPrice));
        }
        return items;
    }

    public BigDecimal calculateCurrentTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem item : getDetailItems()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public void setTotalValue(BigDecimal value) {
        totalValue.setText(MoneyUtil.format(value));
    }

    public void setHistoryRows(List<Object[]> rows) {
        historyModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] displayRow = {
                row[0], row[1], row[2], row[3],
                row[4] instanceof BigDecimal ? MoneyUtil.format((BigDecimal) row[4]) : row[4]
            };
            historyModel.addRow(displayRow);
        }
    }

    // ===== INTERNAL HELPERS =====

    private void applySelectedPartFields() {
        String code = extractCode(sparepartCodeController.getSelectedText());
        if (code.isBlank()) return;
        Sparepart sp = sparepartsByCode.get(code);
        if (sp == null) return;
        partNameField.setText(sp.getName());
        unitPriceField.setText(MoneyUtil.format(sp.getSellingPrice()));
        updateItemSubtotalPreview();
    }

    private void updateItemSubtotalPreview() {
        BigDecimal price = MoneyUtil.parse(unitPriceField.getText());
        int qty = getItemQty();
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));
        itemSubtotalValue.setText(MoneyUtil.format(subtotal));
    }

    private static String formatOption(String code, String name) {
        String safeCode = code == null ? "" : code.trim();
        String safeName = name == null ? "" : name.trim();
        return safeCode + " - " + safeName;
    }

    private static String extractCode(String display) {
        if (display == null) return "";
        String trimmed = display.trim();
        int separator = trimmed.indexOf(" - ");
        return separator > 0 ? trimmed.substring(0, separator).trim() : trimmed;
    }

    // ===== STYLING =====
    private void styleTextField(JTextField f) {
        f.setFont(UiTheme.FONT_BODY);
        f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(UiTheme.FONT_BODY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }

    private void stylePrimaryButton(JButton b) {
        b.setFont(UiTheme.FONT_BODY);
        b.setForeground(Color.WHITE);
        b.setBackground(UiTheme.PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
    }

    private void styleSecondaryButton(JButton b) {
        b.setFont(UiTheme.FONT_BODY);
        b.setForeground(UiTheme.TEXT_PRIMARY);
        b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
    }
}
