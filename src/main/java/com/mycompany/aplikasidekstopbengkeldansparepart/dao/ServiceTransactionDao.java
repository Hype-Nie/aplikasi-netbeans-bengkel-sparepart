package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceTransactionDao {

    private final CustomerDao customerDao = new CustomerDao();

    public String getNextServiceNo(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT service_no
                FROM service_transactions
                WHERE service_no LIKE ?
                ORDER BY service_no DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("service_no");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public void save(ServiceTransaction transaction, List<ServiceItem> items, int adminId) throws SQLException {
        String insertHeaderSql = """
                INSERT INTO service_transactions (
                    service_no, service_date, customer_id, vehicle, complaint,
                    mechanic, status, total, created_by
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String insertItemSql = """
                INSERT INTO service_transaction_items (
                    service_transaction_id, item_type, item_code, description,
                    qty, price, subtotal
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String reduceStockSql = """
                UPDATE spareparts
                SET stock = stock - ?
                WHERE part_code = ? AND stock >= ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int customerId = customerDao
                        .findIdByCode(transaction.getCustomerCode(), connection)
                        .orElseThrow(() -> new SQLException(
                                "Kode pelanggan tidak ditemukan: " + transaction.getCustomerCode()
                        ));

                int serviceTransactionId;
                try (PreparedStatement headerStatement = connection.prepareStatement(
                        insertHeaderSql,
                        Statement.RETURN_GENERATED_KEYS
                )) {
                    headerStatement.setString(1, transaction.getServiceNo());
                    headerStatement.setDate(2, Date.valueOf(transaction.getServiceDate()));
                    headerStatement.setInt(3, customerId);
                    headerStatement.setString(4, transaction.getVehicle());
                    headerStatement.setString(5, transaction.getComplaint());
                    headerStatement.setString(6, transaction.getMechanic());
                    headerStatement.setString(7, transaction.getStatus());
                    headerStatement.setBigDecimal(8, transaction.getTotal());
                    headerStatement.setInt(9, adminId);
                    headerStatement.executeUpdate();

                    try (ResultSet generatedKeys = headerStatement.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Gagal mengambil ID transaksi servis.");
                        }
                        serviceTransactionId = generatedKeys.getInt(1);
                    }
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql);
                     PreparedStatement stockStatement = connection.prepareStatement(reduceStockSql)) {

                    for (ServiceItem item : items) {
                        itemStatement.setInt(1, serviceTransactionId);
                        itemStatement.setString(2, item.getItemType());
                        itemStatement.setString(3, item.getItemCode());
                        itemStatement.setString(4, item.getDescription());
                        itemStatement.setInt(5, item.getQty());
                        itemStatement.setBigDecimal(6, item.getPrice());
                        itemStatement.setBigDecimal(7, item.getSubtotal());
                        itemStatement.addBatch();

                        if ("PART".equalsIgnoreCase(item.getItemType())) {
                            stockStatement.setInt(1, item.getQty());
                            stockStatement.setString(2, item.getItemCode());
                            stockStatement.setInt(3, item.getQty());

                            int updatedRows = stockStatement.executeUpdate();
                            if (updatedRows == 0) {
                                throw new SQLException("Stok part tidak cukup atau kode part tidak ditemukan: "
                                        + item.getItemCode());
                            }
                        }
                    }
                    itemStatement.executeBatch();
                }

                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void updateStatus(String serviceNo, String newStatus) throws SQLException {
        String sql = "UPDATE service_transactions SET status = ? WHERE service_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setString(2, serviceNo);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Nomor servis tidak ditemukan: " + serviceNo);
            }
        }
    }

    public List<Object[]> findAllSummary() throws SQLException {
        String sql = """
                SELECT st.service_no, st.service_date, c.name AS customer_name,
                       st.vehicle, st.status, st.total
                FROM service_transactions st
                JOIN customers c ON c.id = st.customer_id
                ORDER BY st.id DESC
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rows.add(new Object[]{
                    resultSet.getString("service_no"),
                    resultSet.getDate("service_date").toLocalDate().toString(),
                    resultSet.getString("customer_name"),
                    resultSet.getString("vehicle"),
                    resultSet.getString("status"),
                    resultSet.getBigDecimal("total")
                });
            }
        }
        return rows;
    }
}
