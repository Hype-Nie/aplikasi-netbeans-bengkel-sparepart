package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.SparepartPanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class SparepartController {

    private static final String SPAREPART_PREFIX = "SP";

    private final SparepartPanelView view;
    private final SparepartDao dao;

    public SparepartController(SparepartPanelView view, SparepartDao dao) {
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
            String code = dao.getNextPartCode(SPAREPART_PREFIX, CodeGenerator.currentYearMonth());
            view.setPartCode(code);
            view.focusNameField();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal generate kode sparepart: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleSave() {
        Sparepart sparepart = view.getFormData();

        if (sparepart.getPartCode().isBlank() || sparepart.getName().isBlank()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Kode part dan nama part wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            if (sparepart.getId() == null) {
                dao.insert(sparepart);
                JOptionPane.showMessageDialog(view, "Data sparepart berhasil ditambahkan.");
            } else {
                dao.update(sparepart);
                JOptionPane.showMessageDialog(view, "Data sparepart berhasil diperbarui.");
            }

            prepareNewForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menyimpan data sparepart: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }



    private void handleDelete() {
        Integer selectedId = view.getSelectedRowId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(view, "Pilih data sparepart yang akan dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin menghapus data sparepart ini?",
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
            JOptionPane.showMessageDialog(view, "Data sparepart berhasil dihapus.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal menghapus data sparepart: " + ex.getMessage(),
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
                    "Gagal memuat data sparepart: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
