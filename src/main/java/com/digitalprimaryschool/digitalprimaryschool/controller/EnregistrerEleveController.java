package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.*;
import com.digitalprimaryschool.digitalprimaryschool.service.EnregistrementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EnregistrerEleveController {

    // Parent
    @FXML private TextField txtPrenomParent;
    @FXML private TextField txtContactParent;
    @FXML private TextField txtEmailParent;
    @FXML private TextField txtProfessionParent; // Le champ de recherche FXML
    @FXML private ComboBox<Profession> comboProfessionParent;
    @FXML private TextField txtChercherAdresse; // Le champ de recherche FXML
    @FXML private ComboBox<Quartier> comboAdresseParent;

    // Élève
    @FXML private TextField txtNomEleve;
    @FXML private TextField txtPrenomEleve;
    @FXML private DatePicker dpDateNaissance;
    @FXML private ImageView imgApercu;
    @FXML private TextField txtChercherSexe; // Le champ de recherche FXML
    @FXML private ComboBox<Sexe> comboSexe;
    @FXML private TextField txtChercherNationalite; // Le champ de recherche FXML
    @FXML private ComboBox<Nationnalite> comboNationalite;
    @FXML private TextField txtChercherParentExistant; // Le champ de recherche FXML
    @FXML private ComboBox<Parent> comboParentExistant;
    @FXML private TextField txtChercherRegion; // Le champ de recherche FXML
    @FXML private ComboBox<Region> comboRegionOrigine;
    @FXML private TextField txtChercherLieuNaissance; // Le champ de recherche FXML
    @FXML private ComboBox<LieuNaissance> comboLieuNaissance;
    @FXML private TextArea txtAntecedentsMedicaux;

    @FXML private Label labelErreurParent;
    @FXML private Label labelErreurEleve;

    private EnregistrementService enregistrementService;
    private File photoFile;
    private final ObservableList<Parent> tousLesParents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        enregistrementService = new EnregistrementService();

        initComboBoxes();
        chargerParents();
        setupValidators();
    }

    private void initComboBoxes() {
        // Configuration des formats d'affichage visuels (StringConverters)
        comboProfessionParent.setConverter(new StringConverter<>() {
            @Override public String toString(Profession p) {
                return p == null ? "" : p.getLibelle();
            }
            @Override public Profession fromString(String str) { return null; }
        });

        comboLieuNaissance.setConverter(new StringConverter<>() {
            @Override public String toString(LieuNaissance l) { return l == null ? "" : l.getLibelle(); }
            @Override public LieuNaissance fromString(String str) { return null; }
        });

        comboSexe.setConverter(new StringConverter<>() {
            @Override public String toString(Sexe s) { return s == null ? "" : s.getLibelle(); }
            @Override public Sexe fromString(String str) { return null; }
        });

        comboNationalite.setConverter(new StringConverter<>() {
            @Override public String toString(Nationnalite n) { return n == null ? "" : n.getLibelle(); }
            @Override public Nationnalite fromString(String str) { return null; }
        });

        comboAdresseParent.setConverter(new StringConverter<>() {
            @Override public String toString(Quartier q) { return q == null ? "" : q.getLibelle(); }
            @Override public Quartier fromString(String str) { return null; }
        });

        comboRegionOrigine.setConverter(new StringConverter<>() {
            @Override public String toString(Region r) { return r == null ? "" : r.getLibelle(); }
            @Override public Region fromString(String str) { return null; }
        });

        comboParentExistant.setConverter(new StringConverter<>() {
            @Override public String toString(Parent p) { return p == null ? "" : p.getPrenom() + " - " + p.getContactParent(); }
            @Override public Parent fromString(String str) { return null; }
        });

        // Liaison des ComboBox avec leurs TextField de filtrage respectifs (setEditable passe à false)
        configurerFiltreSimple(comboParentExistant, txtChercherParentExistant, tousLesParents,
                (p, t) -> p.getPrenom().toLowerCase().contains(t) || String.valueOf(p.getContactParent()).contains(t));

        configurerFiltreSimple(comboSexe, txtChercherSexe, FXCollections.observableArrayList(Sexe.values()),
                (s, t) -> s.getLibelle().toLowerCase().contains(t));

        configurerFiltreSimple(comboNationalite, txtChercherNationalite, FXCollections.observableArrayList(Nationnalite.values()),
                (n, t) -> n.getLibelle().toLowerCase().contains(t));

        configurerFiltreSimple(comboAdresseParent, txtChercherAdresse, FXCollections.observableArrayList(Quartier.values()),
                (q, t) -> q.getLibelle().toLowerCase().contains(t));

        configurerFiltreSimple(comboRegionOrigine, txtChercherRegion, FXCollections.observableArrayList(Region.values()),
                (r, t) -> r.getLibelle().toLowerCase().contains(t));

        configurerFiltreSimple(comboLieuNaissance, txtChercherLieuNaissance, FXCollections.observableArrayList(LieuNaissance.values()),
                (l, t) -> l.getLibelle().toLowerCase().contains(t));

        configurerFiltreSimple(comboProfessionParent, txtProfessionParent, FXCollections.observableArrayList(Profession.values()),
                (p, t) -> p.getLibelle().toLowerCase().contains(t));
    }

    private void chargerParents() {
        try {
            List<Parent> parents = enregistrementService.getTousLesParents();
            tousLesParents.clear();
            if (parents != null && !parents.isEmpty()) {
                parents.sort(Comparator.comparing(Parent::getDateCreation).reversed());
                tousLesParents.addAll(parents);
                System.out.println("Parents chargés : " + tousLesParents.size());
            } else {
                System.out.println("Aucun parent trouvé.");
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement parents : " + e.getMessage());
            showErrorAlert("Erreur", "Impossible de charger les parents : " + e.getMessage());
        }
    }

    private <T> void configurerFiltreSimple(ComboBox<T> comboBox, TextField searchField, ObservableList<T> items,
                                            java.util.function.BiPredicate<T, String> predicate) {
        FilteredList<T> filteredList = new FilteredList<>(items, p -> true);
        comboBox.setItems(filteredList);
        comboBox.setEditable(false); // Fix principal : évite la disparition au changement de focus

        // Écoute du champ de recherche textuel indépendant
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String filter = newText == null ? "" : newText.trim().toLowerCase();
            filteredList.setPredicate(item -> {
                if (filter.isEmpty()) return true;
                return predicate.test(item, filter);
            });

            // Dérouler automatiquement le combo si l'utilisateur saisit du texte
            if (!filteredList.isEmpty() && searchField.isFocused()) {
                comboBox.show();
            }
        });
    }

    private void setupValidators() {
        txtContactParent.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                if (!newVal.matches("\\d*")) {
                    txtContactParent.setText(oldVal != null ? oldVal : "");
                    showErrorAlert("Saisie Invalide", "Le contact doit contenir uniquement des chiffres.");
                }
            }
        });

        txtNomEleve.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                if (newVal.matches(".*\\d.*")) {
                    txtNomEleve.setText(oldVal != null ? oldVal : "");
                    showErrorAlert("Saisie Invalide", "Le nom de l'élève ne doit pas contenir de chiffres.");
                }
            }
        });

        txtPrenomEleve.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                if (newVal.matches(".*\\d.*")) {
                    txtPrenomEleve.setText(oldVal != null ? oldVal : "");
                    showErrorAlert("Saisie Invalide", "Le prénom de l'élève ne doit pas contenir de chiffres.");
                }
            }
        });

        txtPrenomParent.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                if (newVal.matches(".*\\d.*")) {
                    txtPrenomParent.setText(oldVal != null ? oldVal : "");
                    showErrorAlert("Saisie Invalide", "Le nom du parent ne doit pas contenir de chiffres.");
                }
            }
        });

        dpDateNaissance.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.isAfter(LocalDate.now())) {
                    dpDateNaissance.setValue(oldVal != null ? oldVal : null);
                    showErrorAlert("Date Non Valide", "La date de naissance ne peut pas être dans le futur.");
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return true;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showErrorAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validerParent() {
        if (txtPrenomParent.getText().isEmpty()) { showErrorAlert("Erreur de saisie", "Le nom du parent est obligatoire."); return false; }
        if (txtContactParent.getText().isEmpty()) { showErrorAlert("Erreur de saisie", "Le contact téléphonique est obligatoire."); return false; }
        if (comboAdresseParent.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner un quartier."); return false; }
        if (comboProfessionParent.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner une profession."); return false; }
        if (!txtContactParent.getText().matches("\\d+")) { showErrorAlert("Erreur de validation", "Le contact doit contenir uniquement des chiffres."); return false; }
        if (txtPrenomParent.getText().matches(".*\\d.*")) { showErrorAlert("Erreur de validation", "Le nom du parent ne doit pas contenir de chiffres."); return false; }
        if (!txtEmailParent.getText().isEmpty() && !isValidEmail(txtEmailParent.getText())) { showErrorAlert("Erreur de validation", "Le format de l'adresse email est invalide."); return false; }
        return true;
    }

    private boolean validerEleve() {
        if (txtNomEleve.getText().isEmpty()) { showErrorAlert("Erreur de saisie", "Le nom de l'élève est obligatoire."); return false; }
        if (txtPrenomEleve.getText().isEmpty()) { showErrorAlert("Erreur de saisie", "Le prénom de l'élève est obligatoire."); return false; }
        if (dpDateNaissance.getValue() == null) { showErrorAlert("Erreur de saisie", "La date de naissance est obligatoire."); return false; }
        if (comboLieuNaissance.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner le lieu de naissance."); return false; }
        if (comboSexe.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner le sexe."); return false; }
        if (comboNationalite.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner la nationalité."); return false; }
        if (comboRegionOrigine.getValue() == null) { showErrorAlert("Erreur de saisie", "Veuillez sélectionner la région d'origine."); return false; }
        if (txtNomEleve.getText().matches(".*\\d.*")) { showErrorAlert("Erreur de validation", "Le nom de l'élève ne doit pas contenir de chiffres."); return false; }
        if (txtPrenomEleve.getText().matches(".*\\d.*")) { showErrorAlert("Erreur de validation", "Le prénom de l'élève ne doit pas contenir de chiffres."); return false; }
        return true;
    }

    @FXML
    private void handleChoisirPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            photoFile = file;
            imgApercu.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleEnregistrerParent() {
        try {
            if (!validerParent()) return;
            Parent parent = construireParent();

            if (!showConfirmation("Confirmation", "Voulez-vous vraiment enregistrer ce parent ?\n\nNom: " + parent.getPrenom())) return;

            EnregistrementService.Resultat result = enregistrementService.enregistrerParent(parent);
            if (result.isSuccess()) {
                showAlert("Succès", "Parent enregistré avec succès !");
                reinitialiserFormulaire();
                chargerParents();
            } else {
                showErrorAlert("Erreur", result.getMessage());
            }
        } catch (Exception e) {
            showErrorAlert("Erreur système", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void handleEnregistrerEleve() {
        try {
            if (!validerEleve()) return;
            Parent parent = recupererParent();
            if (parent == null) {
                showErrorAlert("Sélection requise", "Veuillez sélectionner un parent existant ou remplir les détails d'un nouveau parent.");
                return;
            }

            Eleve eleve = construireEleve();
            if (!showConfirmation("Confirmation", "Voulez-vous vraiment enregistrer cet élève ?\n\nNom: " + eleve.getNom() + " " + eleve.getPrenom())) return;

            EnregistrementService.Resultat result = enregistrementService.enregistrerEleve(parent, eleve);
            if (result.isSuccess()) {
                showAlert("Succès", "Élève enregistré avec succès !");
                reinitialiserEleve();
            } else {
                showErrorAlert("Erreur", result.getMessage());
            }
        } catch (Exception e) {
            showErrorAlert("Erreur système", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void handleEnregistrerParentEtEleve() {
        try {
            if (!validerParent() || !validerEleve()) return;
            Parent parent = recupererParent();
            if (parent == null) {
                showErrorAlert("Sélection requise", "Veuillez sélectionner un parent existant ou remplir les détails d'un nouveau parent.");
                return;
            }
            Eleve eleve = construireEleve();

            if (!showConfirmation("Confirmation", "Voulez-vous vraiment enregistrer ce parent et cet élève en bloc ?")) return;

            EnregistrementService.Resultat result = enregistrementService.enregistrerParentEtEleve(parent, eleve);
            if (result.isSuccess()) {
                showAlert("Succès", "Le dossier complet a été enregistré avec succès !");
                reinitialiserFormulaire();
                chargerParents();
            } else {
                showErrorAlert("Erreur", result.getMessage());
            }
        } catch (Exception e) {
            showErrorAlert("Erreur système", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private Parent recupererParent() {
        Parent parent = comboParentExistant.getValue();
        if (parent != null) return parent;
        return (txtPrenomParent.getText().isEmpty() || txtContactParent.getText().isEmpty()) ? null : construireParent();
    }

    private Parent construireParent() {
        Parent parent = new Parent();
        parent.setPrenom(txtPrenomParent.getText());
        try {
            parent.setContactParent(Integer.parseInt(txtContactParent.getText().trim()));
        } catch (NumberFormatException e) {
            return null;
        }
        parent.setEmailParent(txtEmailParent.getText());
        parent.setProfession(comboProfessionParent.getValue());
        parent.setAdresse(comboAdresseParent.getValue());
        return parent;
    }

    private Eleve construireEleve() {
        Eleve eleve = new Eleve();
        eleve.setNom(txtNomEleve.getText());
        eleve.setPrenom(txtPrenomEleve.getText());
        if (dpDateNaissance.getValue() != null) {
            eleve.setDateNaissance(Date.from(dpDateNaissance.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        eleve.setLieuNaissance(comboLieuNaissance.getValue());
        eleve.setSexe(comboSexe.getValue());
        eleve.setNationalite(comboNationalite.getValue());
        eleve.setRegionOrigine(comboRegionOrigine.getValue());
        eleve.setAntecedentsMedicaux(txtAntecedentsMedicaux.getText());
        if (photoFile != null) eleve.setPhoto(photoFile.getAbsolutePath());
        return eleve;
    }

    private void reinitialiserParent() {
        txtPrenomParent.clear();
        txtContactParent.clear();
        txtEmailParent.clear();
        txtProfessionParent.clear();
        txtChercherAdresse.clear();
        comboAdresseParent.setValue(null);
        comboProfessionParent.setValue(null);
    }

    private void reinitialiserEleve() {
        txtNomEleve.clear();
        txtPrenomEleve.clear();
        dpDateNaissance.setValue(null);
        txtChercherLieuNaissance.clear();
        comboLieuNaissance.setValue(null);
        txtChercherSexe.clear();
        comboSexe.setValue(null);
        txtChercherNationalite.clear();
        comboNationalite.setValue(null);
        txtChercherRegion.clear();
        comboRegionOrigine.setValue(null);
        txtAntecedentsMedicaux.clear();
        imgApercu.setImage(null);
        photoFile = null;
    }

    private void reinitialiserFormulaire() {
        reinitialiserParent();
        reinitialiserEleve();
        txtChercherParentExistant.clear();
        comboParentExistant.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        a.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        ButtonType oui = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType non = new ButtonType("Non", ButtonBar.ButtonData.NO);
        a.getButtonTypes().setAll(oui, non);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == oui;
    }
}