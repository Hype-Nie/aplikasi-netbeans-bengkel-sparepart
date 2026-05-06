package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SaleTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.ReceiptPrinter;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.TransactionDetailDialog;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.SaleTransactionPanelView;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SaleTransactionController {

    private static final String SALE_PREFIX = "SAL";

    private final SaleTransactionPanelView view;
    private final SaleTransactionDao dao;
    private final CustomerDao customerDao = new CustomerDao();
    private final SparepartDao sparepartDao = new SparepartDao();
    private final int adminId;
    private final DashboardController dashboardController;

    public SaleTransactionController(
            SaleTransactionPanelView view, SaleTransactionDao dao,
            int adminId, DashboardController dashboardController) {
        this.view = view;
        this.dao = dao;
        this.adminId = adminId;
        this.dashboardController = dashboardController;
        bindActions();
        loadReferenceData();
        prepareNewForm();
        loadHistory();
    }

    private void bindActions() {
        view.addAddItemListener(e -> handleAddItem());
        view.addRemoveItemListener(e -> handleRemoveItem());
        view.addSaveListener(e -> handleSave());
        view.addNewListener(e -> prepareNewForm());
        view.addHistoryClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleHistoryClick();
            }
        });
    }

    public void loadReferenceData() {
        try {
            view.setCustomerOptions(customerDao.findAll());
            view.setSparepartOptions(sparepartDao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data referensi: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prepareNewForm() {
        view.clearTransactionForm();
        try {
            String saleNo = dao.getNextSaleNo(SALE_PREFIX, CodeGenerator.currentYearMonth());
            view.setSaleNo(saleNo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal generate nomor penjualan: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddItem() {
        String partCode = view.getPartCode();
        String partName = view.getPartName();
        int sparepartId = view.getSelectedSparepartId();
        int qty = view.getItemQty();
        BigDecimal price = view.getUnitPrice();

        if (partCode.isBlank() || partName.isBlank() || sparepartId < 0) {
            JOptionPane.showMessageDialog(view, "Pilih sparepart terlebih dahulu.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (qty <= 0) {
            JOptionPane.showMessageDialog(view, "Qty harus lebih dari 0.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(view, "Harga harus lebih dari 0.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SaleItem item = new SaleItem(sparepartId, partCode, partName, qty, price);
        view.addDetailRow(item);
        view.clearItemInput();
        refreshTotal();
    }

    private void handleRemoveItem() {
        view.removeSelectedDetailRow();
        refreshTotal();
    }

    private void handleSave() {
        String saleNo = view.getSaleNo();
        String saleDateText = view.getSaleDateText();

        if (saleNo.isBlank()) {
            JOptionPane.showMessageDialog(view, "No. penjualan wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (view.getDetailCount() == 0) {
            JOptionPane.showMessageDialog(view, "Detail item penjualan masih kosong.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BigDecimal total = view.calculateCurrentTotal();
            String customerCode = view.getCustomerCode();
            SaleTransaction transaction = new SaleTransaction(
                    saleNo, DateUtil.parse(saleDateText),
                    customerCode.isBlank() ? null : customerCode,
                    view.getPaymentMethod().isBlank() ? "Tunai" : view.getPaymentMethod(),
                    total);

            List<SaleItem> items = view.getDetailItems();
            dao.save(transaction, items, adminId);

            // Prompt to print receipt
            int printChoice = JOptionPane.showConfirmDialog(view,
                    "Transaksi penjualan berhasil disimpan.\nCetak struk?",
                    "Cetak Struk", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (printChoice == JOptionPane.YES_OPTION) {
                String customerName = resolveCustomerName(customerCode);
                ReceiptPrinter.printSaleReceipt(transaction, items, customerName);
            }

            prepareNewForm();
            loadHistory();
            dashboardController.loadData();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan transaksi penjualan: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String resolveCustomerName(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) return null;
        try {
            for (Customer c : customerDao.findAll()) {
                if (customerCode.equals(c.getCustomerCode())) return c.getName();
            }
        } catch (SQLException ignored) { }
        return customerCode;
    }

    private void handleHistoryClick() {
        String saleNo = view.getSelectedHistorySaleNo();
        if (saleNo == null) return;
        try {
            SaleTransaction tx = dao.findByNo(saleNo);
            if (tx == null) return;
            List<SaleItem> items = dao.findItemsByNo(saleNo);
            String customerName = resolveCustomerName(tx.getCustomerCode());
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(view);
            TransactionDetailDialog.showSaleDetail(parentFrame, tx, items, customerName);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat detail transaksi: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory() {
        try { view.setHistoryRows(dao.findAllSummary()); } catch (SQLException ex) { /* silent */ }
    }

    private void refreshTotal() {
        view.setTotalValue(view.calculateCurrentTotal());
    }
}
