package com.clinic.models;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    public enum Role {
        ADMIN, NURSE, DOCTOR, DENTIST, PEDIATRICIAN, PHARMACY
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    private final StringProperty username;
    private final StringProperty password;
    private final ObjectProperty<Role> role;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final ObjectProperty<Status> status;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    // Konstruktor default
    public User() {
        this.username = new SimpleStringProperty(null);
        this.password = new SimpleStringProperty(null);
        this.role = new SimpleObjectProperty<>(null);
        this.name = new SimpleStringProperty(null);
        this.email = new SimpleStringProperty(null);
        this.phone = new SimpleStringProperty(null);
        this.status = new SimpleObjectProperty<>(Status.ACTIVE);
        this.createdAt = new SimpleObjectProperty<>(null);
        this.updatedAt = new SimpleObjectProperty<>(null);
    }

    // Konstruktor parameter
    public User(String username, String password, Role role, String name, String email, String phone,
            Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleObjectProperty<>(role);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.status = new SimpleObjectProperty<>(status);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---

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

    // Role
    public ObjectProperty<Role> roleProperty() {
        return role;
    }

    public Role getRole() {
        return role.get();
    }

    public void setRole(Role role) {
        this.role.set(role);
    }

    // Name
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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

    // Phone
    public StringProperty phoneProperty() {
        return phone;
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    // Status
    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public Status getStatus() {
        return status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
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
