package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO {

    // ================================================================
    // AJOUTER une classe
    // ================================================================
    public void ajouter(Classe classe) throws SQLException {
        String sql = """
                INSERT INTO Classe (idClasse, idEcole, nom, niveau, capaciteMax, section)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classe.getIdClasse());
            stmt.setString(2, classe.getIdEcole());
            stmt.setString(3, classe.getNom());
            stmt.setString(4, classe.getNiveau() != null ? classe.getNiveau().name() : null);
            stmt.setInt(5, classe.getCapaciteMax());
            stmt.setString(6, classe.getSection() != null ? classe.getSection().name() : null);

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // MODIFIER une classe
    // ================================================================
    public void modifier(Classe classe) throws SQLException {
        String sql = """
                UPDATE Classe SET
                    nom = ?,
                    niveau = ?,
                    capaciteMax = ?,
                    section = ?
                WHERE idClasse = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classe.getNom());
            stmt.setString(2, classe.getNiveau() != null ? classe.getNiveau().name() : null);
            stmt.setInt(3, classe.getCapaciteMax());
            stmt.setString(4, classe.getSection() != null ? classe.getSection().name() : null);
            stmt.setString(5, classe.getIdClasse());

            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucune classe trouvée avec l'id : " + classe.getIdClasse());
            }
        }
    }

    // ================================================================
    // SUPPRIMER une classe
    // ================================================================
    public void supprimer(String idClasse) throws SQLException {
        String sql = "DELETE FROM Classe WHERE idClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucune classe trouvée avec l'id : " + idClasse);
            }
        }
    }

    // ================================================================
    // TROUVER une classe par ID
    // ================================================================
    public Classe trouverParId(String idClasse) throws SQLException {
        String sql = "SELECT * FROM Classe WHERE idClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireClasse(rs);
                }
            }
        }
        return null;
    }

    // ================================================================
    // LISTER TOUTES LES CLASSES GLOBALES (Optionnel / Rétrocompatibilité)
    // ================================================================
    public List<Classe> listerToutes() throws SQLException {
        String sql = "SELECT * FROM Classe ORDER BY nom ASC";
        List<Classe> classes = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                classes.add(construireClasse(rs));
            }
        }
        return classes;
    }

    // ================================================================
    // VÉRIFIER EXISTENCE NOM PAR ÉCOLE (Corrigé pour éviter les conflits inter-écoles)
    // ================================================================
    public boolean nomExiste(String nom, String idEcole) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe WHERE LOWER(nom) = LOWER(?) AND idEcole = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean nomExiste(String nom, String idClasseExclu, String idEcole) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe WHERE LOWER(nom) = LOWER(?) AND idClasse != ? AND idEcole = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, idClasseExclu);
            stmt.setString(3, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // ================================================================
    // MÉTHODES DE FILTRAGE PAR ÉCOLE (Pour affichage fluide JavaFX)
    // ================================================================
    public List<Classe> listerToutesPourEcole(String idEcole) throws SQLException {
        String sql = "SELECT * FROM Classe WHERE idEcole = ? ORDER BY niveau, nom";
        List<Classe> classes = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(construireClasse(rs));
                }
            }
        }
        return classes;
    }

    // ================================================================
    // LOGIQUE MÉTIER COHÉRENTE (Bulletins, Insolvables, Titulaires)
    // ================================================================
    public List<String> getEleveNonEnRegle(String idClasse) throws SQLException {
        String sql = """
            SELECT i.matriculeEleve FROM Inscription i
            INNER JOIN Classe c ON i.idClasse = c.idClasse
            INNER JOIN TarisScolaire t ON c.niveau = t.niveauClasse AND c.idEcole = t.idEcole
            WHERE i.idClasse = ? AND i.montantPayer < t.pension
            """;
        List<String> matricules = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) { matricules.add(rs.getString("matriculeEleve")); }
            }
        }
        return matricules;
    }

    public double getMoyenneGenerale(String idClasse, String idSequence) throws SQLException {
        String sql = """
            SELECT AVG(n.valeur) as moyenneClasse FROM Notes n
            WHERE n.idMatiere IN (SELECT idMatiere FROM Matiere WHERE idClasse = ?) AND n.idSequence = ?
            """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            stmt.setString(2, idSequence);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("moyenneClasse");
            }
        }
        return 0.0;
    }

    public String getProfTitulaire(String idClasse) throws SQLException {
        String sql = "SELECT nom, prenom FROM Enseignant WHERE idClasse = ? LIMIT 1";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("nom") + " " + rs.getString("prenom");
            }
        }
        return "Aucun titulaire";
    }

    // ================================================================
    // LOGIQUE DE SOUCHE INTERNE
    // ================================================================
    private Classe construireClasse(ResultSet rs) throws SQLException {
        Classe classe = new Classe();
        classe.setIdClasse(rs.getString("idClasse"));
        classe.setIdEcole(rs.getString("idEcole"));
        classe.setNom(rs.getString("nom"));
        classe.setCapaciteMax(rs.getInt("capaciteMax"));

        String niveauStr = rs.getString("niveau");
        if (niveauStr != null && !niveauStr.isEmpty()) {
            try { classe.setNiveau(NiveauClasse.valueOf(niveauStr)); } catch (IllegalArgumentException e) {}
        }
        String sectionStr = rs.getString("section");
        if (sectionStr != null && !sectionStr.isEmpty()) {
            try { classe.setSection(SectionClass.valueOf(sectionStr)); } catch (IllegalArgumentException e) {}
        }

        // Stratégie d'injection : récupération du titulaire via EnseignantDAO
        String sqlProf = "SELECT idEnseignant FROM Enseignant WHERE idClasse = ? LIMIT 1";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmtProf = conn.prepareStatement(sqlProf)) {
            stmtProf.setString(1, classe.getIdClasse());
            try (ResultSet rsProf = stmtProf.executeQuery()) {
                if (rsProf.next()) {
                    EnseignantDAO ensDAO = new EnseignantDAO();
                    classe.setEnseignant(ensDAO.trouverParId(rsProf.getString("idEnseignant")));
                }
            }
        }

        return classe;
    }
}