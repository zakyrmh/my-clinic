package com.clinic.controllers;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DoctorShowController {
    @FXML private Label noSipLabel;
    @FXML private Label namaLengkapLabel;
    @FXML private Label spesialisasiLabel;
    @FXML private Label noTeleponLabel;
    @FXML private Label emailLabel;
    @FXML private Label alamatLabel;
    @FXML private Label tanggalBergabungLabel;
    @FXML private Label statusPraktikLabel;

    public void setDoctorData(Doctor doctor) {
        noSipLabel.setText(doctor.getNoSip());
        namaLengkapLabel.setText(doctor.getNamaLengkap());
        spesialisasiLabel.setText(doctor.getSpesialisasi());
        noTeleponLabel.setText(doctor.getNoTelepon());
        emailLabel.setText(doctor.getEmail());
        alamatLabel.setText(doctor.getAlamat());
        tanggalBergabungLabel.setText(doctor.getTanggalBergabung().toString());
        statusPraktikLabel.setText(doctor.getStatusPraktik().toString());
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
