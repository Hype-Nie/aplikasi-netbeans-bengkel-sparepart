package com.mycompany.aplikasidekstopbengkeldansparepart.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL JDBC driver tidak ditemukan: " + ex.getMessage());
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getJdbcUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
        );
    }
}
