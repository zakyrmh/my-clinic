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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable {
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private ComboBox<User.Role> roleComboBox;
    @FXML Label successMessage;
    @FXML Label errorMessage;
    @FXML private Hyperlink loginLink;

    private AuthService authService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authService = new AuthService();
        roleComboBox.getItems().setAll(User.Role.values());

        successMessage.managedProperty().bind(successMessage.visibleProperty());
        errorMessage.managedProperty().bind(errorMessage.visibleProperty());

        successMessage.setVisible(false);
        errorMessage.setVisible(false);
    }

    @FXML
    protected void handleRegisterButtonAction() {
        successMessage.setVisible(false);
        errorMessage.setVisible(false);

        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        User.Role role = roleComboBox.getValue();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
            showError("Error: Name, Username, Password, and Role fields cannot be empty.");
            return;
        }

        if (password.length() < 8) {
            showError("Error: Password must be at least 8 characters long.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Error: Password and Confirm Password fields do not match.");
            return;
        }

        User newUser = new User(username, password, role, name, null, null, User.Status.ACTIVE, null, null);

        try {
            authService.registerUser(newUser);
            showSuccess("Registration successful!");
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
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}
