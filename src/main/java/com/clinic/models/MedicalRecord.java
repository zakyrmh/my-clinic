package com.clinic.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MedicalRecord {
    private final IntegerProperty id;
    private final IntegerProperty patientId;
    private final IntegerProperty doctorId;
    private final ObjectProperty<LocalDate> visitDate;
    private final StringProperty chiefComplaint;
    private final StringProperty historyOfIllness;
    private final StringProperty vitalSigns;
    private final StringProperty physicalExam;
    private final StringProperty diagnosis;
    private final StringProperty treatment;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    public MedicalRecord() {
        this.id = new SimpleIntegerProperty();
        this.patientId = new SimpleIntegerProperty();
        this.doctorId = new SimpleIntegerProperty();
        this.visitDate = new SimpleObjectProperty<>();
        this.chiefComplaint = new SimpleStringProperty();
        this.historyOfIllness = new SimpleStringProperty();
        this.vitalSigns = new SimpleStringProperty();
        this.physicalExam = new SimpleStringProperty();
        this.diagnosis = new SimpleStringProperty();
        this.treatment = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public MedicalRecord(int id, int patientId, int doctorId, LocalDate visitDate, String chiefComplaint,
            String historyOfIllness, String vitalSigns, String physicalExam, String diagnosis, String treatment,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.doctorId = new SimpleIntegerProperty(doctorId);
        this.visitDate = new SimpleObjectProperty<>(visitDate);
        this.chiefComplaint = new SimpleStringProperty(chiefComplaint);
        this.historyOfIllness = new SimpleStringProperty(historyOfIllness);
        this.vitalSigns = new SimpleStringProperty(vitalSigns);
        this.physicalExam = new SimpleStringProperty(physicalExam);
        this.diagnosis = new SimpleStringProperty(diagnosis);
        this.treatment = new SimpleStringProperty(treatment);
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

    // Getter dan Setter untuk patientId
    public int getPatientId() {
        return patientId.get();
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
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

    // Getter dan Setter untuk visitDate
    public LocalDate getVisitDate() {
        return visitDate.get();
    }

    public ObjectProperty<LocalDate> visitDateProperty() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate.set(visitDate);
    }

    // Getter dan Setter untuk chiefComplaint
    public String getChiefComplaint() {
        return chiefComplaint.get();
    }

    public StringProperty chiefComplaintProperty() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint.set(chiefComplaint);
    }

    // Getter dan Setter untuk historyOfIllness
    public String getHistoryOfIllness() {
        return historyOfIllness.get();
    }

    public StringProperty historyOfIllnessProperty() {
        return historyOfIllness;
    }

    public void setHistoryOfIllness(String historyOfIllness) {
        this.historyOfIllness.set(historyOfIllness);
    }

    // Getter dan Setter untuk vitalSigns
    public String getVitalSigns() {
        return vitalSigns.get();
    }

    public StringProperty vitalSignsProperty() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns.set(vitalSigns);
    }

    // Getter dan Setter untuk physicalExam
    public String getPhysicalExam() {
        return physicalExam.get();
    }

    public StringProperty physicalExamProperty() {
        return physicalExam;
    }

    public void setPhysicalExam(String physicalExam) {
        this.physicalExam.set(physicalExam);
    }

    // Getter dan Setter untuk diagnosis
    public String getDiagnosis() {
        return diagnosis.get();
    }

    public StringProperty diagnosisProperty() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis.set(diagnosis);
    }

    // Getter dan Setter untuk treatment
    public String getTreatment() {
        return treatment.get();
    }

    public StringProperty treatmentProperty() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment.set(treatment);
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
