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

import javafx.beans.property.SimpleStringProperty;
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

public class PatientController {
    @FXML
    private TableView<Patient> tableView;
    @FXML
    private TableColumn<Patient, Integer> no;
    @FXML
    private TableColumn<Patient, String> noRm;
    @FXML
    private TableColumn<Patient, String> namaLengkap;
    @FXML
    private TableColumn<Patient, String> nik;
    @FXML
    private TableColumn<Patient, String> jenisKelamin;
    @FXML
    private TableColumn<Patient, String> tanggalLahir;
    @FXML
    private TableColumn<Patient, Void> action;

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        noRm.setCellValueFactory(new PropertyValueFactory<>("noRm"));
        namaLengkap.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        nik.setCellValueFactory(new PropertyValueFactory<>("nik"));
        jenisKelamin.setCellValueFactory(cellData -> {
            Gender g = cellData.getValue().getJenisKelamin();
            String label;
            if (null == g) {
                label = "";
            } else label = switch (g) {
                case MALE -> "Laki-laki";
                case FEMALE -> "Perempuan";
                default -> "";
            };
            return new SimpleStringProperty(label);
        });
        tanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadPatientData();
        }
    }

    private void loadPatientData() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM pasien")) {

            while (rs.next()) {
                Patient.Gender gender = Patient.Gender.fromString(rs.getString("jenis_kelamin"));
                Patient.MaritalStatus status = Patient.MaritalStatus.fromString(rs.getString("status_pernikahan"));
                Patient.BloodType bloodType = Patient.BloodType.fromString(rs.getString("golongan_darah"));
                Patient patient = new Patient(
                        rs.getInt("id_pasien"),
                        rs.getString("no_rm"),
                        rs.getString("nik"),
                        rs.getString("nama_lengkap"),
                        gender,
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        rs.getString("tempat_lahir"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("pekerjaan"),
                        status,
                        bloodType,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                patientList.add(patient);
            }

            // Mengisi TableView dengan data
            tableView.setItems(patientList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading patient data: " + e.getMessage());
        }
    }

    private void configureActionColumn() {
        action.setCellFactory(param -> new TableCell<Patient, Void>() {
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
                    Patient patient = getTableView().getItems().get(getIndex());
                    handleViewAction(patient);
                });

                // Handler untuk tombol Edit
                editButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    handleEditAction(patient);
                });

                // Handler untuk tombol Hapus
                deleteButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    handleDeleteAction(patient);
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
    private void handleViewAction(Patient patient) {
        // TODO: Implementasi navigasi ke halaman detail pasien
        System.out.println("View patient: " + patient.getNamaLengkap());
        SceneManager.getInstance().switchToPatientShowScene();
    }

    // Handler untuk aksi Edit
    private void handleEditAction(Patient patient) {
        System.out.println("Edit patient: " + patient.getNamaLengkap());
        SceneManager.getInstance().switchToPatientEditScene(patient);
    }

    // Handler untuk aksi Hapus
    private void handleDeleteAction(Patient patient) {
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM pasien WHERE id_pasien = " + patient.getIdPasien());
            tableView.getItems().remove(patient);
            System.out.println("Deleted patient: " + patient.getNamaLengkap());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting patient: " + e.getMessage());
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

    @FXML
    protected void handleDoctorLinkAction() {
        SceneManager.getInstance().switchToDoctorScene();
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
