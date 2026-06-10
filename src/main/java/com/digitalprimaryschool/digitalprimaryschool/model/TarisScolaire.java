/***********************************************************************
 * Module:  TarisScolaire.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class TarisScolaire
 ***********************************************************************/

import java.util.*;

/** @pdOid 4f773eda-cd0d-475d-b7ca-f698c2a8c024 */
public class TarisScolaire {
   /** @pdOid ede65267-43ba-4573-9c33-61627a7d6291 */
   private int idTarifScolaire;
   /** @pdOid dca86760-3322-409a-a58b-bb5d9efae143 */
   private double montantApee;
   /** @pdOid f628934d-abd3-4688-9255-d11cf0344443 */
   private double fraistenueScolaire;
   /** @pdOid da197155-d1da-4aee-89b5-b6010e246381 */
   private double fraistenueSport;
   /** @pdOid 5c9a71c6-5074-4b2b-8d52-2b4aedfbf69f */
   private double fraisInscription;
   
   /** @pdRoleInfo migr=no name=Paiement assc=association16 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
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