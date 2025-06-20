package com.clinic.models;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrescriptionItem {
    private final IntegerProperty idDetailResep;
    private final IntegerProperty idResep;
    private final StringProperty namaObat;
    private final StringProperty kategoriObat;
    private final StringProperty satuan;
    private final IntegerProperty jumlah;
    private final StringProperty aturanPakai;
    private final IntegerProperty hargaSatuan;
    private final IntegerProperty subTotal;
    private final StringProperty catatan;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;
    
    public PrescriptionItem() {
        this.idDetailResep = new SimpleIntegerProperty();
        this.idResep = new SimpleIntegerProperty();
        this.namaObat = new SimpleStringProperty();
        this.kategoriObat = new SimpleStringProperty();
        this.satuan = new SimpleStringProperty();
        this.jumlah = new SimpleIntegerProperty();
        this.aturanPakai = new SimpleStringProperty();
        this.hargaSatuan = new SimpleIntegerProperty();
        this.subTotal = new SimpleIntegerProperty();
        this.catatan = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }
    
    public PrescriptionItem(
        int idDetailResep,
        int idResep,
        String namaObat,
        String kategoriObat,
        String satuan,
        int jumlah,
        String aturanPakai,
        int hargaSatuan,
        int subTotal,
        String catatan,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.idDetailResep = new SimpleIntegerProperty(idDetailResep);
        this.idResep = new SimpleIntegerProperty(idResep);
        this.namaObat = new SimpleStringProperty(namaObat);
        this.kategoriObat = new SimpleStringProperty(kategoriObat);
        this.satuan = new SimpleStringProperty(satuan);
        this.jumlah = new SimpleIntegerProperty(jumlah);
        this.aturanPakai = new SimpleStringProperty(aturanPakai);
        this.hargaSatuan = new SimpleIntegerProperty(hargaSatuan);
        this.subTotal = new SimpleIntegerProperty(subTotal);
        this.catatan = new SimpleStringProperty(catatan);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---

    // idDetailResep
    public IntegerProperty idDetailResepProperty() {
        return idDetailResep;
    }

    public int getIdDetailResep() {
        return idDetailResep.get();
    }

    public void setIdDetailResep(int idDetailResep) {
        this.idDetailResep.set(idDetailResep);
    }

    // idResep
    public IntegerProperty idResepProperty() {
        return idResep;
    }

    public int getIdResep() {
        return idResep.get();
    }

    public void setIdResep(int idResep) {
        this.idResep.set(idResep);
    }

    // namaObat
    public StringProperty namaObatProperty() {
        return namaObat;
    }

    public String getNamaObat() {
        return namaObat.get();
    }

    public void setNamaObat(String namaObat) {
        this.namaObat.set(namaObat);
    }

    // kategoriObat
    public StringProperty kategoriObatProperty() {
        return kategoriObat;
    }

    public String getKategoriObat() {
        return kategoriObat.get();
    }

    public void setKategoriObat(String kategoriObat) {
        this.kategoriObat.set(kategoriObat);
    }

    // satuan
    public StringProperty satuanProperty() {
        return satuan;
    }

    public String getSatuan() {
        return satuan.get();
    }

    public void setSatuan(String satuan) {
        this.satuan.set(satuan);
    }

    // jumlah
    public IntegerProperty jumlahProperty() {
        return jumlah;
    }

    public int getJumlah() {
        return jumlah.get();
    }

    public void setJumlah(int jumlah) {
        this.jumlah.set(jumlah);
    }

    // aturanPakai
    public StringProperty aturanPakaiProperty() {
        return aturanPakai;
    }

    public String getAturanPakai() {
        return aturanPakai.get();
    }

    public void setAturanPakai(String aturanPakai) {
        this.aturanPakai.set(aturanPakai);
    }

    // hargaSatuan
    public IntegerProperty hargaSatuanProperty() {
        return hargaSatuan;
    }

    public int getHargaSatuan() {
        return hargaSatuan.get();
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.hargaSatuan.set(hargaSatuan);
    }

    // subTotal
    public IntegerProperty subTotalProperty() {
        return subTotal;
    }

    public int getSubTotal() {
        return subTotal.get();
    }

    public void setSubTotal(int subTotal) {
        this.subTotal.set(subTotal);
    }

    // catatan
    public StringProperty catatanProperty() {
        return catatan;
    }

    public String getCatatan() {
        return catatan.get();
    }

    public void setCatatan(String catatan) {
        this.catatan.set(catatan);
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
