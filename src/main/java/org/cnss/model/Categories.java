package org.cnss.model;

public class Categories {
    private int code_Categories ;
    private String nom;
    private int prix;
    private int taux_de_rembourssement;

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }



    public Categories(int code_Categories,String nom,int taux_de_rembourssement){
        this.code_Categories=code_Categories;
        this.nom=nom;
        this.taux_de_rembourssement=taux_de_rembourssement;
    }
    public int getCode_Categories() {
        return code_Categories;
    }

    public void setCode_Categories(int code_Categories) {
        this.code_Categories = code_Categories;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTaux_de_rembourssement() {
        return taux_de_rembourssement;
    }

    public void setTaux_de_rembourssement(int taux_de_rembourssement) {
        this.taux_de_rembourssement = taux_de_rembourssement;
    }

}
