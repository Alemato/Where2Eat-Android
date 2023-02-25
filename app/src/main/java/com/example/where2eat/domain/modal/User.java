package com.example.where2eat.domain.modal;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;

    private String nome;

    private String cognome;

    private String email;

    private String telefono;

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
