package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Supplier;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDao {

    public String getNextSupplierCode(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT supplier_code
                FROM suppliers
                WHERE supplier_code LIKE ?
                ORDER BY supplier_code DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("supplier_code");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public List<Supplier> findAll() throws SQLException {
        String sql = """
                SELECT id, supplier_code, name, phone, email, address
                FROM suppliers
                ORDER BY id DESC
                """;

        List<Supplier> suppliers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                suppliers.add(mapRow(resultSet));
            }
        }

        return suppliers;
    }

    public void insert(Supplier supplier) throws SQLException {
        String sql = """
                INSERT INTO suppliers (supplier_code, name, phone, email, address)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, supplier.getSupplierCode());
            statement.setString(2, supplier.getName());
            statement.setString(3, supplier.getPhone());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());
            statement.executeUpdate();
        }
    }

    public void update(Supplier supplier) throws SQLException {
        String sql = """
                UPDATE suppliers
                SET supplier_code = ?, name = ?, phone = ?, email = ?, address = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, supplier.getSupplierCode());
            statement.setString(2, supplier.getName());
            statement.setString(3, supplier.getPhone());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());
            statement.setInt(6, supplier.getId());
            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Integer> findIdByCode(String supplierCode, Connection connection) throws SQLException {
        String sql = "SELECT id FROM suppliers WHERE supplier_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplierCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(resultSet.getInt("id"));
            }
        }
    }

    private Supplier mapRow(ResultSet resultSet) throws SQLException {
        return new Supplier(
                resultSet.getInt("id"),
                resultSet.getString("supplier_code"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getString("address")
        );
    }
}
