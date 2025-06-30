package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.clinic.models.Payment;
import com.clinic.utils.DatabaseUtil;
import com.clinic.utils.PdfGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PaymentShowController {

    @FXML
    private Label noInvoiceLabel;
    @FXML
    private Label tanggalInvoiceLabel;
    @FXML
    private Label noRmLabel;
    @FXML
    private Label namaPasienLabel;
    @FXML
    private Label biayaKonsultasiLabel;
    @FXML
    private Label biayaTindakanLabel;
    @FXML
    private Label biayaObatLabel;
    @FXML
    private Label totalBiayaLabel;
    @FXML
    private Label statusPembayaranLabel;
    @FXML
    private Label tanggalPembayaranLabel;

    private final Map<Integer, Integer> visitMap = new HashMap<>();
    private final Map<Integer, String> pasienMap = new HashMap<>();
    private final Map<Integer, String> noRmPasienMap = new HashMap<>();

    private boolean dataLoaded = false;
    private Payment payment;

    public void setPaymentData(Payment payment) {
        this.payment = payment;

        if (!dataLoaded) {
            loadData();
            dataLoaded = true;
        }

        if (payment == null) {
            return;
        }

        noInvoiceLabel.setText(payment.getNoInvoice());
        tanggalInvoiceLabel.setText(payment.getCreatedAt().toString());
        biayaKonsultasiLabel.setText(String.valueOf(payment.getBiayaKonsultasi()));
        biayaTindakanLabel.setText(String.valueOf(payment.getBiayaTindakan()));
        biayaObatLabel.setText(String.valueOf(payment.getBiayaObat()));
        totalBiayaLabel.setText(String.valueOf(payment.getTotalBiaya()));
        statusPembayaranLabel.setText(payment.getStatusPembayaran().toString());
        tanggalPembayaranLabel.setText(payment.getTanggalPembayaran().toString());

        Integer idPasien = visitMap.get(payment.getIdKunjungan());
        if (idPasien != null) {
            String nama = pasienMap.get(idPasien);
            String noRmText = noRmPasienMap.get(idPasien);
            namaPasienLabel.setText(nama != null ? nama : "-");
            noRmLabel.setText(noRmText != null ? noRmText : "-");
        } else {
            namaPasienLabel.setText("-");
            noRmLabel.setText("-");
        }
    }

    private void loadData() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            ResultSet rsVisit = conn.createStatement().executeQuery("SELECT id_kunjungan, id_pasien FROM kunjungan");
            while (rsVisit.next()) {
                visitMap.put(rsVisit.getInt("id_kunjungan"), rsVisit.getInt("id_pasien"));
            }

            ResultSet rsPasien = conn.createStatement().executeQuery("SELECT id_pasien, nama_lengkap, no_rm FROM pasien");
            while (rsPasien.next()) {
                int id = rsPasien.getInt("id_pasien");
                pasienMap.put(id, rsPasien.getString("nama_lengkap"));
                noRmPasienMap.put(id, rsPasien.getString("no_rm"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrintButtonAction(ActionEvent event) {
        // Dapatkan data pembayaran yang dipilih
        Payment selectedPayment = payment;

        if (selectedPayment == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih data pembayaran terlebih dahulu");
            return;
        }

        // Dapatkan stage dari event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Panggil PDF generator
        PdfGenerator.generateInvoicePDF(selectedPayment, stage);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
