package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.AutoCompleteComboBox;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

public class PurchaseTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;
    private AutoCompleteComboBox.Controller supplierCodeController;
    private AutoCompleteComboBox.Controller partCodeController;
    private final Map<String, Sparepart> sparepartsByCode = new HashMap<>();

    public PurchaseTransactionPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        styleTextField(purchaseNoField); styleTextField(purchaseDateField);
        styleComboBox(supplierCodeCombo);
        styleTextField(paymentMethodField); styleTextField(statusField);
        styleComboBox(partCodeCombo);
        styleTextField(partNameField); styleTextField(qtyField); styleTextField(unitPriceField);
        styleSecondaryButton(addItemButton); styleSecondaryButton(removeItemButton);
        styleSecondaryButton(newButton); stylePrimaryButton(saveButton);
        totalValue.setForeground(UiTheme.PRIMARY_DARK);
        itemSubtotalValue.setForeground(UiTheme.PRIMARY_DARK);
        purchaseDateField.setText(DateUtil.todayText()); statusField.setText("DRAFT"); qtyField.setText("1");

        purchaseNoField.setEditable(false);

        supplierCodeController = AutoCompleteComboBox.attach(supplierCodeCombo);
        partCodeController = AutoCompleteComboBox.attach(partCodeCombo);

        partCodeCombo.addActionListener(e -> applySelectedPartToItemFields());
        qtyField.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateItemSubtotalPreview());
        unitPriceField.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateItemSubtotalPreview());
        updateItemSubtotalPreview();

        detailModel = new DefaultTableModel(new String[]{"Kode", "Nama Part", "Qty", "Harga", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        detailTable.setModel(detailModel);
        UiTheme.styleTable(detailTable);
    }

    private void styleTextField(javax.swing.JTextField f) {
        f.setFont(UiTheme.FONT_BODY); f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER), BorderFactory.createEmptyBorder(2,2,2,2)));
    }
    private void styleComboBox(javax.swing.JComboBox<?> combo) {
        combo.setFont(UiTheme.FONT_BODY);
        combo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER), BorderFactory.createEmptyBorder(2,2,2,2)));
    }
    private void stylePrimaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(java.awt.Color.WHITE); b.setBackground(UiTheme.PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }
    private void styleSecondaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }

    public void addAddItemListener(ActionListener l) { addItemButton.addActionListener(l); }
    public void addRemoveItemListener(ActionListener l) { removeItemButton.addActionListener(l); }
    public void addSaveListener(ActionListener l) { saveButton.addActionListener(l); }
    public void addNewListener(ActionListener l) { newButton.addActionListener(l); }

    public String getPurchaseNo() { return purchaseNoField.getText().trim(); }
    public String getPurchaseDateText() { return purchaseDateField.getText().trim(); }
    public String getSupplierCode() { return extractCode(supplierCodeController.getSelectedText()); }
    public String getPaymentMethod() { return paymentMethodField.getText().trim(); }
    public String getStatus() { return statusField.getText().trim(); }
    public String getPartCode() { return extractCode(partCodeController.getSelectedText()); }
    public String getPartName() { return partNameField.getText().trim(); }
    public int getItemQty() { try { return Integer.parseInt(qtyField.getText().trim()); } catch (Exception e) { return 0; } }
    public BigDecimal getUnitPrice() { return MoneyUtil.parse(unitPriceField.getText()); }

    public void setPurchaseNo(String purchaseNo) {
        purchaseNoField.setText(purchaseNo == null ? "" : purchaseNo);
    }

    public void setSupplierOptions(List<Supplier> suppliers) {
        List<String> options = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            options.add(formatOption(supplier.getSupplierCode(), supplier.getName()));
        }
        supplierCodeController.setItems(options);
    }

    public void setSparepartOptions(List<Sparepart> spareparts) {
        sparepartsByCode.clear();
        List<String> options = new ArrayList<>();
        for (Sparepart sparepart : spareparts) {
            sparepartsByCode.put(sparepart.getPartCode(), sparepart);
            options.add(formatOption(sparepart.getPartCode(), sparepart.getName()));
        }
        partCodeController.setItems(options);
    }

    public void clearItemInput() {
        partCodeController.setSelectedItem("");
        partNameField.setText(""); qtyField.setText("1");
        unitPriceField.setText("");
        updateItemSubtotalPreview();
        partCodeCombo.requestFocus();
    }

    public void clearTransactionForm() {
        purchaseNoField.setText(""); purchaseDateField.setText(DateUtil.todayText());
        supplierCodeController.setSelectedItem("");
        paymentMethodField.setText(""); statusField.setText("DRAFT");
        detailModel.setRowCount(0); setTotalValue(BigDecimal.ZERO);
        clearItemInput(); purchaseNoField.requestFocus();
    }

    public void addDetailRow(PurchaseItem item) {
        detailModel.addRow(new Object[]{item.getPartCode(), item.getPartName(), item.getQty(),
                MoneyUtil.format(item.getUnitPrice()), MoneyUtil.format(item.getSubtotal())});
    }

    public void removeSelectedDetailRow() {
        int sel = detailTable.getSelectedRow();
        if (sel >= 0) detailModel.removeRow(sel);
    }

    public int getDetailCount() { return detailModel.getRowCount(); }

    public List<PurchaseItem> getDetailItems() {
        List<PurchaseItem> items = new ArrayList<>();
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            items.add(new PurchaseItem(
                    String.valueOf(detailModel.getValueAt(i, 0)),
                    String.valueOf(detailModel.getValueAt(i, 1)),
                    Integer.parseInt(String.valueOf(detailModel.getValueAt(i, 2))),
                    MoneyUtil.parse(String.valueOf(detailModel.getValueAt(i, 3)))));
        }
        return items;
    }

    public BigDecimal calculateCurrentTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseItem item : getDetailItems()) total = total.add(item.getSubtotal());
        return total;
    }

    public void setTotalValue(BigDecimal value) { totalValue.setText(MoneyUtil.format(value)); }

    private void applySelectedPartToItemFields() {
        String code = extractCode(partCodeController.getSelectedText());
        if (code.isBlank()) {
            return;
        }

        Sparepart sparepart = sparepartsByCode.get(code);
        if (sparepart == null) {
            return;
        }

        partNameField.setText(sparepart.getName());
        unitPriceField.setText(MoneyUtil.format(sparepart.getPurchasePrice()));
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
        if (display == null) {
            return "";
        }
        String trimmed = display.trim();
        int separator = trimmed.indexOf(" - ");
        return separator > 0 ? trimmed.substring(0, separator).trim() : trimmed;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        headerPanel = new javax.swing.JPanel();
        headerTitleLabel = new javax.swing.JLabel();
        headerFieldsPanel = new javax.swing.JPanel();
        purchaseNoLabel = new javax.swing.JLabel();
        purchaseNoField = new javax.swing.JTextField();
        purchaseDateLabel = new javax.swing.JLabel();
        purchaseDateField = new javax.swing.JTextField();
        supplierCodeLabel = new javax.swing.JLabel();
        supplierCodeCombo = new javax.swing.JComboBox<>();
        paymentMethodLabel = new javax.swing.JLabel();
        paymentMethodField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JTextField();
        detailPanel = new javax.swing.JPanel();
        itemInputPanel = new javax.swing.JPanel();
        detailTitleLabel = new javax.swing.JLabel();
        partCodeLabel = new javax.swing.JLabel();
        partCodeCombo = new javax.swing.JComboBox<>();
        partNameLabel = new javax.swing.JLabel();
        partNameField = new javax.swing.JTextField();
        qtyLabel = new javax.swing.JLabel();
        qtyField = new javax.swing.JTextField();
        unitPriceLabel = new javax.swing.JLabel();
        unitPriceField = new javax.swing.JTextField();
        itemSubtotalLabel = new javax.swing.JLabel();
        itemSubtotalValue = new javax.swing.JLabel();
        addItemButton = new javax.swing.JButton();
        removeItemButton = new javax.swing.JButton();
        detailScrollPane = new javax.swing.JScrollPane();
        detailTable = new javax.swing.JTable();
        footerPanel = new javax.swing.JPanel();
        totalPanel = new javax.swing.JPanel();
        totalTextLabel = new javax.swing.JLabel();
        totalValue = new javax.swing.JLabel();
        footerActionsPanel = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(14, 14));

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        headerPanel.setLayout(new java.awt.BorderLayout(10, 10));
        headerTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        headerTitleLabel.setText("Informasi Pembelian ke Supplier");
        headerPanel.add(headerTitleLabel, java.awt.BorderLayout.NORTH);
        headerFieldsPanel.setOpaque(false);
        headerFieldsPanel.setLayout(new java.awt.GridBagLayout());

        purchaseNoLabel.setText("No. Pembelian");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(purchaseNoLabel, gridBagConstraints);
        purchaseNoField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(purchaseNoField, gridBagConstraints);
        purchaseDateLabel.setText("Tanggal (yyyy-MM-dd)");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=2; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(purchaseDateLabel, gridBagConstraints);
        purchaseDateField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=3; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(purchaseDateField, gridBagConstraints);
        supplierCodeLabel.setText("Kode Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(supplierCodeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(supplierCodeCombo, gridBagConstraints);
        paymentMethodLabel.setText("Metode Bayar");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=2; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(paymentMethodLabel, gridBagConstraints);
        paymentMethodField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=3; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(paymentMethodField, gridBagConstraints);
        statusLabel.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(statusLabel, gridBagConstraints);
        statusField.setColumns(12);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(statusField, gridBagConstraints);

        headerPanel.add(headerFieldsPanel, java.awt.BorderLayout.CENTER);
        add(headerPanel, java.awt.BorderLayout.NORTH);

        detailPanel.setBackground(new java.awt.Color(255, 255, 255));
        detailPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        detailPanel.setLayout(new java.awt.BorderLayout(10, 10));
        itemInputPanel.setOpaque(false);
        itemInputPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc;

        detailTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        detailTitleLabel.setText("Detail Item Pembelian");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=java.awt.GridBagConstraints.REMAINDER; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(0,4,8,4);
        itemInputPanel.add(detailTitleLabel, gbc);

        partCodeLabel.setText("Kode");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=1; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(partCodeLabel, gbc);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=1; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=0.3; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(partCodeCombo, gbc);

        partNameLabel.setText("Nama");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=2; gbc.gridy=1; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(partNameLabel, gbc);
        partNameField.setColumns(20);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=1.0; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(partNameField, gbc);

        qtyLabel.setText("Qty");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(qtyLabel, gbc);
        qtyField.setColumns(6);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=1; gbc.gridy=2; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(qtyField, gbc);

        unitPriceLabel.setText("Harga Satuan");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=2; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(unitPriceLabel, gbc);
        unitPriceField.setColumns(10);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=2; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=1.0; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(unitPriceField, gbc);

        itemSubtotalLabel.setText("Subtotal");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=3; gbc.anchor=java.awt.GridBagConstraints.EAST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemSubtotalLabel, gbc);
        itemSubtotalValue.setText("Rp 0");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=5; gbc.gridy=3; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemSubtotalValue, gbc);

        addItemButton.setText("Tambah Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=1; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(addItemButton, gbc);
        removeItemButton.setText("Hapus Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=2; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(removeItemButton, gbc);

        detailPanel.add(itemInputPanel, java.awt.BorderLayout.NORTH);
        detailScrollPane.setViewportView(detailTable);
        detailPanel.add(detailScrollPane, java.awt.BorderLayout.CENTER);
        add(detailPanel, java.awt.BorderLayout.CENTER);

        footerPanel.setOpaque(false);
        footerPanel.setLayout(new java.awt.BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        totalTextLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        totalTextLabel.setText("Total Pembelian:");
        totalPanel.add(totalTextLabel);
        totalValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        totalValue.setText("Rp 0");
        totalPanel.add(totalValue);
        footerPanel.add(totalPanel, java.awt.BorderLayout.NORTH);
        footerActionsPanel.setOpaque(false);
        footerActionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        newButton.setText("Transaksi Baru"); footerActionsPanel.add(newButton);
        saveButton.setText("Simpan Pembelian"); footerActionsPanel.add(saveButton);
        footerPanel.add(footerActionsPanel, java.awt.BorderLayout.SOUTH);
        add(footerPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemButton;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JScrollPane detailScrollPane;
    private javax.swing.JTable detailTable;
    private javax.swing.JLabel detailTitleLabel;
    private javax.swing.JPanel footerActionsPanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerFieldsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTitleLabel;
    private javax.swing.JPanel itemInputPanel;
    private javax.swing.JButton newButton;
    private javax.swing.JComboBox<String> partCodeCombo;
    private javax.swing.JLabel partCodeLabel;
    private javax.swing.JTextField partNameField;
    private javax.swing.JLabel partNameLabel;
    private javax.swing.JTextField paymentMethodField;
    private javax.swing.JLabel paymentMethodLabel;
    private javax.swing.JTextField purchaseDateField;
    private javax.swing.JLabel purchaseDateLabel;
    private javax.swing.JTextField purchaseNoField;
    private javax.swing.JLabel purchaseNoLabel;
    private javax.swing.JTextField qtyField;
    private javax.swing.JLabel qtyLabel;
    private javax.swing.JButton removeItemButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> supplierCodeCombo;
    private javax.swing.JLabel supplierCodeLabel;
    private javax.swing.JLabel itemSubtotalLabel;
    private javax.swing.JLabel itemSubtotalValue;
    private javax.swing.JLabel totalTextLabel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JLabel totalValue;
    private javax.swing.JTextField unitPriceField;
    private javax.swing.JLabel unitPriceLabel;
    // End of variables declaration//GEN-END:variables
}
