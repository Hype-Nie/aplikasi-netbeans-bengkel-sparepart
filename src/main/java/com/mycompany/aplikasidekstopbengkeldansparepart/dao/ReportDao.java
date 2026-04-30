package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    public List<Object[]> findServiceTransactions(String dateFrom, String dateTo) throws SQLException {
        String sql = """
                SELECT st.service_no, st.service_date, c.name AS customer_name,
                       st.vehicle, st.mechanic, st.status, st.total
                FROM service_transactions st
                JOIN customers c ON c.id = st.customer_id
                WHERE st.service_date BETWEEN ? AND ?
                ORDER BY st.service_date DESC, st.id DESC
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("service_no"),
                        rs.getDate("service_date").toLocalDate().toString(),
                        rs.getString("customer_name"),
                        rs.getString("vehicle"),
                        rs.getString("mechanic"),
                        rs.getString("status"),
                        rs.getBigDecimal("total")
                    });
                }
            }
        }
        return rows;
    }

    public List<Object[]> findPurchaseTransactions(String dateFrom, String dateTo) throws SQLException {
        String sql = """
                SELECT pt.purchase_no, pt.purchase_date, s.name AS supplier_name,
                       pt.payment_method, pt.status, pt.total
                FROM purchase_transactions pt
                JOIN suppliers s ON s.id = pt.supplier_id
                WHERE pt.purchase_date BETWEEN ? AND ?
                ORDER BY pt.purchase_date DESC, pt.id DESC
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("purchase_no"),
                        rs.getDate("purchase_date").toLocalDate().toString(),
                        rs.getString("supplier_name"),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getBigDecimal("total")
                    });
                }
            }
        }
        return rows;
    }

    public BigDecimal sumServiceTotal(String dateFrom, String dateTo) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM service_transactions WHERE service_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        }
    }

    public BigDecimal sumPurchaseTotal(String dateFrom, String dateTo) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM purchase_transactions WHERE purchase_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateFrom);
            stmt.setString(2, dateTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        }
    }
}
