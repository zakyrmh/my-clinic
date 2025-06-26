package com.clinic.models;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MedicalRecord {
    private final IntegerProperty idRekamMedis;
    private final IntegerProperty idKunjungan;
    private final IntegerProperty idPasien;
    private final IntegerProperty idDokter;
    private final StringProperty anamnesis;
    private final StringProperty pemeriksaanFisik;
    private final StringProperty tekananDarah;
    private final IntegerProperty nadi;
    private final IntegerProperty suhu;
    private final IntegerProperty respirasi;
    private final IntegerProperty beratBadan;
    private final IntegerProperty tinggiBadan;
    private final StringProperty diagnosisUtama;
    private final StringProperty diagnosisSekunder;
    private final StringProperty tindakan;
    private final StringProperty terapi;
    private final StringProperty catatanDokter;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    public MedicalRecord() {
        this.idRekamMedis = new SimpleIntegerProperty();
        this.idKunjungan = new SimpleIntegerProperty();
        this.idPasien = new SimpleIntegerProperty();
        this.idDokter = new SimpleIntegerProperty();
        this.anamnesis = new SimpleStringProperty();
        this.pemeriksaanFisik = new SimpleStringProperty();
        this.tekananDarah = new SimpleStringProperty();
        this.nadi = new SimpleIntegerProperty();
        this.suhu = new SimpleIntegerProperty();
        this.respirasi = new SimpleIntegerProperty();
        this.beratBadan = new SimpleIntegerProperty();
        this.tinggiBadan = new SimpleIntegerProperty();
        this.diagnosisUtama = new SimpleStringProperty();
        this.diagnosisSekunder = new SimpleStringProperty();
        this.tindakan = new SimpleStringProperty();
        this.terapi = new SimpleStringProperty();
        this.catatanDokter = new SimpleStringProperty();
        this.createdAt = new SimpleObjectProperty<>();
        this.updatedAt = new SimpleObjectProperty<>();
    }

    public MedicalRecord(
        int idRekamMedis,
        int idKunjungan,
        int idPasien,
        int idDokter,
        String anamnesis,
        String pemeriksaanFisik,
        String tekananDarah,
        int nadi,
        int suhu,
        int respirasi,
        int beratBadan,
        int tinggiBadan,
        String diagnosisUtama,
        String diagnosisSekunder,
        String tindakan,
        String terapi,
        String catatanDokter,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.idRekamMedis = new SimpleIntegerProperty(idRekamMedis);
        this.idKunjungan = new SimpleIntegerProperty(idKunjungan);
        this.idPasien = new SimpleIntegerProperty(idPasien);
        this.idDokter = new SimpleIntegerProperty(idDokter);
        this.anamnesis = new SimpleStringProperty(anamnesis);
        this.pemeriksaanFisik = new SimpleStringProperty(pemeriksaanFisik);
        this.tekananDarah = new SimpleStringProperty(tekananDarah);
        this.nadi = new SimpleIntegerProperty(nadi);
        this.suhu = new SimpleIntegerProperty(suhu);
        this.respirasi = new SimpleIntegerProperty(respirasi);
        this.beratBadan = new SimpleIntegerProperty(beratBadan);
        this.tinggiBadan = new SimpleIntegerProperty(tinggiBadan);
        this.diagnosisUtama = new SimpleStringProperty(diagnosisUtama);
        this.diagnosisSekunder = new SimpleStringProperty(diagnosisSekunder);
        this.tindakan = new SimpleStringProperty(tindakan);
        this.terapi = new SimpleStringProperty(terapi);
        this.catatanDokter = new SimpleStringProperty(catatanDokter);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // --- Property Methods, Getters, and Setters ---
    // idRekamMedis
    public int getIdRekamMedis() {
        return idRekamMedis.get();
    }

    public IntegerProperty idRekamMedisProperty() {
        return idRekamMedis;
    }

    public void setIdRekamMedis(int idRekamMedis) {
        this.idRekamMedis.set(idRekamMedis);
    }

    // idKunjungan
    public int getIdKunjungan() {
        return idKunjungan.get();
    }

    public IntegerProperty idKunjunganProperty() {
        return idKunjungan;
    }

    public void setIdKunjungan(int idKunjungan) {
        this.idKunjungan.set(idKunjungan);
    }

    // idPasien
    public int getIdPasien() {
        return idPasien.get();
    }

    public IntegerProperty idPasienProperty() {
        return idPasien;
    }

    public void setIdPasien(int idPasien) {
        this.idPasien.set(idPasien);
    }

    // idDokter
    public int getIdDokter() {
        return idDokter.get();
    }

    public IntegerProperty idDokterProperty() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter.set(idDokter);
    }

    // anamnesis
    public String getAnamnesis() {
        return anamnesis.get();
    }

    public StringProperty anamnesisProperty() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis.set(anamnesis);
    }

    // pemeriksaanFisik
    public String getPemeriksaanFisik() {
        return pemeriksaanFisik.get();
    }

    public StringProperty pemeriksaanFisikProperty() {
        return pemeriksaanFisik;
    }

    public void setPemeriksaanFisik(String pemeriksaanFisik) {
        this.pemeriksaanFisik.set(pemeriksaanFisik);
    }

    // tekananDarah
    public String getTekananDarah() {
        return tekananDarah.get();
    }

    public StringProperty tekananDarahProperty() {
        return tekananDarah;
    }

    public void setTekananDarah(String tekananDarah) {
        this.tekananDarah.set(tekananDarah);
    }

    // nadi
    public int getNadi() {
        return nadi.get();
    }

    public IntegerProperty nadiProperty() {
        return nadi;
    }

    public void setNadi(int nadi) {
        this.nadi.set(nadi);
    }

    // suhu
    public int getSuhu() {
        return suhu.get();
    }

    public IntegerProperty suhuProperty() {
        return suhu;
    }

    public void setSuhu(int suhu) {
        this.suhu.set(suhu);
    }

    // respirasi
    public int getRespirasi() {
        return respirasi.get();
    }

    public IntegerProperty respirasiProperty() {
        return respirasi;
    }

    public void setRespirasi(int respirasi) {
        this.respirasi.set(respirasi);
    }

    // beratBadan
    public int getBeratBadan() {
        return beratBadan.get();
    }

    public IntegerProperty beratBadanProperty() {
        return beratBadan;
    }

    public void setBeratBadan(int beratBadan) {
        this.beratBadan.set(beratBadan);
    }

    // tinggiBadan
    public int getTinggiBadan() {
        return tinggiBadan.get();
    }

    public IntegerProperty tinggiBadanProperty() {
        return tinggiBadan;
    }

    public void setTinggiBadan(int tinggiBadan) {
        this.tinggiBadan.set(tinggiBadan);
    }

    // diagnosisUtama
    public String getDiagnosisUtama() {
        return diagnosisUtama.get();
    }

    public StringProperty diagnosisUtamaProperty() {
        return diagnosisUtama;
    }

    public void setDiagnosisUtama(String diagnosisUtama) {
        this.diagnosisUtama.set(diagnosisUtama);
    }

    // diagnosisSekunder
    public String getDiagnosisSekunder() {
        return diagnosisSekunder.get();
    }

    public StringProperty diagnosisSekunderProperty() {
        return diagnosisSekunder;
    }

    public void setDiagnosisSekunder(String diagnosisSekunder) {
        this.diagnosisSekunder.set(diagnosisSekunder);
    }

    // tindakan
    public String getTindakan() {
        return tindakan.get();
    }

    public StringProperty tindakanProperty() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        this.tindakan.set(tindakan);
    }

    // terapi
    public String getTerapi() {
        return terapi.get();
    }

    public StringProperty terapiProperty() {
        return terapi;
    }

    public void setTerapi(String terapi) {
        this.terapi.set(terapi);
    }

    // catatanDokter
    public String getCatatanDokter() {
        return catatanDokter.get();
    }

    public StringProperty catatanDokterProperty() {
        return catatanDokter;
    }

    public void setCatatanDokter(String catatanDokter) {
        this.catatanDokter.set(catatanDokter);
    }

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    // untuk updatedAt
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
