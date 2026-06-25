package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class Inscription {
   private String idInscription;
   private int idEcole; // Intégration pour le cloisonnement invisible de la session
   private String idAnnescolaire;
   private String idClasse;
   private String matriculeEleve;
   private double montantPayer;
   private int estReinscript;
   private String dateInscription;

   public Inscription() {
      this.idInscription = UUID.randomUUID().toString();
   }

   // --- Getter et Setter pour l'école ---
   public int getIdEcole() { return idEcole; }
   public void setIdEcole(int idEcole) { this.idEcole = idEcole; }

   // --- Getters et Setters existants ---
   public String getDateInscription() { return this.dateInscription; }
   public void setDateInscription(String dateInscription) { this.dateInscription = dateInscription; }

   public String getIdInscription() { return idInscription; }
   public void setIdInscription(String idInscription) { this.idInscription = idInscription; }

   public String getIdAnnescolaire() { return idAnnescolaire; }
   public void setIdAnnescolaire(String idAnnescolaire) { this.idAnnescolaire = idAnnescolaire; }
   public void setIdAnnescolaire(AnneeScolaire annee) { this.idAnnescolaire = annee.getIdAnnescolaire(); }

   public String getIdClasse() { return idClasse; }
   public void setIdClasse(String idClasse) { this.idClasse = idClasse; }
   public void setIdClasse(Classe classe) { this.idClasse = classe.getIdClasse(); }

   public String getMatriculeEleve() { return matriculeEleve; }
   public void setMatriculeEleve(String matriculeEleve) { this.matriculeEleve = matriculeEleve; }
   public void setMatriculeEleve(Eleve eleve) { this.matriculeEleve = eleve.getMatricule(); }

   public double getMontantPayer() { return montantPayer; }
   public void setMontantPayer(double montantPayer) { this.montantPayer = montantPayer; }

   public int getEstReinscript() { return estReinscript; }
   public void setEstReinscript(int estReinscript) { this.estReinscript = estReinscript; }
   public void setEstReinscript(boolean estReinscript) { this.estReinscript = estReinscript ? 1 : 0; }
}