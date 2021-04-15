package com.example.nextlive.model;

import android.media.Image;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

public class CantanteModel{
    public static final int TIPO = 1;
    private int numero;
    private String descrizione, website, genereMusicali;
    private Image immagineProfilo,immagineCopertina;

    public CantanteModel() {}

    public CantanteModel(String descrizione, String website, int numero, String genereMusicali){
        this.descrizione = descrizione;
        this.website = website;
        this.numero = numero;
        this.genereMusicali = genereMusicali;
        this.immagineProfilo = immagineProfilo;
        this.immagineCopertina = immagineCopertina;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getWebsite() {
        return website;
    }

    public int getNumero() {
        return numero;
    }

    public String getGenereMusicali() {
        return genereMusicali;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setGenereMusicali(String genereMusicali) {
        this.genereMusicali = genereMusicali;
    }

}
