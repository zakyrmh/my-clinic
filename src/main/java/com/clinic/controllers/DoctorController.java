package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;
import com.clinic.utils.DatabaseUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DoctorController {
    @FXML private TableView<Doctor> tableView;
    @FXML private TableColumn<Doctor, Integer> no;
    @FXML private TableColumn<Doctor, String> licenseNo;
    @FXML private TableColumn<Doctor, String> name;
    @FXML private TableColumn<Doctor, String> specialization;
    @FXML private TableColumn<Doctor, String> phone;
    
    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        licenseNo.setCellValueFactory(new PropertyValueFactory<>("licenseNo"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        if (UserSession.getInstance().isLoggedIn()) {
            loadDoctorData();
        }
    }

    private void loadDoctorData() {
        ObservableList<Doctor> doctorList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {

            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("id"),
                    rs.getString("license_no"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
                );
                doctorList.add(doctor);
            }

            // Mengisi TableView dengan data
            tableView.setItems(doctorList);

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
    protected void handleDoctorAddAction(ActionEvent event) {
        SceneManager.getInstance().switchToDoctorAddScene();
    }

    @FXML
    protected void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPatientScene();
    }
}
