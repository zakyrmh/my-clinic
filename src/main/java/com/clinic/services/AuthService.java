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
}