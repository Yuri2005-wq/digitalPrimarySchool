/***********************************************************************
 * Module:  Notes.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Notes
 ***********************************************************************/

import java.util.*;

/** @pdOid 4f98ddb5-b2be-413b-b7a4-70065609a5e6 */
public class Notes {
   /** @pdOid f8fcfbee-a297-4090-b90d-2d1148205133 */
   private double valeur;
   /** @pdOid f43fcb50-9ecc-4671-94c7-19d6c3fd1328 */
   private int noteMax;
   /** @pdOid b968d765-8aeb-4be7-8646-a1e5170991dd */
   private char observation;
   /** @pdOid 394938e3-bb8b-4e7c-8055-a433aa56672b */
   private Date dateSaisie;
   /** @pdOid 81134d51-6f28-4940-a93b-1956ddb33b5c */
   private string saisirPar;
   /** @pdOid efce69c8-121a-4792-b2bd-9861b73ecbe9 */
   private String idNote;
   
   /** @pdRoleInfo migr=no name=Bulletin assc=association14 mult=1 */
   public Bulletin bulletin;
   /** @pdRoleInfo migr=no name=Eleve assc=association18 mult=1 */
   public Eleve eleve;
   /** @pdRoleInfo migr=no name=Sequence assc=association19 mult=1 */
   public Sequence sequence;
   
   /** @pdOid 4a853219-8961-45bc-b4d7-5a77061fcc90 */
   public void calculerPointPondere() {
      // TODO: implement
   }
   
   /** @pdOid 7fa9139e-f2db-4b8c-91d5-0be07a137ee8 */
   public void modifier() {
      // TODO: implement
   }
   
   /** @pdOid efd2e3b3-86a7-490e-b9d3-7d883ee16fb6 */
   public void historiser() {
      // TODO: implement
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
   /** @pdGenerated default parent getter */
   public Eleve getEleve() {
      return eleve;
   }
   
   /** @pdGenerated default parent setter
     * @param newEleve */
   public void setEleve(Eleve newEleve) {
      this.eleve = newEleve;
   }
   /** @pdGenerated default parent getter */
   public Sequence getSequence() {
      return sequence;
   }
   
   /** @pdGenerated default parent setter
     * @param newSequence */
   public void setSequence(Sequence newSequence) {
      this.sequence = newSequence;
   }

}