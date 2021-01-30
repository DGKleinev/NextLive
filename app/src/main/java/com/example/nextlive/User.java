package com.example.nextlive;

public class User {
    private String idRegistrazione;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String username;
    private String genere;

    public User(){}

    public User(String idRegistrazione, String nome, String cognome, String genere, String username, String email, String password) {
        this.idRegistrazione = idRegistrazione;
        this.nome = nome;
        this.cognome = cognome;
        this.genere = genere;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getGenere() {
        return genere;
    }
    public String getEmail() {
        return email;
    }
    public String getIdRegistrazione() {
        return idRegistrazione;
    }

    public String getNome() {
        return nome;
    }


    public String getCognome() {
        return cognome;
    }


    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }
    public void setIdRegistrazione(String idRegistrazione) {
        this.idRegistrazione = idRegistrazione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public boolean existEmail(String email){
        if(getEmail().equalsIgnoreCase(email))
            return true;
        else
            return false;
    }

}
