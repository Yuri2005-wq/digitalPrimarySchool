package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class Sequence extends BaseModel {
   private String idSequence;
   private String libelle;
   private boolean estSaisie;
   private String idTrimestre;

   public Sequence(){
      super();
      this.idSequence = UUID.randomUUID().toString();
   }

   public String getIdSequence() { return idSequence; }
   public void setIdSequence(String idSequence) { this.idSequence = idSequence; }

   public String getLibelle() { return libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public boolean isEstSaisie() { return estSaisie; }
   public void setEstSaisie(boolean estSaisie) { this.estSaisie = estSaisie; }

   public String getIdTrimestre() { return idTrimestre; }
   public void setIdTrimestre(String idTrimestre) { this.idTrimestre = idTrimestre; }
}