package com.example.nextlive;

public class User2 {
    private String nome;
    private String cognome;
    private String username;
    private String genere;

    public User2(){}

    public User2(String nome, String cognome, String genere, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.genere = genere;
        this.username = username;
    }

    public String getGenere() {
        return genere;
    }

    public String getNome() {
        return nome;
    }


    public String getCognome() {
        return cognome;
    }



    public String getUsername() {
        return username;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

}
