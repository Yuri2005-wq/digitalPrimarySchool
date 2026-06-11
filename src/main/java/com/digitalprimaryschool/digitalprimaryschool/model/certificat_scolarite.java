package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class certificat_scolarite extends DocumentScolaire {
   private String idCertificat;

   public certificat_scolarite(){
      super();
      this.setType("certif");
      this.idCertificat = UUID.randomUUID().toString();
   }

   public String getIdCertificat() { return idCertificat; }
   public void setIdCertificat(String idCertificat) { this.idCertificat = idCertificat; }
}