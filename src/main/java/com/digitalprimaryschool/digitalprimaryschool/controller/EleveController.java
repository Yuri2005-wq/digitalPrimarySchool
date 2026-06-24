package com.digitalprimaryschool.digitalprimaryschool.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class EleveController {

    @FXML private TableView<EleveRow> tableauEleves;
    @FXML private ComboBox<String> comboClasse;

    @FXML
    public void initialize() {
        // 1. Initialiser et configurer les colonnes du tableau
        configurerColonnes();

        // 2. Charger les élèves de test dans le tableau
        chargerDonneesExemple();

        // 3. Configurer l'action de clic sur les lignes
        configurerDoubleClicLigne();
    }

    private void configurerColonnes() {
        // COLONNE ELEVE (Avatar + Nom/Genre)
        TableColumn<EleveRow, EleveRow> colEleve = new TableColumn<>("ÉLÈVE");
        colEleve.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colEleve.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(EleveRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10);
                    box.setAlignment(Pos.CENTER_LEFT);

                    Circle circle = new Circle(16, Color.web(item.getColorHex()));
                    Label initiales = new Label(item.getInitials());
                    initiales.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11;");
                    StackPane avatar = new StackPane(circle, initiales);

                    VBox textVbox = new VBox(2);
                    Label lblNom = new Label(item.getNom());
                    lblNom.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E293B;");
                    Label lblGenre = new Label(item.getGenre());
                    lblGenre.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11;");

                    textVbox.getChildren().addAll(lblNom, lblGenre);
                    box.getChildren().addAll(avatar, textVbox);
                    setGraphic(box);
                }
            }
        });

        // COLONNE DATE DE NAISSANCE
        TableColumn<EleveRow, String> colDate = new TableColumn<>("DATE DE NAISSANCE");
        colDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDateNaissance()));

        // COLONNE MOYENNE
        TableColumn<EleveRow, Double> colMoyenne = new TableColumn<>("MOYENNE");
        colMoyenne.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getMoyenne()).asObject());
        colMoyenne.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                    if (item >= 14) setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    else if (item >= 11) setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                    else setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                }
            }
        });

        // COLONNE ABSENCES
        TableColumn<EleveRow, Integer> colAbsences = new TableColumn<>("ABSENCES");
        colAbsences.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getAbsences()).asObject());

        // COLONNE STATUT
        TableColumn<EleveRow, String> colStatut = new TableColumn<>("STATUT");
        colStatut.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getStatut()));
        colStatut.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    if (item.equalsIgnoreCase("Actif")) {
                        badge.setStyle("-fx-background-color: #E6F4EA; -fx-text-fill: #137333; -fx-padding: 3 10; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 11;");
                    } else {
                        badge.setStyle("-fx-background-color: #F1F3F4; -fx-text-fill: #5F6368; -fx-padding: 3 10; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 11;");
                    }
                    setGraphic(badge);
                }
            }
        });

        // COLONNE CONTACT PARENT
        TableColumn<EleveRow, EleveRow> colContact = new TableColumn<>("CONTACT PARENT");
        colContact.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colContact.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(EleveRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(12);
                    actions.setAlignment(Pos.CENTER_LEFT);

                    Button btnPhone = new Button();
                    btnPhone.setGraphic(new FontIcon("fth-phone"));
                    btnPhone.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                    Button btnMail = new Button();
                    btnMail.setGraphic(new FontIcon("fth-mail"));
                    btnMail.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                    actions.getChildren().addAll(btnPhone, btnMail);
                    setGraphic(actions);
                }
            }
        });

        tableauEleves.getColumns().addAll(colEleve, colDate, colMoyenne, colAbsences, colStatut, colContact);
    }

    private void chargerDonneesExemple() {
        ObservableList<EleveRow> liste = FXCollections.observableArrayList(
                new EleveRow("Amina Benali", "Fille", "12/03/2013", 15.2, 2, "Actif", "#60A5FA"),
                new EleveRow("Lucas Dupont", "Garçon", "05/07/2013", 12.8, 5, "Actif", "#F87171"),
                new EleveRow("Fatou Kone", "Fille", "18/11/2012", 16.4, 0, "Actif", "#34D399"),
                new EleveRow("Hugo Martin", "Garçon", "22/01/2013", 11.5, 8, "Actif", "#A78BFA")
        );
        tableauEleves.setItems(liste);
    }

    /**
     * Détecte le double-clic sur une ligne du tableau pour rediriger vers le profil de l'élève
     */
    private void configurerDoubleClicLigne() {
        tableauEleves.setRowFactory(tv -> {
            TableRow<EleveRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // Déclenchement sur un double-clic et s'il y a bien des données dans la ligne
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EleveRow eleveSelectionne = row.getItem();
                    redirigerVersProfilEleve(eleveSelectionne);
                }
            });
            return row;
        });
    }

    private void redirigerVersProfilEleve(EleveRow eleve) {
        try {
            // 1. Charger le fichier FXML de la vue détaillée
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/FicheEleve.fxml"));
            Parent root = loader.load();

            // 2. (Optionnel) Passer l'élève sélectionné au nouveau contrôleur de profil
            // ProfilEleveController controller = loader.getController();
            // controller.initialiserDonnees(eleve);

            // 3. Récupérer la fenêtre actuelle (Stage) et changer la scène
            Stage stage = (Stage) tableauEleves.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil de " + eleve.getNom());
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page de profil : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==========================================
    // Classe modèle interne (Ajout des Getters nécessaires à JavaFX)
    // ==========================================
    public static class EleveRow {
        private final String nom, genre, dateNaissance, statut, colorHex;
        private final double moyenne;
        private final int absences;

        public EleveRow(String nom, String genre, String dateNaissance, double moyenne, int absences, String statut, String colorHex) {
            this.nom = nom; this.genre = genre; this.dateNaissance = dateNaissance;
            this.moyenne = moyenne; this.absences = absences; this.statut = statut; this.colorHex = colorHex;
        }

        public String getInitials() {
            String[] parts = nom.split(" ");
            if (parts.length >= 2) return "" + parts[0].charAt(0) + parts[1].charAt(0);
            return "" + nom.charAt(0);
        }

        public String getNom() { return nom; }
        public String getGenre() { return genre; }
        public String getDateNaissance() { return dateNaissance; }
        public double getMoyenne() { return moyenne; }
        public int getAbsences() { return absences; }
        public String getStatut() { return statut; }
        public String getColorHex() { return colorHex; }
    }
}