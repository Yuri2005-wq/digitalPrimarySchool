package com.digitalprimaryschool.digitalprimaryschool.security;

import com.digitalprimaryschool.digitalprimaryschool.Session;
import com.digitalprimaryschool.digitalprimaryschool.model.Role;
import java.util.*;

public class AccessManager {

    // Table de correspondance contenant les rôles autorisés pour chaque clé d'action
    private static final Map<String, List<Role>> permissionsAction = new HashMap<>();

    static {
        // --- DROITS MULTI-ACTEURS ---

        // Configuration générale (Arretes de création, Logo, Devise de l'école)
        permissionsAction.put("ACTION_CONFIG_ECOLE", Arrays.asList(Role.DIRECTEUR, Role.PROMOTEUR));

        // Académique & Évaluations
        permissionsAction.put("ACTION_SAISIE_NOTES", Arrays.asList(Role.DIRECTEUR, Role.ENSEIGNANT));
        permissionsAction.put("ACTION_CLOTURE_TRIMESTRE", Arrays.asList(Role.DIRECTEUR));
        permissionsAction.put("ACTION_GENERER_BULLETIN", Arrays.asList(Role.DIRECTEUR, Role.ENSEIGNANT));

        // Finance & Gestion des Élèves
        permissionsAction.put("ACTION_INSCRIRE_ELEVE", Arrays.asList(Role.DIRECTEUR, Role.ECONOME));
        permissionsAction.put("ACTION_ENCAISSER_PENSION", Arrays.asList(Role.ECONOME, Role.DIRECTEUR));
        permissionsAction.put("ACTION_CONFIG_TARIFS", Arrays.asList(Role.DIRECTEUR, Role.PROMOTEUR));

        // Droit critique (Suppression définitive)
        permissionsAction.put("ACTION_SUPPRIMER_DONNEES", Arrays.asList(Role.DIRECTEUR));
    }

    /**
     * Vérifie si l'acteur actuellement connecté possède l'autorisation d'exécuter une action ou d'ouvrir un menu.
     * @param codeAction Le libellé de l'action à vérifier (Ex: ACTION_SAISIE_NOTES)
     * @return true si l'accès est légitime, false sinon.
     */
    public static boolean estAutorise(String codeAction) {
        if (Session.get() == null) {
            return false; // Personne n'est authentifié
        }

        Role roleActeur = Session.get().getRole();

        // Sécurité par défaut : Si l'action n'est pas répertoriée, par prudence on bloque
        if (!permissionsAction.containsKey(codeAction)) {
            return false;
        }

        return permissionsAction.get(codeAction).contains(roleActeur);
    }
}