package com.clinic.manager;

import java.io.IOException;
import java.net.URL;

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