package com.digitalprimaryschool.digitalprimaryschool;

import com.digitalprimaryschool.digitalprimaryschool.model.Utilisateur;

/**
 * Gestionnaire de session globale (Pattern Singleton)
 * Permet de conserver l'utilisateur connecté durant toute la durée d'exécution du logiciel.
 */
public class Session {

    private static Utilisateur utilisateurConnecte;

    // Constructeur privé pour empêcher l'instanciation de cette classe de stockage
    private Session() {}

    /**
     * Initialise la session avec l'utilisateur qui vient de s'authentifier réussie.
     */
    public static void ouvrir(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     * Permet à l'AccessManager ou aux contrôleurs de vérifier les rôles ou l'idEcole.
     */
    public static Utilisateur get() {
        return utilisateurConnecte;
    }

    /**
     * Ferme la session (utile en cas de déconnexion pour revenir à l'écran de login).
     */
    public static void fermer() {
        utilisateurConnecte = null;
    }

    /**
     * Vérifie de manière rapide si une session est active.
     */
    public static boolean estActive() {
        return utilisateurConnecte != null;
    }
}