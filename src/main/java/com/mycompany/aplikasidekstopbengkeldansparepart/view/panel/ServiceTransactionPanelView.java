package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Service;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ServiceTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;
    private DefaultTableModel historyModel;
    private AutoCompleteComboBox.Controller customerCodeController;
    private AutoCompleteComboBox.Controller itemCodeController;
    private final Map<String, Sparepart> sparepartsByCode = new HashMap<>();
    private final Map<String, Service> servicesByCode = new HashMap<>();

    private JTextField serviceNoField, serviceDateField, vehicleField, complaintField, mechanicField, statusField;
    private JComboBox<String> customerCodeCombo, itemTypeCombo, itemCodeCombo;
    private JTextField itemDescriptionField, itemQtyField, itemPriceField;
    private JLabel itemSubtotalValue, totalValue;
    private JButton addItemButton, removeItemButton, newButton, saveButton, updateStatusButton;
    private JTable detailTable, historyTable;
    private JComboBox<String> statusUpdateCombo;

    public ServiceTransactionPanelView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // === HEADER ===
        JPanel headerPanel = createStyledPanel("Informasi Servis Masuk");
        JPanel hf = new JPanel(new GridBagLayout()); hf.setOpaque(false);
        GridBagConstraints g = gbc();

        serviceNoField = tf(14); serviceNoField.setEditable(false);
        serviceDateField = tf(14); serviceDateField.setText(DateUtil.todayText());
        customerCodeCombo = new JComboBox<>(); styleComboBox(customerCodeCombo);
        vehicleField = tf(18); complaintField = tf(22); mechanicField = tf(16);
        statusField = tf(12); statusField.setText("MASUK");

        addField(hf, g, 0, 0, "No. Servis", serviceNoField);
        addField(hf, g, 2, 0, "Tanggal (yyyy-MM-dd)", serviceDateField);
        addField(hf, g, 0, 1, "Kode Pelanggan", customerCodeCombo);
        addField(hf, g, 2, 1, "Kendaraan", vehicleField);
        addField(hf, g, 0, 2, "Keluhan", complaintField);
        addField(hf, g, 2, 2, "Mekanik", mechanicField);
        addField(hf, g, 0, 3, "Status", statusField);
        headerPanel.add(hf, BorderLayout.CENTER);

        // === DETAIL ===
        JPanel detailPanel = createStyledPanel("Detail Jasa dan Sparepart Servis");
        JPanel ip = new JPanel(new GridBagLayout()); ip.setOpaque(false);
        g = gbc();

        itemTypeCombo = new JComboBox<>(new String[]{"JASA", "PART"});
        styleComboBox(itemTypeCombo);
        itemCodeCombo = new JComboBox<>(); styleComboBox(itemCodeCombo);
        itemDescriptionField = tf(20); itemQtyField = tf(6); itemQtyField.setText("1");
        itemPriceField = tf(10);
        itemSubtotalValue = new JLabel("Rp 0"); itemSubtotalValue.setForeground(UiTheme.PRIMARY_DARK);

        addItemButton = styledBtn("Tambah Item", false);
        removeItemButton = styledBtn("Hapus Item", false);

        g.gridx=0;g.gridy=1;g.weightx=0;g.fill=GridBagConstraints.NONE;g.anchor=GridBagConstraints.WEST;
        ip.add(new JLabel("Jenis"), g);
        g.gridx=1;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(itemTypeCombo, g);
        g.gridx=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Kode"), g);
        g.gridx=3;g.weightx=0.5;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(itemCodeCombo, g);
        g.gridx=4;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Deskripsi"), g);
        g.gridx=5;g.weightx=1.0;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(itemDescriptionField, g);

        g.gridx=0;g.gridy=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Qty"), g);
        g.gridx=1;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(itemQtyField, g);
        g.gridx=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Harga Satuan"), g);
        g.gridx=3;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(itemPriceField, g);
        g.gridx=4;g.fill=GridBagConstraints.NONE;
        ip.add(addItemButton, g);
        g.gridx=5;g.anchor=GridBagConstraints.WEST;
        ip.add(removeItemButton, g);

        g.gridx=4;g.gridy=3;g.anchor=GridBagConstraints.EAST;
        ip.add(new JLabel("Subtotal"), g);
        g.gridx=5;g.anchor=GridBagConstraints.WEST;
        ip.add(itemSubtotalValue, g);

        detailPanel.add(ip, BorderLayout.NORTH);

        // Detail table: hidden cols for sparepartId(0) and serviceId(1)
        detailModel = new DefaultTableModel(
                new String[]{"SparepartID","ServiceID","Jenis","Kode","Deskripsi","Qty","Harga","Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        detailTable = new JTable(detailModel);
        UiTheme.styleTable(detailTable);
        hideColumn(detailTable, 0); hideColumn(detailTable, 1);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // === FOOTER ===
        JPanel footerPanel = new JPanel(new BorderLayout()); footerPanel.setOpaque(false);
        JPanel tp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); tp.setOpaque(false);
        JLabel tl = new JLabel("Total Servis:"); tl.setFont(new Font("Segoe UI",Font.BOLD,14)); tp.add(tl);
        totalValue = new JLabel("Rp 0"); totalValue.setFont(new Font("Segoe UI",Font.BOLD,20));
        totalValue.setForeground(UiTheme.PRIMARY_DARK); tp.add(totalValue);
        footerPanel.add(tp, BorderLayout.NORTH);
        JPanel ap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); ap.setOpaque(false);
        newButton = styledBtn("Transaksi Baru", false);
        saveButton = styledBtn("Simpan Transaksi", true);
        ap.add(newButton); ap.add(saveButton);
        footerPanel.add(ap, BorderLayout.SOUTH);

        // === HISTORY ===
        JPanel historyPanel = createStyledPanel(null);
        JPanel hh = new JPanel(new BorderLayout(10,0)); hh.setOpaque(false);
        JLabel ht = new JLabel("Riwayat Transaksi Servis"); ht.setFont(new Font("Segoe UI",Font.BOLD,14));
        hh.add(ht, BorderLayout.WEST);
        JPanel sup = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); sup.setOpaque(false);
        sup.add(new JLabel("Ubah Status:"));
        statusUpdateCombo = new JComboBox<>(new String[]{"MASUK","PROSES","SELESAI","DIBATALKAN"});
        styleComboBox(statusUpdateCombo); sup.add(statusUpdateCombo);
        updateStatusButton = styledBtn("Update Status", true); sup.add(updateStatusButton);
        hh.add(sup, BorderLayout.EAST);
        historyPanel.add(hh, BorderLayout.NORTH);

        historyModel = new DefaultTableModel(
                new String[]{"No Servis","Tanggal","Pelanggan","Kendaraan","Status","Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(historyModel);
        UiTheme.styleTable(historyTable);
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // === TABS ===
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPanel mainInput = new JPanel(new BorderLayout(14,14)); mainInput.setOpaque(false);
        mainInput.add(headerPanel, BorderLayout.NORTH);
        mainInput.add(detailPanel, BorderLayout.CENTER);
        mainInput.add(footerPanel, BorderLayout.SOUTH);
        tabPane.addTab("Input Transaksi Baru", mainInput);
        tabPane.addTab("Riwayat & Update Status", historyPanel);
        add(tabPane, BorderLayout.CENTER);

        // === WIRE EVENTS ===
        customerCodeController = AutoCompleteComboBox.attach(customerCodeCombo);
        itemCodeController = AutoCompleteComboBox.attach(itemCodeCombo);
        itemTypeCombo.addActionListener(e -> onItemTypeChanged());
        itemCodeCombo.addActionListener(e -> applySelectedCodeToFields());
        itemQtyField.getDocument().addDocumentListener((SimpleDocumentListener) ev -> updateItemSubtotalPreview());
        itemPriceField.getDocument().addDocumentListener((SimpleDocumentListener) ev -> updateItemSubtotalPreview());
        updateItemSubtotalPreview();
    }

    // ===== PUBLIC LISTENER METHODS =====
    public void addAddItemListener(ActionListener l) { addItemButton.addActionListener(l); }
    public void addRemoveItemListener(ActionListener l) { removeItemButton.addActionListener(l); }
    public void addSaveListener(ActionListener l) { saveButton.addActionListener(l); }
    public void addNewListener(ActionListener l) { newButton.addActionListener(l); }
    public void addUpdateStatusListener(ActionListener l) { updateStatusButton.addActionListener(l); }
    public void addHistoryClickListener(java.awt.event.MouseListener l) { historyTable.addMouseListener(l); }

    // ===== PUBLIC GETTERS =====
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

    /** Returns sparepart ID if type=PART, else null */
    public Integer getSelectedSparepartId() {
        if (!"PART".equalsIgnoreCase(getItemType())) return null;
        Sparepart sp = sparepartsByCode.get(getItemCode());
        return sp != null ? sp.getId() : null;
    }
    /** Returns service ID if type=JASA, else null */
    public Integer getSelectedServiceId() {
        if (!"JASA".equalsIgnoreCase(getItemType())) return null;
        Service svc = servicesByCode.get(getItemCode());
        return svc != null ? svc.getId() : null;
    }

    // ===== PUBLIC SETTERS =====
    public void setServiceNo(String serviceNo) { serviceNoField.setText(serviceNo == null ? "" : serviceNo); }

    public void setCustomerOptions(List<Customer> customers) {
        List<String> options = new ArrayList<>();
        for (Customer c : customers) options.add(formatOption(c.getCustomerCode(), c.getName()));
        customerCodeController.setItems(options);
    }

    public void setSparepartOptions(List<Sparepart> spareparts) {
        sparepartsByCode.clear();
        for (Sparepart sp : spareparts) sparepartsByCode.put(sp.getPartCode(), sp);
        onItemTypeChanged(); // refresh combo
    }

    public void setServiceOptions(List<Service> services) {
        servicesByCode.clear();
        for (Service svc : services) servicesByCode.put(svc.getServiceCode(), svc);
        onItemTypeChanged(); // refresh combo
    }

    public void clearItemInput() {
        itemCodeController.setSelectedItem("");
        itemDescriptionField.setText(""); itemQtyField.setText("1"); itemPriceField.setText("");
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
        detailModel.addRow(new Object[]{
            item.getSparepartId(), item.getServiceId(),
            item.getItemType(),
            resolveDisplayCode(item),
            item.getDescription(), item.getQty(),
            MoneyUtil.format(item.getPrice()), MoneyUtil.format(item.getSubtotal())
        });
    }

    public void removeSelectedDetailRow() {
        int sel = detailTable.getSelectedRow();
        if (sel >= 0) detailModel.removeRow(sel);
    }

    public int getDetailCount() { return detailModel.getRowCount(); }

    public List<ServiceItem> getDetailItems() {
        List<ServiceItem> items = new ArrayList<>();
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            Object spIdObj = detailModel.getValueAt(i, 0);
            Object svcIdObj = detailModel.getValueAt(i, 1);
            Integer sparepartId = spIdObj != null ? (Integer) spIdObj : null;
            Integer serviceId = svcIdObj != null ? (Integer) svcIdObj : null;
            String type = String.valueOf(detailModel.getValueAt(i, 2));
            String desc = String.valueOf(detailModel.getValueAt(i, 4));
            int qty = Integer.parseInt(String.valueOf(detailModel.getValueAt(i, 5)));
            BigDecimal price = MoneyUtil.parse(String.valueOf(detailModel.getValueAt(i, 6)));
            items.add(new ServiceItem(type, sparepartId, serviceId, desc, qty, price));
        }
        return items;
    }

    public BigDecimal calculateCurrentTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ServiceItem item : getDetailItems()) total = total.add(item.getSubtotal());
        return total;
    }

    public void setTotalValue(BigDecimal value) { totalValue.setText(MoneyUtil.format(value)); }

    // --- History ---
    public String getSelectedHistoryServiceNo() {
        int sel = historyTable.getSelectedRow();
        return sel >= 0 ? String.valueOf(historyModel.getValueAt(sel, 0)) : null;
    }
    public String getNewStatusSelection() { return String.valueOf(statusUpdateCombo.getSelectedItem()); }
    public void setHistoryRows(List<Object[]> rows) {
        historyModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] dr = {row[0], row[1], row[2], row[3], row[4],
                row[5] instanceof BigDecimal ? MoneyUtil.format((BigDecimal) row[5]) : row[5]};
            historyModel.addRow(dr);
        }
    }

    // ===== INTERNAL =====
    private void onItemTypeChanged() {
        String type = getItemType();
        List<String> options = new ArrayList<>();
        if ("PART".equalsIgnoreCase(type)) {
            for (Sparepart sp : sparepartsByCode.values())
                options.add(formatOption(sp.getPartCode(), sp.getName()));
        } else {
            for (Service svc : servicesByCode.values())
                options.add(formatOption(svc.getServiceCode(), svc.getName()));
        }
        itemCodeController.setItems(options);
        itemDescriptionField.setText(""); itemPriceField.setText("");
        updateItemSubtotalPreview();
    }

    private void applySelectedCodeToFields() {
        String code = extractCode(itemCodeController.getSelectedText());
        if (code.isBlank()) return;
        if ("PART".equalsIgnoreCase(getItemType())) {
            Sparepart sp = sparepartsByCode.get(code);
            if (sp != null) { itemDescriptionField.setText(sp.getName()); itemPriceField.setText(MoneyUtil.format(sp.getSellingPrice())); }
        } else {
            Service svc = servicesByCode.get(code);
            if (svc != null) { itemDescriptionField.setText(svc.getName()); itemPriceField.setText(MoneyUtil.format(svc.getPrice())); }
        }
        updateItemSubtotalPreview();
    }

    private void updateItemSubtotalPreview() {
        BigDecimal price = MoneyUtil.parse(itemPriceField.getText());
        int qty = getItemQty();
        itemSubtotalValue.setText(MoneyUtil.format(price.multiply(BigDecimal.valueOf(qty))));
    }

    private String resolveDisplayCode(ServiceItem item) {
        if ("PART".equalsIgnoreCase(item.getItemType()) && item.getSparepartId() != null) {
            for (Sparepart sp : sparepartsByCode.values())
                if (sp.getId().equals(item.getSparepartId())) return sp.getPartCode();
        }
        if ("JASA".equalsIgnoreCase(item.getItemType()) && item.getServiceId() != null) {
            for (Service svc : servicesByCode.values())
                if (svc.getId().equals(item.getServiceId())) return svc.getServiceCode();
        }
        return "";
    }

    private static String formatOption(String code, String name) {
        return (code == null ? "" : code.trim()) + " - " + (name == null ? "" : name.trim());
    }
    private static String extractCode(String display) {
        if (display == null) return "";
        String t = display.trim(); int s = t.indexOf(" - ");
        return s > 0 ? t.substring(0, s).trim() : t;
    }

    // ===== UI HELPERS =====
    private JPanel createStyledPanel(String title) {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(12,12,12,12)));
        if (title != null) { JLabel l = new JLabel(title); l.setFont(new Font("Segoe UI",Font.BOLD,14)); p.add(l, BorderLayout.NORTH); }
        return p;
    }
    private JTextField tf(int cols) { JTextField f = new JTextField(cols); styleTextField(f); return f; }
    private GridBagConstraints gbc() { GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(4,4,4,4); return g; }
    private void addField(JPanel p, GridBagConstraints g, int x, int y, String label, java.awt.Component field) {
        g.gridx=x; g.gridy=y; g.weightx=0; g.fill=GridBagConstraints.HORIZONTAL; p.add(new JLabel(label), g);
        g.gridx=x+1; g.weightx=1.0; p.add(field, g);
    }
    private JButton styledBtn(String text, boolean primary) {
        JButton b = new JButton(text);
        if (primary) stylePrimaryButton(b); else styleSecondaryButton(b);
        return b;
    }
    private void hideColumn(JTable t, int col) {
        t.getColumnModel().getColumn(col).setMinWidth(0);
        t.getColumnModel().getColumn(col).setMaxWidth(0);
        t.getColumnModel().getColumn(col).setWidth(0);
    }
    private void styleTextField(JTextField f) {
        f.setFont(UiTheme.FONT_BODY); f.setMargin(UiTheme.FIELD_INSETS);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER), BorderFactory.createEmptyBorder(2,2,2,2)));
    }
    private void styleComboBox(JComboBox<?> c) {
        c.setFont(UiTheme.FONT_BODY);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.BORDER), BorderFactory.createEmptyBorder(2,2,2,2)));
    }
    private void stylePrimaryButton(JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(Color.WHITE); b.setBackground(UiTheme.PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }
    private void styleSecondaryButton(JButton b) {
        b.setFont(UiTheme.FONT_BODY); b.setForeground(UiTheme.TEXT_PRIMARY); b.setBackground(UiTheme.SIDEBAR_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10,16,10,16)); b.setFocusPainted(false); b.setContentAreaFilled(false); b.setOpaque(true);
    }
}
