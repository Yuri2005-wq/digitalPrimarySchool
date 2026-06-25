package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Ecole;

import java.sql.*;

public class EcoleDAO {

    // 1. Insérer une nouvelle école et récupérer son ID généré
    public void insertEcole(Ecole ecole) throws SQLException {
        // CORRECTION : Utilisation de contactEcole et devise selon votre script SQL
        String sql = "INSERT INTO Ecole (nomEcole, adressePhysique, contactEcole, emailEcole, devise) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, ecole.getNomEcole());
            pstmt.setString(2, ecole.getAdresseEcole());
            pstmt.setString(3, ecole.getTelephoneEcole());
            pstmt.setString(4, ecole.getEmailEcole());
            pstmt.setString(5, ecole.getDeviseEcole());

            pstmt.executeUpdate();

            // Puisque idEcole est un INT AUTO_INCREMENT, on récupère la clé générée par MySQL
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // On convertit l'int reçu en String pour votre modèle Ecole
                    ecole.setIdEcole(String.valueOf(generatedKeys.getInt(1)));
                }
            }
        }
    }

    // 2. Récupérer la première école enregistrée
    public Ecole getPremiereEcole() throws SQLException {
        String sql = "SELECT * FROM Ecole LIMIT 1";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Ecole ecole = new Ecole();
                ecole.setIdEcole(String.valueOf(rs.getInt("idEcole")));
                ecole.setNomEcole(rs.getString("nomEcole"));
                ecole.setAdresseEcole(rs.getString("adressePhysique"));
                ecole.setTelephoneEcole(rs.getString("contactEcole"));
                ecole.setEmailEcole(rs.getString("emailEcole"));
                ecole.setDeviseEcole(rs.getString("devise"));
                return ecole;
            }
        }
        return null;
    }

    // 3. Trouver une école par son ID
    public Ecole getEcoleById(String idEcole) throws SQLException {
        String sql = "SELECT * FROM Ecole WHERE idEcole = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Conversion du String en Int pour la requête SQL
            pstmt.setInt(1, Integer.parseInt(idEcole));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ecole ecole = new Ecole();
                    ecole.setIdEcole(String.valueOf(rs.getInt("idEcole")));
                    ecole.setNomEcole(rs.getString("nomEcole"));
                    ecole.setAdresseEcole(rs.getString("adressePhysique"));
                    ecole.setTelephoneEcole(rs.getString("contactEcole"));
                    ecole.setEmailEcole(rs.getString("emailEcole"));
                    ecole.setDeviseEcole(rs.getString("devise"));
                    return ecole;
                }
            }
        }
        return null;
    }
}