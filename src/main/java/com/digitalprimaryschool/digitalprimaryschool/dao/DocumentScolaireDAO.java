package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.DocumentScolaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentScolaireDAO {

    public void ajouter(DocumentScolaire doc) throws SQLException {
        String sql = """
                INSERT INTO DocumentScolaire (
                    matriculeEleve, idDocuments, type,
                    dateGeneration, generePar, formatFichier
                ) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doc.getMatriculeEleve());
            stmt.setString(2, doc.getIdDocuments());
            stmt.setString(3, doc.getType());
            stmt.setString(4, doc.getGenerePar());
            stmt.setString(5, doc.getFormatFichier());
            stmt.executeUpdate();
        }
    }

    public List<DocumentScolaire> listerParEleve(String matriculeEleve) throws SQLException {
        String sql = """
                SELECT * FROM DocumentScolaire
                WHERE matriculeEleve = ?
                ORDER BY dateGeneration DESC
                """;
        List<DocumentScolaire> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matriculeEleve);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DocumentScolaire doc = new DocumentScolaire();
                    doc.setMatriculeEleve(rs.getString("matriculeEleve"));
                    doc.setIdDocuments(rs.getString("idDocuments"));
                    doc.setType(rs.getString("type"));
                    doc.setDateGeneration(rs.getString("dateGeneration"));
                    doc.setGenerePar(rs.getString("generePar"));
                    doc.setFormatFichier(rs.getString("formatFichier"));
                    liste.add(doc);
                }
            }
        }
        return liste;
    }
}