package org.cnss.UI;

import org.cnss.Dao.CategorieDAO;
import org.cnss.model.Categories;

import java.util.List;

public class CategoriesUI {
    static CategorieDAO categorieDAO = new CategorieDAO();

    public  static  void afficherListCategories() {
        List<Categories> listeDesCategories= categorieDAO.afficherTousLesCategories();
        System.out.println("\nListe des Categories  :");
        System.out.println("***************************************");
        System.out.printf("%-10s %-30s %-20s \n", "Code Categories", "Nom Categories", "Taux De Rembourssement");
        System.out.println("***************************************");

        for (Categories Categorie : listeDesCategories) {
            System.out.printf("%-10s %-30s %d%% \n", Categorie.getCode_Categories(), Categorie.getNom(), Categorie.getTaux_de_rembourssement());
        }
        System.out.println("\n\n");
    }

}
