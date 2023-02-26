package com.example.where2eat.domain.modal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @ColumnInfo
    private String longitudine;
    @ColumnInfo
    private String latitudine;
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
    @ColumnInfo(name = "voto_medio")
    private String votoMedio;

    @ColumnInfo
    private String immagine;
    @ColumnInfo
    private String servizi;
    @ColumnInfo(name = "metodi_di_pagamento")
    private String metodiDiPagamento;
    @ColumnInfo(name = "tipi_di_cucina")
    private String tipologiaCucina;


    public Restaurant() {
    }

    public Restaurant(Long id, String ragioneSociale, String indirizzo, String localita, String longitudine, String latitudine, Integer prezzoMedioDichiarato, String emailAziendale, String telefonoAziendale, Boolean statoRistorante, Integer capienzaMassima, String descrizione, String descrizioneBreve, String votoMedio, String immagine, String servizi, String metodiDiPagamento, String tipologiaCucina) {
        this.id = id;
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.localita = localita;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
        this.prezzoMedioDichiarato = prezzoMedioDichiarato;
        this.emailAziendale = emailAziendale;
        this.telefonoAziendale = telefonoAziendale;
        this.statoRistorante = statoRistorante;
        this.capienzaMassima = capienzaMassima;
        this.descrizione = descrizione;
        this.descrizioneBreve = descrizioneBreve;
        this.votoMedio = votoMedio;
        this.immagine = immagine;
        this.servizi = servizi;
        this.metodiDiPagamento = metodiDiPagamento;
        this.tipologiaCucina = tipologiaCucina;
    }

    public static Restaurant parseJson(JSONObject json) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(json.optLong("id"));
        restaurant.setRagioneSociale(json.optString("ragioneSociale"));
        restaurant.setIndirizzo(json.optString("indirizzo"));
        restaurant.setLocalita(json.optString("localita"));
        restaurant.setLongitudine(json.optString("longitudine"));
        restaurant.setLatitudine(json.optString("latitudine"));
        restaurant.setPrezzoMedioDichiarato(json.optInt("prezzoMedioDichiarato"));
        restaurant.setEmailAziendale(json.optString("emailAziendale"));
        restaurant.setStatoRistorante(json.optBoolean("statoRistorante"));
        restaurant.setCapienzaMassima(json.optInt("capienzaMassima"));
        restaurant.setDescrizione(json.optString("descrizione"));
        restaurant.setDescrizioneBreve(json.optString("descrizioneBreve"));

        // Immagine
        JSONArray immagini = json.optJSONArray("immagini");
        if (immagini != null && immagini.length() > 0) {
            //restaurant.setImmagine(restaurant.getId() + "/immagine-copertina");
            try {
                restaurant.setImmagine(immagini.getJSONObject(0).optString("path"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        // Servizi
        JSONArray servizi = json.optJSONArray("servizi");
        StringBuilder serviziStringBuilder = new StringBuilder();
        if (servizi != null && servizi.length() > 0) {
            for (int i = 0; i < servizi.length(); i++) {
                try {
                    serviziStringBuilder.append(servizi.getJSONObject(i).optString("nome")).append(", ");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            restaurant.setServizi(serviziStringBuilder.toString());
        } else {
            restaurant.setServizi("N.D.");
        }

        // Metodi Di Pagamento
        JSONArray metodiPagamento = json.optJSONArray("metodiPagamento");
        StringBuilder metodiPagamentoStringBuilder = new StringBuilder();
        if (metodiPagamento != null && metodiPagamento.length() > 0) {
            for (int i = 0; i < metodiPagamento.length(); i++) {
                try {
                    metodiPagamentoStringBuilder.append(metodiPagamento.getJSONObject(i).optString("nome")).append(", ");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            restaurant.setMetodiDiPagamento(metodiPagamentoStringBuilder.toString());
        } else {
            restaurant.setMetodiDiPagamento("N.D.");
        }

        // Tipi Di Cucina
        JSONArray tipiCucina = json.optJSONArray("tipologiaCucina");
        StringBuilder tipiCucinaStringBuilder = new StringBuilder();
        if (tipiCucina != null && tipiCucina.length() > 0) {
            for (int i = 0; i < tipiCucina.length(); i++) {
                try {
                    tipiCucinaStringBuilder.append(tipiCucina.getJSONObject(i).optString("nome")).append(", ");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            restaurant.setTipologiaCucina(tipiCucinaStringBuilder.toString());
        } else {
            restaurant.setTipologiaCucina("N.D.");
        }

        // Voto Medio
        JSONArray recensioni = json.optJSONArray("recensioni");
        if (recensioni != null && recensioni.length() > 0) {
            int votoTotale = 0;

            for (int i = 0; i < recensioni.length(); i++) {
                try {
                    votoTotale += Integer.parseInt(recensioni.getJSONObject(i).optString("voto"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            float votoMedio;
            votoMedio = (float) votoTotale / recensioni.length();
            restaurant.setVotoMedio(String.valueOf(votoMedio));
        } else {
            restaurant.setVotoMedio("N.D.");
        }

        return restaurant;
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

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
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

    public String getVotoMedio() {
        return votoMedio;
    }

    public void setVotoMedio(String votoMedio) {
        this.votoMedio = votoMedio;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getServizi() {
        return servizi;
    }

    public void setServizi(String servizi) {
        this.servizi = servizi;
    }

    public String getMetodiDiPagamento() {
        return metodiDiPagamento;
    }

    public void setMetodiDiPagamento(String metodiDiPagamento) {
        this.metodiDiPagamento = metodiDiPagamento;
    }

    public String getTipologiaCucina() {
        return tipologiaCucina;
    }

    public void setTipologiaCucina(String tipologiaCucina) {
        this.tipologiaCucina = tipologiaCucina;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", ragioneSociale='" + ragioneSociale + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", localita='" + localita + '\'' +
                ", prezzoMedioDichiarato=" + prezzoMedioDichiarato +
                ", emailAziendale='" + emailAziendale + '\'' +
                ", telefonoAziendale='" + telefonoAziendale + '\'' +
                ", statoRistorante=" + statoRistorante +
                ", capienzaMassima=" + capienzaMassima +
                ", descrizione='" + descrizione + '\'' +
                ", descrizioneBreve='" + descrizioneBreve + '\'' +
                ", votoMedio='" + votoMedio + '\'' +
                ", servizi='" + servizi + '\'' +
                ", metodiDiPagamento='" + metodiDiPagamento + '\'' +
                ", tipiDiCucina='" + tipologiaCucina + '\'' +
                '}';
    }
}
