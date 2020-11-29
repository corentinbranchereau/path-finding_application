package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on souhaite
 * demander une nouvelle livraison et saisir les intersections
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatSelectionPointNouvelleRequete implements State{

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c) {
        //TODO
        System.out.println("selectionnerIntersection [AjouterNouvelleRequete state implementation] --> TODO");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(false);
        c.setEtatCourant(c.etatTourneeCalcule);
    }

}
