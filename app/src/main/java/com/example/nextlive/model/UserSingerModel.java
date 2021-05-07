package com.example.nextlive.model;

public class UserSingerModel extends UserFanModel {
    public UserSingerModel() {}

    //Costruttore completo
    public UserSingerModel(String nome, String genereMusicale, String citta, String imgProfilo,
                           String tipo, String dataNascita, String cognome, String username,
                           String genere, String telefono) {
        super(nome, genereMusicale, citta, imgProfilo, tipo, dataNascita, cognome, username, genere, telefono);
    }

    //prima registrazione con l'immagine di profilo
    public UserSingerModel(String nome, String imgProfilo, String tipo, String dataNascita, String cognome, String username) {
        super(nome, imgProfilo, tipo, dataNascita, cognome, username);
    }

    //prima registrazione senza l'immagine di profilo
    public UserSingerModel(String nome, String tipo, String dataNascita, String cognome, String username) {
        super(nome,tipo, dataNascita, cognome, username);
    }

    //lista Cantante Model
    public void mostraCantanti(String Username, String citta, String genereMusicale, String imgProfilo){

    }
}
