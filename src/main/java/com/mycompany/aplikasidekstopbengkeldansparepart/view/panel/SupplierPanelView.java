package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SupplierPanelView extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private Integer editingId;

    public SupplierPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        styleTextField(supplierCodeField);
        styleTextField(nameField);
        styleTextField(phoneField);
        styleTextField(emailField);
        styleTextField(addressField);
        styleTextField(searchField);

        styleSecondaryButton(newButton);
        styleSecondaryButton(deleteButton);
        styleSecondaryButton(refreshButton);
        stylePrimaryButton(saveButton);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Kode", "Nama", "Telepon", "Email", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        UiTheme.styleTable(table);

        // Auto-fill form when a table row is clicked
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Supplier selected = getSelectedRowAsSupplier();
                if (selected != null) {
                    setFormData(selected);
                }
            }
        });
    }

    private void styleTextField(javax.swing.JTextField field) {
        field.setFont(UiTheme.FONT_BODY);
        field.setMargin(UiTheme.FIELD_INSETS);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }

    private void stylePrimaryButton(javax.swing.JButton button) {
        button.setFont(UiTheme.FONT_BODY);
        button.setForeground(java.awt.Color.WHITE);
        button.setBackground(UiTheme.PRIMARY);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }

    private void styleSecondaryButton(javax.swing.JButton button) {
        button.setFont(UiTheme.FONT_BODY);
        button.setForeground(UiTheme.TEXT_PRIMARY);
        button.setBackground(UiTheme.SIDEBAR_BUTTON);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }

    public void addNewListener(ActionListener listener) { newButton.addActionListener(listener); }
    public void addSaveListener(ActionListener listener) { saveButton.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addSearchListener(DocumentListener listener) { searchField.getDocument().addDocumentListener(listener); }
    public String getSearchText() { return searchField.getText().trim(); }

    public void applySearchFilter(String keyword) {
        if (keyword == null || keyword.isBlank()) { sorter.setRowFilter(null); return; }
        sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(keyword)));
    }

    public Supplier getFormData() {
        return new Supplier(editingId, supplierCodeField.getText().trim(), nameField.getText().trim(),
                phoneField.getText().trim(), emailField.getText().trim(), addressField.getText().trim());
    }

    public void setFormData(Supplier supplier) {
        editingId = supplier.getId();
        supplierCodeField.setText(supplier.getSupplierCode());
        nameField.setText(supplier.getName());
        phoneField.setText(supplier.getPhone());
        emailField.setText(supplier.getEmail());
        addressField.setText(supplier.getAddress());
    }

    public void clearForm() {
        editingId = null;
        supplierCodeField.setText(""); nameField.setText(""); phoneField.setText("");
        emailField.setText(""); addressField.setText("");
        supplierCodeField.requestFocus();
    }

    public Integer getSelectedRowId() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow < 0) return null;
        int modelRow = table.convertRowIndexToModel(selectedViewRow);
        return Integer.valueOf(tableModel.getValueAt(modelRow, 0).toString());
    }

    public Supplier getSelectedRowAsSupplier() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow < 0) return null;
        int modelRow = table.convertRowIndexToModel(selectedViewRow);
        return new Supplier(
                Integer.valueOf(tableModel.getValueAt(modelRow, 0).toString()),
                String.valueOf(tableModel.getValueAt(modelRow, 1)),
                String.valueOf(tableModel.getValueAt(modelRow, 2)),
                String.valueOf(tableModel.getValueAt(modelRow, 3)),
                String.valueOf(tableModel.getValueAt(modelRow, 4)),
                String.valueOf(tableModel.getValueAt(modelRow, 5)));
    }

    public void setTableData(List<Supplier> suppliers) {
        tableModel.setRowCount(0);
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{s.getId(), s.getSupplierCode(), s.getName(),
                    s.getPhone(), s.getEmail(), s.getAddress()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        formPanel = new javax.swing.JPanel();
        formTitleLabel = new javax.swing.JLabel();
        formFieldsPanel = new javax.swing.JPanel();
        supplierCodeLabel = new javax.swing.JLabel();
        supplierCodeField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
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
        formTitleLabel.setText("Form Data Supplier");
        formPanel.add(formTitleLabel, java.awt.BorderLayout.NORTH);

        formFieldsPanel.setOpaque(false);
        formFieldsPanel.setLayout(new java.awt.GridBagLayout());

        supplierCodeLabel.setText("Kode Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(supplierCodeLabel, gridBagConstraints);

        supplierCodeField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(supplierCodeField, gridBagConstraints);

        nameLabel.setText("Nama Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(nameLabel, gridBagConstraints);

        nameField.setColumns(18);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(nameField, gridBagConstraints);

        phoneLabel.setText("Telepon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(phoneLabel, gridBagConstraints);

        phoneField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(phoneField, gridBagConstraints);

        emailLabel.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(emailLabel, gridBagConstraints);

        emailField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3; gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(emailField, gridBagConstraints);

        addressLabel.setText("Alamat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(addressLabel, gridBagConstraints);

        addressField.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        formFieldsPanel.add(addressField, gridBagConstraints);

        formPanel.add(formFieldsPanel, java.awt.BorderLayout.CENTER);
        add(formPanel, java.awt.BorderLayout.NORTH);

        tablePanel.setBackground(new java.awt.Color(255, 255, 255));
        tablePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        tablePanel.setLayout(new java.awt.BorderLayout(10, 10));

        searchPanel.setOpaque(false);
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));

        tableTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tableTitleLabel.setText("Daftar Supplier");
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
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JPanel formFieldsPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel formTitleLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField supplierCodeField;
    private javax.swing.JLabel supplierCodeLabel;
    private javax.swing.JTable table;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JLabel tableTitleLabel;
    // End of variables declaration//GEN-END:variables
}
