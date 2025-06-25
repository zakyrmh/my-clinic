package com.clinic.controllers;

import java.io.IOException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainLayoutController {
    @FXML
    private StackPane contentArea;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnPasien;
    @FXML
    private Button btnDokter;
    @FXML
    private Button btnKunjungan;
    @FXML
    private Button btnRekamMedis;
    @FXML
    private Button btnPembayaran;

    private Button[] allButtons;

    public MainLayoutController() {
        allButtons = new Button[5];
    }

    @FXML
    private void initialize() {
        allButtons = new Button[] { btnDashboard, btnPasien, btnDokter, btnKunjungan, btnRekamMedis, btnPembayaran };
        setPageContent("/com/clinic/view/DashboardView.fxml");
        setActive("btnDashboard");
    }

    public void setPageContent(String fxmlPath) {
        try {
            Node page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            System.err.println("Gagal load content: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void setActive(String activeId) {
        for (Button b : allButtons) {
            b.getStyleClass().remove("sidebar-button-active");
            if (!b.getStyleClass().contains("sidebar-button")) {
                b.getStyleClass().add("sidebar-button");
            }
            if (b.getId().equals(activeId)) {
                b.getStyleClass().remove("sidebar-button");
                b.getStyleClass().add("sidebar-button-active");
            }
        }
    }

    public StackPane getContentArea() {
        return contentArea;
    }

    @FXML
    private void handleDashboardLinkAction() {
        SceneManager.getInstance().switchToDashboard();
        setActive("btnDashboard");
    }

    @FXML
    protected void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPatientScene();
        setActive("btnPasien");
    }

    @FXML
    protected void handleDoctorLinkAction() {
        SceneManager.getInstance().switchToDoctorScene();
        setActive("btnDokter");
    }

    @FXML
    protected void handleVisitLinkAction() {
        SceneManager.getInstance().switchToVisitScene();
        setActive("btnKunjungan");
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
        setActive("btnRekamMedis");
    }

    @FXML
    protected void handlePaymentLinkAction() {
        SceneManager.getInstance().switchToPaymentScene();
        setActive("btnPembayaran");
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().endSession();
        System.out.println("The user session has ended (logout).");

        SceneManager.getInstance().switchToLoginScene();
    }
}
