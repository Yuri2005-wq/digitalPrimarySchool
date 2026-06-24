package com.digitalprimaryschool.digitalprimaryschool.model;

public enum SectionClass {
    ANGLOPHONE("ANGLOPHONE"),
    FRANCOPHONE("FRANCOPHONE");

    private final String libelle;

    SectionClass(String libelle) {
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
