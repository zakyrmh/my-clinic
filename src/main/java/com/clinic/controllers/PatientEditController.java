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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class PatientEditController {

    @FXML
    private TextField nikField;
    @FXML
    private TextField namaLengkapField;
    @FXML
    private ToggleGroup jenisKelaminGroup;
    @FXML
    private RadioButton lakiLakiRadio;
    @FXML
    private RadioButton perempuanRadio;
    @FXML
    private DatePicker tanggalLahirPicker;
    @FXML
    private TextArea tempatLahirField;
    @FXML
    private TextArea alamatField;
    @FXML
    private TextField noTeleponField;
    @FXML
    private TextField pekerjaanField;
    @FXML
    private TextField agamaField;
    @FXML
    private TextField pendidikanField;
    @FXML
    private TextField kontakDaruratField;
    @FXML
    private TextField noTeleponDaruratField;
    @FXML
    private ToggleGroup statusPernikahanGroup;
    @FXML
    private RadioButton belumMenikahRadio;
    @FXML
    private RadioButton menikahRadio;
    @FXML
    private RadioButton ceraiHidupRadio;
    @FXML
    private RadioButton ceraiMatiRadio;
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

        nikField.setText(patient.getNik());
        namaLengkapField.setText(patient.getNamaLengkap());
        tempatLahirField.setText(patient.getTempatLahir());
        tanggalLahirPicker.setValue(patient.getTanggalLahir());
        alamatField.setText(patient.getAlamat());
        noTeleponField.setText(patient.getNoTelepon());
        pekerjaanField.setText(patient.getPekerjaan());
        agamaField.setText(patient.getAgama());
        pendidikanField.setText(patient.getPendidikan());
        kontakDaruratField.setText(patient.getKontakDarurat());
        noTeleponDaruratField.setText(patient.getNoTeleponDarurat());

        // Jenis kelamin
        Toggle selectedJK = jenisKelaminGroup.getToggles().stream()
                .filter(t -> t.getUserData().equals(currentPatient.getJenisKelamin().getValue()))
                .findFirst().orElse(null);
        if (selectedJK != null) {
            jenisKelaminGroup.selectToggle(selectedJK);
        }

        Toggle selStatus = statusPernikahanGroup.getToggles().stream()
                .filter(t -> t.getUserData().equals(currentPatient.getStatusPernikahan().getValue()))
                .findFirst().orElse(null);
        if (selStatus != null) {
            statusPernikahanGroup.selectToggle(selStatus);
        }

    }

    @FXML
    private void handleUpdatePatient(ActionEvent event) {
        if (validateInput()) {
            try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE pasien SET nik = ?, nama_lengkap = ?, tempat_lahir = ?, tanggal_lahir = ?, jenis_kelamin = ?, alamat = ?, no_telepon = ?, pekerjaan = ?, status_pernikahan = ?, agama = ?, pendidikan = ?, kontak_darurat = ?, no_telepon_darurat = ? WHERE id_pasien = ?")) {

                String jkCode = jenisKelaminGroup.getSelectedToggle().getUserData().toString();
                String statusCode = statusPernikahanGroup.getSelectedToggle().getUserData().toString();

                stmt.setString(1, nikField.getText());
                stmt.setString(2, namaLengkapField.getText());
                stmt.setString(3, tempatLahirField.getText());
                stmt.setDate(4, java.sql.Date.valueOf(tanggalLahirPicker.getValue()));
                stmt.setString(5, jkCode);
                stmt.setString(6, alamatField.getText());
                stmt.setString(7, noTeleponField.getText());
                stmt.setString(8, pekerjaanField.getText());
                stmt.setString(9, statusCode);
                stmt.setString(10, agamaField.getText());
                stmt.setString(11, pendidikanField.getText());
                stmt.setString(12, kontakDaruratField.getText());
                stmt.setString(13, noTeleponDaruratField.getText());
                stmt.setInt(14, currentPatient.getIdPasien());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Berhasil", "Data pasien berhasil diperbarui.");
                    SceneManager.getInstance().switchToPatientScene();
                } else {
                    showAlert(AlertType.ERROR, "Gagal", "Data pasien tidak ditemukan atau tidak berubah.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memperbarui data: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nikField.getText().isEmpty()) {
            errorMessage.append("Diperlukan NIK.\n");
        }
        if (namaLengkapField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Nama Lengkap.\n");
        }
        if (!lakiLakiRadio.isSelected() && !perempuanRadio.isSelected()) {
            errorMessage.append("Diperlukan Jenis Kelamin.\n");
        }
        if (tanggalLahirPicker.getValue() == null) {
            errorMessage.append("Diperlukan Tanggal Lahir.\n");
        }
        if (tempatLahirField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Tempat Lahir.\n");
        }
        if (alamatField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Alamat.\n");
        }
        if (noTeleponField.getText().isEmpty()) {
            errorMessage.append("Diperlukan No Telepon.\n");
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
    protected void handleVisitLinkAction() {
        SceneManager.getInstance().switchToVisitScene();
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
