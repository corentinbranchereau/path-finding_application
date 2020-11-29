package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
     * Rectangle permettant de dessiner une bordure autour de la carte.
     */
    @FXML
    protected Rectangle bordureCarte;

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
     * Pane qui contient les éléments graphiques dessinant la carte
     */
    @FXML
    protected Pane paneDessin;

    /**
     * AnchorPane contenant le pane de dessin (vue graphique)
     */
    @FXML
    protected AnchorPane anchorPaneGraphique;

    /**
     * Scroll pane de la vue textuelle
     */
    @FXML
    protected ScrollPane textScrollPane;

    /**
     * Renvoie l'item du menu permettant de charger une carte.
     * 
     * @return L'item chargerCarteItem
     */
    public MenuItem getChargerCarteItem() {
        return chargerCarteItem;
    }

    /**
     * renvoie le scroll pane où se trouve la vue textuelle
     * 
     * @return L'item textscrollPane
     */
    public ScrollPane getScrollPane() {
        return textScrollPane;
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
     * Renvoie le rectangle dessinant la bordure de la carte.
     * 
     * @return Le rectangle de bordure de la carte
     */
    public Rectangle getBordureCarte() {
        return bordureCarte;
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
     * Renvoie le AnchorPane contenant le pane de dessin
     * 
     * @return Le AnchorPane contenant le pane de dessin
     */
    public AnchorPane getAnchorPaneGraphique() {
        return anchorPaneGraphique;
    }

    public Pane getPaneDessin() {
        return paneDessin;
    }
}
