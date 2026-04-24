package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.CustomerPanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CustomerController {

    private final CustomerPanelView view;
    private final CustomerDao dao;

    public CustomerController(CustomerPanelView view, CustomerDao dao) {
        this.view = view;
        this.dao = dao;

        bindActions();
        loadData();
    }

    private void bindActions() {
        view.addNewListener(e -> view.clearForm());
        view.addSaveListener(e -> handleSave());
        view.addEditListener(e -> handleEdit());
        view.addDeleteListener(e -> handleDelete());
        view.addRefreshListener(e -> loadData());
        view.addSearchListener((SimpleDocumentListener) event -> view.applySearchFilter(view.getSearchText()));
    }

    private void handleSave() {
        Customer customer = view.getFormData();

        if (customer.getCustomerCode().isBlank() || customer.getName().isBlank()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Kode pelanggan dan nama pelanggan wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            if (customer.getId() == null) {
                dao.insert(customer);
                JOptionPane.showMessageDialog(view, "Data pelanggan berhasil ditambahkan.");
            } else {
                dao.update(customer);
                JOptionPane.showMessageDialog(view, "Data pelanggan berhasil diperbarui.");
            }

            view.clearForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menyimpan data pelanggan: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleEdit() {
        Customer selectedCustomer = view.getSelectedRowAsCustomer();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(view, "Pilih data pelanggan dari tabel terlebih dahulu.");
            return;
        }

        view.setFormData(selectedCustomer);
    }

    private void handleDelete() {
        Integer selectedId = view.getSelectedRowId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(view, "Pilih data pelanggan yang akan dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin menghapus data pelanggan ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.deleteById(selectedId);
            view.clearForm();
            loadData();
            JOptionPane.showMessageDialog(view, "Data pelanggan berhasil dihapus.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menghapus data pelanggan: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadData() {
        try {
            view.setTableData(dao.findAll());
            view.applySearchFilter(view.getSearchText());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal memuat data pelanggan: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
