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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class DoctorEditController {
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
    private Button cancel;

    private Doctor currentDoctor;

    @FXML
    public void initialize() {
        // Kosongkan untuk saat ini
    }

    public void setDoctorData(Doctor doctor) {
        this.currentDoctor = doctor;

        noSipField.setText(doctor.getNoSip());
        namaLengkapField.setText(doctor.getNamaLengkap());
        spesialisasiField.setText(doctor.getSpesialisasi());
        noTeleponField.setText(doctor.getNoTelepon());
        emailField.setText(doctor.getEmail());
        alamatField.setText(doctor.getAlamat());
        tanggalBergabungPicker.setValue(doctor.getTanggalBergabung());

        Toggle selectedStatus = statusPraktikgGroup.getToggles().stream()
                .filter(t -> t.getUserData().equals(doctor.getStatusPraktik().toString()))
                .findFirst().orElse(null);
        if (selectedStatus != null) {
            statusPraktikgGroup.selectToggle(selectedStatus);
        }
    }

    @FXML
    private void handleEditDoctor(ActionEvent event) {
        if (validateInput()) {
            try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE dokter SET no_sip = ?, nama_lengkap = ?, spesialisasi = ?, no_telepon = ?, email = ?, alamat = ?, tanggal_bergabung = ?, status_praktik = ? WHERE id_dokter = ?")) {

                stmt.setString(1, noSipField.getText());
                stmt.setString(2, namaLengkapField.getText());
                stmt.setString(3, spesialisasiField.getText());
                stmt.setString(4, noTeleponField.getText());
                stmt.setString(5, emailField.getText());
                stmt.setString(6, alamatField.getText());
                stmt.setDate(7, java.sql.Date.valueOf(tanggalBergabungPicker.getValue()));
                stmt.setString(8, ((RadioButton) statusPraktikgGroup.getSelectedToggle()).getUserData().toString());
                stmt.setInt(9, currentDoctor.getIdDokter());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Data dokter berhasil diperbarui.");
                    SceneManager.getInstance().switchToDoctorScene();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Gagal memperbarui data dokter.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error updating patient: " + e.getMessage());
            }
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
