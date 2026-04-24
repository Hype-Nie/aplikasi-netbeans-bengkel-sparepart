package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServiceTransactionDao {

    private final CustomerDao customerDao = new CustomerDao();

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
}
