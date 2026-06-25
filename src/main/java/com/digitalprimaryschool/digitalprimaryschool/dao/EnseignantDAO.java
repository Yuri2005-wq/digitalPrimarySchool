package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Enseignant;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    // ================================================================
    // 1. CRÉER un enseignant (Sans affectation de classe initiale)
    // ================================================================
    public void ajouter(Enseignant ens) throws SQLException {
        String sql = """
                INSERT INTO Enseignant (
                    idEnseignant, idEcole, nom, prenom, contactEnseignant, 
                    qualification, grade, idClasse
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ens.getIdEnseignant());
            stmt.setInt(2, ens.getIdEcole()); // Injecté via ContextApp
            stmt.setString(3, ens.getNom());
            stmt.setString(4, ens.getPrenom());
            stmt.setInt(5, ens.getContactEnseignant());
            stmt.setString(6, ens.getQualification());
            stmt.setString(7, ens.getGrade());

            // Logique métier : La classe peut être nulle au moment de la création
            if (ens.getClasse() != null) {
                stmt.setString(8, ens.getClasse().getIdClasse());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // 2. AFFECTER un enseignant à une classe (Ta logique métier clé)
    // ================================================================
    public boolean affecterAClasse(String idEnseignant, String idClasse) throws SQLException {
        String sql = "UPDATE Enseignant SET idClasse = ? WHERE idEnseignant = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (idClasse != null && !idClasse.trim().isEmpty()) {
                stmt.setString(1, idClasse);
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }
            stmt.setString(2, idEnseignant);

            return stmt.executeUpdate() > 0;
        }
    }

    // ================================================================
    // MODIFIER les informations d'un enseignant
    // ================================================================
    public boolean modifier(Enseignant ens) throws SQLException {
        String sql = """
                UPDATE Enseignant SET
                    nom = ?, prenom = ?, contactEnseignant = ?, 
                    qualification = ?, grade = ?, idClasse = ?
                WHERE idEnseignant = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ens.getNom());
            stmt.setString(2, ens.getPrenom());
            stmt.setInt(3, ens.getContactEnseignant());
            stmt.setString(4, ens.getQualification());
            stmt.setString(5, ens.getGrade());

            if (ens.getClasse() != null) {
                stmt.setString(6, ens.getClasse().getIdClasse());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.setString(7, ens.getIdEnseignant());

            return stmt.executeUpdate() > 0;
        }
    }

    // ================================================================
    // SUPPRIMER un enseignant
    // ================================================================
    public boolean supprimer(String idEnseignant) throws SQLException {
        String sql = "DELETE FROM Enseignant WHERE idEnseignant = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEnseignant);
            return stmt.executeUpdate() > 0;
        }
    }

    // ================================================================
    // TROUVER par ID
    // ================================================================
    public Enseignant trouverParId(String idEnseignant) throws SQLException {
        String sql = "SELECT * FROM Enseignant WHERE idEnseignant = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEnseignant);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireEnseignant(rs);
                }
            }
        }
        return null;
    }

    // ================================================================
    // LISTER tous les enseignants d'une école spécifique (Cloisonné)
    // ================================================================
    public List<Enseignant> listerParEcole(int idEcole) throws SQLException {
        String sql = "SELECT * FROM Enseignant WHERE idEcole = ? ORDER BY nom ASC, prenom ASC";
        List<Enseignant> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(construireEnseignant(rs));
                }
            }
        }
        return liste;
    }

    // ================================================================
    // LOGIQUE INTERNE DE CONSTRUCTION
    // ================================================================
    private Enseignant construireEnseignant(ResultSet rs) throws SQLException {
        Enseignant ens = new Enseignant();
        ens.setIdEnseignant(rs.getString("idEnseignant"));
        ens.setIdEcole(rs.getInt("idEcole"));
        ens.setNom(rs.getString("nom"));
        ens.setPrenom(rs.getString("prenom"));
        ens.setContactEnseignant(rs.getInt("contactEnseignant"));
        ens.setQualification(rs.getString("qualification"));
        ens.setGrade(rs.getString("grade"));

        // Hydratation de la relation Classe si elle existe en BD
        String idClasse = rs.getString("idClasse");
        if (idClasse != null && !idClasse.isEmpty()) {
            ClasseDAO classeDAO = new ClasseDAO();
            Classe c = classeDAO.trouverParId(idClasse);
            ens.setClasse(c);
        }

        return ens;
    }
}