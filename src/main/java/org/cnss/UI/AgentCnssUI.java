package org.cnss.UI;

import org.cnss.Dao.PatientDAO;
import org.cnss.Exceptions.CustomDAOException;
import org.cnss.model.AgentCNSS;
import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.model.Patient;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AgentCnssUI {
    private static String agentConnecteEmail;
    static Scanner scanner = new Scanner(System.in);
    static  AgentCNSSDAO agentDAO = new AgentCNSSDAO();
    static PatientDAO patientDAO = new PatientDAO();
    public static void Authentified()  {
        try {
            System.out.print("Entrez votre email : ");
            String enteredEmail = scanner.nextLine();
            System.out.print("Entrez votre mot de passe : ");
            String enteredPassword = scanner.nextLine();
            System.out.print("Entrez Le Code De verification : ");
            int Code_Verification = scanner.nextInt();
            scanner.nextLine();

            AgentCNSS authenticatedAgent = agentDAO.Authentified(enteredEmail, enteredPassword, Code_Verification);
            if (authenticatedAgent != null) {
                agentConnecteEmail = enteredEmail;
                agentMenu();
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for Code de Verification.");
            scanner.nextLine();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (CustomDAOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void agentMenu() {
        int agentChoice;
            System.out.println("\nAgent CNSS Menu:");
            System.out.println("1. Patient Management");
            System.out.println("2. Dossier Management");
            System.out.println("3. Document Management");

            System.out.println("4. Quit (Log Out)");
            System.out.print("Enter your choice: ");
            agentChoice = scanner.nextInt();
            scanner.nextLine();

            switch (agentChoice) {
                case 1:
                    patientManagementMenu();
                    break;
                case 2:
                    if (agentDAO != null) {
                        DossierUI.AfficherMenuDossiers(scanner, agentDAO);
                    } else {
                        System.out.println("Veuillez vous connecter en tant qu'agent CNSS.");
                    }
                    break;
                case 3:
                   DocumentUI.ajouterDocument();
                    break;
                case 4:
                    System.out.println("Logged out as Agent CNSS.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

    }
    public static String getAgentConnecteEmail() {
        return agentConnecteEmail;
    }
    public static void patientManagementMenu() {
        int patientManagementChoice;
        while (true) {
            System.out.println("\nPatient Management Menu:");
            System.out.println("1. Add a new Patient");
            System.out.println("2. Update Patient information");
            System.out.println("3. Delete Patient");
            System.out.println("4. View All Patients");
            System.out.println("5. Return");
            System.out.print("Enter your choice: ");
            patientManagementChoice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (patientManagementChoice) {
                    case 1:
                        AddnewPatient();
                        break;
                    case 2:
                        UpdatePatientinformation();
                        break;
                    case 3:
                        DeletePatient();
                        break;
                    case 4:
                        ViewAllPatients();
                        break;
                    case 5:
                        agentMenu();
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
    public  static void AddnewPatient()throws Exception {
        System.out.print("Enter Patient Name: ");
        String patientName = scanner.nextLine();
        System.out.print("Enter Patient Surname: ");
        String patientSurname = scanner.nextLine();

        Patient addedPatient = patientDAO.ajouterPatient(patientName, patientSurname);

        if (addedPatient != null) {
            System.out.println("Patient added successfully: " + addedPatient.getNom());
        } else {
            throw new Exception("Failed to add Patient.");
        }
 }
    public  static void UpdatePatientinformation()throws Exception {
        System.out.print("Enter Patient Matricule to update: ");
        int patientMatriculeToUpdate = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Patient Name: ");
        String newPatientName = scanner.nextLine();
        System.out.print("Enter new Patient Surname: ");
        String newPatientSurname = scanner.nextLine();

        boolean updated = patientDAO.updatePatient(patientMatriculeToUpdate, newPatientName, newPatientSurname);

        if (!updated) {
            throw new Exception("Failed to update Patient.");
        }
    }

    public  static void DeletePatient()throws Exception {
        System.out.print("Enter Patient Matricule to delete: ");
        int patientMatriculeToDelete = scanner.nextInt();

        boolean deleted = patientDAO.deletePatient(patientMatriculeToDelete);

        if (!deleted) {
            throw new Exception("Failed to delete Patient.");
        }
    }
    public  static void ViewAllPatients(){
        ArrayList<Patient> patients = patientDAO.afficherTousLespatients();
        for (Patient patient : patients) {
            System.out.println("Matricule: " + patient.getMatricule() + ", Name: " + patient.getNom() + ", Surname: " + patient.getPrenom());
        }
    }


}
