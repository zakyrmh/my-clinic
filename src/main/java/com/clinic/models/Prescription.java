package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clinic.models.Prescription.PrescriptionStatus;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prescription {
    private final IntegerProperty idResep;
    private final IntegerProperty idKunjungan;
    private final IntegerProperty idDokter;
    private final IntegerProperty idPasien;
    private final ObjectProperty<LocalDate> tanggalResep;
    private final ObjectProperty<PrescriptionStatus> statusResep;
    private final StringProperty catatanResep;
    private final IntegerProperty totalBiaya;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untuk status resep
    // 'Diresepkan', 'Sedang Disiapkan', 'Sudah Diambil', 'Batal'\
    public enum PrescriptionStatus {
        PRESCRIBED("Diresepkan"), PREPARING("Sedang Disiapkan"), TAKEN("Sudah Diambil"), CANCELLED("Batal");

        private final String value;

        PrescriptionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PrescriptionStatus fromString(String value) {
            for (PrescriptionStatus status : PrescriptionStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid prescription status value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Prescription() {
        this.idResep = new SimpleIntegerProperty();
        this.idKunjungan = new SimpleIntegerProperty();
        this.idDokter = new SimpleIntegerProperty();
        this.idPasien = new SimpleIntegerProperty();
        this.tanggalResep = new SimpleObjectProperty<>();
        this.statusResep = new SimpleObjectProperty<>();
        this.catatanResep = new SimpleStringProperty();
        this.totalBiaya = new SimpleIntegerProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Prescription(
            int idResep,
            int idKunjungan,
            int idDokter,
            int idPasien,
            LocalDate tanggalResep,
            PrescriptionStatus statusResep,
            String catatanResep,
            int totalBiaya,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idResep = new SimpleIntegerProperty();
        this.idKunjungan = new SimpleIntegerProperty();
        this.idDokter = new SimpleIntegerProperty();
        this.idPasien = new SimpleIntegerProperty();
        this.tanggalResep = new SimpleObjectProperty<>();
        this.statusResep = new SimpleObjectProperty<>();
        this.catatanResep = new SimpleStringProperty();
        this.totalBiaya = new SimpleIntegerProperty();
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---

    // Id Resep
    public IntegerProperty idResepProperty() {
        return idResep;
    }

    public int getIdResep() {
        return idResep.get();
    }

    public void setIdResep(int idResep) {
        this.idResep.set(idResep);
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

    // Id Dokter
    public IntegerProperty idDokterProperty() {
        return idDokter;
    }

    public int getIdDokter() {
        return idDokter.get();
    }

    public void setIdDokter(int idDokter) {
        this.idDokter.set(idDokter);
    }

    // Id Pasien
    public IntegerProperty idPasienProperty() {
        return idPasien;
    }

    public int getIdPasien() {
        return idPasien.get();
    }

    public void setIdPasien(int idPasien) {
        this.idPasien.set(idPasien);
    }

    // Tanggal Resep
    public ObjectProperty<LocalDate> tanggalResepProperty() {
        return tanggalResep;
    }

    public LocalDate getTanggalResep() {
        return tanggalResep.get();
    }

    public void setTanggalResep(LocalDate tanggalResep) {
        this.tanggalResep.set(tanggalResep);
    }

    // Status Resep
    public ObjectProperty<PrescriptionStatus> statusResepProperty() {
        return statusResep;
    }

    public PrescriptionStatus getStatusResep() {
        return statusResep.get();
    }

    public void setStatusResep(PrescriptionStatus statusResep) {
        this.statusResep.set(statusResep);
    }

    // Catatan Resep
    public StringProperty catatanResepProperty() {
        return catatanResep;
    }

    public String getCatatanResep() {
        return catatanResep.get();
    }

    public void setCatatanResep(String catatanResep) {
        this.catatanResep.set(catatanResep);
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

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    // updatedAt
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
