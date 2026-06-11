package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Notes{
   private String idNote;
   private double valeur;
   private int noteMax;
   private String observation;
   private Date dateSaisie;
   private String saisirPar;
   public Eleve eleve;
   public Sequence sequence;
   public Matiere matiere;

   public Notes(){
      this.idNote = UUID.randomUUID().toString();
   }

   public String getIdNote() { return idNote; }
   public void setIdNote(String idNote) { this.idNote = idNote; }

   public double getValeur() { return valeur; }
   public void setValeur(double valeur) { this.valeur = valeur; }

   public int getNoteMax() { return noteMax; }
   public void setNoteMax(int noteMax) { this.noteMax = noteMax; }

   public String getObservation() { return observation; }
   public void setObservation(String observation) { this.observation = observation; }


   public String getSaisirPar() { return saisirPar; }
   public void setSaisirPar(String saisirPar) { this.saisirPar = saisirPar; }


   public String getMatriculeEleve() { return eleve.getMatricule(); }

   public String getIdSequence() { return sequence.getIdSequence(); }

   public String getIdMatiere() { return matiere.getIdMatiere(); }












   public void calculerPointPondere() {
      // TODO: implement
   }
   
   public void modifier() {
      // TODO: implement
   }
   
   public void historiser() {
      // TODO: implement
   }

   public Eleve getEleve() {

      return eleve;
   }
   

   public void setEleve(Eleve newEleve) {
      this.eleve = newEleve;
   }

   public Sequence getSequence() {
      return sequence;
   }
   

   public void setSequence(Sequence newSequence) {

      this.sequence = newSequence;
   }

}