package com.digitalprimaryschool.digitalprimaryschool.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class AppBarController {

    @FXML private HBox appBar;
    @FXML private FontIcon iconMaxRestore;

    // --- Drag de la fenêtre ---
    private double xOffset = 0;
    private double yOffset = 0;

    // --- Mémorisation de la position/taille avant maximisation ---
    private double restoreX, restoreY, restoreW, restoreH;
    private boolean maximise = false;

    @FXML
    public void initialize() {
        appBar.setOnMousePressed(event -> {
            Stage stage = (Stage) appBar.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        appBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) appBar.getScene().getWindow();
            if (!maximise) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });

        // Double-clic sur l'app-bar = agrandir/restaurer (comportement Windows)
        appBar.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                toggleMaximize();
            }
        });
    }

    @FXML
    public void minimiser() {
        Stage stage = (Stage) appBar.getScene().getWindow();
        stage.setIconified(true); // l'OS gère nativement l'animation de réduction
    }

    @FXML
    public void toggleMaximize() {
        Stage stage = (Stage) appBar.getScene().getWindow();

        if (!maximise) {
            // Sauvegarde l'état actuel pour pouvoir restaurer
            restoreX = stage.getX();
            restoreY = stage.getY();
            restoreW = stage.getWidth();
            restoreH = stage.getHeight();

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            animerVers(stage, bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());

            iconMaxRestore.setIconLiteral("fth-copy"); // icône "restaurer"
            maximise = true;
        } else {
            animerVers(stage, restoreX, restoreY, restoreW, restoreH);
            iconMaxRestore.setIconLiteral("fth-square"); // icône "agrandir"
            maximise = false;
        }
    }

    /**
     * Anime la position/taille de la fenêtre avec un effet "snap" fluide
     * (similaire à Windows 11 quand on agrandit/restaure).
     *
     * On utilise une Transition (et non Timeline + KeyValue) car
     * stage.xProperty(), yProperty(), widthProperty() et heightProperty()
     * sont en ReadOnlyDoubleProperty et ne peuvent pas être ciblées par KeyValue.
     */
    private void animerVers(Stage stage, double xCible, double yCible, double largeurCible, double hauteurCible) {
        final double startX = stage.getX();
        final double startY = stage.getY();
        final double startW = stage.getWidth();
        final double startH = stage.getHeight();

        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.millis(180));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                stage.setX(startX + (xCible - startX) * frac);
                stage.setY(startY + (yCible - startY) * frac);
                stage.setWidth(startW + (largeurCible - startW) * frac);
                stage.setHeight(startH + (hauteurCible - startH) * frac);
            }
        };

        transition.play();
    }

    @FXML
    public void fermer() {
        Stage stage = (Stage) appBar.getScene().getWindow();

        FadeTransition fade = new FadeTransition(Duration.millis(150), stage.getScene().getRoot());
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> stage.close());
        fade.play();
    }
}