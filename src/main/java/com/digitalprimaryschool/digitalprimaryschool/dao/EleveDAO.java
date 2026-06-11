package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Insère un élève. Si le matricule existe déjà (rare en UUID mais possible si personnalisé),
     * il est régénéré automatiquement.
     * * @param eleve L'objet élève à insérer
     * @throws SQLException Si une autre erreur SQL stricte survient (ex: contrainte de clé étrangère parent)
     */
    public void insert(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO Eleve (matriculeEleve, idParent, nom, prenom, dateNaissance, lieuNaissance, sexe, nationnalite, photo, aTerminerPension, is_synced) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";

        boolean insertReussie = false;
        int tentatives = 0;

        while (!insertReussie) {
            // Utilisation de la connexion centralisée de ton application
            try (Connection conn = Database.getConnexion();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, eleve.getMatricule());
                pstmt.setString(2, eleve.getIdParent());
                pstmt.setString(3, eleve.getNom());
                pstmt.setString(4, eleve.getPrenom());
                // Correction du bug de Date : conversion sécurisée de java.util.Date à java.sql.Date
                if (eleve.getDateNaissance() != null) {
                    pstmt.setDate(5, new java.sql.Date(eleve.getDateNaissance().getTime()));
                } else {
                    pstmt.setNull(5, java.sql.Types.DATE);
                }

                pstmt.setString(6, eleve.getLieuNaissance());
                pstmt.setString(7, eleve.getSexe());
                pstmt.setString(8, eleve.getNationnalite());
                pstmt.setString(9, eleve.getPhoto());

                // Gestion du boolean pour SQLite (0 ou 1)
                pstmt.setInt(10, eleve.getATerminerPension() ? 1 : 0);

                pstmt.executeUpdate();
                insertReussie = true; // Si on arrive ici sans exception, l'insertion a réussi !

            } catch (SQLException e) {
                // On vérifie si l'erreur est spécifiquement un doublon sur SQLite (Code erreur 19)
                if (e instanceof SQLiteException &&
                        (((SQLiteException) e).getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY ||
                                ((SQLiteException) e).getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT)) {

                    System.out.println("Doublon de matricule local détecté ! Régénération...");

                    // Sécurité anti-boucle infinie (Ex: si la méthode de régénération échoue)
                    if (tentatives > 5) {
                        throw new SQLException("Impossible de générer un matricule unique après 5 tentatives.", e);
                    }

                    eleve.regenererMatricule(); // Ta méthode pour modifier le matricule de l'objet
                    tentatives++;
                } else {
                    throw e;
                }
            }
        }
    }
    public boolean update(Eleve eleve) {
        String sql = "UPDATE Eleve SET idParent = ?, nom = ?, prenom = ?, dateNaissance = ?, lieuNaissance = ?, " +
                "sexe = ?, nationnalite = ?, photo = ?, aTerminerPension = ?, is_synced = 0, derniere_modification = CURRENT_TIMESTAMP " +
                "WHERE matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, eleve.getIdParent());
            pstmt.setString(2, eleve.getNom());
            pstmt.setString(3, eleve.getPrenom());

            if (eleve.getDateNaissance() != null) {
                pstmt.setString(4, dateFormat.format(eleve.getDateNaissance()));
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }

            pstmt.setString(5, eleve.getLieuNaissance());
            pstmt.setString(6, eleve.getSexe());
            pstmt.setString(7, eleve.getNationnalite());
            pstmt.setString(8, eleve.getPhoto());
            pstmt.setInt(9, eleve.getATerminerPension() ? 1 : 0);
            pstmt.setString(10, eleve.getMatricule());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'élève: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String matricule) {
        String sql = "DELETE FROM Eleve WHERE matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'élève: " + e.getMessage());
            return false;
        }
    }

    public Eleve findByMatricule(String matricule) {
        String sql = "SELECT * FROM Eleve WHERE matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEleve(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByMatricule: " + e.getMessage());
        }
        return null;
    }



    public void markAsSynced(String matricule) {
        String sql = "UPDATE Eleve SET is_synced = 1 WHERE matriculeEleve = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur markAsSynced: " + e.getMessage());
        }
    }

    public List<Eleve> findPendingSynchronization() {
        List<Eleve> pending = new ArrayList<>();
        String sql = "SELECT * FROM Eleve WHERE is_synced = 0";

        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pending.add(mapResultSetToEleve(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findPendingSynchronization: " + e.getMessage());
        }
        return pending;
    }

    public List<Eleve> findAll() {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM Eleve ORDER BY nom ASC";

        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                eleves.add(mapResultSetToEleve(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll: " + e.getMessage());
        }
        return eleves;
    }







    private Eleve mapResultSetToEleve(ResultSet rs) throws Exception {
        Eleve eleve = new Eleve();

        // Utilisation de la réflexion ou des setters directs de votre classe Eleve
        // Note: Comme vos attributs de classe sont pour l'instant package-private ou privatise-les et génère les setters.
        // Exemple si vous avez ajouté les setters correspondants dans Eleve.java :

        // Injection du matricule (Variable 'Matricule' avec majuscule dans votre modèle)
        java.lang.reflect.Field fieldMatricule = Eleve.class.getDeclaredField("Matricule");
        fieldMatricule.setAccessible(true);
        fieldMatricule.set(eleve, rs.getString("matriculeEleve"));

        java.lang.reflect.Field fieldNom = Eleve.class.getDeclaredField("nom");
        fieldNom.setAccessible(true);
        fieldNom.set(eleve, rs.getString("nom"));

        java.lang.reflect.Field fieldPrenom = Eleve.class.getDeclaredField("prenom");
        fieldPrenom.setAccessible(true);
        fieldPrenom.set(eleve, rs.getString("prenom"));

        String dateStr = rs.getString("dateNaissance");
        if (dateStr != null) {
            java.lang.reflect.Field fieldDate = Eleve.class.getDeclaredField("dateNaissance");
            fieldDate.setAccessible(true);
            fieldDate.set(eleve, dateFormat.parse(dateStr));
        }

        java.lang.reflect.Field fieldLieu = Eleve.class.getDeclaredField("lieuNaissance");
        fieldLieu.setAccessible(true);
        fieldLieu.set(eleve, rs.getString("lieuNaissance"));

        java.lang.reflect.Field fieldSexe = Eleve.class.getDeclaredField("sexe");
        fieldSexe.setAccessible(true);
        fieldSexe.set(eleve, rs.getString("sexe"));

        java.lang.reflect.Field fieldNat = Eleve.class.getDeclaredField("nationnalite");
        fieldNat.setAccessible(true);
        fieldNat.set(eleve, rs.getString("nationnalite"));

        java.lang.reflect.Field fieldPhoto = Eleve.class.getDeclaredField("photo");
        fieldPhoto.setAccessible(true);
        fieldPhoto.set(eleve, rs.getString("photo"));

        java.lang.reflect.Field fieldPension = Eleve.class.getDeclaredField("aTerminerPension");
        fieldPension.setAccessible(true);
        fieldPension.set(eleve, rs.getInt("aTerminerPension") == 1);

        // Liaison avec l'objet Parent (Instanciation minimale pour récupérer l'ID)
        Parent parentMock = new Parent();
        parentMock.setPrenom(""); // Remplir si besoin, ou laisser le service charger le vrai ParentDAO
        java.lang.reflect.Field fieldIdParent = Parent.class.getDeclaredField("idParent");
        fieldIdParent.setAccessible(true);
        fieldIdParent.set(parentMock, rs.getString("idParent"));

        eleve.parent = parentMock;

        return eleve;
    }

}