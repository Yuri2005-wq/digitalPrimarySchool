package com.digitalprimaryschool.digitalprimaryschool.model;
import java.util.*;

public class Paiement{
   private String reference;
   private double montant;
   private String datePaiement;
   private String libelle;
   private String modePaiement;
   private int tranchePayee;
   private String encaisserPar;
   private String idPaiement;

   
   public recue_paiement recue_paiement;
   public Collection<Tranche> tranche;



   public Paiement(){
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

   public String getIdPaiement() { return idPaiement; }
   public void setIdPaiement(String idPaiement) { this.idPaiement = idPaiement; }
   public String getLibelle() { return this.libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public String getReference() { return reference; }
   public void setReference(String reference) { this.reference = reference; }

   public double getMontant() { return montant; }
   public void setMontant(double montant) { this.montant = montant; }

   public String getDatePaiement() { return datePaiement; }
   public void setDatePaiement(String datePaiement) { this.datePaiement = datePaiement; }

   public String getModePaiement() { return modePaiement; }
   public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

   public int getTranchePayee() { return tranchePayee; }
   public void setTranchePayee(int tranchePayee) { this.tranchePayee = tranchePayee; }

   public String getEncaisserPar() { return encaisserPar; }
   public void setEncaisserPar(String encaisserPar) { this.encaisserPar = encaisserPar; }

//   public String getIdTarifScolaire() { return idTarifScolaire; }
//   public void setIdTarifScolaire(String idTarifScolaire) { this.idTarifScolaire = idTarifScolaire; }
//












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
         addTranche((Tranche)iter.next());
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

}