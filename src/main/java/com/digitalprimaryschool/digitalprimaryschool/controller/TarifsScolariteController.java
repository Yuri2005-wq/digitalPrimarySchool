package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.NiveauClasse;
import com.digitalprimaryschool.digitalprimaryschool.model.TarisScolaire;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService.Resultat;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class TarifsScolariteController {

    private final EnregistrementService enregistrementService = new EnregistrementService();

    @FXML private TextField txtLibelle;
    @FXML private ComboBox<NiveauClasse> comboClasse;
    @FXML private TextField txtInscription;
    @FXML private TextField txtPension;
    @FXML private TextField txtTenueScolaire;
    @FXML private TextField txtTenueSport;
    @FXML private TextField txtRecherche;

    @FXML private TableView<TarisScolaire> tableTarifs;
    @FXML private TableColumn<TarisScolaire, String> colLibelle;
    @FXML private TableColumn<TarisScolaire, String> colClasse;
    @FXML private TableColumn<TarisScolaire, Double> colInscription;
    @FXML private TableColumn<TarisScolaire, Double> colPension;
    @FXML private TableColumn<TarisScolaire, Double> colTenueScolaire;
    @FXML private TableColumn<TarisScolaire, Double> colTenueSport;

    @FXML private Button btnEnregistrer;
    @FXML private Button btnSupprimer;
    private final ObservableList<TarisScolaire> masterData = FXCollections.observableArrayList();
    private TarisScolaire selectedTarif = null;

    @FXML
    public void initialize() {
        // Remplir le ComboBox
        comboClasse.setItems(FXCollections.observableArrayList(NiveauClasse.values()));

        // Configuration des colonnes
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        colClasse.setCellValueFactory(cellData -> {
            TarisScolaire t = cellData.getValue();
            if (t != null && t.getNiveauClasse() != null) {
                return new SimpleStringProperty(t.getNiveauClasse().getLibelle());
            }
            return new SimpleStringProperty("");
        });

        colInscription.setCellValueFactory(new PropertyValueFactory<>("fraisInscription"));
        colPension.setCellValueFactory(new PropertyValueFactory<>("montantPension"));  // Changé
        colTenueScolaire.setCellValueFactory(new PropertyValueFactory<>("fraistenueScolaire"));
        colTenueSport.setCellValueFactory(new PropertyValueFactory<>("fraistenueSport"));

        // Personnalisation du ComboBox
        comboClasse.setConverter(new StringConverter<>() {
            @Override public String toString(NiveauClasse r) { return r == null ? "" : r.getLibelle(); }
            @Override public NiveauClasse fromString(String str) { return null; }
        });

        // Chargement des données
        chargerDonnees();

        // Filtrage dynamique
        FilteredList<TarisScolaire> filteredData = new FilteredList<>(masterData, p -> true);
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(tarif -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lower = newValue.toLowerCase();

                    String niveauClasse = (tarif.getNiveauClasse() != null) ? tarif.getNiveauClasse().getLibelle().toLowerCase() : "";
                    String libelle = (tarif.getLibelle() != null) ? tarif.getLibelle().toLowerCase() : "";

                    return libelle.contains(lower) || niveauClasse.contains(lower);
                })
        );
        tableTarifs.setItems(filteredData);

        // Sélection d'une ligne
        tableTarifs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTarif = newVal;
                txtLibelle.setText(newVal.getLibelle());

                if (newVal.getNiveauClasse() != null) {
                    comboClasse.setValue(newVal.getNiveauClasse());
                }

                txtInscription.setText(String.valueOf(newVal.getFraisInscription()));
                txtPension.setText(String.valueOf(newVal.getMontantPension()));  // Changé
                txtTenueScolaire.setText(String.valueOf(newVal.getFraistenueScolaire()));
                txtTenueSport.setText(String.valueOf(newVal.getFraistenueSport()));
                btnEnregistrer.setText("Modifier le Tarif");
                btnSupprimer.setDisable(false);

            }
        });
    }

    private void chargerDonnees() {
        try {
            List<TarisScolaire> tarifs = enregistrementService.getTousLesTarifsScolaires();
            masterData.setAll(tarifs);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur de chargement",
                    "Impossible de charger les tarifs : " + e.getMessage());
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (!validerChamps()) return;

        String libelle = txtLibelle.getText();
        NiveauClasse niveauClasseSelectionne = comboClasse.getValue();
        double inscription = txtInscription.getText().isEmpty() ? 0 : Double.parseDouble(txtInscription.getText());
        double pension = txtPension.getText().isEmpty() ? 0 : Double.parseDouble(txtPension.getText());  // Changé
        double tScolaire = txtTenueScolaire.getText().isEmpty() ? 0 : Double.parseDouble(txtTenueScolaire.getText());
        double tSport = txtTenueSport.getText().isEmpty() ? 0 : Double.parseDouble(txtTenueSport.getText());

        if (selectedTarif == null) {
            // CRÉATION
            TarisScolaire nTarif = new TarisScolaire();
            nTarif.setLibelle(libelle);
            nTarif.setNiveauClasse(niveauClasseSelectionne);
            nTarif.setFraisInscription(inscription);
            nTarif.setMontantPension(pension);  // Changé
            nTarif.setFraistenueScolaire(tScolaire);
            nTarif.setFraistenueSport(tSport);

            Resultat res = enregistrementService.enregistrerTarifScolaire(nTarif);
            if (res.isSuccess()) {
                masterData.add(nTarif);
                handleAnnuler();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Tarif enregistré avec succès !");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", res.getMessage());
            }
        } else {
            // MODIFICATION
            selectedTarif.setLibelle(libelle);
            selectedTarif.setNiveauClasse(niveauClasseSelectionne);
            selectedTarif.setFraisInscription(inscription);
            selectedTarif.setMontantPension(pension);  // Changé
            selectedTarif.setFraistenueScolaire(tScolaire);
            selectedTarif.setFraistenueSport(tSport);

            Resultat res = enregistrementService.modifierTarifScolaire(selectedTarif);
            if (res.isSuccess()) {
                tableTarifs.refresh();
                handleAnnuler();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Tarif modifié avec succès !");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", res.getMessage());
            }
        }
    }
    @FXML
    private void handleSupprimer() {
        if (selectedTarif == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer le Tarif Scolaire pour la classe " + selectedTarif.getNiveauClasse().getLibelle() + " ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {

            Resultat res = enregistrementService.supprimerTarifScolaire(selectedTarif.getIdTarifScolaire());
            if (res.isSuccess()) {
                masterData.remove(selectedTarif);
                handleAnnuler();
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Contrainte d'intégrité", res.getMessage());
            }
        }
    }
            @FXML
    private void handleAnnuler() {
        txtLibelle.clear();
        comboClasse.setValue(null);
        txtInscription.clear();
        txtPension.clear();  // Changé
        txtTenueScolaire.clear();
        txtTenueSport.clear();
        selectedTarif = null;
        tableTarifs.getSelectionModel().clearSelection();
        btnEnregistrer.setText("Enregistrer le Tarif");
        btnSupprimer.setDisable(true);
    }

    private boolean validerChamps() {
        if (txtLibelle.getText().isEmpty() || comboClasse.getValue() == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs obligatoires",
                    "Veuillez renseigner un libellé et sélectionner une classe.");
            return false;
        }
        try {
            if (!txtInscription.getText().isEmpty()) Double.parseDouble(txtInscription.getText());
            if (!txtPension.getText().isEmpty()) Double.parseDouble(txtPension.getText());  // Changé
            if (!txtTenueScolaire.getText().isEmpty()) Double.parseDouble(txtTenueScolaire.getText());
            if (!txtTenueSport.getText().isEmpty()) Double.parseDouble(txtTenueSport.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Format numérique incorrect",
                    "Les montants doivent être des valeurs numériques.");
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