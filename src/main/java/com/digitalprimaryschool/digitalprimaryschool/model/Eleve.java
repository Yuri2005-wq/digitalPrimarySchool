package com.digitalprimaryschool.digitalprimaryschool.model;

import java.security.SecureRandom;
import java.util.*;

public class Eleve {
   private String Matricule;
   private String nom;
   private String prenom;
   private Date dateNaissance;
   private LieuNaissance lieuNaissance;
   private Sexe sexe;
   private Nationnalite nationalite;
   private String photo;
   private boolean aTerminerPension;
   private Region regionOrigine;
   private String antecedentsMedicaux;

   public Bulletin bulletinEleve;
   public Parent parent;
   public Inscription inscription;
   public Collection<DocumentScolaire> documentScolaire;

   private static final SecureRandom random = new SecureRandom();

   public Eleve() {
      super();
      int anneeActuelle = java.time.Year.now().getValue();
      int nombreAleatoire = random.nextInt(100000);
      String suffixeAleatoire = String.format("%05d", nombreAleatoire);
      this.Matricule = "MAT" + anneeActuelle + suffixeAleatoire;
   }

   // =========================================================================
   //  MÉTHODES REQUISES PAR LE CONTRÔLEUR FICHEELEVE
   // =========================================================================

   /**
    * Récupère dynamiquement l'adresse complète depuis l'objet Parent
    */
   public String getAdresse() {
      if (this.parent != null && this.parent.getAdresse() != null) {
         String quartier = this.parent.getAdresseLibelle();
         String ville = this.parent.getAdresseVille();
         if (ville != null && !ville.isEmpty()) {
            return quartier + ", " + ville;
         }
         return quartier;
      }
      return "";
   }

   /**
    * Extrait la moyenne générale depuis l'objet Bulletin relié à l'élève
    */
   public double getMoyenne() {
      return (this.bulletinEleve != null) ? this.bulletinEleve.getMoyenneGenerale() : 0.0;
   }

   /**
    * Extrait le nombre total d'absences depuis l'objet Inscription ou Bulletin
    */
   public int getNombreAbsences() {
      return  0;
   }

   /**
    * Extrait la date d'inscription depuis le suivi administratif
    */
   public String getDateInscription() {
      return (this.inscription != null) ? this.inscription.getDateInscription() : null;
   }

   /**
    * Récupère la profession textuelle du parent référencé
    */
   public String getProfessionParent() {
      return (this.parent != null) ? this.parent.getProfessionLibelle() : "";
   }

   /**
    * Récupère le contact téléphonique numérique du parent
    */
   public String getTelephoneParent() {
      return (this.parent != null && this.parent.getContactParent() != 0)
              ? String.valueOf(this.parent.getContactParent())
              : "";
   }

   /**
    * Récupère l'adresse de messagerie électronique du parent
    */
   public String getEmailParent() {
      return (this.parent != null) ? this.parent.getEmailParent() : "";
   }

   /**
    * Extrait le libellé textuel du lieu de naissance pour l'affichage
    */
   public LieuNaissance getLieuNaissance() {
      return this.lieuNaissance;
   }

   // =========================================================================
   //  GETTERS ET SETTERS EXISTANTS & AJUSTÉS
   // =========================================================================

   public String getParent() {
      return (this.parent != null) ? this.parent.getIdParent() : null;
   }

   public String getIdParent() {
      return (this.parent != null) ? this.parent.getIdParent() : null;
   }

   public String getMatricule() {
      return this.Matricule;
   }

   public String getNomParent(){
      return (this.parent != null) ? this.parent.getPrenom() : "";
   }

   public void setMatricule(String matricule) {
      this.Matricule = matricule;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String nom) {
      this.nom = nom;
   }

   public String getPrenom() {
      return this.prenom;
   }

   public void setPrenom(String prenom) {
      this.prenom = prenom;
   }

   public Date getDateNaissance() {
      return this.dateNaissance;
   }

   public void setDateNaissance(Date dateNaissance) {
      this.dateNaissance = dateNaissance;
   }

   public void setLieuNaissance(LieuNaissance lieuNaissance) {
      this.lieuNaissance = lieuNaissance;
   }

   public void setLieuNaissance(String lieuNaissance) {
      if (lieuNaissance != null && !lieuNaissance.isEmpty()) {
         try {
            this.lieuNaissance = LieuNaissance.valueOf(lieuNaissance);
         } catch (IllegalArgumentException e) {
            for (LieuNaissance l : LieuNaissance.values()) {
               if (l.getLibelle().equalsIgnoreCase(lieuNaissance)) {
                  this.lieuNaissance = l;
                  return;
               }
            }
            this.lieuNaissance = LieuNaissance.AUTRE;
         }
      } else {
         this.lieuNaissance = LieuNaissance.AUTRE;
      }
   }

   public String getLieuNaissanceLibelle() {
      return this.lieuNaissance != null ? this.lieuNaissance.getLibelle() : "";
   }

   public Sexe getSexe() {
      return this.sexe;
   }

   public void setSexe(Sexe sexeUpdate) {
      this.sexe = sexeUpdate;
   }

   public void setSexe(String sexe) {
      if (sexe != null && !sexe.isEmpty()) {
         try {
            this.sexe = Sexe.valueOf(sexe);
         } catch (IllegalArgumentException e) {
            this.sexe = Sexe.MASCULIN;
         }
      } else {
         this.sexe = Sexe.MASCULIN;
      }
   }

   public Nationnalite getNationalite() {
      return this.nationalite;
   }

   public void setNationalite(Nationnalite nationalite) {
      this.nationalite = nationalite;
   }

   public void setNationalite(String nationalite) {
      if (nationalite != null && !nationalite.isEmpty()) {
         try {
            this.nationalite = Nationnalite.valueOf(nationalite);
         } catch (IllegalArgumentException e) {
            this.nationalite = Nationnalite.AUTRE;
         }
      } else {
         this.nationalite = Nationnalite.AUTRE;
      }
   }

   public String getNationnalite() {
      return this.nationalite != null ? this.nationalite.getLibelle() : "";
   }

   public void setNationnalite(String nationnalite) {
      setNationalite(nationnalite);
   }

   public String getPhoto() {
      return this.photo;
   }

   public void setPhoto(String photo) {
      this.photo = photo;
   }

   public boolean getATerminerPension() {
      return this.aTerminerPension;
   }

   public void setaTerminerPension(boolean aTerminerPension) {
      this.aTerminerPension = aTerminerPension;
   }

   public Region getRegionOrigine() {
      return regionOrigine;
   }

   public void setRegionOrigine(Region regionOrigine) {
      this.regionOrigine = regionOrigine;
   }

   public void setRegionOrigine(String regionOrigine) {
      if (regionOrigine != null && !regionOrigine.isEmpty()) {
         try {
            this.regionOrigine = Region.valueOf(regionOrigine);
         } catch (IllegalArgumentException e) {
            this.regionOrigine = Region.AUTRE;
         }
      } else {
         this.regionOrigine = Region.AUTRE;
      }
   }

   public String getRegionOrigineLibelle() {
      return this.regionOrigine != null ? this.regionOrigine.getLibelle() : "";
   }

   public String getAntecedentsMedicaux() {
      return antecedentsMedicaux;
   }

   public void setAntecedentsMedicaux(String antecedentsMedicaux) {
      this.antecedentsMedicaux = antecedentsMedicaux;
   }

   public void regenererMatricule() {
      int anneeActuelle = java.time.Year.now().getValue();
      int nombreAleatoire = random.nextInt(100000);
      this.Matricule = "MAT" + anneeActuelle + String.format("%05d", nombreAleatoire);
   }

   /**
    * CORRECTION : Ajout d'un espace de séparation entre le nom et le prénom
    */
   public String getFullName(){
      return this.nom + " " + this.prenom;
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