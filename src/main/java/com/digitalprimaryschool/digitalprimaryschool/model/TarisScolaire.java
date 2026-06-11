

package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class TarisScolaire extends BaseModel {
   private String idTarifScolaire;
   private Classe classe;
   private double montantApee;
   private double fraistenueScolaire;
   private double fraistenueSport;
   private double fraisInscription;

   public TarisScolaire(){
      super();
      this.idTarifScolaire = UUID.randomUUID().toString();
   }
   public String getIdClasse(){
      return this.classe.getIdClasse();
   }
   public String getIdTarifScolaire() { return idTarifScolaire; }
   public void setIdTarifScolaire(String idTarifScolaire) { this.idTarifScolaire = idTarifScolaire; }

   public double getMontantApee() { return montantApee; }
   public void setMontantApee(double montantApee) { this.montantApee = montantApee; }

   public double getFraistenueScolaire() { return fraistenueScolaire; }
   public void setFraistenueScolaire(double fraistenueScolaire) { this.fraistenueScolaire = fraistenueScolaire; }

   public double getFraistenueSport() { return fraistenueSport; }
   public void setFraistenueSport(double fraistenueSport) { this.fraistenueSport = fraistenueSport; }

   public double getFraisInscription() { return fraisInscription; }
   public void setFraisInscription(double fraisInscription) { this.fraisInscription = fraisInscription; }
}

























//package com.digitalprimaryschool.digitalprimaryschool.model;
//import java.util.*;
//
//public class TarisScolaire{
//   private String idTarifScolaire;
//   private double montantApee;
//   private double fraistenueScolaire;
//   private double fraistenueSport;
//   private double fraisInscription;
//
//   public Collection<Paiement> paiement;
//
//
//   public TarisScolaire(){
//
//      this.idTarifScolaire  = UUID.randomUUID().toString();
//   }
//
//   public String getIdTarifScolaire() {
//      return idTarifScolaire;
//   }
//
//   public Collection<Paiement> getPaiement() {
//      if (paiement == null)
//         paiement = new HashSet<Paiement>();
//      return paiement;
//   }
//
//   public Iterator getIteratorPaiement() {
//      if (paiement == null)
//         paiement = new HashSet<Paiement>();
//      return paiement.iterator();
//   }
//
//   public void setPaiement(Collection<Paiement> newPaiement) {
//      removeAllPaiement();
//      for (Iterator iter = newPaiement.iterator(); iter.hasNext();)
//         addPaiement((Paiement)iter.next());
//   }
//
//   public void addPaiement(Paiement newPaiement) {
//      if (newPaiement == null)
//         return;
//      if (this.paiement == null)
//         this.paiement = new HashSet<Paiement>();
//      if (!this.paiement.contains(newPaiement))
//         this.paiement.add(newPaiement);
//   }
//
//   public void removePaiement(Paiement oldPaiement) {
//      if (oldPaiement == null)
//         return;
//      if (this.paiement != null)
//         if (this.paiement.contains(oldPaiement))
//            this.paiement.remove(oldPaiement);
//   }
//
//   public void removeAllPaiement() {
//      if (paiement != null)
//         paiement.clear();
//   }
//
//}