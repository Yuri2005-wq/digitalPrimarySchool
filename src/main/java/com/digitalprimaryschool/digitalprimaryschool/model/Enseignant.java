/***********************************************************************
 * Module:  Enseignant.java
 * Author:  DJALEU YURI
 * Purpose: Defines the Class Enseignant
 ***********************************************************************/

import java.util.*;

/** @pdOid 08113053-fd46-42f3-82e8-86df0c920140 */
public class Enseignant {
   /** @pdOid 3a900d3d-5fdb-4be1-af18-58ab44184962 */
   private String idEnseignant;
   /** @pdOid 0ad57780-fbbc-453b-ae58-41cf6dec9126 */
   private String nom;
   /** @pdOid 25134fea-6dc2-4d04-b0fe-8948a1caa5dc */
   private String prenom;
   /** @pdOid b50e29e1-98b6-4380-818c-07e5593044df */
   private int contactEnseignant;
   /** @pdOid 34e93552-4783-4694-ad96-35ac97bc49f9 */
   private String qualification;
   /** @pdOid 0723f666-61e6-425f-ae76-98e3087a1901 */
   private String grade;
   
   /** @pdRoleInfo migr=no name=Classe assc=Association_3 coll=java.util.Collection impl=java.util.HashSet mult=1 */
   public Classe classe;
   
   /** @pdOid f6f2c5aa-fdc5-4553-8236-6055dcf97887 */
   public void getClassesDirigee() {
      // TODO: implement
   }
   
   /** @pdOid 6bb087ed-da3c-411e-84ea-82c294bd634f */
   public void getClassesEnseigne() {
      // TODO: implement
   }
   
   /** @pdOid db25a321-5f0a-4d05-9375-ebd8eb7dba24 */
   public void saisirNote() {
      // TODO: implement
   }
   
   
   /** @pdGenerated default parent getter */
   public Classe getClasse() {
      return classe;
   }
   
   /** @pdGenerated default parent setter
     * @param newClasse */
   public void setClasse(Classe newClasse) {
      this.classe = newClasse;
   }

}