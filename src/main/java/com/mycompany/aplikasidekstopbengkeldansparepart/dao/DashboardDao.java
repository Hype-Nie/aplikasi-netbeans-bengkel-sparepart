package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.DashboardSummary;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardDao {

    public DashboardSummary getSummary() throws SQLException {
        int activeCustomers = querySingleInt("SELECT COUNT(*) FROM customers");
        int totalStock = querySingleInt("SELECT COALESCE(SUM(stock), 0) FROM spareparts");
        int todayServices = querySingleInt(
                "SELECT COUNT(*) FROM service_transactions WHERE service_date = CURRENT_DATE"
        );
        int todaySales = querySingleInt(
                "SELECT COUNT(*) FROM sale_transactions WHERE sale_date = CURRENT_DATE"
        );
        int monthlyPurchases = querySingleInt(
                """
                SELECT COUNT(*)
                FROM purchase_transactions
                WHERE YEAR(purchase_date) = YEAR(CURRENT_DATE)
                  AND MONTH(purchase_date) = MONTH(CURRENT_DATE)
                """
        );
        BigDecimal serviceRevenue = querySingleDecimal(
                "SELECT COALESCE(SUM(total), 0) FROM service_transactions WHERE service_date = CURRENT_DATE"
        );
        BigDecimal saleRevenue = querySingleDecimal(
                "SELECT COALESCE(SUM(total), 0) FROM sale_transactions WHERE sale_date = CURRENT_DATE"
        );
        BigDecimal todayRevenue = serviceRevenue.add(saleRevenue);

        return new DashboardSummary(activeCustomers, totalStock, todayServices, todaySales, monthlyPurchases, todayRevenue);
    }

    public List<Object[]> findRecentServices(int limit) throws SQLException {
        String sql = """
                SELECT st.service_no, c.name AS customer_name, st.vehicle, st.status, st.total
                FROM service_transactions st
                JOIN customers c ON c.id = st.customer_id
                ORDER BY st.id DESC
                LIMIT ?
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rows.add(new Object[]{
                        resultSet.getString("service_no"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("vehicle"),
                        resultSet.getString("status"),
                        resultSet.getBigDecimal("total")
                    });
                }
            }
        }

        return rows;
    }

    public List<Object[]> findRecentSales(int limit) throws SQLException {
        String sql = """
                SELECT st.sale_no, COALESCE(c.name, 'Umum') AS customer_name,
                       st.payment_method, st.total
                FROM sale_transactions st
                LEFT JOIN customers c ON c.id = st.customer_id
                ORDER BY st.id DESC
                LIMIT ?
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rows.add(new Object[]{
                        resultSet.getString("sale_no"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("payment_method"),
                        resultSet.getBigDecimal("total")
                    });
                }
            }
        }

        return rows;
    }

    public List<Object[]> findLowStockSpareparts(int limit) throws SQLException {
        String sql = """
                SELECT part_code, name, stock
                FROM spareparts
                WHERE stock <= min_stock
                ORDER BY stock ASC, id ASC
                LIMIT ?
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rows.add(new Object[]{
                        resultSet.getString("part_code"),
                        resultSet.getString("name"),
                        resultSet.getInt("stock")
                    });
                }
            }
        }

        return rows;
    }

    private int querySingleInt(String sql) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    private BigDecimal querySingleDecimal(String sql) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }
}
