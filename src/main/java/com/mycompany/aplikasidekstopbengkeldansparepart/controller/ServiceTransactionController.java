package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.ServiceTransactionPanelView;

public class ServiceTransactionController {

    private final ServiceTransactionPanelView view;
    private final ServiceTransactionDao dao;
    private final int adminId;
    private final DashboardController dashboardController;

    public ServiceTransactionController(
            ServiceTransactionPanelView view,
            ServiceTransactionDao dao,
            int adminId,
            DashboardController dashboardController
    ) {
        this.view = view;
        this.dao = dao;
        this.adminId = adminId;
        this.dashboardController = dashboardController;

        bindActions();
        view.clearTransactionForm();
    }

    private void bindActions() {
        view.addAddItemListener(e -> handleAddItem());
        view.addRemoveItemListener(e -> handleRemoveItem());
        view.addSaveListener(e -> handleSave());
        view.addNewListener(e -> view.clearTransactionForm());
    }

    private void handleAddItem() {
        String type = view.getItemType();
        String code = view.getItemCode();
        String description = view.getItemDescription();
        int qty = view.getItemQty();
        BigDecimal price = view.getItemPrice();

        if (description.isBlank()) {
            JOptionPane.showMessageDialog(view, "Deskripsi item wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("PART".equalsIgnoreCase(type) && code.isBlank()) {
            JOptionPane.showMessageDialog(view, "Kode part wajib diisi untuk item tipe PART.", "Validasi", JOptionPane.WARNING_MESSAGE);
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

        ServiceItem item = new ServiceItem(type, code, description, qty, price);
        view.addDetailRow(item);
        view.clearItemInput();
        refreshTotal();
    }

    private void handleRemoveItem() {
        view.removeSelectedDetailRow();
        refreshTotal();
    }

    private void handleSave() {
        String serviceNo = view.getServiceNo();
        String serviceDateText = view.getServiceDateText();
        String customerCode = view.getCustomerCode();

        if (serviceNo.isBlank() || customerCode.isBlank()) {
            JOptionPane.showMessageDialog(
                    view,
                    "No. servis dan kode pelanggan wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (view.getDetailCount() == 0) {
            JOptionPane.showMessageDialog(
                    view,
                    "Detail item servis masih kosong.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            BigDecimal total = view.calculateCurrentTotal();
            ServiceTransaction transaction = new ServiceTransaction(
                    serviceNo,
                    DateUtil.parse(serviceDateText),
                    customerCode,
                    view.getVehicle(),
                    view.getComplaint(),
                    view.getMechanic(),
                    view.getStatus().isBlank() ? "MASUK" : view.getStatus(),
                    total
            );

            List<ServiceItem> items = view.getDetailItems();
            dao.save(transaction, items, adminId);

            JOptionPane.showMessageDialog(view, "Transaksi servis berhasil disimpan.");
            view.clearTransactionForm();
            dashboardController.loadData();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menyimpan transaksi servis: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTotal() {
        view.setTotalValue(view.calculateCurrentTotal());
    }
}
