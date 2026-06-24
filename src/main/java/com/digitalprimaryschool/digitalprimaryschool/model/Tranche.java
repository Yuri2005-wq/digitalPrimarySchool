//package com.digitalprimaryschool.digitalprimaryschool.model;
//
//import java.util.*;
//
//public class Tranche {
//   private String idTranche;
//   private String libelleTranche;
//   private Double montantTranche;
//   private Date dateEcheance;
//   private boolean estPaye;
//
//
//   //Constructeur
//   public Tranche(){
//      this.idTranche = UUID.randomUUID().toString();
//   }
//   public int calculerRetard() {
//      // TODO: implement
//      return 0;
//   }
//
//}

package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.UUID;

public class Tranche extends BaseModel {
   private String idTranche;
   private String idTarifScolaire;
   private String libelleTranche;
   private Double montantTranche;
   private String dateEcheance;
   private boolean estPaye;
   private String idPaiement;

   public Tranche(){
      super();
      this.idTranche = UUID.randomUUID().toString();
   }
   public String getIdTarifScolaire() { return idTarifScolaire; }
   public void setIdTarifScolaire(String idTarifScolaire) { this.idTarifScolaire = idTarifScolaire; }
   public String getIdTranche() { return idTranche; }
   public void setIdTranche(String idTranche) { this.idTranche = idTranche; }

   public String getLibelleTranche() { return libelleTranche; }
   public void setLibelleTranche(String libelleTranche) { this.libelleTranche = libelleTranche; }

   public Double getMontantTranche() { return montantTranche; }
   public void setMontantTranche(Double montantTranche) { this.montantTranche = montantTranche; }

   public String getDateEcheance() { return dateEcheance; }
   public void setDateEcheance(String dateEcheance) { this.dateEcheance = dateEcheance; }

   public boolean isEstPaye() { return estPaye; }
   public void setEstPaye(boolean estPaye) { this.estPaye = estPaye; }

   public String getIdPaiement() { return idPaiement; }
   public void setIdPaiement(String idPaiement) { this.idPaiement = idPaiement; }
}