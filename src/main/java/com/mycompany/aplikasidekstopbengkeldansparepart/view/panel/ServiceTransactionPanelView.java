package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
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

public class ServiceTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;
    private AutoCompleteComboBox.Controller customerCodeController;
    private AutoCompleteComboBox.Controller itemCodeController;
    private final Map<String, Sparepart> sparepartsByCode = new HashMap<>();

    public ServiceTransactionPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        styleTextField(serviceNoField); styleTextField(serviceDateField);
        styleComboBox(customerCodeCombo);
        styleTextField(vehicleField); styleTextField(complaintField); styleTextField(mechanicField);
        styleTextField(statusField);
        styleComboBox(itemTypeCombo); styleComboBox(itemCodeCombo);
        styleTextField(itemDescriptionField); styleTextField(itemQtyField); styleTextField(itemPriceField);
        styleSecondaryButton(addItemButton); styleSecondaryButton(removeItemButton);
        styleSecondaryButton(newButton); stylePrimaryButton(saveButton);
        totalValue.setForeground(UiTheme.PRIMARY_DARK);
        itemSubtotalValue.setForeground(UiTheme.PRIMARY_DARK);

        serviceNoField.setEditable(false);

        serviceDateField.setText(DateUtil.todayText());
        statusField.setText("MASUK");
        itemQtyField.setText("1");

        customerCodeController = AutoCompleteComboBox.attach(customerCodeCombo);
        itemCodeController = AutoCompleteComboBox.attach(itemCodeCombo);

        itemTypeCombo.addActionListener(e -> applySelectedPartToItemFields());
        itemCodeCombo.addActionListener(e -> applySelectedPartToItemFields());
        itemQtyField.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateItemSubtotalPreview());
        itemPriceField.getDocument().addDocumentListener((SimpleDocumentListener) event -> updateItemSubtotalPreview());
        updateItemSubtotalPreview();

        detailModel = new DefaultTableModel(
                new String[]{"Jenis", "Kode", "Deskripsi", "Qty", "Harga", "Subtotal"}, 0) {
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
    public void addUpdateStatusListener(ActionListener l) { updateStatusButton.addActionListener(l); }

    public String getServiceNo() { return serviceNoField.getText().trim(); }
    public String getServiceDateText() { return serviceDateField.getText().trim(); }
    public String getCustomerCode() { return extractCode(customerCodeController.getSelectedText()); }
    public String getVehicle() { return vehicleField.getText().trim(); }
    public String getComplaint() { return complaintField.getText().trim(); }
    public String getMechanic() { return mechanicField.getText().trim(); }
    public String getStatus() { return statusField.getText().trim(); }
    public String getItemType() { return String.valueOf(itemTypeCombo.getSelectedItem()); }
    public String getItemCode() { return extractCode(itemCodeController.getSelectedText()); }
    public String getItemDescription() { return itemDescriptionField.getText().trim(); }
    public int getItemQty() { try { return Integer.parseInt(itemQtyField.getText().trim()); } catch (Exception e) { return 0; } }
    public BigDecimal getItemPrice() { return MoneyUtil.parse(itemPriceField.getText()); }

    public void setServiceNo(String serviceNo) {
        serviceNoField.setText(serviceNo == null ? "" : serviceNo);
    }

    public void setCustomerOptions(List<Customer> customers) {
        List<String> options = new ArrayList<>();
        for (Customer customer : customers) {
            options.add(formatOption(customer.getCustomerCode(), customer.getName()));
        }
        customerCodeController.setItems(options);
    }

    public void setSparepartOptions(List<Sparepart> spareparts) {
        sparepartsByCode.clear();
        List<String> options = new ArrayList<>();
        for (Sparepart sparepart : spareparts) {
            sparepartsByCode.put(sparepart.getPartCode(), sparepart);
            options.add(formatOption(sparepart.getPartCode(), sparepart.getName()));
        }
        itemCodeController.setItems(options);
    }

    public void clearItemInput() {
        itemCodeController.setSelectedItem("");
        itemDescriptionField.setText(""); itemQtyField.setText("1");
        itemPriceField.setText("");
        updateItemSubtotalPreview();
        itemCodeCombo.requestFocus();
    }

    public void clearTransactionForm() {
        serviceNoField.setText(""); serviceDateField.setText(DateUtil.todayText());
        customerCodeController.setSelectedItem("");
        vehicleField.setText(""); complaintField.setText("");
        mechanicField.setText(""); statusField.setText("MASUK");
        detailModel.setRowCount(0); setTotalValue(BigDecimal.ZERO);
        clearItemInput(); serviceNoField.requestFocus();
    }

    public void addDetailRow(ServiceItem item) {
        detailModel.addRow(new Object[]{item.getItemType(), item.getItemCode(), item.getDescription(),
                item.getQty(), MoneyUtil.format(item.getPrice()), MoneyUtil.format(item.getSubtotal())});
    }

    public void removeSelectedDetailRow() {
        int sel = detailTable.getSelectedRow();
        if (sel >= 0) detailModel.removeRow(sel);
    }

    public int getDetailCount() { return detailModel.getRowCount(); }

    public List<ServiceItem> getDetailItems() {
        List<ServiceItem> items = new ArrayList<>();
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            items.add(new ServiceItem(
                    String.valueOf(detailModel.getValueAt(i, 0)),
                    String.valueOf(detailModel.getValueAt(i, 1)),
                    String.valueOf(detailModel.getValueAt(i, 2)),
                    Integer.parseInt(String.valueOf(detailModel.getValueAt(i, 3))),
                    MoneyUtil.parse(String.valueOf(detailModel.getValueAt(i, 4)))));
        }
        return items;
    }

    public BigDecimal calculateCurrentTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ServiceItem item : getDetailItems()) total = total.add(item.getSubtotal());
        return total;
    }

    public void setTotalValue(BigDecimal value) { totalValue.setText(MoneyUtil.format(value)); }

    // --- History table + status update ---
    public String getSelectedHistoryServiceNo() {
        int sel = historyTable.getSelectedRow();
        return sel >= 0 ? String.valueOf(historyModel.getValueAt(sel, 0)) : null;
    }

    public String getNewStatusSelection() {
        return String.valueOf(statusUpdateCombo.getSelectedItem());
    }

    public void setHistoryRows(List<Object[]> rows) {
        historyModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] displayRow = {row[0], row[1], row[2], row[3], row[4],
                    row[5] instanceof java.math.BigDecimal ? MoneyUtil.format((java.math.BigDecimal) row[5]) : row[5]};
            historyModel.addRow(displayRow);
        }
    }

    private void applySelectedPartToItemFields() {
        if (!"PART".equalsIgnoreCase(getItemType())) {
            return;
        }

        String code = extractCode(itemCodeController.getSelectedText());
        if (code.isBlank()) {
            return;
        }

        Sparepart sparepart = sparepartsByCode.get(code);
        if (sparepart == null) {
            return;
        }

        itemDescriptionField.setText(sparepart.getName());
        itemPriceField.setText(MoneyUtil.format(sparepart.getSellingPrice()));
        updateItemSubtotalPreview();
    }

    private void updateItemSubtotalPreview() {
        BigDecimal price = MoneyUtil.parse(itemPriceField.getText());
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
        serviceNoLabel = new javax.swing.JLabel();
        serviceNoField = new javax.swing.JTextField();
        serviceDateLabel = new javax.swing.JLabel();
        serviceDateField = new javax.swing.JTextField();
        customerCodeLabel = new javax.swing.JLabel();
        customerCodeCombo = new javax.swing.JComboBox<>();
        vehicleLabel = new javax.swing.JLabel();
        vehicleField = new javax.swing.JTextField();
        complaintLabel = new javax.swing.JLabel();
        complaintField = new javax.swing.JTextField();
        mechanicLabel = new javax.swing.JLabel();
        mechanicField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JTextField();
        detailPanel = new javax.swing.JPanel();
        itemInputPanel = new javax.swing.JPanel();
        detailTitleLabel = new javax.swing.JLabel();
        itemTypeLabel = new javax.swing.JLabel();
        itemTypeCombo = new javax.swing.JComboBox<>();
        itemCodeLabel = new javax.swing.JLabel();
        itemCodeCombo = new javax.swing.JComboBox<>();
        itemDescLabel = new javax.swing.JLabel();
        itemDescriptionField = new javax.swing.JTextField();
        itemQtyLabel = new javax.swing.JLabel();
        itemQtyField = new javax.swing.JTextField();
        itemPriceLabel = new javax.swing.JLabel();
        itemPriceField = new javax.swing.JTextField();
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
        headerTitleLabel.setText("Informasi Servis Masuk");
        headerPanel.add(headerTitleLabel, java.awt.BorderLayout.NORTH);
        headerFieldsPanel.setOpaque(false);
        headerFieldsPanel.setLayout(new java.awt.GridBagLayout());

        serviceNoLabel.setText("No. Servis");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(serviceNoLabel, gridBagConstraints);
        serviceNoField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(serviceNoField, gridBagConstraints);
        serviceDateLabel.setText("Tanggal (yyyy-MM-dd)");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=2; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(serviceDateLabel, gridBagConstraints);
        serviceDateField.setColumns(14);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=3; gridBagConstraints.gridy=0; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(serviceDateField, gridBagConstraints);
        customerCodeLabel.setText("Kode Pelanggan");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(customerCodeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(customerCodeCombo, gridBagConstraints);
        vehicleLabel.setText("Kendaraan");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=2; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(vehicleLabel, gridBagConstraints);
        vehicleField.setColumns(18);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=3; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(vehicleField, gridBagConstraints);
        complaintLabel.setText("Keluhan");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(complaintLabel, gridBagConstraints);
        complaintField.setColumns(22);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(complaintField, gridBagConstraints);
        mechanicLabel.setText("Mekanik");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=2; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(mechanicLabel, gridBagConstraints);
        mechanicField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=3; gridBagConstraints.gridy=2; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(mechanicField, gridBagConstraints);
        statusLabel.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=0; gridBagConstraints.gridy=3; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(statusLabel, gridBagConstraints);
        statusField.setColumns(12);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=3; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(statusField, gridBagConstraints);

        headerPanel.add(headerFieldsPanel, java.awt.BorderLayout.CENTER);

        detailPanel.setBackground(new java.awt.Color(255, 255, 255));
        detailPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        detailPanel.setLayout(new java.awt.BorderLayout(10, 10));
        itemInputPanel.setOpaque(false);
        itemInputPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc;

        detailTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        detailTitleLabel.setText("Detail Jasa dan Sparepart Servis");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=java.awt.GridBagConstraints.REMAINDER; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(0,4,8,4);
        itemInputPanel.add(detailTitleLabel, gbc);

        itemTypeLabel.setText("Jenis");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=1; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemTypeLabel, gbc);
        itemTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"JASA", "PART"}));
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=1; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemTypeCombo, gbc);

        itemCodeLabel.setText("Kode");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=2; gbc.gridy=1; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemCodeLabel, gbc);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=0.5; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemCodeCombo, gbc);

        itemDescLabel.setText("Deskripsi");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=1; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemDescLabel, gbc);
        itemDescriptionField.setColumns(20);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=5; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=1.0; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemDescriptionField, gbc);

        itemQtyLabel.setText("Qty");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=0; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemQtyLabel, gbc);
        itemQtyField.setColumns(6);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=1; gbc.gridy=2; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemQtyField, gbc);

        itemPriceLabel.setText("Harga Satuan");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=2; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemPriceLabel, gbc);
        itemPriceField.setColumns(10);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=2; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=0.5; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemPriceField, gbc);

        itemSubtotalLabel.setText("Subtotal");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=3; gbc.anchor=java.awt.GridBagConstraints.EAST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemSubtotalLabel, gbc);
        itemSubtotalValue.setText("Rp 0");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=5; gbc.gridy=3; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemSubtotalValue, gbc);

        addItemButton.setText("Tambah Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=2; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(addItemButton, gbc);
        removeItemButton.setText("Hapus Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=5; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(removeItemButton, gbc);

        detailPanel.add(itemInputPanel, java.awt.BorderLayout.NORTH);
        detailScrollPane.setViewportView(detailTable);
        detailPanel.add(detailScrollPane, java.awt.BorderLayout.CENTER);

        footerPanel.setOpaque(false);
        footerPanel.setLayout(new java.awt.BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        totalTextLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        totalTextLabel.setText("Total Servis:");
        totalPanel.add(totalTextLabel);
        totalValue.setFont(new java.awt.Font("Segoe UI", 1, 20));
        totalValue.setText("Rp 0");
        totalPanel.add(totalValue);
        footerPanel.add(totalPanel, java.awt.BorderLayout.NORTH);
        footerActionsPanel.setOpaque(false);
        footerActionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        newButton.setText("Transaksi Baru"); footerActionsPanel.add(newButton);
        saveButton.setText("Simpan Transaksi"); footerActionsPanel.add(saveButton);
        footerPanel.add(footerActionsPanel, java.awt.BorderLayout.SOUTH);

        // --- History panel with status update ---
        historyPanel = new javax.swing.JPanel();
        historyPanel.setBackground(new java.awt.Color(255, 255, 255));
        historyPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 222, 230)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        historyPanel.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel historyHeaderPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 0));
        historyHeaderPanel.setOpaque(false);
        javax.swing.JLabel historyTitle = new javax.swing.JLabel("Riwayat Transaksi Servis");
        historyTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        historyHeaderPanel.add(historyTitle, java.awt.BorderLayout.WEST);

        javax.swing.JPanel statusUpdatePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        statusUpdatePanel.setOpaque(false);
        statusUpdatePanel.add(new javax.swing.JLabel("Ubah Status:"));
        statusUpdateCombo = new javax.swing.JComboBox<>(new String[]{"MASUK", "PROSES", "SELESAI", "DIBATALKAN"});
        styleComboBox(statusUpdateCombo);
        statusUpdatePanel.add(statusUpdateCombo);
        updateStatusButton = new javax.swing.JButton("Update Status");
        stylePrimaryButton(updateStatusButton);
        statusUpdatePanel.add(updateStatusButton);
        historyHeaderPanel.add(statusUpdatePanel, java.awt.BorderLayout.EAST);
        historyPanel.add(historyHeaderPanel, java.awt.BorderLayout.NORTH);

        historyModel = new DefaultTableModel(
                new String[]{"No Servis", "Tanggal", "Pelanggan", "Kendaraan", "Status", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new javax.swing.JTable(historyModel);
        UiTheme.styleTable(historyTable);
        javax.swing.JScrollPane historyScroll = new javax.swing.JScrollPane(historyTable);
        historyPanel.add(historyScroll, java.awt.BorderLayout.CENTER);

        // --- JTabbedPane Structure ---
        javax.swing.JTabbedPane tabPane = new javax.swing.JTabbedPane();
        tabPane.setFont(new java.awt.Font("Segoe UI", 0, 14));

        javax.swing.JPanel mainInputPanel = new javax.swing.JPanel(new java.awt.BorderLayout(14, 14));
        mainInputPanel.setOpaque(false);
        mainInputPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainInputPanel.add(detailPanel, java.awt.BorderLayout.CENTER);
        mainInputPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);

        tabPane.addTab("Input Transaksi Baru", mainInputPanel);
        tabPane.addTab("Riwayat & Update Status", historyPanel);

        add(tabPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemButton;
    private javax.swing.JTextField complaintField;
    private javax.swing.JLabel complaintLabel;
    private javax.swing.JComboBox<String> customerCodeCombo;
    private javax.swing.JLabel customerCodeLabel;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JScrollPane detailScrollPane;
    private javax.swing.JTable detailTable;
    private javax.swing.JLabel detailTitleLabel;
    private javax.swing.JPanel footerActionsPanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerFieldsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTitleLabel;
    private javax.swing.JComboBox<String> itemCodeCombo;
    private javax.swing.JLabel itemCodeLabel;
    private javax.swing.JLabel itemDescLabel;
    private javax.swing.JTextField itemDescriptionField;
    private javax.swing.JPanel itemInputPanel;
    private javax.swing.JTextField itemPriceField;
    private javax.swing.JLabel itemPriceLabel;
    private javax.swing.JTextField itemQtyField;
    private javax.swing.JLabel itemQtyLabel;
    private javax.swing.JLabel itemSubtotalLabel;
    private javax.swing.JLabel itemSubtotalValue;
    private javax.swing.JComboBox<String> itemTypeCombo;
    private javax.swing.JLabel itemTypeLabel;
    private javax.swing.JTextField mechanicField;
    private javax.swing.JLabel mechanicLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JButton removeItemButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField serviceDateField;
    private javax.swing.JLabel serviceDateLabel;
    private javax.swing.JTextField serviceNoField;
    private javax.swing.JLabel serviceNoLabel;
    private javax.swing.JTextField statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel totalTextLabel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JLabel totalValue;
    private javax.swing.JTextField vehicleField;
    private javax.swing.JLabel vehicleLabel;
    // End of variables declaration//GEN-END:variables

    // History / status update components (created in initComponents programmatically)
    private javax.swing.JPanel historyPanel;
    private javax.swing.JTable historyTable;
    private DefaultTableModel historyModel;
    private javax.swing.JComboBox<String> statusUpdateCombo;
    private javax.swing.JButton updateStatusButton;
}
