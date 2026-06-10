/***********************************************************************
 * Module:  Paiement.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Paiement
 ***********************************************************************/

import java.util.*;

/** @pdOid d3c9ad95-5197-4bf1-b067-23f5b979e219 */
public class Paiement {
   /** @pdOid 77faa402-ca7e-46cc-85b9-9daedf020276 */
   private char reference;
   /** @pdOid 98586d58-295f-4677-8ca1-d204776c28f1 */
   private double montant;
   /** @pdOid 9a9cba3c-28d8-4a13-9fa5-a5522e4cef80 */
   private Date datePaiement;
   /** @pdOid 8c3fbdc6-1d37-453b-9d33-a279a8133c5a */
   private enum modePaiement;
   /** @pdOid 9f88af97-c262-4420-b5a3-d6e511548440 */
   private int tranchePayee;
   /** @pdOid 39c1b362-290f-41ba-9334-4b9970adc551 */
   private String encaisserPar;
   /** @pdOid ae7ab20d-6713-41c7-b18f-b38c3f00598b */
   private String idPaiement;
   
   /** @pdRoleInfo migr=no name=recue_paiement assc=association15 mult=1 */
   public recue_paiement recue_paiement;
   /** @pdRoleInfo migr=no name=Tranche assc=association22 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<Tranche> tranche;
   
   /** @pdOid c674d4cd-3d96-4833-9992-69767a05ea55 */
   public void valider() {
      // TODO: implement
   }
   
   /** @pdOid 14d1daec-33ef-46e2-8fd0-a7a73eb408fd */
   public void genererRecu() {
      // TODO: implement
   }
   
   /** @pdOid 4e209022-8884-4f1c-8d0e-3a3e2ac4af16 */
   public void annuler() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default parent getter */
   public recue_paiement getRecue_paiement() {
      return recue_paiement;
   }
   
   /** @pdGenerated default parent setter
     * @param newRecue_paiement */
   public void setRecue_paiement(recue_paiement newRecue_paiement) {
      this.recue_paiement = newRecue_paiement;
   }
   /** @pdGenerated default getter */
   public java.util.Collection<Tranche> getTranche() {
      if (tranche == null)
         tranche = new java.util.HashSet<Tranche>();
      return tranche;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorTranche() {
      if (tranche == null)
         tranche = new java.util.HashSet<Tranche>();
      return tranche.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newTranche */
   public void setTranche(java.util.Collection<Tranche> newTranche) {
      removeAllTranche();
      for (java.util.Iterator iter = newTranche.iterator(); iter.hasNext();)
         addTranche((Tranche)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newTranche */
   public void addTranche(Tranche newTranche) {
      if (newTranche == null)
         return;
      if (this.tranche == null)
         this.tranche = new java.util.HashSet<Tranche>();
      if (!this.tranche.contains(newTranche))
         this.tranche.add(newTranche);
   }
   
   /** @pdGenerated default remove
     * @param oldTranche */
   public void removeTranche(Tranche oldTranche) {
      if (oldTranche == null)
         return;
      if (this.tranche != null)
         if (this.tranche.contains(oldTranche))
            this.tranche.remove(oldTranche);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllTranche() {
      if (tranche != null)
         tranche.clear();
   }

}