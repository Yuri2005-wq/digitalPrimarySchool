/***********************************************************************
 * Module:  Classe.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Classe
 ***********************************************************************/

import java.util.*;

/** @pdOid 6728fcef-ffbc-40e8-813d-199b4e07334b */
public class Classe {
   /** @pdOid c73ae128-f690-4791-9715-310b9272a324 */
   private int idClasse;
   /** @pdOid 565ad5a8-25fc-4dcd-80b8-9e47895e5122 */
   private char nom;
   /** @pdOid dd3031d7-dd24-43f5-b368-8d03b21efc3c */
   private char niveau;
   /** @pdOid bab79557-25bd-4c51-b5ae-9e0e2f41b19e */
   private int capaciteMax;
   /** @pdOid 9bdc339d-a7e7-42f8-8464-b5d54b2bd670 */
   private char section;
   
   /** @pdRoleInfo migr=no name=TarisScolaire assc=association8 mult=1 */
   public TarisScolaire tarisScolaire;
   /** @pdRoleInfo migr=no name=Matiere assc=association10 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<Matiere> matiere;
   
   /** @pdOid 10617e95-3caa-4c76-a422-c899c8e75d9f */
   public void getEleveNonEnRegle() {
      // TODO: implement
   }
   
   /** @pdOid 97c84dad-4945-4ebe-9db8-4fa2a69bdb75 */
   public void getMoyenneGenerale() {
      // TODO: implement
   }
   
   /** @pdOid 92328e9e-45a1-4fbd-9032-231721291c08 */
   public void getClassement() {
      // TODO: implement
   }
   
   /** @pdOid 4740e08d-5d9a-46ad-afbd-1f3651dfcf2c */
   public char getProfTitulaire() {
      // TODO: implement
      return 0;
   }
   
   /** @pdOid 6eb959cb-e109-4d1d-ae2a-6a7de1fce4a4 */
   public double totalFraisClasse() {
      // TODO: implement
      return 0;
   }
   
   
   /** @pdGenerated default parent getter */
   public TarisScolaire getTarisScolaire() {
      return tarisScolaire;
   }
   
   /** @pdGenerated default parent setter
     * @param newTarisScolaire */
   public void setTarisScolaire(TarisScolaire newTarisScolaire) {
      this.tarisScolaire = newTarisScolaire;
   }
   /** @pdGenerated default getter */
   public java.util.Collection<Matiere> getMatiere() {
      if (matiere == null)
         matiere = new java.util.HashSet<Matiere>();
      return matiere;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorMatiere() {
      if (matiere == null)
         matiere = new java.util.HashSet<Matiere>();
      return matiere.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newMatiere */
   public void setMatiere(java.util.Collection<Matiere> newMatiere) {
      removeAllMatiere();
      for (java.util.Iterator iter = newMatiere.iterator(); iter.hasNext();)
         addMatiere((Matiere)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newMatiere */
   public void addMatiere(Matiere newMatiere) {
      if (newMatiere == null)
         return;
      if (this.matiere == null)
         this.matiere = new java.util.HashSet<Matiere>();
      if (!this.matiere.contains(newMatiere))
         this.matiere.add(newMatiere);
   }
   
   /** @pdGenerated default remove
     * @param oldMatiere */
   public void removeMatiere(Matiere oldMatiere) {
      if (oldMatiere == null)
         return;
      if (this.matiere != null)
         if (this.matiere.contains(oldMatiere))
            this.matiere.remove(oldMatiere);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllMatiere() {
      if (matiere != null)
         matiere.clear();
   }

}