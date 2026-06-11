package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Enseignant {
   private String idEnseignant;
   private String nom;
   private String prenom;
   private int contactEnseignant;
   private String qualification;
   private String grade;
   
   public Classe classe;
   public Enseignant(){
      this.idEnseignant = UUID.randomUUID().toString();
   }
   public void getClassesDirigee() {
      // TODO: implement
   }
   
   public void getClassesEnseigne() {
      // TODO: implement
   }
   
   public void saisirNote() {
      // TODO: implement
   }
   public String getIdEnseignant() { return idEnseignant; }
   public void setIdEnseignant(String idEnseignant) { this.idEnseignant = idEnseignant; }

   //public int getIdClasse() { return idClasse; }
   //public void setIdClasse(int idClasse) { this.idClasse = idClasse; }

   public String getNom() { return nom; }
   public void setNom(String nom) { this.nom = nom; }

   public String getPrenom() { return prenom; }
   public void setPrenom(String prenom) { this.prenom = prenom; }

   public int getContactEnseignant() { return contactEnseignant; }
   public void setContactEnseignant(int contactEnseignant) { this.contactEnseignant = contactEnseignant; }

   public String getQualification() { return qualification; }
   public void setQualification(String qualification) { this.qualification = qualification; }

   public String getGrade() { return grade; }
   public void setGrade(String grade){
       this.grade = grade;
   }
   
   public Classe getClasse() {
      return classe;
   }
   public void setClasse(Classe newClasse) {
      this.classe = newClasse;
   }


}