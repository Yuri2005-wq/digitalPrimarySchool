package com.digitalprimaryschool.digitalprimaryschool.controller;

import com.digitalprimaryschool.digitalprimaryschool.HelloApplication;
import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
import com.digitalprimaryschool.digitalprimaryschool.dao.ClasseDAO;
import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;

import javafx.beans.property.ReadOnlyObjectWrapper;
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

    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, Object> colPhoto;
    @FXML private TableColumn<Eleve, Object> colNom;
    @FXML private TableColumn<Eleve, Object> colPrenom;
    @FXML private TableColumn<Eleve, Object> colDateNaissance;
    @FXML private TableColumn<Eleve, Object> colClasse;
    @FXML private TableColumn<Eleve, Object> colStatut;

    private final EleveDAO eleveDAO = new EleveDAO();
    private final ClasseDAO classeDAO = new ClasseDAO();
    private final ObservableList<Eleve> masterData = FXCollections.observableArrayList();
    private FilteredList<Eleve> filteredData;

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
        colPhoto.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colNom.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colPrenom.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colDateNaissance.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colClasse.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colStatut.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        colPhoto.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Eleve e = (Eleve) item;
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
                                    chargerAvatarDefaut(e);
                                }
                            } else {
                                imageView.setImage(new Image(photoPath));
                            }
                        } catch (Exception ex) {
                            chargerAvatarDefaut(e);
                        }
                    } else {
                        chargerAvatarDefaut(e);
                    }
                    setGraphic(imageView);
                }
            }
            private void chargerAvatarDefaut(Eleve eleve) {
                try {
                    if(eleve != null && eleve.getSexe() != null && "Masculin".equalsIgnoreCase(eleve.getSexe().getLibelle())){
                        imageView.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/avatar.png")));
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream("/com/digitalprimaryschool/digitalprimaryschool/images/img.png")));
                    }
                } catch (Exception e) {
                    imageView.setImage(null);
                }
            }
        });

        colNom.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(((Eleve) item).getMatricule());
                }
            }
        });

        colPrenom.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(((Eleve) item).getFullName());
                }
            }
        });

        colDateNaissance.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || ((Eleve) item).getDateNaissance() == null) {
                    setText(null);
                } else {
                    try {
                        setText(formatter.format(((Eleve) item).getDateNaissance()));
                    } catch (Exception ex) {
                        setText("Date Invalide");
                    }
                }
            }
        });

        colClasse.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Eleve e = (Eleve) item;
                    if (e.getInscription() != null) {
                        String nomClasse = mapIdClasseVersNom.get(e.getInscription().getIdClasse());
                        setText(nomClasse != null ? nomClasse : "Inconnue");
                    } else {
                        setText("Sans Classe");
                    }
                }
            }
        });

        colStatut.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Eleve e = (Eleve) item;
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
                boolean match = (eleve.getNom() != null && eleve.getNom().toLowerCase().contains(recherche))
                        || (eleve.getPrenom() != null && eleve.getPrenom().toLowerCase().contains(recherche))
                        || (eleve.getMatricule() != null && eleve.getMatricule().toLowerCase().contains(recherche));
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de charger le fichier FXML de la fiche détaillée.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}