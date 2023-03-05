package com.example.where2eat.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CreateBooking implements Serializable {
   /*
    {
        "id": 7,
            "data": "27/03/2023",
            "ora": "20:00",
            "numeroPosti": 2
    }
    */

    private Long idRistorante;
    private String dataPrenotazione;
    private String oraPrenotazione;
    private Integer numeroPosti;

    public CreateBooking() {
    }

    public CreateBooking(Long idRistorante, String dataPrenotazione, String oraPrenotazione, Integer numeroPosti) {
        this.idRistorante = idRistorante;
        this.dataPrenotazione = dataPrenotazione;
        this.oraPrenotazione = oraPrenotazione;
        this.numeroPosti = numeroPosti;
    }

    public JSONObject encodeJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.idRistorante);
            jsonObject.put("data", this.dataPrenotazione);
            jsonObject.put("ora", this.oraPrenotazione);
            jsonObject.put("numeroPosti", this.numeroPosti);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public Long getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(Long idRistorante) {
        this.idRistorante = idRistorante;
    }

    public String getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(String dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public String getOraPrenotazione() {
        return oraPrenotazione;
    }

    public void setOraPrenotazione(String oraPrenotazione) {
        this.oraPrenotazione = oraPrenotazione;
    }

    public Integer getNumeroPosti() {
        return numeroPosti;
    }

    public void setNumeroPosti(Integer numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    @Override
    public String toString() {
        return "CreateBooking{" +
                "idRistorante=" + idRistorante +
                ", dataPrenotazione='" + dataPrenotazione + '\'' +
                ", oraPrenotazione='" + oraPrenotazione + '\'' +
                ", numeroPosti=" + numeroPosti +
                '}';
    }
}
