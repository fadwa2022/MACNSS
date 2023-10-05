package org.cnss.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class AgentCNSS {
         private String nom;
        private String email;
        private String motDePasse;
        private int codeVerification;
        private Timestamp getCodeExpiration;
        private int active;


    public Timestamp getCodeExpiration() {
        return CodeExpiration;
    }

    public void setCodeExpiration(Timestamp codeExpiration) {
        CodeExpiration = codeExpiration;
    }

    private Timestamp CodeExpiration;






    public int getActive() {
        return active;
    }

    public AgentCNSS(String nom, String motDePasse, String email, int codeVerification,Timestamp CodeExpiration, int active ) {
            this.nom = nom;
            this.email = email;
            this.motDePasse = motDePasse;
            this.codeVerification = codeVerification;
            this.CodeExpiration=CodeExpiration;
            this.active=active;
        }

    public void setActive(int active) {
        this.active = active;
    }

    public String getNom() {
        return nom;
    }

          public void setNom(String nom) {
        this.nom = nom;
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

        public int getCodeVerification() {
            return codeVerification;
        }

        public void setCodeVerification(int codeVerification) {
            this.codeVerification = codeVerification;
        }


}


