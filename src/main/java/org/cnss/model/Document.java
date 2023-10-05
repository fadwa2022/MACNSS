package org.cnss.model;

public class Document {
    private DossierRembouresement dossierRembouresement;
    private String Code;
    private float Montant;
    private Categories Category;
    private int taux;
    public int getTaux() {
        return taux;
    }

    public void setTaux(int taux) {
        this.taux = taux;
    }



    public Document(String documentCode, float montant, Categories categorie, DossierRembouresement dossier, int taux) {
        this.Code = documentCode;
        this.Montant = montant;
        this.Category = categorie;
        this.dossierRembouresement = dossier;
        this.taux = taux;
    }


    public DossierRembouresement getDossierRembouresement() {
        return dossierRembouresement;
    }

    public void setDossierRembouresement(DossierRembouresement dossierRembouresement) {
        this.dossierRembouresement = dossierRembouresement;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public float getMontant() {
        return Montant;
    }

    public void setMontant(float montant) {
        Montant = montant;
    }

    public Categories getCategory() {
        return Category;
    }

    public void setCategory(Categories category) {
        Category = category;
    }
}
