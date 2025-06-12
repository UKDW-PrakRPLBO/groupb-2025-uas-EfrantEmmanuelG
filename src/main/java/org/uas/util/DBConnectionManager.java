package org.uas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DBConnectionManager {
    private static final String DB_URL = "jdbc:sqlite:dbuas.db";
    private static Connection connection;

    static {
        try {
            // Ensure the SQLite JDBC driver is loaded
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load SQLite JDBC driver", e);
        }
    }

    // Private constructor to prevent instantiation
    private DBConnectionManager() {}


    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                // Recommended for SQLite to enable foreign key constraints
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.isValid(2); // 2 second timeout
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean verifyDatabase() {
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT 1");
        } catch (SQLException e) {
            return false;
        }
    }
}