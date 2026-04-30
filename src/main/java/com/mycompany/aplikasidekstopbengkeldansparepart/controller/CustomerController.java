package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.CustomerPanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CustomerController {

    private static final String CUSTOMER_PREFIX = "PLG";

    private final CustomerPanelView view;
    private final CustomerDao dao;

    public CustomerController(CustomerPanelView view, CustomerDao dao) {
        this.view = view;
        this.dao = dao;

        bindActions();
        loadData();
        prepareNewForm();
    }

    private void bindActions() {
        view.addNewListener(e -> prepareNewForm());
        view.addSaveListener(e -> handleSave());
        view.addDeleteListener(e -> handleDelete());
        view.addRefreshListener(e -> loadData());
        view.addSearchListener((SimpleDocumentListener) event -> view.applySearchFilter(view.getSearchText()));
    }

    private void prepareNewForm() {
        view.clearForm();
        try {
            String code = dao.getNextCustomerCode(CUSTOMER_PREFIX, CodeGenerator.currentYearMonth());
            view.setCustomerCode(code);
            view.focusNameField();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal generate kode pelanggan: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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

            prepareNewForm();
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
