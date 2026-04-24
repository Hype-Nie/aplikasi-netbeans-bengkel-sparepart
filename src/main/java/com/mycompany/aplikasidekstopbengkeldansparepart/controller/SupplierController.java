package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SupplierDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.SupplierPanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class SupplierController {

    private final SupplierPanelView view;
    private final SupplierDao dao;

    public SupplierController(SupplierPanelView view, SupplierDao dao) {
        this.view = view;
        this.dao = dao;

        bindActions();
        loadData();
    }

    private void bindActions() {
        view.addNewListener(e -> view.clearForm());
        view.addSaveListener(e -> handleSave());
        view.addDeleteListener(e -> handleDelete());
        view.addRefreshListener(e -> loadData());
        view.addSearchListener((SimpleDocumentListener) event -> view.applySearchFilter(view.getSearchText()));
    }

    private void handleSave() {
        Supplier supplier = view.getFormData();

        if (supplier.getSupplierCode().isBlank() || supplier.getName().isBlank()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Kode supplier dan nama supplier wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            if (supplier.getId() == null) {
                dao.insert(supplier);
                JOptionPane.showMessageDialog(view, "Data supplier berhasil ditambahkan.");
            } else {
                dao.update(supplier);
                JOptionPane.showMessageDialog(view, "Data supplier berhasil diperbarui.");
            }

            view.clearForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menyimpan data supplier: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }



    private void handleDelete() {
        Integer selectedId = view.getSelectedRowId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(view, "Pilih data supplier yang akan dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin menghapus data supplier ini?",
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
            JOptionPane.showMessageDialog(view, "Data supplier berhasil dihapus.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menghapus data supplier: " + ex.getMessage(),
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
                    "Gagal memuat data supplier: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
