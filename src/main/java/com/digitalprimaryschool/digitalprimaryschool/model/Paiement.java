package com.digitalprimaryschool.digitalprimaryschool.model;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.util.*;

public class Paiement {
   private String reference;
   private double montant;
   private String datePaiement;
   private String libelle;
   private ModePaiement modePaiement; // Changé de String à ModePaiement
   private int tranchePayee;
   private String encaisserPar;
   private String idPaiement;

   public recue_paiement recue_paiement;
   public Collection<Tranche> tranche;

   private static final SecureRandom random = new SecureRandom();

   public Paiement() {
      this.idPaiement = UUID.randomUUID().toString();
   }

   public void valider() {
      // TODO: implement
   }

   public void genererRecu() {
      // TODO: implement
   }

   public void annuler() {
      // TODO: implement
   }

   public String getIdPaiement() {
      return idPaiement;
   }

   public void setIdPaiement(String idPaiement) {
      this.idPaiement = idPaiement;
   }

   public String getLibelle() {
      return this.libelle;
   }

   public void setLibelle(String libelle) {
      this.libelle = libelle;
   }

   public String getReference() {
      return reference;
   }

   public void setReference(String reference) {
      this.reference = reference;
   }

   public double getMontant() {
      return montant;
   }

   public void setMontant(double montant) {
      this.montant = montant;
   }

   public String getDatePaiement() {
      return datePaiement;
   }

   public void setDatePaiement(String datePaiement) {
      this.datePaiement = datePaiement;
   }

   // Getter pour ModePaiement (retourne l'enum)
   public ModePaiement getModePaiement() {
      return modePaiement;
   }

   // Setter pour ModePaiement (prend l'enum)
   public void setModePaiement(ModePaiement modePaiement) {
      this.modePaiement = modePaiement;
   }

   // Setter pour ModePaiement (prend un String et le convertit en enum)
   public void setModePaiement(String modePaiement) {
      if (modePaiement != null && !modePaiement.isEmpty()) {
         try {
            this.modePaiement = ModePaiement.valueOf(modePaiement);
         } catch (IllegalArgumentException e) {
            System.err.println("Mode de paiement inconnu : " + modePaiement);
            this.modePaiement = ModePaiement.AUTRE;
         }
      } else {
         this.modePaiement = ModePaiement.AUTRE;
      }
   }

   // Setter pour ModePaiement (prend le libellé)
   public void setModePaiementParLibelle(String libelle) {
      if (libelle != null && !libelle.isEmpty()) {
         for (ModePaiement m : ModePaiement.values()) {
            if (m.getLibelle().equalsIgnoreCase(libelle)) {
               this.modePaiement = m;
               return;
            }
         }
      }
      this.modePaiement = ModePaiement.AUTRE;
   }

   // Pour la compatibilité avec l'ancien code (retourne le libellé)
   public String getModePaiementLibelle() {
      return this.modePaiement != null ? this.modePaiement.getLibelle() : "";
   }

   public int getTranchePayee() {
      return tranchePayee;
   }

   public void setTranchePayee(int tranchePayee) {
      this.tranchePayee = tranchePayee;
   }

   public String getEncaisserPar() {
      return encaisserPar;
   }

   public void setEncaisserPar(String encaisserPar) {
      this.encaisserPar = encaisserPar;
   }

   public recue_paiement getRecue_paiement() {
      return recue_paiement;
   }

   public void setRecue_paiement(recue_paiement newRecue_paiement) {
      this.recue_paiement = newRecue_paiement;
   }

   public Collection<Tranche> getTranche() {
      if (tranche == null)
         tranche = new HashSet<Tranche>();
      return tranche;
   }

   public Iterator getIteratorTranche() {
      if (tranche == null)
         tranche = new HashSet<Tranche>();
      return tranche.iterator();
   }

   public void setTranche(Collection<Tranche> newTranche) {
      removeAllTranche();
      for (Iterator iter = newTranche.iterator(); iter.hasNext();)
         addTranche((Tranche) iter.next());
   }

   public void addTranche(Tranche newTranche) {
      if (newTranche == null)
         return;
      if (this.tranche == null)
         this.tranche = new HashSet<Tranche>();
      if (!this.tranche.contains(newTranche))
         this.tranche.add(newTranche);
   }

   public void removeTranche(Tranche oldTranche) {
      if (oldTranche == null)
         return;
      if (this.tranche != null)
         if (this.tranche.contains(oldTranche))
            this.tranche.remove(oldTranche);
   }

   public void removeAllTranche() {
      if (tranche != null)
         tranche.clear();
   }

   public void regenererMatricule() {
      DayOfWeek anneeActuelle = DayOfWeek.of(7);
      int nombreAleatoire = random.nextInt(100000);
      this.reference = "MAT" + anneeActuelle + String.format("%05d", nombreAleatoire);
   }
}