package com.digitalprimaryschool.digitalprimaryschool;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class HelloController {

    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnStudents;
    @FXML private Button btnClasses;

    @FXML
    public void initialize() {
        // Au démarrage de l'application, on charge la vue Inscription par défaut
        loadPage("Dashboard.fxml", btnStudents);
    }

    @FXML
    void handleInscriptionMenu(ActionEvent event) {
        loadPage("view/parent-view.fxml", btnStudents);
    }

    @FXML
    void handleClassesMenu(ActionEvent event) {
        loadPage("view/enseignant-view.fxml", btnClasses);
    }

    @FXML
    void handleDashboardHome(ActionEvent event) {
        // Ajouter ta logique de chargement de tableau de bord ici si nécessaire
    }

    private void loadPage(String fxmlFile, Button activeButton) {
        try {
            // 1. Charger la nouvelle vue
            Node newNode = FXMLLoader.load(getClass().getResource(fxmlFile));

            // 2. Appliquer l'animation de fondu fluide (Fade In)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newNode);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            // 3. Injecter la vue au centre
            contentArea.getChildren().setAll(newNode);
            fadeIn.play();

            // 4. Gestion de la Navbar lumineuse
            btnDashboard.getStyleClass().remove("active");
            btnStudents.getStyleClass().remove("active");
            btnClasses.getStyleClass().remove("active");

            if (activeButton != null && !activeButton.getStyleClass().contains("active")) {
                activeButton.getStyleClass().add("active");
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la page : " + fxmlFile);
            e.printStackTrace();
        }
    }

}