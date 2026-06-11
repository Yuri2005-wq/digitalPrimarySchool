package com.digitalprimaryschool.digitalprimaryschool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;

public class HelloController {

    @FXML private VBox contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnClasses;
    @FXML private Button btnRegisterStudent;

    @FXML
    public void initialize() {
        // CORRIGÉ : On appelle "home.fxml", qui est le vrai fichier présent dans ton dossier view
        showPage("home.fxml");
    }

    @FXML
    void handleDashboardHome(ActionEvent event) {
        setActiveTab(btnDashboard);
        showPage("home.fxml");
    }

    @FXML
    void handleClassesMenu(ActionEvent event) {
        setActiveTab(btnClasses);
        showPage("ClassesList.fxml"); // Assure-toi que ce fichier existe ou remplace par le bon nom (ex: ListeInscriptions.fxml)
    }

    @FXML
    void handleAddStudentMenu(ActionEvent event) {
        setActiveTab(btnRegisterStudent);
        showPage("AddStudentForm.fxml");
    }

    /**
     * Méthode générique de navigation corrigée pour ton arborescence
     */
    private void showPage(String fxmlFileName) {
        try {
            if (contentArea == null) {
                System.err.println("Erreur critique : contentArea n'est pas injecté.");
                return;
            }
            contentArea.getChildren().clear();

            // CORRIGÉ : On utilise un chemin relatif basé sur l'emplacement de ton Dashboard.fxml
            // Puisque Dashboard.fxml et le dossier 'view' sont au même niveau, on entre simplement dans /view/
            URL fxmlLocation = getClass().getResource("view/" + fxmlFileName);

            if (fxmlLocation == null) {
                System.err.println("❌ Erreur : Impossible de trouver le fichier [" + fxmlFileName + "].");
                System.err.println("Vérifie qu'il est bien orthographié dans ton dossier 'view'.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent view = loader.load();
            contentArea.getChildren().add(view);

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement du fichier FXML [" + fxmlFileName + "] : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère l'état visuel de la barre latérale (.active)
     */
    private void setActiveTab(Button activeButton) {
        if (btnDashboard != null) btnDashboard.getStyleClass().remove("active");
        if (btnClasses != null) btnClasses.getStyleClass().remove("active");
        if (btnRegisterStudent != null) btnRegisterStudent.getStyleClass().remove("active");

        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
    }
}