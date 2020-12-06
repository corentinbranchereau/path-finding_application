package fr.hexaone.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
    private MenuItem undoItem ;
    
    
    /**
     * Item du menu permettant de faire un redo
     */
    @FXML
    private MenuItem redoItem ;


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
    private Label pickupDurationLabel;

    /**
     * Champ permettant de préciser la durée de la collecte.
     */
    @FXML
    private TextField pickupDurationField;

    /**
     * Champ permettant de préciser la durée de la livraison.
     */
    @FXML
    private Label deliveryDurationLabel;

    /**
     * Champ permettant de préciser la durée de la livraison.
     */
    @FXML
    private TextField deliveryDurationField;
    
    
    /**
     * Label permettant de préciser la durée lors d'une modification
     */
    @FXML
    private Label durationLabel;

    /**
     * Champ permettant de préciser la durée lors d'une modification
     */
    @FXML
    private TextField durationField;
    

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
    private ScrollPane textScrollPane;

    /**
     * l'endroit ou on écrit les informations sur le dépot
     */
    @FXML
    private TextFlow depotTextInformation;

    /**
     * Bouton qui permet d'afficher l'aide de l'application
     */
    @FXML
    private Button boutonAide;

    /**
     * Renvoie le bouton permettant d'afficher l'aide de l'application
     * 
     * @return le bouton
     */
    public Button getBoutonAide() {
        return boutonAide;
    }

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
     * Renvoie le bouton demandant l'ajout d'une requête.
     *
     * @return Le bouton demandant l'ajout d'une requête.
     */
    public Button getBoutonNouvelleRequete() {
        return boutonNouvelleRequete;
    }

    /**
     * Renvoie le label indiquant le champ permettant de préciser la durée de la
     * collecte.
     *
     * @return Le label indiquant le champ permettant de préciser la durée de la
     *         collecte.
     */
    public Label getPickUpDurationLabel() {
        return pickupDurationLabel;
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
     * Renvoie le label indiquant le champ permettant de préciser la durée de la
     * livraison.
     *
     * @return Le label indiquant le champ permettant de préciser la durée de la
     *         livraison.
     */
    public Label getDeliveryDurationLabel() {
        return deliveryDurationLabel;
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
     * Getter
     * @return le label de la durée lors d'une modification
     */
    public Label getDurationLabel() {
		return durationLabel;
	}

	public void setDurationLabel(Label durationLabel) {
		this.durationLabel = durationLabel;
	}

	/**
     * Getter
     * @return le texte de la durée lors d'une modfification
     */
	public TextField getDurationField() {
		return durationField;
	}

    /**
     * Setter
     * @param durationField La durée de la demande
     */
	public void setDurationField(TextField durationField) {
		this.durationField = durationField;
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
     * Renvoie le bouton de validation de demande.
     *
     * @return Le bouton de validation de demande.
     */
    public Button getBoutonValiderModificationDemande() {
        return boutonValiderModificationDemande;
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
     * Renvoie la box contenant les boutons Valider et Annuler.
     *
     * @return La box contenant les boutons Valider et Annuler.
     */
    public HBox getBoxBoutonsValiderAnnuler() {
        return boxBoutonsValiderAnnuler;
    }

    /**
     * Renvoie le AnchorPane contenant le pane de dessin
     * 
     * @return Le AnchorPane contenant le pane de dessin
     */
    public AnchorPane getAnchorPaneGraphique() {
        return anchorPaneGraphique;
    }

    public TextFlow getDepotTextInformation() {
        return depotTextInformation;
    }

    public Pane getPaneDessin() {
        return paneDessin;
    }
    public MenuItem getUndoItem() {
		return undoItem;
	}

	public MenuItem getRedoItem() {
		return redoItem;
	}

}
