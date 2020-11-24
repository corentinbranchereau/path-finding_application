package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;

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
     * Canvas où sont dessinées la carte et les requêtes. Correspond à la vue
     * graphique de l'application.
     */
    @FXML
    protected Canvas canvas;

    /**
     * Rectangle permettant de délimiter le canvas de dessin.
     */
    @FXML
    protected Rectangle bordureCanvas;

    /**
     * Zone de texte où sont affichées les informations sur le planning. Correspond
     * à la vue textuelle de l'application.
     */
    @FXML
    protected TextFlow zoneTexte;

    /**
     * Bouton permettant de lancer le calcul du planning.
     */
    @FXML
    protected Button boutonLancer;

    /**
     * AnchorPane contenant le canvas de dessin (vue graphique)
     */
    @FXML
    protected AnchorPane anchorPaneGraphique;

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

    /**
     * Renvoie le canvas de l'application (vue graphique)
     * 
     * @return Le canvas de l'application
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Renvoie le rectangle délimitant le canvas.
     * 
     * @return Le rectangle délimitant le canvas de l'application
     */
    public Rectangle getBordureCanvas() {
        return bordureCanvas;
    }

    /**
     * Renvoie la zone de texte (vue textuelle) de l'application.
     * 
     * @return La zone de texte de l'application.
     */
    public TextFlow getZoneTexte() {
        return zoneTexte;
    }

    /**
     * Renvoie le bouton lançant le calcul du planning.
     * 
     * @return Le bouton lançant le calcul du planning.
     */
    public Button getBoutonLancer() {
        return boutonLancer;
    }

    /**
     * Renvoie le AnchorPane contenant le canvas de dessin
     * 
     * @return Le AnchorPane contenant le canvas de dessin
     */
    public AnchorPane getAnchorPaneGraphique() {
        return anchorPaneGraphique;
    }
}
