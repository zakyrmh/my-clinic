package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Visit;
import com.clinic.utils.DatabaseUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Popup;

public class VisitEditController {
    @FXML
    private TextField pasienField;
    @FXML
    private TextField dokterField;
    @FXML
    private DatePicker tanggalKunjunganPicker;
    @FXML
    private TextField jamDaftarField;
    @FXML
    private TextField jamPeriksaField;
    @FXML
    private TextField jamSelesaiField;
    @FXML
    private TextField biayaKonsultasiField;
    @FXML
    private TextArea keluhanUtamaField;
    @FXML
    private ToggleGroup caraBayarGroup;
    @FXML
    private RadioButton tunaiRadio;
    @FXML
    private RadioButton debitRadio;
    @FXML
    private RadioButton kreditRadio;
    @FXML
    private RadioButton transferRadio;
    @FXML
    private RadioButton bpjsRadio;
    @FXML
    private RadioButton asuransiRadio;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancel;

    private Map<String, Integer> pasienMap = new HashMap<>();
    private Map<String, Integer> dokterMap = new HashMap<>();

    private Popup pasienPopup = new Popup();
    private ListView<String> pasienSuggestions = new ListView<>();
    private Popup dokterPopup = new Popup();
    private ListView<String> dokterSuggestions = new ListView<>();

    private Visit currentVisit;

    @FXML
    public void initialize() {
        loadAutocompleteData();
    }

    public void setVisitData(Visit visit) {
        this.currentVisit = visit;

        pasienMap.entrySet().stream()
                .filter(e -> e.getValue().equals(visit.getIdPasien()))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(pasienField::setText);

        dokterMap.entrySet().stream()
                .filter(e -> e.getValue().equals(visit.getIdDokter()))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(dokterField::setText);

        tanggalKunjunganPicker.setValue(visit.getTanggalKunjungan());
        jamDaftarField.setText(visit.getJamDaftar().format(DateTimeFormatter.ofPattern("HH:mm")));
        keluhanUtamaField.setText(visit.getKeluhanUtama());
        if (visit.getJamPeriksa() == null) {
            jamPeriksaField.setText("00:00");
        } else {
            jamPeriksaField.setText(visit.getJamPeriksa().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        if (visit.getJamSelesai() == null) {
            jamSelesaiField.setText("00:00");
        } else {
            jamSelesaiField.setText(visit.getJamSelesai().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        biayaKonsultasiField.setText(String.valueOf(visit.getBiayaKonsultasi()));

        Toggle selectedPayment = caraBayarGroup.getToggles().stream()
                .filter(t -> t.getUserData().equals(visit.getCaraBayar().getValue()))
                .findFirst().orElse(null);
        if (selectedPayment != null) {
            caraBayarGroup.selectToggle(selectedPayment);
        }
    }

    private void loadAutocompleteData() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            ResultSet rsPasien = conn.createStatement().executeQuery("SELECT id_pasien, nama_lengkap FROM pasien");
            List<String> pasienList = new ArrayList<>();

            while (rsPasien.next()) {
                String nama = rsPasien.getString("nama_lengkap");
                int id = rsPasien.getInt("id_pasien");
                pasienList.add(nama);
                pasienMap.put(nama, id);
            }

            pasienSuggestions.getItems().setAll(pasienList);
            pasienPopup.getContent().add(pasienSuggestions);
            pasienField.textProperty().addListener((obs, oldText, newText) -> {
                List<String> filtered = new ArrayList<>();

                for (String nama : pasienList) {
                    if (nama.toLowerCase().contains(newText.toLowerCase())) {
                        filtered.add(nama);
                    }
                }
                pasienSuggestions.getItems().setAll(filtered);
                if (pasienField.getScene() != null && pasienField.getScene().getWindow() != null) {
                    if (!filtered.isEmpty() && !pasienPopup.isShowing()) {
                        pasienPopup.show(
                                pasienField,
                                pasienField.localToScreen(0, pasienField.getHeight()).getX(),
                                pasienField.localToScreen(0, pasienField.getHeight()).getY());
                    } else if (filtered.isEmpty()) {
                        pasienPopup.hide();
                    }
                }
            });
            pasienSuggestions.setOnMouseClicked(e -> {
                pasienField.setText(pasienSuggestions.getSelectionModel().getSelectedItem());
                pasienPopup.hide();
            });

            ResultSet rsDokter = conn.createStatement().executeQuery("SELECT id_dokter, nama_lengkap FROM dokter");
            List<String> dokterList = new ArrayList<>();
            while (rsDokter.next()) {
                String nama = rsDokter.getString("nama_lengkap");
                int id = rsDokter.getInt("id_dokter");
                dokterList.add(nama);
                dokterMap.put(nama, id);
            }

            dokterSuggestions.getItems().setAll(dokterList);
            dokterPopup.getContent().add(dokterSuggestions);
            dokterField.textProperty().addListener((obs, oldText, newText) -> {
                List<String> filtered = new ArrayList<>();
                for (String nama : dokterList) {
                    if (nama.toLowerCase().contains(newText.toLowerCase())) {
                        filtered.add(nama);
                    }
                }
                dokterSuggestions.getItems().setAll(filtered);
                if (dokterField.getScene() != null && dokterField.getScene().getWindow() != null) {
                    if (!filtered.isEmpty() && !dokterPopup.isShowing()) {
                        dokterPopup.show(
                                dokterField,
                                dokterField.localToScreen(0, dokterField.getHeight()).getX(),
                                dokterField.localToScreen(0, dokterField.getHeight()).getY());
                    } else if (filtered.isEmpty()) {
                        dokterPopup.hide();
                    }
                }
            });
            dokterSuggestions.setOnMouseClicked(e -> {
                dokterField.setText(dokterSuggestions.getSelectionModel().getSelectedItem());
                dokterPopup.hide();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEditVisit(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime daftar = LocalTime.parse(jamDaftarField.getText(), tf);
        LocalTime periksa = LocalTime.parse(jamPeriksaField.getText(), tf);
        LocalTime selesai = LocalTime.parse(jamSelesaiField.getText(), tf);

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE kunjungan SET id_pasien = ?, id_dokter = ?, tanggal_kunjungan = ?, jam_daftar = ?, keluhan_utama = ?, jam_periksa = ?, jam_selesai = ?, biaya_konsultasi = ?, cara_bayar = ? WHERE id_kunjungan = ?")) {
            stmt.setInt(1, pasienMap.get(pasienField.getText()));
            stmt.setInt(2, dokterMap.get(dokterField.getText()));
            stmt.setDate(3, java.sql.Date.valueOf(tanggalKunjunganPicker.getValue()));
            stmt.setTime(4, Time.valueOf(daftar));
            stmt.setString(5, keluhanUtamaField.getText());
            stmt.setTime(6, Time.valueOf(periksa));
            stmt.setTime(7, Time.valueOf(selesai));
            stmt.setDouble(8, Double.parseDouble(biayaKonsultasiField.getText()));
            stmt.setString(9, caraBayarGroup.getSelectedToggle().getUserData().toString());
            stmt.setInt(10, currentVisit.getIdKunjungan());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Data kunjungan berhasil diperbarui.");
                SceneManager.getInstance().switchToVisitScene();
            } else {
                showAlert(AlertType.ERROR, "Error", "Gagal memperbarui data kunjungan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memperbarui data kunjungan: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (pasienField.getText() == null || pasienField.getText().isEmpty()) {
            errorMessage.append("Nama pasien tidak boleh kosong.");
            return false;
        }
        if (dokterField.getText() == null || dokterField.getText().isEmpty()) {
            errorMessage.append("Nama dokter tidak boleh kosong.");
            return false;
        }
        if (tanggalKunjunganPicker.getValue() == null) {
            errorMessage.append("Tanggal kunjungan harus dipilih.");
            return false;
        }
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(jamDaftarField.getText(), tf);
            LocalTime.parse(jamPeriksaField.getText(), tf);
            LocalTime.parse(jamSelesaiField.getText(), tf);
        } catch (IllegalArgumentException e) {
            errorMessage.append("Format jam harus HH:mm, misalnya 14:30.");
            return false;
        }
        try {
            Double.parseDouble(biayaKonsultasiField.getText());
        } catch (NumberFormatException e) {
            errorMessage.append("Biaya konsultasi harus berupa angka.");
            return false;
        }
        if (caraBayarGroup.getSelectedToggle() == null) {
            errorMessage.append("Cara bayar harus dipilih.");
            return false;
        }
        return true;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
