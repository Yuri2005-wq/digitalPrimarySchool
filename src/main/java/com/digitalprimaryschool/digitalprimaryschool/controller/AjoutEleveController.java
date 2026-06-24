package com.digitalprimaryschool.digitalprimaryschool.controller;

//import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class AjoutEleveController {
//
//    @FXML private HBox modalAppBar;
//    @FXML private TextField champNom;
//    @FXML private TextField champPrenom;
//    @FXML private DatePicker champDateNaissance;
//    @FXML private ComboBox<Classe> comboClasse;
//    @FXML private Label labelErreur;
//
//    // Callback appelé après un enregistrement réussi (pour rafraîchir la liste du parent)
//    private Runnable onEleveAjoute;
//
//    public void setOnEleveAjoute(Runnable callback) {
//        this.onEleveAjoute = callback;
//    }
//
//    @FXML
//    public void initialize() {
//        // Remplit le ComboBox avec les classes existantes
//        try {
//            ClasseDAO classeDAO = new ClasseDAO();
//            comboClasse.getItems().addAll(classeDAO.listerToutes());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        // Affichage du nom de la classe dans le ComboBox (au lieu de toString())
//        comboClasse.setConverter(new javafx.util.StringConverter<>() {
//            @Override
//            public String toString(Classe classe) {
//                return classe == null ? "" : classe.getNom();
//            }
//            @Override
//            public Classe fromString(String s) {
//                return null; // pas nécessaire ici (lecture seule)
//            }
//        });
//    }
//
//    @FXML
//    public void closeModal() {
//        Stage stage = (Stage) modalAppBar.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    public void enregistrer() {
//        String nom = champNom.getText().trim();
//        String prenom = champPrenom.getText().trim();
//        LocalDate dateNaissance = champDateNaissance.getValue();
//        Classe classe = comboClasse.getValue();
//
//        // --- Validation ---
//        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null || classe == null) {
//            afficherErreur("Veuillez remplir tous les champs.");
//            return;
//        }
//
//        try {
//            Eleve eleve = new Eleve();
//            eleve.setNom(nom);
//            eleve.setPrenom(prenom);
//            eleve.setDateNaissance(dateNaissance);
//            eleve.setClasseId(classe.getId());
//
//            EleveDAO eleveDAO = new EleveDAO();
//            eleveDAO.ajouter(eleve);
//
//            // Prévient le parent pour rafraîchir sa liste
//            if (onEleveAjoute != null) {
//                onEleveAjoute.run();
//            }
//
//            closeModal();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            afficherErreur("Erreur lors de l'enregistrement : " + e.getMessage());
//        }
//    }
//
//    private void afficherErreur(String message) {
//        labelErreur.setText(message);
//        labelErreur.setVisible(true);
//        labelErreur.setManaged(true);
//    }
}