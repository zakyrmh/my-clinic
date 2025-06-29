package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;
import com.clinic.models.Visit;
import com.clinic.utils.DatabaseUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientShowController {
    @FXML
    private Label noRmLabel;
    @FXML
    private Label nikLabel;
    @FXML
    private Label namaLengkapLabel;
    @FXML
    private Label jenisKelaminLabel;
    @FXML
    private Label ttlLabel;
    @FXML
    private Label alamatLabel;
    @FXML
    private Label noTeleponLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label pekerjaanLabel;
    @FXML
    private Label statusPernikahanLabel;
    @FXML
    private Label golonganDarahLabel;
    @FXML
    private TableView<Visit> tableView;
    @FXML
    private TableColumn<Visit, Integer> no;
    @FXML
    private TableColumn<Visit, String> tanggalKunjungan;
    @FXML
    private TableColumn<Visit, String> jamDaftar;
    @FXML
    private TableColumn<Visit, String> jamPeriksa;
    @FXML
    private TableColumn<Visit, String> jamSelesai;
    @FXML
    private TableColumn<Visit, String> jenisKunjungan;
    @FXML
    private TableColumn<Visit, String> statusKunjungan;
    @FXML
    private TableColumn<Visit, String> keluhanUtama;

    private Patient patient;

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        tanggalKunjungan.setCellValueFactory(new PropertyValueFactory<>("tanggalKunjungan"));
        jamDaftar.setCellValueFactory(new PropertyValueFactory<>("jamDaftar"));
        jamPeriksa.setCellValueFactory(new PropertyValueFactory<>("jamPeriksa"));
        jamSelesai.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        jenisKunjungan.setCellValueFactory(new PropertyValueFactory<>("jenisKunjungan"));
        statusKunjungan.setCellValueFactory(new PropertyValueFactory<>("statusKunjungan"));
        keluhanUtama.setCellValueFactory(new PropertyValueFactory<>("keluhanUtama"));
    }

    private void loadVisitData() {
        ObservableList<Visit> visitList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM kunjungan WHERE id_pasien = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patient.getIdPasien());
            ResultSet rs = pstmt.executeQuery();
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
            }

            // Mengisi TableView dengan data
            tableView.setItems(visitList);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading visit data: " + e.getMessage());
        }
    }

    public void setPatientData(Patient patient) {
        this.patient = patient;

        noRmLabel.setText(patient.getNoRm());
        nikLabel.setText(patient.getNik());
        namaLengkapLabel.setText(patient.getNamaLengkap());
        jenisKelaminLabel.setText(patient.getJenisKelamin() == Patient.Gender.MALE ? "Laki-laki" : "Perempuan");
        ttlLabel.setText(patient.getTanggalLahir().toString());
        alamatLabel.setText(patient.getAlamat());
        noTeleponLabel.setText(patient.getNoTelepon());
        pekerjaanLabel.setText(patient.getPekerjaan());
        statusPernikahanLabel.setText(patient.getStatusPernikahan().toString());

        loadVisitData();
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
    protected void handleVisitLinkAction() {
        SceneManager.getInstance().switchToVisitScene();
    }

    @FXML
    protected void handleMedicalRecordLinkAction() {
        SceneManager.getInstance().switchToMedicalRecordScene();
    }
}
