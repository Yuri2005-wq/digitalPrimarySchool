package com.digitalprimaryschool.digitalprimaryschool;

import atlantafx.base.theme.PrimerLight;
import com.digitalprimaryschool.digitalprimaryschool.dao.ParentDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.UtilisateurDAO;
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

        Utilisateur user = new Utilisateur();
        user.setUsername("econome");
        user.setMotDePasseUtilisateur("mp3");

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        utilisateurDAO.insertUser(user);

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