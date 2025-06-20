package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clinic.models.Patient.BloodType;
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
    private final ObjectProperty<Gender> jenisKelamin;
    private final ObjectProperty<LocalDate> tanggalLahir;
    private final StringProperty tempatLahir;
    private final StringProperty alamat;
    private final StringProperty noTelepon;
    private final StringProperty email;
    private final StringProperty pekerjaan;
    private final ObjectProperty<MaritalStatus> statusPernikahan;
    private final ObjectProperty<BloodType> golonganDarah;
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

    // Enum untuk golongan darah agar sesuai dengan database
    public enum BloodType {
        // ENUM('A', 'B', 'AB', 'O', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-')
        A("A"), B("B"), AB("AB"), O("O"), AP("A+"), AM("A-"), BP("B+"), BM("B-"), ABP("AB+"), ABM("AB-"), OP("O+"), OM("O-");
    
        private final String value;
    
        BloodType(String value) {
            this.value = value;
        }
    
        public String getValue() {
            return value;
        }
    
        public static BloodType fromString(String value) {
            for (BloodType bloodType : BloodType.values()) {
                if (bloodType.value.equalsIgnoreCase(value)) {
                    return bloodType;
                }
            }
            throw new IllegalArgumentException("Invalid blood type value: " + value);
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
        this.jenisKelamin = new SimpleObjectProperty<>();
        this.tanggalLahir = new SimpleObjectProperty<>();
        this.tempatLahir = new SimpleStringProperty();
        this.alamat = new SimpleStringProperty();
        this.noTelepon = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.pekerjaan = new SimpleStringProperty();
        this.statusPernikahan = new SimpleObjectProperty<>();
        this.golonganDarah = new SimpleObjectProperty<>();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Patient(
            int idPasien,
            String noRm,
            String nik,
            String namaLengkap,
            Gender jenisKelamin,
            LocalDate tanggalLahir,
            String tempatLahir,
            String alamat,
            String noTelepon,
            String email,
            String pekerjaan,
            MaritalStatus statusPernikahan,
            BloodType golonganDarah,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.idPasien = new SimpleIntegerProperty(idPasien);
        this.noRm = new SimpleStringProperty(noRm);
        this.nik = new SimpleStringProperty(nik);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.jenisKelamin = new SimpleObjectProperty<>(jenisKelamin);
        this.tanggalLahir = new SimpleObjectProperty<>(tanggalLahir);
        this.tempatLahir = new SimpleStringProperty(tempatLahir);
        this.alamat = new SimpleStringProperty(alamat);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.email = new SimpleStringProperty(email);
        this.pekerjaan = new SimpleStringProperty(pekerjaan);
        this.statusPernikahan = new SimpleObjectProperty<>(statusPernikahan);
        this.golonganDarah = new SimpleObjectProperty<>(golonganDarah);
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

    // Getter dan Setter untuk golongan darah
    public BloodType getGolonganDarah() {
        return golonganDarah.get();
    }

    public ObjectProperty<BloodType> golonganDarahProperty() {
        return golonganDarah;
    }

    public void setGolonganDarah(BloodType golonganDarah) {
        this.golonganDarah.set(golonganDarah);
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