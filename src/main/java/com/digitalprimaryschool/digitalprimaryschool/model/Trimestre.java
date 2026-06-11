package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Trimestre{
   private String idTrimestre;
   private String libelle;
   private Date dateDebut;
   private Date dateFin;
   private boolean estClos;
   private AnneeScolaire idAnneeScolaire;
   
   public Collection<Sequence> sequence;
   public Bulletin bulletin;

   public Trimestre(){
      this.idTrimestre = UUID.randomUUID().toString();
   }
   public double calculerMoyenne() {
      // TODO: implement
      return 0;
   }
   
   public void genererBulletin() {
      // TODO: implement
   }
   
   public void cloturer() {
      // TODO: implement
   }
   public String getIdTrimestre() { return idTrimestre; }
   public void setIdTrimestre(String idTrimestre) { this.idTrimestre = idTrimestre; }

   public String getLibelle() { return libelle; }
   public void setLibelle(String libelle) { this.libelle = libelle; }

   public Date getDateDebut() { return dateDebut; }
   public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

   public Date getDateFin() { return dateFin; }
   public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

   public boolean isEstClos() { return estClos; }
   public void setEstClos(boolean estClos) { this.estClos = estClos; }

   public String getIdAnneeScolaire() { return idAnneeScolaire.getIdAnnescolaire(); }











   public Collection<Sequence> getSequence() {
      if (sequence == null)
         sequence = new HashSet<Sequence>();
      return sequence;
   }
   
   public Iterator getIteratorSequence() {
      if (sequence == null)
         sequence = new HashSet<Sequence>();
      return sequence.iterator();
   }
   
   public void setSequence(Collection<Sequence> newSequence) {
      removeAllSequence();
      for (Iterator iter = newSequence.iterator(); iter.hasNext();)
         addSequence((Sequence)iter.next());
   }

   public void addSequence(Sequence newSequence) {
      if (newSequence == null)
         return;
      if (this.sequence == null)
         this.sequence = new HashSet<Sequence>();
      if (!this.sequence.contains(newSequence))
         this.sequence.add(newSequence);
   }

   public void removeSequence(Sequence oldSequence) {
      if (oldSequence == null)
         return;
      if (this.sequence != null)
         if (this.sequence.contains(oldSequence))
            this.sequence.remove(oldSequence);
   }

   public void removeAllSequence() {
      if (sequence != null)
         sequence.clear();
   }
   public Bulletin getBulletin() {
      return bulletin;
   }
   

   public void setBulletin(Bulletin newBulletin) {
      this.bulletin = newBulletin;
   }

}