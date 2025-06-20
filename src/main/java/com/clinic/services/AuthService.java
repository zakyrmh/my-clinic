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
        String checkUserSql = "SELECT id FROM user WHERE username = ?";
        String insertUserSql = "INSERT INTO user (username, password, nama_lengkap, no_telepon, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, user.getUsername());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalArgumentException("Username '" + user.getUsername() + "' telah digunakan.");
                    }
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {
                String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
                insertStmt.setString(1, user.getUsername());
                insertStmt.setString(2, hashedPassword);
                insertStmt.setString(3, user.getNamaLengkap());
                insertStmt.setString(4, user.getNoTelepon());
                insertStmt.setString(5, user.getEmail());

                insertStmt.executeUpdate();
            }
        }
    }

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id_user, username, password, nama_lengkap, no_telepon, email FROM user WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    
                    if (SecurityUtil.verifyPassword(storedHash, password)) {
                        int idUser = rs.getInt("id_user");
                        String namaLengkap = rs.getString("name");
                        String email = rs.getString("email");
                        String noTelepon = rs.getString("phone");
                        
                        return new User(idUser, username, storedHash, namaLengkap, noTelepon, email, null, null);
                    }
                }
            }
        }
        
        return null;
    }
}