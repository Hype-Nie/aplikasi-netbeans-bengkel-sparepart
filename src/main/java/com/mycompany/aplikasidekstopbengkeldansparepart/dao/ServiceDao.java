package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Service;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDao {

    public String getNextServiceCode(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT service_code
                FROM services
                WHERE service_code LIKE ?
                ORDER BY service_code DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("service_code");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public List<Service> findAll() throws SQLException {
        String sql = """
                SELECT id, service_code, name, price
                FROM services
                ORDER BY id DESC
                """;

        List<Service> services = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                services.add(mapRow(resultSet));
            }
        }

        return services;
    }

    public void insert(Service service) throws SQLException {
        String sql = """
                INSERT INTO services (service_code, name, price)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getServiceCode());
            statement.setString(2, service.getName());
            statement.setBigDecimal(3, service.getPrice());
            statement.executeUpdate();
        }
    }

    public void update(Service service) throws SQLException {
        String sql = """
                UPDATE services
                SET service_code = ?, name = ?, price = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getServiceCode());
            statement.setString(2, service.getName());
            statement.setBigDecimal(3, service.getPrice());
            statement.setInt(4, service.getId());
            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Integer> findIdByCode(String serviceCode, Connection connection) throws SQLException {
        String sql = "SELECT id FROM services WHERE service_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, serviceCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(resultSet.getInt("id"));
            }
        }
    }

    private Service mapRow(ResultSet resultSet) throws SQLException {
        return new Service(
                resultSet.getInt("id"),
                resultSet.getString("service_code"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }
}
