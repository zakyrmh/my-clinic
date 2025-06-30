package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clinic.models.Payment.PaymentStatus;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Payment {
    private final IntegerProperty idPembayaran;
    private final IntegerProperty idKunjungan;
    private final StringProperty noInvoice;
    private final IntegerProperty biayaKonsultasi;
    private final IntegerProperty biayaObat;
    private final IntegerProperty biayaTindakan;
    private final IntegerProperty totalBiaya;
    private final ObjectProperty<PaymentStatus> statusPembayaran;
    private final ObjectProperty<LocalDate> tanggalPembayaran;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untuk status pembayaran
    // 'Pending', 'Lunas', 'Partial', 'Refund'
    public enum PaymentStatus {
        PENDING("Pending"),
        PAID("Lunas"),
        PARTIAL("Partial"),
        REFUND("Refund");

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PaymentStatus fromString(String value) {
            for (PaymentStatus paymentStatus : PaymentStatus.values()) {
                if (paymentStatus.value.equalsIgnoreCase(value)) {
                    return paymentStatus;
                }
            }
            throw new IllegalArgumentException("Invalid payment status value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Payment() {
        this.idPembayaran = new SimpleIntegerProperty();
        this.idKunjungan = new SimpleIntegerProperty();
        this.noInvoice = new SimpleStringProperty();
        this.biayaKonsultasi = new SimpleIntegerProperty();
        this.biayaObat = new SimpleIntegerProperty();
        this.biayaTindakan = new SimpleIntegerProperty();
        this.totalBiaya = new SimpleIntegerProperty();
        this.statusPembayaran = new SimpleObjectProperty<>();
        this.tanggalPembayaran = new SimpleObjectProperty<>();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Payment(
            int idPembayaran,
            int idKunjungan,
            String noInvoice,
            int biayaKonsultasi,
            int biayaObat,
            int biayaTindakan,
            int totalBiaya,
            PaymentStatus statusPembayaran,
            LocalDate tanggalPembayaran,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idPembayaran = new SimpleIntegerProperty(idPembayaran);
        this.idKunjungan = new SimpleIntegerProperty(idKunjungan);
        this.noInvoice = new SimpleStringProperty(noInvoice);
        this.biayaKonsultasi = new SimpleIntegerProperty(biayaKonsultasi);
        this.biayaObat = new SimpleIntegerProperty(biayaObat);
        this.biayaTindakan = new SimpleIntegerProperty(biayaTindakan);
        this.totalBiaya = new SimpleIntegerProperty(totalBiaya);
        this.statusPembayaran = new SimpleObjectProperty<>(statusPembayaran);
        this.tanggalPembayaran = new SimpleObjectProperty<>(tanggalPembayaran);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---

    // Id Pembayaran
    public IntegerProperty idPembayaranProperty() {
        return idPembayaran;
    }

    public int getIdPembayaran() {
        return idPembayaran.get();
    }

    public void setIdPembayaran(int idPembayaran) {
        this.idPembayaran.set(idPembayaran);
    }

    // Id Kunjungan
    public IntegerProperty idKunjunganProperty() {
        return idKunjungan;
    }

    public int getIdKunjungan() {
        return idKunjungan.get();
    }

    public void setIdKunjungan(int idKunjungan) {
        this.idKunjungan.set(idKunjungan);
    }

    // No Invoice
    public StringProperty noInvoiceProperty() {
        return noInvoice;
    }

    public String getNoInvoice() {
        return noInvoice.get();
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice.set(noInvoice);
    }

    // Biaya Konsultasi
    public IntegerProperty biayaKonsultasiProperty() {
        return biayaKonsultasi;
    }

    public int getBiayaKonsultasi() {
        return biayaKonsultasi.get();
    }

    public void setBiayaKonsultasi(int biayaKonsultasi) {
        this.biayaKonsultasi.set(biayaKonsultasi);
    }

    // Biaya Obat
    public IntegerProperty biayaObatProperty() {
        return biayaObat;
    }

    public int getBiayaObat() {
        return biayaObat.get();
    }

    public void setBiayaObat(int biayaObat) {
        this.biayaObat.set(biayaObat);
    }

    // Biaya Tindakan
    public IntegerProperty biayaTindakanProperty() {
        return biayaTindakan;
    }

    public int getBiayaTindakan() {
        return biayaTindakan.get();
    }

    public void setBiayaTindakan(int biayaTindakan) {
        this.biayaTindakan.set(biayaTindakan);
    }

    // Total Biaya
    public IntegerProperty totalBiayaProperty() {
        return totalBiaya;
    }

    public int getTotalBiaya() {
        return totalBiaya.get();
    }

    public void setTotalBiaya(int totalBiaya) {
        this.totalBiaya.set(totalBiaya);
    }

    // Status Pembayaran
    public ObjectProperty<PaymentStatus> statusPembayaranProperty() {
        return statusPembayaran;
    }

    public PaymentStatus getStatusPembayaran() {
        return statusPembayaran.get();
    }

    public void setStatusPembayaran(PaymentStatus statusPembayaran) {
        this.statusPembayaran.set(statusPembayaran);
    }

    // Tanggal Pembayaran
    public ObjectProperty<LocalDate> tanggalPembayaranProperty() {
        return tanggalPembayaran;
    }

    public LocalDate getTanggalPembayaran() {
        return tanggalPembayaran.get();
    }

    public void setTanggalPembayaran(LocalDate tanggalPembayaran) {
        this.tanggalPembayaran.set(tanggalPembayaran);
    }

    // CreatedAt
    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    // UpdatedAt
    public ObjectProperty<LocalDateTime> updatedAtProperty() {
        return updatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt.get();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt.set(updatedAt);
    }
}
