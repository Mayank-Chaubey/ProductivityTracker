package com.productivitytracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    // ---------- CONFIGURATION ----------
    private static final String DB_URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/productivity_db?useSSL=false&serverTimezone=UTC"
            );

    private static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "root@123");

    // ---------- DRIVER INIT ----------
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC Driver not found. Check classpath."
            );
        }
    }

    private DBConnection() {
        throw new AssertionError("DBConnection should not be instantiated");
    }

    // ---------- CONNECTION ----------
    public static Connection getConnection() throws SQLException {
        Connection connection =
                DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        if (!connection.isValid(2)) {
            throw new SQLException("Failed to obtain a valid database connection");
        }

        return connection;
    }
}