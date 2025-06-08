package com.clinic;

import com.clinic.manager.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setPrimaryStage(primaryStage);
        
        primaryStage.setTitle("Aplikasi Klinik");
        primaryStage.setResizable(false);
        
        sceneManager.switchToRegisterScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}