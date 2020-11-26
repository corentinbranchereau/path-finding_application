package fr.hexaone.controller;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque le pop
 * up d'une nouvelle demande de livraison est affiché
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatPopUpNouvelleRequeteAffiche implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ouvrirPopUpNouvelleRequete(Controleur c) {
        // TODO
        try {
            c.getFenetre().getVueTextuelle().afficherPopUpNouvelleDemandeLivraison();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage du pop up de demande d'une nouvelle livraison : " + e);
        }
    }
}
