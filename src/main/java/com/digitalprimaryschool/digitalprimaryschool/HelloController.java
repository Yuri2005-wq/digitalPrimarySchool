package com.digitalprimaryschool.digitalprimaryschool;

import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;
import com.digitalprimaryschool.digitalprimaryschool.security.AccessManager;
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
        Utilisateur user = Session.get();
        if (user != null) {
            // Restriction d'affichage des boutons FXML selon les droits
            btnSettings.setVisible(AccessManager.estAutorise("ACTION_CONFIG_ECOLE"));
            btnInscription.setVisible(AccessManager.estAutorise("ACTION_INSCRIRE_ELEVE"));
            btnEnregistrement.setVisible(AccessManager.estAutorise("ACTION_INSCRIRE_ELEVE"));
            btnNotes.setVisible(AccessManager.estAutorise("ACTION_SAISIE_NOTES"));
        }

        // Chargement de l'accueil par défaut
        loadPage("Dashboard.fxml", btnDashboard);
    }

    @FXML
    void handleListEleve(ActionEvent event){
        loadPage("view/listeEleve.fxml", btnStudents);
    }

    @FXML
    void handleInscriptionMenu(ActionEvent event) {
        if (AccessManager.estAutorise("ACTION_INSCRIRE_ELEVE")) {
            loadPage("inscription-view.fxml", btnInscription);
        }
    }

    @FXML
    void handleEnregistrement(ActionEvent event){
        if (AccessManager.estAutorise("ACTION_INSCRIRE_ELEVE")) {
            loadPage("view/EnregistrementEleveParent.fxml", btnEnregistrement);
        }
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
        if (AccessManager.estAutorise("ACTION_CONFIG_ECOLE")) {
            loadPage("view/settings-view.fxml", btnSettings);
        }
    }

    @FXML
    void handleNotes(ActionEvent event){
        if (AccessManager.estAutorise("ACTION_SAISIE_NOTES")) {
            loadPage("view/listeEleve.fxml", btnNotes);
        }
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Déconnexion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment vous déconnecter ?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Nettoyage de la session globale avant retour
            Session.fermer();
            Stage stageActuel = (Stage) btnLogout.getScene().getWindow();
            returnVerLogin(stageActuel);
        }
    }

    private void loadPage(String fxmlFile, Button activeButton) {
        try {
            Node newNode = FXMLLoader.load(getClass().getResource(fxmlFile));

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newNode);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            contentArea.getChildren().setAll(newNode);
            fadeIn.play();

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

        Stage loginStage = new Stage();
        loginStage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));

        loginStage.setTitle("DigitalPrimarySchool");
        loginStage.setScene(mainScene);
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setWidth(1000);
        loginStage.setHeight(640);

        currentStage.close();
        loginStage.show();
    }
}