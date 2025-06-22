package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Visit.VisitStatus;
import com.clinic.utils.DatabaseUtil;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {
    @FXML private Label labelJumlahPasienHariIni;
    @FXML private Label labelJumlahKunjunganHariIni;
    @FXML private Label labelPasienDalamAntrian;

    @FXML private TableView<AntrianRecord> tableAntrian;
    @FXML private TableColumn<AntrianRecord, String> colNoAntrian;
    @FXML private TableColumn<AntrianRecord, String> colNamaPasien;
    @FXML private TableColumn<AntrianRecord, String> colNamaDokter;
    @FXML private TableColumn<AntrianRecord, VisitStatus> colStatusKunjungan;

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            SceneManager.getInstance().switchToLoginScene();
            return;
        }

        colNoAntrian.setCellValueFactory(new PropertyValueFactory<>("noAntrian"));
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colNamaDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colStatusKunjungan.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data
        loadStats();
        loadAntrianTable();
    }

    private void loadStats() {
        LocalDate today = LocalDate.now();
        String sqlPasien    = "SELECT COUNT(*) FROM pasien WHERE DATE(created_at) = ?";
        String sqlKunjungan = "SELECT COUNT(*) FROM kunjungan WHERE tanggal_kunjungan = ?";
        String sqlAntrian   = "SELECT COUNT(*) FROM kunjungan WHERE tanggal_kunjungan = ? AND status_kunjungan = 'Menunggu'";

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Jumlah pasien hari ini
            try (PreparedStatement ps = conn.prepareStatement(sqlPasien)) {
                ps.setDate(1, java.sql.Date.valueOf(today));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) labelJumlahPasienHariIni.setText(String.valueOf(rs.getInt(1)));
            }

            // Jumlah kunjungan hari ini
            try (PreparedStatement ps = conn.prepareStatement(sqlKunjungan)) {
                ps.setDate(1, java.sql.Date.valueOf(today));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) labelJumlahKunjunganHariIni.setText(String.valueOf(rs.getInt(1)));
            }

            // Pasien dalam antrian saat ini
            try (PreparedStatement ps = conn.prepareStatement(sqlAntrian)) {
                ps.setDate(1, java.sql.Date.valueOf(today));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) labelPasienDalamAntrian.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAntrianTable() {
        LocalDate today = LocalDate.now();
        String sql = ""
            + "SELECT k.no_antrian, p.nama_lengkap AS pasien, d.nama_lengkap AS dokter, k.status_kunjungan "
            + "FROM kunjungan k "
            + "JOIN pasien p ON k.id_pasien = p.id_pasien "
            + "JOIN dokter d ON k.id_dokter = d.id_dokter "
            + "WHERE k.tanggal_kunjungan = ? "
            + "ORDER BY k.no_antrian";

        List<AntrianRecord> rows = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(today));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String noAntrian   = rs.getString("no_antrian");
                    String pasienName  = rs.getString("pasien");
                    String dokterName  = rs.getString("dokter");
                    VisitStatus status = VisitStatus.fromString(rs.getString("status_kunjungan"));

                    rows.add(new AntrianRecord(noAntrian, pasienName, dokterName, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableAntrian.setItems(FXCollections.observableArrayList(rows));
    }

    public static class AntrianRecord {
        private final SimpleStringProperty noAntrian;
        private final SimpleStringProperty namaPasien;
        private final SimpleStringProperty namaDokter;
        private final SimpleObjectProperty<VisitStatus> status;

        public AntrianRecord(String noAntrian, String namaPasien, String namaDokter, VisitStatus status) {
            this.noAntrian  = new SimpleStringProperty(noAntrian);
            this.namaPasien = new SimpleStringProperty(namaPasien);
            this.namaDokter = new SimpleStringProperty(namaDokter);
            this.status     = new SimpleObjectProperty<>(status);
        }

        public String getNoAntrian()    { return noAntrian.get(); }
        public String getNamaPasien()   { return namaPasien.get(); }
        public String getNamaDokter()   { return namaDokter.get(); }
        public VisitStatus getStatus()  { return status.get(); }
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().endSession();
        System.out.println("The user session has ended (logout).");

        SceneManager.getInstance().switchToLoginScene();
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
