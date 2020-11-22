package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque la carte est chargée dans l'application
 * @author HexaOne
 * @version 1.0
 */
public class EtatCarteChargee implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClicChargerRequetes(Controleur c) {
        System.out.println("handleClicChargerRequetes [carte loaded state implementation]");
        c.setEtatCourant(c.etatRequetesChargees);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClicQuitter(Controleur c) {
        System.out.println("handleClicQuitter [carte loaded state implementation] --> TODO");
    }
}
