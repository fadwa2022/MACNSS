package org.cnss.Stockage;

import org.cnss.model.Société;

import java.util.HashMap;

public class SocieteManager {
    private static SocieteManager instance;
    private static HashMap<Integer, Société> sociétésEnregistrées = new HashMap<>();

    private SocieteManager() {}

    public static SocieteManager getInstance() {
        if (instance == null) {
            instance = new SocieteManager();
        }
        return instance;
    }

    public static void ajouterSociete(int numéroSociété, Société société) {
        sociétésEnregistrées.put(numéroSociété, société);
    }


    public HashMap<Integer, Société> getSociétésEnregistrées() {
        return sociétésEnregistrées;
    }
}

