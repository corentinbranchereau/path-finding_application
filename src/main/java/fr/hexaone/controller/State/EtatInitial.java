package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état initial de l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatInitial implements State {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().getFenetreControleur().getDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(false);
        // c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);

        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
        
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(false);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chargerRequetes(Controleur c) {
        Alert messageAlerte = new Alert(AlertType.INFORMATION);
        messageAlerte.setTitle("Information");
        messageAlerte.setHeaderText(null);
        messageAlerte.setContentText("Vous devez charger une carte avant de pouvoir charger des requêtes !");
        messageAlerte.showAndWait();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        Alert messageAlerte = new Alert(AlertType.INFORMATION);
        messageAlerte.setTitle("Information");
        messageAlerte.setHeaderText(null);
        messageAlerte
                .setContentText("Vous devez charger une carte et des requêtes avant de pouvoir lancer le calcul !");
        messageAlerte.showAndWait();
    }
}
