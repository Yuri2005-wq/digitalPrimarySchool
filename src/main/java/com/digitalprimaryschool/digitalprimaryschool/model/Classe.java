package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Classe {
   private String idClasse;
   private String nom;
   private NiveauClasse niveau;
   private int capaciteMax;
   private SectionClass section;
   private CategoryClasse categorieClasse;
   public Enseignant enseignant;

   public Inscription inscription;
   public TarisScolaire tarisScolaire;
   public Collection<Matiere> matiere;


   public Classe(){
      super();
      this.idClasse = UUID.randomUUID().toString();
   }

   public String getIdClasse() { return idClasse; }
   public void setIdClasse(String idClasse) { this.idClasse = idClasse; }
   public String getnombreEleve(){
      return "0";
   }
   public String getNom() { return nom; }
   public void setNom(String nom) { this.nom = nom; }

   public NiveauClasse getNiveau() { return niveau; }
   public void setNiveau(NiveauClasse niveau) { this.niveau = niveau; }

   public Enseignant getEnseignant(){
      return this.enseignant;
   }
   public String getEnseignantNom(){
      return this.enseignant != null ? enseignant.getFullName() : "Aucun Enseignant Affecté a cette Classe";
   }
   // Setter pour NiveauClasse (prend un String et le convertit en enum)
   public void setNiveau(String niveau) {
      if (niveau != null && !niveau.isEmpty()) {
         try {
            this.niveau = NiveauClasse.valueOf(niveau);
         } catch (IllegalArgumentException e) {
            System.err.println("Valeur de niveau inconnue : " + niveau);
         }
      }
   }

   // Setter pour NiveauClasse (prend le libellé)
   public void setNiveauParLibelle(String libelle) {
      if (libelle != null && !libelle.isEmpty()) {
         for (NiveauClasse n : NiveauClasse.values()) {
            if (n.getLibelle().equalsIgnoreCase(libelle)) {
               this.niveau = n;
               return;
            }
         }
      }
   }

   public int getCapaciteMax() { return capaciteMax; }
   public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

   public SectionClass getSection() { return section; }
   public void setSection(SectionClass section) { this.section = section; }

   // Setter pour SectionClass (prend un String et le convertit en enum)
   public void setSection(String section) {
      if (section != null && !section.isEmpty()) {
         try {
            this.section = SectionClass.valueOf(section);
         } catch (IllegalArgumentException e) {
            System.err.println("Valeur de section inconnue : " + section);
         }
      }
   }

   public CategoryClasse getCategorieClasse(){
      return this.categorieClasse;
   }

   public void setCategorieClasse(CategoryClasse categorieClasse) {
      this.categorieClasse = categorieClasse;
   }

   // Setter pour CategoryClasse (prend un String et le convertit en enum)
   public void setCategorieClasse(String categorieClasse) {
      if (categorieClasse != null && !categorieClasse.isEmpty()) {
         try {
            this.categorieClasse = CategoryClasse.valueOf(categorieClasse);
         } catch (IllegalArgumentException e) {
            System.err.println("Valeur de catégorie inconnue : " + categorieClasse);
         }
      }
   }

   // Setter pour CategoryClasse (prend le libellé)
   public void setCategorieClasseParLibelle(String libelle) {
      if (libelle != null && !libelle.isEmpty()) {
         for (CategoryClasse c : CategoryClasse.values()) {
            if (c.getLibelle().equalsIgnoreCase(libelle)) {
               this.categorieClasse = c;
               return;
            }
         }
      }
   }

   // Méthode pour obtenir le libellé de la catégorie
   public String getCategorieClasseLibelle() {
      return this.categorieClasse != null ? this.categorieClasse.getLibelle() : "";
   }

   public String getIdTarifScolaire() {
      return tarisScolaire != null ? tarisScolaire.getIdTarifScolaire() : null;
   }

   public void getEleveNonEnRegle() {
      // TODO: implement
   }

   public void getMoyenneGenerale() {
      // TODO: implement
   }

   public void getClassement() {
      // TODO: implement
   }

   public char getProfTitulaire() {
      // TODO: implement
      return 0;
   }

   public double totalFraisClasse() {
      // TODO: implement
      return 0;
   }


   public TarisScolaire getTarisScolaire() {
      return tarisScolaire;
   }

   public void setTarisScolaire(TarisScolaire newTarisScolaire) {
      this.tarisScolaire = newTarisScolaire;
   }

   public Collection<Matiere> getMatiere() {
      if (matiere == null)
         matiere = new HashSet<Matiere>();
      return matiere;
   }

   public Iterator getIteratorMatiere() {
      if (matiere == null)
         matiere = new HashSet<Matiere>();
      return matiere.iterator();
   }


   public void setMatiere(Collection<Matiere> newMatiere) {
      removeAllMatiere();
      for (Iterator iter = newMatiere.iterator(); iter.hasNext();)
         addMatiere((Matiere)iter.next());
   }


   public void addMatiere(Matiere newMatiere) {
      if (newMatiere == null)
         return;
      if (this.matiere == null)
         this.matiere = new HashSet<Matiere>();
      if (!this.matiere.contains(newMatiere))
         this.matiere.add(newMatiere);
   }


   public void removeMatiere(Matiere oldMatiere) {
      if (oldMatiere == null)
         return;
      if (this.matiere != null)
         if (this.matiere.contains(oldMatiere))
            this.matiere.remove(oldMatiere);
   }

   public void removeAllMatiere() {
      if (matiere != null)
         matiere.clear();
   }

}