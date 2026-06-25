package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    /**
     * Vérifie les identifiants et charge l'utilisateur complet avec son rôle et son école
     */
    public Utilisateur verifierIdentifiants(String username, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE username = ? AND motDePasseUtilisateur = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, motDePasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireUtilisateur(rs);
                }
            }
        }
        return null;
    }

    /**
     * Insère un utilisateur complet dans le système
     */
    public void insertUser(Utilisateur utilisateur) throws SQLException {
        String sql = """
            INSERT INTO Utilisateur(idUtilisateur, idEcole, username, motDePasseUtilisateur, role, is_synced) 
            VALUES(?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getIdUtilisateur());
            pstmt.setString(2, utilisateur.getIdEcole());
            pstmt.setString(3, utilisateur.getUsername());
            pstmt.setString(4, utilisateur.getMotDePasseUtilisateur());
            pstmt.setString(5, utilisateur.getRole());
            pstmt.setInt(6, utilisateur.getIsSynced());

            pstmt.executeUpdate();
        }
    }

    /**
     * Met à jour les informations d'un utilisateur (sauf son mot de passe pour des raisons de sécurité)
     */
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String sql = """
            UPDATE Utilisateur SET 
                username = ?, 
                role = ?, 
                is_synced = 0 
            WHERE idUtilisateur = ?
            """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getUsername());
            pstmt.setString(2, utilisateur.getRole());
            pstmt.setString(3, utilisateur.getIdUtilisateur());

            pstmt.executeUpdate();
        }
    }

    /**
     * Permet à un utilisateur de modifier son mot de passe
     */
    public void modifierMotDePasse(String idUtilisateur, String nouveauMdp) throws SQLException {
        String sql = "UPDATE Utilisateur SET motDePasseUtilisateur = ?, is_synced = 0 WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nouveauMdp);
            pstmt.setString(2, idUtilisateur);

            pstmt.executeUpdate();
        }
    }

    /**
     * Supprime un utilisateur du système
     */
    public void supprimer(String idUtilisateur) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUtilisateur);
            pstmt.executeUpdate();
        }
    }

    /**
     * Liste tous les utilisateurs rattachés à une école spécifique
     */
    public List<Utilisateur> listerParEcole(String idEcole) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE idEcole = ? ORDER BY username ASC";
        List<Utilisateur> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEcole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(construireUtilisateur(rs));
                }
            }
        }
        return liste;
    }

    /**
     * Marque l'utilisateur comme synchronisé avec le serveur cloud
     */
    public void marquerCommeSynchronise(String idUtilisateur) throws SQLException {
        String sql = "UPDATE Utilisateur SET is_synced = 1 WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUtilisateur);
            pstmt.executeUpdate();
        }
    }

    /**
     * Centralisation de la construction de l'objet de données
     */
    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getString("idUtilisateur"));
        u.setIdEcole(rs.getString("idEcole"));
        u.setUsername(rs.getString("username"));
        u.setMotDePasseUtilisateur(rs.getString("motDePasseUtilisateur"));
        u.setRole(rs.getString("role"));
        u.setIsSynced(rs.getInt("is_synced"));
        u.setDerniereModification(rs.getString("derniere_modification"));
        return u;
    }
}