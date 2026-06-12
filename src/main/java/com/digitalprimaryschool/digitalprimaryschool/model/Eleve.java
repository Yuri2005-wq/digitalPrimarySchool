package com.digitalprimaryschool.digitalprimaryschool.model;

import java.security.SecureRandom;
import java.util.*;

public class Eleve {
   private String Matricule;
   private String nom;
   private String prenom;
   private Date dateNaissance;
   private String lieuNaissance;
   private String sexe;
   private String nationnalite;
   private String photo;
   private boolean aTerminerPension;
   public Bulletin bulletinEleve;
   public Parent parent;
   public Inscription inscription;
   public Collection<DocumentScolaire> documentScolaire;


   private static final SecureRandom random = new SecureRandom();

   //Constructeur Par defaut
   public Eleve(){
      super();
      int anneeActuelle = java.time.Year.now().getValue();

      // 2. Générer un nombre aléatoire entre 0 et 9999
      int nombreAleatoire = random.nextInt(100000);

      // 3. Formater le nombre pour qu'il fasse toujours 4 chiffres (ex: 7 devient 0007)
      String suffixeAleatoire = String.format("%05d", nombreAleatoire);

      // 4. Assembler le matricule final
      this.Matricule = "MAT" + anneeActuelle + suffixeAleatoire;
   }

   public String getParent(){
      return this.parent.getIdParent();
   }

   public String getIdParent() {
      // Si l'objet parent existe, on récupère son ID parent, sinon on renvoie une valeur par défaut ou null
      return (this.parent != null) ? this.parent.getIdParent() : null;
   }
   public String getMatricule(){
      return this.Matricule;
   }
   public String getNom(){
      return this.nom;
   }
   public String getPrenom(){
      return this.prenom;
   }
   public Date getDateNaissance(){
      return this.dateNaissance;
   }
   public String getLieuNaissance(){
      return this.lieuNaissance;
   }
   public String getSexe(){
      return this.sexe;
   }
   public String getNationnalite(){
      return this.nationnalite;
   }
   public String getPhoto(){
      return this.photo;
   }
   public boolean getATerminerPension(){
      return this.aTerminerPension;
   }


   public void regenererMatricule() {
      int anneeActuelle = java.time.Year.now().getValue();
      int nombreAleatoire = random.nextInt(100000);
      this.Matricule = "MAT" + anneeActuelle + String.format("%05d", nombreAleatoire);
   }


   public void genereFiche() {
      // TODO: implement
   }
   
   public void genereCertificatScolarite() {
      // TODO: implement
   }
   
   public Bulletin getBulletin() {
      return this.bulletinEleve;
   }
   
   
   public Inscription getInscription() {
      return inscription;
   }
   
   public void setInscription(Inscription newInscription) {
      this.inscription = newInscription;
   }
   public Collection<DocumentScolaire> getDocumentScolaire() {
      if (documentScolaire == null)
         documentScolaire = new HashSet<DocumentScolaire>();
      return documentScolaire;
   }
   
   public Iterator getIteratorDocumentScolaire() {
      if (documentScolaire == null)
         documentScolaire = new HashSet<DocumentScolaire>();
      return documentScolaire.iterator();
   }
   

   public void setDocumentScolaire(Collection<DocumentScolaire> newDocumentScolaire) {
      removeAllDocumentScolaire();
      for (Iterator iter = newDocumentScolaire.iterator(); iter.hasNext();)
         addDocumentScolaire((DocumentScolaire)iter.next());
   }
   

   public void addDocumentScolaire(DocumentScolaire newDocumentScolaire) {
      if (newDocumentScolaire == null)
         return;
      if (this.documentScolaire == null)
         this.documentScolaire = new HashSet<DocumentScolaire>();
      if (!this.documentScolaire.contains(newDocumentScolaire))
         this.documentScolaire.add(newDocumentScolaire);
   }
   

   public void removeDocumentScolaire(DocumentScolaire oldDocumentScolaire) {
      if (oldDocumentScolaire == null)
         return;
      if (this.documentScolaire != null)
         if (this.documentScolaire.contains(oldDocumentScolaire))
            this.documentScolaire.remove(oldDocumentScolaire);
   }
   
   public void removeAllDocumentScolaire() {
      if (documentScolaire != null)
         documentScolaire.clear();
   }

}