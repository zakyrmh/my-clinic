package com.clinic.models;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
    private final IntegerProperty id;
    private final StringProperty licenseNo;
    private final StringProperty name;
    private final StringProperty specialization;
    private final StringProperty phone;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    public Doctor() {
        this.id = new SimpleIntegerProperty();
        this.licenseNo = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.specialization = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Doctor(int id, String licenseNo, String name, String specialization, String phone, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.licenseNo = new SimpleStringProperty(licenseNo);
        this.name = new SimpleStringProperty(name);
        this.specialization = new SimpleStringProperty(specialization);
        this.phone = new SimpleStringProperty(phone);
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

    // Getter dan Setter untuk licenseNo
    public String getLicenseNo() {
        return licenseNo.get();
    }

    public StringProperty licenseNoProperty() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo.set(licenseNo);
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

    // Getter dan Setter untuk specialization
    public String getSpecialization() {
        return specialization.get();
    }

    public StringProperty specializationProperty() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization.set(specialization);
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