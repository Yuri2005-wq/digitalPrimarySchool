package com.digitalprimaryschool.digitalprimaryschool;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class SettingsController {

    @FXML private VBox modalOverlay;
    @FXML private Label modalTitle;

    @FXML
    public void showYearModal() {
        modalTitle.setText("Configuration Année Scolaire");
        modalOverlay.setVisible(true);
        // On peut ajouter une petite animation de fondu ici avec FadeTransition
    }

    @FXML
    public void closeModal() {
        modalOverlay.setVisible(false);
    }

    // Tu peux créer d'autres méthodes pour les autres boutons (Tarifs, etc.)
}