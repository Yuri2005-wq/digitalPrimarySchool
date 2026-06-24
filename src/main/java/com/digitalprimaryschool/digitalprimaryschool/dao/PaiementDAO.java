package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Paiement;
import com.digitalprimaryschool.digitalprimaryschool.model.ModePaiement;
import com.digitalprimaryschool.digitalprimaryschool.model.recue_paiement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {

    // ================================================================
    // ENREGISTRER un paiement + son reçu (transaction atomique)
    // ================================================================
    public void enregistrerAvecRecu(Paiement paiement, recue_paiement recu,
                                    String idTarifScolaire, String idUtilisateur,
                                    String idEconome) throws SQLException {

        Connection conn = Database.getConnexion();
        conn.setAutoCommit(false);

        try {
            // 1. Insérer dans DocumentScolaire (parent de recue_paiement)
            String sqlDoc = """
                    INSERT INTO DocumentScolaire (
                        matriculeEleve, idDocuments, type,
                        dateGeneration, generePar, formatFichier
                    ) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDoc)) {
                stmt.setString(1, recu.getMatriculeEleve());
                stmt.setString(2, recu.getIdDocuments());
                stmt.setString(3, recu.getType());
                stmt.setString(4, recu.getGenerePar());
                stmt.setString(5, recu.getFormatFichier());
                stmt.executeUpdate();
            }

            // 2. Insérer dans recue_paiement
            String sqlRecu = """
                    INSERT INTO recue_paiement (matriculeEleve, idDocuments, idRecu)
                    VALUES (?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlRecu)) {
                stmt.setString(1, recu.getMatriculeEleve());
                stmt.setString(2, recu.getIdDocuments());
                stmt.setString(3, recu.getIdRecu());
                stmt.executeUpdate();
            }

            // 3. Insérer le Paiement
            String sqlPaiement = """
                    INSERT INTO Paiement (
                        reference, montant, datePaiement, modePaiement,
                        encaisserPar, idPaiement, idTarifScolaire,
                        matriculeEleve, idDocuments, idRecu,
                        idUtilisateur, idEconome
                    ) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaiement)) {
                stmt.setString(1, paiement.getReference());
                stmt.setDouble(2, paiement.getMontant());

                // MODIFICATION 1: Utiliser getModePaiement().name() pour l'enum
                stmt.setString(3, paiement.getModePaiement() != null
                        ? paiement.getModePaiement().name() : null);

                stmt.setString(4, paiement.getEncaisserPar());
                stmt.setString(5, paiement.getIdPaiement());
                stmt.setString(6, idTarifScolaire);
                stmt.setString(7, recu.getMatriculeEleve());
                stmt.setString(8, recu.getIdDocuments());
                stmt.setString(9, recu.getIdRecu());
                stmt.setString(10, idUtilisateur);
                stmt.setString(11, idEconome);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Paiement enregistré : " + paiement.getIdPaiement());

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // ================================================================
    // LISTER les paiements d'un élève
    // ================================================================
    public List<Paiement> listerParEleve(String matriculeEleve) throws SQLException {
        String sql = """
                SELECT * FROM Paiement
                WHERE matriculeEleve = ?
                ORDER BY datePaiement DESC
                """;
        List<Paiement> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matriculeEleve);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) liste.add(construirePaiement(rs));
            }
        }
        return liste;
    }

    // ================================================================
    // LISTER les paiements par mode de paiement
    // ================================================================
    public List<Paiement> listerParModePaiement(ModePaiement mode) throws SQLException {
        String sql = """
                SELECT * FROM Paiement
                WHERE modePaiement = ?
                ORDER BY datePaiement DESC
                """;
        List<Paiement> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mode.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) liste.add(construirePaiement(rs));
            }
        }
        return liste;
    }

    // ================================================================
    // LISTER les paiements par date
    // ================================================================
    public List<Paiement> listerParDate(String dateDebut, String dateFin) throws SQLException {
        String sql = """
                SELECT * FROM Paiement
                WHERE datePaiement BETWEEN ? AND ?
                ORDER BY datePaiement DESC
                """;
        List<Paiement> liste = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) liste.add(construirePaiement(rs));
            }
        }
        return liste;
    }

    // ================================================================
    // TOTAL encaissé sur une période (pour le tableau de bord)
    // ================================================================
    public double totalEncaisse(String dateDebut, String dateFin) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(montant), 0) FROM Paiement
                WHERE datePaiement BETWEEN ? AND ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    // ================================================================
    // TOTAL encaissé par mode de paiement
    // ================================================================
    public double totalParModePaiement(ModePaiement mode) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(montant), 0) FROM Paiement
                WHERE modePaiement = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mode.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    // ================================================================
    // COMPTER le nombre de paiements par mode
    // ================================================================
    public int compterParModePaiement(ModePaiement mode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Paiement WHERE modePaiement = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mode.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ================================================================
    // STATISTIQUES : répartition des paiements par mode
    // ================================================================
    public List<StatPaiementMode> statistiquesParMode() throws SQLException {
        String sql = """
                SELECT modePaiement, COUNT(*) as nombre, SUM(montant) as total
                FROM Paiement
                GROUP BY modePaiement
                ORDER BY total DESC
                """;
        List<StatPaiementMode> stats = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StatPaiementMode stat = new StatPaiementMode();
                String modeStr = rs.getString("modePaiement");
                if (modeStr != null && !modeStr.isEmpty()) {
                    try {
                        stat.setMode(ModePaiement.valueOf(modeStr));
                    } catch (IllegalArgumentException e) {
                        stat.setMode(ModePaiement.AUTRE);
                    }
                }
                stat.setNombre(rs.getInt("nombre"));
                stat.setTotal(rs.getDouble("total"));
                stats.add(stat);
            }
        }
        return stats;
    }

    // ================================================================
    // MÉTHODE PRIVÉE : construire un objet Paiement depuis un ResultSet
    // ================================================================
    private Paiement construirePaiement(ResultSet rs) throws SQLException {
        Paiement p = new Paiement();
        p.setIdPaiement(rs.getString("idPaiement"));
        p.setReference(rs.getString("reference"));
        p.setMontant(rs.getDouble("montant"));
        p.setDatePaiement(rs.getString("datePaiement"));
        p.setEncaisserPar(rs.getString("encaisserPar"));

        // MODIFICATION 2: Conversion du modePaiement (TEXT SQLite → enum ModePaiement)
        String modeStr = rs.getString("modePaiement");
        if (modeStr != null && !modeStr.isEmpty()) {
            try {
                p.setModePaiement(ModePaiement.valueOf(modeStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Mode de paiement inconnu : " + modeStr);
                p.setModePaiement(ModePaiement.AUTRE);
            }
        } else {
            p.setModePaiement(ModePaiement.AUTRE);
        }

        return p;
    }

    // ================================================================
    // CLASSE STATISTIQUE (pour les résultats d'agrégation)
    // ================================================================
    public static class StatPaiementMode {
        private ModePaiement mode;
        private int nombre;
        private double total;

        public ModePaiement getMode() { return mode; }
        public void setMode(ModePaiement mode) { this.mode = mode; }

        public int getNombre() { return nombre; }
        public void setNombre(int nombre) { this.nombre = nombre; }

        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }

        public String getModeLibelle() {
            return mode != null ? mode.getLibelle() : "";
        }
    }
}