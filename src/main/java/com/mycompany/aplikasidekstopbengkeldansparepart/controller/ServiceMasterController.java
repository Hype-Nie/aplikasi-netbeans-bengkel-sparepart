package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Service;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.SimpleDocumentListener;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.ServicePanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ServiceMasterController {

    private static final String SERVICE_PREFIX = "JS";
    private final ServicePanelView view;
    private final ServiceDao dao;

    public ServiceMasterController(ServicePanelView view, ServiceDao dao) {
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
            String code = dao.getNextServiceCode(SERVICE_PREFIX, CodeGenerator.currentYearMonth());
            view.setServiceCode(code);
            view.focusNameField();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal generate kode jasa: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSave() {
        Service service = view.getFormData();
        if (service.getServiceCode().isBlank() || service.getName().isBlank()) {
            JOptionPane.showMessageDialog(view, "Kode jasa dan nama jasa wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (service.getId() == null) {
                dao.insert(service);
                JOptionPane.showMessageDialog(view, "Data jasa berhasil ditambahkan.");
            } else {
                dao.update(service);
                JOptionPane.showMessageDialog(view, "Data jasa berhasil diperbarui.");
            }
            prepareNewForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan data jasa: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        Integer selectedId = view.getSelectedRowId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(view, "Pilih data jasa yang akan dihapus.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Yakin ingin menghapus data jasa ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            dao.deleteById(selectedId);
            view.clearForm();
            loadData();
            JOptionPane.showMessageDialog(view, "Data jasa berhasil dihapus.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menghapus data jasa: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        try {
            view.setTableData(dao.findAll());
            view.applySearchFilter(view.getSearchText());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data jasa: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
