package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Parent extends BaseModel {
   private String idParent;
   private String prenom; // Corrigé de char à String
   private int contactParent;
   private String emailParent; // Corrigé de char à String
   private String profession;  // Corrigé de char à String
   private String adresse;     // Corrigé de char à String

   public Collection<Eleve> eleve;

   // Constructeur
   public Parent(){
      super(); // Appelle le constructeur d'Utilisateur
      this.idParent = UUID.randomUUID().toString(); // Génération sécurisée hors-ligne
   }
   public Parent(String idParent, String prenom, int contactParent, String emailParent, String profession, String adresse){
      this.idParent = idParent;
      this.prenom = prenom;
      this.contactParent = contactParent;
      this.emailParent = emailParent;
      this.profession = profession;
      this.adresse = adresse;
   }

   // Getters requis par le DAO et l'Élève
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
   public String getProfession() {
      return this.profession;
   }
   public String getAdresse() {
      return this.adresse;
   }

   // Setters (Utiles pour ton formulaire JavaFX)
   public void setPrenom(String prenom) { this.prenom = prenom; }
   public void setContactParent(int contactParent) { this.contactParent = contactParent; }
   public void setEmailParent(String emailParent) { this.emailParent = emailParent; }
   public void setProfession(String profession) { this.profession = profession; }
   public void setAdresse(String adresse) { this.adresse = adresse; }

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