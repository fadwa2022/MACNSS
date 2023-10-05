package org.cnss.model;

public class Société {
    private int numéroSociété;
    private String nomSociété;
    private String email;
    private int password;

    public Société(int numéroSociété, String nomSociété, String email, int password) {
        this.numéroSociété = numéroSociété;
        this.nomSociété = nomSociété;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getNuméroSociété() {
        return numéroSociété;
    }

    public void setNuméroSociété(int numéroSociété) {
        this.numéroSociété = numéroSociété;
    }

    public String getNomSociété() {
        return nomSociété;
    }

    public void setNomSociété(String nomSociété) {
        this.nomSociété = nomSociété;
    }

}
