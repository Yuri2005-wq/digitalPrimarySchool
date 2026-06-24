package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.HelloApplication;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class GestionElevesController {

    @FXML private StackPane rootStackPane;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboClasse;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Label lblCompteur;

    // Déclaration de la TableView mise à jour selon tes exigences
    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, String> colPhoto; // Contient l'aperçu de la photo
    @FXML private TableColumn<Eleve, String> colNom;
    @FXML private TableColumn<Eleve, String> colPrenom;
    @FXML private TableColumn<Eleve, String> colDateNaissance;
    @FXML private TableColumn<Eleve, String> colClasse;
    @FXML private TableColumn<Eleve, String> colStatut;

    private final EleveDAO eleveDAO = new EleveDAO();
    private final ClasseDAO classeDAO = new ClasseDAO();
    private ObservableList<Eleve> masterData = FXCollections.observableArrayList();
    private FilteredList<Eleve> filteredData;

    // Dictionnaire d'ID Classe vers Nom Classe
    private final Map<String, String> mapIdClasseVersNom = new HashMap<>();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerFiltres();
        chargerDonneesBDD();

        tableEleves.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableEleves.getSelectionModel().getSelectedItem() != null) {
                ouvrirFicheDetaillee(tableEleves.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurerColonnes() {
        // 1. Colonne Aperçu Photo Élève
        colPhoto.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Eleve e = getTableRow().getItem();
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);
                    imageView.setPreserveRatio(true);

                    if (e.getPhoto() != null && !e.getPhoto().isEmpty()) {
                        try {
                            String photoPath = e.getPhoto();

                            if (photoPath.contains(":") || photoPath.startsWith("/") || photoPath.startsWith("\\")) {
                                File file = new File(photoPath);
                                if (file.exists()) {
                                    imageView.setImage(new Image(file.toURI().toString()));
                                } else {
                                    chargerAvatarDefaut();
                                }
                            } else {
                                imageView.setImage(new Image(photoPath));
                            }
                        } catch (Exception ex) {
                            System.out.println("Erreur lors du chargement de l'image de l'eleve : " + ex);
                            chargerAvatarDefaut();
                        }
                    } else {
                        chargerAvatarDefaut();
                    }
                    setGraphic(imageView);
                }
            }
            private void chargerAvatarDefaut() {
                Eleve eleve = getTableRow().getItem();
                try {
                    // Utilisation de .equals() plutôt que == pour la comparaison de chaînes de caractères
                    if(eleve != null && eleve.getSexe() != null && "Masculin".equalsIgnoreCase(eleve.getSexe().getLibelle())){
                        imageView.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/avatar.png")));
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/img.png")));
                    }
                } catch (Exception e) {
                    imageView.setImage(null); // Protection si l'image par défaut manque
                }
            }
        });

        // 2. Colonne pour le nom complet
        colNom.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(getTableRow().getItem().getMatricule());
                }
            }
        });

        // 3. Colonne Prénom
        colPrenom.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(getTableRow().getItem().getFullName());
                }
            }
        });

        // 4. Colonne Date de naissance
        colDateNaissance.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null || getTableRow().getItem().getDateNaissance() == null) {
                    setText(null);
                } else {
                    setText(formatter.format(getTableRow().getItem().getDateNaissance()));
                }
            }
        });

        // 5. Colonne Classe (via correspondance ID -> Nom)
        colClasse.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Eleve e = getTableRow().getItem();
                    if (e.getInscription() != null) {
                        String nomClasse = mapIdClasseVersNom.get(e.getInscription().getIdClasse());
                        setText(nomClasse != null ? nomClasse : "Inconnue");
                    } else {
                        setText("Sans Classe");
                    }
                }
            }
        });

        // 6. Colonne Statut (Badge graphique)
        colStatut.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Eleve e = getTableRow().getItem();
                    boolean estInscrit = (e.getInscription() != null);
                    Label badge = new Label(estInscrit ? "Actif" : "Non Inscrit");
                    badge.setStyle(estInscrit
                            ? "-fx-background-color: #e6f4ea; -fx-text-fill: #137333; -fx-padding: 4 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;"
                            : "-fx-background-color: #fce8e6; -fx-text-fill: #c5221f; -fx-padding: 4 8; -fx-background-radius: 10; -fx-font-size: 11px; -fx-font-weight: bold;"
                    );
                    setGraphic(badge);
                }
            }
        });
    }

    private void chargerFiltres() {
        comboStatut.setItems(FXCollections.observableArrayList("Tous", "Actif", "Non Inscrit"));
        comboStatut.getSelectionModel().selectFirst();

        try {
            ObservableList<String> classesRecuperees = FXCollections.observableArrayList("Toutes");
            mapIdClasseVersNom.clear();
            for (Classe c : classeDAO.listerToutes()) {
                classesRecuperees.add(c.getNom());
                mapIdClasseVersNom.put(c.getIdClasse(), c.getNom());
            }
            comboClasse.setItems(classesRecuperees);
            comboClasse.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            comboClasse.setItems(FXCollections.observableArrayList("Toutes"));
        }
    }

    private void chargerDonneesBDD() {
        try {
            masterData.clear();
            masterData.addAll(eleveDAO.listerTous());
            filteredData = new FilteredList<>(masterData, p -> true);

            txtRecherche.textProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
            comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());
            comboStatut.valueProperty().addListener((obs, oldVal, newVal) -> appliquerFiltres());

            tableEleves.setItems(filteredData);
            mettreAJourCompteur();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void appliquerFiltres() {
        String recherche = txtRecherche.getText() == null ? "" : txtRecherche.getText().toLowerCase().trim();
        String classeFiltre = comboClasse.getValue();
        String statutFiltre = comboStatut.getValue();

        filteredData.setPredicate(eleve -> {
            if (!recherche.isEmpty()) {
                boolean match = eleve.getNom().toLowerCase().contains(recherche)
                        || eleve.getPrenom().toLowerCase().contains(recherche)
                        || eleve.getMatricule().toLowerCase().contains(recherche);
                if (!match) return false;
            }

            if (classeFiltre != null && !classeFiltre.equals("Toutes")) {
                if (eleve.getInscription() == null) return false;
                String nomCls = mapIdClasseVersNom.get(eleve.getInscription().getIdClasse());
                if (nomCls == null || !nomCls.equalsIgnoreCase(classeFiltre)) return false;
            }

            if (statutFiltre != null && !statutFiltre.equals("Tous")) {
                boolean aUneInscription = (eleve.getInscription() != null);
                if (statutFiltre.equals("Actif") && !aUneInscription) return false;
                if (statutFiltre.equals("Non Inscrit") && aUneInscription) return false;
            }

            return true;
        });

        mettreAJourCompteur();
    }

    private void mettreAJourCompteur() {
        lblCompteur.setText(filteredData.size() + " élève(s) trouvé(s)");
    }

    private void ouvrirFicheDetaillee(Eleve eleve) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/digitalprimaryschool/digitalprimaryschool/view/FicheEleve.fxml"));

            if (loader.getLocation() == null) {
                loader.setLocation(HelloApplication.class.getResource("view/FicheEleve.fxml"));
            }

            Parent ficheView = loader.load();

            FicheEleveController controller = loader.getController();
            controller.setMapClasses(mapIdClasseVersNom);
            controller.setEleveFormulaire(eleve, () -> {
                rootStackPane.getChildren().remove(ficheView);
                chargerDonneesBDD();
            });

            rootStackPane.getChildren().add(ficheView);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de charger le fichier FXML de la fiche détaillée. Vérifiez son emplacement.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}