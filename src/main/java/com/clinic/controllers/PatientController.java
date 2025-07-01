package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<Patient, String> jenisKelamin;
    @FXML
    private TableColumn<Patient, String> tanggalLahir;
    @FXML
    private TableColumn<Patient, String> noTelepon;
    @FXML
    private TableColumn<Patient, Void> action;
    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        noRm.setCellValueFactory(new PropertyValueFactory<>("noRm"));
        namaLengkap.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        jenisKelamin.setCellValueFactory(cellData -> {
            Gender g = cellData.getValue().getJenisKelamin();
            String label;
            if (null == g) {
                label = "";
            } else {
                label = switch (g) {
                    case MALE ->
                        "Laki-laki";
                    case FEMALE ->
                        "Perempuan";
                    default ->
                        "";
                };
            }
            return new SimpleStringProperty(label);
        });
        tanggalLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        noTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadPatientData();
        }
    }

    private void loadPatientData() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM pasien")) {

            while (rs.next()) {
                Patient.Gender gender = Patient.Gender.fromString(rs.getString("jenis_kelamin"));
                Patient.MaritalStatus maritalStatus = Patient.MaritalStatus.fromString(rs.getString("status_pernikahan"));
                Patient patient = new Patient(
                        rs.getInt("id_pasien"),
                        rs.getString("no_rm"),
                        rs.getString("nik"),
                        rs.getString("nama_lengkap"),
                        rs.getString("tempat_lahir"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        gender,
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("pekerjaan"),
                        maritalStatus,
                        rs.getString("agama"),
                        rs.getString("pendidikan"),
                        rs.getString("kontak_darurat"),
                        rs.getString("no_telepon_darurat"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                patientList.add(patient);
            }

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
                viewButton.getStyleClass().add("view-button");
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                viewButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    handleViewAction(patient);
                });

                editButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    handleEditAction(patient);
                });

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

    @FXML
    private void handleSearchAction() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadPatientData();
            return;
        }

        ObservableList<Patient> filteredList = FXCollections.observableArrayList();

        String sql = "("
                + "SELECT *, 1 AS priority "
                + "FROM pasien "
                + "WHERE no_rm = ? "
                + "LIMIT 20"
                + ") "
                + "UNION ALL "
                + "("
                + "SELECT *, 2 AS priority "
                + "FROM pasien "
                + "WHERE nik = ? "
                + "LIMIT 20"
                + ") "
                + "UNION ALL "
                + "("
                + "SELECT *, 3 AS priority "
                + "FROM pasien "
                + "WHERE nama_lengkap LIKE ? "
                + "LIMIT 20"
                + ") "
                + "ORDER BY priority, nama_lengkap "
                + "LIMIT 20";

        try (Connection conn = DatabaseUtil.getConnection(); var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            pstmt.setString(3, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Patient.Gender gender = Patient.Gender.fromString(rs.getString("jenis_kelamin"));
                Patient.MaritalStatus maritalStatus = Patient.MaritalStatus.fromString(rs.getString("status_pernikahan"));

                Patient patient = new Patient(
                        rs.getInt("id_pasien"),
                        rs.getString("no_rm"),
                        rs.getString("nik"),
                        rs.getString("nama_lengkap"),
                        rs.getString("tempat_lahir"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        gender,
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("pekerjaan"),
                        maritalStatus,
                        rs.getString("agama"),
                        rs.getString("pendidikan"),
                        rs.getString("kontak_darurat"),
                        rs.getString("no_telepon_darurat"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

                filteredList.add(patient);
            }

            tableView.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching patient data: " + e.getMessage());
        }
    }

    private void handleViewAction(Patient patient) {
        SceneManager.getInstance().switchToPatientShowScene(patient);
    }

    private void handleEditAction(Patient patient) {
        SceneManager.getInstance().switchToPatientEditScene(patient);
    }

    private void handleDeleteAction(Patient patient) {
        String checkVisitSql = "SELECT COUNT(*) FROM kunjungan WHERE id_pasien = ?";
        String deletePatientSql = "DELETE FROM pasien WHERE id_pasien = ?";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement checkStmt = conn.prepareStatement(checkVisitSql)) {

            // 1. Cek apakah ada kunjungan untuk pasien ini
            checkStmt.setInt(1, patient.getIdPasien());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Pasien masih punya data kunjungan: tampilkan alert dan batalkan delete
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Tidak Bisa Dihapus");
                    alert.setHeaderText("Pasien Memiliki Data Kunjungan");
                    alert.setContentText(
                            "Pasien \"" + patient.getNamaLengkap()
                            + "\" tidak dapat dihapus karena masih memiliki "
                            + rs.getInt(1) + " kunjungan.\n"
                            + "Silakan hapus data kunjungan terlebih dahulu."
                    );
                    alert.showAndWait();
                    return;
                }
            }

            // 2. Jika tidak ada kunjungan, lanjutkan delete
            try (PreparedStatement deleteStmt = conn.prepareStatement(deletePatientSql)) {
                deleteStmt.setInt(1, patient.getIdPasien());
                int affected = deleteStmt.executeUpdate();
                if (affected > 0) {
                    tableView.getItems().remove(patient);
                    System.out.println("Deleted patient: " + patient.getNamaLengkap());
                } else {
                    System.out.println("No patient deleted, id_pasien not found: " + patient.getIdPasien());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Tampilkan error dialog jika perlu
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Gagal Menghapus Pasien");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    protected void handlePatientAddAction() {
        SceneManager.getInstance().switchToPatientAddScene();
    }
}
