package com.example.where2eat.domain.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "bookings")
public class Booking {

    @PrimaryKey
    private Long id;

    @ColumnInfo
    private String data;

    @ColumnInfo
    private String ora;

    @ColumnInfo(name = "stato_prenotazione")
    private String statoPrenotazione;
    @ColumnInfo(name = "numero_posti")
    private Integer numeroPosti;

    @ColumnInfo(name = "ristorante_id")
    private Long ristoranteId;

    @ColumnInfo(name = "nome_ristorante")
    private String nomeRistorante;
    @ColumnInfo
    private String localita;
    @ColumnInfo
    private String indirizzo;

    @ColumnInfo
    private String immagine;


    public Booking() {
    }

    public Booking(Long id, String data, String ora, String statoPrenotazione, Integer numeroPosti, Long ristoranteId, String nomeRistorante, String localita, String indirizzo, String immagine) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.statoPrenotazione = statoPrenotazione;
        this.numeroPosti = numeroPosti;
        this.ristoranteId = ristoranteId;
        this.nomeRistorante = nomeRistorante;
        this.localita = localita;
        this.indirizzo = indirizzo;
        this.immagine = immagine;
    }
    public static Booking parseJson(JSONObject json) {
        Booking booking = new Booking();
        booking.setId(json.optLong("id"));
        booking.setData(json.optString("data"));
        booking.setOra(json.optString("ora"));
        String statoPrenotazione = json.optString("statoPrenotazione");
        if (!statoPrenotazione.equals("")) {
            if (statoPrenotazione.equals("0")) {
                statoPrenotazione = "PRENOTATO";
            } else if (statoPrenotazione.equals("1")) {
                statoPrenotazione = "CONFERMATO";
            } else if (statoPrenotazione.equals("2")) {
                statoPrenotazione = "RIFIUTATO";
            } else if (statoPrenotazione.equals("3")) {
                statoPrenotazione = "COMPLETATO";
            } else if (statoPrenotazione.equals("4")) {
                statoPrenotazione = "ANNULLATO";
            }
        }
        booking.setStatoPrenotazione(statoPrenotazione);
        booking.setNumeroPosti(json.optInt("numeroPosti"));
        try {
            JSONObject rist = json.getJSONObject("ristorante");
            booking.setRistoranteId(rist.optLong("id"));
            booking.setNomeRistorante(rist.optString("ragioneSociale"));
            booking.setIndirizzo(rist.optString("indirizzo"));
            booking.setLocalita(rist.optString("localita"));

            JSONArray immagini = rist.optJSONArray("immagini");
            if (immagini != null && immagini.length() > 0) {
                String url = "http://192.168.0.160:8080/api/uploads/" + immagini.getJSONObject(0).optString("path");
                booking.setImmagine(url);

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return booking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getStatoPrenotazione() {
        return statoPrenotazione;
    }

    public void setStatoPrenotazione(String statoPrenotazione) {
        this.statoPrenotazione = statoPrenotazione;
    }

    public Integer getNumeroPosti() {
        return numeroPosti;
    }

    public void setNumeroPosti(Integer numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public Long getRistoranteId() {
        return ristoranteId;
    }

    public void setRistoranteId(Long ristoranteId) {
        this.ristoranteId = ristoranteId;
    }

    public String getNomeRistorante() {
        return nomeRistorante;
    }

    public void setNomeRistorante(String nomeRistorante) {
        this.nomeRistorante = nomeRistorante;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", ora='" + ora + '\'' +
                ", statoPrenotazione='" + statoPrenotazione + '\'' +
                ", numeroPosti=" + numeroPosti +
                ", ristoranteId=" + ristoranteId +
                ", nomeRistorante='" + nomeRistorante + '\'' +
                ", localita='" + localita + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", immagine='" + immagine + '\'' +
                '}';
    }
}
