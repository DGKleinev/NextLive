package com.example.nextlive;

public class User {
    public String idRegistrazione, nome, cognome, email, password,username;

    public User(String idRegistrazione, String nome, String cognome, String username, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.username = username;
        this.idRegistrazione = idRegistrazione;
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
/*
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public void setIdRegistrazione(String idRegistrazione) {
        this.idRegistrazione = idRegistrazione;
    }



    public void setEmail(String email) {
        this.email = email;
    }
*/
}
