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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class DoctorController {
    @FXML private TableView<Doctor> tableView;
    @FXML private TableColumn<Doctor, Integer> no;
    @FXML private TableColumn<Doctor, String> licenseNo;
    @FXML private TableColumn<Doctor, String> name;
    @FXML private TableColumn<Doctor, String> specialization;
    @FXML private TableColumn<Doctor, String> phone;
    @FXML private TableColumn<Doctor, Void> action;
    
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

        configureActionColumn();

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

    private void configureActionColumn() {
        action.setCellFactory(param -> new TableCell<Doctor, Void>() {
            private final Button viewButton = new Button("Lihat");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Hapus");
            private final HBox pane = new HBox(5, viewButton, editButton, deleteButton);

            {
                // Styling tombol
                viewButton.getStyleClass().add("view-button");
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                // Handler untuk tombol Lihat
                viewButton.setOnAction(event -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleViewAction(doctor);
                });

                // Handler untuk tombol Edit
                editButton.setOnAction(event -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleEditAction(doctor);
                });

                // Handler untuk tombol Hapus
                deleteButton.setOnAction(event -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleDeleteAction(doctor);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    // Handler untuk aksi Lihat
    private void handleViewAction(Doctor doctor) {
        // TODO: Implementasi navigasi ke halaman detail pasien
        System.out.println("View doctor: " + doctor.getName());
        // Contoh: SceneManager.getInstance().switchToDoctorDetailScene(doctor);
    }

    // Handler untuk aksi Edit
    private void handleEditAction(Doctor doctor) {
        // TODO: Implementasi navigasi ke halaman edit pasien
        System.out.println("Edit doctor: " + doctor.getName());
        SceneManager.getInstance().switchToDoctorEditScene(doctor);
    }

    // Handler untuk aksi Hapus
    private void handleDeleteAction(Doctor doctor) {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM doctors WHERE id = " + doctor.getId());
            tableView.getItems().remove(doctor);
            System.out.println("Deleted doctor: " + doctor.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting doctor: " + e.getMessage());
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
