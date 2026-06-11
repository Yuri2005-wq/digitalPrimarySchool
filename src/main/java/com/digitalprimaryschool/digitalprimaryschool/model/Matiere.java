package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Matiere {
   private String idMatiere;
   private String libelle;
   private double coefficient;
   private int volumeHoraire;
   private String categorie;
   private Classe classe;
   
   public Notes notes;

   public Matiere(){
      this.idMatiere  = UUID.randomUUID().toString();
   }



   public String getIdMatiere() { return idMatiere; }
   public void setIdMatiere(String idMatiere) { this.idMatiere = idMatiere; }

   public String getLibelle() { return libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public double getCoefficient() { return coefficient; }
   public void setCoefficient(double coefficient) { this.coefficient = coefficient; }

   public int getVolumeHoraire() { return volumeHoraire; }
   public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }

   public String getCategorie() { return categorie; }
   public void setCategorie(String categorie) { this.categorie = categorie; }

   public String getIdClasse() { return classe.getIdClasse(); }



   public void getMoyenneClasse() {
      // TODO: implement
   }
   
   public void getCoefficientPondere() {
      // TODO: implement
   }
   
   
   public Notes getNotes() {
      return notes;
   }

   public void setNotes(Notes newNotes) {
      this.notes = newNotes;
   }

}