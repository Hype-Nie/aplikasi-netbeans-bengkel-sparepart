package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.PurchaseTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SupplierDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.PurchaseTransactionPanelView;

public class PurchaseTransactionController {

    private static final String PURCHASE_PREFIX = "PO";

    private final PurchaseTransactionPanelView view;
    private final PurchaseTransactionDao dao;
    private final SupplierDao supplierDao = new SupplierDao();
    private final SparepartDao sparepartDao = new SparepartDao();
    private final int adminId;
    private final DashboardController dashboardController;

    public PurchaseTransactionController(
            PurchaseTransactionPanelView view,
            PurchaseTransactionDao dao,
            int adminId,
            DashboardController dashboardController
    ) {
        this.view = view;
        this.dao = dao;
        this.adminId = adminId;
        this.dashboardController = dashboardController;

        bindActions();
        loadReferenceData();
        prepareNewForm();
    }

    private void bindActions() {
        view.addAddItemListener(e -> handleAddItem());
        view.addRemoveItemListener(e -> handleRemoveItem());
        view.addSaveListener(e -> handleSave());
        view.addNewListener(e -> prepareNewForm());
    }

    private void loadReferenceData() {
        try {
            view.setSupplierOptions(supplierDao.findAll());
            view.setSparepartOptions(sparepartDao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal memuat data referensi: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void prepareNewForm() {
        view.clearTransactionForm();
        try {
            String purchaseNo = dao.getNextPurchaseNo(PURCHASE_PREFIX, CodeGenerator.currentYearMonth());
            view.setPurchaseNo(purchaseNo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal generate nomor pembelian: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleAddItem() {
        String partCode = view.getPartCode();
        String partName = view.getPartName();
        int qty = view.getItemQty();
        BigDecimal price = view.getUnitPrice();

        if (partCode.isBlank() || partName.isBlank()) {
            JOptionPane.showMessageDialog(view, "Kode dan nama part wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
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

        PurchaseItem item = new PurchaseItem(partCode, partName, qty, price);
        view.addDetailRow(item);
        view.clearItemInput();
        refreshTotal();
    }

    private void handleRemoveItem() {
        view.removeSelectedDetailRow();
        refreshTotal();
    }

    private void handleSave() {
        String purchaseNo = view.getPurchaseNo();
        String purchaseDateText = view.getPurchaseDateText();
        String supplierCode = view.getSupplierCode();

        if (purchaseNo.isBlank() || supplierCode.isBlank()) {
            JOptionPane.showMessageDialog(
                    view,
                    "No. pembelian dan kode supplier wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (view.getDetailCount() == 0) {
            JOptionPane.showMessageDialog(
                    view,
                    "Detail item pembelian masih kosong.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            BigDecimal total = view.calculateCurrentTotal();
            PurchaseTransaction transaction = new PurchaseTransaction(
                    purchaseNo,
                    DateUtil.parse(purchaseDateText),
                    supplierCode,
                    view.getPaymentMethod().isBlank() ? "Transfer" : view.getPaymentMethod(),
                    view.getStatus().isBlank() ? "DRAFT" : view.getStatus(),
                    total
            );

            List<PurchaseItem> items = view.getDetailItems();
            dao.save(transaction, items, adminId);

            JOptionPane.showMessageDialog(view, "Transaksi pembelian berhasil disimpan.");
            prepareNewForm();
            dashboardController.loadData();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menyimpan transaksi pembelian: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTotal() {
        view.setTotalValue(view.calculateCurrentTotal());
    }
}
