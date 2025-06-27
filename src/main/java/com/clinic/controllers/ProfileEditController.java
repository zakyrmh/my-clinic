package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.models.User;
import com.clinic.utils.DatabaseUtil;
import com.clinic.utils.SecurityUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class ProfileEditController {

    @FXML
    private TextField namaLengkapField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField noTeleponField;
    @FXML
    private TextField passwordSekarangField;
    @FXML
    private TextField passwordBaruField;
    @FXML
    private TextField konfirmasiPasswordField;

    private User currentUser;

    @FXML
    public void initialize() {
        // Kosongkan untuk saat ini
    }

    public void setUserData(User user) {
        this.currentUser = user;

        namaLengkapField.setText(user.getNamaLengkap());
        emailField.setText(user.getEmail());
        noTeleponField.setText(user.getNoTelepon());
    }

    public User getUserById(int userId) {
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id_user = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setNamaLengkap(rs.getString("nama_lengkap"));
                user.setEmail(rs.getString("email"));
                user.setNoTelepon(rs.getString("no_telepon"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleEditProfileAction() {
        if (validateInputProfile()) {
            try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE user SET nama_lengkap = ?, email = ?, no_telepon = ? WHERE id_user = ?;")) {

                stmt.setString(1, namaLengkapField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setString(3, noTeleponField.getText());
                stmt.setInt(4, currentUser.getIdUser());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Profile berhasil diperbarui.");
                    SceneManager.getInstance().switchToDashboard();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Gagal memperbarui profile.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error updating profile: " + e.getMessage());
            }
        }
    }

    private boolean validateInputProfile() {
        StringBuilder errorMessage = new StringBuilder();

        if (namaLengkapField.getText() == null || namaLengkapField.getText().isEmpty()) {
            errorMessage.append("Nama Lengkap harus diisi.\n");
        }
        if (!emailField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            errorMessage.append("Format email tidak valid.\n");
        }
        if (!noTeleponField.getText().matches("\\d+")) {
            errorMessage.append("No Telepon harus berupa angka.\n");
        } else if (noTeleponField.getText().length() > 13) {
            errorMessage.append("No Telepon tidak boleh lebih dari 13 angka.\n");
        }
        return true;
    }

    @FXML
    private void handleEditPasswordAction() {
        if (true) {
            try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE user SET password = ? WHERE id_user = ?;")) {

                String passwordHash = SecurityUtil.hashPassword(passwordBaruField.getText());
                stmt.setString(1, passwordHash);
                stmt.setInt(2, currentUser.getIdUser());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Password berhasil diperbarui.");
                    SceneManager.getInstance().switchToDashboard();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Gagal memperbarui password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error updating password: " + e.getMessage());
            }
        }
    }

    private boolean validateInputPassword() {
        StringBuilder errorMessage = new StringBuilder();
        String storedHash = currentUser.getPassword();

        if (!SecurityUtil.verifyPassword(storedHash, passwordSekarangField.getText())) {
            errorMessage.append("Password sekarang salah.\n");
        }

        if (!passwordBaruField.getText().equals(konfirmasiPasswordField.getText())) {
            errorMessage.append("Password baru dan konfirmasi password tidak cocok.\n");
        }

        return true;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
