package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;
import com.clinic.utils.DatabaseUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class PatientEditController {
    @FXML
    private TextField medicalRecordField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private TextArea addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField identityNumberField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancel;

    private Patient currentPatient;

    @FXML
    public void initialize() {
        // Kosongkan untuk saat ini
    }

    public void setPatientData(Patient patient) {
        this.currentPatient = patient;

        medicalRecordField.setText(patient.getMedicalRecord());
        nameField.setText(patient.getName());
        dateOfBirthPicker.setValue(patient.getDateOfBirth());
        if (patient.getGender() == Patient.Gender.MALE) {
            maleRadio.setSelected(true);
        } else {
            femaleRadio.setSelected(true);
        }
        addressField.setText(patient.getAddress());
        phoneField.setText(patient.getPhone());
        identityNumberField.setText(patient.getIdentityNumber());
    }

    @FXML
    private void handleUpdatePatient(ActionEvent event) {
        if (validateInput()) {
            try (Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE patients SET medical_record = ?, name = ?, date_of_birth = ?, gender = ?, address = ?, phone = ?, identity_number = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?")) {

                stmt.setString(1, medicalRecordField.getText());
                stmt.setString(2, nameField.getText());
                stmt.setDate(3, java.sql.Date.valueOf(dateOfBirthPicker.getValue()));
                stmt.setString(4, maleRadio.isSelected() ? "male" : "female");
                stmt.setString(5, addressField.getText());
                stmt.setString(6, phoneField.getText());
                stmt.setString(7, identityNumberField.getText());
                stmt.setInt(8, currentPatient.getId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Patient updated successfully.");
                    SceneManager.getInstance().switchToPatientScene();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to update patient.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error updating patient: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (medicalRecordField.getText().isEmpty() || nameField.getText().isEmpty() ||
                dateOfBirthPicker.getValue() == null || addressField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || identityNumberField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all fields.");
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
}
