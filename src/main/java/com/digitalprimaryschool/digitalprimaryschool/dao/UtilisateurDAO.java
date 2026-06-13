package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    public Utilisateur verifierIdentifiants(String username, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE username = ? AND  motDePasseUtilisateur= ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, motDePasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setIdUtilisateur(rs.getString("idUtilisateur"));
                    utilisateur.setUsername(rs.getString("username"));
                    utilisateur.setMotDePasseUtilisateur(rs.getString("motDePasseUtilisateur"));
                    return utilisateur;
                }
            }
        }
        return null; // identifiants incorrects
    }

    public void insertUser(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO Utilisateur(idUtilisateur, username, motDePasseUtilisateur) VALUES(?, ?, ?)";
        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur.getIdUtilisateur());
            pstmt.setString(2, utilisateur.getUsername());
            pstmt.setString(3, utilisateur.getMotDePasseUtilisateur());

            pstmt.executeUpdate();
        }
    }
}