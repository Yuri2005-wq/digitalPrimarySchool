package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DetailClasseController {

    @FXML private Label lblNomClasse;
    @FXML private Label lblNiveauClasse;
    @FXML private Label lblSection;
    @FXML private Label lblEnseignant;
    @FXML private Label lblNombreEleves;
    @FXML private Label lblStatutClasse;
    @FXML private Label lblMoyenneClasse;

    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colMatricule;
    @FXML private TableColumn<Eleve, String> colNomEleve;
    @FXML private TableColumn<Eleve, String> colPrenomEleve;
    @FXML private TableColumn<Eleve, String> colSexe;
    @FXML private TableColumn<Eleve, Double> colMoyenne;

    private Classe classeActuelle;
    private Runnable actionRetour;
    private final EleveDAO eleveDAO = new EleveDAO();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public void setClasse(Classe classe) {
        this.classeActuelle = classe;
        afficherInformationsClasse();
        chargerEleves();
    }

    public void setActionRetour(Runnable actionRetour) {
        this.actionRetour = actionRetour;
    }

    private void afficherInformationsClasse() {
        if (classeActuelle == null) return;

        lblNomClasse.setText(classeActuelle.getNom());
        lblNiveauClasse.setText(classeActuelle.getNiveau().getLibelle());
        lblSection.setText(classeActuelle.getSection().getLibelle());
        lblEnseignant.setText(classeActuelle.getEnseignantNom() != null ? classeActuelle.getEnseignantNom() : "Non assigné");

        int nbEleves = classeActuelle.getNombreEleve();
        lblNombreEleves.setText(String.valueOf(nbEleves));

        boolean estActive = nbEleves > 0;
        lblStatutClasse.setText(estActive ? "Active" : "Vide");
        lblStatutClasse.setStyle(estActive
                ? "-fx-text-fill: #137333; -fx-font-weight: bold;"
                : "-fx-text-fill: #c5221f; -fx-font-weight: bold;");

        lblMoyenneClasse.setText("--");
    }

    private void chargerEleves() {
        try {
            List<Eleve> eleves = eleveDAO.listerParClasse(classeActuelle.getIdClasse());

            colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
            colNomEleve.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenomEleve.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
            colMoyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));

            tableEleves.getItems().clear();
            tableEleves.getItems().addAll(eleves);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        if (actionRetour != null) {
            actionRetour.run();
        }
    }

    @FXML
    private void handleModifier() {
        // À implémenter selon votre formulaire de classe
        System.out.println("Modification de la classe : " + classeActuelle.getNom());
    }
}