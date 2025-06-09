package com.clinic.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clinic.models.User;
import com.clinic.utils.DatabaseUtil;
import com.clinic.utils.SecurityUtil;

public class AuthService {
    public void registerUser(User user) throws SQLException, IllegalArgumentException {
        String checkUserSql = "SELECT id FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users (name, username, password, role, email, phone, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, user.getUsername());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalArgumentException("Username '" + user.getUsername() + "' has been used.");
                    }
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {
                String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
                insertStmt.setString(1, user.getName());
                insertStmt.setString(2, user.getUsername());
                insertStmt.setString(3, hashedPassword);
                insertStmt.setString(4, user.getRole().name());
                insertStmt.setString(5, user.getEmail());
                insertStmt.setString(6, user.getPhone());
                insertStmt.setString(7, user.getStatus().name());

                insertStmt.executeUpdate();
            }
        }
    }

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id, password, role, name, email, phone, status FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    
                    if (SecurityUtil.verifyPassword(storedHash, password)) {
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String phone = rs.getString("phone");
                        User.Role role = User.Role.valueOf(rs.getString("role").toUpperCase());
                        User.Status status = User.Status.valueOf(rs.getString("status").toUpperCase());
                        
                        return new User(username, storedHash, role, name, email, phone, status, null, null);
                    }
                }
            }
        }
        
        return null;
    }
}