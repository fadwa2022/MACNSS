package org.cnss.UI;
import org.cnss.Dao.SociétéDAO;
import org.cnss.model.Société;

import java.text.ParseException;
import org.cnss.Stockage.SocieteManager;

import java.util.List;
import java.util.Scanner;

public class SocieteUI {

    static Scanner scanner = new Scanner(System.in);
    static SociétéDAO sociétéDAO =new SociétéDAO();

    public static void MenuSociete(){
        int agentChoice;
        System.out.println("\nSociété  Menu:");
        System.out.println("1. Ajouter Société");
        System.out.println("2. Mettre à jour Société");
        System.out.println("3. Supprimer Société");
        System.out.println("4. Afficher toutes les sociétés");
        System.out.println("5. Quit (Log Out)");
        System.out.print("Enter your choice: ");
        agentChoice = scanner.nextInt();
        scanner.nextLine();

        switch (agentChoice) {
            case 1:
                ajoutersociété();
                break;
            case 2:
                mettreÀJourSociété();
                break;
            case 3:
                supprimerSociété();
                break;
            case 4:
                afficherToutesLesSociétés();
                break;
            case 5:
                System.out.println("Logged out as Agent CNSS.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
    }

}
    public static void authentifierSociete() throws ParseException {
        System.out.print("Email de la société : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        int password = scanner.nextInt();
        scanner.nextLine();

        Société société = sociétéDAO.authentifierSociété(email, password);

        if (société != null) {
            SocieteManager.ajouterSociete(1, société);
            System.out.println("Authentification réussie ! Vous pouvez accéder aux fonctionnalités.");
            EmployerUI.menuEmployé();
        } else {
            System.out.println("Authentification échouée. Veuillez réessayer.");
        }
    }

    public static void ajoutersociété(){
        System.out.println("\nAjouter Une Société");

        System.out.print("Nom de la société: ");
        String nomSociete = scanner.nextLine();

        String email;
        boolean isValidEmail = false;
        do {
            System.out.print("Email de la société: ");
            email = scanner.nextLine();
            isValidEmail = EmailValidation.isEmailValid(email);
            if (!isValidEmail) {
                System.out.println("L'email n'est pas valide. Veuillez réessayer.");
            }
        } while (!isValidEmail);

        System.out.print("Password de la société: ");
        int password = scanner.nextInt();
        int numéroSociété= 0;

        Société nouvelleSociete = new Société(numéroSociété, nomSociete, email,password);

        sociétéDAO.ajouterSociété(nouvelleSociete);
        System.out.println("Société ajoutée avec succès !");
    }
    public static void mettreÀJourSociété() {
        int numéroSociété;
        Société société;

        do {
            System.out.print("Entrez le numéro de société à mettre a jour : ");
            numéroSociété = scanner.nextInt();
            scanner.nextLine();
            société = sociétéDAO.getSociétéById(numéroSociété);

            if (société == null) {
                System.out.println("Aucune société avec ce numéro n'a été trouvée. Veuillez réessayer.");
            }
        } while (société == null);


        System.out.print("Entrez le nouveau nom de la société: ");
        String nouveauNomSociété = scanner.nextLine();

        sociétéDAO.updateSociété(numéroSociété, nouveauNomSociété);
        System.out.println("Société mise à jour avec succès !");
    }
    public static void supprimerSociété() {
        int numéroSociété;
        Société société;

        do {
            System.out.print("Entrez le numéro de société à supprimer : ");
            numéroSociété = scanner.nextInt();
            scanner.nextLine();
            société = sociétéDAO.getSociétéById(numéroSociété);

            if (société == null) {
                System.out.println("Aucune société avec ce numéro n'a été trouvée. Veuillez réessayer.");
            }
        } while (société == null);

        sociétéDAO.deleteSociété(numéroSociété);
        System.out.println("Société supprimée avec succès !");

    }
    public static void afficherToutesLesSociétés() {
        List<Société> sociétés = sociétéDAO.afficherToutesSociétés();

        if (sociétés.isEmpty()) {
            System.out.println("Aucune société trouvée.");
        } else {
            System.out.println("\nListe de toutes les sociétés:");
            for (Société société : sociétés) {
                System.out.println(société.getNuméroSociété()+ "-" +société.getNomSociété() +"-"+ société.getEmail() );
            }
        }
    }
}