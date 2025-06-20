package com.clinic.controllers;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PatientShowController {
    @FXML private Label noRmLabel;
    @FXML private Label nikLabel;
    @FXML private Label namaLengkapLabel;
    @FXML private Label jenisKelaminLabel;
    @FXML private Label ttlLabel;
    @FXML private Label alamatLabel;
    @FXML private Label noTeleponLabel;
    @FXML private Label emailLabel;
    @FXML private Label pekerjaanLabel;
    @FXML private Label statusPernikahanLabel;
    @FXML private Label golonganDarahLabel;

    public void setPatientData(Patient patient) {
        noRmLabel.setText(patient.getNoRm());
        nikLabel.setText(patient.getNik());
        namaLengkapLabel.setText(patient.getNamaLengkap());
        jenisKelaminLabel.setText(patient.getJenisKelamin() == Patient.Gender.MALE ? "Laki-laki" : "Perempuan");
        ttlLabel.setText(patient.getTanggalLahir().toString());
        alamatLabel.setText(patient.getAlamat());
        noTeleponLabel.setText(patient.getNoTelepon());
        emailLabel.setText(patient.getEmail());
        pekerjaanLabel.setText(patient.getPekerjaan());
        statusPernikahanLabel.setText(patient.getStatusPernikahan().toString());
        golonganDarahLabel.setText(patient.getGolonganDarah().toString());
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
