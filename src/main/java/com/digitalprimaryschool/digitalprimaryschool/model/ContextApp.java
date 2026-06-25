package com.digitalprimaryschool.digitalprimaryschool.model;

public class ContextApp {
    private static ContextApp instance;
    private int idEcoleConnectee;

    private ContextApp() {
        // Par défaut, on initialise avec l'ID 1 pour que tout fonctionne
        // immédiatement sans écran de connexion obligatoire pendant le développement.
        this.idEcoleConnectee = 1;
    }

    public static synchronized ContextApp getInstance() {
        if (instance == null) {
            instance = new ContextApp();
        }
        return instance;
    }

    public int getIdEcoleConnectee() {
        return idEcoleConnectee;
    }

    public void setIdEcoleConnectee(int idEcoleConnectee) {
        this.idEcoleConnectee = idEcoleConnectee;
    }
}