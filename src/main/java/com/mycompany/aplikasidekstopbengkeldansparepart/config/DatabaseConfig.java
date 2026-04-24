package com.mycompany.aplikasidekstopbengkeldansparepart.config;

public final class DatabaseConfig {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB = "bengkel_sparepart";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    private DatabaseConfig() {
    }

    public static String getJdbcUrl() {
        String host = read("APP_DB_HOST", DEFAULT_HOST);
        String port = read("APP_DB_PORT", DEFAULT_PORT);
        String dbName = read("APP_DB_NAME", DEFAULT_DB);

        return "jdbc:mysql://" + host + ":" + port + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    }

    public static String getUsername() {
        return read("APP_DB_USER", DEFAULT_USER);
    }

    public static String getPassword() {
        return read("APP_DB_PASS", DEFAULT_PASS);
    }

    private static String read(String key, String fallback) {
        String fromProperty = System.getProperty(key);
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }

        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }

        return fallback;
    }
}
