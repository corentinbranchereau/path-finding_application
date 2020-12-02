package fr.hexaone.controller.State;

import java.util.Optional;

import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Trajet;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
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
    public void init(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setVisible(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        c.getPlanning().calculerMeilleurTournee();
        c.getFenetre().getVueGraphique().effacerTrajets();
        for (Trajet trajet : c.getPlanning().getListeTrajets()) {
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
        }

        // c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(),
        // c.getCarte(),
        // c.getFenetre().getMapCouleurRequete());
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajoutNouvelleRequete(Controleur c) {
        // TODO à faire dans l'init
        // c.etatAjoutNouvelleRequete.setIdPickup(null);
        // c.etatAjoutNouvelleRequete.setIdDelivery(null);
        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatAjoutNouvelleRequete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerDemande(Controleur c, Demande demande) {

        if (demande == null) {
            System.out.println("Il faut sélectionner une requete avant.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une requete avant.");
            alert.show();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer la requete ?");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer la demande ? Le point associé peut être orphelin.");

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            c.planning.supprimerDemande(demande);

            c.getFenetre().getVueGraphique().effacerTrajets();
            for (Trajet trajet : c.getPlanning().getListeTrajets()) {
                Color couleur = Color.color(Math.random(), Math.random(), Math.random());
                c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
            }
            c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
            // TODO : Retirer les icônes de la vue Graphique
            // afficherSuppressionRequeteVueTextuelle(requete);

            c.resetDemandeSelectionnee();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierPlanning(Controleur c) {
        // c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(true);
        // c.setEtatModifierPlanning();
    }
}
