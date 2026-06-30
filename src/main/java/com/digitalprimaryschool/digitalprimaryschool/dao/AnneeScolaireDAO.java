package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.AnneeScolaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnneeScolaireDAO {

    // ================================================================
    // AJOUTER une année scolaire
    // ================================================================
    public void ajouter(AnneeScolaire anneeScolaire) {
        String sql = """
            INSERT INTO AnneeScolaire(idAnnescolaire, libelle, dateDebut, dateFin, estActive)
            VALUES(?, ?, ?, ?, ?)
            """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anneeScolaire.getIdAnnescolaire());
            stmt.setString(2, anneeScolaire.getLibelle());
            stmt.setString(3, anneeScolaire.getDateDebut());
            stmt.setString(4, anneeScolaire.getDateFin());
            stmt.setInt(5, anneeScolaire.isEstActive() ? 1 : 0);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'année scolaire : " + e.getMessage(), e);
        }
    }

    // ================================================================
    // MODIFIER une année scolaire
    // ================================================================
    public void modifier(AnneeScolaire annee) throws SQLException {
        String sql = """
                UPDATE AnneeScolaire SET
                    libelle = ?,
                    dateDebut = ?,
                    dateFin = ?,
                    estActive = ?
                WHERE idAnnescolaire = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, annee.getLibelle());
            stmt.setString(2, annee.getDateDebut());
            stmt.setString(3, annee.getDateFin());
            stmt.setInt(4, annee.isEstActive() ? 1 : 0);
            stmt.setString(5, annee.getIdAnnescolaire());

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // SUPPRIMER une année scolaire
    // ================================================================
    public void supprimer(String idAnnee) throws SQLException {
        String sql = "DELETE FROM AnneeScolaire WHERE idAnnescolaire = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAnnee);
            stmt.executeUpdate();
        }
    }

    // ================================================================
    // MÉTHODES DE GESTION GLOBALE UNIQUE
    // ================================================================

    /**
     * Désactive absolument toutes les années scolaires de l'établissement
     */
    public void desactiverToutes() throws SQLException {
        String sql = "UPDATE AnneeScolaire SET estActive = 0";
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Liste toutes les années scolaires de l'établissement
     */
    public List<AnneeScolaire> listerToutes() throws SQLException {
        String sql = "SELECT * FROM AnneeScolaire ORDER BY dateDebut DESC";
        List<AnneeScolaire> annees = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                annees.add(construireAnnee(rs));
            }
        }
        return annees;
    }

    /**
     * Trouve l'année scolaire active unique de l'établissement
     */
    public AnneeScolaire trouverActive() throws SQLException {
        String sql = "SELECT * FROM AnneeScolaire WHERE estActive = 1 LIMIT 1";
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return construireAnnee(rs);
            }
        }
        return null;
    }

    // ================================================================
    // CONSTRUCTEUR D'OBJETS INTERNE
    // ================================================================
    private AnneeScolaire construireAnnee(ResultSet rs) throws SQLException {
        AnneeScolaire annee = new AnneeScolaire();
        annee.setIdAnnescolaire(rs.getString("idAnnescolaire"));
        annee.setLibelle(rs.getString("libelle"));
        annee.setDateDebut(rs.getString("dateDebut"));
        annee.setDateFin(rs.getString("dateFin"));
        annee.setEstActive(rs.getInt("estActive") == 1);

        return annee;
    }
}