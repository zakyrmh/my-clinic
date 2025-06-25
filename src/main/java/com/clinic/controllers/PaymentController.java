package com.clinic.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.clinic.manager.SceneManager;
import com.clinic.manager.UserSession;
import com.clinic.models.Patient;
import com.clinic.models.Payment;
import com.clinic.models.Visit;
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

public class PaymentController {
    @FXML
    private TableView<Payment> tableView;
    @FXML
    private TableColumn<Payment, Integer> no;
    @FXML
    private TableColumn<Payment, String> noInvoice;
    @FXML
    private TableColumn<Payment, String> namaPasien;
    @FXML
    private TableColumn<Payment, String> biayaKonsultasi;
    @FXML
    private TableColumn<Payment, String> biayaObat;
    @FXML
    private TableColumn<Payment, String> biayaTindakan;
    @FXML
    private TableColumn<Payment, String> totalBiaya;
    @FXML
    private TableColumn<Payment, Void> action;
    @FXML
    private TextField searchField;

    private final Map<Integer, Patient> patientMap = new HashMap<>();
    private final Map<Integer, Visit> visitMap = new HashMap<>();

    @FXML
    public void initialize() {
        no.setCellValueFactory(cellData -> {
            int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
        });
        no.setSortable(false);
        noInvoice.setCellValueFactory(new PropertyValueFactory<>("noInvoice"));
        namaPasien.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            Visit visit = visitMap.get(payment.getIdKunjungan());
            int patientId = visit != null ? visit.getIdPasien() : -1;
            Patient patient = patientMap.get(patientId);
            return new SimpleStringProperty(patient != null ? patient.getNamaLengkap() : "Unknown");
        });
        biayaKonsultasi.setCellValueFactory(new PropertyValueFactory<>("biayaKonsultasi"));
        biayaObat.setCellValueFactory(new PropertyValueFactory<>("biayaObat"));
        biayaTindakan.setCellValueFactory(new PropertyValueFactory<>("biayaTindakan"));
        totalBiaya.setCellValueFactory(new PropertyValueFactory<>("totalBiaya"));

        configureActionColumn();

        if (UserSession.getInstance().isLoggedIn()) {
            loadPaymentData();
        }
    }

    private void loadPaymentData() {
        ObservableList<Payment> paymentList = FXCollections.observableArrayList();

        String sql = "SELECT p.*, k.*, pa.* FROM pembayaran p LEFT JOIN kunjungan k ON p.id_kunjungan = k.id_kunjungan LEFT JOIN pasien pa ON k.id_pasien = pa.id_pasien;";

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Payment.PaymentStatus statusPembayaran = Payment.PaymentStatus
                        .fromString(rs.getString("status_pembayaran"));

                Payment payment = new Payment(
                        rs.getInt("id_pembayaran"),
                        rs.getInt("id_kunjungan"),
                        rs.getString("no_invoice"),
                        rs.getInt("biaya_konsultasi"),
                        rs.getInt("biaya_obat"),
                        rs.getInt("biaya_tindakan"),
                        rs.getInt("total_biaya"),
                        rs.getInt("jumlah_bayar"),
                        rs.getInt("kembalian"),
                        statusPembayaran,
                        rs.getDate("tanggal_pembayaran").toLocalDate(),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

                paymentList.add(payment);

                int visitId = rs.getInt("k.id_kunjungan");
                if (!visitMap.containsKey(visitId)) {
                    Visit.VisitStatus status = Visit.VisitStatus.fromString(rs.getString("status_kunjungan"));
                    Visit.VisitType type = Visit.VisitType.fromString(rs.getString("jenis_kunjungan"));
                    Visit.PaymentMethod caraBayar = Visit.PaymentMethod.fromString(rs.getString("cara_bayar"));
                    Visit visit = new Visit(
                            rs.getInt("k.id_kunjungan"),
                            rs.getInt("k.id_pasien"),
                            rs.getInt("k.id_dokter"),
                            rs.getString("k.no_antrian"),
                            rs.getDate("k.tanggal_kunjungan").toLocalDate(),
                            rs.getTime("k.jam_daftar") != null ? rs.getTime("k.jam_daftar").toLocalTime() : null,
                            rs.getTime("k.jam_periksa") != null ? rs.getTime("k.jam_periksa").toLocalTime() : null,
                            rs.getTime("k.jam_selesai") != null ? rs.getTime("k.jam_selesai").toLocalTime() : null,
                            type,
                            status,
                            rs.getString("k.keluhan_utama"),
                            rs.getInt("k.biaya_konsultasi"),
                            caraBayar,
                            rs.getTimestamp("k.created_at") != null ? rs.getTimestamp("k.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("k.updated_at") != null ? rs.getTimestamp("k.updated_at").toLocalDateTime()
                                    : null);
                    visitMap.put(visitId, visit);

                    int patientId = rs.getInt("pa.id_pasien");
                    if (!patientMap.containsKey(patientId)) {
                        Patient.Gender gender = Patient.Gender.fromString(rs.getString("pa.jenis_kelamin"));
                        Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                                .fromString(rs.getString("pa.status_pernikahan"));
                        Patient.BloodType bloodType = Patient.BloodType.fromString(rs.getString("pa.golongan_darah"));
                        Patient patient = new Patient(
                                patientId,
                                rs.getString("pa.no_rm"),
                                rs.getString("pa.nik"),
                                rs.getString("pa.nama_lengkap"),
                                gender,
                                rs.getDate("pa.tanggal_lahir").toLocalDate(),
                                rs.getString("pa.tempat_lahir"),
                                rs.getString("pa.alamat"),
                                rs.getString("pa.no_telepon"),
                                rs.getString("pa.email"),
                                rs.getString("pa.pekerjaan"),
                                statusPernikahan,
                                bloodType,
                                rs.getTimestamp("pa.created_at") != null
                                        ? rs.getTimestamp("pa.created_at").toLocalDateTime()
                                        : null,
                                rs.getTimestamp("pa.updated_at") != null
                                        ? rs.getTimestamp("pa.updated_at").toLocalDateTime()
                                        : null);
                        patientMap.put(patientId, patient);
                    }
                }
            }

            tableView.setItems(paymentList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading payment data: " + e.getMessage());
        }
    }

    private void configureActionColumn() {
        action.setCellFactory(param -> new TableCell<Payment, Void>() {
            private final Button viewButton = new Button("Lihat");
            private final Button editButton = new Button("Edit");
            private final HBox pane = new HBox(5, viewButton, editButton);

            {
                // Styling tombol
                viewButton.getStyleClass().add("view-button");
                editButton.getStyleClass().add("edit-button");

                // Handler untuk tombol Lihat
                viewButton.setOnAction(event -> {
                    Payment payment = getTableView().getItems().get(getIndex());
                    handleViewAction(payment);
                });

                // Handler untuk tombol Edit
                editButton.setOnAction(event -> {
                    Payment payment = getTableView().getItems().get(getIndex());
                    handleEditAction(payment);
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

    private void handleViewAction(Payment payment) {
        // Logika untuk menampilkan detail pembayaran
    }

    private void handleEditAction(Payment payment) {
        SceneManager.getInstance().switchToPaymentEditScene(payment);
    }

    @FXML
    private void handleSearchAction() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadPaymentData();
            return;
        }

        ObservableList<Payment> filteredList = FXCollections.observableArrayList();

        String sql = "SELECT p.*, k.*, pa.* FROM pembayaran p LEFT JOIN kunjungan k ON p.id_kunjungan = k.id_kunjungan LEFT JOIN pasien pa ON k.id_pasien = pa.id_pasien WHERE p.no_invoice LIKE ? OR LOWER(pa.nama_lengkap) LIKE ?;";

        try (Connection conn = DatabaseUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword.toLowerCase() + "%");
                
                ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Payment.PaymentStatus statusPembayaran = Payment.PaymentStatus
                        .fromString(rs.getString("status_pembayaran"));

                Payment payment = new Payment(
                        rs.getInt("id_pembayaran"),
                        rs.getInt("id_kunjungan"),
                        rs.getString("no_invoice"),
                        rs.getInt("biaya_konsultasi"),
                        rs.getInt("biaya_obat"),
                        rs.getInt("biaya_tindakan"),
                        rs.getInt("total_biaya"),
                        rs.getInt("jumlah_bayar"),
                        rs.getInt("kembalian"),
                        statusPembayaran,
                        rs.getDate("tanggal_pembayaran").toLocalDate(),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

                filteredList.add(payment);

                int visitId = rs.getInt("k.id_kunjungan");
                if (!visitMap.containsKey(visitId)) {
                    Visit.VisitStatus status = Visit.VisitStatus.fromString(rs.getString("status_kunjungan"));
                    Visit.VisitType type = Visit.VisitType.fromString(rs.getString("jenis_kunjungan"));
                    Visit.PaymentMethod caraBayar = Visit.PaymentMethod.fromString(rs.getString("cara_bayar"));
                    Visit visit = new Visit(
                            rs.getInt("k.id_kunjungan"),
                            rs.getInt("k.id_pasien"),
                            rs.getInt("k.id_dokter"),
                            rs.getString("k.no_antrian"),
                            rs.getDate("k.tanggal_kunjungan").toLocalDate(),
                            rs.getTime("k.jam_daftar") != null ? rs.getTime("k.jam_daftar").toLocalTime() : null,
                            rs.getTime("k.jam_periksa") != null ? rs.getTime("k.jam_periksa").toLocalTime() : null,
                            rs.getTime("k.jam_selesai") != null ? rs.getTime("k.jam_selesai").toLocalTime() : null,
                            type,
                            status,
                            rs.getString("k.keluhan_utama"),
                            rs.getInt("k.biaya_konsultasi"),
                            caraBayar,
                            rs.getTimestamp("k.created_at") != null ? rs.getTimestamp("k.created_at").toLocalDateTime()
                                    : null,
                            rs.getTimestamp("k.updated_at") != null ? rs.getTimestamp("k.updated_at").toLocalDateTime()
                                    : null);
                    visitMap.put(visitId, visit);

                    int patientId = rs.getInt("pa.id_pasien");
                    if (!patientMap.containsKey(patientId)) {
                        Patient.Gender gender = Patient.Gender.fromString(rs.getString("pa.jenis_kelamin"));
                        Patient.MaritalStatus statusPernikahan = Patient.MaritalStatus
                                .fromString(rs.getString("pa.status_pernikahan"));
                        Patient.BloodType bloodType = Patient.BloodType.fromString(rs.getString("pa.golongan_darah"));
                        Patient patient = new Patient(
                                patientId,
                                rs.getString("pa.no_rm"),
                                rs.getString("pa.nik"),
                                rs.getString("pa.nama_lengkap"),
                                gender,
                                rs.getDate("pa.tanggal_lahir").toLocalDate(),
                                rs.getString("pa.tempat_lahir"),
                                rs.getString("pa.alamat"),
                                rs.getString("pa.no_telepon"),
                                rs.getString("pa.email"),
                                rs.getString("pa.pekerjaan"),
                                statusPernikahan,
                                bloodType,
                                rs.getTimestamp("pa.created_at") != null
                                        ? rs.getTimestamp("pa.created_at").toLocalDateTime()
                                        : null,
                                rs.getTimestamp("pa.updated_at") != null
                                        ? rs.getTimestamp("pa.updated_at").toLocalDateTime()
                                        : null);
                        patientMap.put(patientId, patient);
                    }
                }
            }

            tableView.setItems(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error searching payment data: " + e.getMessage());
        }
    }
}
