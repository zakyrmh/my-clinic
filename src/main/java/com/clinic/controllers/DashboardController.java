package com.clinic.controllers;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;

import javafx.fxml.FXML;

public class DashboardController {
    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            return;
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().endSession();
        System.out.println("The user session has ended (logout).");

        SceneManager.getInstance().switchToLoginScene();
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
