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
import javafx.scene.control.TextField;

public class DoctorEditController {
    @FXML
    private TextField licenseNoField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextField phoneField;

    private Doctor currentDoctor;

    @FXML
    public void initialize() {
        // Kosongkan untuk saat ini
    }

    public void setDoctorData(Doctor doctor) {
        this.currentDoctor = doctor;

        licenseNoField.setText(doctor.getLicenseNo());
        nameField.setText(doctor.getName());
        specializationField.setText(doctor.getSpecialization());
        phoneField.setText(doctor.getPhone());
    }

    @FXML
    private void handleEditDoctor(ActionEvent event) {
        if (validateInput()) {
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE doctors SET license_no = ?, name = ?, specialization = ?, phone = ? WHERE id = ?")) {

                pstmt.setString(1, licenseNoField.getText());
                pstmt.setString(2, nameField.getText());
                pstmt.setString(3, specializationField.getText());
                pstmt.setString(4, phoneField.getText());
                pstmt.setInt(5, currentDoctor.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Doctor data updated successfully.");
                    SceneManager.getInstance().switchToDoctorScene();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to update doctor data.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error updating doctor data: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (licenseNoField.getText().isEmpty() || nameField.getText().isEmpty() ||
                specializationField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return false;
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
