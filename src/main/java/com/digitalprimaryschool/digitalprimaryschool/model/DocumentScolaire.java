package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class DocumentScolaire extends BaseModel {
   private String type; // 'fiche' ou 'certif'
   private String dateGeneration;
   private String generePar;
   private String formatFichier;
   private String idDocuments;
   private String matriculeEleve;

   public DocumentScolaire(){
      super();
      this.idDocuments = UUID.randomUUID().toString();
   }

   public String getType() { return type; }
   public void setType(String type) { this.type = type; }

   public String getDateGeneration() { return dateGeneration; }
   public void setDateGeneration(String dateGeneration) { this.dateGeneration = dateGeneration; }

   public String getGenerePar() { return generePar; }
   public void setGenerePar(String generePar) { this.generePar = generePar; }

   public String getFormatFichier() { return formatFichier; }
   public void setFormatFichier(String formatFichier) { this.formatFichier = formatFichier; }

   public String getIdDocuments() { return idDocuments; }
   public void setIdDocuments(String idDocuments) { this.idDocuments = idDocuments; }

   public String getMatriculeEleve() { return matriculeEleve; }
   public void setMatriculeEleve(String matriculeEleve) { this.matriculeEleve = matriculeEleve; }
}