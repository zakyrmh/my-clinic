package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.clinic.models.MedicalRecord;
import com.clinic.utils.DatabaseUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MedicalRecordShowController {

    @FXML
    private Label noRekamMedisLabel;
    @FXML
    private Label namaPasienLabel;
    @FXML
    private Label namaDokterLabel;
    @FXML
    private Label anamnesisLabel;
    @FXML
    private Label pemeriksaanFisikLabel;
    @FXML
    private Label tekananDarahLabel;
    @FXML
    private Label nadiLabel;
    @FXML
    private Label suhuLabel;
    @FXML
    private Label respirasiLabel;
    @FXML
    private Label beratBadanLabel;
    @FXML
    private Label tinggiBadanLabel;
    @FXML
    private Label diagnosisUtamaLabel;
    @FXML
    private Label diagnosisSekunderLabel;
    @FXML
    private Label tindakanLabel;
    @FXML
    private Label terapiLabel;
    @FXML
    private Label catatanDokterLabel;

    private final Map<Integer, String> pasienMap = new HashMap<>();
    private final Map<Integer, String> dokterMap = new HashMap<>();
    private final Map<Integer, String> noRmPasienMap = new HashMap<>();

    private boolean dataLoaded = false;

    private MedicalRecord medicalRecord;

    public void setMedicalRecordData(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;

        if (!dataLoaded) {
            loadData();
            dataLoaded = true;
        }

        // Set teks field medis
        anamnesisLabel.setText(medicalRecord.getAnamnesis());
        pemeriksaanFisikLabel.setText(medicalRecord.getPemeriksaanFisik());
        tekananDarahLabel.setText(medicalRecord.getTekananDarah());
        nadiLabel.setText(String.valueOf(medicalRecord.getNadi()));
        suhuLabel.setText(String.valueOf(medicalRecord.getSuhu()));
        respirasiLabel.setText(String.valueOf(medicalRecord.getRespirasi()));
        beratBadanLabel.setText(String.valueOf(medicalRecord.getBeratBadan()));
        tinggiBadanLabel.setText(String.valueOf(medicalRecord.getTinggiBadan()));
        diagnosisUtamaLabel.setText(medicalRecord.getDiagnosisUtama());
        diagnosisSekunderLabel.setText(medicalRecord.getDiagnosisSekunder());
        tindakanLabel.setText(medicalRecord.getTindakan());
        terapiLabel.setText(medicalRecord.getTerapi());
        catatanDokterLabel.setText(medicalRecord.getCatatanDokter());

        // Set nama pasien & nomor RM
        String namaPasien = pasienMap.get(medicalRecord.getIdPasien());
        String noRekamMedis = noRmPasienMap.get(medicalRecord.getIdPasien());

        namaPasienLabel.setText(namaPasien != null ? namaPasien : "-");
        noRekamMedisLabel.setText(noRekamMedis != null ? noRekamMedis : "-");

        // Set nama dokter
        String namaDokter = dokterMap.get(medicalRecord.getIdDokter());
        namaDokterLabel.setText(namaDokter != null ? namaDokter : "-");
    }

    private void loadData() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Load data pasien
            ResultSet rsPasien = conn.createStatement().executeQuery("SELECT id_pasien, nama_lengkap, no_rm FROM pasien");
            while (rsPasien.next()) {
                int idPasien = rsPasien.getInt("id_pasien");
                pasienMap.put(idPasien, rsPasien.getString("nama_lengkap"));
                noRmPasienMap.put(idPasien, rsPasien.getString("no_rm"));
            }

            // Load data dokter
            ResultSet rsDokter = conn.createStatement().executeQuery("SELECT id_dokter, nama_lengkap FROM dokter");
            while (rsDokter.next()) {
                dokterMap.put(rsDokter.getInt("id_dokter"), rsDokter.getString("nama_lengkap"));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Di dunia nyata, gunakan logging
        }
    }
}
