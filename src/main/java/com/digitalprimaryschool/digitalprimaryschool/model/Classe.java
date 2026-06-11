package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Classe {
   private String idClasse;
   private String nom;
   private NiveauClasse niveau;
   private int capaciteMax;
   private SectionClass section;
   
   public TarisScolaire tarisScolaire;
   public Collection<Matiere> matiere;

   public Classe(){
      super();
      this.idClasse = UUID.randomUUID().toString();
   }

   public String getIdClasse() { return idClasse; }
   public void setIdClasse(String idClasse) { this.idClasse = idClasse; }

   public String getNom() { return nom; }
   public void setNom(String nom) { this.nom = nom; }

   public NiveauClasse getNiveau() { return niveau; }
   public void setNiveau(NiveauClasse niveau) { this.niveau = niveau; }

   public int getCapaciteMax() { return capaciteMax; }
   public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

   public SectionClass getSection() { return section; }
   public void setSection(SectionClass section) { this.section = section; }

   public String getIdTarifScolaire() { return tarisScolaire.getIdTarifScolaire(); }

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