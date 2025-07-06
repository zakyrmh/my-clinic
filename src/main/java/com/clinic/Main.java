package com.clinic;

import java.io.IOException;
import java.io.PrintWriter;

import com.clinic.manager.SceneManager;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.setPrimaryStage(primaryStage);

            primaryStage.setTitle("Aplikasi Klinik");
            primaryStage.setResizable(true);

            sceneManager.switchToLoginScene();
        } catch (Exception e) {
            e.printStackTrace();

            try (PrintWriter pw = new PrintWriter("error.log")) {
                e.printStackTrace(pw);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal Error");
            alert.setHeaderText("Application Failed to Start");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
