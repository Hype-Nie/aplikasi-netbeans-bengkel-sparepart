package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.AdminDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Admin;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.LoginView;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    private final LoginView view;
    private final AdminDao adminDao;

    public LoginController() {
        this.view = new LoginView();
        this.adminDao = new AdminDao();
        bindActions();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bindActions() {
        view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isBlank() || password.isBlank()) {
            view.showWarning("Username dan password wajib diisi.");
            return;
        }

        try {
            Optional<Admin> authenticatedAdmin = adminDao.authenticate(username, password);

            if (authenticatedAdmin.isEmpty()) {
                view.showError("Login gagal. Cek username/password.");
                view.clearPassword();
                return;
            }

            AdminDashboardController dashboardController = new AdminDashboardController(authenticatedAdmin.get());
            dashboardController.show();
            view.dispose();
        } catch (SQLException ex) {
            view.showError("Tidak bisa terhubung ke database: " + ex.getMessage());
        }
    }
}
