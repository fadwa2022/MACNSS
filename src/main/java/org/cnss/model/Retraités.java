package org.cnss.model;

public class Retraités {
    private Employé employé;
    private float salaireDeRetraite;

    public Retraités(Employé employé, float salaireDeRetraite) {
        this.employé = employé;
        this.salaireDeRetraite = salaireDeRetraite;
    }

    public Employé getEmployé() {
        return employé;
    }

    public void setEmployé(Employé employé ) {
        this.employé = employé;
    }

    public float getSalaireDeRetraite() {
        return salaireDeRetraite;
    }

    public void setSalaireDeRetraite(float salaireDeRetraite) {
        this.salaireDeRetraite = salaireDeRetraite;
    }
}
