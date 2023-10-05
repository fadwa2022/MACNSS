package org.cnss.UI;
import java.text.ParseException;
import java.util.Scanner;

public class RetraiteUI {
    static Scanner scanner = new Scanner(System.in);

    public static void MenuRetraite() throws ParseException {
        int agentChoice;
        System.out.println("\nRetraite  Menu:");
        System.out.println("1. Zone Des Sociétés");
        System.out.println("2. Zone Des Employer");
        System.out.println("3. Quit (Log Out)");
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
                System.out.println("Logged out as Agent CNSS.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

    }
}
