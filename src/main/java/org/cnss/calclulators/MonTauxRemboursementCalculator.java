package org.cnss.calclulators;

import org.cnss.Dao.DocumentsDAO;
import org.cnss.Ennum.Etat;
import org.cnss.model.DossierRembouresement;

public class MonTauxRemboursementCalculator implements TauxRemboursementCalculator {
    private DocumentsDAO documentsDAO = new DocumentsDAO();

    @Override
    public double calculerTauxRemboursement(DossierRembouresement dossier) {
        if (!Etat.VALIDE.equals(dossier.getEtat())) {
            return 0.0;
        }

        return documentsDAO.getDocumentPardossier(dossier)
                .stream()
                .mapToDouble(document -> {
                    double montantDocument = document.getMontant();
                    double tauxDocument = document.getTaux() / 100.0;
                    return montantDocument * tauxDocument;
                })
                .sum();
    }
}
