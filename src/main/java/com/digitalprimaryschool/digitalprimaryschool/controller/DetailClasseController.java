package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.TarifScolaireDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.TarisScolaire;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.sql.SQLException;
import java.util.List;

public class DetailClasseController {

    @FXML private Label lblNomClasse;
    @FXML private Label lblMontantPension;
    @FXML private Label lblFraisInscription;
    @FXML private TableView<Eleve> tableEleves; // Assigne tes colonnes (Matricule, Nom, Prénom) dans Scene Builder

    private final TarifScolaireDAO tarifDAO = new TarifScolaireDAO();
    private final EleveDAO eleveDAO = new EleveDAO();

    public void initialiserDonnees(Classe classe) {
        lblNomClasse.setText("Détails - " + classe.getNom());

        try {
            // 1. Chargement et affichage des tarifs réels de la BD basés sur le niveau de la classe
            if (classe.getNiveau() != null) {
                TarisScolaire tarif = tarifDAO.trouverParClasse(classe.getNiveau().name());
                if (tarif != null) {
                    lblMontantPension.setText(tarif.getMontantPension() + " FCFA");
                    lblFraisInscription.setText(tarif.getFraisInscription() + " FCFA");
                } else {
                    lblMontantPension.setText("Non défini");
                    lblFraisInscription.setText("Non défini");
                }
            }

            // 2. Chargement de la liste complète des élèves inscrits dans cette classe spécifique
            List<Eleve> listeEleves = eleveDAO.listerParClasse(classe.getIdClasse());
            tableEleves.getItems().clear();
            tableEleves.getItems().addAll(listeEleves);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}