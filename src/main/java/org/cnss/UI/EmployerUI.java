package org.cnss.UI;

import org.cnss.Dao.*;
import org.cnss.Ennum.StatutDeTravail;
import org.cnss.model.*;

import org.cnss.Stockage.SocieteManager;


import java.text.*;
import java.util.*;


public class EmployerUI {
    static Scanner scanner = new Scanner(System.in);
    static EmployéDAO employéDAO =new EmployéDAO();
    static SalairesDAO salairesDAO = new SalairesDAO();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SocieteManager societeManager = SocieteManager.getInstance();
    static HashMap<Integer, Société> sociétésEnregistrées = societeManager.getSociétésEnregistrées();
    static Société société = sociétésEnregistrées.get(1);
    public static void menuEmployé() throws ParseException {
        int Employerchoix;
        System.out.println("\nEmployer Menu:");
        System.out.println("1. Déclarer des nouveaux employés");
        System.out.println("2. Modifier le salaire Actuel d'un employé");
        System.out.println("3. Déclarer les jours d'absence d'un employé");
        System.out.println("4. Afficher tous vos employés");
        System.out.println("5. Supprimer un employé");

        System.out.println("6. Quit (Log Out)");
        System.out.print("Enter your choice: ");
        Employerchoix = scanner.nextInt();
        scanner.nextLine();

        switch (Employerchoix) {
            case 1:
                ajouterEmployes();
                break;
            case 2:
                modifierSalaireEmployé();
                break;
            case 3:
                Déclarerjoursabsencedemployé();
            break;
            case 4:
                afficherEmployésDeLaSociété();
                break;
            case 5:
                mettreEnChômage();
                break;
            case 6:
                System.out.println("Logged out as Agent CNSS.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

    }
    public static void ajouterEmployes() throws ParseException {
        System.out.println("\nDéclarer des employés");
        System.out.println("\nSaisir le nombre d'employés que vous souhaitez déclarer :");
        int nombreEmployesDeclarer = scanner.nextInt();
        scanner.nextLine();

        Employé[] employes = new Employé[nombreEmployesDeclarer];

        for (int i = 0; i < nombreEmployesDeclarer; i++) {
            System.out.println("\nSaisir les détails de l'employé " + (i + 1) + ":");
            System.out.print("Nom : ");
            String nom = scanner.nextLine();
            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Salaire actuel : ");
            int salaireActuel = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Date de naissance (format YYYY-MM-DD) : ");
            String date = scanner.nextLine();
            Date dateNaissance = dateFormat.parse(date);

            System.out.print("Matricule : ");
            String matricule =scanner.nextLine();

            StatutDeTravail statut = StatutDeTravail.EMPLOYE;


            Employé employe = new Employé(matricule,salaireActuel,société,statut,dateNaissance,nom,prenom);
            employes[i] = employe;
        }
        List<Employé> employésAjoutés = employéDAO.ajouterEmployés(List.of(employes));

        if (!employésAjoutés.isEmpty()) {
            System.out.println("Les employés ont été ajoutés avec succès.");
        } else {
            System.out.println("Erreur : Les employés n'ont pas pu être ajoutés.");
            menuEmployé();
        }
    }
    public static void modifierSalaireEmployé() {
        System.out.println("\nModifier Salaire d'employé");

        while (true) {
            System.out.print("Saisir Matricule : ");
            String matricule = scanner.nextLine();
            Employé employé = employéDAO.getEmployéByMatricule(matricule);

            if (employé != null) {
                System.out.print("Saisir Nouveau Salaire : ");
                int nouveauSalaire = scanner.nextInt();
                scanner.nextLine(); // Lire la nouvelle ligne en mémoire tampon après nextInt()

                employé.setSalaireActuel(nouveauSalaire);
                employéDAO.updateEmployé(employé);
                System.out.println("Salaire de l'employé " + matricule + " mis à jour avec succès.");
                break; // Sortir de la boucle car l'employé a été trouvé et mis à jour
            } else {
                System.out.println("L'employé avec le matricule " + matricule + " n'existe pas.");
                System.out.print("Voulez-vous essayer un autre matricule ? (O/N) : ");
                String choix = scanner.nextLine();
                if (!choix.equalsIgnoreCase("O")) {
                    break;
                }
            }
        }
    }
    public static void afficherEmployésDeLaSociété() {

        List<Employé> employés = employéDAO.getAllEmployés(société);

        if (employés.isEmpty()) {
            System.out.println("Aucun employé trouvé pour la société " + société.getNomSociété());
        } else {
            System.out.println("Employés de la société " + société.getNuméroSociété() + ":");
            for (Employé employé : employés) {
                System.out.println("Matricule : " + employé.getMatricule());
                System.out.println("Nom : " + employé.getNom());
                System.out.println("Prénom : " + employé.getPrenom());
                System.out.println("Salaire actuel : " + employé.getSalaireActuel());
                System.out.println("----------------------------------");
            }
        }
    }
    public static int estEmployéDansSociété(Employé employé, Société société) {
        Société société1=société;
        Société sociétéActuelle = employé.gatsocieteActuel();
        if (sociétéActuelle != null && société1 != null) {
            if (sociétéActuelle.getNuméroSociété() == société1.getNuméroSociété()) {
                return 1;
            }
        }



        return 0;
    }
    public static void mettreEnChômage() {
        System.out.println("\nSupprimer  employer");

        while (true) {
            System.out.print("Saisir Matricule : ");
            String matricule = scanner.nextLine();
            Employé employé = employéDAO.getEmployéByMatricule(matricule);

            if (employé != null) {

                if (estEmployéDansSociété(employé, société)==1) {

                    employé.setStatutTravail(StatutDeTravail.CHOMAGE);
                    employé.setsocieteActuel(null);
                    employé.setSalaireActuel(0);
                    employéDAO.updateEmployé(employé);
                    System.out.println("L'employé a été mis en chômage avec succès.");
                } else {
                    System.out.println("L'employé n'appartient pas à votre société .");
                }
            } else {
                System.out.println("L'employé avec le matricule " + matricule + " n'existe pas.");
                System.out.print("Voulez-vous essayer un autre matricule ? (O/N) : ");
                String choix = scanner.nextLine();
                if (!choix.equalsIgnoreCase("O")) {
                    break;
                }
            }
        }

 }
    public static void Déclarerjoursabsencedemployé()  {
        System.out.print("Saisir Matricule : ");
        String matricule = scanner.nextLine();
        Employé employé = employéDAO.getEmployéByMatricule(matricule);
        System.out.print("Le mois et l'année de déclaration (format YYYY-MM) : ");
        String dateStr = scanner.nextLine();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        java.util.Date utilDate = null;

        try {
            utilDate = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Format de date incorrect. Utilisez le format YYYY-MM.");
            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        System.out.print("Saisir les jours d'absence de l'employé: ");
        int joursAbsents = scanner.nextInt();
        Salaires salairesMisAJour = salairesDAO.ajouterLesjoursCotises(employé,sqlDate, joursAbsents);

        if (salairesMisAJour != null) {
            System.out.println("Jours cotisés mis à jour avec succès !");
        } else {
            System.out.println("La mise à jour des jours cotisés a échoué.");
        }
    }



}
