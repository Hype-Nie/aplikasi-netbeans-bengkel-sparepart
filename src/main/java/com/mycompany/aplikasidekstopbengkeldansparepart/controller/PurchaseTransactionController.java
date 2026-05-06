package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.PurchaseTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SupplierDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.ReceiptPrinter;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.TransactionDetailDialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
        loadHistory();
    }

    private void bindActions() {
        view.addAddItemListener(e -> handleAddItem());
        view.addRemoveItemListener(e -> handleRemoveItem());
        view.addSaveListener(e -> handleSave());
        view.addNewListener(e -> prepareNewForm());
        view.addUpdateStatusListener(e -> handleUpdateStatus());
        view.addHistoryClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleHistoryClick();
            }
        });
    }

    public void loadReferenceData() {
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

            // Prompt to print receipt
            int printChoice = JOptionPane.showConfirmDialog(view,
                    "Transaksi pembelian berhasil disimpan.\nCetak struk?",
                    "Cetak Struk", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (printChoice == JOptionPane.YES_OPTION) {
                String supplierName = resolveSupplierName(supplierCode);
                ReceiptPrinter.printPurchaseReceipt(transaction, items, supplierName);
            }

            prepareNewForm();
            loadHistory();
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

    private void handleUpdateStatus() {
        String purchaseNo = view.getSelectedHistoryPurchaseNo();
        if (purchaseNo == null) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi dari tabel riwayat terlebih dahulu.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newStatus = view.getNewStatusSelection();
        int confirm = JOptionPane.showConfirmDialog(view,
                "Ubah status transaksi " + purchaseNo + " menjadi \"" + newStatus + "\"?",
                "Konfirmasi Ubah Status", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.updateStatus(purchaseNo, newStatus);
            JOptionPane.showMessageDialog(view,
                    "Status transaksi " + purchaseNo + " berhasil diubah menjadi " + newStatus + ".");
            loadHistory();
            dashboardController.loadData();

            // Prompt to print receipt when status changes to SELESAI
            if ("SELESAI".equalsIgnoreCase(newStatus)) {
                int printChoice = JOptionPane.showConfirmDialog(view,
                        "Pembelian telah selesai. Cetak struk?",
                        "Cetak Struk", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (printChoice == JOptionPane.YES_OPTION) {
                    PurchaseTransaction tx = dao.findByNo(purchaseNo);
                    if (tx != null) {
                        List<PurchaseItem> items = dao.findItemsByNo(purchaseNo);
                        String supplierName = resolveSupplierName(tx.getSupplierCode());
                        ReceiptPrinter.printPurchaseReceipt(tx, items, supplierName);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengubah status: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String resolveSupplierName(String supplierCode) {
        if (supplierCode == null || supplierCode.isBlank()) return null;
        try {
            for (Supplier s : supplierDao.findAll()) {
                if (supplierCode.equals(s.getSupplierCode())) return s.getName();
            }
        } catch (SQLException ignored) { }
        return supplierCode;
    }

    private void handleHistoryClick() {
        String purchaseNo = view.getSelectedHistoryPurchaseNo();
        if (purchaseNo == null) return;
        try {
            PurchaseTransaction tx = dao.findByNo(purchaseNo);
            if (tx == null) return;
            List<PurchaseItem> items = dao.findItemsByNo(purchaseNo);
            String supplierName = resolveSupplierName(tx.getSupplierCode());
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(view);
            TransactionDetailDialog.showPurchaseDetail(parentFrame, tx, items, supplierName);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat detail transaksi: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory() {
        try { view.setHistoryRows(dao.findAllSummary()); } catch (SQLException ex) { /* silent */ }
    }
}
