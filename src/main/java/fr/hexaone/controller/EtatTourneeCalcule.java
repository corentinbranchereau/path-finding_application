package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsqu'une tournée est calculé dans l'application
 * @author HexaOne
 * @version 1.0
 */
public class EtatTourneeCalcule implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClicQuitter(Controleur c) {
        System.out.println("handleClicQuitter [tour computed state implementation] --> TODO");
    }

}
