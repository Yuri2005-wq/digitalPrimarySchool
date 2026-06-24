package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public void ajouter(Eleve eleve, String idParent) throws SQLException {
        String sql = """
                INSERT INTO Eleve (
                    matriculeEleve, idParent, nom, prenom,
                    dateNaissance, lieuNaissance, sexe,
                    nationnalite, photo, aTerminerPension,
                    regionOrigine, antecedentsMedicaux
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleve.getMatricule());
            stmt.setString(2, idParent);
            stmt.setString(3, eleve.getNom());
            stmt.setString(4, eleve.getPrenom());
            stmt.setString(5, eleve.getDateNaissance() != null
                    ? SDF.format(eleve.getDateNaissance()) : null);
            stmt.setString(6, eleve.getLieuNaissance() != null
                    ? eleve.getLieuNaissance().name() : null);
            stmt.setString(7, eleve.getSexe() != null
                    ? eleve.getSexe().name() : null);
            stmt.setString(8, eleve.getNationalite() != null
                    ? eleve.getNationalite().name() : null);
            stmt.setString(9, eleve.getPhoto());
            stmt.setInt(10, eleve.getATerminerPension() ? 1 : 0);
            stmt.setString(11, eleve.getRegionOrigine() != null
                    ? eleve.getRegionOrigine().name() : null);
            stmt.setString(12, eleve.getAntecedentsMedicaux());

            stmt.executeUpdate();
            System.out.println("Élève ajouté : " + eleve.getMatricule());
        }
    }

    public void modifier(Eleve eleve) throws SQLException {
        String sql = """
                UPDATE Eleve SET
                    nom = ?,
                    prenom = ?,
                    dateNaissance = ?,
                    lieuNaissance = ?,
                    sexe = ?,
                    nationnalite = ?,
                    photo = ?,
                    aTerminerPension = ?,
                    regionOrigine = ?,
                    antecedentsMedicaux = ?,
                    derniere_modification = CURRENT_TIMESTAMP
                WHERE matriculeEleve = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleve.getNom());
            stmt.setString(2, eleve.getPrenom());
            stmt.setString(3, eleve.getDateNaissance() != null
                    ? SDF.format(eleve.getDateNaissance()) : null);
            stmt.setString(4, eleve.getLieuNaissance() != null
                    ? eleve.getLieuNaissance().name() : null);
            stmt.setString(5, eleve.getSexe() != null
                    ? eleve.getSexe().name() : null);
            stmt.setString(6, eleve.getNationalite() != null
                    ? eleve.getNationalite().name() : null);
            stmt.setString(7, eleve.getPhoto());
            stmt.setInt(8, eleve.getATerminerPension() ? 1 : 0);
            stmt.setString(9, eleve.getRegionOrigine() != null
                    ? eleve.getRegionOrigine().name() : null);
            stmt.setString(10, eleve.getAntecedentsMedicaux());
            stmt.setString(11, eleve.getMatricule());

            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucun élève trouvé avec le matricule : " + eleve.getMatricule());
            }
        }
    }

    public void supprimer(String matricule) throws SQLException {
        String sql = "DELETE FROM Eleve WHERE matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucun élève trouvé avec le matricule : " + matricule);
            }
            System.out.println("Élève supprimé : " + matricule);
        }
    }

    // --- REQUÊTE DE BASE CENTRALISÉE AVEC LES JOINTURES ---
    private String getBaseSelectQuery() {
        return """
                SELECT e.*, 
                       p.prenom AS p_prenom, p.contactParent AS p_contact, p.emailParent AS p_email, p.profession AS p_profession,
                       i.idClasse AS i_idClasse, i.idAnnescolaire AS i_anneeScolaire
                FROM Eleve e
                LEFT JOIN Parent p ON e.idParent = p.idParent
                LEFT JOIN Inscription i ON e.matriculeEleve = i.matriculeEleve AND i.idAnnescolaire = '2025-2026'
                """;
    }

    public Eleve trouverParMatricule(String matricule) throws SQLException {
        String sql = getBaseSelectQuery() + " WHERE e.matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireEleve(rs);
                }
            }
        }
        return null;
    }

    public List<Eleve> listerTous() throws SQLException {
        String sql = getBaseSelectQuery() + " ORDER BY e.nom, e.prenom";
        List<Eleve> eleves = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                eleves.add(construireEleve(rs));
            }
        }
        return eleves;
    }

    public List<Eleve> listerParClasse(String idClasse) throws SQLException {
        String sql = getBaseSelectQuery() + " WHERE i.idClasse = ? ORDER BY e.nom, e.prenom";
        List<Eleve> eleves = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    eleves.add(construireEleve(rs));
                }
            }
        }
        return eleves;
    }

    public List<Eleve> rechercher(String motCle) throws SQLException {
        String sql = getBaseSelectQuery() + """
                 WHERE LOWER(e.nom) LIKE LOWER(?)
                    OR LOWER(e.prenom) LIKE LOWER(?)
                    OR e.matriculeEleve LIKE ?
                 ORDER BY e.nom, e.prenom
                """;
        List<Eleve> eleves = new ArrayList<>();
        String parametre = "%" + motCle + "%";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, parametre);
            stmt.setString(2, parametre);
            stmt.setString(3, parametre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    eleves.add(construireEleve(rs));
                }
            }
        }
        return eleves;
    }

    public int compterTous() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Eleve";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean matriculeExiste(String matricule) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Eleve WHERE matriculeEleve = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private Eleve construireEleve(ResultSet rs) throws SQLException {
        Eleve eleve = new Eleve();

        eleve.setMatricule(rs.getString("matriculeEleve"));
        eleve.setNom(rs.getString("nom"));
        eleve.setPrenom(rs.getString("prenom"));

        // Conversion LieuNaissance
        String lieuStr = rs.getString("lieuNaissance");
        if (lieuStr != null && !lieuStr.isEmpty()) {
            try {
                eleve.setLieuNaissance(LieuNaissance.valueOf(lieuStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Lieu de naissance inconnu : " + lieuStr);
                eleve.setLieuNaissance(LieuNaissance.AUTRE);
            }
        } else {
            eleve.setLieuNaissance(LieuNaissance.AUTRE);
        }

        // Conversion Nationalité
        String nationaliteStr = rs.getString("nationnalite");
        if (nationaliteStr != null && !nationaliteStr.isEmpty()) {
            try {
                eleve.setNationalite(Nationnalite.valueOf(nationaliteStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Valeur de nationalité inconnue : " + nationaliteStr);
                eleve.setNationalite(Nationnalite.AUTRE);
            }
        } else {
            eleve.setNationalite(Nationnalite.AUTRE);
        }

        eleve.setPhoto(rs.getString("photo"));
        eleve.setaTerminerPension(rs.getInt("aTerminerPension") == 1);

        // Conversion date
        String dateStr = rs.getString("dateNaissance");
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                eleve.setDateNaissance(SDF.parse(dateStr));
            } catch (ParseException e) {
                System.err.println("Format de date invalide pour : " + dateStr);
            }
        }

        // Conversion Sexe
        String sexeStr = rs.getString("sexe");
        if (sexeStr != null && !sexeStr.isEmpty()) {
            try {
                eleve.setSexe(Sexe.valueOf(sexeStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Valeur de sexe inconnue : " + sexeStr);
                eleve.setSexe(Sexe.MASCULIN);
            }
        } else {
            eleve.setSexe(Sexe.MASCULIN);
        }

        // Conversion Region
        String regionStr = rs.getString("regionOrigine");
        if (regionStr != null && !regionStr.isEmpty()) {
            try {
                eleve.setRegionOrigine(Region.valueOf(regionStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Région inconnue : " + regionStr);
                eleve.setRegionOrigine(Region.AUTRE);
            }
        } else {
            eleve.setRegionOrigine(Region.AUTRE);
        }

        eleve.setAntecedentsMedicaux(rs.getString("antecedentsMedicaux"));

        // --- ENCAPSULATION DU PARENT (LIEN DIRECT VERS L'ATTRIBUT PUBLIC) ---
        String pPrenom = rs.getString("p_prenom");
        if (pPrenom != null) {
            Parent parent = new Parent();
            parent.setPrenom(pPrenom);
            parent.setContactParent(rs.getInt("p_contact"));
            parent.setEmailParent(rs.getString("p_email"));

            String profStr = rs.getString("p_profession");
            if (profStr != null) {
                parent.setProfession(profStr);
            }

            // Assignation directe à la variable publique de l'objet Eleve
            eleve.parent = parent;
        }

        // --- ENCAPSULATION DE L'INSCRIPTION (LIEN DIRECT VERS L'ATTRIBUT PUBLIC) ---
        String idClasse = rs.getString("i_idClasse");
        if (idClasse != null) {
            Inscription inscription = new Inscription();
            inscription.setMatriculeEleve(eleve.getMatricule());
            inscription.setIdClasse(idClasse);
            inscription.setIdAnnescolaire(rs.getString("i_anneeScolaire"));

            // Assignation directe à la variable publique de l'objet Eleve
            eleve.inscription = inscription;
        }

        return eleve;
    }
}