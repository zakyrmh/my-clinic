package com.clinic.manager;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import com.clinic.controllers.DoctorEditController;
import com.clinic.controllers.DoctorShowController;
import com.clinic.controllers.MainLayoutController;
import com.clinic.controllers.PatientEditController;
import com.clinic.controllers.PatientShowController;
import com.clinic.controllers.VisitEditController;
import com.clinic.models.Doctor;
import com.clinic.models.Patient;
import com.clinic.models.Visit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private MainLayoutController mainLayoutController;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ————————————————
    // 1) SCENES TANPA LAYOUT
    // ————————————————

    public void switchToLoginScene() {
        loadScene("/com/clinic/view/LoginView.fxml");
    }

    public void switchToRegisterScene() {
        loadScene("/com/clinic/view/RegisterView.fxml");
    }

    /**
     * Utility untuk load FXML biasa (login/register)
     */
    private void loadScene(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("FXML not found: " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            // stylesheet global (jika ada)
            URL css = getClass().getResource("/com/clinic/css/style.css");
            if (css != null)
                scene.getStylesheets().add(css.toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // ————————————————
    // 2) INISIALISASI MAIN LAYOUT (hanya sekali)
    // ————————————————

    /**
     * Dipanggil sekali setelah login sukses.
     * Load MainLayout.fxml, simpan controller, dan tampilkan scene.
     * Lalu otomatis navigasi ke Dashboard.
     */
    public void showMainLayout() { // ← baru
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/clinic/view/layout/MainLayout.fxml"));
            Parent root = loader.load();
            mainLayoutController = loader.getController();

            Scene scene = new Scene(root);
            URL css = getClass().getResource("/com/clinic/css/style.css");
            if (css != null)
                scene.getStylesheets().add(css.toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

            // load default content → langsung Dashboard
            switchToDashboard(); // ← memanfaatkan controller yang sudah ada

        } catch (IOException e) {
            System.err.println("Failed to load MainLayout.fxml");
            e.printStackTrace();
        }
    }

    // ————————————————
    // 3) NAVIGASI DENGAN MAIN LAYOUT (swap konten + set aktif)
    // ————————————————

    public void switchToDashboard() {
        // hanya swap content, tidak reload layout
        mainLayoutController.setPageContent("/com/clinic/view/DashboardView.fxml");
        mainLayoutController.setActive("btnDashboard");
    }

    public void switchToPatientScene() {
        mainLayoutController.setPageContent("/com/clinic/view/patients/PatientView.fxml");
        mainLayoutController.setActive("btnPasien");
    }

    public void switchToPatientAddScene() {
        mainLayoutController.setPageContent("/com/clinic/view/patients/PatientAdd.fxml");
        mainLayoutController.setActive("btnPasien");
    }

    public void switchToDoctorScene() {
        mainLayoutController.setPageContent("/com/clinic/view/doctors/DoctorView.fxml");
        mainLayoutController.setActive("btnDokter");
    }

    public void switchToDoctorAddScene() {
        mainLayoutController.setPageContent("/com/clinic/view/doctors/DoctorAdd.fxml");
        mainLayoutController.setActive("btnDokter");
    }

    public void switchToVisitScene() {
        mainLayoutController.setPageContent("/com/clinic/view/visits/VisitView.fxml");
        mainLayoutController.setActive("btnKunjungan");
    }

    public void switchToVisitAddScene() {
        mainLayoutController.setPageContent("/com/clinic/view/visits/VisitAdd.fxml");
        mainLayoutController.setActive("btnKunjungan");
    }

    public void switchToMedicalRecordScene() {
        mainLayoutController.setPageContent("/com/clinic/view/medicalRecords/MedicalRecordView.fxml");
        mainLayoutController.setActive("btnRekamMedis");
    }

    public void switchToPaymentScene() {
        mainLayoutController.setPageContent("/com/clinic/view/payments/PaymentView.fxml");
        mainLayoutController.setActive("btnPembayaran");
    }

    // ————————————————
    // 4) HALAMAN DENGAN PARAMETER (model injection)
    // ————————————————

    /**
     * Generic injector: load pageFxml, inject ke controller, lalu tampilkan
     */
    private <C> void loadPageWithController(String pageFxml, Consumer<C> injector) { // ← baru
        try {
            FXMLLoader pageLoader = new FXMLLoader(getClass().getResource(pageFxml));
            Parent pageRoot = pageLoader.load();

            @SuppressWarnings("unchecked")
            C controller = (C) pageLoader.getController();
            injector.accept(controller);

            mainLayoutController.getContentArea().getChildren().setAll(pageRoot);
        } catch (IOException e) {
            System.err.println("Failed to load page: " + pageFxml);
            e.printStackTrace();
        }
    }

    public void switchToPatientShowScene(Patient patient) {
        loadPageWithController(
                "/com/clinic/view/patients/PatientShow.fxml",
                (PatientShowController ctrl) -> ctrl.setPatientData(patient));
        mainLayoutController.setActive("btnPasien");
    }

    public void switchToPatientEditScene(Patient patient) {
        loadPageWithController(
                "/com/clinic/view/patients/PatientEdit.fxml",
                (PatientEditController ctrl) -> ctrl.setPatientData(patient));
        mainLayoutController.setActive("btnPasien");
    }

    public void switchToDoctorEditScene(Doctor doctor) {
        loadPageWithController(
                "/com/clinic/view/doctors/DoctorEdit.fxml",
                (DoctorEditController ctrl) -> ctrl.setDoctorData(doctor));
        mainLayoutController.setActive("btnDokter");
    }

    public void switchToDoctorShowScene(Doctor doctor) {
        loadPageWithController(
                "/com/clinic/view/doctors/DoctorShow.fxml",
                (DoctorShowController ctrl) -> ctrl.setDoctorData(doctor));
        mainLayoutController.setActive("btnDokter");
    }

    public void switchToVisitEditScene(Visit visit) {
        loadPageWithController(
                "/com/clinic/view/visits/VisitEdit.fxml",
                (VisitEditController ctrl) -> ctrl.setVisitData(visit));
        mainLayoutController.setActive("btnKunjungan");
    }
}