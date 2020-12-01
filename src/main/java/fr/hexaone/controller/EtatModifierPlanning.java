package fr.hexaone.controller;

import java.util.ArrayList;
import java.util.List;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Trajet;
import javafx.scene.control.Alert;
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

    @Override
    public void modifierPlanning(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(false);
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setText("Modifier l'ordre");
        List<Demande> nouvelOrdre = new ArrayList<Demande>();
        for (Demande demande : c.getFenetre().getListeDemandes()) {
            System.out.println(demande.getDateDepartProperty().get());
            nouvelOrdre.add(demande);
        }
        c.getPlanning().setDemandesOrdonnees(nouvelOrdre);
        c.getPlanning().recalculerTournee();
        // c.getFenetre().getVueGraphique().effacerTrajets();
        // for (Trajet trajet : c.getPlanning().getListeTrajets()) {
        // Color couleur = Color.color(Math.random(), Math.random(), Math.random());
        // c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet,
        // couleur);
        // }
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());
        c.setEtatCourant(c.etatTourneeCalcule);
    }

}
