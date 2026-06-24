package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.NiveauClasse;
import com.digitalprimaryschool.digitalprimaryschool.model.SectionClass;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService.Resultat;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ClasseController {

    private final EnregistrementService enregistrementService = new EnregistrementService();

    @FXML private TextField txtNom;
    @FXML private ComboBox<NiveauClasse> comboNiveau;
    @FXML private ComboBox<SectionClass> comboSection;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtRecherche;

    @FXML private TableView<Classe> tableClasses;
    @FXML private TableColumn<Classe, String> colNom;
    @FXML private TableColumn<Classe, String> colNiveau;
    @FXML private TableColumn<Classe, String> colSection;
    @FXML private TableColumn<Classe, String> colCategorie;
    @FXML private TableColumn<Classe, Integer> colCapacite;

    @FXML private Button btnEnregistrer;
    @FXML private Button btnSupprimer;

    private final ObservableList<Classe> masterData = FXCollections.observableArrayList();
    private Classe selectedClasse = null;

    @FXML
    public void initialize() {
        // Mappage des colonnes du tableau
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNiveau.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNiveau() != null ? cell.getValue().getNiveau().name() : ""));
        colSection.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSection() != null ? cell.getValue().getSection().name() : ""));
        colCategorie.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategorieClasseLibelle()));
        colCapacite.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCapaciteMax()).asObject());

        // Remplissage des ComboBoxes avec vos Enums
        comboNiveau.setItems(FXCollections.observableArrayList(NiveauClasse.values()));
        comboSection.setItems(FXCollections.observableArrayList(SectionClass.values()));

        // Chargement des données initiales
        rafraichirListe();

        // Logique de filtrage en temps réel
        FilteredList<Classe> filteredData = new FilteredList<>(masterData, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(classe -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lower = newValue.toLowerCase();
                    return classe.getNom().toLowerCase().contains(lower) ||
                            (classe.getNiveau() != null && classe.getNiveau().name().toLowerCase().contains(lower));
                })
        );
        tableClasses.setItems(filteredData);

        // Sélection d'une ligne pour modification / suppression
        tableClasses.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedClasse = newVal;
                txtNom.setText(newVal.getNom());
                comboNiveau.setValue(newVal.getNiveau());
                comboSection.setValue(newVal.getSection());
                txtCapacite.setText(String.valueOf(newVal.getCapaciteMax()));
                btnEnregistrer.setText("Enregistrer les modifications");
                btnSupprimer.setDisable(false);
            }
        });
    }

    private void rafraichirListe() {
        try {
            masterData.clear();
            masterData.addAll(enregistrementService.getToutesLesClasses());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les classes : " + e.getMessage());
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (!validerChamps()) return;

        String nom = txtNom.getText();
        NiveauClasse niveau = comboNiveau.getValue();
        SectionClass section = comboSection.getValue();
        int capacite = Integer.parseInt(txtCapacite.getText());

        if (selectedClasse == null) {
            // AJOUT
            Classe nouvelleClasse = new Classe(); // Génère un UUID automatique interne
            nouvelleClasse.setNom(nom);
            nouvelleClasse.setNiveau(niveau.name());
            nouvelleClasse.setSection(section.name());
            nouvelleClasse.setCapaciteMax(capacite);

            Resultat res = enregistrementService.enregistrerClasse(nouvelleClasse);
            if (res.isSuccess()) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("CONFIRMATION");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Vous confirmez cette Opération ?");

                if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    masterData.add(nouvelleClasse);
                    handleAnnuler();
                }

            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", res.getMessage());
            }
        } else {
            // MODIFICATION
            selectedClasse.setNom(nom);
            selectedClasse.setNiveau(niveau.name());
            selectedClasse.setSection(section.name());
            selectedClasse.setCapaciteMax(capacite);

            Resultat res = enregistrementService.modifierClasse(selectedClasse);
            if (res.isSuccess()) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("CONFIRMATION");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Vous confirmez cette Opération ?");

                if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    tableClasses.refresh();
                    handleAnnuler();
                }
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", res.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedClasse == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer la classe " + selectedClasse.getNom() + " ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {

            Resultat res = enregistrementService.supprimerClasse(selectedClasse.getIdClasse());
            if (res.isSuccess()) {
                masterData.remove(selectedClasse);
                handleAnnuler();
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Contrainte d'intégrité", res.getMessage());
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        txtNom.clear();
        comboNiveau.setValue(null);
        comboSection.setValue(null);
        txtCapacite.clear();
        selectedClasse = null;
        tableClasses.getSelectionModel().clearSelection();
        btnEnregistrer.setText("Créer la Classe");
        btnSupprimer.setDisable(true);
    }

    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty() || comboNiveau.getValue() == null || comboSection.getValue() == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Attention", "Veuillez renseigner le nom, le niveau et la section.");
            return false;
        }
        try {
            int cap = Integer.parseInt(txtCapacite.getText());
            if (cap <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur de format", "La capacité doit être un nombre entier positif.");
            return false;
        }
        return true;
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}