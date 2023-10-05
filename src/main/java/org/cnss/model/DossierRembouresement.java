package org.cnss.model;

import org.cnss.Ennum.Etat;

import java.util.List;

public class DossierRembouresement {

    private int code;
    private Etat etat;
    private Integer montant;
    private AgentCNSS agentCNSS;
    private Patient patient;
    private List<Document> documents;
    private  List<Medicament> medicaments;

    public DossierRembouresement(int code, Etat etat, Integer montant,AgentCNSS agentCNSS,Patient patient) {
        this.code = code;
        this.etat = etat;
        this.montant = montant;
        this.agentCNSS = agentCNSS;
        this.patient = patient;


    }
    public AgentCNSS getAgentCNSS() {
        return agentCNSS;
    }

    public void setAgentCNSS(AgentCNSS agentCNSS) {
        this.agentCNSS = agentCNSS;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer matricule) {
        this.montant = montant;
    }

}
