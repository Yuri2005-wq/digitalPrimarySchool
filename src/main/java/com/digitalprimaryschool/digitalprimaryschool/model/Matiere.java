/***********************************************************************
 * Module:  Matiere.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Matiere
 ***********************************************************************/

import java.util.*;

/** @pdOid c35ea25a-53fc-4805-82d5-2ba7b1f984f3 */
public class Matiere {
   /** @pdOid 3a60d76c-e7f3-4cd5-b036-f2f197d8ef2e */
   private String idMatiere;
   /** @pdOid d24f1381-fa5b-4abb-ab21-0eac803a365e */
   private String libelle;
   /** @pdOid 7a0fb16c-d364-4120-9396-1b2936b86d4d */
   private double coefficient;
   /** @pdOid 55604df4-6a8b-4928-bc57-9c93eaa4ae4d */
   private int volumeHoraire;
   /** @pdOid d700fb5b-141a-415f-8cd6-86e34a5268b8 */
   private String categorie;
   
   /** @pdRoleInfo migr=no name=Notes assc=association12 mult=1 type=Composition */
   public Notes notes;
   
   /** @pdOid 72d1c248-2392-4e84-9c76-27a23b4cd487 */
   public void getMoyenneClasse() {
      // TODO: implement
   }
   
   /** @pdOid 200660e0-21aa-451e-8150-45bd0a459b87 */
   public void getCoefficientPondere() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default parent getter */
   public Notes getNotes() {
      return notes;
   }
   
   /** @pdGenerated default parent setter
     * @param newNotes */
   public void setNotes(Notes newNotes) {
      this.notes = newNotes;
   }

}