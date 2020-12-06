package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque les
 * requetes sont chargées dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatRequetesChargees implements State {

        /**
         * {@inheritDoc}
         */
        @Override
        public void init(Controleur c) {
            c.getFenetre().getFenetreControleur().getDurationField().setVisible(false);
            c.getFenetre().getFenetreControleur().getDurationLabel().setVisible(false);
            c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setVisible(false);
            c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
            c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(true);
            c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
            c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
            c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
            c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
            c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
            c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);
            c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(false);
            c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void lancerCalcul(Controleur c) {
                c.getPlanning().calculerMeilleurTournee();
                c.setEtatTourneeCalcule();
        }
}
