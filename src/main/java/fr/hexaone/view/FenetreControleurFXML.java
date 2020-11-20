package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Controleur JavaFX permettant de récupérer les éléments graphiques issus du
 * fichier FXML
 * 
 * @author HexaOne
 * @version 1.0
 */

public class FenetreControleurFXML {

    /**
     * Item du menu permettant de charger une carte
     */
    @FXML
    protected MenuItem chargerCarteItem;

    /**
     * Item du menu permettant de charger des requêtes
     */
    @FXML
    protected MenuItem chargerRequetesItem;

    /**
     * Item du menu permettant de quitter l'application
     */
    @FXML
    protected MenuItem quitterItem;

    /**
     * Renvoie l'item du menu permettant de charger une carte.
     * 
     * @return L'item chargerCarteItem
     */
    public MenuItem getChargerCarteItem() {
        return chargerCarteItem;
    }

    /**
     * Renvoie l'item du menu permettant de charger des requêtes.
     * 
     * @return L'item chargerRequetesItem
     */
    public MenuItem getChargerRequetesItem() {
        return chargerRequetesItem;
    }

    /**
     * Renvoie l'item du menu permettant de quitter l'application.
     * 
     * @return L'item quitterItem
     */
    public MenuItem getQuitterItem() {
        return quitterItem;
    }
}
