package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;
import com.clinic.utils.DatabaseUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DoctorAddController {
    @FXML
    private TextField licenseNoField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        if (!validateInput()) {
            return;
        }

        Doctor doctor = new Doctor();
        doctor.setLicenseNo(licenseNoField.getText());
        doctor.setName(nameField.getText());
        doctor.setSpecialization(specializationField.getText());
        doctor.setPhone(phoneField.getText());

        if (saveToDatabase(doctor)) {
            showAlert(AlertType.INFORMATION, "Success", "Doctor data saved successfully.");
            handleClear();
            SceneManager.getInstance().switchToDoctorScene();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to save doctor data.");
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (licenseNoField.getText().isEmpty()) {
            errorMessage.append("License No is required.\n");
        }
        if (nameField.getText().isEmpty()) {
            errorMessage.append("Name is required.\n");
        }
        if (specializationField.getText().isEmpty()) {
            errorMessage.append("Specialization is required.\n");
        }
        if (phoneField.getText().isEmpty()) {
            errorMessage.append("Phone is required.\n");
        } else if (!phoneField.getText().matches("\\d+")) {
            errorMessage.append("Phone must contain only numbers.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private boolean saveToDatabase(Doctor doctor) {
        String query = "INSERT INTO doctors (license_no, name, specialization, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctor.getLicenseNo());
            pstmt.setString(2, doctor.getName());
            pstmt.setString(3, doctor.getSpecialization());
            pstmt.setString(4, doctor.getPhone());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleClear() {
        licenseNoField.clear();
        nameField.clear();
        specializationField.clear();
        phoneField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().endSession();
        System.out.println("The user session has ended (logout).");

        SceneManager.getInstance().switchToLoginScene();
    }

    @FXML
    protected void handleDashboardLinkAction(ActionEvent event) {
        SceneManager.getInstance().switchToDashboard();
    }

    @FXML
    protected void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPatientScene();
    }

    @FXML
    protected void handleDoctorLinkAction() {
        SceneManager.getInstance().switchToDoctorScene();
    }
    
    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
