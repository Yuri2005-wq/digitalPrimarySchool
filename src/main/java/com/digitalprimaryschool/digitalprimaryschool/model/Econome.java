package com.digitalprimaryschool.digitalprimaryschool.model;
import java.util.*;

public class Econome extends Utilisateur {
   private String idEconome;
   
   public Collection<Paiement> paiement;
   
   public Econome(){
      this.idEconome = UUID.randomUUID().toString();
   }


   public String getIdEconome() { return idEconome; }
   public void setIdEconome(String idEconome) { this.idEconome = idEconome; }


   public Collection<Paiement> getPaiement() {
      if (paiement == null)
         paiement = new HashSet<Paiement>();
      return paiement;
   }
   
   public Iterator getIteratorPaiement() {
      if (paiement == null)
         paiement = new HashSet<Paiement>();
      return paiement.iterator();
   }
   
   public void setPaiement(Collection<Paiement> newPaiement) {
      removeAllPaiement();
      for (Iterator iter = newPaiement.iterator(); iter.hasNext();)
         addPaiement((Paiement)iter.next());
   }

   public void addPaiement(Paiement newPaiement) {
      if (newPaiement == null)
         return;
      if (this.paiement == null)
         this.paiement = new HashSet<Paiement>();
      if (!this.paiement.contains(newPaiement))
         this.paiement.add(newPaiement);
   }
   

   public void removePaiement(Paiement oldPaiement) {
      if (oldPaiement == null)
         return;
      if (this.paiement != null)
         if (this.paiement.contains(oldPaiement))
            this.paiement.remove(oldPaiement);
   }
   
   public void removeAllPaiement() {
      if (paiement != null)
         paiement.clear();
   }

}