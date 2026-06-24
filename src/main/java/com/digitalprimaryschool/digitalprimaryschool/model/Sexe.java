package com.digitalprimaryschool.digitalprimaryschool.model;

// Sexe.java
public enum Sexe {
    MASCULIN("Masculin"),
    FEMININ("Féminin");

    private final String libelle;

    Sexe(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
