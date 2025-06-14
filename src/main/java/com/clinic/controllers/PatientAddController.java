package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;
import com.clinic.models.Patient.Gender;
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

public class PatientAddController {
    @FXML private TextField medicalRecordField;
    @FXML private TextField nameField;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;
    @FXML private TextArea addressField;
    @FXML private TextField phoneField;
    @FXML private TextField identityNumberField;
    @FXML private Button submitButton;
    
    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        if (!validateInput()) {
            return;
        }

        Patient patient = new Patient();
        patient.setMedicalRecord(medicalRecordField.getText());
        patient.setName(nameField.getText());
        patient.setDateOfBirth(dateOfBirthPicker.getValue());
        patient.setGender(maleRadio.isSelected() ? Gender.MALE : Gender.FEMALE);
        patient.setAddress(addressField.getText());
        patient.setPhone(phoneField.getText());
        patient.setIdentityNumber(identityNumberField.getText());

        if (saveToDatabase(patient)) {
            showAlert(AlertType.INFORMATION, "Success", "Patient data saved successfully.");
            handleClear();
            SceneManager.getInstance().switchToPatientScene();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to save patient data.");
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (medicalRecordField.getText().isEmpty()) {
            errorMessage.append("Medical Record is required.\n");
        }
        if (nameField.getText().isEmpty()) {
            errorMessage.append("Name is required.\n");
        }
        if (dateOfBirthPicker.getValue() == null) {
            errorMessage.append("Date of Birth is required.\n");
        }
        if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
            errorMessage.append("Gender is required.\n");
        }
        if (addressField.getText().isEmpty()) {
            errorMessage.append("Address is required.\n");
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

    private boolean saveToDatabase(Patient patient) {
        String sql = "INSERT INTO patients (medical_record, name, date_of_birth, gender, address, phone, identity_number) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getMedicalRecord());
            pstmt.setString(2, patient.getName());
            pstmt.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
            pstmt.setString(4, patient.getGender().toString());
            pstmt.setString(5, patient.getAddress());
            pstmt.setString(6, patient.getPhone());
            pstmt.setString(7, patient.getIdentityNumber());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleClear() {
        medicalRecordField.clear();
        nameField.clear();
        dateOfBirthPicker.setValue(null);
        genderGroup.selectToggle(null);
        addressField.clear();
        phoneField.clear();
        identityNumberField.clear();
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
    protected void handlePatientAction() {
        SceneManager.getInstance().switchToPatientScene();
    }
}
