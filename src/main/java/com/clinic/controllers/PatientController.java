package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;
import com.clinic.models.Patient.Gender;
import com.clinic.utils.DatabaseUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientController {
    @FXML private TableView<Patient> tableView;
    @FXML private TableColumn<Patient, Integer> no;
    @FXML private TableColumn<Patient, String> name;
    @FXML private TableColumn<Patient, String> medicalRecord;
    @FXML private TableColumn<Patient, String> dateOfBirth;
    @FXML private TableColumn<Patient, String> gender;
    
    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        medicalRecord.setCellValueFactory(new PropertyValueFactory<>("medicalRecord"));
        dateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        if (UserSession.getInstance().isLoggedIn()) {
            loadPatientData();
        }
    }

    private void loadPatientData() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {

            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("id"),
                    rs.getString("medical_record"),
                    rs.getString("name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("gender").equals("male") ? Gender.MALE : Gender.FEMALE,
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("identity_number"),
                    rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
                );
                patientList.add(patient);
            }

            // Mengisi TableView dengan data
            tableView.setItems(patientList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading patient data: " + e.getMessage());
        }
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
    protected void handlePatientAddAction(ActionEvent event) {
        SceneManager.getInstance().switchToPatientAddScene();
    }
}
