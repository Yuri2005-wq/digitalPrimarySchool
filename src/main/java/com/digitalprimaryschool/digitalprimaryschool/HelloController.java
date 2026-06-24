package com.digitalprimaryschool.digitalprimaryschool;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class HelloController {

    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnStudents;
    @FXML private Button btnClasses;
    @FXML private Button btnSettings;
    @FXML private Button btnNotes;
    @FXML private Button btnLogout;
    @FXML private Button btnEnregistrement;
    @FXML private Button btnInscription;

    @FXML
    public void initialize() {
        // Au démarrage de l'application, on charge la vue Inscription par défaut
        loadPage("Dashboard.fxml", btnDashboard);
    }
    @FXML
    void handleListEleve(ActionEvent event){
        loadPage("view/listeEleve.fxml", btnStudents);
    }
    @FXML
    void handleInscriptionMenu(ActionEvent event) {
        loadPage("inscription-view.fxml", btnInscription);
    }
    @FXML
    void handleEnregistrement(ActionEvent event){
        loadPage("view/EnregistrementEleveParent.fxml", btnEnregistrement);
    }
    @FXML
    void handleClassesMenu(ActionEvent event) {
        loadPage("view/classe-view.fxml", btnClasses);
    }

    @FXML
    void handleDashboardHome(ActionEvent event) {
        loadPage("Dashboard.fxml", btnDashboard);
    }
    @FXML
    void handleSettings(ActionEvent event){
        loadPage("view/settings-view.fxml", btnSettings);
    }
    @FXML
    void handleNotes(ActionEvent event){
        loadPage("view/listeEleve.fxml", btnNotes);
    }
    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Déconnexion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment vous déconnecter ?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Stage stageActuel = (Stage) btnLogout.getScene().getWindow();
            returnVerLogin(stageActuel);
        }

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
            btnSettings.getStyleClass().remove("active");
            btnNotes.getStyleClass().remove("active");
            btnEnregistrement.getStyleClass().remove("active");
            btnInscription.getStyleClass().remove("active");


            if (activeButton != null && !activeButton.getStyleClass().contains("active")) {
                activeButton.getStyleClass().add("active");
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la page : " + fxmlFile);
            e.printStackTrace();
        }
    }
    private void returnVerLogin(Stage currentStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Login.fxml"));
        Scene mainScene = new Scene(loader.load(), 1000, 640);


        Stage LoginStage = new Stage();
        LoginStage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));

        LoginStage.setTitle("DigitalPrimarySchool");
        LoginStage.setScene(mainScene);
        LoginStage.initStyle(StageStyle.UNDECORATED);
        LoginStage.setWidth(1000);
        LoginStage.setHeight(640);



        currentStage.close();
        LoginStage.show();
    }

}