package com.digitalprimaryschool.digitalprimaryschool.model;

// Enum pour le mode de paiement
public enum ModePaiement {
   ESPECES("Espèces"),
   CHEQUE("Chèque"),
   VIREMENT("Virement bancaire"),
   MOBILE_MONEY("Mobile Money"),
   CARTE_BANCAIRE("Carte bancaire"),
   TRANSFERT("Transfert"),
   AUTRE("Autre");

   private final String libelle;

   ModePaiement(String libelle) {
      this.libelle = libelle;
   }

   public String getLibelle() {
      return libelle;
   }

   @Override
   public String toString() {
      return libelle;
   }
}
