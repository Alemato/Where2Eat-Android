package com.example.where2eat.domain.modal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey
    private Long id;

    @ColumnInfo
    private String nome;

    @ColumnInfo
    private String cognome;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String telefono;

    @ColumnInfo
    private String token;

    public User() {
    }

    public User(Long id, String nome, String cognome, String email, String telefono, String token) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.token = token;
    }

    public static User parseJson(JSONObject json){
        System.out.println("#####");
        System.out.println(json);
        User user = new User();
        user.setId(json.optLong("id"));
        user.setNome(json.optString("nome"));
        user.setCognome(json.optString("cognome"));
        user.setEmail(json.optString("email"));
        user.setTelefono(json.optString("telefono"));
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
