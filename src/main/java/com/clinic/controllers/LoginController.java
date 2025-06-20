package com.clinic.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.User;
import com.clinic.services.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements Initializable  {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
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
    protected void handleLoginButtonAction() {
        successMessage.setVisible(false);
        errorMessage.setVisible(false);

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Kesalahan: Kolom Userbane atau Password tidak boleh kosong.");
            return;
        }

        try {
            User loggedInUser = authService.login(username, password);

            if (loggedInUser != null) {
                showSuccess("Login berhasil! Selamat datang, " + loggedInUser.getNamaLengkap());
                UserSession.getInstance().startSession(loggedInUser);
                SceneManager.getInstance().switchToDashboard();
            } else {
                showError("Username atau Password salah.");
            }
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
    protected void handleRegisterLinkAction(ActionEvent event) {
        SceneManager.getInstance().switchToRegisterScene();
    }
}
