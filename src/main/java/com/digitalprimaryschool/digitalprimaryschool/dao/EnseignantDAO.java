package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Enseignant;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    public void ajouter(Enseignant ens) throws SQLException {
        String sql = """
                INSERT INTO Enseignant (
                    idEnseignant, nom, prenom, contactEnseignant, photo, 
                    qualification, grade, idClasse, idUtilisateur
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ens.getIdEnseignant());
            stmt.setString(2, ens.getNom());
            stmt.setString(3, ens.getPrenom());
            stmt.setString(4, ens.getContactEnseignant());
            stmt.setString(5, ens.getPhoto());
            stmt.setString(6, ens.getQualification());
            stmt.setString(7, ens.getGrade());

            if (ens.getClasse() != null) {
                stmt.setString(7, ens.getClasse().getIdClasse());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            if (ens.getIdUtilisateur() != null) {
                stmt.setString(8, ens.getIdUtilisateur());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            stmt.executeUpdate();
        }
    }

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

    public boolean modifier(Enseignant ens) throws SQLException {
        String sql = """
                UPDATE Enseignant SET
                    nom = ?, prenom = ?, contactEnseignant = ?, 
                    qualification = ?, grade = ?, idClasse = ?, idUtilisateur = ?
                WHERE idEnseignant = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ens.getNom());
            stmt.setString(2, ens.getPrenom());
            stmt.setString(3, ens.getContactEnseignant());
            stmt.setString(4, ens.getQualification());
            stmt.setString(5, ens.getGrade());

            if (ens.getClasse() != null) {
                stmt.setString(6, ens.getClasse().getIdClasse());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            if (ens.getIdUtilisateur() != null) {
                stmt.setString(7, ens.getIdUtilisateur());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.setString(8, ens.getIdEnseignant());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean supprimer(String idEnseignant) throws SQLException {
        String sql = "DELETE FROM Enseignant WHERE idEnseignant = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEnseignant);
            return stmt.executeUpdate() > 0;
        }
    }

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

    public List<Enseignant> listerTous() throws SQLException {
        String sql = "SELECT * FROM Enseignant ORDER BY nom ASC, prenom ASC";
        List<Enseignant> liste = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(construireEnseignant(rs));
            }
        }
        return liste;
    }

    private Enseignant construireEnseignant(ResultSet rs) throws SQLException {
        Enseignant ens = new Enseignant();
        ens.setIdEnseignant(rs.getString("idEnseignant"));
        ens.setIdUtilisateur(rs.getString("idUtilisateur"));
        ens.setNom(rs.getString("nom"));
        ens.setPrenom(rs.getString("prenom"));
        ens.setContactEnseignant(rs.getString("contactEnseignant"));
        ens.setQualification(rs.getString("qualification"));
        ens.setGrade(rs.getString("grade"));

        String idClasse = rs.getString("idClasse");
        if (idClasse != null && !idClasse.isEmpty()) {
            ClasseDAO classeDAO = new ClasseDAO();
            Classe c = classeDAO.trouverParId(idClasse);
            ens.setClasse(c);
        }
        return ens;
    }
}