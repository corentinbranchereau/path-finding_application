package fr.hexaone.controller;

import fr.hexaone.view.Fenetre;
import javafx.stage.Stage;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    /**
     * Gère l'affichage de l'application (Vue du MVC)
     */
    protected Fenetre fenetre;

    /**
     * Constructeur de Controleur. Instancie la fenêtre de l'application et
     * l'affiche à l'écran.
     * 
     * @param stage Conteneur principal des éléments graphiques de la fenêtre
     */
    public Controleur(Stage stage) {
        this.fenetre = new Fenetre(stage, this);
        this.fenetre.dessinerFenetre();
    }

    /**
     * Méthode gérant le clic sur l'item "Charger une carte" du menu
     */
    public void handleClicChargerCarte() {
        System.out.println("Charger carte");
    }

    /**
     * Méthode gérant le clic sur l'item "Charger des requêtes" du menu
     */
    public void handleClicChargerRequetes() {
        System.out.println("Charger requêtes");
    }

    /**
     * Méthode gérant le clic sur l'item "Quitter" du menu
     */
    public void handleClicQuitter() {
        System.out.println("Quitter");
    }

    /**
     * Renvoie la fenêtre de l'application.
     * 
     * @return La fenêtre de l'application
     */
    public Fenetre getFenetre() {
        return fenetre;
    }
}
