package com.digitalprimaryschool.digitalprimaryschool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException {
        Database.initializeDatabase();
        String css = getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/style.css").toExternalForm();
        stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dashboard.fxml"));
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