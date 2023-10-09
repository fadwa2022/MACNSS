package org.cnss.UI;
import org.cnss.Dao.EmployéDAO;
import org.cnss.Dao.SalairesDAO;
import org.cnss.model.Employé;
import org.cnss.model.Retraités;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;


public class RetraiteUI {
    static EmployéDAO employéDAO =new EmployéDAO();
        static SalairesDAO salairesDAO =new SalairesDAO();
    static Scanner scanner = new Scanner(System.in);

    public static void MenuRetraite() throws ParseException, SQLException {
        int agentChoice;
        System.out.println("\nRetraite  Menu:");
        System.out.println("1. Zone Des Sociétés");
        System.out.println("2. Zone Des Employer");
        System.out.println("3. Salaire de Retraite");
        System.out.println("4. Quit (Log Out)");
        System.out.print("Enter your choice: ");
        agentChoice = scanner.nextInt();
        scanner.nextLine();

        switch (agentChoice) {
            case 1:
                SocieteUI.MenuSociete();
                break;
            case 2:
                SocieteUI.authentifierSociete();
                break;
            case 3:
                calculersalairederetraite();
                break;
            case 4:
                System.out.println("Logged out as Agent CNSS.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

    }

    public static void calculersalairederetraite() throws SQLException {
        System.out.println("\nSaisir le matricule de l'employé:");
        System.out.print("Saisir Matricule : ");
        String matricule = scanner.nextLine();
        Employé employé = employéDAO.getEmployéByMatricule(matricule);

        if (employé != null) {
            Retraités retraités = salairesDAO.calculderetraite(employé);
            System.out.println("Le salaire de retraite est : " + retraités.getSalaireDeRetraite());
        } else {
            System.out.println("Employé non trouvé.");
        }
    }

}
