package com.digitalprimaryschool.digitalprimaryschool;

import atlantafx.base.theme.PrimerLight;
import com.digitalprimaryschool.digitalprimaryschool.dao.EcoleDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.ParentDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.UtilisateurDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Ecole;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Database.initializeDatabase();
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        String css = getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/style.css").toExternalForm();
        stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 640);
        scene.getStylesheets().add(css);

        // Assurez-vous que ces lignes d'importation sont bien présentes tout en haut du fichier HelloApplication.java :
// import com.digitalprimaryschool.digitalprimaryschool.dao.EcoleDAO;
// import com.digitalprimaryschool.digitalprimaryschool.model.Ecole;

        EcoleDAO ecoleDAO = new EcoleDAO();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        try {
            // 1. Création et configuration de l'école (Utilisation de setNomEcole)
            Ecole ecole = new Ecole();
            ecole.setNomEcole("Digital Primary School");
            ecole.setAdresseEcole("Douala, Cameroun");

            // 2. Sauvegarde de l'école (Utilisation de insertEcole)
            ecoleDAO.insertEcole(ecole);
            System.out.println("École créée avec succès ! ID: " + ecole.getIdEcole());

            // 3. Création de l'utilisateur lié à cette école
            Utilisateur user = new Utilisateur();
            user.setUsername("econome");
            user.setMotDePasseUtilisateur("mp3");
            user.setIdEcole(ecole.getIdEcole()); // Liaison via l'UUID généré

            // 4. Sauvegarde de l'utilisateur
            utilisateurDAO.insertUser(user);
            System.out.println("Utilisateur économe créé et rattaché à l'école !");

        } catch (SQLException e) {
            System.err.println("Erreur d'initialisation : " + e.getMessage());
            e.printStackTrace();
        }

        // --- Fenêtre de Login : taille fixe + sans bordure native ---
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setWidth(1000);
        stage.setHeight(640);

        stage.setTitle("DigitalPrimarySchool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}