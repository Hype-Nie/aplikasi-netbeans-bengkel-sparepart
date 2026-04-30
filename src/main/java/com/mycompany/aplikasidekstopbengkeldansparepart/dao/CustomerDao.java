package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Customer;
import com.mycompany.aplikasidekstopbengkeldansparepart.util.CodeGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao {

    public String getNextCustomerCode(String prefix, String yearMonth) throws SQLException {
        String sql = """
                SELECT customer_code
                FROM customers
                WHERE customer_code LIKE ?
                ORDER BY customer_code DESC
                LIMIT 1
                """;

        String lastCode = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, CodeGenerator.likePattern(prefix, yearMonth));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lastCode = resultSet.getString("customer_code");
                }
            }
        }

        return CodeGenerator.nextCode(prefix, yearMonth, lastCode);
    }

    public List<Customer> findAll() throws SQLException {
        String sql = """
                SELECT id, customer_code, name, phone, vehicle, address
                FROM customers
                ORDER BY id DESC
                """;

        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                customers.add(mapRow(resultSet));
            }
        }

        return customers;
    }

    public void insert(Customer customer) throws SQLException {
        String sql = """
                INSERT INTO customers (customer_code, name, phone, vehicle, address)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerCode());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getVehicle());
            statement.setString(5, customer.getAddress());
            statement.executeUpdate();
        }
    }

    public void update(Customer customer) throws SQLException {
        String sql = """
                UPDATE customers
                SET customer_code = ?, name = ?, phone = ?, vehicle = ?, address = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerCode());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getVehicle());
            statement.setString(5, customer.getAddress());
            statement.setInt(6, customer.getId());
            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Integer> findIdByCode(String customerCode, Connection connection) throws SQLException {
        String sql = "SELECT id FROM customers WHERE customer_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(resultSet.getInt("id"));
            }
        }
    }

    private Customer mapRow(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("customer_code"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("vehicle"),
                resultSet.getString("address")
        );
    }
}
