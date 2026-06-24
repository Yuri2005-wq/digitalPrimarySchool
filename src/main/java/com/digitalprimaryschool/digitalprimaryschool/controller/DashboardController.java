package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML private Label lblTotalEleves;
    @FXML private Label lblTotalEnseignants;
    @FXML private Label lblTotalClasses;

    @FXML private BarChart<String, Number> presenceChart;
    @FXML private LineChart<String, Number> averageChart;

    @FXML
    public void initialize() {
        chargerStatistiquesGlobales();
        chargerGraphiquePresence();
        chargerGraphiqueMoyennes();
    }

    /**
     * Exécute les requêtes COUNT directes sur ton schéma SQLite
     */
    private void chargerStatistiquesGlobales() {
        try (Connection conn = Database.getConnexion()) {

            // 1. Compter les élèves
            String sqlEleves = "SELECT COUNT(*) FROM Eleve";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEleves);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblTotalEleves.setText(String.format("%,d", rs.getInt(1)));
                }
            }

            // 2. Compter les enseignants
            String sqlEnseignants = "SELECT COUNT(*) FROM Enseignant";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEnseignants);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblTotalEnseignants.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // 3. Compter les classes
            String sqlClasses = "SELECT COUNT(*) FROM Classe";
            try (PreparedStatement stmt = conn.prepareStatement(sqlClasses);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lblTotalClasses.setText(String.valueOf(rs.getInt(1)));
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des compteurs du Dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Alimente le BarChart de présence (Exemple de données hebdomadaires stabilisées)
     */
    private void chargerGraphiquePresence() {
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Taux de présence (%)");

        dataSeries.getData().add(new XYChart.Data<>("Lun", 95.2));
        dataSeries.getData().add(new XYChart.Data<>("Mar", 94.1));
        dataSeries.getData().add(new XYChart.Data<>("Mer", 96.0));
        dataSeries.getData().add(new XYChart.Data<>("Jeu", 92.5));
        dataSeries.getData().add(new XYChart.Data<>("Ven", 89.8));

        presenceChart.getData().add(dataSeries);
    }

    /**
     * Alimente le LineChart des moyennes par niveau ou par section académique
     */
    private void chargerGraphiqueMoyennes() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Moyenne Générale");

        // Extraction de la tendance moyenne calculée depuis la table des Bulletins de ton SQLite
        String sqlMoyenneEvolution = """
            SELECT c.niveau, AVG(b.moyenneGenerale) as moyenne 
            FROM Bulletin b
            JOIN Inscription i ON b.matriculeEleve = i.matriculeEleve
            JOIN Classe c ON i.idClasse = c.idClasse
            GROUP BY c.niveau
        """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sqlMoyenneEvolution);
             ResultSet rs = stmt.executeQuery()) {

            boolean aDesDonnees = false;
            while (rs.next()) {
                String niveau = rs.getString("niveau");
                double moyenne = rs.getDouble("moyenne");
                if (niveau != null) {
                    series.getData().add(new XYChart.Data<>(niveau, moyenne));
                    aDesDonnees = true;
                }
            }

            // Fallback (Données de secours) si ta table Bulletin est vide lors du premier lancement
            if (!aDesDonnees) {
                series.getData().add(new XYChart.Data<>("SIL", 14.5));
                series.getData().add(new XYChart.Data<>("CP", 13.2));
                series.getData().add(new XYChart.Data<>("CE1", 12.8));
                series.getData().add(new XYChart.Data<>("CE2", 15.1));
                series.getData().add(new XYChart.Data<>("CM1", 11.4));
                series.getData().add(new XYChart.Data<>("CM2", 13.9));
            }

            averageChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}