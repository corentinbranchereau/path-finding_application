package fr.hexaone.controller;

import fr.hexaone.model.Trajet;
import javafx.scene.paint.Color;

/**
 * Implémentation d'un State représentant l'état de l'application lorsqu'une
 * tournée est calculé dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatTourneeCalcule implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        c.getCarte().calculerTournee(c.getPlanning());
        for (Trajet trajet : c.getPlanning().getListeTrajets()) {
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
        }

        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte(),
                c.getFenetre().getMapCouleurRequete());

        // TODO : Afficher bouton "Ajouter une demande de livraison"
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajoutNouvelleRequete(Controleur c) {
        c.setEtatCourant(c.etatAjouterNouvelleRequete);
    }
}
