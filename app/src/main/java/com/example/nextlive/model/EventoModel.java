package com.example.nextlive.model;

import java.util.Date;

public class EventoModel {
    private String idAutore;
    private String titoloEvento;
    private String descrizioneEvento;
    private String indirizzoEvento;
   // private Date dataEvento;
    private String dataEvento;
    private String nomeCantante;
    private String genereMusicale;
    private String urlImmagineEvento;

    //https://stackoverflow.com/questions/47706601/users-does-not-define-no-argument-constructor
    public EventoModel() {}

    public EventoModel(String idAutore, String titoloEvento, String descrizioneEvento,
                       String indirizzoEvento, String dataEvento, String genereMusicale,
                       String urlImmagineEvento) {
        this.idAutore = idAutore;
        this.titoloEvento = titoloEvento;
        this.descrizioneEvento = descrizioneEvento;
        this.indirizzoEvento = indirizzoEvento;
        this.dataEvento = dataEvento;
        this.genereMusicale = genereMusicale;
        this.urlImmagineEvento = urlImmagineEvento;
    }

    public EventoModel(String idAutore, String titoloEvento, String descrizioneEvento,
                       String indirizzoEvento, String dataEvento, String nomeCantante,
                       String genereMusicale, String urlImmagineEvento) {
        this.idAutore = idAutore;
        this.titoloEvento = titoloEvento;
        this.descrizioneEvento = descrizioneEvento;
        this.indirizzoEvento = indirizzoEvento;
        this.dataEvento = dataEvento;
        this.nomeCantante = nomeCantante;
        this.genereMusicale = genereMusicale;
        this.urlImmagineEvento = urlImmagineEvento;
    }

    public String getIdAutore() {
        return idAutore;
    }

    public void setIdAutore(String idAutore) {
        this.idAutore = idAutore;
    }

    public String getTitoloEvento() {
        return titoloEvento;
    }

    public void setTitoloEvento(String titoloEvento) {
        this.titoloEvento = titoloEvento;
    }

    public String getDescrizioneEvento() {
        return descrizioneEvento;
    }

    public void setDescrizioneEvento(String descrizioneEvento) {
        this.descrizioneEvento = descrizioneEvento;
    }

    public String getIndirizzoEvento() {
        return indirizzoEvento;
    }

    public void setIndirizzoEvento(String indirizzoEvento) {
        this.indirizzoEvento = indirizzoEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getNomeCantante() {
        return nomeCantante;
    }

    public void setNomeCantante(String nomeCantante) {
        this.nomeCantante = nomeCantante;
    }

    public String getGenereMusicale() {
        return genereMusicale;
    }

    public void setGenereMusicale(String genereMusicale) {
        this.genereMusicale = genereMusicale;
    }

    public String geturlImmagineEvento() {
        return urlImmagineEvento;
    }

    public void seturlImmagineEvento(String urlImmagineEvento) {
        this.urlImmagineEvento = urlImmagineEvento;
    }

}
