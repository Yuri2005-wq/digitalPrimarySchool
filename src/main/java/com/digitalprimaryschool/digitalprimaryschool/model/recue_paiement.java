package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class recue_paiement extends DocumentScolaire {
   private String idRecu;
   private String idPaiement;

   public recue_paiement(){
      super();
      this.setType("recu");
      this.idRecu = UUID.randomUUID().toString();
   }

   public String getIdRecu() { return idRecu; }
   public void setIdRecu(String idRecu) { this.idRecu = idRecu; }

   public String getIdPaiement() { return idPaiement; }
   public void setIdPaiement(String idPaiement) { this.idPaiement = idPaiement; }
}