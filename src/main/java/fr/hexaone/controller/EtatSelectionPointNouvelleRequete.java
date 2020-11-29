package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on souhaite
 * demander une nouvelle livraison et saisir les intersections
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatSelectionPointNouvelleRequete implements State{

    private Long idPickup = null;
    private Long idDelivery = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
        if(idPickup==null){
            idPickup = idIntersection;
        } else if(idDelivery==null && !idIntersection.equals(idPickup)){
            idDelivery = idIntersection;
            c.setEtatCourant(c.etatSaisieDureeNouvelleRequete);
            c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(false);
            c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(false);
            c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(false);
            ((EtatSaisieDureeNouvelleRequete)c.etatCourant).setIdPickup(idPickup);
            ((EtatSaisieDureeNouvelleRequete) c.etatCourant).setIdDelivery(idDelivery);
            idPickup = null;
            idDelivery = null;
        }
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
        c.getFenetre().getFenetreControleur().getPickUpDurationField().clear();
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().clear();
        idPickup = null;
        idDelivery = null;
        c.setEtatCourant(c.etatTourneeCalcule);
    }

}
