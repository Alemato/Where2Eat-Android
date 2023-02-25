package com.example.where2eat.domain.modal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "restaurants")
public class Restaurant implements Serializable {

    @PrimaryKey
    private Long id;
    @ColumnInfo(name = "ragione_sociale")
    private String ragioneSociale;
    @ColumnInfo
    private String indirizzo;
    @ColumnInfo
    private String localita;
    @ColumnInfo(name = "prezzo_medio_dichiarato")
    private Integer prezzoMedioDichiarato;
    @ColumnInfo(name = "email_aziendale")
    private String emailAziendale;
    @ColumnInfo(name = "telefono_aziendale")
    private String telefonoAziendale;
    @ColumnInfo(name = "stato_ristorante")
    private Boolean statoRistorante;
    @ColumnInfo(name = "capienza_massima")
    private Integer capienzaMassima;
    @ColumnInfo
    private String descrizione;
    @ColumnInfo(name = "descrizione_breve")
    private String descrizioneBreve;

    public Restaurant() {
    }

    public Restaurant(Long id, String ragioneSociale, String indirizzo, String localita, Integer prezzoMedioDichiarato, String emailAziendale, String telefonoAziendale, Boolean statoRistorante, Integer capienzaMassima, String descrizione, String descrizioneBreve) {
        this.id = id;
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.localita = localita;
        this.prezzoMedioDichiarato = prezzoMedioDichiarato;
        this.emailAziendale = emailAziendale;
        this.telefonoAziendale = telefonoAziendale;
        this.statoRistorante = statoRistorante;
        this.capienzaMassima = capienzaMassima;
        this.descrizione = descrizione;
        this.descrizioneBreve = descrizioneBreve;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public Integer getPrezzoMedioDichiarato() {
        return prezzoMedioDichiarato;
    }

    public void setPrezzoMedioDichiarato(Integer prezzoMedioDichiarato) {
        this.prezzoMedioDichiarato = prezzoMedioDichiarato;
    }

    public String getEmailAziendale() {
        return emailAziendale;
    }

    public void setEmailAziendale(String emailAziendale) {
        this.emailAziendale = emailAziendale;
    }

    public String getTelefonoAziendale() {
        return telefonoAziendale;
    }

    public void setTelefonoAziendale(String telefonoAziendale) {
        this.telefonoAziendale = telefonoAziendale;
    }

    public Boolean getStatoRistorante() {
        return statoRistorante;
    }

    public void setStatoRistorante(Boolean statoRistorante) {
        this.statoRistorante = statoRistorante;
    }

    public Integer getCapienzaMassima() {
        return capienzaMassima;
    }

    public void setCapienzaMassima(Integer capienzaMassima) {
        this.capienzaMassima = capienzaMassima;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizioneBreve() {
        return descrizioneBreve;
    }

    public void setDescrizioneBreve(String descrizioneBreve) {
        this.descrizioneBreve = descrizioneBreve;
    }
}
