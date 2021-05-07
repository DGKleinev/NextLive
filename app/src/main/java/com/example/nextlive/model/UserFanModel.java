package com.example.nextlive.model;

public class UserFanModel extends UserParentModel {

    private String dataNascita;
    private String cognome;
    private String username;
    private String genere;

    public UserFanModel() {}

    //Costruttore completo: Serve al momento della modifica dell'oggetto
    public UserFanModel(String nome, String genereMusicale, String citta, String imgProfilo,
                        String tipo, String dataNascita, String cognome, String username,
                        String genere, String telefono) {
        super(nome, genereMusicale, citta, imgProfilo, tipo, telefono);
        this.dataNascita = dataNascita;
        this.cognome = cognome;
        this.username = username;
        this.genere = genere;
    }

    //Costruttore che serve per la prima registrazione dell'utente
    public UserFanModel(String nome, String imgProfilo, String tipo,
                        String dataNascita, String cognome, String username) {
        super(nome, imgProfilo, tipo);
        this.dataNascita = dataNascita;
        this.cognome = cognome;
        this.username = username;
    }

    //Costruttore che serve per la prima registrazione dell'utente nel caso non voglia inserire una
    //immagine di profilo
    public UserFanModel(String nome, String tipo, String dataNascita,
                        String cognome, String username) {
        super(nome, tipo);
        this.dataNascita = dataNascita;
        this.cognome = cognome;
        this.username = username;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }
}
