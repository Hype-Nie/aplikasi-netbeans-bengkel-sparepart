package com.mycompany.aplikasidekstopbengkeldansparepart.dao;

import com.mycompany.aplikasidekstopbengkeldansparepart.config.DatabaseConnection;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDao {

    public Optional<Admin> authenticate(String username, String password) throws SQLException {
        String sql = """
                SELECT id, username, full_name, role
                FROM admins
                WHERE username = ? AND password = ? AND is_active = 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Admin admin = new Admin(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("full_name"),
                        resultSet.getString("role")
                );
                return Optional.of(admin);
            }
        }
    }
}
