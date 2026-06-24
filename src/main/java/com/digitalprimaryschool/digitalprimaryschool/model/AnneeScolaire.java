package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class AnneeScolaire extends BaseModel {
   private String idAnnescolaire;
   private String libelle;
   private String dateDebut;
   private String dateFin;
   private boolean estActive;

   public Collection<Trimestre> trimestre;

   public AnneeScolaire(){
      super();
      this.idAnnescolaire = UUID.randomUUID().toString();
   }

   public String getIdAnnescolaire() { return idAnnescolaire; }
   public void setIdAnnescolaire(String idAnnescolaire) { this.idAnnescolaire = idAnnescolaire; }

   public String getLibelle() { return libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public  String getDateDebut() { return dateDebut; }
   public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

   public String getDateFin() { return dateFin; }
   public void setDateFin(String dateFin) { this.dateFin = dateFin; }

   public boolean isEstActive() { return estActive; }
   public void setEstActive(boolean estActive) { this.estActive = estActive; }

   public Collection<Trimestre> getTrimestre() {
      if (trimestre == null) trimestre = new HashSet<>();
      return trimestre;
   }
}