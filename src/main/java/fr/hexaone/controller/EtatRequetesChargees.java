package fr.hexaone.controller;

import fr.hexaone.model.Trajet;
import javafx.scene.paint.Color;

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
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setVisible(false);
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
                // c.getPlanning().setCarte(c.getCarte());
                c.getPlanning().calculerMeilleurTournee();
                c.getFenetre().getVueGraphique().effacerTrajets();
                for (Trajet trajet : c.getPlanning().getListeTrajets()) {
                        Color couleur = Color.color(Math.random(), Math.random(), Math.random());
                        c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
                }

                // c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(),
                // c.getCarte(),
                // c.getFenetre().getMapCouleurRequete());
                // c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setDisable(false);
                // c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
                c.setEtatTourneeCalcule();
                // c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
                // c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setDisable(false);
                // c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
                
        }
}
