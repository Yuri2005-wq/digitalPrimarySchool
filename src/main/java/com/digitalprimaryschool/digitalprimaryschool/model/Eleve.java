/***********************************************************************
 * Module:  Eleve.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Eleve
 ***********************************************************************/

import java.util.*;

/** @pdOid 4c2ef48e-f18a-4f0d-9c13-84b4046598e2 */
public class Eleve {
   /** @pdOid eaf54a10-607f-40ad-bbf1-eaaf7e9e13a5 */
   private String idMatricule;
   /** @pdOid 83f99175-1a6f-4efe-b9b7-2765c09a3f5c */
   private String nom;
   /** @pdOid 175b4fcc-a6d4-45fe-9a18-1cbade24d046 */
   private String prenom;
   /** @pdOid 6b5d8022-ed39-4105-b636-ff788dcf2cd4 */
   private Date dateNaissance;
   /** @pdOid 675323fb-3107-4f34-8c9d-a2324ae22762 */
   private String lieuNaissance;
   /** @pdOid 2a093ff1-ef5f-4297-9bbd-556a437db7a1 */
   private enum(F,M) sexe;
   /** @pdOid c6ae3663-0f5f-462a-89d7-8fbd2a65e594 */
   private String nationnalite;
   /** @pdOid ce022daf-a23e-436c-ba70-19d4a2a0d442 */
   private String photo;
   /** @pdOid 7564c38d-170c-4858-a140-e6daf568517e */
   private boolean aTerminerPension;
   
   /** @pdRoleInfo migr=no name=Inscription assc=association5 mult=1 */
   public Inscription inscription;
   /** @pdRoleInfo migr=no name=DocumentScolaire assc=association13 coll=java.util.Collection impl=java.util.HashSet mult=1..* type=Composition */
   public java.util.Collection<DocumentScolaire> documentScolaire;
   
   /** @pdOid b1dd922f-1be2-4db7-b7a4-7d1abfe8a27c */
   public void genereFiche() {
      // TODO: implement
   }
   
   /** @pdOid 319a318b-369c-4e09-a424-6f9d19dab6f2 */
   public void genereCertificatScolarite() {
      // TODO: implement
   }
   
   /** @pdOid 559236d0-83ac-44d3-8350-6e7661516367 */
   public void getBulletin() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default parent getter */
   public Inscription getInscription() {
      return inscription;
   }
   
   /** @pdGenerated default parent setter
     * @param newInscription */
   public void setInscription(Inscription newInscription) {
      this.inscription = newInscription;
   }
   /** @pdGenerated default getter */
   public java.util.Collection<DocumentScolaire> getDocumentScolaire() {
      if (documentScolaire == null)
         documentScolaire = new java.util.HashSet<DocumentScolaire>();
      return documentScolaire;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorDocumentScolaire() {
      if (documentScolaire == null)
         documentScolaire = new java.util.HashSet<DocumentScolaire>();
      return documentScolaire.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newDocumentScolaire */
   public void setDocumentScolaire(java.util.Collection<DocumentScolaire> newDocumentScolaire) {
      removeAllDocumentScolaire();
      for (java.util.Iterator iter = newDocumentScolaire.iterator(); iter.hasNext();)
         addDocumentScolaire((DocumentScolaire)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newDocumentScolaire */
   public void addDocumentScolaire(DocumentScolaire newDocumentScolaire) {
      if (newDocumentScolaire == null)
         return;
      if (this.documentScolaire == null)
         this.documentScolaire = new java.util.HashSet<DocumentScolaire>();
      if (!this.documentScolaire.contains(newDocumentScolaire))
         this.documentScolaire.add(newDocumentScolaire);
   }
   
   /** @pdGenerated default remove
     * @param oldDocumentScolaire */
   public void removeDocumentScolaire(DocumentScolaire oldDocumentScolaire) {
      if (oldDocumentScolaire == null)
         return;
      if (this.documentScolaire != null)
         if (this.documentScolaire.contains(oldDocumentScolaire))
            this.documentScolaire.remove(oldDocumentScolaire);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllDocumentScolaire() {
      if (documentScolaire != null)
         documentScolaire.clear();
   }

}