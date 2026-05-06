package com.mycompany.aplikasidekstopbengkeldansparepart.view.panel;

import com.mycompany.aplikasidekstopbengkeldansparepart.UiTheme;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
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

public class PurchaseTransactionPanelView extends javax.swing.JPanel {

    private DefaultTableModel detailModel;
    private DefaultTableModel historyModel;
    private AutoCompleteComboBox.Controller supplierCodeController;
    private AutoCompleteComboBox.Controller partCodeController;
    private final Map<String, Sparepart> sparepartsByCode = new HashMap<>();

    private JTextField purchaseNoField, purchaseDateField, paymentMethodField, statusField;
    private JComboBox<String> supplierCodeCombo, partCodeCombo;
    private JTextField partNameField, qtyField, unitPriceField;
    private JLabel itemSubtotalValue, totalValue;
    private JButton addItemButton, removeItemButton, newButton, saveButton, updateStatusButton;
    private JTable detailTable, historyTable;
    private JComboBox<String> statusUpdateCombo;

    public PurchaseTransactionPanelView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UiTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // === HEADER ===
        JPanel headerPanel = createStyledPanel("Informasi Pembelian ke Supplier");
        JPanel hf = new JPanel(new GridBagLayout()); hf.setOpaque(false);
        GridBagConstraints g = gbc();

        purchaseNoField = tf(14); purchaseNoField.setEditable(false);
        purchaseDateField = tf(14); purchaseDateField.setText(DateUtil.todayText());
        supplierCodeCombo = new JComboBox<>(); styleComboBox(supplierCodeCombo);
        paymentMethodField = tf(16);
        statusField = tf(12); statusField.setText("DRAFT");

        addField(hf, g, 0, 0, "No. Pembelian", purchaseNoField);
        addField(hf, g, 2, 0, "Tanggal (yyyy-MM-dd)", purchaseDateField);
        addField(hf, g, 0, 1, "Kode Supplier", supplierCodeCombo);
        addField(hf, g, 2, 1, "Metode Bayar", paymentMethodField);
        addField(hf, g, 0, 2, "Status", statusField);
        headerPanel.add(hf, BorderLayout.CENTER);

        // === DETAIL ===
        JPanel detailPanel = createStyledPanel("Detail Item Pembelian");
        JPanel ip = new JPanel(new GridBagLayout()); ip.setOpaque(false);
        g = gbc();

        partCodeCombo = new JComboBox<>(); styleComboBox(partCodeCombo);
        partNameField = tf(20); qtyField = tf(6); qtyField.setText("1");
        unitPriceField = tf(10);
        itemSubtotalValue = new JLabel("Rp 0"); itemSubtotalValue.setForeground(UiTheme.PRIMARY_DARK);

        addItemButton = styledBtn("Tambah Item", false);
        removeItemButton = styledBtn("Hapus Item", false);

        g.gridx=0;g.gridy=1;g.weightx=0;g.fill=GridBagConstraints.NONE;g.anchor=GridBagConstraints.WEST;
        ip.add(new JLabel("Kode Part"), g);
        g.gridx=1;g.fill=GridBagConstraints.HORIZONTAL;g.weightx=0.3;
        ip.add(partCodeCombo, g);
        g.gridx=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Nama"), g);
        g.gridx=3;g.weightx=1.0;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(partNameField, g);

        g.gridx=0;g.gridy=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Qty"), g);
        g.gridx=1;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(qtyField, g);
        g.gridx=2;g.weightx=0;g.fill=GridBagConstraints.NONE;
        ip.add(new JLabel("Harga Satuan"), g);
        g.gridx=3;g.fill=GridBagConstraints.HORIZONTAL;
        ip.add(unitPriceField, g);
        g.gridx=4;g.fill=GridBagConstraints.NONE;
        ip.add(addItemButton, g);
        g.gridx=5;g.anchor=GridBagConstraints.WEST;
        ip.add(removeItemButton, g);

        g.gridx=4;g.gridy=3;g.anchor=GridBagConstraints.EAST;
        ip.add(new JLabel("Subtotal"), g);
        g.gridx=5;g.anchor=GridBagConstraints.WEST;
        ip.add(itemSubtotalValue, g);

        detailPanel.add(ip, BorderLayout.NORTH);

        detailModel = new DefaultTableModel(
                new String[]{"Kode", "Nama Part", "Qty", "Harga", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        detailTable = new JTable(detailModel);
        UiTheme.styleTable(detailTable);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // === FOOTER ===
        JPanel footerPanel = new JPanel(new BorderLayout()); footerPanel.setOpaque(false);
        JPanel tp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); tp.setOpaque(false);
        JLabel tl = new JLabel("Total Pembelian:"); tl.setFont(new Font("Segoe UI",Font.BOLD,14)); tp.add(tl);
        totalValue = new JLabel("Rp 0"); totalValue.setFont(new Font("Segoe UI",Font.BOLD,20));
        totalValue.setForeground(UiTheme.PRIMARY_DARK); tp.add(totalValue);
        footerPanel.add(tp, BorderLayout.NORTH);
        JPanel ap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); ap.setOpaque(false);
        newButton = styledBtn("Transaksi Baru", false);
        saveButton = styledBtn("Simpan Pembelian", true);
        ap.add(newButton); ap.add(saveButton);
        footerPanel.add(ap, BorderLayout.SOUTH);

        // === HISTORY ===
        JPanel historyPanel = createStyledPanel(null);
        JPanel hh = new JPanel(new BorderLayout(10,0)); hh.setOpaque(false);
        JLabel ht = new JLabel("Riwayat Transaksi Pembelian"); ht.setFont(new Font("Segoe UI",Font.BOLD,14));
        hh.add(ht, BorderLayout.WEST);
        JPanel sup = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); sup.setOpaque(false);
        sup.add(new JLabel("Ubah Status:"));
        statusUpdateCombo = new JComboBox<>(new String[]{"DRAFT","PROSES","SELESAI","DIBATALKAN"});
        styleComboBox(statusUpdateCombo); sup.add(statusUpdateCombo);
        updateStatusButton = styledBtn("Update Status", true); sup.add(updateStatusButton);
        hh.add(sup, BorderLayout.EAST);
        historyPanel.add(hh, BorderLayout.NORTH);

        historyModel = new DefaultTableModel(
                new String[]{"No Pembelian","Tanggal","Supplier","Status","Total"}, 0) {
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
        supplierCodeController = AutoCompleteComboBox.attach(supplierCodeCombo);
        partCodeController = AutoCompleteComboBox.attach(partCodeCombo);
        
        partCodeCombo.addActionListener(e -> applySelectedPartToItemFields());
        qtyField.getDocument().addDocumentListener((SimpleDocumentListener) ev -> updateItemSubtotalPreview());
        unitPriceField.getDocument().addDocumentListener((SimpleDocumentListener) ev -> updateItemSubtotalPreview());
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
    public String getPurchaseNo() { return purchaseNoField.getText().trim(); }
    public String getPurchaseDateText() { return purchaseDateField.getText().trim(); }
    public String getSupplierCode() { return extractCode(supplierCodeController.getSelectedText()); }
    public String getPaymentMethod() { return paymentMethodField.getText().trim(); }
    public String getStatus() { return statusField.getText().trim(); }
    public String getPartCode() { return extractCode(partCodeController.getSelectedText()); }
    public String getPartName() { return partNameField.getText().trim(); }
    public int getItemQty() { try { return Integer.parseInt(qtyField.getText().trim()); } catch (Exception e) { return 0; } }
    public BigDecimal getUnitPrice() { return MoneyUtil.parse(unitPriceField.getText()); }

    // ===== PUBLIC SETTERS =====
    public void setPurchaseNo(String purchaseNo) { purchaseNoField.setText(purchaseNo == null ? "" : purchaseNo); }

    public void setSupplierOptions(List<Supplier> suppliers) {
        List<String> options = new ArrayList<>();
        for (Supplier s : suppliers) options.add(formatOption(s.getSupplierCode(), s.getName()));
        supplierCodeController.setItems(options);
    }

    public void setSparepartOptions(List<Sparepart> spareparts) {
        sparepartsByCode.clear();
        List<String> options = new ArrayList<>();
        for (Sparepart sp : spareparts) {
            sparepartsByCode.put(sp.getPartCode(), sp);
            options.add(formatOption(sp.getPartCode(), sp.getName()));
        }
        partCodeController.setItems(options);
    }

    public void clearItemInput() {
        partCodeController.setSelectedItem("");
        partNameField.setText(""); qtyField.setText("1"); unitPriceField.setText("");
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

    // --- History ---
    public String getSelectedHistoryPurchaseNo() {
        int sel = historyTable.getSelectedRow();
        return sel >= 0 ? String.valueOf(historyModel.getValueAt(sel, 0)) : null;
    }
    public String getNewStatusSelection() { return String.valueOf(statusUpdateCombo.getSelectedItem()); }
    
    public void setHistoryRows(List<Object[]> rows) {
        historyModel.setRowCount(0);
        for (Object[] row : rows) {
            Object[] dr = {row[0], row[1], row[2], row[3],
                row[4] instanceof BigDecimal ? MoneyUtil.format((BigDecimal) row[4]) : row[4]};
            historyModel.addRow(dr);
        }
    }

    // ===== INTERNAL =====
    private void applySelectedPartToItemFields() {
        String code = extractCode(partCodeController.getSelectedText());
        if (code.isBlank()) return;
        Sparepart sparepart = sparepartsByCode.get(code);
        if (sparepart == null) return;
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
