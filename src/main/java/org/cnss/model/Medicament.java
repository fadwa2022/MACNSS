package org.cnss.model;

public class Medicament {
    private String code_barre;
    private String nom;
    private int Prix;
    private int Taux_remboursement;

    public Medicament(int codeBarre, String nom, int prix, int tauxRemboursement) {
    }

    public int getTaux_remboursement() {
        return Taux_remboursement;
    }

    public void setTaux_remboursement(int taux_remboursement) {
        Taux_remboursement = taux_remboursement;
    }



    public String getCode_barre() {
        return code_barre;
    }

    public void setCode_barre(String code_barre) {
        this.code_barre = code_barre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
        Prix = prix;
    }


}
