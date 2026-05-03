package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.CustomerDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.DashboardDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.PurchaseTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ReportDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.SaleTransactionDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ServiceDao;
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
    private final ServiceMasterController serviceMasterController;
    private final ServiceTransactionController serviceTransactionController;
    private final PurchaseTransactionController purchaseTransactionController;
    private final SaleTransactionController saleTransactionController;
    private final ReportController reportController;

    public AdminDashboardController(Admin admin) {
        this.view = new AdminDashboardView(admin.getFullName());

        this.dashboardController = new DashboardController(view.getDashboardPanel(), new DashboardDao());
        this.customerController = new CustomerController(view.getCustomerPanel(), new CustomerDao());
        this.sparepartController = new SparepartController(view.getSparepartPanel(), new SparepartDao());
        this.supplierController = new SupplierController(view.getSupplierPanel(), new SupplierDao());
        this.serviceMasterController = new ServiceMasterController(view.getServiceMasterPanel(), new ServiceDao());
        this.serviceTransactionController = new ServiceTransactionController(
                view.getServicePanel(), new ServiceTransactionDao(), admin.getId(), dashboardController);
        this.purchaseTransactionController = new PurchaseTransactionController(
                view.getPurchasePanel(), new PurchaseTransactionDao(), admin.getId(), dashboardController);
        this.saleTransactionController = new SaleTransactionController(
                view.getSalePanel(), new SaleTransactionDao(), admin.getId(), dashboardController);
        this.reportController = new ReportController(view.getReportPanel(), new ReportDao());

        bindActions();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bindActions() {
        view.addLogoutListener(e -> handleLogout());

        view.addTabSelectListener(AdminDashboardView.KEY_SERVIS, () -> serviceTransactionController.loadReferenceData());
        view.addTabSelectListener(AdminDashboardView.KEY_PEMBELIAN, () -> purchaseTransactionController.loadReferenceData());
        view.addTabSelectListener(AdminDashboardView.KEY_PENJUALAN, () -> saleTransactionController.loadReferenceData());
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(view, "Keluar dari dashboard admin?",
                "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            new LoginController().show();
        }
    }
}
