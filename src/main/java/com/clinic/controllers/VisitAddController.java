package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Visit;
import com.clinic.models.Visit.PaymentMethod;
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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Popup;

public class VisitAddController {
    @FXML
    private TextField pasienField;
    @FXML
    private TextField dokterField;
    @FXML
    private DatePicker tanggalKunjunganPicker;
    @FXML
    private TextField jamDaftarField;
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

    private Map<String, Integer> pasienMap = new HashMap<>();
    private Map<String, Integer> dokterMap = new HashMap<>();

    private Popup pasienPopup = new Popup();
    private ListView<String> pasienSuggestions = new ListView<>();
    private Popup dokterPopup = new Popup();
    private ListView<String> dokterSuggestions = new ListView<>();

    @FXML
    public void initialize() {
        loadAutocompleteData();
        submitButton.setOnAction(event -> handleSubmit());
    }

    private int generateNoAntrian(LocalDate tanggalKunjungan) {
        int noAntrian = 1;
        String sql = "SELECT MAX(no_antrian) FROM kunjungan WHERE tanggal_kunjungan = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(tanggalKunjungan));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                noAntrian = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return noAntrian;
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
                if (!filtered.isEmpty() && !pasienPopup.isShowing()) {
                    pasienPopup.show(pasienField, pasienField.getScene().getWindow().getX() + pasienField.getLayoutX(),
                            pasienField.getScene().getWindow().getY() + pasienField.getLayoutY()
                                    + pasienField.getHeight());
                } else if (filtered.isEmpty()) {
                    pasienPopup.hide();
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
                if (!filtered.isEmpty() && !dokterPopup.isShowing()) {
                    dokterPopup.show(dokterField, dokterField.getScene().getWindow().getX() + dokterField.getLayoutX(),
                            dokterField.getScene().getWindow().getY() + dokterField.getLayoutY()
                                    + dokterField.getHeight());
                } else if (filtered.isEmpty()) {
                    dokterPopup.hide();
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

    private void handleSubmit() {
        if (!validateInput()) {
            return;
        }

        Visit visit = new Visit();

        int idPasien = pasienMap.getOrDefault(pasienField.getText(), 0);
        int idDokter = dokterMap.getOrDefault(dokterField.getText(), 0);
        visit.setIdPasien(idPasien);
        visit.setIdDokter(idDokter);
        
        LocalDate tanggalKunjungan = tanggalKunjunganPicker.getValue();
        visit.setTanggalKunjungan(tanggalKunjungan);
        
        int noAntrian = generateNoAntrian(tanggalKunjungan);
        visit.setNoAntrian(String.valueOf(noAntrian));
        visit.setTanggalKunjungan(tanggalKunjunganPicker.getValue());
        visit.setKeluhanUtama(keluhanUtamaField.getText());

        try {
            LocalTime jamDaftar = LocalTime.parse(jamDaftarField.getText());
            visit.setJamDaftar(jamDaftar);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Format Jam Salah", "Gunakan format HH:mm untuk jam daftar.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM kunjungan WHERE id_pasien = ?");
            ps.setInt(1, idPasien);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                visit.setJenisKunjungan(Visit.VisitType.NEW);
            } else {
                visit.setJenisKunjungan(Visit.VisitType.FOLLOWUP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RadioButton selectedPayment = (RadioButton) caraBayarGroup.getSelectedToggle();
        visit.setCaraBayar(PaymentMethod.fromString(selectedPayment.getUserData().toString()));

        if (saveToDatabase(visit)) {
            showAlert(AlertType.INFORMATION, "Success", "Data kunjungan berhasil disimpan.");
            handleClear();
            SceneManager.getInstance().switchToVisitScene();
        } else {
            showAlert(AlertType.ERROR, "Error", "Gagal menyimpan data kunjungan.");
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (pasienField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Pasien.\n");
        }
        if (dokterField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Dokter.\n");
        }
        if (tanggalKunjunganPicker.getValue() == null) {
            errorMessage.append("Diperlukan Tanggal Kunjungan.\n");
        }
        if (jamDaftarField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Jam Daftar.\n");
        }
        if (keluhanUtamaField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Keluhan Utama.\n");
        }
        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private boolean saveToDatabase(Visit visit) {
        String sql = "INSERT INTO kunjungan (id_pasien, id_dokter, no_antrian, tanggal_kunjungan, jam_daftar, keluhan_utama, cara_bayar, jenis_kunjungan) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, visit.getIdPasien());
            pstmt.setInt(2, visit.getIdDokter());
            pstmt.setString(3, visit.getNoAntrian());
            pstmt.setDate(4, java.sql.Date.valueOf(visit.getTanggalKunjungan()));
            pstmt.setTime(5, Time.valueOf(visit.getJamDaftar()));
            pstmt.setString(6, visit.getKeluhanUtama());
            pstmt.setString(7, visit.getCaraBayar().toString());
            pstmt.setString(8, visit.getJenisKunjungan().toString());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleClear() {
        pasienField.clear();
        dokterField.clear();
        tanggalKunjunganPicker.setValue(null);
        jamDaftarField.clear();
        keluhanUtamaField.clear();
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
