package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class fiche_renseignement extends DocumentScolaire {
   private String idficheRenseignement;

   public fiche_renseignement(){
      super();
      this.setType("fiche");
      this.idficheRenseignement = UUID.randomUUID().toString();
   }

   public String getIdficheRenseignement() { return idficheRenseignement; }
   public void setIdficheRenseignement(String idficheRenseignement) { this.idficheRenseignement = idficheRenseignement; }
}