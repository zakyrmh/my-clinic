package com.clinic.models;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final IntegerProperty idUser;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty namaLengkap;
    private final StringProperty noTelepon;
    private final StringProperty email;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Konstruktor default
    public User() {
        this.idUser = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.namaLengkap = new SimpleStringProperty();
        this.noTelepon = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    // Konstruktor parameter
    public User(int idUser, String username, String password, String namaLengkap, String noTelepon, String email,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idUser = new SimpleIntegerProperty(idUser);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.email = new SimpleStringProperty(email);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---
    // Id User
    public IntegerProperty idUserProperty() {
        return idUser;
    }

    public int getIdUser() {
        return idUser.get();
    }

    public void setIdUser(int idUser) {
        this.idUser.set(idUser);
    }

    // Username
    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    // Password
    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Nama Lengkap
    public StringProperty namaLengkapProperty() {
        return namaLengkap;
    }

    public String getNamaLengkap() {
        return namaLengkap.get();
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap.set(namaLengkap);
    }

    // No Telepon
    public StringProperty noTeleponProperty() {
        return noTelepon;
    }

    public String getNoTelepon() {
        return noTelepon.get();
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon.set(noTelepon);
    }

    // Email
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
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
