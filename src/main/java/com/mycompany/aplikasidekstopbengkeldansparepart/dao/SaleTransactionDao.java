package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionDao {

    private final CustomerDao customerDao = new CustomerDao();

    public String getNextSaleNo(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT sale_no
                FROM sale_transactions
                WHERE sale_no LIKE ?
                ORDER BY sale_no DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("sale_no");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public void save(SaleTransaction transaction, List<SaleItem> items, int adminId) throws SQLException {
        String insertHeaderSql = """
                INSERT INTO sale_transactions (
                    sale_no, sale_date, customer_id,
                    payment_method, total, created_by
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        String insertItemSql = """
                INSERT INTO sale_transaction_items (
                    sale_transaction_id, sparepart_id, part_code,
                    part_name, qty, unit_price, subtotal
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String reduceStockSql = """
                UPDATE spareparts
                SET stock = stock - ?
                WHERE id = ? AND stock >= ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // Resolve optional customer
                Integer customerId = null;
                String customerCode = transaction.getCustomerCode();
                if (customerCode != null && !customerCode.isBlank()) {
                    customerId = customerDao
                            .findIdByCode(customerCode, connection)
                            .orElseThrow(() -> new SQLException(
                                    "Kode pelanggan tidak ditemukan: " + customerCode
                            ));
                }

                int saleTransactionId;
                try (PreparedStatement headerStatement = connection.prepareStatement(
                        insertHeaderSql,
                        Statement.RETURN_GENERATED_KEYS
                )) {
                    headerStatement.setString(1, transaction.getSaleNo());
                    headerStatement.setDate(2, Date.valueOf(transaction.getSaleDate()));
                    if (customerId != null) {
                        headerStatement.setInt(3, customerId);
                    } else {
                        headerStatement.setNull(3, Types.INTEGER);
                    }
                    headerStatement.setString(4, transaction.getPaymentMethod());
                    headerStatement.setBigDecimal(5, transaction.getTotal());
                    headerStatement.setInt(6, adminId);
                    headerStatement.executeUpdate();

                    try (ResultSet generatedKeys = headerStatement.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Gagal mengambil ID transaksi penjualan.");
                        }
                        saleTransactionId = generatedKeys.getInt(1);
                    }
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql);
                     PreparedStatement stockStatement = connection.prepareStatement(reduceStockSql)) {

                    for (SaleItem item : items) {
                        itemStatement.setInt(1, saleTransactionId);
                        itemStatement.setInt(2, item.getSparepartId());
                        itemStatement.setString(3, item.getPartCode());
                        itemStatement.setString(4, item.getPartName());
                        itemStatement.setInt(5, item.getQty());
                        itemStatement.setBigDecimal(6, item.getUnitPrice());
                        itemStatement.setBigDecimal(7, item.getSubtotal());
                        itemStatement.addBatch();

                        stockStatement.setInt(1, item.getQty());
                        stockStatement.setInt(2, item.getSparepartId());
                        stockStatement.setInt(3, item.getQty());
                        int updatedRows = stockStatement.executeUpdate();
                        if (updatedRows == 0) {
                            throw new SQLException(
                                    "Stok part tidak cukup atau kode part tidak ditemukan: " + item.getPartCode());
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

    public List<Object[]> findAllSummary() throws SQLException {
        String sql = """
                SELECT st.sale_no, st.sale_date, COALESCE(c.name, 'Umum') AS customer_name,
                       st.payment_method, st.total
                FROM sale_transactions st
                LEFT JOIN customers c ON c.id = st.customer_id
                ORDER BY st.id DESC
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rows.add(new Object[]{
                    resultSet.getString("sale_no"),
                    resultSet.getDate("sale_date").toLocalDate().toString(),
                    resultSet.getString("customer_name"),
                    resultSet.getString("payment_method"),
                    resultSet.getBigDecimal("total")
                });
            }
        }
        return rows;
    }
}
