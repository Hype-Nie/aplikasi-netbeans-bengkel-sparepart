package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.DashboardDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.PurchaseTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ReportDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SparepartDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SupplierDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Admin;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.AdminDashboardView;
import javax.swing.JOptionPane;

public class AdminDashboardController {

    private final AdminDashboardView view;

    private final DashboardController dashboardController;
    private final CustomerController customerController;
    private final SparepartController sparepartController;
    private final SupplierController supplierController;
    private final ServiceTransactionController serviceTransactionController;
    private final PurchaseTransactionController purchaseTransactionController;
    private final ReportController reportController;

    public AdminDashboardController(Admin admin) {
        this.view = new AdminDashboardView(admin.getFullName());

        CustomerDao customerDao = new CustomerDao();
        SparepartDao sparepartDao = new SparepartDao();
        SupplierDao supplierDao = new SupplierDao();

        this.dashboardController = new DashboardController(view.getDashboardPanel(), new DashboardDao());
        this.customerController = new CustomerController(view.getCustomerPanel(), customerDao);
        this.sparepartController = new SparepartController(view.getSparepartPanel(), sparepartDao);
        this.supplierController = new SupplierController(view.getSupplierPanel(), supplierDao);
        this.serviceTransactionController = new ServiceTransactionController(
                view.getServicePanel(),
                new ServiceTransactionDao(),
                admin.getId(),
                dashboardController
        );
        this.purchaseTransactionController = new PurchaseTransactionController(
                view.getPurchasePanel(),
                new PurchaseTransactionDao(),
                admin.getId(),
                dashboardController
        );
        this.reportController = new ReportController(view.getReportPanel(), new ReportDao());

        bindActions();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bindActions() {
        view.addLogoutListener(e -> handleLogout());
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Keluar dari dashboard admin?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            new LoginController().show();
        }
    }
}
