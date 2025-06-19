package com.clinic.manager;

import java.io.IOException;
import java.net.URL;

import com.clinic.controllers.PatientEditController;
import com.clinic.controllers.DoctorEditController;
import com.clinic.models.Patient;
import com.clinic.models.Doctor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {}
    
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchToLoginScene() {
        loadScene("/com/clinic/view/LoginView.fxml");
    }

    public void switchToRegisterScene() {
        loadScene("/com/clinic/view/RegisterView.fxml");
    }

    public void switchToDashboard() {
        loadScene("/com/clinic/view/DashboardView.fxml");
    }

    public void switchToPatientScene() {
        loadScene("/com/clinic/view/patients/PatientView.fxml");
    }

    public void switchToPatientAddScene() {
        loadScene("/com/clinic/view/patients/PatientAdd.fxml");
    }

    public void switchToDoctorScene() {
        loadScene("/com/clinic/view/doctors/DoctorView.fxml");
    }

    public void switchToDoctorAddScene() {
        loadScene("/com/clinic/view/doctors/DoctorAdd.fxml");
    }

    public void switchToPatientEditScene(Patient patient) {
        try {
            URL fxmlUrl = getClass().getResource("/com/clinic/view/patients/PatientEdit.fxml");
            if (fxmlUrl == null) {
                System.err.println("Tidak dapat menemukan file FXML: /com/clinic/view/patients/PatientEdit.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Ambil controller dari loader dan set data pasien
            PatientEditController controller = loader.getController();
            controller.setPatientData(patient);

            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("/com/clinic/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Edit Patient");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat scene: /com/clinic/view/patients/PatientEdit.fxml");
            e.printStackTrace();
        }
    }

    public void switchToDoctorEditScene(Doctor doctor) {
        try {
            URL fxmlUrl = getClass().getResource("/com/clinic/view/doctors/doctorEdit.fxml");
            if (fxmlUrl == null) {
                System.err.println("Tidak dapat menemukan file FXML: /com/clinic/view/doctors/doctorEdit.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Ambil controller dari loader dan set data pasien
            DoctorEditController controller = loader.getController();
            controller.setDoctorData(doctor);

            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("/com/clinic/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Edit Doctor");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat scene: /com/clinic/view/doctors/DoctorEdit.fxml");
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Tidak dapat menemukan file FXML: " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);
            
            URL cssUrl = getClass().getResource("/com/clinic/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat scene: " + fxmlPath);
            e.printStackTrace();
        }
    }
}