package org.cnss.UI;

import org.cnss.Dao.MedicamentDAO;
import org.cnss.model.Medicament;

import java.util.List;

public class MedicamentUI {
    static MedicamentDAO medicamentDAO =new MedicamentDAO();

    public  static  void afficherListMedicamentRechercher(String nom_medicament) {
        String Nom_medicament = null;

        List<Medicament> listeDesMedicaments= medicamentDAO.rechercherMedicamentParNom(Nom_medicament);;
        System.out.println("\nListe des Medicament  :");
        System.out.println("***************************************");
        System.out.printf("%-10s %-10s %-30s %-20s  \n", "Code","Nom", "Prix", "Taux de remboursement");
        System.out.println("***************************************");

        for (Medicament medicament : listeDesMedicaments) {
            System.out.printf("%-10s %-10s %-30s %-20s \n", medicament.getCode_barre(),medicament.getNom(),medicament.getPrix(),medicament.getTaux_remboursement());
        }
        System.out.println("\n\n");
    }
}
