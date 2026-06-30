package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.HelloApplication;
import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ClassesViewController {

    @FXML private StackPane rootStackPane;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboClasse;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Label lblCompteur;
    @FXML private Label lblTotalClasses;
    @FXML private Label lblTotalEleves;
    @FXML private Label lblMoyenneGenerale;
    @FXML private Label lblTotalNiveaux;
    @FXML private VBox vboxConteneurNiveaux;

    @FXML private TableView<Classe> tableClasse;
    @FXML private TableColumn<Classe, Object> colNom;
    @FXML private TableColumn<Classe, Object> colNiveauClasse;
    @FXML private TableColumn<Classe, Object> colSection;
    @FXML private TableColumn<Classe, Object> colEnseignant;
    @FXML private TableColumn<Classe, Object> colNombreEleve;
    @FXML private TableColumn<Classe, Object> colStatut;

    private final ClasseDAO classeDAO = new ClasseDAO();
    private final EleveDAO eleveDAO = new EleveDAO();
    private final ObservableList<Classe> masterData = FXCollections.observableArrayList();
    private FilteredList<Classe> filteredData;

    private final Map<String, String> mapIdClasseVersNom = new HashMap<>();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerFiltres();
        chargerDonneesBDD();
        mettreAJourStatistiques();

        // Double-clic pour ouvrir le détail
        tableClasse.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableClasse.getSelectionModel().getSelectedItem() != null) {
                ouvrirDetailClasse(tableClasse.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurerColonnes() {
        colNom.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colNiveauClasse.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colSection.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colEnseignant.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colNombreEleve.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colStatut.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        // Colonne Nom avec avatar
        colNom.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Classe c = (Classe) item;
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    Label avatar = new Label("📚");
                    avatar.setStyle("-fx-font-size: 20px;");

                    VBox infoBox = new VBox(2);
                    Label nomLabel = new Label(c.getNom());
                    nomLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a; -fx-font-size: 14px;");

                    Label detailLabel = new Label(c.getNiveau().getLibelle() + " - " + c.getSection().getLibelle());
                    detailLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

                    infoBox.getChildren().addAll(nomLabel, detailLabel);
                    hbox.getChildren().addAll(avatar, infoBox);
                    setGraphic(hbox);
                }
            }
        });

        // Colonne Niveau Classe
        colNiveauClasse.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Classe c = (Classe) item;
                    setText(c.getNiveau().getLibelle());
                }
            }
        });

        // Colonne Section
        colSection.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Classe c = (Classe) item;
                    setText(c.getSection().getLibelle());
                }
            }
        });

        // Colonne Enseignant
        colEnseignant.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Classe c = (Classe) item;
                    setText(c.getEnseignantNom() != null ? c.getEnseignantNom() : "Non assigné");
                }
            }
        });

        // Colonne Nombre d'élèves
        colNombreEleve.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Classe c = (Classe) item;
                    int nbEleves = c.getNombreEleve();
                    setText(String.valueOf(nbEleves));
                }
            }
        });

        // Colonne Statut
        colStatut.setCellFactory(column -> new TableCell<Classe, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Classe c = (Classe) item;
                    int nbEleves = c.getNombreEleve();
                    boolean estActive = nbEleves > 0;
                    Label badge = new Label(estActive ? "Active" : "Vide");
                    badge.setStyle(estActive
                            ? "-fx-background-color: #e6f4ea; -fx-text-fill: #137333; -fx-padding: 4 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;"
                            : "-fx-background-color: #fce8e6; -fx-text-fill: #c5221f; -fx-padding: 4 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;"
                    );
                    setGraphic(badge);
                }
            }
        });
    }

    private void chargerFiltres() {
        comboStatut.setItems(FXCollections.observableArrayList("Tous", "Actif", "Vide"));
        comboStatut.getSelectionModel().selectFirst();

        try {
            ObservableList<String> classesRecuperees = FXCollections.observableArrayList("Toutes");
            mapIdClasseVersNom.clear();
            for (Classe c : classeDAO.listerToutes()) {
                classesRecuperees.add(c.getNom());
                mapIdClasseVersNom.put(c.getIdClasse(), c.getNom());
            }
            comboClasse.setItems(classesRecuperees);
            comboClasse.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            comboClasse.setItems(FXCollections.observableArrayList("Toutes"));
        }
    }

    private void chargerDonneesBDD() {
        try {
            masterData.clear();
            // Utiliser la méthode avec effectif
            masterData.addAll(classeDAO.listerToutesAvecEffectif());

            filteredData = new FilteredList<>(masterData, p -> true);

            txtRecherche.textProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
            comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
            comboStatut.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());

            tableClasse.setItems(filteredData);
            mettreAJourCompteur();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Erreur lors du chargement des classes : " + e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void appliquerFiltres() {
        String recherche = txtRecherche.getText() == null ? "" : txtRecherche.getText().toLowerCase().trim();
        String classeFiltre = comboClasse.getValue();
        String statutFiltre = comboStatut.getValue();

        filteredData.setPredicate(classe -> {
            // Filtre par recherche
            if (!recherche.isEmpty()) {
                boolean match = (classe.getNom() != null && classe.getNom().toLowerCase().contains(recherche))
                        || (classe.getEnseignantNom() != null && classe.getEnseignantNom().toLowerCase().contains(recherche));
                if (!match) return false;
            }

            // Filtre par classe
            if (classeFiltre != null && !classeFiltre.equals("Toutes")) {
                if (!classe.getNom().equalsIgnoreCase(classeFiltre)) return false;
            }

            // Filtre par statut
            if (statutFiltre != null && !statutFiltre.equals("Tous")) {
                int nbEleves = classe.getNombreEleve();
                boolean aDesEleves = nbEleves > 0;
                if (statutFiltre.equals("Actif") && !aDesEleves) return false;
                if (statutFiltre.equals("Vide") && aDesEleves) return false;
            }

            return true;
        });

        mettreAJourCompteur();
        mettreAJourStatistiques();
    }

    private void mettreAJourCompteur() {
        lblCompteur.setText(filteredData.size() + " classe(s) trouvée(s)");
    }

    private void mettreAJourStatistiques() {
        try {
            int totalClasses = classeDAO.compterTotalClasses();
            int totalEleves = classeDAO.compterTotalEleves();
            int totalNiveaux = classeDAO.compterNiveauxDistincts();

            lblTotalClasses.setText(String.valueOf(totalClasses));
            lblTotalEleves.setText(String.valueOf(totalEleves));
            lblTotalNiveaux.setText(String.valueOf(totalNiveaux));

            // Moyenne générale (à adapter selon votre logique)
            lblMoyenneGenerale.setText("--");

        } catch (SQLException e) {
            e.printStackTrace();
            lblTotalClasses.setText("0");
            lblTotalEleves.setText("0");
            lblTotalNiveaux.setText("0");
            lblMoyenneGenerale.setText("--");
        }
    }

    private void ouvrirDetailClasse(Classe classe) {
        try {
            // Charger le FXML de la vue détaillée
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/DetailClasseView.fxml"));

            // Si le chemin est null, essayer avec HelloApplication
            if (loader.getLocation() == null) {
                loader = new FXMLLoader(HelloApplication.class.getResource("view/DetailClasseView.fxml"));
            }

            Parent detailView = loader.load();

            DetailClasseController controller = loader.getController();
            controller.setClasse(classe);
            controller.setActionRetour(() -> {
                rootStackPane.getChildren().remove(detailView);
                chargerDonneesBDD();
                mettreAJourStatistiques();
            });

            rootStackPane.getChildren().add(detailView);

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger la fiche détaillée");
            alert.setContentText("Erreur : " + e.getMessage() + "\nVérifiez que le fichier DetailClasseView.fxml existe.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAfficherTout() {
        txtRecherche.clear();
        comboClasse.getSelectionModel().selectFirst();
        comboStatut.getSelectionModel().selectFirst();
        chargerDonneesBDD();
    }

    @FXML
    private void handleActualiser() {
        chargerDonneesBDD();
        mettreAJourStatistiques();
    }
}