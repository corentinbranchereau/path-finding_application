package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on
 * souhaite demander une nouvelle livraison et saisir les durées
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatSaisieDureeNouvelleRequete implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String pickUpDurationField, String deliveryDurationField) {
        // TODO
        System.out.println("valider [SaisieDureeNouvelleRequete state implementation]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(true);
        c.setEtatCourant(c.etatTourneeCalcule);
    }
}
