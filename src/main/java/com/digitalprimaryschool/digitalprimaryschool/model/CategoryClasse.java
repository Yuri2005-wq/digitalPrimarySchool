package com.digitalprimaryschool.digitalprimaryschool.model;

public enum CategoryClasse {
    MATERNELLE("Maternelle"),
    PRIMAIRE("Primaire");

    private final String libelle;

    CategoryClasse(String libelle) {
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
