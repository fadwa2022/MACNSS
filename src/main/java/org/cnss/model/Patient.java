package org.cnss.model;

import java.util.List;

public class Patient {

    private String nom;
    private String prenom;
    private Integer matricule;
    private List<DossierRembouresement> dossierRembouresement;

    public Patient(String nom, String prenom, Integer matricule ) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;

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

    public Integer getMatricule() {
        return matricule;
    }

    public void setMatricule(Integer matricule) {
        this.matricule = matricule;
    }
    public List<DossierRembouresement> getDossierRembouresement() {
        return dossierRembouresement;
    }

    public void setDossierRembouresement(List<DossierRembouresement> dossierRembouresement) {
        this.dossierRembouresement = dossierRembouresement;
    }
}
