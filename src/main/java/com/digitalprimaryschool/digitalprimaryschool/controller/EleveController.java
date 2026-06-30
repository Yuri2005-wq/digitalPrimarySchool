package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class EleveController {

    @FXML private TableView<Eleve> tableauEleves; // Utilise directement Eleve
    @FXML private ComboBox<Classe> comboClasse;
    @FXML private TextField txtRecherche;

    private final EnregistrementService enregistrementService = new EnregistrementService();
    private final ObservableList<Eleve> masterData = FXCollections.observableArrayList();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // 1. Initialiser et configurer les colonnes pour le modèle Eleve
        configurerColonnes();

        // 2. Charger dynamiquement les classes de l'école dans le ComboBox
        chargerComboBoxClasses();

        // 3. Configurer le filtrage textuel (Barre de recherche)
        configurerBarreRecherche();

        // 4. Configurer l'action de double-clic
        configurerDoubleClicLigne();

        // 5. Charger tous les élèves de l'école par défaut au démarrage
        rafraichirListeEleves();
    }

    private void chargerComboBoxClasses() {
        try {
            List<Classe> listeClasses = enregistrementService.getToutesLesClasses();
            comboClasse.setItems(FXCollections.observableArrayList(listeClasses));

            comboClasse.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Classe item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(item.getNom());
                }
            });

            comboClasse.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Classe item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(item.getNom());
                }
            });

            comboClasse.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    chargerElevesParClasse(newVal.getIdClasse());
                }
            });
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les classes : " + e.getMessage());
        }
    }

    private void rafraichirListeEleves() {
        try {
            masterData.clear();
            masterData.addAll(enregistrementService.getTousLesEleves());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les élèves : " + e.getMessage());
        }
    }

    private void chargerElevesParClasse(String idClasse) {
        try {
            masterData.clear();
            // Utilise la méthode d'enregistrementService liée au EleveDAO mis à jour
            masterData.addAll(enregistrementService.getElevesParParent(idClasse));
            // Note : Si tu as besoin d'une méthode dédiée par classe, utilise eleveDAO.listerParClasse(idClasse)
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du filtrage par classe.");
        }
    }

    private void configurerBarreRecherche() {
        FilteredList<Eleve> filteredData = new FilteredList<>(masterData, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(eleve -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return eleve.getNom().toLowerCase().contains(lower) ||
                        eleve.getPrenom().toLowerCase().contains(lower) ||
                        eleve.getMatricule().toLowerCase().contains(lower);
            });
        });
        tableauEleves.setItems(filteredData);
    }

    private void configurerColonnes() {
        tableauEleves.getColumns().clear();

        // COLONNE ELEVE (Génère l'avatar dynamiquement à partir du Nom/Prénom de l'entité)
        TableColumn<Eleve, Eleve> colEleve = new TableColumn<>("ÉLÈVE");
        colEleve.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colEleve.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Eleve item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10);
                    box.setAlignment(Pos.CENTER_LEFT);

                    // Génération dynamique des initiales
                    String init = (item.getNom().isEmpty() ? "" : item.getNom().substring(0,1)) +
                            (item.getPrenom().isEmpty() ? "" : item.getPrenom().substring(0,1));

                    Circle circle = new Circle(16, Color.web("#4F46E5")); // Couleur thématique unique
                    Label initiales = new Label(init.toUpperCase());
                    initiales.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11;");
                    StackPane avatar = new StackPane(circle, initiales);

                    VBox textVbox = new VBox(2);
                    Label lblNom = new Label(item.getNom() + " " + item.getPrenom());
                    lblNom.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E293B;");
                    Label lblGenre = new Label(item.getSexe() != null ? item.getSexe().getLibelle() : "Non spécifié");
                    lblGenre.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11;");

                    textVbox.getChildren().addAll(lblNom, lblGenre);
                    box.getChildren().addAll(avatar, textVbox);
                    setGraphic(box);
                }
            }
        });

        // COLONNE MATRICULE
        TableColumn<Eleve, String> colMatricule = new TableColumn<>("MATRICULE");
        colMatricule.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMatricule()));

        // COLONNE DATE DE NAISSANCE
        TableColumn<Eleve, String> colDate = new TableColumn<>("DATE DE NAISSANCE");
        colDate.setCellValueFactory(p -> {
            if (p.getValue().getDateNaissance() != null) {
                return new SimpleStringProperty(dateFormat.format(p.getValue().getDateNaissance()));
            }
            return new SimpleStringProperty("-");
        });

        // COLONNE ACTIONS / CONTACT
        TableColumn<Eleve, Eleve> colContact = new TableColumn<>("CONTACT PARENT");
        colContact.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colContact.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Eleve item, boolean empty) {
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

        tableauEleves.getColumns().addAll(colEleve, colMatricule, colDate, colContact);
    }

    private void configurerDoubleClicLigne() {
        tableauEleves.setRowFactory(tv -> {
            TableRow<Eleve> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Eleve eleveSelectionne = row.getItem();
                    redirigerVersProfilEleve(eleveSelectionne);
                }
            });
            return row;
        });
    }

    private void redirigerVersProfilEleve(Eleve eleve) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/FicheEleve.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableauEleves.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil de " + eleve.getNom());
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'affichage", "Impossible de charger l'écran FicheEleve.fxml.");
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}