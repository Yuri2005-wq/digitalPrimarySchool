package com.digitalprimaryschool.digitalprimaryschool.service;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import java.sql.*;

public class SyncService {

    private static final String URL_DISTANTE = "jdbc:mysql://ton-serveur.com:3306/edugestion";
    private static final String USER_DISTANT = "ton_user";
    private static final String PASS_DISTANT = "ton_password";

    public void synchroniser() {
        if (!ConnectiviteService.estConnecte()) {
            System.out.println("Pas de connexion internet — synchronisation ignorée.");
            return;
        }

        try (Connection connDistante = DriverManager.getConnection(URL_DISTANTE, USER_DISTANT, PASS_DISTANT)) {

            // ==========================================
            // FLUX 1 : UPLOAD (Local -> En ligne)
            // ==========================================
            synchroniserTable("Parent", connDistante);
            synchroniserTable("Eleve", connDistante);
            synchroniserTable("Inscription", connDistante);
            synchroniserTable("Paiement", connDistante);

            // ==========================================
            // FLUX 2 : DOWNLOAD (En ligne -> Local)
            // ==========================================
            telechargerDepuisDistant("Parent", "idParent", connDistante);
            telechargerDepuisDistant("Eleve", "matriculeEleve", connDistante);
            telechargerDepuisDistant("Inscription", "idInscription", connDistante);
            telechargerDepuisDistant("Paiement", "idPaiement", connDistante);

            System.out.println("Synchronisation bidirectionnelle terminée avec succès.");

        } catch (SQLException e) {
            System.err.println("Erreur de synchronisation : " + e.getMessage());
        }
    }

    // --- Garde les méthodes d'upload existantes de la réponse de Claude ici ---
    private void synchroniserTable(String nomTable, Connection connDistante) throws SQLException { /* ... */ }
    private void enviarLigneVersDistant() { /* ... */ }
    private void marquerCommeSynchronise() { /* ... */ }

    // =========================================================================
    // NOUVELLE MÉTHODE : EN LIGNE -> LOCAL (DOWNLOAD)
    // =========================================================================
    private void telechargerDepuisDistant(String nomTable, String clePrimaire, Connection connDistante) throws SQLException {
        try (Connection connLocale = Database.getConnexion()) {

            // 1. Trouver la date de la dernière modification connue en local
            String sqlDerniereDateLocal = "SELECT MAX(derniere_modification) FROM " + nomTable;
            String derniereModifLocale = "1970-01-01 00:00:00"; // Par défaut si la table est vide

            try (Statement stmt = connLocale.createStatement();
                 ResultSet rsDate = stmt.executeQuery(sqlDerniereDateLocal)) {
                if (rsDate.next() && rsDate.getString(1) != null) {
                    derniereModifLocale = rsDate.getString(1);
                }
            }

            // 2. Demander au serveur les lignes modifiées APRÈS cette date
            String sqlSelectDistant = "SELECT * FROM " + nomTable + " WHERE derniere_modification > ?";

            try (PreparedStatement pstmtDistant = connDistante.prepareStatement(sqlSelectDistant)) {
                pstmtDistant.setString(1, derniereModifLocale);

                try (ResultSet rsDistand = pstmtDistant.executeQuery()) {
                    ResultSetMetaData meta = rsDistand.getMetaData();
                    int nbColonnes = meta.getColumnCount();

                    while (rsDistand.next()) {
                        // 3. Appliquer ou mettre à jour la ligne récupérée dans SQLite local
                        appliquerLigneEnLocal(nomTable, clePrimaire, rsDistand, meta, nbColonnes, connLocale);
                    }
                }
            }
        }
    }

    // =========================================================================
    // ÉCRITURE DANS SQLITE (Fonctionne comme le ON DUPLICATE KEY de MySQL)
    // =========================================================================
    private void appliquerLigneEnLocal(String table, String clePrimaire, ResultSet rsDistant,
                                       ResultSetMetaData meta, int nbColonnes, Connection connLocale) throws SQLException {

        StringBuilder colonnes = new StringBuilder();
        StringBuilder valeurs = new StringBuilder();

        for (int i = 1; i <= nbColonnes; i++) {
            if (i > 1) {
                colonnes.append(", ");
                valeurs.append(", ");
            }
            colonnes.append(meta.getColumnName(i));
            valeurs.append("?");
        }

        // SQLite utilise "INSERT OR REPLACE" au lieu de "ON DUPLICATE KEY UPDATE"
        // C'est l'équivalent parfait et ultra-rapide sous SQLite.
        String sqlInsertOrReplace = "INSERT OR REPLACE INTO " + table + " (" + colonnes + ") VALUES (" + valeurs + ")";

        try (PreparedStatement stmt = connLocale.prepareStatement(sqlInsertOrReplace)) {
            for (int i = 1; i <= nbColonnes; i++) {
                // Si la colonne est 'is_synced', on la force à 1 en local puisque la donnée vient du cloud
                if (meta.getColumnName(i).equalsIgnoreCase("is_synced")) {
                    stmt.setInt(i, 1);
                } else {
                    stmt.setObject(i, rsDistant.getObject(i));
                }
            }
            stmt.executeUpdate();
        }
    }
}