package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.HelloApplication;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.service.PDFService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

public class FicheEleveController {

    @FXML private Label lblMatriculeHeader;
    @FXML private Label lblNomComplet;
    @FXML private Label lblBadgeStatut;
    @FXML private Label lblClasseGenre;
    @FXML private Label lblMetasNaissance;
    @FXML private Label lblMoyenneHeader;
    @FXML private Label lblAbsencesHeader;
    @FXML private ImageView imgPhotoEleve;

    @FXML private Label lblAdresse;
    @FXML private Label lblNationalite;
    @FXML private Label lblDateInscription;
    @FXML private Label lblMatriculeCorps;
    @FXML private Label lblAntecedentsMedicaux;

    @FXML private Label lblNomParent;
    @FXML private Label lblProfessionParent;
    @FXML private Label lblTelephoneParent;
    @FXML private Label lblEmailParent;

    private Eleve eleveActuel;
    private Runnable actionRetour;
    private Map<String, String> mapClasses;
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");

    public void setMapClasses(Map<String, String> mapClasses) {
        this.mapClasses = mapClasses;
    }

    public void setEleveFormulaire(Eleve eleve, Runnable actionRetour) {
        this.eleveActuel = eleve;
        this.actionRetour = actionRetour;

        if (eleve == null) return;

        lblMatriculeHeader.setText("Matricule : " + eleve.getMatricule());
        lblMatriculeCorps.setText(eleve.getMatricule());
        lblNomComplet.setText(eleve.getFullName() != null ? eleve.getFullName() : eleve.getNom() + " " + eleve.getPrenom());

        boolean estInscrit = (eleve.getInscription() != null);
        if (estInscrit) {
            lblBadgeStatut.setText("Actif");
            lblBadgeStatut.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8;");
        } else {
            lblBadgeStatut.setText("Non Inscrit");
            lblBadgeStatut.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8;");
        }

        String nomClasse = "Sans Classe";
        if (estInscrit && mapClasses != null) {
            String resolution = mapClasses.get(eleve.getInscription().getIdClasse());
            if (resolution != null) nomClasse = resolution;
        }
        String genre = (eleve.getSexe() != null) ? eleve.getSexe().getLibelle() : "Non spécifié";
        lblClasseGenre.setText(nomClasse + " · " + genre);

        String dateNaiss = (eleve.getDateNaissance() != null) ? formatterDate.format(eleve.getDateNaissance()) : "--/--/----";
        String lieuNaiss = (eleve.getLieuNaissance() != null ? eleve.getLieuNaissance().getLibelle() : "Lieu inconnu");
        lblNationalite.setText(eleve.getNationalite() != null ? eleve.getNationalite().getLibelle() :  "Non renseignée");
        lblMetasNaissance.setText("Né(e) le " + dateNaiss + " à " + lieuNaiss + " · " + lblNationalite.getText());

        lblMoyenneHeader.setText(eleve.getMoyenne() > 0 ? String.format("%.2f", eleve.getMoyenne()) : "--.-");
        lblAbsencesHeader.setText(String.valueOf(eleve.getNombreAbsences()));

        lblAdresse.setText(eleve.getAdresse() != null && !eleve.getAdresse().isEmpty() ? eleve.getAdresse() : "Non renseignée");
        lblDateInscription.setText(eleve.getDateInscription() != null ? formatterDate.format(eleve.getDateInscription()) : dateNaiss);
        lblAntecedentsMedicaux.setText(eleve.getAntecedentsMedicaux() != null && !eleve.getAntecedentsMedicaux().isEmpty() ? eleve.getAntecedentsMedicaux() : "Aucun");

        lblNomParent.setText(eleve.getNomParent() != null && !eleve.getNomParent().isEmpty() ? eleve.getNomParent() : "Non renseigné");
        lblProfessionParent.setText(eleve.getProfessionParent() != null && !eleve.getProfessionParent().isEmpty() ? eleve.getProfessionParent() : "Non renseignée");
        lblTelephoneParent.setText(eleve.getTelephoneParent() != null && !eleve.getTelephoneParent().isEmpty() ? eleve.getTelephoneParent() : "Aucun numéro");
        lblEmailParent.setText(eleve.getEmailParent() != null && !eleve.getEmailParent().isEmpty() ? eleve.getEmailParent() : "Aucun email");

        chargerPhotoIdentite();
    }

    private void chargerPhotoIdentite() {
        if (eleveActuel.getPhoto() != null && !eleveActuel.getPhoto().isEmpty()) {
            try {
                String cheminPhoto = eleveActuel.getPhoto();
                if (cheminPhoto.contains(":") || cheminPhoto.startsWith("/") || cheminPhoto.startsWith("\\")) {
                    File dossierFichier = new File(cheminPhoto);
                    if (dossierFichier.exists()) {
                        imgPhotoEleve.setImage(new Image(dossierFichier.toURI().toString()));
                        return;
                    }
                } else {
                    File fichierRelatif = new File(cheminPhoto);
                    if (fichierRelatif.exists()) {
                        imgPhotoEleve.setImage(new Image(fichierRelatif.toURI().toString()));
                        return;
                    }
                }
            } catch (Exception ex) {
                System.out.println("Échec du chargement de l'image personnalisée : " + ex.getMessage());
            }
        }
        chargerAvatarParDefaut();
    }

    private void chargerAvatarParDefaut() {
        try {
            // Harmonisation avec l'extension .png trouvée dans GestionElevesController
            if (eleveActuel.getSexe() != null && "Masculin".equalsIgnoreCase(eleveActuel.getSexe().getLibelle())) {
                imgPhotoEleve.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/avatar.png")));
            } else {
                imgPhotoEleve.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/img.png")));
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement avatar par défaut : " + e);
        }
    }

    @FXML
    private void handleRetour() {
        if (actionRetour != null) {
            actionRetour.run();
        } else {
            try {
                BorderPane root = (BorderPane) lblNomComplet.getScene().getRoot();
                Parent vueListe = new javafx.fxml.FXMLLoader(getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/GestionElevesView.fxml")).load();
                root.setCenter(vueListe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleModifier() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/FormulaireEleve.fxml")
            );
            Parent root = loader.load();

            FormulaireEleveController formController = loader.getController();
            formController.initialiserDonnees(this.eleveActuel, () -> {
                // S'exécute après la sauvegarde pour recharger la fiche avec les nouvelles valeurs
                setEleveFormulaire(this.eleveActuel, this.actionRetour);
            });

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));
            stage.setTitle("Modifier les informations de l'élève");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.setWidth(1000);
            stage.setResizable(false);
            stage.setHeight(640);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExporterPDF() {
        PDFService servicePdf = new PDFService();
        boolean estInscrit = (eleveActuel.getInscription() != null);
        String nomClasse = null;
        if (estInscrit && mapClasses != null) {
            String resolution = mapClasses.get(eleveActuel.getInscription().getIdClasse());
            nomClasse = "";
            if (resolution != null) nomClasse = resolution;
        }
        servicePdf.genererFicheEleve(eleveActuel, nomClasse);
    }
}