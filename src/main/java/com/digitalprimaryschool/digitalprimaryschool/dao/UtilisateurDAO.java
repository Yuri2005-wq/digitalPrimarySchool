package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;
import com.digitalprimaryschool.digitalprimaryschool.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

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

    public void insertUser(Utilisateur utilisateur) throws SQLException {
        String sql = """
            INSERT INTO Utilisateur(idUtilisateur, username, motDePasseUtilisateur, role, is_synced) 
            VALUES(?, ?, ?, ?, ?)
            """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getIdUtilisateur());
            pstmt.setString(2, utilisateur.getUsername());
            pstmt.setString(3, utilisateur.getMotDePasseUtilisateur());

            // Sécurité si aucun rôle n'est spécifié à l'enregistrement
            if (utilisateur.getRole() != null) {
                pstmt.setString(4, utilisateur.getRole().name());
            } else {
                pstmt.setString(4, Role.ECONOME.name()); // Rôle par défaut de secours
            }

            pstmt.setInt(5, utilisateur.getIsSynced());

            pstmt.executeUpdate();
        }
    }

    public void modifier(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE Utilisateur SET username = ?, role = ?, is_synced = 0 WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getUsername());
            pstmt.setString(2, utilisateur.getRole() != null ? utilisateur.getRole().name() : Role.ECONOME.name());
            pstmt.setString(3, utilisateur.getIdUtilisateur());

            pstmt.executeUpdate();
        }
    }

    public void modifierMotDePasse(String idUtilisateur, String nouveauMdp) throws SQLException {
        String sql = "UPDATE Utilisateur SET motDePasseUtilisateur = ?, is_synced = 0 WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nouveauMdp);
            pstmt.setString(2, idUtilisateur);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String idUtilisateur) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUtilisateur);
            pstmt.executeUpdate();
        }
    }

    public List<Utilisateur> listerTous() throws SQLException {
        String sql = "SELECT * FROM Utilisateur ORDER BY username ASC";
        List<Utilisateur> liste = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(construireUtilisateur(rs));
            }
        }
        return liste;
    }

    public void marquerCommeSynchronise(String idUtilisateur) throws SQLException {
        String sql = "UPDATE Utilisateur SET is_synced = 1 WHERE idUtilisateur = ?";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUtilisateur);
            pstmt.executeUpdate();
        }
    }

    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getString("idUtilisateur"));
        u.setUsername(rs.getString("username"));
        u.setMotDePasseUtilisateur(rs.getString("motDePasseUtilisateur"));

        // =============== RECOUVREMENT ET PROTECTION NULLPOINTER ===============
        String roleString = rs.getString("role");
        if (roleString != null && !roleString.trim().isEmpty()) {
            try {
                u.setRole(Role.valueOf(roleString.toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                System.err.println("Rôle invalide trouvé en BDD: " + roleString + ". Attribution du rôle ECONOME par défaut.");
                u.setRole(Role.ECONOME);
            }
        } else {
            u.setRole(Role.ECONOME);
        }
        // =======================================================================

        u.setIsSynced(rs.getInt("is_synced"));
        u.setDerniereModification(rs.getString("derniere_modification"));
        return u;
    }
}