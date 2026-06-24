package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.NiveauClasse;
import com.digitalprimaryschool.digitalprimaryschool.model.TarisScolaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifScolaireDAO {

    // ================================================================
    // AJOUTER un tarif scolaire
    // ================================================================
    public void ajouter(TarisScolaire tarif) throws SQLException {
        String sql = """
                INSERT INTO TarisScolaire (
                    idTarifScolaire, niveauClasse, libelle, pension,
                    fraistenueScolaire, fraistenueSport, fraisInscription
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarif.getIdTarifScolaire());
            stmt.setString(2, tarif.getNiveauClasse() != null ? tarif.getNiveauClasse().name() : null);
            stmt.setString(3, tarif.getLibelle());
            stmt.setDouble(4, tarif.getMontantPension());
            stmt.setDouble(5, tarif.getFraistenueScolaire());
            stmt.setDouble(6, tarif.getFraistenueSport());
            stmt.setDouble(7, tarif.getFraisInscription());

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // MODIFIER un tarif scolaire
    // ================================================================
    public void modifier(TarisScolaire tarif) throws SQLException {
        String sql = """
                UPDATE TarisScolaire SET
                    niveauClasse = ?,
                    libelle = ?,
                    pension = ?,
                    fraistenueScolaire = ?,
                    fraistenueSport = ?,
                    fraisInscription = ?
                WHERE idTarifScolaire = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarif.getNiveauClasse() != null ? tarif.getNiveauClasse().name() : null);
            stmt.setString(2, tarif.getLibelle());
            stmt.setDouble(3, tarif.getMontantPension());
            stmt.setDouble(4, tarif.getFraistenueScolaire());
            stmt.setDouble(5, tarif.getFraistenueSport());
            stmt.setDouble(6, tarif.getFraisInscription());
            stmt.setString(7, tarif.getIdTarifScolaire());

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // SUPPRIMER un tarif
    // ================================================================
    public void supprimer(String idTarif) throws SQLException {
        String sql = "DELETE FROM TarisScolaire WHERE idTarifScolaire = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idTarif);
            stmt.executeUpdate();
        }
    }

    // ================================================================
    // TROUVER le tarif d'une classe par niveauClasse
    // ================================================================
    public TarisScolaire trouverParClasse(String niveauClasse) throws SQLException {
        String sql = "SELECT * FROM TarisScolaire WHERE niveauClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, niveauClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireTarif(rs);
                }
            }
        }
        return null;
    }

    // ================================================================
    // TROUVER un tarif par son propre ID
    // ================================================================
    public TarisScolaire trouverParId(String idTarif) throws SQLException {
        String sql = "SELECT * FROM TarisScolaire WHERE idTarifScolaire = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idTarif);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireTarif(rs);
                }
            }
        }
        return null;
    }

    // ================================================================
    // LISTER tous les tarifs
    // ================================================================
    public List<TarisScolaire> listerTous() throws SQLException {
        String sql = """
                SELECT *
                FROM TarisScolaire
                ORDER BY niveauClasse
                """;
        List<TarisScolaire> tarifs = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TarisScolaire tarif = construireTarif(rs);
                tarifs.add(tarif);
            }
        }
        return tarifs;
    }

    // ================================================================
    // CALCULER le total des frais d'une classe
    // ================================================================
    public double calculerTotalFrais(String niveauClasse) throws SQLException {
        String sql = """
                SELECT (pension + fraistenueScolaire + fraistenueSport + fraisInscription) as total
                FROM TarisScolaire WHERE niveauClasse = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, niveauClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    // ================================================================
    // VÉRIFIER si un tarif existe pour une classe
    // ================================================================
    public boolean tarifExistePourClasse(NiveauClasse niveauClasse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TarisScolaire WHERE niveauClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, niveauClasse.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // ================================================================
    // MÉTHODE PRIVÉE
    // ================================================================
    private TarisScolaire construireTarif(ResultSet rs) throws SQLException {
        TarisScolaire tarif = new TarisScolaire();
        tarif.setIdTarifScolaire(rs.getString("idTarifScolaire"));

        String niveauStr = rs.getString("niveauClasse");
        if (niveauStr != null && !niveauStr.isEmpty()) {
            try {
                tarif.setNiveauClasse(NiveauClasse.valueOf(niveauStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Niveau inconnu : " + niveauStr);
            }
        }

        tarif.setLibelle(rs.getString("libelle"));
        tarif.setMontantPension(rs.getDouble("pension"));
        tarif.setFraistenueScolaire(rs.getDouble("fraistenueScolaire"));
        tarif.setFraistenueSport(rs.getDouble("fraistenueSport"));
        tarif.setFraisInscription(rs.getDouble("fraisInscription"));
        return tarif;
    }
}