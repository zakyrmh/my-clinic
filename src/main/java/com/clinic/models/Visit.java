package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Visit {
    private final IntegerProperty idKunjungan;
    private final IntegerProperty idPasien;
    private final IntegerProperty idDokter;
    private final StringProperty noAntrian;
    private final ObjectProperty<LocalDate> tanggalKunjungan;
    private final ObjectProperty<LocalTime> jamDaftar;
    private final ObjectProperty<LocalTime> jamPeriksa;
    private final ObjectProperty<LocalTime> jamSelesai;
    private final ObjectProperty<VisitType> jenisKunjungan;
    private final ObjectProperty<VisitStatus> statusKunjungan;
    private final StringProperty keluhanUtama;
    private final IntegerProperty biayaKonsultasi;
    private final ObjectProperty<PaymentMethod> caraBayar;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untuk jenis kunjungan
    // 'Baru', 'Lama', 'Rujukan', 'Kontrol'
    public enum VisitType {
        NEW("Baru"), OLD("Lama"), REFERAL("Rujukan"), FOLLOWUP("Kontrol");

        private final String value;

        VisitType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static VisitType fromString(String value) {
            for (VisitType visitType : VisitType.values()) {
                if (visitType.value.equalsIgnoreCase(value)) {
                    return visitType;
                }
            }
            throw new IllegalArgumentException("Invalid visit type value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Enum untuk status kunjungan
    // 'Terdaftar', 'Menunggu', 'Sedang Diperiksa', 'Selesai', 'Batal'
    public enum VisitStatus {
        REGISTERED("Terdaftar"), WAITING("Menunggu"), IN_PROGRESS("Sedang Diperiksa"), COMPLETED("Selesai"), CANCELED("Batal");

        private final String value;

        VisitStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static VisitStatus fromString(String value) {
            for (VisitStatus visitStatus : VisitStatus.values()) {
                if (visitStatus.value.equalsIgnoreCase(value)) {
                    return visitStatus;
                }
            }
            throw new IllegalArgumentException("Invalid visit status value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Enum untuk cara bayar
    // 'Tunai', 'Debit', 'Kredit', 'Transfer', 'BPJS', 'Asuransi'
    public enum PaymentMethod {
        CASH("Tunai"), DEBIT("Debit"), CREDIT("Kredit"), TRANSFER("Transfer"), BPJS("BPJS"), INSURANCE("Asuransi");

        private final String value;

        PaymentMethod(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PaymentMethod fromString(String value) {
            for (PaymentMethod paymentMethod : PaymentMethod.values()) {
                if (paymentMethod.value.equalsIgnoreCase(value)) {
                    return paymentMethod;
                }
            }
            throw new IllegalArgumentException("Invalid payment method value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Visit() {
        this.idKunjungan = new SimpleIntegerProperty();
        this.idPasien = new SimpleIntegerProperty();
        this.idDokter = new SimpleIntegerProperty();
        this.noAntrian = new SimpleStringProperty();
        this.tanggalKunjungan = new SimpleObjectProperty<>();
        this.jamDaftar = new SimpleObjectProperty<>();
        this.jamPeriksa = new SimpleObjectProperty<>();
        this.jamSelesai = new SimpleObjectProperty<>();
        this.jenisKunjungan = new SimpleObjectProperty<>();
        this.statusKunjungan = new SimpleObjectProperty<>();
        this.keluhanUtama = new SimpleStringProperty();
        this.biayaKonsultasi = new SimpleIntegerProperty();
        this.caraBayar = new SimpleObjectProperty<>();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Visit(
            int idKunjungan,
            int idPasien,
            int idDokter,
            String noAntrian,
            LocalDate tanggalKunjungan,
            LocalTime jamDaftar,
            LocalTime jamPeriksa,
            LocalTime jamSelesai,
            VisitType jenisKunjungan,
            VisitStatus statusKunjungan,
            String keluhanUtama,
            int biayaKonsultasi,
            PaymentMethod caraBayar,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idKunjungan = new SimpleIntegerProperty(idKunjungan);
        this.idPasien = new SimpleIntegerProperty(idPasien);
        this.idDokter = new SimpleIntegerProperty(idDokter);
        this.noAntrian = new SimpleStringProperty(noAntrian);
        this.tanggalKunjungan = new SimpleObjectProperty<>(tanggalKunjungan);
        this.jamDaftar = new SimpleObjectProperty<>(jamDaftar);
        this.jamPeriksa = new SimpleObjectProperty<>(jamPeriksa);
        this.jamSelesai = new SimpleObjectProperty<>(jamSelesai);
        this.jenisKunjungan = new SimpleObjectProperty<>(jenisKunjungan);
        this.statusKunjungan = new SimpleObjectProperty<>(statusKunjungan);
        this.keluhanUtama = new SimpleStringProperty(keluhanUtama);
        this.biayaKonsultasi = new SimpleIntegerProperty(biayaKonsultasi);
        this.caraBayar = new SimpleObjectProperty<>(caraBayar);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---
    // Id visit
    public IntegerProperty idKunjunganProperty() {
        return idKunjungan;
    }

    public int getIdKunjungan() {
        return idKunjungan.get();
    }

    public void setIdKunjungan(int idKunjungan) {
        this.idKunjungan.set(idKunjungan);
    }

    // Id patient
    public IntegerProperty idPasienProperty() {
        return idPasien;
    }

    public int getIdPasien() {
        return idPasien.get();
    }

    public void setIdPasien(int idPasien) {
        this.idPasien.set(idPasien);
    }

    // Id doctor
    public IntegerProperty idDokterProperty() {
        return idDokter;
    }

    public int getIdDokter() {
        return idDokter.get();
    }

    public void setIdDokter(int idDokter) {
        this.idDokter.set(idDokter);
    }
    
    // No antrian
    public StringProperty noAntrianProperty() {
        return noAntrian;
    }

    public String getNoAntrian() {
        return noAntrian.get();
    }

    public void setNoAntrian(String noAntrian) {
        this.noAntrian.set(noAntrian);
    }

    // Tanggal kunjungan
    public ObjectProperty<LocalDate> tanggalKunjunganProperty() {
        return tanggalKunjungan;
    }

    public LocalDate getTanggalKunjungan() {
        return tanggalKunjungan.get();
    }

    public void setTanggalKunjungan(LocalDate tanggalKunjungan) {
        this.tanggalKunjungan.set(tanggalKunjungan);
    }

    // Jam daftar
    public ObjectProperty<LocalTime> jamDaftarProperty() {
        return jamDaftar;
    }

    public LocalTime getJamDaftar() {
        return jamDaftar.get();
    }

    public void setJamDaftar(LocalTime jamDaftar) {
        this.jamDaftar.set(jamDaftar);
    }

    // Jam periksa
    public ObjectProperty<LocalTime> jamPeriksaProperty() {
        return jamPeriksa;
    }

    public LocalTime getJamPeriksa() {
        return jamPeriksa.get();
    }

    public void setJamPeriksa(LocalTime jamPeriksa) {
        this.jamPeriksa.set(jamPeriksa);
    }

    // Jam selesai
    public ObjectProperty<LocalTime> jamSelesaiProperty() {
        return jamSelesai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai.get();
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai.set(jamSelesai);
    }

    // Jenis kunjungan
    public ObjectProperty<VisitType> jenisKunjunganProperty() {
        return jenisKunjungan;
    }

    public VisitType getJenisKunjungan() {
        return jenisKunjungan.get();
    }

    public void setJenisKunjungan(VisitType jenisKunjungan) {
        this.jenisKunjungan.set(jenisKunjungan);
    }

    // Status kunjungan
    public ObjectProperty<VisitStatus> statusKunjunganProperty() {
        return statusKunjungan;
    }

    public VisitStatus getStatusKunjungan() {
        return statusKunjungan.get();
    }

    public void setStatusKunjungan(VisitStatus statusKunjungan) {
        this.statusKunjungan.set(statusKunjungan);
    }

    // Keluhan utama
    public StringProperty keluhanUtamaProperty() {
        return keluhanUtama;
    }

    public String getKeluhanUtama() {
        return keluhanUtama.get();
    }

    public void setKeluhanUtama(String keluhanUtama) {
        this.keluhanUtama.set(keluhanUtama);
    }

    // Biaya konsultasi
    public IntegerProperty biayaKonsultasiProperty() {
        return biayaKonsultasi;
    }

    public int getBiayaKonsultasi() {
        return biayaKonsultasi.get();
    }

    public void setBiayaKonsultasi(int biayaKonsultasi) {
        this.biayaKonsultasi.set(biayaKonsultasi);
    }

    // Cara bayar
    public ObjectProperty<PaymentMethod> caraBayarProperty() {
        return caraBayar;
    }

    public PaymentMethod getCaraBayar() {
        return caraBayar.get();
    }

    public void setCaraBayar(PaymentMethod caraBayar) {
        this.caraBayar.set(caraBayar);
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