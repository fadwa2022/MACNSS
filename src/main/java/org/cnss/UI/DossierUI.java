package org.cnss.UI;

import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.Dao.DossierRembouresementDAO;
import org.cnss.Dao.PatientDAO;
import org.cnss.Ennum.Etat;
import org.cnss.calclulators.MonTauxRemboursementCalculator;
import org.cnss.model.AgentCNSS;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Patient;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DossierUI {
    static DossierRembouresementDAO dossierRembouresementDAO = new DossierRembouresementDAO();
    PatientDAO patientDAO = new PatientDAO();
    AgentCNSSDAO agentCNSS=new AgentCNSSDAO();
    static MonTauxRemboursementCalculator monTauxRemboursementCalculator=new MonTauxRemboursementCalculator();

    static Scanner scanner = new Scanner(System.in);
    public static void AfficherMenuDossiers(Scanner scanner, AgentCNSSDAO agentCNSS) {
        int choix;

        do {
            System.out.println("\nMenu Dossiers de Remboursement:");
            System.out.println("1. Ajouter un Dossier");
            System.out.println("2. Mettre à jour Etat Dossier ");
            System.out.println("3. Supprimer un Dossier");
            System.out.println("4. Afficher Mantant du remboursement du dossier ");
            System.out.println("5. Quitter le Menu Dossiers");
            System.out.print("Entrez votre choix: ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    try {
                        AjouterDossier(agentCNSS);
                    } catch (InputMismatchException e) {
                        System.out.println("Saisie invalide. Assurez-vous d'entrer un nombre.");
                    }
                    break;
                case 2:
                    MettreAJourEtatDossier();
                    break;
                case 3:
                    SupprimerDossier();
                    break;
                case 4:
                    Mantantrembourcementdossier();
                    break;
                case 5:
                    System.out.println("Retour au Menu Principal.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 5);
    }
    public static void AjouterDossier(AgentCNSSDAO agentCNSSDAO) {
        System.out.println("Ajouter un Dossier:");
        System.out.println("Choisir le patient pour le dossier:");

        try {
            int codePatient = scanner.nextInt();
            scanner.nextLine();
            PatientDAO patientDAO = new PatientDAO();
            Patient patientTrouve = patientDAO.getPatientParMatricule(codePatient);

            if (patientTrouve != null) {
                AgentCNSS agentCNSS = agentCNSSDAO.getAgentParEmail(AgentCnssUI.getAgentConnecteEmail());
                DossierRembouresement dossier = dossierRembouresementDAO.ajouterDossierRemboursement(agentCNSS, patientTrouve);
                if (dossier != null) {
                    System.out.println("Dossier ajouté avec succès.");
                } else {
                    System.out.println("Échec de l'ajout du dossier.");
                }
            } else {
                System.out.println("Patient non trouvé avec le code saisi.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Saisie invalide. Assurez-vous d'entrer un nombre.");
        }
    }

    public static void SupprimerDossier() {
        System.out.println("Supprimer un Dossier:");
        System.out.print("Entrez le code du dossier que vous souhaitez supprimer: ");
        try {
            int codeDossier = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            DossierRembouresement dossier = recherchedossierparcode(codeDossier);
            if (dossier != null) {
                boolean supprime = dossierRembouresementDAO.deleteDossierRemboursement(codeDossier);
                if (supprime) {
                    System.out.println("Dossier supprimé avec succès.");
                } else {
                    System.out.println("Échec de la suppression du dossier.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Code de dossier invalide. Assurez-vous d'entrer un nombre entier.");
        }
    }

    public static DossierRembouresement recherchedossierparcode(int code) {
        DossierRembouresement dossierRembouresement = dossierRembouresementDAO.getDossierParCode(code);
        if (dossierRembouresement == null) {
            System.out.println("Aucun dossier trouvé avec le code saisi.");
        }
        return dossierRembouresement;
    }
    public static void AfficherTousLesDossiers() {
        List<DossierRembouresement> dossiers = dossierRembouresementDAO.getAllDossier();

        if (dossiers.isEmpty()) {
            System.out.println("Aucun dossier trouvé.");
        } else {
            System.out.println("Liste de tous les dossiers:");
            for (DossierRembouresement dossier : dossiers) {
                System.out.println("Code Dossier: " + dossier.getCode());
                System.out.println("Patient: " + dossier.getPatient().getNom() + " " + dossier.getPatient().getPrenom());
                System.out.println("--------------------------");
            }
        }
    }
    public static  void Mantantrembourcementdossier(){
        System.out.print("Entrez le code du dossier que vous souhaitez : ");
        int codeDossier = scanner.nextInt();
        scanner.nextLine();

        DossierRembouresement dossier = recherchedossierparcode(codeDossier);
        MonTauxRemboursementCalculator monTauxRemboursementCalculator = new MonTauxRemboursementCalculator();
        double TauxRemboursementCalculator = monTauxRemboursementCalculator.calculerTauxRemboursement(dossier);
        System.out.print("le taux de rembourcement est: "+TauxRemboursementCalculator +" DH");

    }
    public static void MettreAJourEtatDossier() {
        try {
            System.out.println("Mettre à jour l'état du dossier :");
            System.out.print("Entrez le code du dossier que vous souhaitez mettre à jour : ");
            int codeDossier = scanner.nextInt();
            scanner.nextLine();

            DossierRembouresement dossier = recherchedossierparcode(codeDossier);

            if (dossier != null) {
                System.out.println("États possibles : ");
                for (Etat etat : Etat.values()) {
                    System.out.println(etat.toString());
                }

                System.out.print("Choisissez le nouvel état du dossier : ");
                String nouvelEtatString = scanner.nextLine();
                try {
                    Etat nouvelEtat = Etat.valueOf(nouvelEtatString);

                    dossier.setEtat(nouvelEtat);
                    dossierRembouresementDAO.updateEtatDossier(dossier);
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide. Assurez-vous d'utiliser un état valide.");
                }
            } else {
                System.out.println("Dossier introuvable.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Code de dossier invalide. Assurez-vous d'entrer un nombre entier.");
        }
    }

}
