package com.clinic.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.clinic.manager.SceneManager;
import com.clinic.models.Payment;
import com.clinic.utils.DatabaseUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class PaymentEditController {
    @FXML
    private TextField biayaKonsultasiField;
    @FXML
    private TextField biayaObatField;
    @FXML
    private TextField biayaTindakanField;
    @FXML
    private DatePicker tanggalPembayaranPicker;
    @FXML
    private ToggleGroup statusPembayaranGroup;
    @FXML
    private RadioButton pendingRadio, lunasRadio, partialRadio, refundRadio;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancel;

    private Payment currentPayment;

    @FXML
    public void initialize() {
        // Kosongkan untuk saat ini
    }

    public void setPaymentData(Payment payment) {
        this.currentPayment = payment;

        biayaKonsultasiField.setText(String.valueOf(payment.getBiayaKonsultasi()));
        biayaObatField.setText(String.valueOf(payment.getBiayaObat()));
        biayaTindakanField.setText(String.valueOf(payment.getBiayaTindakan()));
        tanggalPembayaranPicker.setValue(payment.getTanggalPembayaran());

        Toggle selectedStatusPembayaran = statusPembayaranGroup.getToggles().stream()
                .filter(t -> t.getUserData().equals(currentPayment.getStatusPembayaran().toString()))
                .findFirst().orElse(null);
        if (selectedStatusPembayaran != null) {
            statusPembayaranGroup.selectToggle(selectedStatusPembayaran);
        }
    }

    @FXML
    private void handleEditPayment() {
        if (validateInput()) {
            try (Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE pembayaran SET biaya_konsultasi = ?, biaya_obat = ?, biaya_tindakan = ?, total_biaya = ?, tanggal_pembayaran = ?, status_pembayaran = ? WHERE id_pembayaran = ?")) {
                int biayaKonsultasi = Integer.parseInt(biayaKonsultasiField.getText());
                int biayaObat = Integer.parseInt(biayaObatField.getText());
                int biayaTindakan = Integer.parseInt(biayaTindakanField.getText());
                int totalBiaya = biayaKonsultasi + biayaObat + biayaTindakan;

                stmt.setInt(1, biayaKonsultasi);
                stmt.setInt(2, biayaObat);
                stmt.setInt(3, biayaTindakan);
                stmt.setInt(4, totalBiaya);
                stmt.setDate(5, java.sql.Date.valueOf(tanggalPembayaranPicker.getValue()));
                stmt.setString(6, statusPembayaranGroup.getSelectedToggle().getUserData().toString());
                stmt.setInt(7, currentPayment.getIdPembayaran());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Data pembayaran berhasil diperbarui.");
                    SceneManager.getInstance().switchToPaymentScene();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Gagal memperbarui data pembayaran.");
                }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memperbarui data pembayaran: " + e.getMessage());
        }
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (biayaKonsultasiField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Biaya Konsultasi.\n");
        }
        if (biayaObatField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Biaya Obat.\n");
        }
        if (biayaTindakanField.getText().isEmpty()) {
            errorMessage.append("Diperlukan Biaya Tindakan.\n");
        }
        if (tanggalPembayaranPicker.getValue() == null) {
            errorMessage.append("Diperlukan Tanggal Pembayaran.\n");
        }
        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
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
    private void handlePatientLinkAction() {
        SceneManager.getInstance().switchToPaymentScene();
    }
}
