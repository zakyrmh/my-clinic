package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;
import com.clinic.models.Patient;
import com.clinic.models.Visit;
import com.clinic.utils.DatabaseUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class VisitController {
    @FXML
    private TableView<Visit> tableView;
    @FXML
    private TableColumn<Visit, Integer> no;
    @FXML
    private TableColumn<Visit, String> namaPasien;
    @FXML
    private TableColumn<Visit, String> namaDokter;
    @FXML
    private TableColumn<Visit, String> noAntrian;
    @FXML
    private TableColumn<Visit, String> tanggalKunjungan;
    @FXML
    private TableColumn<Visit, String> statusKunjungan;
    @FXML
    private TableColumn<Visit, Void> action;
    @FXML
    private TextField searchField;

    private final Map<Integer, Patient> patientMap = new HashMap<>();
    private final Map<Integer, Doctor> doctorMap = new HashMap<>();

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        namaPasien.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getIdPasien();
            Patient patient = patientMap.get(patientId);
            return new SimpleStringProperty(patient != null ? patient.getNamaLengkap() : "Unknown");
        });

        namaDokter.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getIdDokter();
            Doctor doctor = doctorMap.get(doctorId);
            return new SimpleStringProperty(doctor != null ? doctor.getNamaLengkap() : "Unknown");
        });
        noAntrian.setCellValueFactory(new PropertyValueFactory<>("noAntrian"));
        tanggalKunjungan.setCellValueFactory(new PropertyValueFactory<>("tanggalKunjungan"));
        statusKunjungan.setCellValueFactory(new PropertyValueFactory<>("statusKunjungan"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadVisitData();
        }
    }

    private void loadVisitData() {
        ObservableList<Visit> visitList = FXCollections.observableArrayList();

        String sql = "SELECT k.*, p.*, d.* " +
                "FROM kunjungan k " +
                "LEFT JOIN pasien p ON k.id_pasien = p.id_pasien " +
                "LEFT JOIN dokter d ON k.id_dokter = d.id_dokter";

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit.VisitStatus status = Visit.VisitStatus.fromString(rs.getString("status_kunjungan"));
                Visit.VisitType type = Visit.VisitType.fromString(rs.getString("jenis_kunjungan"));
                Visit.PaymentMethod caraBayar = Visit.PaymentMethod.fromString(rs.getString("cara_bayar"));
                Visit visit = new Visit(
                        rs.getInt("id_kunjungan"),
                        rs.getInt("id_pasien"),
                        rs.getInt("id_dokter"),
                        rs.getString("no_antrian"),
                        rs.getDate("tanggal_kunjungan").toLocalDate(),
                        rs.getTime("jam_daftar") != null ? rs.getTime("jam_daftar").toLocalTime() : null,
                        rs.getTime("jam_periksa") != null ? rs.getTime("jam_periksa").toLocalTime() : null,
                        rs.getTime("jam_selesai") != null ? rs.getTime("jam_selesai").toLocalTime() : null,
                        type,
                        status,
                        rs.getString("keluhan_utama"),
                        rs.getInt("biaya_konsultasi"),
                        caraBayar,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                visitList.add(visit);

                int patientId = rs.getInt("p.id_pasien");
                if (!patientMap.containsKey(patientId)) {
                    Patient.Gender gender = Patient.Gender.fromString(rs.getString("p.jenis_kelamin"));
                    Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                            .fromString(rs.getString("p.status_pernikahan"));
                    Patient.BloodType bloodType = Patient.BloodType.fromString(rs.getString("p.golongan_darah"));
                    Patient patient = new Patient(
                            patientId,
                            rs.getString("p.no_rm"),
                            rs.getString("p.nik"),
                            rs.getString("p.nama_lengkap"),
                            gender,
                            rs.getDate("p.tanggal_lahir").toLocalDate(),
                            rs.getString("p.tempat_lahir"),
                            rs.getString("p.alamat"),
                            rs.getString("p.no_telepon"),
                            rs.getString("p.email"),
                            rs.getString("p.pekerjaan"),
                            statusPernikahan,
                            bloodType,
                            rs.getTimestamp("p.created_at") != null ? rs.getTimestamp("p.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("p.updated_at") != null ? rs.getTimestamp("p.updated_at").toLocalDateTime()
                                    : null);
                    patientMap.put(patientId, patient);
                }

                // Parse data dokter (Doctor) jika belum ada di map
                int doctorId = rs.getInt("d.id_dokter");
                if (!doctorMap.containsKey(doctorId)) {
                    Doctor.PracticeStatus statusPraktik = Doctor.PracticeStatus
                            .fromString(rs.getString("d.status_praktik"));
                    Doctor doctor = new Doctor(
                            doctorId,
                            rs.getString("d.no_sip"),
                            rs.getString("d.nama_lengkap"),
                            rs.getString("d.spesialisasi"),
                            rs.getString("d.no_telepon"),
                            rs.getString("d.email"),
                            rs.getString("d.alamat"),
                            rs.getDate("d.tanggal_bergabung").toLocalDate(),
                            statusPraktik,
                            rs.getTimestamp("d.created_at") != null ? rs.getTimestamp("d.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("d.updated_at") != null ? rs.getTimestamp("d.updated_at").toLocalDateTime()
                                    : null);
                    doctorMap.put(doctorId, doctor);
                }
            }

            // Mengisi TableView dengan data
            tableView.setItems(visitList);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading visit data: " + e.getMessage());
        }
    }

    private void configureActionColumn() {
        action.setCellFactory(param -> new TableCell<Visit, Void>() {
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
                    Visit visit = getTableView().getItems().get(getIndex());
                    handleViewAction(visit);
                });

                // Handler untuk tombol Edit
                editButton.setOnAction(event -> {
                    Visit visit = getTableView().getItems().get(getIndex());
                    handleEditAction(visit);
                });

                // Handler untuk tombol Hapus
                deleteButton.setOnAction(event -> {
                    Visit visit = getTableView().getItems().get(getIndex());
                    handleDeleteAction(visit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void handleViewAction(Visit visit) {
        // Logika untuk menampilkan detail kunjungan
    }

    private void handleEditAction(Visit visit) {
        SceneManager.getInstance().switchToVisitEditScene(visit);
    }

    private void handleDeleteAction(Visit visit) {
        // 1) Tampilkan konfirmasi
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin menghapus kunjungan dengan ID "
                + visit.getIdKunjungan() + " beserta data rekam medis & pembayaran?");

        // Tunggu pilihan user
        Optional<ButtonType> pilihan = confirm.showAndWait();
        if (pilihan.isEmpty() || pilihan.get() != ButtonType.OK) {
            // Jika bukan OK, batalkan
            return;
        }

        // 2) Jika OK, lanjutkan hapus
        String sqlDeletePembayaran = "DELETE FROM pembayaran WHERE id_kunjungan = ?";
        String sqlDeleteRekamMedis = "DELETE FROM rekam_medis WHERE id_kunjungan = ?";
        String sqlDeleteKunjungan = "DELETE FROM kunjungan WHERE id_kunjungan = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psPemb = conn.prepareStatement(sqlDeletePembayaran);
                PreparedStatement psRekm = conn.prepareStatement(sqlDeleteRekamMedis);
                PreparedStatement psKunj = conn.prepareStatement(sqlDeleteKunjungan)) {

            conn.setAutoCommit(false); // transaksi

            // Hapus pembayaran
            psPemb.setInt(1, visit.getIdKunjungan());
            psPemb.executeUpdate();

            // Hapus rekam medis
            psRekm.setInt(1, visit.getIdKunjungan());
            psRekm.executeUpdate();

            // Hapus kunjungan
            psKunj.setInt(1, visit.getIdKunjungan());
            psKunj.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);

            // Update UI
            tableView.getItems().remove(visit);
            System.out.println("Deleted visit and related data: " + visit.getIdKunjungan());

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // rollback jika error
                Connection conn = DatabaseUtil.getConnection();
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error deleting visit: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchAction() {
        String keyword = searchField.getText();

        if (keyword.isEmpty()) {
            loadVisitData();
            return;
        }

        ObservableList<Visit> filteredList = FXCollections.observableArrayList();

        String sql = "SELECT k.*, p.*, d.* " +
                "FROM kunjungan k " +
                "LEFT JOIN pasien p ON k.id_pasien = p.id_pasien " +
                "LEFT JOIN dokter d ON k.id_dokter = d.id_dokter " +
                "WHERE LOWER(p.nama_lengkap) LIKE ? OR LOWER(d.nama_lengkap) LIKE ? OR LOWER(k.no_antrian) LIKE ?;";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword.toLowerCase() + "%");
            ps.setString(3, "%" + keyword.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Visit.VisitStatus status = Visit.VisitStatus.fromString(rs.getString("status_kunjungan"));
                Visit.VisitType type = Visit.VisitType.fromString(rs.getString("jenis_kunjungan"));
                Visit.PaymentMethod caraBayar = Visit.PaymentMethod.fromString(rs.getString("cara_bayar"));
                Visit visit = new Visit(
                        rs.getInt("id_kunjungan"),
                        rs.getInt("id_pasien"),
                        rs.getInt("id_dokter"),
                        rs.getString("no_antrian"),
                        rs.getDate("tanggal_kunjungan").toLocalDate(),
                        rs.getTime("jam_daftar") != null ? rs.getTime("jam_daftar").toLocalTime() : null,
                        rs.getTime("jam_periksa") != null ? rs.getTime("jam_periksa").toLocalTime() : null,
                        rs.getTime("jam_selesai") != null ? rs.getTime("jam_selesai").toLocalTime() : null,
                        type,
                        status,
                        rs.getString("keluhan_utama"),
                        rs.getInt("biaya_konsultasi"),
                        caraBayar,
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                filteredList.add(visit);

                int patientId = rs.getInt("p.id_pasien");
                if (!patientMap.containsKey(patientId)) {
                    Patient.Gender gender = Patient.Gender.fromString(rs.getString("p.jenis_kelamin"));
                    Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                            .fromString(rs.getString("p.status_pernikahan"));
                    Patient.BloodType bloodType = Patient.BloodType.fromString(rs.getString("p.golongan_darah"));
                    Patient patient = new Patient(
                            patientId,
                            rs.getString("p.no_rm"),
                            rs.getString("p.nik"),
                            rs.getString("p.nama_lengkap"),
                            gender,
                            rs.getDate("p.tanggal_lahir").toLocalDate(),
                            rs.getString("p.tempat_lahir"),
                            rs.getString("p.alamat"),
                            rs.getString("p.no_telepon"),
                            rs.getString("p.email"),
                            rs.getString("p.pekerjaan"),
                            statusPernikahan,
                            bloodType,
                            rs.getTimestamp("p.created_at") != null ? rs.getTimestamp("p.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("p.updated_at") != null ? rs.getTimestamp("p.updated_at").toLocalDateTime()
                                    : null);
                    patientMap.put(patientId, patient);
                }

                // Parse data dokter (Doctor) jika belum ada di map
                int doctorId = rs.getInt("d.id_dokter");
                if (!doctorMap.containsKey(doctorId)) {
                    Doctor.PracticeStatus statusPraktik = Doctor.PracticeStatus
                            .fromString(rs.getString("d.status_praktik"));
                    Doctor doctor = new Doctor(
                            doctorId,
                            rs.getString("d.no_sip"),
                            rs.getString("d.nama_lengkap"),
                            rs.getString("d.spesialisasi"),
                            rs.getString("d.no_telepon"),
                            rs.getString("d.email"),
                            rs.getString("d.alamat"),
                            rs.getDate("d.tanggal_bergabung").toLocalDate(),
                            statusPraktik,
                            rs.getTimestamp("d.created_at") != null ? rs.getTimestamp("d.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("d.updated_at") != null ? rs.getTimestamp("d.updated_at").toLocalDateTime()
                                    : null);
                    doctorMap.put(doctorId, doctor);
                }
            }

            tableView.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error searching visit data: " + e.getMessage());
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
    protected void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPatientScene();
    }

    @FXML
    protected void handleDoctorLinkAction() {
        SceneManager.getInstance().switchToDoctorScene();
    }

    @FXML
    protected void handleVisitAddAction() {
        SceneManager.getInstance().switchToVisitAddScene();
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
