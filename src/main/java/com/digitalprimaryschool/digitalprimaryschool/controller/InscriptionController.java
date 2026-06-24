package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.dao.InscriptionDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.PaiementDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.TarifScolaireDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.*;
import com.digitalprimaryschool.digitalprimaryschool.service.PDFService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class InscriptionController {

    @FXML private ComboBox<String> comboAnneeScolaire;
    @FXML private TextField txtDateInscription;
    @FXML private TextField txtChercherEleve;
    @FXML private ComboBox<Eleve> comboEleveSearch;
    @FXML private Label lblNomEleve;
    @FXML private Label lblParentEleve;
    @FXML private ComboBox<Classe> comboClasseInscription;
    @FXML private ComboBox<String> comboTypeInscription;
    @FXML private TextField txtFraisInscription;
    @FXML private ComboBox<String> comboModePaiement;
    @FXML private Button btnValiderInscription;

    // Tes instances de DAOs réelles
    private final InscriptionDAO inscriptionDAO = new InscriptionDAO();
    private final PaiementDAO paiementDAO = new PaiementDAO();
    private final TarifScolaireDAO tarifScolaireDAO = new TarifScolaireDAO();
    private final ClasseDAO classeDAO = new ClasseDAO();
    private final EleveDAO eleveDAO = new EleveDAO();

    // Liste pour la recherche d'élèves
    private ObservableList<Eleve> tousLesEleves = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Initialisation des dates et années
        txtDateInscription.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        comboAnneeScolaire.setItems(FXCollections.observableArrayList("2025-2026", "2026-2027"));
        comboAnneeScolaire.getSelectionModel().select("2025-2026");

        // 2. Types d'inscription
        comboTypeInscription.setItems(FXCollections.observableArrayList("Nouvelle Inscription", "Réinscription"));
        comboTypeInscription.getSelectionModel().selectFirst();

        // Mode de paiement à partir de l'Enum réel
        comboModePaiement.getItems().clear();
        for (ModePaiement mode : ModePaiement.values()) {
            comboModePaiement.getItems().add(mode.getLibelle());
        }

        // 3. Convertisseurs d'objets pour les ComboBox
        configurerConvertisseurs();

        // 4. Gestion des événements graphiques
        attacherEvenements();

        // 5. Chargement initial de la Base de Données
        chargerDonneesInitiales();
    }

    private void configurerConvertisseurs() {
        comboClasseInscription.setConverter(new StringConverter<Classe>() {
            @Override public String toString(Classe classe) { return classe != null ? classe.getNom() : ""; }
            @Override public Classe fromString(String string) { return null; }
        });

        comboEleveSearch.setConverter(new StringConverter<Eleve>() {
            @Override public String toString(Eleve eleve) {
                return eleve != null ? eleve.getMatricule() + " - " + eleve.getNom() + " " + eleve.getPrenom() : "";
            }
            @Override public Eleve fromString(String string) { return null; }
        });
    }

    private void attacherEvenements() {
        // Filtrage dynamique des élèves
        txtChercherEleve.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.trim().isEmpty()) {
                comboEleveSearch.setItems(tousLesEleves);
            } else {
                String filtre = newText.toLowerCase().trim();
                ObservableList<Eleve> elevesFiltrer = FXCollections.observableArrayList();
                for (Eleve e : tousLesEleves) {
                    if (e.getNom().toLowerCase().contains(filtre) ||
                            e.getPrenom().toLowerCase().contains(filtre) ||
                            e.getMatricule().toLowerCase().contains(filtre)) {
                        elevesFiltrer.add(e);
                    }
                }
                comboEleveSearch.setItems(elevesFiltrer);
                if (!elevesFiltrer.isEmpty()) {
                    comboEleveSearch.show();
                }
            }
        });

        // Sélection d'un élève
        comboEleveSearch.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, eleve) -> {
            if (eleve != null) {
                lblNomEleve.setText(eleve.getNom() + " " + eleve.getPrenom());
                lblParentEleve.setText(eleve.getNomParent() != null ? eleve.getNomParent() : "Non renseigné");
            }
        });

        // Chargement du tarif dynamique selon la classe sélectionnée
        comboClasseInscription.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, classe) -> {
            if (classe != null && classe.getNiveau() != null) {
                try {
                    TarisScolaire tarif = tarifScolaireDAO.trouverParClasse(classe.getNiveau().name());
                    if (tarif != null) {
                        txtFraisInscription.setText(String.valueOf(tarif.getFraisInscription()));
                    } else {
                        txtFraisInscription.setText("0.0");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    txtFraisInscription.setText("0.0");
                }
            }
        });
    }

    private void chargerDonneesInitiales() {
        try {
            comboClasseInscription.setItems(FXCollections.observableArrayList(classeDAO.listerToutes()));
            tousLesEleves.clear();
            tousLesEleves.addAll(eleveDAO.listerTous());
            comboEleveSearch.setItems(tousLesEleves);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de charger les données initiales : " + e.getMessage());
        }
    }

    @FXML
    private void handleValiderInscription(ActionEvent event) {
        Eleve eleveSelectionne = comboEleveSearch.getValue();
        Classe classeSelectionne = comboClasseInscription.getValue();

        if (eleveSelectionne == null || classeSelectionne == null || comboModePaiement.getSelectionModel().isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs requis", "Veuillez sélectionner un élève, une classe et un mode de paiement.");
            return;
        }

        try {
            String matricule = eleveSelectionne.getMatricule();
            String idClasse = classeSelectionne.getIdClasse();
            String idAnnee = comboAnneeScolaire.getValue();
            double montant = Double.parseDouble(txtFraisInscription.getText());
            boolean estReinscript = "Réinscription".equals(comboTypeInscription.getValue());

            // 1. Vérification doublon inscription
            if (inscriptionDAO.inscriptionExiste(matricule, idClasse, idAnnee)) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Cet élève possède déjà une inscription pour cette année.");
                return;
            }

            // 2. CRÉATION ET ENREGISTREMENT DE L'INSCRIPTION
            Inscription inscription = new Inscription();
            inscription.setIdAnnescolaire(idAnnee);
            inscription.setIdClasse(classeSelectionne);
            inscription.setMatriculeEleve(eleveSelectionne);
            inscription.setMontantPayer(montant);
            inscription.setEstReinscript(estReinscript);

            inscriptionDAO.ajouter(inscription);

            // 3. CRÉATION DU PAIEMENT (Instanciation explicite corrigée)
            Paiement paiement = new Paiement();
            paiement.setReference("REG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            paiement.setMontant(montant);
            paiement.setModePaiementParLibelle(comboModePaiement.getValue());
            paiement.setEncaisserPar("Econome Principal");

            // 4. CRÉATION DU REÇU (Contrainte SQLite type 'certif' appliquée)
            recue_paiement recu = new recue_paiement();
            recu.setIdDocuments(UUID.randomUUID().toString());
            recu.setIdRecu(UUID.randomUUID().toString());
            recu.setMatriculeEleve(matricule);
            recu.setType("certif");
            recu.setGenerePar("Econome");
            recu.setFormatFichier("PDF");

            // Récupération du lien vers le tarif appliqué
            TarisScolaire tarifApplique = tarifScolaireDAO.trouverParClasse(classeSelectionne.getNiveau().name());
            String idTarif = (tarifApplique != null) ? tarifApplique.getIdTarifScolaire() : "TARIF-INCONNU";

            // 5. Sauvegarde conjointe du Paiement et du Recu via ton PaiementDAO
            paiementDAO.enregistrerAvecRecu(paiement, recu, idTarif, "USER-ACTIVE", "ECO-ACTIVE");

            // 6. GÉNÉRATION DIRECTE DU REÇU PDF (Via iText 7)
            PDFService.genererRecuInscription(eleveSelectionne, classeSelectionne, paiement, txtDateInscription.getText());

            // 7. Affichage du pop-up de succès de l'opération
            afficherPopUpSucces(eleveSelectionne, classeSelectionne, paiement);

            // Réinitialisation globale
            reinitialiserFormulaire();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur Critique Sauvegarde", "Échec de la transaction : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire() {
        comboEleveSearch.getSelectionModel().clearSelection();
        comboClasseInscription.getSelectionModel().clearSelection();
        comboModePaiement.getSelectionModel().clearSelection();
        txtChercherEleve.clear();
        lblNomEleve.setText("Aucun sélectionné");
        lblParentEleve.setText("-");
        txtFraisInscription.clear();
    }

    private void afficherPopUpSucces(Eleve eleve, Classe classe, Paiement paiement) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscription Confirmée");
        alert.setHeaderText("Élève inscrit avec succès !");
        alert.setContentText(String.format(
                "L'élève %s %s a bien été affecté à la classe %s.\n\n" +
                        "Le reçu PDF lié à la référence %s a été généré et ouvert.",
                eleve.getNom(), eleve.getPrenom(), classe.getNom(), paiement.getReference()
        ));
        alert.showAndWait();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}