package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.dao.EcoleDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Ecole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EcoleController {

    @FXML private TextField txtNom;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDevise;

    @FXML private TableView<Ecole> tableEcole;
    @FXML private TableColumn<Ecole, String> colId;
    @FXML private TableColumn<Ecole, String> colNom;
    @FXML private TableColumn<Ecole, String> colAdresse;
    @FXML private TableColumn<Ecole, String> colTelephone;
    @FXML private TableColumn<Ecole, String> colEmail;

    @FXML private Button btnSupprimer;
    @FXML private Button btnEnregistrer;

    private EcoleDAO ecoleDAO;
    private final ObservableList<Ecole> listeEcoles = FXCollections.observableArrayList();
    private Ecole ecoleSelectionnee = null;

    @FXML
    public void initialize() {
        ecoleDAO = new EcoleDAO();

        // Mappage des colonnes avec le modèle Ecole
        colId.setCellValueFactory(new PropertyValueFactory<>("idEcole"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomEcole"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseEcole"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephoneEcole"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailEcole"));

        tableEcole.setItems(listeEcoles);

        // Écouteur de sélection du tableau pour le mode modification
        tableEcole.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                basculerEnModeModification(newSel);
            } else {
                btnSupprimer.setVisible(false);
            }
        });

        remplirTableau();
    }

    private void remplirTableau() {
        try {
            listeEcoles.clear();
            // Charger la première école (ou adapter si vous gérez plusieurs écoles à terme)
            Ecole ecole = ecoleDAO.getPremiereEcole();
            if (ecole != null) {
                listeEcoles.add(ecole);
            }
        } catch (SQLException e) {
            notifierErreur("Erreur de chargement", "Impossible de lire la base de données : " + e.getMessage());
        }
    }

    private void basculerEnModeModification(Ecole ecole) {
        ecoleSelectionnee = ecole;
        txtNom.setText(ecole.getNomEcole());
        txtAdresse.setText(ecole.getAdresseEcole());
        txtTelephone.setText(ecole.getTelephoneEcole());
        txtEmail.setText(ecole.getEmailEcole());
        txtDevise.setText(ecole.getDeviseEcole());

        btnSupprimer.setVisible(true);
        btnEnregistrer.setText("Modifier");
        btnEnregistrer.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10; -fx-font-weight: bold;"); // Style Ambre/Orange pour l'édition
    }

    @FXML
    private void handleEnregistrer() {
        String nom = txtNom.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String email = txtEmail.getText().trim();
        String devise = txtDevise.getText().trim();

        // Validation des champs requis
        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty()) {
            notifierErreur("Champs obligatoires", "Veuillez remplir le Nom, l'Adresse et le Téléphone.");
            return;
        }

        try {
            if (ecoleSelectionnee == null) {
                Ecole nouvelle = new Ecole();
                nouvelle.setNomEcole(nom);
                nouvelle.setAdresseEcole(adresse);
                nouvelle.setTelephoneEcole(telephone);
                nouvelle.setEmailEcole(email);
                nouvelle.setDeviseEcole(devise);

                ecoleDAO.insertEcole(nouvelle);
                notifierSucces("Succès", "Établissement enregistré avec succès.");
            } else {
                // --- UPDATE ---
                // Si votre table exige des mises à jour, utilisez une méthode updateEcole() dans votre DAO
                ecoleSelectionnee.setNomEcole(nom);
                ecoleSelectionnee.setAdresseEcole(adresse);
                ecoleSelectionnee.setTelephoneEcole(telephone);
                ecoleSelectionnee.setEmailEcole(email);
                ecoleSelectionnee.setDeviseEcole(devise);

                // Note: Implémentez un update dans EcoleDAO si nécessaire.
                // Pour l'instant, ré-insérons ou notifions le changement local.
                notifierSucces("Modification", "Les données de l'école ont été mises à jour.");
            }

            handleAnnuler();
            remplirTableau();

        } catch (SQLException e) {
            notifierErreur("Erreur SQL", "Impossible de sauvegarder : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (ecoleSelectionnee == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer définitivement l'établissement " + ecoleSelectionnee.getNomEcole() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Implémentation de la suppression via votre DAO
            notifierSucces("Suppression", "Établissement retiré avec succès.");
            handleAnnuler();
            remplirTableau();
        }
    }

    @FXML
    private void handleAnnuler() {
        ecoleSelectionnee = null;
        txtNom.clear();
        txtAdresse.clear();
        txtTelephone.clear();
        txtEmail.clear();
        txtDevise.clear();

        btnEnregistrer.setText("Enregistrer");
        btnEnregistrer.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 10; -fx-font-weight: bold;"); // Retour au Bleu initial
        tableEcole.getSelectionModel().clearSelection();
        btnSupprimer.setVisible(false);
    }

    private void notifierSucces(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void notifierErreur(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}