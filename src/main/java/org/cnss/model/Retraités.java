package org.cnss.model;

public class Retraités {
    private String matricule;
    private int salaireDeRetraite;

    public Retraités(String matricule, int salaireDeRetraite) {
        this.matricule = matricule;
        this.salaireDeRetraite = salaireDeRetraite;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getSalaireDeRetraite() {
        return salaireDeRetraite;
    }

    public void setSalaireDeRetraite(int salaireDeRetraite) {
        this.salaireDeRetraite = salaireDeRetraite;
    }
}
