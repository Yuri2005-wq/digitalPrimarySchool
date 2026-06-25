package com.digitalprimaryschool.digitalprimaryschool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Database {
    // Remplacer 'localhost' par l'IP fixe du serveur à l'école en production
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "digital_primary_school_db"; // Créez d'abord cette base vide sous MySQL Workbench
    private static final String USER = "root";
    private static final String PASSWORD = "yuriDjaleu";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=UTC&allowMultiQueries=true";

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnexion(); Statement stmt = conn.createStatement()) {
            InputStream is = Database.class.getResourceAsStream("DB.sql");

            if (is == null) {
                System.err.println("Le fichier DB.sql est introuvable dans les ressources.");
                return;
            }

            String sqlScript = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Découpage des requêtes par point-virgule pour MySQL
            String[] commands = sqlScript.split(";");

            for (String command : commands) {
                String trimmedCommand = command.trim();
                if (!trimmedCommand.isEmpty()) {
                    stmt.execute(trimmedCommand);
                }
            }

            System.out.println("Base de données initialisée avec succès avec l'entité Ecole !");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur d'initialisation MySQL : " + e.getMessage(), e);
        }
    }
}