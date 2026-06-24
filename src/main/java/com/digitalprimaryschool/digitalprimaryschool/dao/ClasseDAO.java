package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.NiveauClasse;
import com.digitalprimaryschool.digitalprimaryschool.model.SectionClass;
import com.digitalprimaryschool.digitalprimaryschool.model.CategoryClasse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO {

    // ================================================================
    // AJOUTER une classe
    // ================================================================
    public void ajouter(Classe classe) throws SQLException {
        String sql = """
                INSERT INTO Classe (idClasse, nom, niveau, capaciteMax, section)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classe.getIdClasse());
            stmt.setString(2, classe.getNom());

            // MODIFICATION 1: Utiliser name() pour l'enum NiveauClasse
            stmt.setString(3, classe.getNiveau() != null
                    ? classe.getNiveau().name() : null);

            stmt.setInt(4, classe.getCapaciteMax());

            // MODIFICATION 2: Utiliser name() pour l'enum SectionClass
            stmt.setString(5, classe.getSection() != null
                    ? classe.getSection().name() : null);

            stmt.executeUpdate();
            System.out.println("Classe ajoutée : " + classe.getNom());
        }
    }

    // ================================================================
    // MODIFIER une classe
    // ================================================================
    public void modifier(Classe classe) throws SQLException {
        String sql = """
                UPDATE Classe SET
                    nom = ?,
                    niveau = ?,
                    capaciteMax = ?,
                    section = ?
                WHERE idClasse = ?
                """;

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classe.getNom());

            // MODIFICATION 3: Utiliser name() pour l'enum NiveauClasse
            stmt.setString(2, classe.getNiveau() != null
                    ? classe.getNiveau().name() : null);

            stmt.setInt(3, classe.getCapaciteMax());

            // MODIFICATION 4: Utiliser name() pour l'enum SectionClass
            stmt.setString(4, classe.getSection() != null
                    ? classe.getSection().name() : null);

            stmt.setString(5, classe.getIdClasse());

            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucune classe trouvée avec l'id : " + classe.getIdClasse());
            }
        }
    }

    // ================================================================
    // SUPPRIMER une classe
    // ================================================================
    public void supprimer(String idClasse) throws SQLException {
        String sql = "DELETE FROM Classe WHERE idClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);
            int lignes = stmt.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Aucune classe trouvée avec l'id : " + idClasse);
            }
            System.out.println("Classe supprimée : " + idClasse);
        }
    }

    // ================================================================
    // TROUVER une classe par ID
    // ================================================================
    public Classe trouverParId(String idClasse) throws SQLException {
        String sql = "SELECT * FROM Classe WHERE idClasse = ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idClasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construireClasse(rs);
                }
            }
        }
        return null;
    }

    // ================================================================
    // LISTER toutes les classes
    // ================================================================
    public List<Classe> listerToutes() throws SQLException {
        String sql = "SELECT * FROM Classe ORDER BY niveau, nom";
        List<Classe> classes = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(construireClasse(rs));
            }
        }
        return classes;
    }

    // ================================================================
    // LISTER les classes par niveau
    // ================================================================
    public List<Classe> listerParNiveau(NiveauClasse niveau) throws SQLException {
        String sql = "SELECT * FROM Classe WHERE niveau = ? ORDER BY nom";
        List<Classe> classes = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, niveau.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(construireClasse(rs));
                }
            }
        }
        return classes;
    }

    // ================================================================
    // LISTER les classes par catégorie (Maternelle/Primaire)
    // ================================================================
    public List<Classe> listerParCategorie(CategoryClasse categorie) throws SQLException {
        String sql = """
                SELECT * FROM Classe 
                WHERE niveau IN (?, ?, ?) 
                   OR niveau IN (?, ?, ?, ?, ?, ?)
                ORDER BY niveau, nom
                """;

        List<Classe> classes = new ArrayList<>();
        List<String> niveaux = new ArrayList<>();

        if (categorie == CategoryClasse.MATERNELLE) {
            niveaux.add(NiveauClasse.PETITE_SECTION.name());
            niveaux.add(NiveauClasse.MOYENNE_SECTION.name());
            niveaux.add(NiveauClasse.GRANDE_SECTION.name());
        } else if (categorie == CategoryClasse.PRIMAIRE) {
            niveaux.add(NiveauClasse.SIL.name());
            niveaux.add(NiveauClasse.CP.name());
            niveaux.add(NiveauClasse.CE1.name());
            niveaux.add(NiveauClasse.CE2.name());
            niveaux.add(NiveauClasse.CM1.name());
            niveaux.add(NiveauClasse.CM2.name());
        }

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < niveaux.size(); i++) {
                stmt.setString(i + 1, niveaux.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(construireClasse(rs));
                }
            }
        }
        return classes;
    }

    // ================================================================
    // COMPTER le nombre total de classes (pour le tableau de bord)
    // ================================================================
    public int compterToutes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // ================================================================
    // COMPTER les classes par catégorie
    // ================================================================
    public int compterParCategorie(CategoryClasse categorie) throws SQLException {
        List<String> niveaux = new ArrayList<>();

        if (categorie == CategoryClasse.MATERNELLE) {
            niveaux.add(NiveauClasse.PETITE_SECTION.name());
            niveaux.add(NiveauClasse.MOYENNE_SECTION.name());
            niveaux.add(NiveauClasse.GRANDE_SECTION.name());
        } else if (categorie == CategoryClasse.PRIMAIRE) {
            niveaux.add(NiveauClasse.SIL.name());
            niveaux.add(NiveauClasse.CP.name());
            niveaux.add(NiveauClasse.CE1.name());
            niveaux.add(NiveauClasse.CE2.name());
            niveaux.add(NiveauClasse.CM1.name());
            niveaux.add(NiveauClasse.CM2.name());
        }

        String sql = "SELECT COUNT(*) FROM Classe WHERE niveau IN (" +
                String.join(",", niveaux.stream().map(n -> "?").toArray(String[]::new)) +
                ")";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < niveaux.size(); i++) {
                stmt.setString(i + 1, niveaux.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ================================================================
    // VÉRIFIER si une classe existe déjà par nom
    // ================================================================
    public boolean nomExiste(String nom) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe WHERE LOWER(nom) = LOWER(?)";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // ================================================================
    // VÉRIFIER si une classe existe déjà par nom (sauf pour une classe donnée)
    // ================================================================
    public boolean nomExiste(String nom, String idClasseExclu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classe WHERE LOWER(nom) = LOWER(?) AND idClasse != ?";

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, idClasseExclu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // ================================================================
    // MÉTHODE PRIVÉE : construire un objet Classe depuis un ResultSet
    // ================================================================
    private Classe construireClasse(ResultSet rs) throws SQLException {
        Classe classe = new Classe();

        classe.setIdClasse(rs.getString("idClasse"));
        classe.setNom(rs.getString("nom"));
        classe.setCapaciteMax(rs.getInt("capaciteMax"));

        // MODIFICATION 5: Conversion du niveau (TEXT SQLite → enum NiveauClasse)
        String niveauStr = rs.getString("niveau");
        if (niveauStr != null && !niveauStr.isEmpty()) {
            try {
                classe.setNiveau(NiveauClasse.valueOf(niveauStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Niveau inconnu : " + niveauStr);
            }
        }

        // MODIFICATION 6: Conversion de la section (TEXT SQLite → enum SectionClass)
        String sectionStr = rs.getString("section");
        if (sectionStr != null && !sectionStr.isEmpty()) {
            try {
                classe.setSection(SectionClass.valueOf(sectionStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Section inconnue : " + sectionStr);
            }
        }

        // MODIFICATION 7: Déterminer la catégorie en fonction du niveau
        if (classe.getNiveau() != null) {
            switch (classe.getNiveau()) {
                case PETITE_SECTION:
                case MOYENNE_SECTION:
                case GRANDE_SECTION:
                case NURSERY_1:
                case NURSERY_2:
                case NURSERY_3:
                    classe.setCategorieClasse(CategoryClasse.MATERNELLE);
                    break;
                case SIL:
                case CP:
                case CE1:
                case CE2:
                case CM1:
                case CM2:
                case CLASS_1:
                case CLASS_2:
                case CLASS_3:
                case CLASS_4:
                case CLASS_5:
                case CLASS_6:
                    classe.setCategorieClasse(CategoryClasse.PRIMAIRE);
                    break;
                default:
                    classe.setCategorieClasse(CategoryClasse.MATERNELLE);
                    break;
            }
        }

        return classe;
    }
    // ================================================================
// LISTER toutes les classes avec leur effectif réel (Jointure Inscription)
// ================================================================
    public List<Classe> listerToutesAvecEffectifs() throws SQLException {
        // Compte le nombre d'élèves liés à chaque classe dans la table Inscription
        String sql = """
            SELECT c.*, 
                   (SELECT COUNT(*) FROM Inscription i WHERE i.idClasse = c.idClasse) AS effectifReel
            FROM Classe c
            ORDER BY c.nom
            """;
        List<Classe> classes = new ArrayList<>();

        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Classe classe = construireClasse(rs);

                // Si ta classe modèle 'Classe' possède une propriété de stockage temporaire pour l'effectif :
                // classe.setNombreEleves(rs.getInt("effectifReel"));
                // Sinon, nous stockerons ou utiliserons directement cette valeur à l'affichage via une propriété utilisateur.
                classe.setCapaciteMax(rs.getInt("effectifReel")); // Utilisons transitoirement capaciteMax ou une Map si l'attribut n'existe pas.

                classes.add(classe);
            }
        }
        return classes;
    }
}