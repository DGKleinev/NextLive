package com.example.nextlive.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CandidaturaModel {
    private String evento;
    private ArrayList<String> listaEventi = new ArrayList<String>();

    public CandidaturaModel(){}

    public CandidaturaModel(String evento) {
        this.evento = evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public ArrayList<String> getListaEventi(){
        return listaEventi;
    }

    public void setListaEventi(String eventi){
        listaEventi.addAll(Arrays.asList(eventi));
    }

    public  boolean isPresent(String evento){
        for ( String s: this.listaEventi) {
            if (s.equalsIgnoreCase(evento))
                return true;
        }
        return false;
    }

    public void removeEvent(String evento){
        Iterator<String> iter = this.listaEventi.iterator();
        int i = 0;
        while (iter.hasNext()){
            if(iter.next().equalsIgnoreCase(evento)){
                listaEventi.remove(i);
            }
        }
    }
}
