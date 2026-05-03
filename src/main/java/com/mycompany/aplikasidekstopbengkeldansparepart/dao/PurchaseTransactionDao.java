package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseTransactionDao {

    private final SupplierDao supplierDao = new SupplierDao();
    private final SparepartDao sparepartDao = new SparepartDao();

    public String getNextPurchaseNo(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT purchase_no
                FROM purchase_transactions
                WHERE purchase_no LIKE ?
                ORDER BY purchase_no DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("purchase_no");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public void save(PurchaseTransaction transaction, List<PurchaseItem> items, int adminId) throws SQLException {
        String insertHeaderSql = """
                INSERT INTO purchase_transactions (
                    purchase_no, purchase_date, supplier_id,
                    payment_method, status, total, created_by
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String insertItemSql = """
                INSERT INTO purchase_transaction_items (
                    purchase_transaction_id, sparepart_id, part_code,
                    part_name, qty, unit_price, subtotal
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String addStockSql = """
                UPDATE spareparts
                SET stock = stock + ?
                WHERE part_code = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int supplierId = supplierDao
                        .findIdByCode(transaction.getSupplierCode(), connection)
                        .orElseThrow(() -> new SQLException(
                                "Kode supplier tidak ditemukan: " + transaction.getSupplierCode()
                        ));

                int purchaseTransactionId;
                try (PreparedStatement headerStatement = connection.prepareStatement(
                        insertHeaderSql,
                        Statement.RETURN_GENERATED_KEYS
                )) {
                    headerStatement.setString(1, transaction.getPurchaseNo());
                    headerStatement.setDate(2, Date.valueOf(transaction.getPurchaseDate()));
                    headerStatement.setInt(3, supplierId);
                    headerStatement.setString(4, transaction.getPaymentMethod());
                    headerStatement.setString(5, transaction.getStatus());
                    headerStatement.setBigDecimal(6, transaction.getTotal());
                    headerStatement.setInt(7, adminId);
                    headerStatement.executeUpdate();

                    try (ResultSet generatedKeys = headerStatement.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Gagal mengambil ID transaksi pembelian.");
                        }
                        purchaseTransactionId = generatedKeys.getInt(1);
                    }
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql);
                     PreparedStatement stockStatement = connection.prepareStatement(addStockSql)) {

                    for (PurchaseItem item : items) {
                        int sparepartId = sparepartDao
                                .findIdByCode(item.getPartCode(), connection)
                                .orElseThrow(() -> new SQLException(
                                        "Kode part tidak ditemukan di master sparepart: " + item.getPartCode()
                                ));

                        itemStatement.setInt(1, purchaseTransactionId);
                        itemStatement.setInt(2, sparepartId);
                        itemStatement.setString(3, item.getPartCode());
                        itemStatement.setString(4, item.getPartName());
                        itemStatement.setInt(5, item.getQty());
                        itemStatement.setBigDecimal(6, item.getUnitPrice());
                        itemStatement.setBigDecimal(7, item.getSubtotal());
                        itemStatement.addBatch();

                        stockStatement.setInt(1, item.getQty());
                        stockStatement.setString(2, item.getPartCode());
                        int updatedRows = stockStatement.executeUpdate();
                        if (updatedRows == 0) {
                            throw new SQLException("Gagal update stok part: " + item.getPartCode());
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

    public void updateStatus(String purchaseNo, String newStatus) throws SQLException {
        String sql = "UPDATE purchase_transactions SET status = ? WHERE purchase_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setString(2, purchaseNo);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Nomor pembelian tidak ditemukan: " + purchaseNo);
            }
        }
    }

    public List<Object[]> findAllSummary() throws SQLException {
        String sql = """
                SELECT pt.purchase_no, pt.purchase_date, s.name AS supplier_name,
                       pt.status, pt.total
                FROM purchase_transactions pt
                JOIN suppliers s ON s.id = pt.supplier_id
                ORDER BY pt.id DESC
                """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rows.add(new Object[]{
                    resultSet.getString("purchase_no"),
                    resultSet.getDate("purchase_date").toLocalDate().toString(),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("status"),
                    resultSet.getBigDecimal("total")
                });
            }
        }
        return rows;
    }
}
