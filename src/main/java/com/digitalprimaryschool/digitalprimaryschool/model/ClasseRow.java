package com.digitalprimaryschool.digitalprimaryschool.model;

import javafx.beans.property.*;

public class ClasseRow {
    private final StringProperty idClasse;  // Gardé pour usage interne
    private final StringProperty nom;
    private final StringProperty niveau;
    private final StringProperty section;
    private final StringProperty categorie;
    private final IntegerProperty capaciteMax;
    private final IntegerProperty effectif;

    public ClasseRow(String idClasse, String nom, String niveau, String section,
                     String categorie, int capaciteMax, int effectif) {
        this.idClasse = new SimpleStringProperty(idClasse);
        this.nom = new SimpleStringProperty(nom);
        this.niveau = new SimpleStringProperty(niveau);
        this.section = new SimpleStringProperty(section);
        this.categorie = new SimpleStringProperty(categorie);
        this.capaciteMax = new SimpleIntegerProperty(capaciteMax);
        this.effectif = new SimpleIntegerProperty(effectif);
    }

    // Getter pour usage interne (pas affiché dans le tableau)
    public String getIdClasse() { return idClasse.get(); }
    public StringProperty idClasseProperty() { return idClasse; }
    public void setIdClasse(String idClasse) { this.idClasse.set(idClasse); }

    // Getter pour l'affichage
    public String getNom() { return nom.get(); }
    public StringProperty nomProperty() { return nom; }
    public void setNom(String nom) { this.nom.set(nom); }

    public String getNiveau() { return niveau.get(); }
    public StringProperty niveauProperty() { return niveau; }
    public void setNiveau(String niveau) { this.niveau.set(niveau); }

    public String getSection() { return section.get(); }
    public StringProperty sectionProperty() { return section; }
    public void setSection(String section) { this.section.set(section); }

    public String getCategorie() { return categorie.get(); }
    public StringProperty categorieProperty() { return categorie; }
    public void setCategorie(String categorie) { this.categorie.set(categorie); }

    public int getCapaciteMax() { return capaciteMax.get(); }
    public IntegerProperty capaciteMaxProperty() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax.set(capaciteMax); }

    public int getEffectif() { return effectif.get(); }
    public IntegerProperty effectifProperty() { return effectif; }
    public void setEffectif(int effectif) { this.effectif.set(effectif); }
}