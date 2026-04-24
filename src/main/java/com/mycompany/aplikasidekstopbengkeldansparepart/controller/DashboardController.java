package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.DashboardDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.DashboardSummary;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.DashboardPanelView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DashboardController {

    private final DashboardPanelView view;
    private final DashboardDao dao;

    public DashboardController(DashboardPanelView view, DashboardDao dao) {
        this.view = view;
        this.dao = dao;

        bindActions();
        loadData();
    }

    public void loadData() {
        try {
            DashboardSummary summary = dao.getSummary();
            view.setSummary(summary);
            view.setRecentServiceRows(dao.findRecentServices(10));
            view.setLowStockRows(dao.findLowStockSpareparts(10));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Gagal memuat data dashboard: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void bindActions() {
        view.addRefreshListener(e -> loadData());
    }
}
