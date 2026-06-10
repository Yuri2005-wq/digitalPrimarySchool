/***********************************************************************
 * Module:  AnneeScolaire.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class AnneeScolaire
 ***********************************************************************/

import java.util.*;

/** @pdOid 0cb692cf-8d41-4c7f-b212-56a9fe6449c3 */
public class AnneeScolaire {
   /** @pdOid 0c1f540b-b250-4b1c-b7b1-7868922d78ca */
   private String idAnnescolaire;
   /** @pdOid 240943a6-f009-47b0-92ba-359613bb5310 */
   private char libelle;
   /** @pdOid 2108cd7a-87ec-4691-9703-85f4f1373805 */
   private Date dateDebut;
   /** @pdOid c5a8b9d1-aa9c-4b81-8e80-17bbea2c8c09 */
   private Date dateFin;
   /** @pdOid 857c253c-6cf7-4883-acbe-9277b10ea609 */
   private boolean estActive;
   
   /** @pdRoleInfo migr=no name=Trimestre assc=association9 coll=java.util.Collection impl=java.util.HashSet mult=1..* type=Composition */
   public java.util.Collection<Trimestre> trimestre;
   
   /** @pdOid 7420584b-8f8a-4412-b404-9497e6e55895 */
   public void reinscriptionMasse() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Trimestre> getTrimestre() {
      if (trimestre == null)
         trimestre = new java.util.HashSet<Trimestre>();
      return trimestre;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorTrimestre() {
      if (trimestre == null)
         trimestre = new java.util.HashSet<Trimestre>();
      return trimestre.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newTrimestre */
   public void setTrimestre(java.util.Collection<Trimestre> newTrimestre) {
      removeAllTrimestre();
      for (java.util.Iterator iter = newTrimestre.iterator(); iter.hasNext();)
         addTrimestre((Trimestre)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newTrimestre */
   public void addTrimestre(Trimestre newTrimestre) {
      if (newTrimestre == null)
         return;
      if (this.trimestre == null)
         this.trimestre = new java.util.HashSet<Trimestre>();
      if (!this.trimestre.contains(newTrimestre))
         this.trimestre.add(newTrimestre);
   }
   
   /** @pdGenerated default remove
     * @param oldTrimestre */
   public void removeTrimestre(Trimestre oldTrimestre) {
      if (oldTrimestre == null)
         return;
      if (this.trimestre != null)
         if (this.trimestre.contains(oldTrimestre))
            this.trimestre.remove(oldTrimestre);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllTrimestre() {
      if (trimestre != null)
         trimestre.clear();
   }

}