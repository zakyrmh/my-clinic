package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.clinic.models.Visit;
import com.clinic.utils.DatabaseUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VisitShowController {

    @FXML private Label noAntrian;
    @FXML private Label noRmLabel;
    @FXML private Label namaPasienLabel;
    @FXML private Label namaDokterLabel;
    @FXML private Label tanggalKunjunganLabel;
    @FXML private Label jamDaftarLabel;
    @FXML private Label jamPeriksaLabel;
    @FXML private Label jamSelesaiLabel;
    @FXML private Label jenisKunjunganLabel;
    @FXML private Label keluhanUtamaLabel;

    private Map<Integer, String> pasienMap = new HashMap<>();
    private Map<Integer, String> dokterMap = new HashMap<>();
    private Map<Integer, String> noRmPasienMap = new HashMap<>();

    private Visit visit;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            ResultSet rsPasien = conn.createStatement().executeQuery("SELECT id_pasien, nama_lengkap, no_rm FROM pasien");
            while (rsPasien.next()) {
                pasienMap.put(rsPasien.getInt("id_pasien"), rsPasien.getString("nama_lengkap"));
                noRmPasienMap.put(rsPasien.getInt("id_pasien"), rsPasien.getString("no_rm"));
            }

            ResultSet rsDokter = conn.createStatement().executeQuery("SELECT id_dokter, nama_lengkap FROM dokter");
            while (rsDokter.next()) {
                dokterMap.put(rsDokter.getInt("id_dokter"), rsDokter.getString("nama_lengkap"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setVisitData(Visit visit) {
        this.visit = visit;

        noAntrian.setText(visit.getNoAntrian());

        tanggalKunjunganLabel.setText(
            visit.getTanggalKunjungan() != null
                ? visit.getTanggalKunjungan().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                : "-"
        );
        jamDaftarLabel.setText(
            visit.getJamDaftar() != null
                ? visit.getJamDaftar().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "-"
        );
        jamPeriksaLabel.setText(
            visit.getJamPeriksa() != null
                ? visit.getJamPeriksa().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "-"
        );
        jamSelesaiLabel.setText(
            visit.getJamSelesai() != null
                ? visit.getJamSelesai().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "-"
        );
        jenisKunjunganLabel.setText(
            visit.getJenisKunjungan() != null
                ? visit.getJenisKunjungan().getValue()
                : "-"
        );
        keluhanUtamaLabel.setText(
            visit.getKeluhanUtama() != null
                ? visit.getKeluhanUtama()
                : "-"
        );

        // Ambil nama dari map berdasarkan ID
        String namaPasien = pasienMap.getOrDefault(visit.getIdPasien(), "-");
        String namaDokter = dokterMap.getOrDefault(visit.getIdDokter(), "-");
        String noRm = noRmPasienMap.getOrDefault(visit.getIdPasien(), "-");

        namaPasienLabel.setText(namaPasien);
        namaDokterLabel.setText(namaDokter);
        noRmLabel.setText(noRm);
    }
}
