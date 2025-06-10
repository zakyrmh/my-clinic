package com.clinic.controllers;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {
    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            return;
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().endSession();
        System.out.println("The user session has ended (logout).");

        SceneManager.getInstance().switchToLoginScene();
    }
}
