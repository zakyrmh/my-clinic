package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class DoctorController {

    @FXML
    private TableView<Doctor> tableView;
    @FXML
    private TableColumn<Doctor, Integer> no;
    @FXML
    private TableColumn<Doctor, String> noSip;
    @FXML
    private TableColumn<Doctor, String> namaLengkap;
    @FXML
    private TableColumn<Doctor, String> spesialisasi;
    @FXML
    private TableColumn<Doctor, String> noTelepon;
    @FXML
    private TableColumn<Doctor, String> statusPraktik;
    @FXML
    private TableColumn<Doctor, Void> action;
    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        noSip.setCellValueFactory(new PropertyValueFactory<>("noSip"));
        namaLengkap.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        spesialisasi.setCellValueFactory(new PropertyValueFactory<>("spesialisasi"));
        noTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        statusPraktik.setCellValueFactory(new PropertyValueFactory<>("statusPraktik"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadDoctorData();
        }
    }

    private void loadDoctorData() {
        ObservableList<Doctor> doctorList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM dokter")) {

            while (rs.next()) {
                Doctor.PracticeStatus statusPraktik = Doctor.PracticeStatus.fromString(rs.getString("status_praktik"));
                Doctor doctor = new Doctor(
                        rs.getInt("id_dokter"),
                        rs.getString("no_sip"),
                        rs.getString("nama_lengkap"),
                        rs.getString("spesialisasi"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("alamat"),
                        rs.getDate("tanggal_bergabung").toLocalDate(),
                        statusPraktik,
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
        System.out.println("View doctor: " + doctor.getNamaLengkap());
        SceneManager.getInstance().switchToDoctorShowScene(doctor);
    }

    // Handler untuk aksi Edit
    private void handleEditAction(Doctor doctor) {
        System.out.println("Edit doctor: " + doctor.getNamaLengkap());
        SceneManager.getInstance().switchToDoctorEditScene(doctor);
    }

    // Handler untuk aksi Hapus
    private void handleDeleteAction(Doctor doctor) {
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM dokter WHERE id_dokter = " + doctor.getIdDokter());
            tableView.getItems().remove(doctor);
            System.out.println("Deleted doctor: " + doctor.getNamaLengkap());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchAction() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadDoctorData();
            return;
        }

        ObservableList<Doctor> filteredList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM dokter WHERE LOWER(nama_lengkap) LIKE ? OR LOWER(no_sip) LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword.toLowerCase() + "%");
            pstmt.setString(2, "%" + keyword.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Doctor.PracticeStatus statusPraktik = Doctor.PracticeStatus.fromString(rs.getString("status_praktik"));
                Doctor doctor = new Doctor(
                        rs.getInt("id_dokter"),
                        rs.getString("no_sip"),
                        rs.getString("nama_lengkap"),
                        rs.getString("spesialisasi"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("alamat"),
                        rs.getDate("tanggal_bergabung").toLocalDate(),
                        statusPraktik,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
                );
                filteredList.add(doctor);
            }

            tableView.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error searching doctor data: " + e.getMessage());
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

    @FXML
    protected void handleVisitLinkAction() {
        SceneManager.getInstance().switchToVisitScene();
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
