package org.cnss.model;

import java.util.Date;

public class Salaires {
    private int id;
    private String matriculeEmployer;
    private int Salaire ;
  private   Société societe;
  private int nombreJoursTravailles;
  private  int joursCotises;
  private Date dateSalaire;

    public Salaires(int id, String matriculeEmployer, int salaire, Société societe, int nombreJoursTravailles, int joursCotises, Date dateSalaire) {
        this.id = id;
        this.matriculeEmployer = matriculeEmployer;
        Salaire = salaire;
        this.societe = societe;
        this.nombreJoursTravailles = nombreJoursTravailles;
        this.joursCotises = joursCotises;
        this.dateSalaire = dateSalaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatriculeEmployer() {
        return matriculeEmployer;
    }

    public void setMatriculeEmployer(String matriculeEmployer) {
        this.matriculeEmployer = matriculeEmployer;
    }

    public int getSalaire() {
        return Salaire;
    }

    public void setSalaire(int salaire) {
        Salaire = salaire;
    }

    public Société getSociete() {
        return societe;
    }

    public void setSociete(Société societe) {
        this.societe = societe;
    }

    public int getNombreJoursTravailles() {
        return nombreJoursTravailles;
    }

    public void setNombreJoursTravailles(int nombreJoursTravailles) {
        this.nombreJoursTravailles = nombreJoursTravailles;
    }

    public int getJoursCotises() {
        return joursCotises;
    }

    public void setJoursCotises(int joursCotises) {
        this.joursCotises = joursCotises;
    }

    public Date getDateSalaire() {
        return dateSalaire;
    }

    public void setDateSalaire(Date dateSalaire) {
        this.dateSalaire = dateSalaire;
    }
}
