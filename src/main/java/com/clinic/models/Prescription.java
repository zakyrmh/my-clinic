package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prescription {
    private final IntegerProperty id;
    private final IntegerProperty medicalRecordId;
    private final IntegerProperty doctorId;
    private final ObjectProperty<LocalDate> prescriptionDate;
    private final StringProperty note;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    public Prescription() {
        this.id = new SimpleIntegerProperty();
        this.medicalRecordId = new SimpleIntegerProperty();
        this.doctorId = new SimpleIntegerProperty();
        this.prescriptionDate = new SimpleObjectProperty<>();
        this.note = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public Prescription(int id, int medicalRecordId, int doctorId, LocalDate prescriptionDate, String note,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.medicalRecordId = new SimpleIntegerProperty(medicalRecordId);
        this.doctorId = new SimpleIntegerProperty(doctorId);
        this.prescriptionDate = new SimpleObjectProperty<>(prescriptionDate);
        this.note = new SimpleStringProperty(note);
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

    // Getter dan Setter untuk medicalRecordId
    public int getMedicalRecordId() {
        return medicalRecordId.get();
    }

    public IntegerProperty medicalRecordIdProperty() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId.set(medicalRecordId);
    }

    // Getter dan Setter untuk doctorId
    public int getDoctorId() {
        return doctorId.get();
    }

    public IntegerProperty doctorIdProperty() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId.set(doctorId);
    }

    // Getter dan Setter untuk prescriptionDate
    public LocalDate getPrescriptionDate() {
        return prescriptionDate.get();
    }

    public ObjectProperty<LocalDate> prescriptionDateProperty() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate.set(prescriptionDate);
    }

    // Getter dan Setter untuk note
    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
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
