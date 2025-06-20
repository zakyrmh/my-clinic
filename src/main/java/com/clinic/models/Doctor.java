package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
    private final IntegerProperty idDokter;
    private final StringProperty noSip;
    private final StringProperty namaLengkap;
    private final StringProperty spesialisasi;
    private final StringProperty noTelepon;
    private final StringProperty email;
    private final StringProperty alamat;
    private final ObjectProperty<LocalDate> tanggalBergabung;
    private final ObjectProperty<PracticeStatus> statusPraktik;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untu status praktik
    // Aktif, Tidak Aktif, Cuti

    public enum PracticeStatus {
        ACTIVE("Aktif"), INACTIVE("Tidak Aktif"), VACATION("Cuti");

        private final String value;

        PracticeStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PracticeStatus fromString(String value) {
            for (PracticeStatus status : PracticeStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid practice status value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Doctor() {
        this.idDokter = new SimpleIntegerProperty();
        this.noSip = new SimpleStringProperty();
        this.namaLengkap = new SimpleStringProperty();
        this.spesialisasi = new SimpleStringProperty();
        this.noTelepon = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.alamat = new SimpleStringProperty();
        this.tanggalBergabung = new SimpleObjectProperty<>();
        this.statusPraktik = new SimpleObjectProperty<>();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Doctor(
            int idDokter,
            String noSip,
            String namaLengkap,
            String spesialisasi,
            String noTelepon,
            String email,
            String alamat,
            LocalDate tanggalBergabung,
            PracticeStatus statusPraktik,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idDokter = new SimpleIntegerProperty(idDokter);
        this.noSip = new SimpleStringProperty(noSip);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.spesialisasi = new SimpleStringProperty(spesialisasi);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.email = new SimpleStringProperty(email);
        this.alamat = new SimpleStringProperty(alamat);
        this.tanggalBergabung = new SimpleObjectProperty<>(tanggalBergabung);
        this.statusPraktik = new SimpleObjectProperty<>(statusPraktik);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // Getter dan Setter untuk idDokter
    public int getIdDokter() {
        return idDokter.get();
    }

    public IntegerProperty idDokterProperty() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter.set(idDokter);
    }

    // Getter dan Setter untuk noSip
    public String getNoSip() {
        return noSip.get();
    }

    public StringProperty noSipProperty() {
        return noSip;
    }

    public void setNoSip(String noSip) {
        this.noSip.set(noSip);
    }

    // Getter dan Setter untuk namaLengkap
    public String getNamaLengkap() {
        return namaLengkap.get();
    }

    public StringProperty namaLengkapProperty() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap.set(namaLengkap);
    }

    // Getter dan Setter untuk spesialisasi
    public String getSpesialisasi() {
        return spesialisasi.get();
    }

    public StringProperty spesialisasiProperty() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi.set(spesialisasi);
    }

    // Getter dan Setter untuk noTelepon
    public String getNoTelepon() {
        return noTelepon.get();
    }

    public StringProperty noTeleponProperty() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon.set(noTelepon);
    }
    
    // Getter dan Setter untuk email
    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    // Getter dan Setter untuk alamat
    public String getAlamat() {
        return alamat.get();
    }

    public StringProperty alamatProperty() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat.set(alamat);
    }

    // Getter dan Setter untuk tanggalBergabung
    public LocalDate getTanggalBergabung() {
        return tanggalBergabung.get();
    }

    public ObjectProperty<LocalDate> tanggalBergabungProperty() {
        return tanggalBergabung;
    }

    public void setTanggalBergabung(LocalDate tanggalBergabung) {
        this.tanggalBergabung.set(tanggalBergabung);
    }

    // Getter dan Setter untuk statusPraktik
    public PracticeStatus getStatusPraktik() {
        return statusPraktik.get();
    }

    public ObjectProperty<PracticeStatus> statusPraktikProperty() {
        return statusPraktik;
    }

    public void setStatusPraktik(PracticeStatus statusPraktik) {
        this.statusPraktik.set(statusPraktik);
    }

    // Getter dan Setter untuk createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    // Getter dan Setter untuk updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt.get();
    }

    public ObjectProperty<LocalDateTime> updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt.set(updatedAt);
    }
}