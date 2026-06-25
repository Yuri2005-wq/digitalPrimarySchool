package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Inscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAO {

    // ================================================================
    // AJOUTER une inscription
    // ================================================================
    public void ajouter(Inscription inscription) throws SQLException {
        String sql = """
                INSERT INTO Inscription (
                    idInscription, idEcole, idAnnescolaire, idClasse, 
                    matriculeEleve, montantPayer, estReinscript
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inscription.getIdInscription());
            stmt.setInt(2, inscription.getIdEcole()); // Injection de la variable d'environnement de session
            stmt.setString(3, inscription.getIdAnnescolaire());
            stmt.setString(4, inscription.getIdClasse());
            stmt.setString(5, inscription.getMatriculeEleve());
            stmt.setDouble(6, inscription.getMontantPayer());
            stmt.setInt(7, inscription.getEstReinscript());

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // VÉRIFIER si une inscription existe
    // ================================================================
    public boolean inscriptionExiste(String matricule, String idClasse, String idAnnee) throws SQLException {
        String sql = """
                SELECT COUNT(*) FROM Inscription 
                WHERE matriculeEleve = ? AND idClasse = ? AND idAnnescolaire = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            stmt.setString(2, idClasse);
            stmt.setString(3, idAnnee);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // ================================================================
    // LISTER les inscriptions d'un élève
    // ================================================================
    public List<Inscription> listerParEleve(String matricule) throws SQLException {
        String sql = "SELECT * FROM Inscription WHERE matriculeEleve = ?";
        List<Inscription> inscriptions = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(construireInscription(rs));
                }
            }
        }
        return inscriptions;
    }

    // ================================================================
    // SUPPRIMER une inscription
    // ================================================================
    public void supprimer(String idInscription) throws SQLException {
        String sql = "DELETE FROM Inscription WHERE idInscription = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idInscription);
            stmt.executeUpdate();
        }
    }

    // ================================================================
    // LISTER toutes les inscriptions d'une classe pour une année
    // ================================================================
    public List<Inscription> listerParClasseEtAnnee(String idClasse, String idAnnee) throws SQLException {
        String sql = "SELECT * FROM Inscription WHERE idClasse = ? AND idAnnescolaire = ?";
        List<Inscription> inscriptions = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            stmt.setString(2, idAnnee);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(construireInscription(rs));
                }
            }
        }
        return inscriptions;
    }

    // ================================================================
    // LISTER toutes les inscriptions d'une école entière (Nouveau)
    // ================================================================
    public List<Inscription> listerToutesPourEcole(int idEcole) throws SQLException {
        String sql = "SELECT * FROM Inscription WHERE idEcole = ?";
        List<Inscription> inscriptions = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(construireInscription(rs));
                }
            }
        }
        return inscriptions;
    }

    // ================================================================
    // LOGIQUE INTERNE DE CONSTRUCTION
    // ================================================================
    private Inscription construireInscription(ResultSet rs) throws SQLException {
        Inscription ins = new Inscription();
        ins.setIdInscription(rs.getString("idInscription"));
        ins.setIdEcole(rs.getInt("idEcole"));
        ins.setIdAnnescolaire(rs.getString("idAnnescolaire"));
        ins.setIdClasse(rs.getString("idClasse"));
        ins.setMatriculeEleve(rs.getString("matriculeEleve"));
        ins.setMontantPayer(rs.getDouble("montantPayer"));
        ins.setEstReinscript(rs.getInt("estReinscript"));
        ins.setDateInscription(rs.getString("dateInscription"));
        return ins;
    }
}