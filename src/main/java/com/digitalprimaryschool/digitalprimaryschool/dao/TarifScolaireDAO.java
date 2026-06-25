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
                    idTarifScolaire, idEcole, niveauClasse, libelle, pension,
                    fraistenueScolaire, fraistenueSport, fraisInscription
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarif.getIdTarifScolaire());
            stmt.setInt(2, tarif.getIdEcole()); // Injection invisible multi-école
            stmt.setString(3, tarif.getNiveauClasse() != null ? tarif.getNiveauClasse().name() : null);
            stmt.setString(4, tarif.getLibelle());
            stmt.setDouble(5, tarif.getMontantPension());
            stmt.setDouble(6, tarif.getFraistenueScolaire());
            stmt.setDouble(7, tarif.getFraistenueSport());
            stmt.setDouble(8, tarif.getFraisInscription());

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
    // TROUVER le tarif d'une classe par niveauClasse (Surcharge String)
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
    // LISTER tous les tarifs de la base de données
    // ================================================================
    public List<TarisScolaire> listerTous() throws SQLException {
        String sql = "SELECT * FROM TarisScolaire ORDER BY niveauClasse";
        List<TarisScolaire> tarifs = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tarifs.add(construireTarif(rs));
            }
        }
        return tarifs;
    }

    // ================================================================
    // VÉRIFIER si un tarif existe pour une classe (Accepte un String)
    // ================================================================
    public boolean tarifExistePourClasse(String niveauClasse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TarisScolaire WHERE niveauClasse = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, niveauClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Surcharge acceptant un type Enum NiveauClasse (Requis par EnregistrementService)
    public boolean tarifExistePourClasse(NiveauClasse niveauClasse) throws SQLException {
        if (niveauClasse == null) return false;
        return tarifExistePourClasse(niveauClasse.name());
    }

    // ================================================================
    // LOGIQUE INTERNE DE CONSTRUCTION
    // ================================================================
    private TarisScolaire construireTarif(ResultSet rs) throws SQLException {
        TarisScolaire tarif = new TarisScolaire();
        tarif.setIdTarifScolaire(rs.getString("idTarifScolaire"));
        tarif.setIdEcole(rs.getInt("idEcole")); // Hydratation de la clé école

        String niveauStr = rs.getString("niveauClasse");
        if (niveauStr != null && !niveauStr.isEmpty()) {
            try {
                tarif.setNiveauClasse(NiveauClasse.valueOf(niveauStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Niveau de classe inconnu à la lecture : " + niveauStr);
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