package org.cnss.model;

import java.util.Date;

public class Salaires {
    private int id;
    private Employé employé;
    private int Salaire ;
  private  Société societe;
  private int nombreJoursTravailles;
  private  int joursCotises;
  private java.sql.Date dateSalaire;

    public Salaires(int id, Employé employé, int salaire, Société societe, int joursCotises, java.sql.Date dateSalaire) {
        this.id = id;
        this.employé = employé;
        this.Salaire = salaire;
        this.societe = societe;
        this.nombreJoursTravailles = 26;
        this.joursCotises = joursCotises;
        this.dateSalaire = dateSalaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

        public Employé getemployé() {
        return employé;
    }

    public void setemployé(Employé employé) {
        this.employé = employé;
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


    public int getJoursCotises() {
        return joursCotises;
    }

    public void setJoursCotises(int joursCotises) {
        this.joursCotises = joursCotises;
    }

    public Date getDateSalaire() {
        return dateSalaire;
    }
    public void setDateSalaire(java.sql.Date dateSalaire){
        this.dateSalaire=dateSalaire;
    }

}
