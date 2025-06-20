package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;
import com.clinic.models.Doctor.PracticeStatus;
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

public class DoctorAddController {
    @FXML
    private TextField noSipField;
    @FXML
    private TextField namaLengkapField;
    @FXML
    private TextField spesialisasiField;
    @FXML
    private TextField noTeleponField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea alamatField;
    @FXML
    private DatePicker tanggalBergabungPicker;
    @FXML
    private ToggleGroup statusPraktikgGroup;
    @FXML
    private RadioButton aktifRadio;
    @FXML
    private RadioButton tidakAktifRadio;
    @FXML
    private RadioButton cutiRadio;
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
        doctor.setNoSip(noSipField.getText());
        doctor.setNamaLengkap(namaLengkapField.getText());
        doctor.setSpesialisasi(spesialisasiField.getText());
        doctor.setNoTelepon(noTeleponField.getText());
        doctor.setEmail(emailField.getText());
        doctor.setAlamat(alamatField.getText());
        doctor.setTanggalBergabung(tanggalBergabungPicker.getValue());
        
        // Ambil status praktik
        RadioButton selectedStatus = (RadioButton) statusPraktikgGroup.getSelectedToggle();
        doctor.setStatusPraktik(PracticeStatus.fromString(selectedStatus.getUserData().toString()));

        if (saveToDatabase(doctor)) {
            showAlert(AlertType.INFORMATION, "Success", "Data dokter berhasil disimpan.");
            handleClear();
            SceneManager.getInstance().switchToDoctorScene();
        } else {
            showAlert(AlertType.ERROR, "Error", "Gagal menyimpan data dokter.");
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (noSipField.getText().isEmpty()) {
            errorMessage.append("Diperlukan No SIP.\n");
        }
        if (namaLengkapField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Nama Lengkap.\n");
        }
        if (spesialisasiField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Spesialisasi.\n");
        }
        if (noTeleponField.getText().isEmpty()) {
            errorMessage.append("Diperlukan No Telepon.\n");
        } else if (!noTeleponField.getText().matches("\\d+")) {
            errorMessage.append("No telepon harus berupa angka.\n");
        }
        if (alamatField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Alamat.\n");
        }
        if (tanggalBergabungPicker.getValue() == null) {
            errorMessage.append("Diperlukan Tanggal Bergabung.\n");
        }
        if (statusPraktikgGroup.getSelectedToggle() == null) {
            errorMessage.append("Diperlukan Status Praktik.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private boolean saveToDatabase(Doctor doctor) {
        String query = "INSERT INTO dokter (no_sip, nama_lengkap, spesialisasi, no_telepon, email, alamat, tanggal_bergabung, status_praktik) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctor.getNoSip());
            pstmt.setString(2, doctor.getNamaLengkap());
            pstmt.setString(3, doctor.getSpesialisasi());
            pstmt.setString(4, doctor.getNoTelepon());
            pstmt.setString(5, doctor.getEmail());
            pstmt.setString(6, doctor.getAlamat());
            pstmt.setDate(7, java.sql.Date.valueOf(doctor.getTanggalBergabung()));
            pstmt.setString(8, doctor.getStatusPraktik().toString());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleClear() {
        noSipField.clear();
        namaLengkapField.clear();
        spesialisasiField.clear();
        noTeleponField.clear();
        emailField.clear();
        alamatField.clear();
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
