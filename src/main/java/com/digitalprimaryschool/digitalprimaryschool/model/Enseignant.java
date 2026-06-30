package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class Enseignant {
   private String idEnseignant;
   private String idEcole; // Corrigé en String (UUID) pour le cloisonnement Multi-Écoles
   private String idUtilisateur; // Clé optionnelle reliant cet enseignant à ses identifiants
   private String nom;
   private String prenom;
   private String contactEnseignant; // Changé en String pour supporter les caractères (+237)
   private String qualification;
   private String grade;
   public Classe classe;
   private String photo;

   public Enseignant(){
      this.idEnseignant = UUID.randomUUID().toString();
   }

   // --- Getters & Setters ---
   public String getIdEcole() { return idEcole; }
   public void setIdEcole(String idEcole) { this.idEcole = idEcole; }

   public String getIdUtilisateur() { return idUtilisateur; }
   public void setIdUtilisateur(String idUtilisateur) { this.idUtilisateur = idUtilisateur; }

   public String getPhoto() { return this.photo; }
   public void setPhoto(String photo) { this.photo = photo; }
   public String getIdEnseignant() { return idEnseignant; }
   public void setIdEnseignant(String idEnseignant) { this.idEnseignant = idEnseignant; }

   public String getNom() { return nom; }
   public void setNom(String nom) { this.nom = nom; }

   public String getPrenom() { return prenom; }
   public void setPrenom(String prenom) { this.prenom = prenom; }

   public String getContactEnseignant() { return contactEnseignant; }
   public void setContactEnseignant(String contactEnseignant) { this.contactEnseignant = contactEnseignant; }

   public String getQualification() { return qualification; }
   public void setQualification(String qualification) { this.qualification = qualification; }

   public String getGrade() { return grade; }
   public void setGrade(String grade){ this.grade = grade; }

   public Classe getClasse() { return classe; }
   public void setClasse(Classe newClasse) { this.classe = newClasse; }

   public String getFullName(){
      return this.nom + " " + this.prenom;
   }
}