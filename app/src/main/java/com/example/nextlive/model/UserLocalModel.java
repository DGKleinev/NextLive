package com.example.nextlive.model;

public class UserLocalModel extends UserParentModel{

    private String indirizzo;
    private String proprietario;
    private String orarioApertura;
    private String orarioChiusura;

    public UserLocalModel() {}

    //Costruttore completo
    public UserLocalModel(String nome, String proprietario, String genereMusicale, String citta, String imgProfilo,
                          String tipo, String telefono, String indirizzo, String orarioApertura,
                          String orarioChiusura) {
        super(nome, genereMusicale, citta, imgProfilo, tipo, telefono);
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.proprietario = proprietario;
    }

    //Costruttore per la prima registrazione, con la immagine di profilo
    public UserLocalModel(String nome, String proprietario, String imgProfilo, String tipo, String indirizzo) {
        super(nome, imgProfilo, tipo);
        this.indirizzo = indirizzo;
        this.proprietario = proprietario;
    }

    //Costruttore per la registrazione, senza immagine di profilo
    public UserLocalModel(String nome, String proprietario, String tipo, String indirizzo) {
        super(nome, tipo);
        this.indirizzo = indirizzo;
        this.proprietario = proprietario;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public String getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(String orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }
}
