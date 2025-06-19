package com.clinic.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrescriptionItem {
    private final IntegerProperty id;
    private final IntegerProperty prescriptionId;
    private final StringProperty drug;
    private final StringProperty dosage;
    private final StringProperty frequency;
    private final StringProperty duration;
    private final StringProperty instructions;

    public PrescriptionItem() {
        this.id = new SimpleIntegerProperty();
        this.prescriptionId = new SimpleIntegerProperty();
        this.drug = new SimpleStringProperty();
        this.dosage = new SimpleStringProperty();
        this.frequency = new SimpleStringProperty();
        this.duration = new SimpleStringProperty();
        this.instructions = new SimpleStringProperty();
    }
    
    public PrescriptionItem(int id, int prescriptionId, String drug, String dosage, String frequency, String duration, String instructions) {
        this.id = new SimpleIntegerProperty(id);
        this.prescriptionId = new SimpleIntegerProperty(prescriptionId);
        this.drug = new SimpleStringProperty(drug);
        this.dosage = new SimpleStringProperty(dosage);
        this.frequency = new SimpleStringProperty(frequency);
        this.duration = new SimpleStringProperty(duration);
        this.instructions = new SimpleStringProperty(instructions);
    }

    // Getter dan Setter untuk id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getter dan Setter untuk prescriptionId
    public int getPrescriptionId() {
        return prescriptionId.get();
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId.set(prescriptionId);
    }

    public IntegerProperty prescriptionIdProperty() {
        return prescriptionId;
    }

    // Getter dan Setter untuk drug
    public String getDrug() {
        return drug.get();
    }

    public void setDrug(String drug) {
        this.drug.set(drug);
    }

    public StringProperty drugProperty() {
        return drug;
    }

    // Getter dan Setter untuk dosage
    public String getDosage() {
        return dosage.get();
    }

    public void setDosage(String dosage) {
        this.dosage.set(dosage);
    }

    public StringProperty dosageProperty() {
        return dosage;
    }

    // Getter dan Setter untuk frequency
    public String getFrequency() {
        return frequency.get();
    }

    public void setFrequency(String frequency) {
        this.frequency.set(frequency);
    }

    public StringProperty frequencyProperty() {
        return frequency;
    }

    // Getter dan Setter untuk duration
    public String getDuration() {
        return duration.get();
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public StringProperty durationProperty() {
        return duration;
    }

    // Getter dan Setter untuk instructions
    public String getInstructions() {
        return instructions.get();
    }

    public void setInstructions(String instructions) {
        this.instructions.set(instructions);
    }

    public StringProperty instructionsProperty() {
        return instructions;
    }
}
