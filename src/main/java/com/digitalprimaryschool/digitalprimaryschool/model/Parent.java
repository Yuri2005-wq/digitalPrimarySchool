package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Parent extends BaseModel {
   private String idParent;
   private String prenom;
   private int contactParent;
   private String emailParent;
   private Profession profession; // CHANGÉ : String → Profession
   private Quartier adresse;
   private String dateCreation;

   public Collection<Eleve> eleve;

   public Parent(){
      super();
      this.idParent = UUID.randomUUID().toString();
      this.dateCreation = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
   }

   public Parent(String idParent, String prenom, int contactParent, String emailParent, Profession profession, Quartier adresse){
      this.idParent = idParent;
      this.prenom = prenom;
      this.contactParent = contactParent;
      this.emailParent = emailParent;
      this.profession = profession;
      this.adresse = adresse;
      this.dateCreation = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
   }

   // Getters
   public String getIdParent(){
      return this.idParent;
   }

   public String getPrenom() {
      return this.prenom;
   }

   public int getContactParent() {
      return this.contactParent;
   }

   public String getEmailParent() {
      return this.emailParent;
   }

   public Profession getProfession() {
      return this.profession;
   }

   public String getProfessionLibelle() {
      return this.profession != null ? this.profession.getLibelle() : "";
   }

   public Quartier getAdresse() {
      return this.adresse;
   }

   public String getAdresseLibelle() {
      return this.adresse != null ? this.adresse.getLibelle() : "";
   }

   public String getAdresseVille() {
      return this.adresse != null ? this.adresse.getVille() : "";
   }

   public String getAdresseRegion() {
      return this.adresse != null ? this.adresse.getRegion() : "";
   }

   public String getDateCreation() {
      return dateCreation;
   }

   // Setters
   public void setPrenom(String prenom) {
      this.prenom = prenom;
   }

   public void setContactParent(int contactParent) {
      this.contactParent = contactParent;
   }

   public void setEmailParent(String emailParent) {
      this.emailParent = emailParent;
   }

   public void setProfession(Profession profession) {
      this.profession = profession;
   }

   public void setProfession(String profession) {
      if (profession != null && !profession.isEmpty()) {
         try {
            this.profession = Profession.valueOf(profession);
         } catch (IllegalArgumentException e) {
            // Si la valeur ne correspond pas à un enum, chercher par libellé
            for (Profession p : Profession.values()) {
               if (p.getLibelle().equalsIgnoreCase(profession)) {
                  this.profession = p;
                  return;
               }
            }
            this.profession = Profession.AUTRE;
         }
      } else {
         this.profession = Profession.AUTRE;
      }
   }

   public void setAdresse(Quartier adresse) {
      this.adresse = adresse;
   }

   public void setAdresse(String adresse) {
      if (adresse != null && !adresse.isEmpty()) {
         try {
            this.adresse = Quartier.valueOf(adresse);
         } catch (IllegalArgumentException e) {
            // Chercher par libellé
            for (Quartier q : Quartier.values()) {
               if (q.getLibelle().equalsIgnoreCase(adresse)) {
                  this.adresse = q;
                  return;
               }
            }
            this.adresse = null;
         }
      } else {
         this.adresse = null;
      }
   }

   public void setAdresseParLibelle(String libelle) {
      if (libelle != null && !libelle.isEmpty()) {
         for (Quartier q : Quartier.values()) {
            if (q.getLibelle().equalsIgnoreCase(libelle)) {
               this.adresse = q;
               return;
            }
         }
      }
      this.adresse = null;
   }

   public void setDateCreation(String dateCreation) {
      this.dateCreation = dateCreation;
   }

   // --- Gestion de la collection d'enfants ---
   public int nbrEnfantDansEcole(){
      return eleve != null ? eleve.size() : 0;
   }

   public Collection<Eleve> getEleve() {
      if (eleve == null)
         eleve = new HashSet<Eleve>();
      return eleve;
   }

   public Iterator<Eleve> getIteratorEleve() {
      if (eleve == null)
         eleve = new HashSet<Eleve>();
      return eleve.iterator();
   }

   public void setEleve(Collection<Eleve> newEleve) {
      removeAllEleve();
      for (Eleve e : newEleve) addEleve(e);
   }

   public void addEleve(Eleve newEleve) {
      if (newEleve == null) return;
      if (this.eleve == null) this.eleve = new HashSet<Eleve>();
      if (!this.eleve.contains(newEleve)) this.eleve.add(newEleve);
   }

   public void removeEleve(Eleve oldEleve) {
      if (oldEleve == null) return;
      if (this.eleve != null && this.eleve.contains(oldEleve)) this.eleve.remove(oldEleve);
   }

   public void removeAllEleve() {
      if (eleve != null) eleve.clear();
   }
}