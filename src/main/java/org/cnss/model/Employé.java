package org.cnss.model;

import org.cnss.Ennum.StatutDeTravail;

import java.util.Date;

public class Employé {
    private String matricule;
    private int salaireActuel;
    private Société societeActuel;
    private StatutDeTravail statutTravail;
    private Date dateNaissance;
    private String nom;
    private  String prenom;

    public Employé(String matricule, int salaireActuel, Société societeActuel, StatutDeTravail statutTravail, Date dateNaissance, String nom, String prenom) {
        this.matricule = matricule;
        this.salaireActuel = salaireActuel;
        this.societeActuel = societeActuel;
        this.statutTravail = statutTravail;
        this.dateNaissance = dateNaissance;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getSalaireActuel() {
        return salaireActuel;
    }

    public void setSalaireActuel(int salaireActuel) {
        this.salaireActuel = salaireActuel;
    }

    public Société gatsocieteActuel() {
        return societeActuel;
    }

    public void setsocieteActuel(Société societeActuel) {
        this.societeActuel = societeActuel;
    }

    public StatutDeTravail getStatutTravail() {
        return statutTravail;
    }

    public void setStatutTravail(StatutDeTravail statutTravail) {
        this.statutTravail = statutTravail;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
