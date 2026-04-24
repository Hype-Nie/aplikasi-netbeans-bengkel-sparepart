package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.MoneyUtil;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

public class ServiceTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;

    public ServiceTransactionPanelView() {
        initComponents();
        customInit();
    }

    private void customInit() {
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        styleTextField(serviceNoField); styleTextField(serviceDateField); styleTextField(customerCodeField);
        styleTextField(vehicleField); styleTextField(complaintField); styleTextField(mechanicField);
        styleTextField(statusField); styleTextField(itemCodeField); styleTextField(itemDescriptionField);
        styleTextField(itemQtyField); styleTextField(itemPriceField);
        styleSecondaryButton(addItemButton); styleSecondaryButton(removeItemButton);
        styleSecondaryButton(newButton); stylePrimaryButton(saveButton);
        totalValue.setForeground(UiTheme.PRIMARY_DARK);

        serviceDateField.setText(DateUtil.todayText());
        statusField.setText("MASUK");
        itemQtyField.setText("1");

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

    public String getServiceNo() { return serviceNoField.getText().trim(); }
    public String getServiceDateText() { return serviceDateField.getText().trim(); }
    public String getCustomerCode() { return customerCodeField.getText().trim(); }
    public String getVehicle() { return vehicleField.getText().trim(); }
    public String getComplaint() { return complaintField.getText().trim(); }
    public String getMechanic() { return mechanicField.getText().trim(); }
    public String getStatus() { return statusField.getText().trim(); }
    public String getItemType() { return String.valueOf(itemTypeCombo.getSelectedItem()); }
    public String getItemCode() { return itemCodeField.getText().trim(); }
    public String getItemDescription() { return itemDescriptionField.getText().trim(); }
    public int getItemQty() { try { return Integer.parseInt(itemQtyField.getText().trim()); } catch (Exception e) { return 0; } }
    public BigDecimal getItemPrice() { return MoneyUtil.parse(itemPriceField.getText()); }

    public void clearItemInput() {
        itemCodeField.setText(""); itemDescriptionField.setText(""); itemQtyField.setText("1");
        itemPriceField.setText(""); itemCodeField.requestFocus();
    }

    public void clearTransactionForm() {
        serviceNoField.setText(""); serviceDateField.setText(DateUtil.todayText());
        customerCodeField.setText(""); vehicleField.setText(""); complaintField.setText("");
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
        customerCodeField = new javax.swing.JTextField();
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
        itemCodeField = new javax.swing.JTextField();
        itemDescLabel = new javax.swing.JLabel();
        itemDescriptionField = new javax.swing.JTextField();
        itemQtyLabel = new javax.swing.JLabel();
        itemQtyField = new javax.swing.JTextField();
        itemPriceLabel = new javax.swing.JLabel();
        itemPriceField = new javax.swing.JTextField();
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
        customerCodeField.setColumns(16);
        gridBagConstraints = new java.awt.GridBagConstraints(); gridBagConstraints.gridx=1; gridBagConstraints.gridy=1; gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints.weightx=1.0; gridBagConstraints.insets=new java.awt.Insets(6,6,6,6);
        headerFieldsPanel.add(customerCodeField, gridBagConstraints);
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
        add(headerPanel, java.awt.BorderLayout.NORTH);

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
        itemCodeField.setColumns(10);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=1; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=0.5; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemCodeField, gbc);

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

        itemPriceLabel.setText("Harga");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=2; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemPriceLabel, gbc);
        itemPriceField.setColumns(10);
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=3; gbc.gridy=2; gbc.fill=java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx=0.5; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(itemPriceField, gbc);

        addItemButton.setText("Tambah Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=4; gbc.gridy=2; gbc.insets=new java.awt.Insets(4,4,4,4);
        itemInputPanel.add(addItemButton, gbc);
        removeItemButton.setText("Hapus Item");
        gbc = new java.awt.GridBagConstraints(); gbc.gridx=5; gbc.gridy=2; gbc.anchor=java.awt.GridBagConstraints.WEST; gbc.insets=new java.awt.Insets(4,4,4,4);
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
        add(footerPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemButton;
    private javax.swing.JTextField complaintField;
    private javax.swing.JLabel complaintLabel;
    private javax.swing.JTextField customerCodeField;
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
    private javax.swing.JTextField itemCodeField;
    private javax.swing.JLabel itemCodeLabel;
    private javax.swing.JLabel itemDescLabel;
    private javax.swing.JTextField itemDescriptionField;
    private javax.swing.JPanel itemInputPanel;
    private javax.swing.JTextField itemPriceField;
    private javax.swing.JLabel itemPriceLabel;
    private javax.swing.JTextField itemQtyField;
    private javax.swing.JLabel itemQtyLabel;
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
}
