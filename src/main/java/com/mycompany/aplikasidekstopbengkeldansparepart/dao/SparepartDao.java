package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Sparepart;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SparepartDao {

    public String getNextPartCode(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT part_code
                FROM spareparts
                WHERE part_code LIKE ?
                ORDER BY part_code DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("part_code");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public List<Sparepart> findAll() throws SQLException {
        String sql = """
                SELECT id, part_code, name, category, unit, purchase_price, selling_price, stock, min_stock
                FROM spareparts
                ORDER BY id DESC
                """;

        List<Sparepart> spareparts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                spareparts.add(mapRow(resultSet));
            }
        }

        return spareparts;
    }

    public void insert(Sparepart sparepart) throws SQLException {
        String sql = """
                INSERT INTO spareparts (
                    part_code, name, category, unit, purchase_price, selling_price, stock, min_stock
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sparepart.getPartCode());
            statement.setString(2, sparepart.getName());
            statement.setString(3, sparepart.getCategory());
            statement.setString(4, sparepart.getUnit());
            statement.setBigDecimal(5, sparepart.getPurchasePrice());
            statement.setBigDecimal(6, sparepart.getSellingPrice());
            statement.setInt(7, sparepart.getStock());
            statement.setInt(8, sparepart.getMinStock());
            statement.executeUpdate();
        }
    }

    public void update(Sparepart sparepart) throws SQLException {
        String sql = """
                UPDATE spareparts
                SET part_code = ?, name = ?, category = ?, unit = ?,
                    purchase_price = ?, selling_price = ?, stock = ?, min_stock = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sparepart.getPartCode());
            statement.setString(2, sparepart.getName());
            statement.setString(3, sparepart.getCategory());
            statement.setString(4, sparepart.getUnit());
            statement.setBigDecimal(5, sparepart.getPurchasePrice());
            statement.setBigDecimal(6, sparepart.getSellingPrice());
            statement.setInt(7, sparepart.getStock());
            statement.setInt(8, sparepart.getMinStock());
            statement.setInt(9, sparepart.getId());
            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM spareparts WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Integer> findIdByCode(String partCode, Connection connection) throws SQLException {
        String sql = "SELECT id FROM spareparts WHERE part_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, partCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(resultSet.getInt("id"));
            }
        }
    }

    private Sparepart mapRow(ResultSet resultSet) throws SQLException {
        return new Sparepart(
                resultSet.getInt("id"),
                resultSet.getString("part_code"),
                resultSet.getString("name"),
                resultSet.getString("category"),
                resultSet.getString("unit"),
                resultSet.getBigDecimal("purchase_price"),
                resultSet.getBigDecimal("selling_price"),
                resultSet.getInt("stock"),
                resultSet.getInt("min_stock")
        );
    }
}
