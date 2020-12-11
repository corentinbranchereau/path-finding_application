package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private MenuItem chargerCarteItem;

    /**
     * Item du menu permettant de charger des requêtes
     */
    @FXML
    private MenuItem chargerRequetesItem;

    /**
     * Item du menu permettant de quitter l'application
     */
    @FXML
    private MenuItem quitterItem;

    /**
     * Item du menu permettant de faire un undo
     */
    @FXML
    private MenuItem undoItem;

    /**
     * Menu d'aide pour l'utilisateur
     */
    @FXML
    private MenuItem aide;

    /**
     * Item du menu permettant de faire un redo
     */
    @FXML
    private MenuItem redoItem;

    /**
     * Rectangle permettant de dessiner une bordure autour de la carte.
     */
    @FXML
    private Rectangle bordureCarte;

    /**
     * Zone de texte où sont affichées les informations sur le planning. Correspond
     * à la vue textuelle de l'application.
     */
    @FXML
    private TextFlow zoneTexte;

    /**
     * Bouton permettant de lancer le calcul du planning.
     */
    @FXML
    private Button boutonLancer;

    /**
     * Bouton permettant de lancer le processus d'ajout d'une nouvelle requête.
     */
    @FXML
    private Button boutonNouvelleRequete;

    /**
     * Label indiquant le champ permettant de préciser la durée de la collecte.
     */
    @FXML
    private Label labelDureeCollecte;

    /**
     * Champ permettant de préciser la durée de la collecte.
     */
    @FXML
    private TextField champDureeCollecte;

    /**
     * Champ permettant de préciser la durée de la livraison.
     */
    @FXML
    private Label labelDureeLivraison;

    /**
     * Champ permettant de préciser la durée de la livraison.
     */
    @FXML
    private TextField champDureeLivraison;

    /**
     * Label permettant de préciser la durée lors d'une modification
     */
    @FXML
    private Label labelDuree;

    /**
     * Champ permettant de préciser la durée lors d'une modification
     */
    @FXML
    private TextField champDuree;

    /**
     * Bouton permettant de valider un choix qui le nécessite.
     */
    @FXML
    private Button boutonValider;

    /**
     * Bouton permettant de valider la modification d'une demande
     */
    @FXML
    private Button boutonValiderModificationDemande;

    /**
     * Bouton permettant d'annuler un choix qui le nécessite.
     */
    @FXML
    private Button boutonAnnuler;

    /**
     * Box contenant les boutons Valider et Annuler.
     */
    @FXML
    private HBox boxBoutonsValiderAnnuler;

    /**
     * Pane qui contient les éléments graphiques dessinant la carte
     */
    @FXML
    private Pane paneDessin;

    /**
     * AnchorPane contenant le pane de dessin (vue graphique)
     */
    @FXML
    private AnchorPane anchorPaneGraphique;

    /**
     * Scroll pane de la vue textuelle
     */
    @FXML
    private ScrollPane scrollPaneTexte;

    /**
     * L'endroit où on écrit les informations sur le dépôt
     */
    @FXML
    private TextFlow texteInformationDepot;

    /**
     * @return L'item d'aide du menu
     */
    public MenuItem getAide() {
        return aide;
    }

    /**
     * @return L'item chargerCarteItem du menu
     */
    public MenuItem getChargerCarteItem() {
        return chargerCarteItem;
    }

    /**
     * @return L'item scrollPaneTexte du menu
     */
    public ScrollPane getScrollPaneTexte() {
        return scrollPaneTexte;
    }

    /**
     * @return L'item chargerRequetesItem du menu
     */
    public MenuItem getChargerRequetesItem() {
        return chargerRequetesItem;
    }

    /**
     * @return L'item quitterItem du menu
     */
    public MenuItem getQuitterItem() {
        return quitterItem;
    }

    /**
     * @return Le rectangle dessinant la bordure de la carte
     */
    public Rectangle getBordureCarte() {
        return bordureCarte;
    }

    /**
     * @return La zone de texte de l'application.
     */
    public TextFlow getZoneTexte() {
        return zoneTexte;
    }

    /**
     * @return Le bouton lançant le calcul du planning.
     */
    public Button getBoutonLancer() {
        return boutonLancer;
    }

    /**
     * @return Le bouton demandant l'ajout d'une requête.
     */
    public Button getBoutonNouvelleRequete() {
        return boutonNouvelleRequete;
    }

    /**
     * @return Le label indiquant le champ permettant de préciser la durée de la
     *         collecte.
     */
    public Label getLabelDureeCollecte() {
        return labelDureeCollecte;
    }

    /**
     * @return Le texte précisant la durée de la collecte.
     */
    public TextField getChampDureeCollecte() {
        return champDureeCollecte;
    }

    /**
     * @return Le label indiquant le champ permettant de préciser la durée de la
     *         livraison.
     */
    public Label getLabelDureeLivraison() {
        return labelDureeLivraison;
    }

    /**
     * @return Le texte précisant la durée de la livraison.
     */
    public TextField getChampDureeLivraison() {
        return champDureeLivraison;
    }

    /**
     * @return Le label de la durée lors d'une modification
     */
    public Label getLabelDuree() {
        return labelDuree;
    }

    /**
     * Change la valeur du label de durée
     * 
     * @param labelDuree
     */
    public void setLabelDuree(Label labelDuree) {
        this.labelDuree = labelDuree;
    }

    /**
     * @return Le texte de la durée lors d'une modfification
     */
    public TextField getChampDuree() {
        return champDuree;
    }

    /**
     * Change la valeur du champ de la durée
     * 
     * @param champDuree La durée de la demande
     */
    public void setChampDuree(TextField champDuree) {
        this.champDuree = champDuree;
    }

    /**
     * @return Le bouton de validation.
     */
    public Button getBoutonValider() {
        return boutonValider;
    }

    /**
     * @return Le bouton de validation de demande.
     */
    public Button getBoutonValiderModificationDemande() {
        return boutonValiderModificationDemande;
    }

    /**
     * @return Le bouton d'annulation.
     */
    public Button getBoutonAnnuler() {
        return boutonAnnuler;
    }

    /**
     * @return La box contenant les boutons Valider et Annuler.
     */
    public HBox getBoxBoutonsValiderAnnuler() {
        return boxBoutonsValiderAnnuler;
    }

    /**
     * @return Le AnchorPane contenant le pane de dessin
     */
    public AnchorPane getAnchorPaneGraphique() {
        return anchorPaneGraphique;
    }

    /**
     * @return La zone d'information autour du dépôt
     */
    public TextFlow getTexteInformationDepot() {
        return texteInformationDepot;
    }

    /**
     * @return La pane de dessin
     */
    public Pane getPaneDessin() {
        return paneDessin;
    }

    /**
     * @return L'item du menu permettant l'undo
     */
    public MenuItem getUndoItem() {
        return undoItem;
    }

    /**
     * @return L'item du menu permettant le redo
     */
    public MenuItem getRedoItem() {
        return redoItem;
    }
}
