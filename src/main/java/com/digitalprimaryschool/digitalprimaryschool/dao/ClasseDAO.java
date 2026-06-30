package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO {

    public void ajouter(Classe classe) throws SQLException {
        String sql = """
                INSERT INTO Classe (idClasse, nom, niveau, capaciteMax, section)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classe.getIdClasse());
            stmt.setString(2, classe.getNom());
            stmt.setString(3, classe.getNiveau() != null ? classe.getNiveau().name() : null);
            stmt.setInt(4, classe.getCapaciteMax());
            stmt.setString(5, classe.getSection() != null ? classe.getSection().name() : null);

            stmt.executeUpdate();
        }
    }

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
                throw new SQLException("Aucune classe trouvée avec l'id spécifié.");
            }
        }
    }

    public void supprimer(String idClasse) throws SQLException {
        String sql = "DELETE FROM Classe WHERE idClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucune classe trouvée.");
            }
        }
    }

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

    /**
     * Récupère toutes les classes SANS les effectifs
     */
    public List<Classe> listerToutes() throws SQLException {
        String sql = "SELECT * FROM Classe ORDER BY niveau, nom";
        List<Classe> classes = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(construireClasse(rs));
            }
        }
        return classes;
    }

    /**
     * Récupère toutes les classes AVEC les effectifs (nombre d'élèves actifs)
     * Utilise estReinscript = 1 pour les élèves actifs
     */
    public List<Classe> listerToutesAvecEffectif() throws SQLException {
        String sql = """
            SELECT c.*, 
                   COALESCE(
                       (SELECT COUNT(*) 
                        FROM Inscription i 
                        WHERE i.idClasse = c.idClasse 
                          AND i.estReinscript = 1
                       ), 0
                   ) as totalEleves
            FROM Classe c
            ORDER BY c.niveau, c.nom
            """;

        List<Classe> classes = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Classe classe = construireClasse(rs);
                int totalEleves = rs.getInt("totalEleves");
                classe.setNombreEleve(totalEleves);
                classes.add(classe);
            }
        }
        return classes;
    }

    /**
     * Récupère une classe AVEC son effectif
     * Utilise estReinscript = 1 pour les élèves actifs
     */
    public Classe trouverParIdAvecEffectif(String idClasse) throws SQLException {
        String sql = """
            SELECT c.*, 
                   COALESCE(
                       (SELECT COUNT(*) 
                        FROM Inscription i 
                        WHERE i.idClasse = c.idClasse 
                          AND i.estReinscript = 1
                       ), 0
                   ) as totalEleves
            FROM Classe c
            WHERE c.idClasse = ?
            """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Classe classe = construireClasse(rs);
                int totalEleves = rs.getInt("totalEleves");
                classe.setNombreEleve(totalEleves);
                return classe;
            }
        }
        return null;
    }

    /**
     * Compte le nombre d'élèves actifs dans une classe
     * Utilise estReinscript = 1 pour les élèves actifs
     */
    public int compterElevesParClasse(String idClasse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Inscription WHERE idClasse = ? AND estReinscript = 1";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Compte le nombre total d'élèves actifs dans toutes les classes
     * Utilise estReinscript = 1 pour les élèves actifs
     */
    public int compterTotalEleves() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Inscription WHERE estReinscript = 1";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Compte le nombre total de classes
     */
    public int compterTotalClasses() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Compte le nombre de niveaux distincts
     */
    public int compterNiveauxDistincts() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT niveau) FROM Classe WHERE niveau IS NOT NULL";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean nomExiste(String nom, String idClasseExclu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe WHERE LOWER(nom) = LOWER(?) AND idClasse != ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, idClasseExclu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Récupère les élèves non en règle (montant payé < pension)
     */
    public List<String> getEleveNonEnRegle(String idClasse) throws SQLException {
        String sql = """
            SELECT i.matriculeEleve 
            FROM Inscription i
            INNER JOIN Classe c ON i.idClasse = c.idClasse
            INNER JOIN TarisScolaire t ON c.niveau = t.niveauClasse
            WHERE i.idClasse = ? 
              AND i.estReinscript = 1
              AND i.montantPayer < t.pension
            """;
        List<String> matricules = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matricules.add(rs.getString("matriculeEleve"));
                }
            }
        }
        return matricules;
    }

    /**
     * Récupère le nombre d'élèves non en règle dans une classe
     */
    public int compterElevesNonEnRegle(String idClasse) throws SQLException {
        String sql = """
            SELECT COUNT(*) 
            FROM Inscription i
            INNER JOIN Classe c ON i.idClasse = c.idClasse
            INNER JOIN TarisScolaire t ON c.niveau = t.niveauClasse
            WHERE i.idClasse = ? 
              AND i.estReinscript = 1
              AND i.montantPayer < t.pension
            """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
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
        String sql = "SELECT nom, prenom FROM Enseignant WHERE idClasse = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("nom") + " " + rs.getString("prenom");
            }
        }
        return "Aucun titulaire";
    }

    /**
     * Construit un objet Classe à partir d'un ResultSet
     */
    private Classe construireClasse(ResultSet rs) throws SQLException {
        Classe classe = new Classe();
        classe.setIdClasse(rs.getString("idClasse"));
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

        String sqlProf = "SELECT idEnseignant FROM Enseignant WHERE idClasse = ?";
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