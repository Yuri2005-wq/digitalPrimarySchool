/***********************************************************************
 * Module:  Parent.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Parent
 ***********************************************************************/

import java.util.*;

/** @pdOid 4285c1f7-a095-4c28-99bc-dea94966caeb */
public class Parent extends Utilisateur {
   /** @pdOid f2dba8f3-2995-43ce-bbae-791516e0a0ad */
   private String idParent;
   /** @pdOid 994794bc-07b3-49f3-9a8d-f7615d4d2420 */
   private char prenom;
   /** @pdOid cc41add3-fcc1-4a52-8709-ad36f47cc79b */
   private int contactParent;
   /** @pdOid 5e7bc6af-080e-4493-9d6e-50517378b6e4 */
   private char emailParent;
   /** @pdOid 3e113196-6026-4d7e-a2f4-92374b13c77c */
   private char profession;
   /** @pdOid 692e61b8-7c99-4d3a-95eb-b549746015b0 */
   private char adresse;
   
   /** @pdRoleInfo migr=no name=Eleve assc=association3 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<Eleve> eleve;
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Eleve> getEleve() {
      if (eleve == null)
         eleve = new java.util.HashSet<Eleve>();
      return eleve;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorEleve() {
      if (eleve == null)
         eleve = new java.util.HashSet<Eleve>();
      return eleve.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newEleve */
   public void setEleve(java.util.Collection<Eleve> newEleve) {
      removeAllEleve();
      for (java.util.Iterator iter = newEleve.iterator(); iter.hasNext();)
         addEleve((Eleve)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newEleve */
   public void addEleve(Eleve newEleve) {
      if (newEleve == null)
         return;
      if (this.eleve == null)
         this.eleve = new java.util.HashSet<Eleve>();
      if (!this.eleve.contains(newEleve))
         this.eleve.add(newEleve);
   }
   
   /** @pdGenerated default remove
     * @param oldEleve */
   public void removeEleve(Eleve oldEleve) {
      if (oldEleve == null)
         return;
      if (this.eleve != null)
         if (this.eleve.contains(oldEleve))
            this.eleve.remove(oldEleve);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllEleve() {
      if (eleve != null)
         eleve.clear();
   }

}