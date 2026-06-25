package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class TarisScolaire {
   private String idTarifScolaire;
   private int idEcole; // Ajout requis pour le cloisonnement invisible
   private NiveauClasse niveauClasse;
   private double montantPension;
   private double fraistenueScolaire;
   private double fraistenueSport;
   private double fraisInscription;
   private String libelle;

   public Collection<Paiement> paiement;

   public TarisScolaire(){
      super();
      this.idTarifScolaire = UUID.randomUUID().toString();
   }

   // --- Getters et Setters pour la session/école ---
   public int getIdEcole() { return idEcole; }
   public void setIdEcole(int idEcole) { this.idEcole = idEcole; }

   // --- Anciens Getters et Setters ---
   public String getLibelle() { return libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public String getIdTarifScolaire() { return idTarifScolaire; }
   public void setIdTarifScolaire(String idTarifScolaire) { this.idTarifScolaire = idTarifScolaire; }

   public double getMontantPension() { return montantPension; }
   public void setMontantPension(double montantPension) { this.montantPension = montantPension; }

   public double getFraistenueScolaire() { return fraistenueScolaire; }
   public void setFraistenueScolaire(double fraistenueScolaire) { this.fraistenueScolaire = fraistenueScolaire; }

   public double getFraistenueSport() { return fraistenueSport; }
   public void setFraistenueSport(double fraistenueSport) { this.fraistenueSport = fraistenueSport; }

   public double getFraisInscription() { return fraisInscription; }
   public void setFraisInscription(double fraisInscription) { this.fraisInscription = fraisInscription; }

   public void setNiveauClasse(NiveauClasse niveauClasse) { this.niveauClasse = niveauClasse; }
   public NiveauClasse getNiveauClasse() { return this.niveauClasse; }

   public double getTotalFrais() {
      return fraisInscription + montantPension + fraistenueScolaire + fraistenueSport;
   }

   // --- Gestion de la collection de Paiement ---
   public Collection<Paiement> getPaiement() {
      if (paiement == null) paiement = new HashSet<>();
      return paiement;
   }

   public void setPaiement(Collection<Paiement> newPaiement) {
      if (paiement != null) paiement.clear();
      if (newPaiement != null) {
         for (Paiement p : newPaiement) addPaiement(p);
      }
   }

   public void addPaiement(Paiement newPaiement) {
      if (newPaiement == null) return;
      if (this.paiement == null) this.paiement = new HashSet<>();
      this.paiement.add(newPaiement);
   }
}