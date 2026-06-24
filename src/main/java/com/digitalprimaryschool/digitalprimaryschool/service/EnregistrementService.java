//package com.digitalprimaryschool.digitalprimaryschool.service;
//
//import com.digitalprimaryschool.digitalprimaryschool.dao.AnneeScolaireDAO;
//import com.digitalprimaryschool.digitalprimaryschool.dao.EleveDAO;
//import com.digitalprimaryschool.digitalprimaryschool.dao.ParentDAO;
//import com.digitalprimaryschool.digitalprimaryschool.dao.TarifScolaireDAO;
//import com.digitalprimaryschool.digitalprimaryschool.model.AnneeScolaire;
//import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
//import com.digitalprimaryschool.digitalprimaryschool.model.Parent;
//import com.digitalprimaryschool.digitalprimaryschool.model.TarisScolaire; // Utilisation du nom exact de votre modèle
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class EnregistrementService {
//
//    private final ParentDAO parentDAO;
//    private final EleveDAO eleveDAO;
//    private final AnneeScolaireDAO anneeScolaireDAO;
//    private final TarifScolaireDAO tarifScolaireDAO;
//
//    public EnregistrementService() {
//        this.parentDAO = new ParentDAO();
//        this.eleveDAO = new EleveDAO();
//        this.anneeScolaireDAO = new AnneeScolaireDAO();
//        this.tarifScolaireDAO = new TarifScolaireDAO();
//    }
//
//    // ================================================================
//    // VÉRIFICATIONS EXISTANTES
//    // ================================================================
//
//    public boolean parentExiste(String idParent) throws SQLException {
//        return parentDAO.findById(idParent) != null;
//    }
//
//    public boolean parentExisteParContact(int contact) throws SQLException {
//        return parentDAO.findByContact(contact) != null;
//    }
//
//    public boolean eleveExiste(String matricule) throws SQLException {
//        return eleveDAO.trouverParMatricule(matricule) != null;
//    }
//
//    public boolean eleveExiste(String nom, String prenom) throws SQLException {
//        List<Eleve> eleves = eleveDAO.listerTous();
//        for (Eleve e : eleves) {
//            if (e.getNom().equalsIgnoreCase(nom) && e.getPrenom().equalsIgnoreCase(prenom)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean parentADejaCetEleve(String idParent, String nom, String prenom) throws SQLException {
//        List<Eleve> eleves = eleveDAO.listerTous();
//        for (Eleve e : eleves) {
//            if (idParent.equals(e.getIdParent()) &&
//                    e.getNom().equalsIgnoreCase(nom) &&
//                    e.getPrenom().equalsIgnoreCase(prenom)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // ================================================================
//    // PARENT / ELEVE CORE CRUD (Inchangé)
//    // ================================================================
//
//    public Resultat enregistrerParent(Parent parent) {
//        try {
//            if (parent == null) return new Resultat(false, "Le parent ne peut pas être null.");
//            if (parentExiste(parent.getIdParent())) return new Resultat(false, "Un parent avec cet ID existe déjà.");
//            if (parentExisteParContact(parent.getContactParent())) return new Resultat(false, "Un parent avec ce numéro existe déjà.");
//
//            parentDAO.insert(parent);
//            return new Resultat(true, "Parent enregistré avec succès. ID: " + parent.getIdParent());
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur base de données : " + e.getMessage());
//        }
//    }
//
//    public Resultat enregistrerEleve(Parent parent, Eleve eleve) {
//        try {
//            if (parent == null || eleve == null) return new Resultat(false, "Les paramètres ne peuvent pas être null.");
//            if (!parentExiste(parent.getIdParent())) return new Resultat(false, "Le parent n'existe pas.");
//            if (eleveExiste(eleve.getMatricule())) {
//                eleve.regenererMatricule();
//                if (eleveExiste(eleve.getMatricule())) return new Resultat(false, "Impossible de générer un matricule unique.");
//            }
//            if (parentADejaCetEleve(parent.getIdParent(), eleve.getNom(), eleve.getPrenom())) {
//                return new Resultat(false, "Cet élève est déjà enregistré avec ce parent.");
//            }
//
//            eleveDAO.ajouter(eleve, parent.getIdParent());
//            return new Resultat(true, "Élève enregistré avec succès. Matricule: " + eleve.getMatricule());
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur base de données : " + e.getMessage());
//        }
//    }
//
//    public Resultat enregistrerParentEtEleve(Parent parent, Eleve eleve) {
//        try {
//            if (parent == null || eleve == null) return new Resultat(false, "Les paramètres ne peuvent pas être null.");
//            if (parentExiste(parent.getIdParent())) return new Resultat(false, "Un parent avec cet ID existe déjà.");
//            if (parentExisteParContact(parent.getContactParent())) return new Resultat(false, "Un parent avec ce numéro existe déjà.");
//            if (eleveExiste(eleve.getMatricule())) {
//                eleve.regenererMatricule();
//                if (eleveExiste(eleve.getMatricule())) return new Resultat(false, "Impossible de générer un matricule unique.");
//            }
//            if (parentADejaCetEleve(parent.getIdParent(), eleve.getNom(), eleve.getPrenom())) {
//                return new Resultat(false, "Cet élève est déjà enregistré avec ce parent.");
//            }
//
//            parentDAO.insert(parent);
//            eleveDAO.ajouter(eleve, parent.getIdParent());
//            return new Resultat(true, "Parent et élève enregistrés avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur base de données : " + e.getMessage());
//        }
//    }
//
//    // ================================================================
//    // SERVICE : MANIPULATION DE L'ANNEE SCOLAIRE
//    // ================================================================
//
//    public Resultat enregistrerAnneeScolaire(AnneeScolaire annee) {
//        try {
//            if (annee == null) return new Resultat(false, "L'année ne peut pas être nulle.");
//
//            // Gestion de la règle d'unicité demandée
//            if (annee.isEstActive()) {
//                anneeScolaireDAO.desactiverToutes();
//            }
//
//            anneeScolaireDAO.ajouter(annee);
//            return new Resultat(true, "Année scolaire ajoutée avec succès.");
//        } catch (Exception e) {
//            return new Resultat(false, "Erreur lors de la création de l'année : " + e.getMessage());
//        }
//    }
//
//    public Resultat modifierAnneeScolaire(AnneeScolaire annee) {
//        try {
//            if (annee == null) return new Resultat(false, "L'année ne peut pas être nulle.");
//
//            if (annee.isEstActive()) {
//                anneeScolaireDAO.desactiverToutes();
//            }
//
//            anneeScolaireDAO.modifier(annee);
//            return new Resultat(true, "Année scolaire modifiée avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur lors de la mise à jour de l'année : " + e.getMessage());
//        }
//    }
//
//    public Resultat supprimerAnneeScolaire(String idAnnee) {
//        try {
//            anneeScolaireDAO.supprimer(idAnnee);
//            return new Resultat(true, "Année scolaire supprimée avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Impossible de supprimer : cette année possède des relations actives.");
//        }
//    }
//
//    public List<AnneeScolaire> getTousLesAnneesScolaires() throws SQLException {
//        return anneeScolaireDAO.listerToutes();
//    }
//
//    public AnneeScolaire getAnneeScolaireActive() throws SQLException {
//        return anneeScolaireDAO.trouverActive();
//    }
//
//    // ================================================================
//    // SERVICE : MANIPULATION DES TARIFS SCOLAIRES
//    // ================================================================
//
//    /**
//     * Enregistre un nouveau tarif scolaire en vérifiant si la classe n'est pas déjà configurée
//     */
//    public Resultat enregistrerTarifScolaire(TarisScolaire tarif, String idClasse) {
//        try {
//            if (tarif == null || idClasse == null || idClasse.trim().isEmpty()) {
//                return new Resultat(false, "Le tarif et l'identifiant de la classe sont requis.");
//            }
//
//            // Vérifier si la classe a déjà une configuration budgétaire active
//            if (tarifScolaireDAO.tarifExistePourClasse(idClasse)) {
//                return new Resultat(false, "Un tarif a déjà été configuré pour cette classe.");
//            }
//
//            // Respect strict de votre signature DAO : ajouter(TarisScolaire, String)
//            tarifScolaireDAO.ajouter(tarif, idClasse);
//            return new Resultat(true, "Tarif scolaire enregistré avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur base de données (Ajout Tarif) : " + e.getMessage());
//        }
//    }
//
//    public Resultat modifierTarifScolaire(TarisScolaire tarif) {
//        try {
//            if (tarif == null) return new Resultat(false, "Le tarif ne peut pas être null.");
//
//            tarifScolaireDAO.modifier(tarif);
//            return new Resultat(true, "Tarif mis à jour avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Erreur base de données (Modification Tarif) : " + e.getMessage());
//        }
//    }
//
//    public Resultat supprimerTarifScolaire(String idTarif) {
//        try {
//            tarifScolaireDAO.supprimer(idTarif);
//            return new Resultat(true, "Tarif supprimé avec succès.");
//        } catch (SQLException e) {
//            return new Resultat(false, "Impossible de supprimer ce barème tarifaire.");
//        }
//    }
//
//    public List<TarisScolaire> getTousLesTarifsScolaires() throws SQLException {
//        return tarifScolaireDAO.listerTous();
//    }
//
//    public TarisScolaire getTarifParClasse(String idClasse) throws SQLException {
//        return tarifScolaireDAO.trouverParClasse(idClasse);
//    }
//
//    // ================================================================
//    // AUTRES MÉTHODES DE RECHERCHE
//    // ================================================================
//
//    public List<Parent> getTousLesParents() throws SQLException { return parentDAO.findAll(); }
//    public Parent getParentParId(String id) throws SQLException { return parentDAO.findById(id); }
//    public Parent getParentParContact(int contact) throws SQLException { return parentDAO.findByContact(contact); }
//    public List<Eleve> getTousLesEleves() throws SQLException { return eleveDAO.listerTous(); }
//    public Eleve getEleveParMatricule(String matricule) throws SQLException { return eleveDAO.trouverParMatricule(matricule); }
//    public int compterParents() throws SQLException { return parentDAO.findAll().size(); }
//    public int compterEleves() throws SQLException { return eleveDAO.compterTous(); }
//    public boolean supprimerParent(String idParent) throws SQLException { return parentDAO.delete(idParent); }
//    public boolean supprimerEleve(String matricule) throws SQLException {
//        try { eleveDAO.supprimer(matricule); return true; } catch (SQLException e) { return false; }
//    }
//    public List<Eleve> getElevesParParent(String idParent) throws SQLException {
//        return eleveDAO.listerTous().stream().filter(e -> idParent.equals(e.getIdParent())).toList();
//    }
//
//    // ================================================================
//    // ENCAPSULATION DU RÉSULTAT
//    // ================================================================
//    public static class Resultat {
//        private final boolean success;
//        private final String message;
//
//        public Resultat(boolean success, String message) {
//            this.success = success;
//            this.message = message;
//        }
//
//        public boolean isSuccess() { return success; }
//        public String getMessage() { return message; }
//    }
//}
package com.digitalprimaryschool.digitalprimaryschool.service;

import com.digitalprimaryschool.digitalprimaryschool.dao.*;
import com.digitalprimaryschool.digitalprimaryschool.model.*;

import java.sql.SQLException;
import java.util.List;

public class EnregistrementService {

    private final ParentDAO parentDAO;
    private final EleveDAO eleveDAO;
    private final AnneeScolaireDAO anneeScolaireDAO;
    private final ClasseDAO classeDAO;
    private final TarifScolaireDAO tarifScolaireDAO;

    public EnregistrementService() {
        this.parentDAO = new ParentDAO();
        this.eleveDAO = new EleveDAO();
        this.anneeScolaireDAO = new AnneeScolaireDAO();
        this.tarifScolaireDAO = new TarifScolaireDAO();
        this.classeDAO = new ClasseDAO();
    }

    // ================================================================
    // VÉRIFICATIONS EXISTANTES
    // ================================================================

    public boolean parentExiste(String idParent) throws SQLException {
        return parentDAO.findById(idParent) != null;
    }

    public boolean parentExisteParContact(int contact) throws SQLException {
        return parentDAO.findByContact(contact) != null;
    }

    public boolean eleveExiste(String matricule) throws SQLException {
        return eleveDAO.trouverParMatricule(matricule) != null;
    }

    public boolean eleveExiste(String nom, String prenom) throws SQLException {
        List<Eleve> eleves = eleveDAO.listerTous();
        for (Eleve e : eleves) {
            if (e.getNom().equalsIgnoreCase(nom) && e.getPrenom().equalsIgnoreCase(prenom)) {
                return true;
            }
        }
        return false;
    }

    public boolean parentADejaCetEleve(String idParent, String nom, String prenom) throws SQLException {
        List<Eleve> eleves = eleveDAO.listerTous();
        for (Eleve e : eleves) {
            if (idParent.equals(e.getIdParent()) &&
                    e.getNom().equalsIgnoreCase(nom) &&
                    e.getPrenom().equalsIgnoreCase(prenom)) {
                return true;
            }
        }
        return false;
    }

    // ================================================================
    // PARENT / ELEVE CORE CRUD (Inchangé)
    // ================================================================

    public Resultat enregistrerParent(Parent parent) {
        try {
            if (parent == null) return new Resultat(false, "Le parent ne peut pas être null.");
            if (parentExiste(parent.getIdParent())) return new Resultat(false, "Un parent avec cet ID existe déjà.");
            if (parentExisteParContact(parent.getContactParent())) return new Resultat(false, "Un parent avec ce numéro existe déjà.");

            parentDAO.insert(parent);
            return new Resultat(true, "Parent enregistré avec succès. ID: " + parent.getIdParent());
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données : " + e.getMessage());
        }
    }

    public Resultat enregistrerEleve(Parent parent, Eleve eleve) {
        try {
            if (parent == null || eleve == null) return new Resultat(false, "Les paramètres ne peuvent pas être null.");
            if (!parentExiste(parent.getIdParent())) return new Resultat(false, "Le parent n'existe pas.");
            if (eleveExiste(eleve.getMatricule())) {
                eleve.regenererMatricule();
                if (eleveExiste(eleve.getMatricule())) return new Resultat(false, "Impossible de générer un matricule unique.");
            }
            if (parentADejaCetEleve(parent.getIdParent(), eleve.getNom(), eleve.getPrenom())) {
                return new Resultat(false, "Cet élève est déjà enregistré avec ce parent.");
            }

            eleveDAO.ajouter(eleve, parent.getIdParent());
            return new Resultat(true, "Élève enregistré avec succès. Matricule: " + eleve.getMatricule());
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données : " + e.getMessage());
        }
    }

    public Resultat enregistrerParentEtEleve(Parent parent, Eleve eleve) {
        try {
            if (parent == null || eleve == null) return new Resultat(false, "Les paramètres ne peuvent pas être null.");
            if (parentExiste(parent.getIdParent())) return new Resultat(false, "Un parent avec cet ID existe déjà.");
            if (parentExisteParContact(parent.getContactParent())) return new Resultat(false, "Un parent avec ce numéro existe déjà.");
            if (eleveExiste(eleve.getMatricule())) {
                eleve.regenererMatricule();
                if (eleveExiste(eleve.getMatricule())) return new Resultat(false, "Impossible de générer un matricule unique.");
            }
            if (parentADejaCetEleve(parent.getIdParent(), eleve.getNom(), eleve.getPrenom())) {
                return new Resultat(false, "Cet élève est déjà enregistré avec ce parent.");
            }

            parentDAO.insert(parent);
            eleveDAO.ajouter(eleve, parent.getIdParent());
            return new Resultat(true, "Parent et élève enregistrés avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données : " + e.getMessage());
        }
    }

    // ================================================================
    // SERVICE : MANIPULATION DE L'ANNEE SCOLAIRE
    // ================================================================

    public Resultat enregistrerAnneeScolaire(AnneeScolaire annee) {
        try {
            if (annee == null) return new Resultat(false, "L'année ne peut pas être nulle.");

            if (annee.isEstActive()) {
                anneeScolaireDAO.desactiverToutes();
            }

            anneeScolaireDAO.ajouter(annee);
            return new Resultat(true, "Année scolaire ajoutée avec succès.");
        } catch (Exception e) {
            return new Resultat(false, "Erreur lors de la création de l'année : " + e.getMessage());
        }
    }

    public Resultat modifierAnneeScolaire(AnneeScolaire annee) {
        try {
            if (annee == null) return new Resultat(false, "L'année ne peut pas être nulle.");

            if (annee.isEstActive()) {
                anneeScolaireDAO.desactiverToutes();
            }

            anneeScolaireDAO.modifier(annee);
            return new Resultat(true, "Année scolaire modifiée avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur lors de la mise à jour de l'année : " + e.getMessage());
        }
    }

    public Resultat supprimerAnneeScolaire(String idAnnee) {
        try {
            anneeScolaireDAO.supprimer(idAnnee);
            return new Resultat(true, "Année scolaire supprimée avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Impossible de supprimer : cette année possède des relations actives.");
        }
    }

    public List<AnneeScolaire> getTousLesAnneesScolaires() throws SQLException {
        return anneeScolaireDAO.listerToutes();
    }

    public AnneeScolaire getAnneeScolaireActive() throws SQLException {
        return anneeScolaireDAO.trouverActive();
    }

    // ================================================================
    // SERVICE : OPERATIONS DES TARIFS SCOLAIRES
    // ================================================================

    // ================================================================
// SERVICE : OPERATIONS DES TARIFS SCOLAIRES
// ================================================================

    public Resultat enregistrerTarifScolaire(TarisScolaire tarif) {
        try {
            if (tarif == null) {
                return new Resultat(false, "Le tarif est requis.");
            }
            if (tarifScolaireDAO.tarifExistePourClasse(tarif.getNiveauClasse())) {
                return new Resultat(false, "Un tarif a déjà été configuré pour cette classe.");
            }

            tarifScolaireDAO.ajouter(tarif);
            return new Resultat(true, "Tarif scolaire enregistré avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données (Ajout Tarif) : " + e.getMessage());
        }
    }

    public Resultat modifierTarifScolaire(TarisScolaire tarif) {
        try {
            if (tarif == null) return new Resultat(false, "Le tarif ne peut pas être null.");

            tarifScolaireDAO.modifier(tarif);
            return new Resultat(true, "Tarif mis à jour avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données (Modification Tarif) : " + e.getMessage());
        }
    }

    public Resultat supprimerTarifScolaire(String idTarif) {
        try {
            tarifScolaireDAO.supprimer(idTarif);
            return new Resultat(true, "Tarif supprimé avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Impossible de supprimer ce barème tarifaire.");
        }
    }

    public List<TarisScolaire> getTousLesTarifsScolaires() throws SQLException {
        return tarifScolaireDAO.listerTous();
    }

    public TarisScolaire getTarifParClasse(String niveauClasse) throws SQLException {
        return tarifScolaireDAO.trouverParClasse(niveauClasse);
    }
    // ================================================================
    // AUTRES MÉTHODES DE RECHERCHE
    // ================================================================

    public List<Parent> getTousLesParents() throws SQLException { return parentDAO.findAll(); }
    public Parent getParentParId(String id) throws SQLException { return parentDAO.findById(id); }
    public Parent getParentParContact(int contact) throws SQLException { return parentDAO.findByContact(contact); }
    public List<Eleve> getTousLesEleves() throws SQLException { return eleveDAO.listerTous(); }
    public Eleve getEleveParMatricule(String matricule) throws SQLException { return eleveDAO.trouverParMatricule(matricule); }
    public int compterParents() throws SQLException { return parentDAO.findAll().size(); }
    public int compterEleves() throws SQLException { return eleveDAO.compterTous(); }
    public boolean supprimerParent(String idParent) throws SQLException { return parentDAO.delete(idParent); }
    public boolean supprimerEleve(String matricule) throws SQLException {
        try { eleveDAO.supprimer(matricule); return true; } catch (SQLException e) { return false; }
    }
    public List<Eleve> getElevesParParent(String idParent) throws SQLException {
        return eleveDAO.listerTous().stream().filter(e -> idParent.equals(e.getIdParent())).toList();
    }

    // ================================================================
    // ENCAPSULATION DU RÉSULTAT
    // ================================================================
    public static class Resultat {
        private final boolean success;
        private final String message;

        public Resultat(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public Resultat enregistrerClasse(Classe classe) {
        try {
            if (classe == null) return new Resultat(false, "La classe ne peut pas être nulle.");
            if (classeDAO.nomExiste(classe.getNom())) {
                return new Resultat(false, "Une classe portant le nom '" + classe.getNom() + "' existe déjà.");
            }
            classeDAO.ajouter(classe);
            return new Resultat(true, "Classe enregistrée avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données (Ajout Classe) : " + e.getMessage());
        }
    }

    public Resultat modifierClasse(Classe classe) {
        try {
            if (classe == null) return new Resultat(false, "La classe ne peut pas être nulle.");
            if (classeDAO.nomExiste(classe.getNom(), classe.getIdClasse())) {
                return new Resultat(false, "Le nom '" + classe.getNom() + "' est déjà utilisé par une autre classe.");
            }
            classeDAO.modifier(classe);
            return new Resultat(true, "Classe modifiée avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Erreur base de données (Modification Classe) : " + e.getMessage());
        }
    }

    public Resultat supprimerClasse(String idClasse) {
        try {
            classeDAO.supprimer(idClasse);
            return new Resultat(true, "Classe supprimée avec succès.");
        } catch (SQLException e) {
            return new Resultat(false, "Impossible de supprimer : cette classe contient potentiellement des élèves ou des tarifs liés.");
        }
    }

    public List<Classe> getToutesLesClasses() throws SQLException {
        return classeDAO.listerToutes();
    }

// SERVICE : RECUPERER LE TARIF D'UNE CLASSE

    public TarisScolaire getTarifParClasse(Classe classe) throws SQLException {
        if (classe == null || classe.getNiveau() == null) {
            return null;
        }
        return tarifScolaireDAO.trouverParClasse(classe.getNiveau().name());
    }

    public TarisScolaire getTarifParNiveau(NiveauClasse niveau) throws SQLException {
        if (niveau == null) {
            return null;
        }
        return tarifScolaireDAO.trouverParClasse(niveau.name());
    }
}