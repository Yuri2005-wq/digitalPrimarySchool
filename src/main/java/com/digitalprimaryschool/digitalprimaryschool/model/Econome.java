/***********************************************************************
 * Module:  Econome.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Econome
 ***********************************************************************/

import java.util.*;

/** @pdOid f528f91f-9ceb-4bfc-ab33-eaec638bab24 */
public class Econome extends Utilisateur {
   /** @pdOid f7e671fb-9664-4718-a874-2f9fb1cc5f84 */
   private String idEconome;
   
   /** @pdRoleInfo migr=no name=Paiement assc=association17 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<Paiement> paiement;
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Paiement> getPaiement() {
      if (paiement == null)
         paiement = new java.util.HashSet<Paiement>();
      return paiement;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorPaiement() {
      if (paiement == null)
         paiement = new java.util.HashSet<Paiement>();
      return paiement.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newPaiement */
   public void setPaiement(java.util.Collection<Paiement> newPaiement) {
      removeAllPaiement();
      for (java.util.Iterator iter = newPaiement.iterator(); iter.hasNext();)
         addPaiement((Paiement)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newPaiement */
   public void addPaiement(Paiement newPaiement) {
      if (newPaiement == null)
         return;
      if (this.paiement == null)
         this.paiement = new java.util.HashSet<Paiement>();
      if (!this.paiement.contains(newPaiement))
         this.paiement.add(newPaiement);
   }
   
   /** @pdGenerated default remove
     * @param oldPaiement */
   public void removePaiement(Paiement oldPaiement) {
      if (oldPaiement == null)
         return;
      if (this.paiement != null)
         if (this.paiement.contains(oldPaiement))
            this.paiement.remove(oldPaiement);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllPaiement() {
      if (paiement != null)
         paiement.clear();
   }

}