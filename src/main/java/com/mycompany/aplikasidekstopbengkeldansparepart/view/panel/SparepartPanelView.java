package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SparepartPanelView extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private Integer editingId;

    public SparepartPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        styleTextField(partCodeField); styleTextField(nameField); styleTextField(categoryField);
        styleTextField(unitField); styleTextField(purchasePriceField); styleTextField(sellingPriceField);
        styleTextField(stockField); styleTextField(minStockField); styleTextField(searchField);
        styleSecondaryButton(newButton);
        styleSecondaryButton(deleteButton); styleSecondaryButton(refreshButton);
        stylePrimaryButton(saveButton);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Kode", "Nama", "Kategori", "Satuan", "Harga Beli", "Harga Jual", "Stok", "Min Stok"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        UiTheme.styleTable(table);

        // Auto-fill form when a table row is clicked
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Sparepart selected = getSelectedRowAsSparepart();
                if (selected != null) {
                    setFormData(selected);
                }
            }
        });
    }

    private void styleTextField(javax.swing.JTextField f) {
        f.setFont(UiTheme.FONT_BODY); f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER), BorderFactory.createEmptyBorder(2,2,2,2)));
    }
    private void stylePrimaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(java.awt.Color.WHITE); b.setBackground(UiTheme.PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }
    private void styleSecondaryButton(javax.swing.JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }

    public void addNewListener(ActionListener l) { newButton.addActionListener(l); }
    public void addSaveListener(ActionListener l) { saveButton.addActionListener(l); }
    public void addDeleteListener(ActionListener l) { deleteButton.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { refreshButton.addActionListener(l); }
    public void addSearchListener(DocumentListener l) { searchField.getDocument().addDocumentListener(l); }
    public String getSearchText() { return searchField.getText().trim(); }

    public void applySearchFilter(String keyword) {
        if (keyword == null || keyword.isBlank()) { sorter.setRowFilter(null); return; }
        sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(keyword)));
    }

    public Sparepart getFormData() {
        Sparepart s = new Sparepart(); s.setId(editingId);
        s.setPartCode(partCodeField.getText().trim()); s.setName(nameField.getText().trim());
        s.setCategory(categoryField.getText().trim()); s.setUnit(unitField.getText().trim());
        s.setPurchasePrice(MoneyUtil.parse(purchasePriceField.getText()));
        s.setSellingPrice(MoneyUtil.parse(sellingPriceField.getText()));
        s.setStock(parseIntSafe(stockField.getText())); s.setMinStock(parseIntSafe(minStockField.getText()));
        return s;
    }

    public void setFormData(Sparepart s) {
        editingId = s.getId(); partCodeField.setText(s.getPartCode()); nameField.setText(s.getName());
        categoryField.setText(s.getCategory()); unitField.setText(s.getUnit());
        purchasePriceField.setText(s.getPurchasePrice() == null ? "" : s.getPurchasePrice().toPlainString());
        sellingPriceField.setText(s.getSellingPrice() == null ? "" : s.getSellingPrice().toPlainString());
        stockField.setText(String.valueOf(s.getStock())); minStockField.setText(String.valueOf(s.getMinStock()));
    }

    public void clearForm() {
        editingId = null; partCodeField.setText(""); nameField.setText(""); categoryField.setText("");
        unitField.setText(""); purchasePriceField.setText(""); sellingPriceField.setText("");
        stockField.setText("0"); minStockField.setText("0"); partCodeField.requestFocus();
    }

    public Integer getSelectedRowId() {
        int v = table.getSelectedRow(); if (v < 0) return null;
        int m = table.convertRowIndexToModel(v);
        return Integer.valueOf(tableModel.getValueAt(m, 0).toString());
    }

    public Sparepart getSelectedRowAsSparepart() {
        int v = table.getSelectedRow(); if (v < 0) return null;
        int m = table.convertRowIndexToModel(v);
        Sparepart s = new Sparepart();
        s.setId(Integer.valueOf(tableModel.getValueAt(m, 0).toString()));
        s.setPartCode(String.valueOf(tableModel.getValueAt(m, 1)));
        s.setName(String.valueOf(tableModel.getValueAt(m, 2)));
        s.setCategory(String.valueOf(tableModel.getValueAt(m, 3)));
        s.setUnit(String.valueOf(tableModel.getValueAt(m, 4)));
        s.setPurchasePrice(MoneyUtil.parse(String.valueOf(tableModel.getValueAt(m, 5))));
        s.setSellingPrice(MoneyUtil.parse(String.valueOf(tableModel.getValueAt(m, 6))));
        s.setStock(Integer.parseInt(String.valueOf(tableModel.getValueAt(m, 7))));
        s.setMinStock(Integer.parseInt(String.valueOf(tableModel.getValueAt(m, 8))));
        return s;
    }

    public void setTableData(List<Sparepart> list) {
        tableModel.setRowCount(0);
        for (Sparepart s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getPartCode(), s.getName(), s.getCategory(),
                    s.getUnit(), MoneyUtil.format(s.getPurchasePrice()), MoneyUtil.format(s.getSellingPrice()),
                    s.getStock(), s.getMinStock()});
        }
    }

    private int parseIntSafe(String t) { try { return Integer.parseInt(t.trim()); } catch (Exception e) { return 0; } }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        formPanel = new javax.swing.JPanel();
        formTitleLabel = new javax.swing.JLabel();
        formFieldsPanel = new javax.swing.JPanel();
        partCodeLabel = new javax.swing.JLabel();
        partCodeField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        categoryLabel = new javax.swing.JLabel();
        categoryField = new javax.swing.JTextField();
        unitLabel = new javax.swing.JLabel();
        unitField = new javax.swing.JTextField();
        purchasePriceLabel = new javax.swing.JLabel();
        purchasePriceField = new javax.swing.JTextField();
        sellingPriceLabel = new javax.swing.JLabel();
        sellingPriceField = new javax.swing.JTextField();
        stockLabel = new javax.swing.JLabel();
        stockField = new javax.swing.JTextField();
        minStockLabel = new javax.swing.JLabel();
        minStockField = new javax.swing.JTextField();
        tablePanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        tableTitleLabel = new javax.swing.JLabel();
        searchLabel = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        tableScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        actionPanel = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(14, 14));

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        formPanel.setLayout(new java.awt.BorderLayout(10, 10));

        formTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        formTitleLabel.setText("Form Data Sparepart");
        formPanel.add(formTitleLabel, java.awt.BorderLayout.NORTH);

        formFieldsPanel.setOpaque(false);
        formFieldsPanel.setLayout(new java.awt.GridBagLayout());

        partCodeLabel.setText("Kode Part");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(partCodeLabel, gridBagConstraints);
        partCodeField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 0; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(partCodeField, gridBagConstraints);
        nameLabel.setText("Nama Part");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 0; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(nameLabel, gridBagConstraints);
        nameField.setColumns(18);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 0; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(nameField, gridBagConstraints);

        categoryLabel.setText("Kategori");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 1; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(categoryLabel, gridBagConstraints);
        categoryField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 1; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(categoryField, gridBagConstraints);
        unitLabel.setText("Satuan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 1; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(unitLabel, gridBagConstraints);
        unitField.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 1; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(unitField, gridBagConstraints);

        purchasePriceLabel.setText("Harga Beli");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 2; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(purchasePriceLabel, gridBagConstraints);
        purchasePriceField.setColumns(12);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 2; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(purchasePriceField, gridBagConstraints);
        sellingPriceLabel.setText("Harga Jual");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 2; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(sellingPriceLabel, gridBagConstraints);
        sellingPriceField.setColumns(12);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 2; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(sellingPriceField, gridBagConstraints);

        stockLabel.setText("Stok");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 3; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(stockLabel, gridBagConstraints);
        stockField.setColumns(8);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 3; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(stockField, gridBagConstraints);
        minStockLabel.setText("Min Stok");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 3; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(minStockLabel, gridBagConstraints);
        minStockField.setColumns(8);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 3; gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx = 1.0; gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(minStockField, gridBagConstraints);

        formPanel.add(formFieldsPanel, java.awt.BorderLayout.CENTER);
        add(formPanel, java.awt.BorderLayout.NORTH);

        tablePanel.setBackground(new java.awt.Color(255, 255, 255));
        tablePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        tablePanel.setLayout(new java.awt.BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        tableTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tableTitleLabel.setText("Daftar Sparepart");
        searchPanel.add(tableTitleLabel);
        searchLabel.setText("Cari:");
        searchPanel.add(searchLabel);
        searchField.setColumns(20);
        searchPanel.add(searchField);
        tablePanel.add(searchPanel, java.awt.BorderLayout.NORTH);
        tableScrollPane.setViewportView(table);
        tablePanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);
        add(tablePanel, java.awt.BorderLayout.CENTER);

        actionPanel.setOpaque(false);
        actionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        refreshButton.setText("Refresh"); actionPanel.add(refreshButton);
        newButton.setText("Baru"); actionPanel.add(newButton);
        saveButton.setText("Simpan"); actionPanel.add(saveButton);
        deleteButton.setText("Hapus"); actionPanel.add(deleteButton);
        add(actionPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JTextField categoryField;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel formFieldsPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel formTitleLabel;
    private javax.swing.JTextField minStockField;
    private javax.swing.JLabel minStockLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JTextField partCodeField;
    private javax.swing.JLabel partCodeLabel;
    private javax.swing.JTextField purchasePriceField;
    private javax.swing.JLabel purchasePriceLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField sellingPriceField;
    private javax.swing.JLabel sellingPriceLabel;
    private javax.swing.JTextField stockField;
    private javax.swing.JLabel stockLabel;
    private javax.swing.JTable table;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JLabel tableTitleLabel;
    private javax.swing.JTextField unitField;
    private javax.swing.JLabel unitLabel;
    // End of variables declaration//GEN-END:variables
}
