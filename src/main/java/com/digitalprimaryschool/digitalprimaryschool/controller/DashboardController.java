package com.digitalprimaryschool.digitalprimaryschool.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class DashboardController {

    @FXML private BarChart<String, Number> presenceChart;
    @FXML private LineChart<String, Number> averageChart;

    @FXML
    public void initialize() {
        chargerPresenceHebdomadaire();
        chargerMoyenneGenerale();
    }

    private void chargerPresenceHebdomadaire() {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Présence");

        serie.getData().add(new XYChart.Data<>("Lun", 95));
        serie.getData().add(new XYChart.Data<>("Mar", 92));
        serie.getData().add(new XYChart.Data<>("Mer", 97));
        serie.getData().add(new XYChart.Data<>("Jeu", 90));
        serie.getData().add(new XYChart.Data<>("Ven", 93));

        presenceChart.getData().add(serie);
    }

    private void chargerMoyenneGenerale() {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Moyenne");

        serie.getData().add(new XYChart.Data<>("Trim 1", 12.5));
        serie.getData().add(new XYChart.Data<>("Trim 2", 13.2));
        serie.getData().add(new XYChart.Data<>("Trim 3", 12.9));

        averageChart.getData().add(serie);
    }
}