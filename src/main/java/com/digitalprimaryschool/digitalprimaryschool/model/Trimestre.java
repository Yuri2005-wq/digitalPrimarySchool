/***********************************************************************
 * Module:  Trimestre.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Trimestre
 ***********************************************************************/

import java.util.*;

/** @pdOid dc569889-87b7-4991-84bf-496b17c21c72 */
public class Trimestre {
   /** @pdOid a470cec6-0b11-4e87-a612-4f118c6d0b71 */
   private String idTrimestre;
   /** @pdOid 23f2bc4c-0bb0-4e7d-8da9-226f534f59a9 */
   private char libelle;
   /** @pdOid 2cc2c6ac-7f1a-4a8f-8cb4-d4e3d8e81bf2 */
   private Date dateDebut;
   /** @pdOid e32c1d3f-4459-4b27-8097-b91d3aabd839 */
   private Date dateFin;
   /** @pdOid f333bcdc-7a98-46df-8a1e-da3d95da62c8 */
   private boolean estClos;
   
   /** @pdRoleInfo migr=no name=Sequence assc=association11 coll=java.util.Collection impl=java.util.HashSet mult=1..2 type=Composition */
   public java.util.Collection<Sequence> sequence;
   /** @pdRoleInfo migr=no name=Bulletin assc=association20 mult=1 */
   public Bulletin bulletin;
   
   /** @pdOid 8634bc37-6c42-43e4-bd77-1b4f0e8d1f95 */
   public double calculerMoyenne() {
      // TODO: implement
      return 0;
   }
   
   /** @pdOid 567fbef7-d8c1-4b17-9a0f-cb1c969a23ea */
   public void genererBulletin() {
      // TODO: implement
   }
   
   /** @pdOid b9794d5e-454f-4518-85f4-0be75bd181bb */
   public void cloturer() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Sequence> getSequence() {
      if (sequence == null)
         sequence = new java.util.HashSet<Sequence>();
      return sequence;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorSequence() {
      if (sequence == null)
         sequence = new java.util.HashSet<Sequence>();
      return sequence.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newSequence */
   public void setSequence(java.util.Collection<Sequence> newSequence) {
      removeAllSequence();
      for (java.util.Iterator iter = newSequence.iterator(); iter.hasNext();)
         addSequence((Sequence)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newSequence */
   public void addSequence(Sequence newSequence) {
      if (newSequence == null)
         return;
      if (this.sequence == null)
         this.sequence = new java.util.HashSet<Sequence>();
      if (!this.sequence.contains(newSequence))
         this.sequence.add(newSequence);
   }
   
   /** @pdGenerated default remove
     * @param oldSequence */
   public void removeSequence(Sequence oldSequence) {
      if (oldSequence == null)
         return;
      if (this.sequence != null)
         if (this.sequence.contains(oldSequence))
            this.sequence.remove(oldSequence);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllSequence() {
      if (sequence != null)
         sequence.clear();
   }
   /** @pdGenerated default parent getter */
   public Bulletin getBulletin() {
      return bulletin;
   }
   
   /** @pdGenerated default parent setter
     * @param newBulletin */
   public void setBulletin(Bulletin newBulletin) {
      this.bulletin = newBulletin;
   }

}