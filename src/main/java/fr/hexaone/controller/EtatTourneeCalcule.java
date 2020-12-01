package fr.hexaone.controller;

import java.util.Optional;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
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
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(true);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(true);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(false);
        c.etatAjoutNouvelleRequete.setIdPickup(null);
        c.etatAjoutNouvelleRequete.setIdDelivery(null);
        c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatCourant(c.etatAjoutNouvelleRequete);
    }

    @Override
    public void supprimerRequete(Controleur c, Demande demande) {

        if (demande == null) {
            System.out.println("Il faut sélectionner une requete avant.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une requete avant.");
            alert.show();
            return;
        }

        Requete requete = demande.getRequete();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Supprimer la requete ?");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer la requete (demande de collecte et de livraison) ?");

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            c.planning.supprimerRequete(requete);
            // TODO refresh la vue textuelle et la vue graphique
            c.getFenetre().getVueGraphique().effacerTrajets();
            for (Trajet trajet : c.getPlanning().getListeTrajets()) {
                Color couleur = Color.color(Math.random(), Math.random(), Math.random());
                c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
            }
            c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
            // TODO Supprimer la requete de l'observable list de Coco ?
            // afficherSuppressionRequeteVueTextuelle(requete);
        }
    }
}
