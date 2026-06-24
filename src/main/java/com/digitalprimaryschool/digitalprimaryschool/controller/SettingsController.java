package com.digitalprimaryschool.digitalprimaryschool.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsController {

    @FXML private StackPane tabContentContainer;

    @FXML private Button btnTabAnnee;
    @FXML private Button btnTabTrimestre;
    @FXML private Button btnTabSequence;
    @FXML private Button btnTabTarif;
    @FXML private Button btnTabTranche;
    @FXML private Button btnTabClasse;

    private final Map<Button, String> tabViewsMap = new HashMap<>();
    private Button currentActiveButton = null;

    @FXML
    public void initialize() {
        // Liaison entre boutons et fichiers FXML correspondants
        tabViewsMap.put(btnTabAnnee, "/com/digitalprimaryschool/digitalprimaryschool/view/anneeScolaire-view.fxml");
        tabViewsMap.put(btnTabTrimestre, "/com/digitalprimaryschool/digitalprimaryschool/view/trimestre.fxml");
        tabViewsMap.put(btnTabSequence, "/com/digitalprimaryschool/digitalprimaryschool/view/sequence.fxml");
        tabViewsMap.put(btnTabTarif, "/com/digitalprimaryschool/digitalprimaryschool/view/tarif-view.fxml");
        tabViewsMap.put(btnTabTranche, "/com/digitalprimaryschool/digitalprimaryschool/view/tranches.fxml");
        tabViewsMap.put(btnTabClasse, "/com/digitalprimaryschool/digitalprimaryschool/view/classe-config.fxml");

        // Focus et chargement automatique du premier onglet (Années Scolaires) au démarrage
        mettreEnValeurBouton(btnTabAnnee);
        chargerSousVue(tabViewsMap.get(btnTabAnnee));
    }

    /**
     * Se déclenche au clic sur un des onglets de la TopBar
     */
    @FXML
    private void switchTab(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton == currentActiveButton) return;

        mettreEnValeurBouton(clickedButton);
        chargerSousVue(tabViewsMap.get(clickedButton));
    }

    private void mettreEnValeurBouton(Button boutonActif) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("active-top-tab");
        }
        boutonActif.getStyleClass().add("active-top-tab");
        currentActiveButton = boutonActif;
    }

    private void chargerSousVue(String fxmlPath) {
        try {
            tabContentContainer.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vue = loader.load();

            // Animation moderne de fondu lors du changement de vue
            FadeTransition ft = new FadeTransition(Duration.millis(150), vue);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);

            tabContentContainer.getChildren().add(vue);
            ft.play();
        } catch (IOException e) {
            System.err.println("Erreur d'injection FXML : " + fxmlPath);
            e.printStackTrace();
        }
    }
}