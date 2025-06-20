package com.clinic.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.clinic.manager.SceneManager;
import com.clinic.models.User;
import com.clinic.services.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable {
    @FXML private TextField namaLengkapField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML Label successMessage;
    @FXML Label errorMessage;

    private AuthService authService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authService = new AuthService();

        successMessage.managedProperty().bind(successMessage.visibleProperty());
        errorMessage.managedProperty().bind(errorMessage.visibleProperty());
        successMessage.setVisible(false);
        errorMessage.setVisible(false);
    }

    @FXML
    protected void handleRegisterButtonAction() {
        successMessage.setVisible(false);
        errorMessage.setVisible(false);

        String namaLengkap = namaLengkapField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (namaLengkap.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Kesalahan: Kolom Nama, Username, Password tidak boleh kosong.");
            return;
        }

        if (password.length() < 8) {
            showError("Kesalahan: Password harus terdiri dari minimal 8 karakter.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Kesalahan: Kolom Password dan Konfirmasi Password tidak cocok.");
            return;
        }

        User newUser = new User(0, username, password, namaLengkap, null, null, null, null);

        try {
            authService.registerUser(newUser);
            showSuccess("Registerasi berhasil!");
            clearForm();
        } catch (SQLException e) {
            showError("Error database: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }

    private void showSuccess(String message) {
        successMessage.setText(message);
        successMessage.setVisible(true);
    }

    @FXML
    protected void handleLoginLinkAction(ActionEvent event) {
        SceneManager.getInstance().switchToLoginScene();
    }

    private void clearForm() {
        namaLengkapField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
