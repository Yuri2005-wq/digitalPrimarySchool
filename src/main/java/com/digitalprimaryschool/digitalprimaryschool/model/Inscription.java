/***********************************************************************
 * Module:  Inscription.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Inscription
 ***********************************************************************/

import java.util.*;

/** @pdOid 4fa9a8fe-81ce-4c78-a5e3-741c1313d30e */
public class Inscription {
   /** @pdOid 62c9a17f-bcd4-46c3-b339-f28a8952ff1d */
   private String idInscription;
   /** @pdOid 77e31c04-7e1f-42c5-8df3-975e7aca7f82 */
   private double montantPayer;
   /** @pdOid 8f78c6a9-48e4-4191-8c82-7b0950d704e7 */
   private enum statut;
   /** @pdOid f269beb4-52f5-455f-b3f0-1a6575f91f2f */
   private boolean estReinscript;
   /** @pdOid 3e94c743-d3ca-4480-a597-47e962b19d30 */
   private char numeroBordero;
   /** @pdOid 6e5636ee-88a7-4fd1-a0ab-e86724fa3afc */
   private date dateInscription;
   
   /** @pdRoleInfo migr=no name=Classe assc=association6 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<Classe> classe;
   /** @pdRoleInfo migr=no name=AnneeScolaire assc=association7 coll=java.util.Collection impl=java.util.HashSet mult=1..* */
   public java.util.Collection<AnneeScolaire> anneeScolaire;
   /** @pdRoleInfo migr=no name=Paiement assc=association21 mult=1 */
   public Paiement paiement;
   
   /** @pdOid b8cee9d0-e5a5-4cd2-9f44-b380ea366491 */
   public void genererBordereau() {
      // TODO: implement
   }
   
   /** @pdOid c8f22845-3e09-41be-a8c2-d24123087285 */
   public void valider() {
      // TODO: implement
   }
   
   /** @pdOid 50951e70-d400-4816-ae76-522d2e5a8f34 */
   public void imprimerRecusInscription() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Classe> getClasse() {
      if (classe == null)
         classe = new java.util.HashSet<Classe>();
      return classe;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorClasse() {
      if (classe == null)
         classe = new java.util.HashSet<Classe>();
      return classe.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newClasse */
   public void setClasse(java.util.Collection<Classe> newClasse) {
      removeAllClasse();
      for (java.util.Iterator iter = newClasse.iterator(); iter.hasNext();)
         addClasse((Classe)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newClasse */
   public void addClasse(Classe newClasse) {
      if (newClasse == null)
         return;
      if (this.classe == null)
         this.classe = new java.util.HashSet<Classe>();
      if (!this.classe.contains(newClasse))
         this.classe.add(newClasse);
   }
   
   /** @pdGenerated default remove
     * @param oldClasse */
   public void removeClasse(Classe oldClasse) {
      if (oldClasse == null)
         return;
      if (this.classe != null)
         if (this.classe.contains(oldClasse))
            this.classe.remove(oldClasse);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllClasse() {
      if (classe != null)
         classe.clear();
   }
   /** @pdGenerated default getter */
   public java.util.Collection<AnneeScolaire> getAnneeScolaire() {
      if (anneeScolaire == null)
         anneeScolaire = new java.util.HashSet<AnneeScolaire>();
      return anneeScolaire;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorAnneeScolaire() {
      if (anneeScolaire == null)
         anneeScolaire = new java.util.HashSet<AnneeScolaire>();
      return anneeScolaire.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newAnneeScolaire */
   public void setAnneeScolaire(java.util.Collection<AnneeScolaire> newAnneeScolaire) {
      removeAllAnneeScolaire();
      for (java.util.Iterator iter = newAnneeScolaire.iterator(); iter.hasNext();)
         addAnneeScolaire((AnneeScolaire)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newAnneeScolaire */
   public void addAnneeScolaire(AnneeScolaire newAnneeScolaire) {
      if (newAnneeScolaire == null)
         return;
      if (this.anneeScolaire == null)
         this.anneeScolaire = new java.util.HashSet<AnneeScolaire>();
      if (!this.anneeScolaire.contains(newAnneeScolaire))
         this.anneeScolaire.add(newAnneeScolaire);
   }
   
   /** @pdGenerated default remove
     * @param oldAnneeScolaire */
   public void removeAnneeScolaire(AnneeScolaire oldAnneeScolaire) {
      if (oldAnneeScolaire == null)
         return;
      if (this.anneeScolaire != null)
         if (this.anneeScolaire.contains(oldAnneeScolaire))
            this.anneeScolaire.remove(oldAnneeScolaire);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllAnneeScolaire() {
      if (anneeScolaire != null)
         anneeScolaire.clear();
   }
   /** @pdGenerated default parent getter */
   public Paiement getPaiement() {
      return paiement;
   }
   
   /** @pdGenerated default parent setter
     * @param newPaiement */
   public void setPaiement(Paiement newPaiement) {
      this.paiement = newPaiement;
   }

}