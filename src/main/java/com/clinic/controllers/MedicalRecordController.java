package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.clinic.manager.UserSession;
import com.clinic.models.Doctor;
import com.clinic.models.MedicalRecord;
import com.clinic.models.Patient;
import com.clinic.utils.DatabaseUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MedicalRecordController {

    @FXML
    private TableView<MedicalRecord> tableView;
    @FXML
    private TableColumn<MedicalRecord, Integer> no;
    @FXML
    private TableColumn<MedicalRecord, String> namaPasien;
    @FXML
    private TableColumn<MedicalRecord, String> noRekamMedis;
    @FXML
    private TableColumn<MedicalRecord, String> namaDokter;
    @FXML
    private TableColumn<MedicalRecord, String> diagnosisUtama;
    @FXML
    private TableColumn<MedicalRecord, Void> action;
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
        noRekamMedis.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getIdPasien();
            Patient patient = patientMap.get(patientId);
            return new SimpleStringProperty(patient != null ? patient.getNoRm() : "Unknown");
        });
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
        diagnosisUtama.setCellValueFactory(new PropertyValueFactory<>("diagnosisUtama"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadMedicalRecordData();
        }
    }

    private void loadMedicalRecordData() {
        ObservableList<MedicalRecord> medicalRecordList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(
                "SELECT rm.*, p.*, d.* FROM rekam_medis rm LEFT JOIN pasien p ON rm.id_pasien = p.id_pasien LEFT JOIN dokter d ON rm.id_dokter = d.id_dokter")) {
            while (rs.next()) {
                MedicalRecord medicalRecord = new MedicalRecord(
                        rs.getInt("rm.id_rekam_medis"),
                        rs.getInt("rm.id_kunjungan"),
                        rs.getInt("rm.id_pasien"),
                        rs.getInt("rm.id_dokter"),
                        rs.getString("rm.anamnesis"),
                        rs.getString("rm.pemeriksaan_fisik"),
                        rs.getString("rm.tekanan_darah"),
                        rs.getInt("rm.nadi"),
                        rs.getInt("rm.suhu"),
                        rs.getInt("rm.respirasi"),
                        rs.getInt("rm.berat_badan"),
                        rs.getInt("rm.tinggi_badan"),
                        rs.getString("rm.diagnosis_utama"),
                        rs.getString("rm.diagnosis_sekunder"),
                        rs.getString("rm.tindakan"),
                        rs.getString("rm.terapi"),
                        rs.getString("rm.catatan_dokter"),
                        rs.getTimestamp("rm.created_at") != null ? rs.getTimestamp("rm.created_at").toLocalDateTime()
                        : null,
                        rs.getTimestamp("rm.updated_at") != null ? rs.getTimestamp("rm.updated_at").toLocalDateTime()
                        : null);

                medicalRecordList.add(medicalRecord);

                int patientId = rs.getInt("p.id_pasien");
                if (!patientMap.containsKey(patientId)) {
                    Patient.Gender gender = Patient.Gender.fromString(rs.getString("p.jenis_kelamin"));
                    Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                            .fromString(rs.getString("p.status_pernikahan"));
                    Patient patient = new Patient(
                            patientId,
                            rs.getString("p.no_rm"),
                            rs.getString("p.nik"),
                            rs.getString("p.nama_lengkap"),
                            rs.getString("p.tempat_lahir"),
                            rs.getDate("p.tanggal_lahir").toLocalDate(),
                            gender,
                            rs.getString("p.alamat"),
                            rs.getString("p.no_telepon"),
                            rs.getString("p.pekerjaan"),
                            statusPernikahan,
                            rs.getString("p.agama"),
                            rs.getString("p.pendidikan"),
                            rs.getString("p.kontak_darurat"),
                            rs.getString("p.no_telepon_darurat"),
                            rs.getTimestamp("p.created_at") != null
                            ? rs.getTimestamp("p.created_at").toLocalDateTime()
                            : null,
                            rs.getTimestamp("p.updated_at") != null
                            ? rs.getTimestamp("p.updated_at").toLocalDateTime()
                            : null);
                    patientMap.put(patientId, patient);
                }

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

            tableView.setItems(medicalRecordList);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading medical record data: " + e.getMessage());
        }
    }

    private void configureActionColumn() {
        action.setCellFactory(param -> new TableCell<MedicalRecord, Void>() {
            private final Button viewButton = new Button("Lihat");
            private final Button editButton = new Button("Edit");
            private final HBox pane = new HBox(5, viewButton, editButton);

            {
                // Styling tombol
                viewButton.getStyleClass().add("view-button");
                editButton.getStyleClass().add("edit-button");

                // Handler untuk tombol Lihat
                viewButton.setOnAction(event -> {
                    MedicalRecord medicalRecord = getTableView().getItems().get(getIndex());
                    handleViewAction(medicalRecord);
                });

                // Handler untuk tombol Edit
                editButton.setOnAction(event -> {
                    MedicalRecord medicalRecord = getTableView().getItems().get(getIndex());
                    handleEditAction(medicalRecord);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void handleViewAction(MedicalRecord medicalRecord) {
        System.out.println("View medicalRecord: " + medicalRecord.getIdRekamMedis());
        // SceneManager.getInstance().switchToPatientShowScene(medicalRecord);
    }

    private void handleEditAction(MedicalRecord medicalRecord) {
        System.out.println("Edit medicalRecord: " + medicalRecord.getIdRekamMedis());
        // SceneManager.getInstance().switchToPatientEditScene(medicalRecord);
    }

    @FXML
    private void handleSearchAction() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadMedicalRecordData();
            return;
        }

        ObservableList<MedicalRecord> filteredList = FXCollections.observableArrayList();

        String sql = "SELECT rm.*, p.* FROM rekam_medis rm LEFT JOIN pasien p ON rm.id_pasien = p.id_pasien WHERE LOWER(p.nama_lengkap) LIKE ? OR LOWER(p.no_rm) LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection(); var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword.toLowerCase() + "%");
            pstmt.setString(2, "%" + keyword.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MedicalRecord medicalRecord = new MedicalRecord(
                        rs.getInt("rm.id_rekam_medis"),
                        rs.getInt("rm.id_kunjungan"),
                        rs.getInt("rm.id_pasien"),
                        rs.getInt("rm.id_dokter"),
                        rs.getString("rm.anamnesis"),
                        rs.getString("rm.pemeriksaan_fisik"),
                        rs.getString("rm.tekanan_darah"),
                        rs.getInt("rm.nadi"),
                        rs.getInt("rm.suhu"),
                        rs.getInt("rm.respirasi"),
                        rs.getInt("rm.berat_badan"),
                        rs.getInt("rm.tinggi_badan"),
                        rs.getString("rm.diagnosis_utama"),
                        rs.getString("rm.diagnosis_sekunder"),
                        rs.getString("rm.tindakan"),
                        rs.getString("rm.terapi"),
                        rs.getString("rm.catatan_dokter"),
                        rs.getTimestamp("rm.created_at") != null ? rs.getTimestamp("rm.created_at").toLocalDateTime()
                        : null,
                        rs.getTimestamp("rm.updated_at") != null ? rs.getTimestamp("rm.updated_at").toLocalDateTime()
                        : null);

                filteredList.add(medicalRecord);

                int patientId = rs.getInt("p.id_pasien");
                if (!patientMap.containsKey(patientId)) {
                    Patient.Gender gender = Patient.Gender.fromString(rs.getString("p.jenis_kelamin"));
                    Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                            .fromString(rs.getString("p.status_pernikahan"));
                    Patient patient = new Patient(
                            patientId,
                            rs.getString("p.no_rm"),
                            rs.getString("p.nik"),
                            rs.getString("p.nama_lengkap"),
                            rs.getString("p.tempat_lahir"),
                            rs.getDate("p.tanggal_lahir").toLocalDate(),
                            gender,
                            rs.getString("p.alamat"),
                            rs.getString("p.no_telepon"),
                            rs.getString("p.pekerjaan"),
                            statusPernikahan,
                            rs.getString("p.agama"),
                            rs.getString("p.pendidikan"),
                            rs.getString("p.kontak_darurat"),
                            rs.getString("p.no_telepon_darurat"),
                            rs.getTimestamp("p.created_at") != null
                            ? rs.getTimestamp("p.created_at").toLocalDateTime()
                            : null,
                            rs.getTimestamp("p.updated_at") != null
                            ? rs.getTimestamp("p.updated_at").toLocalDateTime()
                            : null);
                    patientMap.put(patientId, patient);
                }

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
            System.err.println("Error searching medical record data: " + e.getMessage());
        }
    }
}
