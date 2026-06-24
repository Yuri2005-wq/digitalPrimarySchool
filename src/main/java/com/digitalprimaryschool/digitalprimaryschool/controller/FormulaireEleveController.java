package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
import com.digitalprimaryschool.digitalprimaryschool.model.Sexe;
import com.digitalprimaryschool.digitalprimaryschool.model.LieuNaissance;
import com.digitalprimaryschool.digitalprimaryschool.model.Nationnalite;
import com.digitalprimaryschool.digitalprimaryschool.model.Region;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.ZoneId;
import java.util.Date;

public class FormulaireEleveController {

    // ============================================================
    // COMPOSANTS CARTE PARENT
    // ============================================================
    @FXML private Label labelErreurParent;
    @FXML private TextField txtPrenomParent; // Utilisé pour le nom complet dans ton modèle
    @FXML private TextField txtContactParent;
    @FXML private TextField txtEmailParent;
    @FXML private TextField txtProfessionParent;
    @FXML private ComboBox<String> comboProfessionParent;
    @FXML private TextField txtChercherAdresse;
    @FXML private ComboBox<String> comboAdresseParent;

    // ============================================================
    // COMPOSANTS CARTE ÉLÈVE
    // ============================================================
    @FXML private Label labelErreurEleve;
    @FXML private ImageView imgApercu;
    @FXML private TextField txtNomEleve;
    @FXML private TextField txtPrenomEleve;
    @FXML private DatePicker dpDateNaissance;
    @FXML private ComboBox<String> comboLieuNaissance;
    @FXML private ComboBox<String> comboSexe;
    @FXML private ComboBox<String> comboNationalite;
    @FXML private ComboBox<String> comboRegionOrigine;
    @FXML private TextArea txtAntecedentsMedicaux;

    // Masquer les boutons/champs inutiles en mode modification pure
    @FXML private TextField txtChercherParentExistant;
    @FXML private ComboBox<Object> comboParentExistant;

    private Eleve eleveActuel;
    private Runnable onSaveCallback;
    private String cheminPhotoSelectionnee = null;

    @FXML
    public void initialize() {
        // Désactiver la recherche de parent existant en mode modification directe de la fiche
        if (txtChercherParentExistant != null) txtChercherParentExistant.setDisable(true);
        if (comboParentExistant != null) comboParentExistant.setDisable(true);

        // Initialiser les ComboBox avec les Enums existants de ton application
        for (Sexe s : Sexe.values()) comboSexe.getItems().add(s.getLibelle());
        for (LieuNaissance l : LieuNaissance.values()) comboLieuNaissance.getItems().add(l.getLibelle());
        for (Nationnalite n : Nationnalite.values()) comboNationalite.getItems().add(n.getLibelle());
        for (Region r : Region.values()) comboRegionOrigine.getItems().add(r.getLibelle());
    }

    /**
     * Reçoit l'élève, remplit les formulaires et prépare la mise à jour
     */
    public void initialiserDonnees(Eleve eleve, Runnable onSaveCallback) {
        this.eleveActuel = eleve;
        this.onSaveCallback = onSaveCallback;

        if (eleve == null) return;

        // 1. Pré-remplissage des données de l'élève
        txtNomEleve.setText(eleve.getNom());
        txtPrenomEleve.setText(eleve.getPrenom());
        txtAntecedentsMedicaux.setText(eleve.getAntecedentsMedicaux());

        if (eleve.getDateNaissance() != null) {
            dpDateNaissance.setValue(eleve.getDateNaissance().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

        if (eleve.getSexe() != null) comboSexe.setValue(eleve.getSexe().getLibelle());
        if (eleve.getLieuNaissance() != null) comboLieuNaissance.setValue(eleve.getLieuNaissanceLibelle());
        comboNationalite.setValue(eleve.getNationnalite());
        if (eleve.getRegionOrigine() != null) comboRegionOrigine.setValue(eleve.getRegionOrigineLibelle());

        // Gestion de la photo d'identité actuelle
        if (eleve.getPhoto() != null && !eleve.getPhoto().isEmpty()) {
            this.cheminPhotoSelectionnee = eleve.getPhoto();
            try {
                File file = new File(eleve.getPhoto());
                if (file.exists()) imgApercu.setImage(new Image(file.toURI().toString()));
            } catch (Exception e) {
                System.out.println("Impossible de charger l'aperçu de l'image.");
            }
        }

        // 2. Pré-remplissage des données du Parent rattaché
        Parent p = eleve.parent;
        if (p != null) {
            txtPrenomParent.setText(p.getPrenom());
            txtContactParent.setText(String.valueOf(p.getContactParent()));
            txtEmailParent.setText(p.getEmailParent());

            if (p.getProfession() != null) {
                comboProfessionParent.getItems().add(p.getProfessionLibelle());
                comboProfessionParent.setValue(p.getProfessionLibelle());
            }
            if (p.getAdresse() != null) {
                comboAdresseParent.getItems().add(p.getAdresseLibelle());
                comboAdresseParent.setValue(p.getAdresseLibelle());
            }
        }
    }

    /**
     * Sélection d'une nouvelle photo d'identité
     */
    @FXML
    private void handleChoisirPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir la photo de l'élève");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(txtNomEleve.getScene().getWindow());
        if (selectedFile != null) {
            this.cheminPhotoSelectionnee = selectedFile.getAbsolutePath();
            imgApercu.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    /**
     * Action combinée : Enregistre les modifications sur le parent et l'élève
     */
    @FXML
    private void handleEnregistrerParentEtEleve() {
        if (!validerFormulaire()) return;

        Connection conn = null;
        PreparedStatement stmtParent = null;
        PreparedStatement stmtEleve = null;

        try {
            conn = Database.getConnexion();
            conn.setAutoCommit(false); // Mode transactionnel sécurisé

            // 1. Mise à jour de l'entité Parent dans SQLite
            String sqlParent = """
                UPDATE Parent SET prenom = ?, contactParent = ?, emailParent = ?, derniere_modification = CURRENT_TIMESTAMP
                WHERE idParent = ?
                """;
            stmtParent = conn.prepareStatement(sqlParent);
            stmtParent.setString(1, txtPrenomParent.getText());
            stmtParent.setInt(2, Integer.parseInt(txtContactParent.getText().trim()));
            stmtParent.setString(3, txtEmailParent.getText());
            stmtParent.setString(4, eleveActuel.getIdParent());
            stmtParent.executeUpdate();

            // 2. Mise à jour de l'entité Eleve dans SQLite
            String sqlEleve = """
                UPDATE Eleve SET nom = ?, prenom = ?, dateNaissance = ?, lieuNaissance = ?, 
                sexe = ?, nationnalite = ?, photo = ?, regionOrigine = ?, antecedentsMedicaux = ?, 
                derniere_modification = CURRENT_TIMESTAMP 
                WHERE matriculeEleve = ?
                """;
            stmtEleve = conn.prepareStatement(sqlEleve);
            stmtEleve.setString(1, txtNomEleve.getText());
            stmtEleve.setString(2, txtPrenomEleve.getText());
            stmtEleve.setString(3, dpDateNaissance.getValue() != null ? dpDateNaissance.getValue().toString() : null);
            stmtEleve.setString(4, comboLieuNaissance.getValue());
            stmtEleve.setString(5, comboSexe.getValue() != null ? comboSexe.getValue().toUpperCase() : "MASCULIN");
            stmtEleve.setString(6, comboNationalite.getValue());
            stmtEleve.setString(7, cheminPhotoSelectionnee);
            stmtEleve.setString(8, comboRegionOrigine.getValue());
            stmtEleve.setString(9, txtAntecedentsMedicaux.getText());
            stmtEleve.setString(10, eleveActuel.getMatricule());
            stmtEleve.executeUpdate();

            conn.commit(); // Validation finale de la transaction

            // 3. Mettre à jour l'instance locale en mémoire pour l'affichage de la Fiche Élève
            eleveActuel.setNom(txtNomEleve.getText());
            eleveActuel.setPrenom(txtPrenomEleve.getText());
            if (dpDateNaissance.getValue() != null) {
                eleveActuel.setDateNaissance(Date.from(dpDateNaissance.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            eleveActuel.setSexe(comboSexe.getValue());
            eleveActuel.setNationalite(comboNationalite.getValue());
            eleveActuel.setRegionOrigine(comboRegionOrigine.getValue());
            eleveActuel.setLieuNaissance(comboLieuNaissance.getValue());
            eleveActuel.setAntecedentsMedicaux(txtAntecedentsMedicaux.getText());
            eleveActuel.setPhoto(cheminPhotoSelectionnee);

            if (eleveActuel.parent != null) {
                eleveActuel.parent.setPrenom(txtPrenomParent.getText());
                eleveActuel.parent.setContactParent(Integer.parseInt(txtContactParent.getText().trim()));
                eleveActuel.parent.setEmailParent(txtEmailParent.getText());
            }

            // 4. Exécuter le rafraîchissement graphique de la fiche parente et fermer
            if (onSaveCallback != null) onSaveCallback.run();
            handleAnnuler();

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            labelErreurEleve.setText("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (stmtParent != null) stmtParent.close(); } catch (Exception e) {}
            try { if (stmtEleve != null) stmtEleve.close(); } catch (Exception e) {}
        }
    }

    /**
     * Enregistrer l'élève seul (Bouton alternatif réutilisant l'action globale)
     */
    @FXML
    private void handleEnregistrerEleve() {
        handleEnregistrerParentEtEleve();
    }

    /**
     * Bouton Enregistrer Parent seul (Bouton alternatif réutilisant l'action globale)
     */
    @FXML
    private void handleEnregistrerParent() {
        handleEnregistrerParentEtEleve();
    }

    /**
     * Annule les modifications en fermant simplement le Stage actuel
     */
    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) txtNomEleve.getScene().getWindow();
        stage.close();
    }

    /**
     * Validateur de cohérence basique pour éviter les valeurs nulles
     */
    private boolean validerFormulaire() {
        labelErreurEleve.setText("");
        labelErreurParent.setText("");

        if (txtNomEleve.getText().isEmpty() || txtPrenomEleve.getText().isEmpty() || dpDateNaissance.getValue() == null) {
            labelErreurEleve.setText("Veuillez remplir les champs obligatoires (*) de l'élève.");
            return false;
        }
        if (txtPrenomParent.getText().isEmpty() || txtContactParent.getText().isEmpty()) {
            labelErreurParent.setText("Veuillez renseigner le nom et le contact du parent.");
            return false;
        }
        try {
            Integer.parseInt(txtContactParent.getText().trim());
        } catch (NumberFormatException e) {
            labelErreurParent.setText("Le contact téléphonique doit être uniquement numérique.");
            return false;
        }
        return true;
    }
}