package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    // ================================================================
    // CRÉER / AJOUTER un élève (Version standard)
    // ================================================================
    public void inserer(Eleve eleve) throws SQLException {
        ajouter(eleve, eleve.getIdParent());
    }

    // ================================================================
    // AJOUTER un élève avec ID Parent explicite (Appelé par le Service)
    // ================================================================
    public void ajouter(Eleve eleve, String idParent) throws SQLException {
        String sql = """
            INSERT INTO Eleve (matriculeEleve, idParent, nom, prenom, dateNaissance, lieuNaissance, 
                               sexe, nationalite, photo, aTerminerPension, regionOrigine, antecedentsMedicaux, is_synced)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)
            """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleve.getMatricule());
            stmt.setString(2, idParent);
            stmt.setString(3, eleve.getNom());
            stmt.setString(4, eleve.getPrenom());
            stmt.setDate(5, eleve.getDateNaissance() != null ? new java.sql.Date(eleve.getDateNaissance().getTime()) : null);
            stmt.setString(6, eleve.getLieuNaissance() != null ? eleve.getLieuNaissance().name() : null);
            stmt.setString(7, eleve.getSexe() != null ? eleve.getSexe().name() : null);
            stmt.setString(8, eleve.getNationalite() != null ? eleve.getNationalite().name() : null);
            stmt.setString(9, eleve.getPhoto());
            stmt.setInt(10, eleve.getATerminerPension() ? 1 : 0);
            stmt.setString(11, eleve.getRegionOrigine() != null ? eleve.getRegionOrigine().name() : null);
            stmt.setString(12, eleve.getAntecedentsMedicaux());

            stmt.executeUpdate();
        }
    }

    // ================================================================
    // MODIFIER un élève
    // ================================================================
    public boolean modifier(Eleve eleve) throws SQLException {
        String sql = """
            UPDATE Eleve SET 
                idParent = ?, nom = ?, prenom = ?, dateNaissance = ?, lieuNaissance = ?, 
                sexe = ?, nationalite = ?, photo = ?, aTerminerPension = ?, 
                regionOrigine = ?, antecedentsMedicaux = ?, is_synced = 0 
            WHERE matriculeEleve = ?
            """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleve.getIdParent());
            stmt.setString(2, eleve.getNom());
            stmt.setString(3, eleve.getPrenom());
            stmt.setDate(4, eleve.getDateNaissance() != null ? new java.sql.Date(eleve.getDateNaissance().getTime()) : null);
            stmt.setString(5, eleve.getLieuNaissance() != null ? eleve.getLieuNaissance().name() : null);
            stmt.setString(6, eleve.getSexe() != null ? eleve.getSexe().name() : null);
            stmt.setString(7, eleve.getNationalite() != null ? eleve.getNationalite().name() : null);
            stmt.setString(8, eleve.getPhoto());
            stmt.setInt(9, eleve.getATerminerPension() ? 1 : 0);
            stmt.setString(10, eleve.getRegionOrigine() != null ? eleve.getRegionOrigine().name() : null);
            stmt.setString(11, eleve.getAntecedentsMedicaux());
            stmt.setString(12, eleve.getMatricule());

            return stmt.executeUpdate() > 0;
        }
    }

    // ================================================================
    // SUPPRIMER un élève
    // ================================================================
    public boolean supprimer(String matricule) throws SQLException {
        String sql = "DELETE FROM Eleve WHERE matriculeEleve = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, matricule);
            return stmt.executeUpdate() > 0;
        }
    }

    // ================================================================
    // TROUVER par Matricule
    // ================================================================
    public Eleve trouverParMatricule(String matricule) throws SQLException {
        String sql = "SELECT * FROM Eleve WHERE matriculeEleve = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireEleve(rs, true);
                }
            }
        }
        return null;
    }

    // ================================================================
    // LISTER TOUS LES ÉLÈVES (Requis par le Service)
    // ================================================================
    public List<Eleve> listerTous() throws SQLException {
        String sql = "SELECT * FROM Eleve ORDER BY nom ASC, prenom ASC";
        List<Eleve> liste = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(construireEleve(rs, false));
            }
        }
        return liste;
    }

    // ================================================================
    // COMPTER TOUS LES ÉLÈVES (Requis par le Service)
    // ================================================================
    public int compterTous() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Eleve";
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // ================================================================
    // LISTER les élèves d'une classe spécifique (Via Inscription)
    // ================================================================
    public List<Eleve> listerParClasse(String idClasse) throws SQLException {
        String sql = """
            SELECT e.* FROM Eleve e
            INNER JOIN Inscription i ON e.matriculeEleve = i.matriculeEleve
            WHERE i.idClasse = ?
            ORDER BY e.nom ASC, e.prenom ASC
            """;
        List<Eleve> liste = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(construireEleve(rs, true));
                }
            }
        }
        return liste;
    }

    // ================================================================
    // LISTER tous les élèves d'une École entière
    // ================================================================
    public List<Eleve> listerToutPourEcole(String idEcole) throws SQLException {
        String sql = """
            SELECT DISTINCT e.* FROM Eleve e
            INNER JOIN Inscription i ON e.matriculeEleve = i.matriculeEleve
            INNER JOIN Classe c ON i.idClasse = c.idClasse
            WHERE c.idEcole = ?
            ORDER BY e.nom ASC, e.prenom ASC
            """;
        List<Eleve> liste = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(construireEleve(rs, false));
                }
            }
        }
        return liste;
    }

    // ================================================================
    // INTERNE : Construction de l'objet Eleve
    // ================================================================
    private Eleve construireEleve(ResultSet rs, boolean chargerParent) throws SQLException {
        Eleve eleve = new Eleve();
        eleve.setMatricule(rs.getString("matriculeEleve"));
        eleve.setNom(rs.getString("nom"));
        eleve.setPrenom(rs.getString("prenom"));
        eleve.setDateNaissance(rs.getDate("dateNaissance"));
        eleve.setPhoto(rs.getString("photo"));
        eleve.setaTerminerPension(rs.getInt("aTerminerPension") == 1);
        eleve.setAntecedentsMedicaux(rs.getString("antecedentsMedicaux"));
        eleve.setIsSynced(rs.getInt("is_synced"));
        eleve.setDerniereModification(rs.getString("derniere_modification"));

        eleve.setLieuNaissance(rs.getString("lieuNaissance"));
        eleve.setSexe(rs.getString("sexe"));
        eleve.setNationalite(rs.getString("nationalite"));
        eleve.setRegionOrigine(rs.getString("regionOrigine"));

        String idParent = rs.getString("idParent");
        if (chargerParent && idParent != null && !idParent.isEmpty()) {
            ParentDAO parentDAO = new ParentDAO();
            eleve.parent = parentDAO.findById(idParent);
        }

        return eleve;
    }
}