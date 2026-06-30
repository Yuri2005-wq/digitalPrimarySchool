package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.dao.EnseignantDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.AnneeScolaireDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Enseignant;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.AnneeScolaire;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.UUID;

public class EnseignantController {

    @FXML private Button btnTabListe;
    @FXML private Button btnTabAffectation;
    @FXML private StackPane stackContainer;
    @FXML private GridPane paneGestion;
    @FXML private VBox paneAffectation;
    @FXML private Label lblAnneeActive;

    // Formulaire CRUD (Ajout/Modification)
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtContact;
    @FXML private TextField txtQualification;
    @FXML private TextField txtGrade;
    @FXML private Button btnEnregistrer;

    // TableView de la liste des enseignants
    @FXML private TableView<Enseignant> tableEnseignants;
    @FXML private TableColumn<Enseignant, String> colNom;
    @FXML private TableColumn<Enseignant, String> colPrenom;
    @FXML private TableColumn<Enseignant, String> colContact;
    @FXML private TableColumn<Enseignant, String> colQualification;
    @FXML private TableColumn<Enseignant, String> colClasse;

    // ComboBox pour l'affectation
    @FXML private ComboBox<Enseignant> comboAffectEnseignant;
    @FXML private ComboBox<Classe> comboAffectClasse;

    // Classes d'accès aux données (DAO)
    private final EnseignantDAO enseignantDAO = new EnseignantDAO();
    private final ClasseDAO classeDAO = new ClasseDAO();
    private final AnneeScolaireDAO anneeScolaireDAO = new AnneeScolaireDAO();

    // Listes observables pour l'interface graphique
    private final ObservableList<Enseignant> listeEnseignants = FXCollections.observableArrayList();
    private final ObservableList<Classe> listeClasses = FXCollections.observableArrayList();

    // Variables de contexte localisées
    private String idAnneeActive = null;
    private Enseignant enseignantSelectionne = null;

    @FXML
    public void initialize() {
        configurerTableau();
        configurerConvertisseursCombos();
        chargerDonneesContextuelles();

        // Détecter la sélection d'une ligne du tableau pour l'édition
        tableEnseignants.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                remplirFormulairePourEdition(newVal);
            }
        });
    }

    /**
     * Liaison des colonnes du tableau avec les propriétés du modèle Enseignant
     */
    private void configurerTableau() {
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        colContact.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactEnseignant()));
        colQualification.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQualification()));
        colClasse.setCellValueFactory(data -> {
            Classe c = data.getValue().getClasse();
            return new SimpleStringProperty(c != null ? c.getNom() : "Aucune affectation");
        });
        tableEnseignants.setItems(listeEnseignants);
    }

    /**
     * Configuration de l'affichage textuel des objets dans les ComboBox
     */
    private void configurerConvertisseursCombos() {
        comboAffectEnseignant.setConverter(new StringConverter<>() {
            @Override public String toString(Enseignant e) { return e != null ? e.getNom() + " " + e.getPrenom() : ""; }
            @Override public Enseignant fromString(String s) { return null; }
        });
        comboAffectClasse.setConverter(new StringConverter<>() {
            @Override public String toString(Classe c) { return c != null ? c.getNom() : ""; }
            @Override public Classe fromString(String s) { return null; }
        });
    }

    /**
     * Chargement initial et rafraîchissement des données depuis la BDD
     */
    private void chargerDonneesContextuelles() {
        try {
            listeEnseignants.setAll(enseignantDAO.listerTous());
            listeClasses.setAll(classeDAO.listerToutes());
            comboAffectEnseignant.setItems(listeEnseignants);
            comboAffectClasse.setItems(listeClasses);

            // Recherche automatique de l'unique année scolaire active
            AnneeScolaire annee = anneeScolaireDAO.trouverActive();
            if (annee != null) {
                this.idAnneeActive = annee.getIdAnnescolaire();
                lblAnneeActive.setText("Année Active : " + annee.getLibelle());
            } else {
                this.idAnneeActive = null;
                lblAnneeActive.setText("⚠️ Aucune année active !");
            }
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'initialisation", "Impossible de charger les données : " + e.getMessage());
        }
    }

    /**
     * Traitement de la validation de l'affectation avec contrôle de la règle métier
     */
    @FXML
    private void handleValiderAffectation(ActionEvent event) {
        Enseignant enseignant = comboAffectEnseignant.getValue();
        Classe classe = comboAffectClasse.getValue();

        if (enseignant == null || classe == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez sélectionner à la fois un enseignant et une classe.");
            return;
        }

        if (idAnneeActive == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Contexte absent", "L'affectation est impossible car aucune année scolaire n'est active.");
            return;
        }

        try {
            // APPLICATION DE LA RÈGLE MÉTIER CRITIQUE :
            // On récupère l'état à jour de l'enseignant pour voir s'il possède déjà une classe cette année
            Enseignant verifEnseignant = enseignantDAO.trouverParId(enseignant.getIdEnseignant());

            if (verifEnseignant != null && verifEnseignant.getClasse() != null) {
                Alert alerteDoublon = new Alert(Alert.AlertType.CONFIRMATION);
                alerteDoublon.setTitle("Règle de gestion : Classe unique");
                alerteDoublon.setHeaderText("Attention : " + enseignant.getNom() + " est déjà affecté à la classe " + verifEnseignant.getClasse().getNom());
                alerteDoublon.setContentText("Un enseignant ne peut pas tenir deux classes durant la même année scolaire active.\n\nVoulez-vous écraser son ancienne affectation et le muter vers la classe " + classe.getNom() + " ?");

                ButtonType btnOk = alerteDoublon.showAndWait().orElse(ButtonType.NO);
                if (btnOk != ButtonType.OK) {
                    return; // L'utilisateur annule l'opération
                }
            }

            // Exécution de la requête de modification/enregistrement de l'affectation
            boolean reussite = enseignantDAO.affecterAClasse(enseignant.getIdEnseignant(), classe.getIdClasse());
            if (reussite) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Affectation réussie", "L'enseignant " + enseignant.getNom() + " a été affecté avec succès à la classe " + classe.getNom() + ".");
                comboAffectEnseignant.getSelectionModel().clearSelection();
                comboAffectClasse.getSelectionModel().clearSelection();
                chargerDonneesContextuelles();
            }
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'affectation", "Erreur SQL lors de l'enregistrement : " + e.getMessage());
        }
    }

    /**
     * Enregistrement (Ajout ou Modification) d'un enseignant
     */
    @FXML
    private void handleEnregistrer(ActionEvent event) {
        String nom = txtNom.getText().trim();
        String contact = txtContact.getText().trim();

        if (nom.isEmpty() || contact.isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs obligatoires", "Le Nom et le Téléphone sont requis pour valider.");
            return;
        }

        try {
            if (enseignantSelectionne == null) {
                // Mode Création
                Enseignant newEns = new Enseignant();
                newEns.setIdEnseignant(UUID.randomUUID().toString());
                newEns.setNom(nom.toUpperCase());
                newEns.setPrenom(txtPrenom.getText().trim());
                newEns.setContactEnseignant(contact);
                newEns.setQualification(txtQualification.getText().trim());
                newEns.setGrade(txtGrade.getText().trim());
                enseignantDAO.ajouter(newEns);
            } else {
                // Mode Modification
                enseignantSelectionne.setNom(nom.toUpperCase());
                enseignantSelectionne.setPrenom(txtPrenom.getText().trim());
                enseignantSelectionne.setContactEnseignant(contact);
                enseignantSelectionne.setQualification(txtQualification.getText().trim());
                enseignantSelectionne.setGrade(txtGrade.getText().trim());
                enseignantDAO.modifier(enseignantSelectionne);
            }
            handleClear();
            chargerDonneesContextuelles();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur de sauvegarde", e.getMessage());
        }
    }

    /**
     * Suppression de l'enseignant sélectionné
     */
    @FXML
    private void handleSupprimer(ActionEvent event) {
        Enseignant selection = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un enseignant dans la liste avant de supprimer.");
            return;
        }

        try {
            enseignantDAO.supprimer(selection.getIdEnseignant());
            handleClear();
            chargerDonneesContextuelles();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur de suppression", e.getMessage());
        }
    }

    /**
     * Navigation entre les onglets personnalisés (Style de la charte graphique)
     */
    @FXML
    private void switchTab(ActionEvent event) {
        if (event.getSource() == btnTabListe) {
            btnTabListe.getStyleClass().add("active-top-tab");
            btnTabAffectation.getStyleClass().remove("active-top-tab");
            paneGestion.setVisible(true);
            paneAffectation.setVisible(false);
        } else {
            btnTabAffectation.getStyleClass().add("active-top-tab");
            btnTabListe.getStyleClass().remove("active-top-tab");
            paneGestion.setVisible(false);
            paneAffectation.setVisible(true);
        }
        chargerDonneesContextuelles();
    }

    /**
     * Réinitialisation complète du formulaire de saisie
     */
    @FXML
    private void handleClear() {
        enseignantSelectionne = null;
        txtNom.clear();
        txtPrenom.clear();
        txtContact.clear();
        txtQualification.clear();
        txtGrade.clear();
        btnEnregistrer.setText("Enregistrer");
        tableEnseignants.getSelectionModel().clearSelection();
    }

    /**
     * Remplissage automatique des champs lors de la sélection d'une ligne
     */
    private void remplirFormulairePourEdition(Enseignant ens) {
        this.enseignantSelectionne = ens;
        txtNom.setText(ens.getNom());
        txtPrenom.setText(ens.getPrenom());
        txtContact.setText(ens.getContactEnseignant());
        txtQualification.setText(ens.getQualification());
        txtGrade.setText(ens.getGrade());
        btnEnregistrer.setText("Mettre à jour");
    }

    /**
     * Utilitaire d'affichage des boîtes de dialogue JavaFX (Alerts)
     */
    private void afficherAlerte(Alert.AlertType type, String titre, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}