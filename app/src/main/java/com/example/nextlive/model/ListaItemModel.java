package com.example.nextlive.model;

public class ListaItemModel {

    private int imageId;
    private String titolo;
    private String citta;
    private String prima_informazione;

    public ListaItemModel(int imageId, String titolo, String citta, String prima_informazione){
        this.imageId = imageId;
        this.titolo = titolo;
        this.citta = citta;
        this.prima_informazione = prima_informazione;
    }

    public int getImageId(){
        return imageId;
    }

    public String getTitolo(){
        return titolo;
    }

    public String getCitta(){
        return citta;
    }

    public String getPrima_informazione(){
        return prima_informazione;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }

    public void setTitolo(String titolo){
        this.titolo = titolo;
    }

    public void setCitta(String citta){
        this.citta = citta;
    }

    public void setPrima_informazione(String prima_informazione){
        this.prima_informazione = prima_informazione;
    }

}
