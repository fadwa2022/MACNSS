package org.cnss.model;

public class CompteEmployer {
    private   Employé employé;
    private   Société société;

    public CompteEmployer(Employé employé, Société société) {
        this.employé = employé;
        this.société = société;
    }

    public Employé getEmployé() {
        return employé;
    }

    public void setEmployé(Employé employé) {
        this.employé = employé;
    }

    public Société getSociété() {
        return société;
    }

    public void setSociété(Société société) {
        this.société = société;
    }
}
