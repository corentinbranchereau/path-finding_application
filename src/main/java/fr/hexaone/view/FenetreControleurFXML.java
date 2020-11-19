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
    protected MenuItem chargerMapItem;

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

}
