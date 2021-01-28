package com.example.nextlive.model;

public class GenereMusicaleModel {
    private String genere;
    private boolean selected;

    public String getGenere(){
        return genere;
    }

    public void setGenere(String genere){
        this.genere = genere;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
