package com.digitalprimaryschool.digitalprimaryschool.model;

public enum Role {
    DIRECTEUR("Directeur"),
    PROMOTEUR("Promoteur"),
    ENSEIGNANT("Enseignant"),
    ECONOME("Économe / Secrétaire");

    private final String libelle;

    Role(String libelle) {
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