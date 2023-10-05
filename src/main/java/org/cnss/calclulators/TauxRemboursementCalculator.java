package org.cnss.calclulators;

import org.cnss.model.DossierRembouresement;
import org.cnss.Ennum.Etat;
public interface TauxRemboursementCalculator {
    double calculerTauxRemboursement(DossierRembouresement dossier);
}


