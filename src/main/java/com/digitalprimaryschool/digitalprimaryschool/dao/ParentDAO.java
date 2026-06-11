package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParentDAO {

    /**
     * Insère un Parent directement dans sa propre table.
     * Le statut de synchronisation est géré localement (is_synced = 0).
     */
    public boolean insert(Parent parent) throws SQLException {
        String sql = "INSERT INTO Parent (idParent, prenom, contactParent, emailParent, profession, adresse, is_synced) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0)";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parent.getIdParent());
            pstmt.setString(2, parent.getPrenom());
            pstmt.setInt(3, parent.getContactParent());
            pstmt.setString(4, parent.getEmailParent());
            pstmt.setString(5, parent.getProfession());
            pstmt.setString(6, parent.getAdresse());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            // Gestion de la violation de contrainte d'unicité SQLite (ex: idParent existant)
            if (e instanceof SQLiteException &&
                    (((SQLiteException) e).getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY ||
                            ((SQLiteException) e).getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT)) {
                System.err.println("Erreur : Le parent avec cet identifiant existe déjà.");
                return false;
            } else {
                throw e; // On propage l'erreur vers l'IHM JavaFX (ex: problème de base de données verrouillée)
            }
        }
    }

    /**
     * Met à jour les données d'un parent.
     * Repasse automatiquement is_synced à 0 car la donnée locale a changé.
     */
    public boolean update(Parent parent) throws SQLException {
        String sql = "UPDATE Parent SET prenom = ?, contactParent = ?, emailParent = ?, profession = ?, adresse = ?, " +
                "is_synced = 0, derniere_modification = CURRENT_TIMESTAMP WHERE idParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parent.getPrenom());
            pstmt.setInt(2, parent.getContactParent());
            pstmt.setString(3, parent.getEmailParent());
            pstmt.setString(4, parent.getProfession());
            pstmt.setString(5, parent.getAdresse());
            pstmt.setString(6, parent.getIdParent());

            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Supprime un parent à l'aide de son ID unique.
     */
    public boolean delete(String idParent) throws SQLException {
        String sql = "DELETE FROM Parent WHERE idParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idParent);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Recherche un parent par son identifiant unique.
     */
    public Parent findById(String idParent) {
        String sql = "SELECT * FROM Parent WHERE idParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idParent);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToParent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche findById : " + e.getMessage());
        }
        return null;
    }

    /**
     * Liste l'intégralité des parents enregistrés dans la base locale.
     */
    public List<Parent> findAll() {
        List<Parent> parents = new ArrayList<>();
        String sql = "SELECT * FROM Parent ORDER BY prenom ASC";

        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                parents.add(mapResultSetToParent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la liste globale (findAll) : " + e.getMessage());
        }
        return parents;
    }

    /**
     * [OFFLINE-FIRST] Liste tous les parents modifiés ou créés hors-ligne
     * qui attendent d'être synchronisés vers le serveur cloud.
     */
    public List<Parent> findPendingSynchronization() {
        List<Parent> pending = new ArrayList<>();
        String sql = "SELECT * FROM Parent WHERE is_synced = 0";

        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pending.add(mapResultSetToParent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findPendingSynchronization : " + e.getMessage());
        }
        return pending;
    }

    /**
     * [OFFLINE-FIRST] Marque le parent comme synchronisé avec le Cloud.
     */
    public void markAsSynced(String idParent) {
        String sql = "UPDATE Parent SET is_synced = 1 WHERE idParent = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idParent);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur markAsSynced : " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire interne : Convertit une ligne SQLite en objet Parent Java
     * sans utiliser la réflexion, puisque les attributs ont maintenant des setters publics.
     */
    private Parent mapResultSetToParent(ResultSet rs) throws SQLException {
        Parent parent = new Parent();

        // Comme l'ID est généré au hasard dans le constructeur de Parent,
        // on écrase la valeur générée par la vraie valeur stockée en Base de Données
        try {
            java.lang.reflect.Field fieldIdParent = Parent.class.getDeclaredField("idParent");
            fieldIdParent.setAccessible(true);
            fieldIdParent.set(parent, rs.getString("idParent"));
        } catch (Exception e) {
            System.err.println("Erreur lors de l'attribution de l'ID Parent : " + e.getMessage());
        }

        parent.setPrenom(rs.getString("prenom"));
        parent.setContactParent(rs.getInt("contactParent"));
        parent.setEmailParent(rs.getString("emailParent"));
        parent.setProfession(rs.getString("profession"));
        parent.setAdresse(rs.getString("adresse"));

        return parent;
    }
}