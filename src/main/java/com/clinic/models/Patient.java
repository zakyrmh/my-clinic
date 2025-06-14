package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clinic.models.Patient.Gender;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final IntegerProperty id;
    private final StringProperty medicalRecord;
    private final StringProperty name;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final ObjectProperty<Gender> gender;
    private final StringProperty address;
    private final StringProperty phone;
    private final StringProperty identityNumber;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Enum untuk gender agar sesuai dengan database
    public enum Gender {
        MALE("male"), FEMALE("female");

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

    // Constructor
    public Patient() {
        this.id = new SimpleIntegerProperty();
        this.medicalRecord = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.dateOfBirth = new SimpleObjectProperty<>();
        this.gender = new SimpleObjectProperty<>();
        this.address = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.identityNumber = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Patient(int id, String medicalRecord, String name, LocalDate dateOfBirth, Gender gender,
                   String address, String phone, String identityNumber,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.medicalRecord = new SimpleStringProperty(medicalRecord);
        this.name = new SimpleStringProperty(name);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.gender = new SimpleObjectProperty<>(gender);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.identityNumber = new SimpleStringProperty(identityNumber);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // Getter dan Setter untuk id
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getter dan Setter untuk medicalRecord
    public String getMedicalRecord() {
        return medicalRecord.get();
    }

    public StringProperty medicalRecordProperty() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord.set(medicalRecord);
    }

    // Getter dan Setter untuk name
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Getter dan Setter untuk dateOfBirth
    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    // Getter dan Setter untuk gender
    public Gender getGender() {
        return gender.get();
    }

    public ObjectProperty<Gender> genderProperty() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender.set(gender);
    }

    // Getter dan Setter untuk address
    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    // Getter dan Setter untuk phone
    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    // Getter dan Setter untuk identityNumber
    public String getIdentityNumber() {
        return identityNumber.get();
    }

    public StringProperty identityNumberProperty() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber.set(identityNumber);
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