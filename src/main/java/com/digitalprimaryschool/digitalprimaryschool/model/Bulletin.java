package com.digitalprimaryschool.digitalprimaryschool.model;

import java.util.*;

public class Bulletin extends DocumentScolaire {
   private String idBulletin;

   public Bulletin(){
      super();
      this.idBulletin = UUID.randomUUID().toString();
   }

   public String getIdBulletin() { return idBulletin; }
   public void setIdBulletin(String idBulletin) { this.idBulletin = idBulletin; }
}