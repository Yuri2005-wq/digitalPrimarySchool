package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import com.digitalprimaryschool.digitalprimaryschool.model.Profession;
import com.digitalprimaryschool.digitalprimaryschool.model.Quartier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParentDAO {

    public void insert(Parent parent) throws SQLException {
        String sql = "INSERT INTO Parent (idParent, prenom, contactParent, emailParent, profession, adresse, is_synced, date_creation) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0, ?)";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parent.getIdParent());
            pstmt.setString(2, parent.getPrenom());
            pstmt.setInt(3, parent.getContactParent());
            pstmt.setString(4, parent.getEmailParent());
            pstmt.setString(5, parent.getProfession() != null ? parent.getProfession().name() : null);
            pstmt.setString(6, parent.getAdresse() != null ? parent.getAdresse().name() : null);
            pstmt.setString(7, parent.getDateCreation());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            // "23000" correspond au code d'état SQL standard (SQLState) pour les violations d'intégrité (Duplicate Key)
            if ("23000".equals(e.getSQLState())) {
                System.err.println("Erreur : Le parent avec cet identifiant existe déjà.");
            } else {
                throw e;
            }
        }
    }

    public boolean update(Parent parent) throws SQLException {
        String sql = "UPDATE Parent SET prenom = ?, contactParent = ?, emailParent = ?, profession = ?, adresse = ?, " +
                "is_synced = 0, derniere_modification = CURRENT_TIMESTAMP WHERE idParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parent.getPrenom());
            pstmt.setInt(2, parent.getContactParent());
            pstmt.setString(3, parent.getEmailParent());
            pstmt.setString(4, parent.getProfession() != null ? parent.getProfession().name() : null);
            pstmt.setString(5, parent.getAdresse() != null ? parent.getAdresse().name() : null);
            pstmt.setString(6, parent.getIdParent());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(String idParent) throws SQLException {
        String sql = "DELETE FROM Parent WHERE idParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idParent);
            return pstmt.executeUpdate() > 0;
        }
    }

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

    public Parent findByContact(int contact) throws SQLException {
        String sql = "SELECT * FROM Parent WHERE contactParent = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, contact);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToParent(rs);
                }
            }
        }
        return null;
    }

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

    private Parent mapResultSetToParent(ResultSet rs) throws SQLException {
        Parent parent = new Parent();

        // Récupération sécurisée du champ idParent via la réflexion Java
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
        parent.setDateCreation(rs.getString("date_creation"));
        parent.setIsSynced(rs.getInt("is_synced"));
        parent.setLabDerniereModification(rs.getString("derniere_modification"));

        // Extraction robuste de l'Enum Profession
        String professionStr = rs.getString("profession");
        if (professionStr != null && !professionStr.isEmpty()) {
            try {
                parent.setProfession(Profession.valueOf(professionStr));
            } catch (IllegalArgumentException e) {
                parent.setProfession(Profession.AUTRE);
            }
        } else {
            parent.setProfession(Profession.AUTRE);
        }

        // Extraction robuste de l'Enum Quartier
        String adresseStr = rs.getString("adresse");
        if (adresseStr != null && !adresseStr.isEmpty()) {
            try {
                parent.setAdresse(Quartier.valueOf(adresseStr));
            } catch (IllegalArgumentException e) {
                parent.setAdresse((Quartier) null);
            }
        } else {
            parent.setAdresse((Quartier) null);
        }

        return parent;
    }
}