package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PurchaseTransactionDao {

    private final SupplierDao supplierDao = new SupplierDao();
    private final SparepartDao sparepartDao = new SparepartDao();

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
}
