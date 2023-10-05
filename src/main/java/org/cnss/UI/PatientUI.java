package org.cnss.UI;

import org.cnss.Dao.DossierRembouresementDAO;
import org.cnss.Dao.PatientDAO;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PatientUI {
    static Scanner scanner = new Scanner(System.in);

    static PatientDAO patientDAO = new PatientDAO();
    static DossierRembouresementDAO dossierRembouresementDAO=new DossierRembouresementDAO();

    public static void ajouterPatient() {
        System.out.println("Saisir le nom du patient :");
        String nomPatient = scanner.nextLine();

        System.out.println("Saisir le prénom du patient :");
        String prenomPatient = scanner.nextLine();

        Patient patientAjoute = patientDAO.ajouterPatient(nomPatient, prenomPatient);

        if (patientAjoute != null) {
            System.out.println("Le patient a été ajouté avec succès. Nom : " + patientAjoute.getNom());
        } else {
            System.out.println("Le patient n'a pas été ajouté.");
        }

    }
    public  static void  AfficherDossierPatient(){
        List<DossierRembouresement> dossierRembouresements=null;
        System.out.println("Choisir le patient:");
        ArrayList<Patient> patients = patientDAO.afficherTousLespatients();
        for (Patient patient : patients) {
            System.out.println("Code: " + patient.getMatricule() + ", Nom: " + patient.getNom() + ", Prénom: " + patient.getPrenom());
        }

        System.out.print("Entrez le code du patient que vous souhaitez afficher ces dossiers: ");
        int codePatient = scanner.nextInt();
        scanner.nextLine();
        Patient patient =patientDAO.getPatientParMatricule(codePatient);
        if (patient != null) {
            dossierRembouresements = dossierRembouresementDAO.getDossierParPatient(patient);
           if (dossierRembouresements!=null && !dossierRembouresements.isEmpty()){
               for (DossierRembouresement dossier : dossierRembouresements) {
                   System.out.println(dossier.getCode() + "\t" + dossier.getEtat() + "\t" + dossier.getMontant() + "\t" + dossier.getAgentCNSS().getEmail()+ "\t" + dossier.getPatient().getNom() + " " + dossier.getPatient().getPrenom());
               }
           }else {
               System.out.println("Ce patient na aucun dossier");
           }

        } else {
            System.out.println("Patient non trouvé avec le code saisi.");
        }

      }
}
