package org.uas.repository;

import org.uas.data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    public void createTable() {
        // Create database tables if they don't exist
        // Implement this method to create tables for users, courses, classes, and attendance records
        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "email TEXT NOT NULL PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ")";
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(userTableSql);
                // Execute more table creation statements as needed
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Handle table creation error
            }
        }
    }

    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                users.add(new User(email, username, password));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }

        return users;
    }


    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Jika ada hasil, berarti user valid
        } catch (SQLException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return false;
        }
    }


    public boolean insertUser(String email, String username, String password) {
        String query = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }


    public boolean updateUser(String email, String username, String password) {
        String query = "UPDATE users SET username = ?, password = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteUser(String email) {
        String query = "DELETE FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}

