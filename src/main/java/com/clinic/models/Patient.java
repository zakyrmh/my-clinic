package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clinic.models.Patient.Gender;
import com.clinic.models.Patient.MaritalStatus;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final IntegerProperty idPasien;
    private final StringProperty noRm;
    private final StringProperty nik;
    private final StringProperty namaLengkap;
    private final StringProperty tempatLahir;
    private final ObjectProperty<LocalDate> tanggalLahir;
    private final ObjectProperty<Gender> jenisKelamin;
    private final StringProperty alamat;
    private final StringProperty noTelepon;
    private final StringProperty pekerjaan;
    private final ObjectProperty<MaritalStatus> statusPernikahan;
    private final StringProperty agama;
    private final StringProperty pendidikan;
    private final StringProperty kontakDarurat;
    private final StringProperty noTeleponDarurat;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untuk gender agar sesuai dengan database
    public enum Gender {
        MALE("L"), FEMALE("P");

        private final String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Gender fromString(String value) {
            for (Gender gender : Gender.values()) {
                if (gender.value.equalsIgnoreCase(value)) {
                    return gender;
                }
            }
            throw new IllegalArgumentException("Invalid gender value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Enum untuk status pernikahan agar sesuai dengan database
    public enum MaritalStatus {
        SINGLE("Belum Menikah"), MARRIED("Menikah"), DIVORCED("Cerai Hidup"), WIDOWED("Cerai Mati");

        private final String value;

        MaritalStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MaritalStatus fromString(String value) {
            for (MaritalStatus status : MaritalStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid marital status value: " + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    // Constructor
    public Patient() {
        this.idPasien = new SimpleIntegerProperty();
        this.noRm = new SimpleStringProperty();
        this.nik = new SimpleStringProperty();
        this.namaLengkap = new SimpleStringProperty();
        this.tempatLahir = new SimpleStringProperty();
        this.tanggalLahir = new SimpleObjectProperty<>();
        this.jenisKelamin = new SimpleObjectProperty<>();
        this.alamat = new SimpleStringProperty();
        this.noTelepon = new SimpleStringProperty();
        this.pekerjaan = new SimpleStringProperty();
        this.statusPernikahan = new SimpleObjectProperty<>();
        this.agama = new SimpleStringProperty();
        this.pendidikan = new SimpleStringProperty();
        this.kontakDarurat = new SimpleStringProperty();
        this.noTeleponDarurat = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Patient(
            int idPasien,
            String noRm,
            String nik,
            String namaLengkap,
            String tempatLahir,
            LocalDate tanggalLahir,
            Gender jenisKelamin,
            String alamat,
            String noTelepon,
            String pekerjaan,
            MaritalStatus statusPernikahan,
            String agama,
            String pendidikan,
            String kontakDarurat,
            String noTeleponDarurat,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idPasien = new SimpleIntegerProperty(idPasien);
        this.noRm = new SimpleStringProperty(noRm);
        this.nik = new SimpleStringProperty(nik);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.tempatLahir = new SimpleStringProperty(tempatLahir);
        this.tanggalLahir = new SimpleObjectProperty<>(tanggalLahir);
        this.jenisKelamin = new SimpleObjectProperty<>(jenisKelamin);
        this.alamat = new SimpleStringProperty(alamat);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.pekerjaan = new SimpleStringProperty(pekerjaan);
        this.statusPernikahan = new SimpleObjectProperty<>(statusPernikahan);
        this.agama = new SimpleStringProperty(agama);
        this.pendidikan = new SimpleStringProperty(pendidikan);
        this.kontakDarurat = new SimpleStringProperty(kontakDarurat);
        this.noTeleponDarurat = new SimpleStringProperty(noTeleponDarurat);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // Getter dan Setter untuk id pasien
    public int getIdPasien() {
        return idPasien.get();
    }

    public IntegerProperty idPasienProperty() {
        return idPasien;
    }

    public void setIdPasien(int idPasien) {
        this.idPasien.set(idPasien);
    }

    // Getter dan Setter untuk no RM
    public String getNoRm() {
        return noRm.get();
    }

    public StringProperty noRmProperty() {
        return noRm;
    }

    public void setNoRm(String noRm) {
        this.noRm.set(noRm);
    }

    // Getter dan Setter untuk NIK
    public String getNik() {
        return nik.get();
    }

    public StringProperty nikProperty() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik.set(nik);
    }

    // Getter dan Setter untuk nama lengkap
    public String getNamaLengkap() {
        return namaLengkap.get();
    }

    public StringProperty namaLengkapProperty() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap.set(namaLengkap);
    }

    // Getter dan Setter untuk tempat lahir
    public String getTempatLahir() {
        return tempatLahir.get();
    }

    public StringProperty tempatLahirProperty() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir.set(tempatLahir);
    }

    // Getter dan Setter untuk tanggal lahir
    public LocalDate getTanggalLahir() {
        return tanggalLahir.get();
    }

    public ObjectProperty<LocalDate> tanggalLahirProperty() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir.set(tanggalLahir);
    }

    // Getter dan Setter untuk jenis kelamin
    public Gender getJenisKelamin() {
        return jenisKelamin.get();
    }

    public ObjectProperty<Gender> jenisKelaminProperty() {
        return jenisKelamin;
    }

    public void setJenisKelamin(Gender jenisKelamin) {
        this.jenisKelamin.set(jenisKelamin);
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

    // Getter dan Setter untuk no telepon
    public String getNoTelepon() {
        return noTelepon.get();
    }

    public StringProperty noTeleponProperty() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon.set(noTelepon);
    }

    // Getter dan Setter untuk pekerjaan
    public String getPekerjaan() {
        return pekerjaan.get();
    }

    public StringProperty pekerjaanProperty() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan.set(pekerjaan);
    }

    // Getter dan Setter untuk status pernikahan
    public MaritalStatus getStatusPernikahan() {
        return statusPernikahan.get();
    }

    public ObjectProperty<MaritalStatus> statusPernikahanProperty() {
        return statusPernikahan;
    }

    public void setStatusPernikahan(MaritalStatus statusPernikahan) {
        this.statusPernikahan.set(statusPernikahan);
    }

    // Getter dan Setter untuk agama
    public String getAgama() {
        return agama.get();
    }

    public StringProperty agamaProperty() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama.set(agama);
    }

    // Getter dan Setter untuk pendidikan
    public String getPendidikan() {
        return pendidikan.get();
    }

    public StringProperty pendidikanProperty() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan.set(pendidikan);
    }

    // Getter dan Setter untuk kontak darurat
    public String getKontakDarurat() {
        return kontakDarurat.get();
    }

    public StringProperty kontakDaruratProperty() {
        return kontakDarurat;
    }

    public void setKontakDarurat(String kontakDarurat) {
        this.kontakDarurat.set(kontakDarurat);
    }

    // Getter dan Setter untuk no telepon darurat
    public String getNoTeleponDarurat() {
        return noTeleponDarurat.get();
    }

    public StringProperty noTeleponDaruratProperty() {
        return noTeleponDarurat;
    }

    public void setNoTeleponDarurat(String noTeleponDarurat) {
        this.noTeleponDarurat.set(noTeleponDarurat);
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