package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.DateUtil;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.ServiceTransactionPanelView;

public class ServiceTransactionController {

    private static final String SERVICE_PREFIX = "SRV";

    private final ServiceTransactionPanelView view;
    private final ServiceTransactionDao dao;
    private final CustomerDao customerDao = new CustomerDao();
    private final SparepartDao sparepartDao = new SparepartDao();
    private final ServiceDao serviceDao = new ServiceDao();
    private final int adminId;
    private final DashboardController dashboardController;

    public ServiceTransactionController(
            ServiceTransactionPanelView view, ServiceTransactionDao dao,
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
        view.addUpdateStatusListener(e -> handleUpdateStatus());
    }

    private void loadReferenceData() {
        try {
            view.setCustomerOptions(customerDao.findAll());
            view.setSparepartOptions(sparepartDao.findAll());
            view.setServiceOptions(serviceDao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data referensi: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prepareNewForm() {
        view.clearTransactionForm();
        try {
            String serviceNo = dao.getNextServiceNo(SERVICE_PREFIX, CodeGenerator.currentYearMonth());
            view.setServiceNo(serviceNo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal generate nomor servis: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddItem() {
        String type = view.getItemType();
        String description = view.getItemDescription();
        int qty = view.getItemQty();
        BigDecimal price = view.getItemPrice();

        if (description.isBlank()) {
            JOptionPane.showMessageDialog(view, "Deskripsi item wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
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

        Integer sparepartId = view.getSelectedSparepartId();
        Integer serviceId = view.getSelectedServiceId();

        if ("PART".equalsIgnoreCase(type) && sparepartId == null) {
            JOptionPane.showMessageDialog(view, "Pilih sparepart untuk item tipe PART.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ("JASA".equalsIgnoreCase(type) && serviceId == null) {
            JOptionPane.showMessageDialog(view, "Pilih jasa untuk item tipe JASA.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ServiceItem item = new ServiceItem(type, sparepartId, serviceId, description, qty, price);
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
            JOptionPane.showMessageDialog(view, "No. servis dan kode pelanggan wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (view.getDetailCount() == 0) {
            JOptionPane.showMessageDialog(view, "Detail item servis masih kosong.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BigDecimal total = view.calculateCurrentTotal();
            ServiceTransaction transaction = new ServiceTransaction(
                    serviceNo, DateUtil.parse(serviceDateText), customerCode,
                    view.getVehicle(), view.getComplaint(), view.getMechanic(),
                    view.getStatus().isBlank() ? "MASUK" : view.getStatus(), total);

            List<ServiceItem> items = view.getDetailItems();
            dao.save(transaction, items, adminId);

            JOptionPane.showMessageDialog(view, "Transaksi servis berhasil disimpan.");
            prepareNewForm();
            loadHistory();
            dashboardController.loadData();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan transaksi servis: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateStatus() {
        String serviceNo = view.getSelectedHistoryServiceNo();
        if (serviceNo == null) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi dari tabel riwayat terlebih dahulu.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newStatus = view.getNewStatusSelection();
        int confirm = JOptionPane.showConfirmDialog(view,
                "Ubah status transaksi " + serviceNo + " menjadi \"" + newStatus + "\"?",
                "Konfirmasi Ubah Status", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.updateStatus(serviceNo, newStatus);
            JOptionPane.showMessageDialog(view,
                    "Status transaksi " + serviceNo + " berhasil diubah menjadi " + newStatus + ".");
            loadHistory();
            dashboardController.loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengubah status: " + ex.getMessage(),
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
