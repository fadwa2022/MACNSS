package org.cnss.model;
import java.util.List;

public class Administrateur {
    private String email;
    private String motDePasse;
    private List<AgentCNSS> agentsCNSS;

    public Administrateur(String motDePasse, String email) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<AgentCNSS> getAgentsCNSS() {
        return agentsCNSS;
    }

    public void setAgentsCNSS(List<AgentCNSS> agentsCNSS) {
        this.agentsCNSS = agentsCNSS;
    }
}
