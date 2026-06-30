package com.digitalprimaryschool.digitalprimaryschool;

import com.digitalprimaryschool.digitalprimaryschool.dao.UtilisateurDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private Button btnConnecter;
    @FXML private HBox loginAppBar;

    @FXML private TextField champEmail;
    @FXML private PasswordField champMotDePasse;
    @FXML private TextField champMotDePasseVisible;
    @FXML private SVGPath iconeOeil;
    @FXML private Label labelErreur;

    private double xOffset = 0;
    private double yOffset = 0;
    private boolean motDePasseVisible = false;

    private static final String OEIL_OUVERT =
            "M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6z";
    private static final String OEIL_FERME =
            "M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a18.5 18.5 0 0 1 5.06-5.94 " +
                    "M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19 " +
                    "M1 1l22 22";

    @FXML
    public void initialize() {
        loginAppBar.setOnMousePressed(event -> {
            Stage stage = (Stage) loginAppBar.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        loginAppBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) loginAppBar.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) loginAppBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void toggleMotDePasse() {
        motDePasseVisible = !motDePasseVisible;

        if (motDePasseVisible) {
            champMotDePasseVisible.setText(champMotDePasse.getText());
            champMotDePasse.setVisible(false);
            champMotDePasse.setManaged(false);
            champMotDePasseVisible.setVisible(true);
            champMotDePasseVisible.setManaged(true);
            iconeOeil.setContent(OEIL_FERME);
        } else {
            champMotDePasse.setText(champMotDePasseVisible.getText());
            champMotDePasseVisible.setVisible(false);
            champMotDePasseVisible.setManaged(false);
            champMotDePasse.setVisible(true);
            champMotDePasse.setManaged(true);
            iconeOeil.setContent(OEIL_OUVERT);
        }
    }

    @FXML
    public void handleLogin() {
        String login = champEmail.getText().trim();
        String motDePasse = motDePasseVisible ? champMotDePasseVisible.getText() : champMotDePasse.getText();

        if (login.isEmpty() || motDePasse.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        try {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            Utilisateur utilisateur = utilisateurDAO.verifierIdentifiants(login, motDePasse);

            if (utilisateur != null) {
                // Ouverture globale de la session
                Session.ouvrir(utilisateur);
                utilisateur.seConnecter();

                Stage currentStage = (Stage) btnConnecter.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Scene mainScene = new Scene(loader.load(), 1080, 740);

                Stage appStage = new Stage();
                appStage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));

                appStage.setTitle("DigitalPrimarySchool");
                appStage.setScene(mainScene);
                appStage.initStyle(StageStyle.UNDECORATED);

                currentStage.close();
                appStage.show();

            } else {
                afficherErreur("Login ou mot de passe incorrect.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement du tableau de bord.");
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de connexion à la base de données.");
        }
    }

    private void afficherErreur(String message) {
        labelErreur.setText(message);
        labelErreur.setVisible(true);
        labelErreur.setManaged(true);
    }
}