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

    // Éléments de l'en-tête
    @FXML private Label lblMatriculeHeader;

    // Éléments du bandeau principal (Profil)
    @FXML private Label lblNomComplet;
    @FXML private Label lblBadgeStatut;
    @FXML private Label lblClasseGenre;
    @FXML private Label lblMetasNaissance;
    @FXML private Label lblMoyenneHeader;
    @FXML private Label lblAbsencesHeader;
    @FXML private ImageView imgPhotoEleve;

    // Section 1 : Informations personnelles
    @FXML private Label lblAdresse;
    @FXML private Label lblNationalite;
    @FXML private Label lblDateInscription;
    @FXML private Label lblMatriculeCorps;
    @FXML private Label lblAntecedentsMedicaux;

    // Section 2 : Contacts parents
    @FXML private Label lblNomParent;
    @FXML private Label lblProfessionParent;
    @FXML private Label lblTelephoneParent;
    @FXML private Label lblEmailParent;

    private Eleve eleveActuel;
    private Runnable actionRetour;
    private Map<String, String> mapClasses;
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Reçoit le dictionnaire de correspondance ID Classe -> Nom de la classe
     */
    public void setMapClasses(Map<String, String> mapClasses) {
        this.mapClasses = mapClasses;
    }

    /**
     * Initialise la fiche détaillée avec les données réelles issues de la base de données
     */

    public void setEleveFormulaire(Eleve eleve, Runnable actionRetour) {
        this.eleveActuel = eleve;
        this.actionRetour = actionRetour;

        if (eleve == null) return;

        // 1. Remplissage des En-têtes & Identifiants uniques
        lblMatriculeHeader.setText("Matricule : " + eleve.getMatricule());
        lblMatriculeCorps.setText(eleve.getMatricule());
        lblNomComplet.setText(eleve.getFullName() != null ? eleve.getFullName() : eleve.getNom() + " " + eleve.getPrenom());

        // 2. Gestion dynamique du Badge de Statut (Actif / Non Inscrit)
        boolean estInscrit = (eleve.getInscription() != null);
        if (estInscrit) {
            lblBadgeStatut.setText("Actif");
            lblBadgeStatut.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8;");
        } else {
            lblBadgeStatut.setText("Non Inscrit");
            lblBadgeStatut.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8;");
        }

        // 3. Résolution de la classe et formatage du Genre
        String nomClasse = "Sans Classe";
        if (estInscrit && mapClasses != null) {
            String resolution = mapClasses.get(eleve.getInscription().getIdClasse());
            if (resolution != null) nomClasse = resolution;
        }
        String genre = (eleve.getSexe() != null) ? eleve.getSexe().getLibelle() : "Non spécifié";
        lblClasseGenre.setText(nomClasse + " · " + genre);

        // 4. Métadonnées de Naissance
        String dateNaiss = (eleve.getDateNaissance() != null) ? formatterDate.format(eleve.getDateNaissance()) : "--/--/----";
        String lieuNaiss = (eleve.getLieuNaissance() != null ? eleve.getLieuNaissance().getLibelle() : "Lieu inconnu");
        lblNationalite.setText(eleve.getNationalite() != null ? eleve.getNationalite().getLibelle() :  "Non renseignée");
        lblMetasNaissance.setText("Né(e) le " + dateNaiss + " à " + lieuNaiss + " · " + lblNationalite.getText());

        // 5. KPIs et Suivi Académique (Sécurisé)
        lblMoyenneHeader.setText(eleve.getMoyenne() > 0 ? String.format("%.2f", eleve.getMoyenne()) : "--.-");
        lblAbsencesHeader.setText(String.valueOf(eleve.getNombreAbsences()));

        // 6. Informations Complémentaires Corps
        lblAdresse.setText(eleve.getAdresse() != null && !eleve.getAdresse().isEmpty() ? eleve.getAdresse() : "Non renseignée");
        lblDateInscription.setText(eleve.getDateInscription() != null ? formatterDate.format(eleve.getDateInscription()) : dateNaiss);
        lblAntecedentsMedicaux.setText(eleve.getAntecedentsMedicaux() != null && !eleve.getAntecedentsMedicaux().isEmpty() ? eleve.getAntecedentsMedicaux() : "Aucun");

        // 7. Contacts & Parents
        lblNomParent.setText(eleve.getNomParent() != null && !eleve.getNomParent().isEmpty() ? eleve.getNomParent() : "Non renseigné");
        lblProfessionParent.setText(eleve.getProfessionParent() != null && !eleve.getProfessionParent().isEmpty() ? eleve.getProfessionParent() : "Non renseignée");
        lblTelephoneParent.setText(eleve.getTelephoneParent() != null && !eleve.getTelephoneParent().isEmpty() ? eleve.getTelephoneParent() : "Aucun numéro");
        lblEmailParent.setText(eleve.getEmailParent() != null && !eleve.getEmailParent().isEmpty() ? eleve.getEmailParent() : "Aucun email");

        // 8. Chargement Sécurisé de la Photo d'identité (Gestion absolue/relative + URI)
        chargerPhotoIdentite();
    }

    private void chargerPhotoIdentite() {
        if (eleveActuel.getPhoto() != null && !eleveActuel.getPhoto().isEmpty()) {
            try {
                String cheminPhoto = eleveActuel.getPhoto();
                // Si c'est un chemin absolu Windows ou Linux/macOS
                if (cheminPhoto.contains(":") || cheminPhoto.startsWith("/") || cheminPhoto.startsWith("\\")) {
                    File dossierFichier = new File(cheminPhoto);
                    if (dossierFichier.exists()) {
                        imgPhotoEleve.setImage(new Image(dossierFichier.toURI().toString()));
                        return;
                    }
                } else {
                    // Si c'est un chemin relatif au projet (ex: uploads/eleve_xyz.jpg)
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
            if (eleveActuel.getSexe() != null && "Masculin".equalsIgnoreCase(eleveActuel.getSexe().getLibelle())) {
                imgPhotoEleve.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/avatar.jpg")));
                System.out.println("erreur");
            } else {
                imgPhotoEleve.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/img.jpg")));
            }
        } catch (Exception e) {
            System.out.println("erreur" + e);
        }
    }

    @FXML
    private void handleRetour() {
        if (actionRetour != null) {
            actionRetour.run();
        } else {
            // Option de repli si le runnable parent est absent
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
            // 1. Charger le FXML du formulaire de modification
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/FormulaireEleve.fxml")
            );
            Parent root = loader.load();

            // 2. Récupérer son contrôleur et lui injecter l'élève actuel pour pré-remplir les champs
            FormulaireEleveController formController = loader.getController();
            formController.initialiserDonnees(this.eleveActuel, () -> {
                // Ce code s'exécutera après la sauvegarde réussie pour rafraîchir la fiche actuelle
                setEleveFormulaire(this.eleveActuel, this.actionRetour);
            });

            // 3. Créer et afficher la nouvelle fenêtre de dialogue (Modal)
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("oh.png")));
            stage.setTitle("Modifier les informations de l'élève");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Bloque la fenêtre principale derrière
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
//        String nomFichier = "Fiche_Eleve_" + eleveActuel.getMatricule() + ".pdf";
//
//        try {
//            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(nomFichier);
//            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
//            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
//
//            // Écriture du contenu
//            document.add(new com.itextpdf.layout.element.Paragraph("FICHE DE L'ÉLÈVE").setBold().setFontSize(20));
//            document.add(new com.itextpdf.layout.element.Paragraph("Matricule : " + eleveActuel.getMatricule()));
//            document.add(new com.itextpdf.layout.element.Paragraph("Nom Complet : " + eleveActuel.getFullName()));
//            document.add(new com.itextpdf.layout.element.Paragraph("Antécédents Médicaux : " + eleveActuel.getAntecedentsMedicaux()));
//
//            if (eleveActuel.parent != null) {
//                document.add(new com.itextpdf.layout.element.Paragraph("Responsable Légal : " + eleveActuel.getNomParent()));
//                document.add(new com.itextpdf.layout.element.Paragraph("Téléphone : " + eleveActuel.getTelephoneParent()));
//            }
//
//            document.close();
//            System.out.println("PDF généré avec succès sous le nom : " + nomFichier);
//
//        } catch (Exception e) {
//            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
//            e.printStackTrace();
//        }
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