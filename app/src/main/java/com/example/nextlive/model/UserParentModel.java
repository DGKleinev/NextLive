package com.example.nextlive.model;

public class UserParentModel {
    //Motivazioni esclusioni attributi durante la registrazione
    /*
     * Data di nascita: Siccome non sappiamo l'identità del nuovo utente, prefiriamo chiederlo durante la selezione utente
     * Email, Password: Vengono salvati in Firebase Authentication
     * Username, Genere: non serve al locale
     * Cognome: non serve al locale, il nome può essere pensato come nomeLocale nel caso del Locale
     */
    private String nome;
    private String genereMusicale;
    private String citta;
    private String imgProfilo;
    private String tipo;
    private String telefono;

    public UserParentModel(){}

    //Costruttore completo
    public UserParentModel(String nome, String genereMusicale, String citta, String imgProfilo, String tipo, String telefono) {
        this.nome = nome;
        this.genereMusicale = genereMusicale;
        this.citta = citta;
        this.imgProfilo = imgProfilo;
        this.tipo = tipo;
        this.telefono = telefono;
    }

    //Costruttore con l'assensa del genereMusicale (durante la prima registrazione dell'utente)
    public UserParentModel(String nome, String imgProfilo, String tipo) {
        this.nome = nome;
        this.imgProfilo = imgProfilo;
        this.tipo = tipo;
    }

    //Costruttore per la prima registrazione, con l'assenza dell'immagine di profilo
    public UserParentModel(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImgProfilo() {
        return imgProfilo;
    }

    public void setImgProfilo(String imgProfilo) {
        this.imgProfilo = imgProfilo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenereMusicale() {
        return genereMusicale;
    }

    public void setGenereMusicale(String genereMusicale) {
        this.genereMusicale = genereMusicale;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}