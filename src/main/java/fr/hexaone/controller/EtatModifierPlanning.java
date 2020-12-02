package fr.hexaone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Trajet;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque on
 * peut modifier le planning
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatModifierPlanning implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(false);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierPlanning(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setDisable(false);
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setText("Modifier l'ordre");
        List<Demande> nouvelOrdre = new ArrayList<Demande>();
        for (Demande demande : c.getFenetre().getListeDemandes()) {
            nouvelOrdre.add(demande);
        }
        c.getPlanning().setDemandesOrdonnees(nouvelOrdre);
        c.getPlanning().recalculerTournee();
        c.getFenetre().getVueGraphique().effacerTrajets();
        for (Trajet trajet : c.getPlanning().getListeTrajets()) {
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
        }
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
        c.setEtatTourneeCalcule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chargerCarte(Controleur c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Charger une nouvelle Carte ?");
        alert.setHeaderText(null);
        alert.setContentText("Les modifications réalisées seront effacées. Continuer ?");

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            c.getFenetre().getVueTextuelle().effacerVueTextuelle();
            c.setEtatInitial();
            c.chargerCarte();
        } else {
            // Rien
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chargerRequetes(Controleur c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Charger de nouvelles Requêtes ?");
        alert.setHeaderText(null);
        alert.setContentText("Les modifications réalisées seront effacées. Continuer ?");

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            c.getFenetre().getVueTextuelle().effacerVueTextuelle();
            c.setEtatCarteChargee();
            c.chargerRequetes();
        } else {
            // Rien
        }
    }

}
