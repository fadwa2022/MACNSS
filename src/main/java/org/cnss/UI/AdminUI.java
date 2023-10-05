package org.cnss.UI;

import org.cnss.Dao.AdministrateurDAO;
import org.cnss.Dao.AgentCNSSDAO;
import org.cnss.model.Administrateur;
import org.cnss.model.AgentCNSS;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminUI {
    static AdministrateurDAO administrateurDAO = new AdministrateurDAO();
    static AgentCNSSDAO agentDAO = new AgentCNSSDAO();
    static Scanner scanner = new Scanner(System.in);

    public static void Authentication() {
        try {
            System.out.print("Entrez votre email : ");
            String email = scanner.nextLine();
            System.out.print("Entrez votre mot de passe : ");
            String code = scanner.nextLine();
            Administrateur authenticatedAdmin = administrateurDAO.authentifier(email, code);
            if (authenticatedAdmin != null) {
                administratorMenu();
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    public static void administratorMenu() {
        int adminChoice;
        while (true) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. Agent Management");
            System.out.println("2. Quit (Log Out)");
            System.out.print("Enter your choice: ");
            adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    agentManagementMenu();
                    break;
                case 2:
                    System.out.println("Logged out as Administrator.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public static void agentManagementMenu() {
        int agentManagementChoice;
        while (true) {
            System.out.println("\nAgent CNSS Management Menu:");
            System.out.println("1. Add a new Agent");
            System.out.println("2. Update Agent information");
            System.out.println("3. Delete Agent");
            System.out.println("4. View All Agents");
            System.out.println("5. Active Agents");
            System.out.println("6. Quit (Log Out)");
            System.out.print("Enter your choice: ");
            agentManagementChoice = scanner.nextInt();
            scanner.nextLine();

            switch (agentManagementChoice) {
                case 1:
                    addNewAgent();
                    break;
                case 2:
                    updateAgentInformation();
                    break;
                case 3:
                    deleteAgent();
                    break;
                case 4:
                    viewAllAgents();
                    break;
                case 5:
                    activeAgents();
                    break;
                case 6:
                    System.out.println("Logged out as Agent CNSS.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void addNewAgent() {
        try {
            System.out.print("Enter Agent Name: ");
            String agentName = scanner.nextLine();
            System.out.print("Enter Agent Email: ");
            String agentEmail = scanner.nextLine();
            System.out.print("Enter Agent Code: ");
            String agentCode = scanner.nextLine();

            String addedAgent = agentDAO.ajoutAgentCnss(agentName, agentEmail, agentCode);
            if (addedAgent != null) {
                System.out.println("Agent added successfully: " + addedAgent);
            } else {
                System.out.println("Failed to add Agent.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    private static void updateAgentInformation() {
        try {
            System.out.print("Enter Agent Email to update: ");
            String agentToUpdateEmail = scanner.nextLine();
            System.out.print("Enter new Agent Name: ");
            String newAgentName = scanner.nextLine();
            System.out.print("Enter new Agent Email: ");
            String newAgentEmail = scanner.nextLine();

            boolean updated = agentDAO.updateAgentCnss(agentToUpdateEmail, newAgentName, newAgentEmail);

            if (updated) {
                System.out.println("Agent updated successfully.");
            } else {
                System.out.println("Failed to update Agent.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    private static void deleteAgent() {
        try {
            System.out.print("Enter Agent Email to delete: ");
            String agentToDeleteEmail = scanner.nextLine();

            boolean deleted = agentDAO.deleteAgentCnss(agentToDeleteEmail);

            if (deleted) {
                System.out.println("Agent deleted successfully.");
            } else {
                System.out.println("Failed to delete Agent.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    private static void viewAllAgents() {
        try {
            ArrayList<AgentCNSS> agents = agentDAO.afficherTousLesAgents();
            for (AgentCNSS agent : agents) {
                System.out.println("Name: " + agent.getNom() + ", Email: " + agent.getEmail());
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    private static void activeAgents() {
        try {
            ArrayList<AgentCNSS> agentList = agentDAO.afficherTousLesAgentsdesactiver();
            for (AgentCNSS agent : agentList) {
                System.out.println("Name: " + agent.getNom() + ", Email: " + agent.getEmail());
            }
            System.out.print("Enter Agent Email to activate: ");
            String enteredEmail = scanner.nextLine();
            agentDAO.activateAgent(enteredEmail);
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }


}
