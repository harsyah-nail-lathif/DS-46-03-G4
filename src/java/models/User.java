/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author LENOVO
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;

    private final String DB_URL = "jdbc:mysql://localhost:3306/db_barang";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    public User() {
        // Default constructor
    }

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Method untuk menyimpan user ke database
    public boolean save() throws SQLException {
        String query = "INSERT INTO users (id, username, password, role) VALUES (?, ?, MD5(?), ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, id);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            return ps.executeUpdate() > 0;
        }
    }

    // Method untuk login
    public static User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    // Method untuk generate ID berdasarkan role
    public static String generateId(String role) throws SQLException {
        String prefix = role.equals("kasir") ? "KSR" : "INV";
        String query = "SELECT MAX(id) AS maxId FROM users WHERE id LIKE ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maxId = rs.getString("maxId");
                    if (maxId != null) {
                        int num = Integer.parseInt(maxId.substring(3)) + 1;
                        return prefix + String.format("%03d", num);
                    }
                }
            }
        }
        return prefix + "001"; // ID pertama jika belum ada
    }

    // Getters dan Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
