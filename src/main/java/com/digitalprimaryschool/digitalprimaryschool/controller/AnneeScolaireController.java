package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.AnneeScolaire;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService.Resultat;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AnneeScolaireController {

    @FXML private TextField txtLibelleAnnee;
    @FXML private DatePicker dpDebut;
    @FXML private DatePicker dpFin;
    @FXML private ToggleButton btnToggleStatut;
    @FXML private Label lblStatutDescription;

    @FXML private TableView<AnneeScolaire> tableAnneeScolaire;
    @FXML private TableColumn<AnneeScolaire, String> colIdAnnescolaire;
    @FXML private TableColumn<AnneeScolaire, String> colLibelle;
    @FXML private TableColumn<AnneeScolaire, String> colDebut;
    @FXML private TableColumn<AnneeScolaire, String> colFin;
    @FXML private TableColumn<AnneeScolaire, Boolean> colStatut;

    @FXML private Button btnSupprimer;
    @FXML private Button btnEnregistrer;

    private EnregistrementService enregistrementService;
    private final ObservableList<AnneeScolaire> listeAnnees = FXCollections.observableArrayList();
    private AnneeScolaire anneeSelectionnee = null;

    // Le formateur strict demandé
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        enregistrementService = new EnregistrementService();

        // --- CONFIGURATION STRICTE DES DATEPICKERS (Format: yyyy-MM-dd) ---
        StringConverter<LocalDate> dateConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (Exception e) {
                        // Empêche le plantage en cas de saisie manuelle invalide
                        return null;
                    }
                }
                return null;
            }
        };

        dpDebut.setConverter(dateConverter);
        dpFin.setConverter(dateConverter);
        dpDebut.setPromptText("AAAA-MM-JJ");
        dpFin.setPromptText("AAAA-MM-JJ");

        // --- VISUEL DU SWITCH ---
        btnToggleStatut.setText("");
        Circle cirecleBille = new Circle(8);
        cirecleBille.getStyleClass().add("switch-bille");
        btnToggleStatut.setGraphic(cirecleBille);

        // --- MAPPAGE DES COLONNES TABLEAU ---
        colIdAnnescolaire.setCellValueFactory(new PropertyValueFactory<>("idAnnescolaire"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colStatut.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isEstActive()));

        // Rendu visuel de la colonne État
        colStatut.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    AnneeScolaire data = getTableRow().getItem();
                    if (data.isEstActive()) {
                        setText("ACTIF");
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    } else {
                        setText("INACTIF");
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                    }
                }
            }
        });

        tableAnneeScolaire.setItems(listeAnnees);

        tableAnneeScolaire.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                basculerEnModeModification(newSel);
            } else {
                btnSupprimer.setVisible(false);
            }
        });

        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            // Récupération globale sécurisée (Mono-établissement)
            List<AnneeScolaire> list = enregistrementService.getTousLesAnneesScolaires();
            listeAnnees.clear();

            if (list != null) {
                // Tri décroissant sur le libellé (ex: 2026 avant 2025)
                list.sort((annee1, annee2) -> {
                    String l1 = (annee1.getLibelle() != null) ? annee1.getLibelle() : "";
                    String l2 = (annee2.getLibelle() != null) ? annee2.getLibelle() : "";
                    return String.CASE_INSENSITIVE_ORDER.compare(l2, l1);
                });

                listeAnnees.addAll(list);
            }
        } catch (Exception e) {
            showErrorAlert("Erreur Base", "Impossible de lire la table AnneeScolaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleToggleActive() {
        if (btnToggleStatut.isSelected()) {
            lblStatutDescription.setText("Année active par défaut pour tout le système.");
            lblStatutDescription.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
        } else {
            lblStatutDescription.setText("Cette année est actuellement désactivée/close.");
            lblStatutDescription.setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
        }
    }

    private void basculerEnModeModification(AnneeScolaire annee) {
        anneeSelectionnee = annee;
        txtLibelleAnnee.setText(annee.getLibelle());

        // Analyse sécurisée de la chaîne provenant de la BD vers le DatePicker
        if (annee.getDateDebut() != null && !annee.getDateDebut().isEmpty()) {
            try {
                dpDebut.setValue(LocalDate.parse(annee.getDateDebut(), formatter));
            } catch (Exception e) { dpDebut.setValue(null); }
        } else { dpDebut.setValue(null); }

        if (annee.getDateFin() != null && !annee.getDateFin().isEmpty()) {
            try {
                dpFin.setValue(LocalDate.parse(annee.getDateFin(), formatter));
            } catch (Exception e) { dpFin.setValue(null); }
        } else { dpFin.setValue(null); }

        btnToggleStatut.setSelected(annee.isEstActive());
        handleToggleActive();

        btnSupprimer.setVisible(true);
        btnEnregistrer.setText("Modifier");
    }

    @FXML
    private void handleEnregistrer() {
        String libelle = txtLibelleAnnee.getText().trim();

        if (libelle.isEmpty()) {
            showErrorAlert("Validation", "Le libellé complet est obligatoire.");
            return;
        }

        // Récupération et formatage explicite en chaîne yyyy-MM-dd
        String debutStr = (dpDebut.getValue() != null) ? dpDebut.getValue().format(formatter) : null;
        String finStr = (dpFin.getValue() != null) ? dpFin.getValue().format(formatter) : null;
        boolean statutActiveValue = btnToggleStatut.isSelected();

        try {
            Resultat res;
            if (anneeSelectionnee == null) {
                // --- CREATE : UUID automatique ---
                AnneeScolaire nouvelle = new AnneeScolaire();
                nouvelle.setIdAnnescolaire(UUID.randomUUID().toString());
                nouvelle.setLibelle(libelle);
                nouvelle.setDateDebut(debutStr);
                nouvelle.setDateFin(finStr);
                nouvelle.setEstActive(statutActiveValue);

                // Plus besoin de clé étrangère d'école ici, l'entité est globale au logiciel
                res = enregistrementService.enregistrerAnneeScolaire(nouvelle);
            } else {
                // --- UPDATE ---
                anneeSelectionnee.setLibelle(libelle);
                anneeSelectionnee.setDateDebut(debutStr);
                anneeSelectionnee.setDateFin(finStr);
                anneeSelectionnee.setEstActive(statutActiveValue);

                res = enregistrementService.modifierAnneeScolaire(anneeSelectionnee);
            }

            if (res.isSuccess()) {
                showAlert("Succès", res.getMessage());
                handleAnnuler();
                chargerDonnees();
            } else {
                showErrorAlert("Erreur de validation", res.getMessage());
            }

        } catch (Exception e) {
            showErrorAlert("Erreur d'écriture", "Impossible d'enregistrer : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (anneeSelectionnee == null) return;

        if (showConfirmation("Confirmation", "Voulez-vous vraiment supprimer l'année " + anneeSelectionnee.getLibelle() + " ?")) {
            try {
                Resultat res = enregistrementService.supprimerAnneeScolaire(anneeSelectionnee.getIdAnnescolaire());
                if (res.isSuccess()) {
                    showAlert("Succès", res.getMessage());
                    handleAnnuler();
                    chargerDonnees();
                } else {
                    showErrorAlert("Échec", res.getMessage());
                }
            } catch (Exception e) {
                showErrorAlert("Erreur", "Une erreur est survenue lors de la suppression : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        anneeSelectionnee = null;
        txtLibelleAnnee.clear();
        dpDebut.setValue(null);
        dpFin.setValue(null);
        btnToggleStatut.setSelected(false);
        handleToggleActive();
        btnEnregistrer.setText("Enregistrer");
        tableAnneeScolaire.getSelectionModel().clearSelection();
        btnSupprimer.setVisible(false);
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        ButtonType oui = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType non = new ButtonType("Non", ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(oui, non);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == oui;
    }
}