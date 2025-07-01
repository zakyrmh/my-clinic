package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.models.Patient;
import com.clinic.models.Patient.Gender;
import com.clinic.models.Patient.MaritalStatus;
import com.clinic.utils.DatabaseUtil;

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
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        if (!validateInput()) {
            return;
        }

        Patient patient = new Patient();
        patient.setNik(nikField.getText());
        patient.setNamaLengkap(namaLengkapField.getText());
        patient.setTempatLahir(tempatLahirField.getText());
        patient.setTanggalLahir(tanggalLahirPicker.getValue());
        patient.setAlamat(alamatField.getText());
        patient.setNoTelepon(noTeleponField.getText());
        patient.setPekerjaan(pekerjaanField.getText());
        patient.setAgama(agamaField.getText());
        patient.setPendidikan(pendidikanField.getText());
        patient.setKontakDarurat(kontakDaruratField.getText());
        patient.setNoTeleponDarurat(noTeleponDaruratField.getText());

        // Ambil jenis kelamin
        RadioButton selectedGender = (RadioButton) jenisKelaminGroup.getSelectedToggle();
        patient.setJenisKelamin(Gender.fromString(selectedGender.getUserData().toString()));

        // Ambil status pernikahan
        RadioButton selectedStatus = (RadioButton) statusPernikahanGroup.getSelectedToggle();
        patient.setStatusPernikahan(MaritalStatus.fromString(selectedStatus.getUserData().toString()));

        if (saveToDatabase(patient)) {
            showAlert(AlertType.INFORMATION, "Success", "Data pasien berhasil disimpan.");
            handleClear();
            SceneManager.getInstance().switchToPatientScene();
        } else {
            showAlert(AlertType.ERROR, "Error", "Gagal menyimpan data pasien!");
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nikField.getText().isEmpty()) {
            errorMessage.append("Diperlukan NIK.\n");
        } else if (nikField.getText().length() != 16) {
            errorMessage.append("NIK harus terdiri dari 16 karakter.\n");
        } else if (!nikField.getText().matches("\\d+")) {
            errorMessage.append("NIK harus berupa angka.\n");
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
        } else if (!noTeleponField.getText().matches("\\d+")) {
            errorMessage.append("No telepon harus berupa angka.\n");
        } else if (noTeleponField.getText().length() > 13) {
            errorMessage.append("No telepon harus terdiri maksimal 13 karakter.\n");
        }
        if (statusPernikahanGroup.getSelectedToggle() == null) {
            errorMessage.append("Diperlukan Status Pernikahan.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private boolean saveToDatabase(Patient patient) {
        String sql = "INSERT INTO pasien (nik, nama_lengkap, tempat_lahir, tanggal_lahir, jenis_kelamin, alamat, no_telepon, pekerjaan, status_pernikahan, agama, pendidikan, kontak_darurat, no_telepon_darurat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getNik());
            pstmt.setString(2, patient.getNamaLengkap());
            pstmt.setString(3, patient.getTempatLahir());
            pstmt.setDate(4, java.sql.Date.valueOf(patient.getTanggalLahir()));
            pstmt.setString(5, patient.getJenisKelamin().toString());
            pstmt.setString(6, patient.getAlamat());
            pstmt.setString(7, patient.getNoTelepon());
            pstmt.setString(8, patient.getPekerjaan());
            pstmt.setString(9, patient.getStatusPernikahan().toString());
            pstmt.setString(10, patient.getAgama());
            pstmt.setString(11, patient.getPendidikan());
            pstmt.setString(12, patient.getKontakDarurat());
            pstmt.setString(13, patient.getNoTeleponDarurat());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleClear() {
        nikField.clear();
        namaLengkapField.clear();
        tempatLahirField.clear();
        tanggalLahirPicker.setValue(null);
        alamatField.clear();
        noTeleponField.clear();
        pekerjaanField.clear();
        agamaField.clear();
        pendidikanField.clear();
        kontakDaruratField.clear();
        noTeleponDaruratField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPatientScene();
    }
}
