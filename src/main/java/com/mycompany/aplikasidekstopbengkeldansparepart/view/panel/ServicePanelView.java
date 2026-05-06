package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Service;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD panel for the services master table (Jasa).
 */
public class ServicePanelView extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private List<Service> fullData;

    // Form fields
    private JTextField idField;
    private JTextField serviceCodeField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField searchField;

    // Buttons
    private JButton newButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton refreshButton;

    // Table
    private JTable dataTable;

    public ServicePanelView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // ===== FORM PANEL (LEFT) =====
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel formTitle = new JLabel("Form Data Jasa / Layanan");
        formTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        formPanel.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(10);
        idField.setEditable(false);
        idField.setVisible(false);

        serviceCodeField = new JTextField(14);
        serviceCodeField.setEditable(false);
        nameField = new JTextField(20);
        priceField = new JTextField(14);

        styleTextField(serviceCodeField);
        styleTextField(nameField);
        styleTextField(priceField);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fieldsPanel.add(new JLabel("Kode Jasa"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(serviceCodeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fieldsPanel.add(new JLabel("Nama Jasa"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        fieldsPanel.add(new JLabel("Harga Default"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(priceField, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        newButton = new JButton("Baru");
        saveButton = new JButton("Simpan");
        deleteButton = new JButton("Hapus");
        refreshButton = new JButton("Refresh");

        styleSecondaryButton(newButton);
        stylePrimaryButton(saveButton);
        styleSecondaryButton(deleteButton);
        styleSecondaryButton(refreshButton);

        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== TABLE PANEL (CENTER) =====
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JPanel tableHeaderPanel = new JPanel(new BorderLayout(10, 0));
        tableHeaderPanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Data Jasa / Layanan");
        tableTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        tableHeaderPanel.add(tableTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Cari:"));
        searchField = new JTextField(16);
        styleTextField(searchField);
        searchPanel.add(searchField);
        tableHeaderPanel.add(searchPanel, BorderLayout.EAST);
        tablePanel.add(tableHeaderPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Kode Jasa", "Nama Jasa", "Harga Default"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        UiTheme.styleTable(dataTable);
        dataTable.getColumnModel().getColumn(0).setMinWidth(0);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(0);
        dataTable.getColumnModel().getColumn(0).setWidth(0);

        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRow();
            }
        });

        tablePanel.add(new JScrollPane(dataTable), BorderLayout.CENTER);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(14, 14));
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    // ===== PUBLIC METHODS =====

    public void addNewListener(ActionListener l) { newButton.addActionListener(l); }
    public void addSaveListener(ActionListener l) { saveButton.addActionListener(l); }
    public void addDeleteListener(ActionListener l) { deleteButton.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { refreshButton.addActionListener(l); }
    public void addSearchListener(DocumentListener l) { searchField.getDocument().addDocumentListener(l); }

    public String getSearchText() { return searchField.getText().trim(); }

    public Service getFormData() {
        Service service = new Service();
        String idText = idField.getText().trim();
        if (!idText.isBlank()) {
            service.setId(Integer.parseInt(idText));
        }
        service.setServiceCode(serviceCodeField.getText().trim());
        service.setName(nameField.getText().trim());
        service.setPrice(MoneyUtil.parse(priceField.getText()));
        return service;
    }

    public void setServiceCode(String code) {
        serviceCodeField.setText(code == null ? "" : code);
    }

    public void clearForm() {
        idField.setText("");
        serviceCodeField.setText("");
        nameField.setText("");
        priceField.setText("");
        dataTable.clearSelection();
    }

    public void focusNameField() {
        nameField.requestFocus();
    }

    public Integer getSelectedRowId() {
        int sel = dataTable.getSelectedRow();
        if (sel < 0) return null;
        return (Integer) tableModel.getValueAt(sel, 0);
    }

    public void setTableData(List<Service> data) {
        this.fullData = data;
        tableModel.setRowCount(0);
        for (Service s : data) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getServiceCode(), s.getName(), MoneyUtil.format(s.getPrice())
            });
        }
    }

    public void applySearchFilter(String keyword) {
        if (fullData == null) return;
        tableModel.setRowCount(0);
        String lowerKeyword = keyword.toLowerCase();
        for (Service s : fullData) {
            if (keyword.isBlank()
                    || s.getServiceCode().toLowerCase().contains(lowerKeyword)
                    || s.getName().toLowerCase().contains(lowerKeyword)) {
                tableModel.addRow(new Object[]{
                    s.getId(), s.getServiceCode(), s.getName(), MoneyUtil.format(s.getPrice())
                });
            }
        }
    }

    private void loadSelectedRow() {
        int sel = dataTable.getSelectedRow();
        if (sel < 0) return;
        idField.setText(String.valueOf(tableModel.getValueAt(sel, 0)));
        serviceCodeField.setText(String.valueOf(tableModel.getValueAt(sel, 1)));
        nameField.setText(String.valueOf(tableModel.getValueAt(sel, 2)));
        priceField.setText(String.valueOf(tableModel.getValueAt(sel, 3)));
    }

    // ===== STYLING HELPERS =====
    private void styleTextField(JTextField f) {
        f.setFont(UiTheme.FONT_BODY);
        f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(
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
