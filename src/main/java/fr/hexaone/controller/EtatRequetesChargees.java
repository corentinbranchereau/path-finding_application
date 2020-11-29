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
    public void lancerCalcul(Controleur c) {
        // c.getPlanning().setCarte(c.getCarte());
        c.getPlanning().calculerTournee();
        c.getFenetre().getVueGraphique().effacerTrajets();
        for (Trajet trajet : c.getPlanning().getListeTrajets()) {
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
        }

        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte(),
                c.getFenetre().getMapCouleurRequete());

        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);

        c.setEtatCourant(c.etatTourneeCalcule);
    }
}
