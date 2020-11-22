package fr.hexaone.controller;

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
    public void lancerCalcul(Controleur c) {
        System.out.println("handleClicBoutonCalcul [requests loaded state implementation]");
        c.setEtatCourant(c.etatTourneeCalcule);
    }
}
