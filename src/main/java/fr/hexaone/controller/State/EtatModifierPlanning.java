package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;

/**
 * Implémentation d'un State représentant l'état de l'application lorsqu'une
 * tournée est calculé dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatModifierPlanning implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(true);
    }

    /**
     * {@inheritDoc}
     */
    /*@Override
    public void modifierPlanning(Controleur c) {
        c.setEtatTourneeCalcule();
    }
    */
}