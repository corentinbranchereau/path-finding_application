package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
     * Bouton permettant de lancer le processus d'ajout d'une nouvelle requête.
     */
    @FXML
    protected Button boutonNouvelleRequete;

    /**
     * Champ permettant de préciser la durée de la collecte.
     */
    @FXML
    protected TextField pickupDurationField;

    /**
     * Champ permettant de préciser la durée de la livraison.
     */
    @FXML
    protected TextField deliveryDurationField;

    /**
     * Bouton permettant de valider un choix qui le nécessite.
     */
    @FXML
    protected Button boutonValider;

    /**
     * Bouton permettant d'annuler un choix qui le nécessite.
     */
    @FXML
    protected Button boutonAnnuler;

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
     * Renvoie le bouton demandant l'ajout d'une requête.
     *
     * @return Le bouton demandant l'ajout d'une requête.
     */
    public Button getBoutonNouvelleRequete() {
        return boutonNouvelleRequete;
    }

    /**
     * Renvoie le texte précisant la durée de la collecte.
     *
     * @return Le texte précisant la durée de la collecte.
     */
    public TextField getPickUpDurationField() {
        return pickupDurationField;
    }

    /**
     * Renvoie le texte précisant la durée de la livraison.
     *
     * @return Le texte précisant la durée de la livraison.
     */
    public TextField getDeliveryDurationField() {
        return deliveryDurationField;
    }

    /**
     * Renvoie le bouton de validation.
     *
     * @return Le bouton de validation.
     */
    public Button getBoutonValider() {
        return boutonValider;
    }

    /**
     * Renvoie le bouton d'annulation.
     *
     * @return Le bouton d'annulation.
     */
    public Button getBoutonAnnuler() {
        return boutonAnnuler;
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
