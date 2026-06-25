package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class Ecole extends BaseModel {
    private String idEcole; // UUID sous forme de String
    private String nomEcole;
    private String adresseEcole;
    private String telephoneEcole;
    private String emailEcole;
    private String deviseEcole;

    public Ecole() {
        super();
        this.idEcole = UUID.randomUUID().toString(); // Génération de l'UUID
    }

    // --- Getters & Setters ---
    public String getIdEcole() { return idEcole; }
    public void setIdEcole(String idEcole) { this.idEcole = idEcole; }

    public String getNomEcole() { return nomEcole; }
    public void setNomEcole(String nomEcole) { this.nomEcole = nomEcole; }

    public String getAdresseEcole() { return adresseEcole; }
    public void setAdresseEcole(String adresseEcole) { this.adresseEcole = adresseEcole; }

    public String getTelephoneEcole() { return telephoneEcole; }
    public void setTelephoneEcole(String telephoneEcole) { this.telephoneEcole = telephoneEcole; }

    public String getEmailEcole() { return emailEcole; }
    public void setEmailEcole(String emailEcole) { this.emailEcole = emailEcole; }

    public String getDeviseEcole() { return deviseEcole; }
    public void setDeviseEcole(String deviseEcole) { this.deviseEcole = deviseEcole; }
}