package com.example.nextlive.model;

public class GridViewModel {
    private int imageID;
    private String titolo;

    public GridViewModel(int imageID, String titolo){
        this.imageID = imageID;
        this.titolo = titolo;
    }

    public int getImageID(){
        return imageID;
    }

    public String getTitolo(){
        return titolo;
    }

    public void setImageID(int imageID){
        this.imageID = imageID;
    }

    public void setTitolo(String titolo){
        this.titolo = titolo;
    }


}
