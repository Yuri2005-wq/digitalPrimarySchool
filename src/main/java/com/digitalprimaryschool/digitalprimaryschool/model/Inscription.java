package com.digitalprimaryschool.digitalprimaryschool.model;
import java.util.*;

public class Inscription {
   private String idInscription;
   private Eleve eleve;
   private Classe classes;
   private double montantPayer;
   private boolean estReinscript;
   private Date dateInscription;
   
   public Collection<Classe> classe;
   public Collection<AnneeScolaire> anneeScolaire;
   public Paiement paiement;
   public Inscription(){
      this.idInscription = UUID.randomUUID().toString();

   }




   public String getIdInscription() { return idInscription; }
   public void setIdInscription(String idInscription) { this.idInscription = idInscription; }

   public String getMatriculeEleve() { return eleve.getMatricule(); }

   public String getIdClasses(){
      return this.classes.getIdClasse();
   }
   public double getMontantPayer() { return montantPayer; }
   public void setMontantPayer(double montantPayer) { this.montantPayer = montantPayer; }


   public boolean isEstReinscript() { return estReinscript; }
   public void setEstReinscript(boolean estReinscript) { this.estReinscript = estReinscript; }

   public Date getDateInscription() { return dateInscription; }
   public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }





   public void genererBordereau() {
      // TODO: implement
   }
   
   public void valider() {
      // TODO: implement
   }
   
   public void imprimerRecusInscription() {
      // TODO: implement
   }
   
   
   public Collection<Classe> getClasse() {
      if (classe == null)
         classe = new HashSet<Classe>();
      return classe;
   }
   
   public Iterator getIteratorClasse() {
      if (classe == null)
         classe = new HashSet<Classe>();
      return classe.iterator();
   }
   
   public void setClasse(Collection<Classe> newClasse) {
      removeAllClasse();
      for (Iterator iter = newClasse.iterator(); iter.hasNext();)
         addClasse((Classe)iter.next());
   }
   

   public void addClasse(Classe newClasse) {
      if (newClasse == null)
         return;
      if (this.classe == null)
         this.classe = new HashSet<Classe>();
      if (!this.classe.contains(newClasse))
         this.classe.add(newClasse);
   }
   

   public void removeClasse(Classe oldClasse) {
      if (oldClasse == null)
         return;
      if (this.classe != null)
         if (this.classe.contains(oldClasse))
            this.classe.remove(oldClasse);
   }
   
   public void removeAllClasse() {
      if (classe != null)
         classe.clear();
   }
   public Collection<AnneeScolaire> getAnneeScolaire() {
      if (anneeScolaire == null)
         anneeScolaire = new HashSet<AnneeScolaire>();
      return anneeScolaire;
   }
   
   public Iterator getIteratorAnneeScolaire() {
      if (anneeScolaire == null)
         anneeScolaire = new HashSet<AnneeScolaire>();
      return anneeScolaire.iterator();
   }

   public void setAnneeScolaire(Collection<AnneeScolaire> newAnneeScolaire) {
      removeAllAnneeScolaire();
      for (Iterator iter = newAnneeScolaire.iterator(); iter.hasNext();)
         addAnneeScolaire((AnneeScolaire)iter.next());
   }
   

   public void addAnneeScolaire(AnneeScolaire newAnneeScolaire) {
      if (newAnneeScolaire == null)
         return;
      if (this.anneeScolaire == null)
         this.anneeScolaire = new HashSet<AnneeScolaire>();
      if (!this.anneeScolaire.contains(newAnneeScolaire))
         this.anneeScolaire.add(newAnneeScolaire);
   }
   

   public void removeAnneeScolaire(AnneeScolaire oldAnneeScolaire) {
      if (oldAnneeScolaire == null)
         return;
      if (this.anneeScolaire != null)
         if (this.anneeScolaire.contains(oldAnneeScolaire))
            this.anneeScolaire.remove(oldAnneeScolaire);
   }
   
   public void removeAllAnneeScolaire() {
      if (anneeScolaire != null)
         anneeScolaire.clear();
   }
   public Paiement getPaiement() {
      return paiement;
   }
   

   public void setPaiement(Paiement newPaiement) {
      this.paiement = newPaiement;
   }

}