package org.cnss.UI;

import org.cnss.Dao.CategorieDAO;
import org.cnss.Dao.DocumentsDAO;
import org.cnss.Dao.DossierRembouresementDAO;
import org.cnss.Dao.MedicamentDAO;
import org.cnss.model.Categories;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Medicament;

import java.util.List;
import java.util.Scanner;

public class DocumentUI {
    private static DocumentsDAO documentsDAO = new DocumentsDAO();
    private  static CategorieDAO categorieDAO =new CategorieDAO();
    private static MedicamentDAO medicamentDAO=new MedicamentDAO();
    private static DossierRembouresementDAO dossierRembouresementDAO =new DossierRembouresementDAO();
    public static void ajouterDocument() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Saisir le code du document:");
            String codeDocument = scanner.nextLine();

            DossierUI.AfficherTousLesDossiers();
            List<DossierRembouresement> dossiers = dossierRembouresementDAO.getAllDossier();

            if (dossiers.isEmpty()) {
                System.out.println("Aucun dossier trouvé. Retour au menu principal.");
                return;
            }
            System.out.println("Saisir le code du dossier:");
            int codeDossier = Integer.parseInt(scanner.nextLine());
            DossierRembouresement dossier = DossierUI.recherchedossierparcode(codeDossier);

            System.out.println("Choisir la catégorie du document:");
            CategoriesUI.afficherListCategories();
            int codeCategorie = Integer.parseInt(scanner.nextLine());
            Categories categorieDocument = categorieDAO.getCategoriesParcode(codeCategorie);

            if (categorieDocument != null) {
                if ("MEDICAMENT".equals(categorieDocument.getNom())) {
                    System.out.println("Saisir le nom du médicament:");
                    String nomMedicament = scanner.nextLine();
                    MedicamentUI.afficherListMedicamentRechercher(nomMedicament);
                    System.out.println("Choisir le médicament:");
                    int codeMedicament = Integer.parseInt(scanner.nextLine());
                    Medicament medicament = medicamentDAO.rechercherMedicamentParCode(codeMedicament);

                    if (medicament != null) {
                        int prix = medicament.getPrix();
                        int tauxRemboursement = medicament.getTaux_remboursement();
                        Document document = new Document(codeDocument, prix, categorieDocument, dossier, tauxRemboursement);
                        documentsDAO.ajouterDocument(document);
                        System.out.println("Document ajouté avec succès.");
                    } else {
                        System.out.println("Médicament invalide sélectionné.");
                    }
                } else {
                    int tauxRemboursement = categorieDocument.getTaux_de_rembourssement();
                    System.out.println("Saisir le prix du document:");
                    int prix = Integer.parseInt(scanner.nextLine());
                    Document document = new Document(codeDocument, prix, categorieDocument, dossier, tauxRemboursement);
                    documentsDAO.ajouterDocument(document);
                    System.out.println("Document ajouté avec succès.");
                }
            } else {
                System.out.println("Catégorie invalide sélectionnée.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : Veuillez saisir un nombre valide.");
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}
