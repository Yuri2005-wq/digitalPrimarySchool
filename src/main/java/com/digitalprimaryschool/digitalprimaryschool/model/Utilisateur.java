package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Utilisateur extends BaseModel {
   private String idUtilisateur;
   private String username;
   private String motDePasseUtilisateur;
   private Boolean isLogged;
   private String role;
   private String idEcole;

   // Champs requis par le nouveau schéma pour la synchronisation
   private int isSynced;
   private String derniereModification;

   public Utilisateur(){
      super();
      this.idUtilisateur = UUID.randomUUID().toString();
      this.isLogged = false;
      this.isSynced = 0;
   }

   // --- Getters & Setters ---
   public String getIdUtilisateur() { return idUtilisateur; }
   public void setIdUtilisateur(String idUtilisateur) { this.idUtilisateur = idUtilisateur; }

   public String getUsername() { return username; }
   public void setUsername(String username) { this.username = username; }

   public String getMotDePasseUtilisateur() { return motDePasseUtilisateur; }
   public void setMotDePasseUtilisateur(String motDePasseUtilisateur) { this.motDePasseUtilisateur = motDePasseUtilisateur; }

   public Boolean getIsLogged() { return isLogged; }
   public void setIsLogged(Boolean isLogged) { this.isLogged = isLogged; }

   public String getRole() { return role; }
   public void setRole(String role) { this.role = role; }

   public String getIdEcole() { return idEcole; }
   public void setIdEcole(String idEcole) { this.idEcole = idEcole; }

   public int getIsSynced() { return isSynced; }
   public void setIsSynced(int isSynced) { this.isSynced = isSynced; }

   public String getDerniereModification() { return derniereModification; }
   public void setDerniereModification(String derniereModification) { this.derniereModification = derniereModification; }

   // --- Méthodes métiers ---
   public void seConnecter() { this.isLogged = true; }
   public void seDeconnecter() { this.isLogged = false; }
}