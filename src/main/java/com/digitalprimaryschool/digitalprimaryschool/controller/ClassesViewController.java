package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.CategoryClasse;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.NiveauClasse;
import com.digitalprimaryschool.digitalprimaryschool.controller.DetailClasseController; // IMPORT CORRIGÉ

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ClassesViewController {

    @FXML private StackPane rootStackPane;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboClasse;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Label lblCompteur;

    // Déclaration de la TableView mise à jour selon tes exigences
    @FXML private TableView<Classe> tableClasse;
    @FXML private TableColumn<Classe, String> colNom; // Contient l'aperçu de la photo
    @FXML private TableColumn<Classe, String> colNiveauClasse;
    @FXML private TableColumn<Classe, String> colSection;
    @FXML private TableColumn<Classe, String> colEnseignant;
    @FXML private TableColumn<Classe, String> colNombreEleve;
    @FXML private TableColumn<Classe, String> colStatut;

    private final ClasseDAO Classe = new ClasseDAO();
    private final EleveDAO eleve = new EleveDAO();
    private ObservableList<Classe> masterData = FXCollections.observableArrayList();
    private FilteredList<Classe> filteredData;

    // Dictionnaire d'ID Classe vers Nom Classe
    private final Map<String, String> mapIdClasseVersNom = new HashMap<>();

    @FXML
    public void initialize() {
        ConfigurerColonnes();
    }

    private void ConfigurerColonnes() {
        colNom.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(getTableRow().getItem().getNom());
                }
            }
        });

        // 3. Colonne niveau de classe
        colNiveauClasse.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(getTableRow().getItem().getNiveau().getLibelle());
                }
            }
        });
        colSection.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getTableView() == null){
                    setText(null);
                }else {
                    setText(getTableRow().getItem().getSection().getLibelle());
                }
            }
        });
        colEnseignant.setCellFactory(column -> new TableCell<>(){
            @Override
            protected  void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getTableView() == null){
                    setText(null);
                }else {
                    setText(getTableRow().getItem().getEnseignantNom());
                }
            }
        });

        colNombreEleve.setCellFactory(column -> new TableCell<>(){
            @Override
            protected  void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getTableView() == null){
                    setText(null);
                }else {
                    setText(getTableRow().getItem().getnombreEleve());
                }
            }
        });


    }

    private String remplacerNomNiveau(NiveauClasse niveau) {
        return niveau.name().replace("_", " ");
    }

    private void redirigerVersDetail(Classe classe) {
    }
}