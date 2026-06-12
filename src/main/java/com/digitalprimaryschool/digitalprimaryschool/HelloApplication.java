package com.digitalprimaryschool.digitalprimaryschool;

import atlantafx.base.theme.PrimerLight;
import com.digitalprimaryschool.digitalprimaryschool.dao.ParentDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Database.initializeDatabase();
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        String css = getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/style.css").toExternalForm();
        stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 740);
        scene.getStylesheets().add(css);
        stage.setMinHeight(740);
        stage.setMinWidth(1080);
        stage.setTitle("DigitalPrimarySchool");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){

        launch(args);
    }
}