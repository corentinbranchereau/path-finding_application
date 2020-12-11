package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ModifierPlanningCommand;
import fr.hexaone.controller.Command.SupprimerDemandeCommand;
import fr.hexaone.controller.Command.SupprimerRequeteCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.utils.Utils;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsqu'une
 * tournée a été calculée dans l'application
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
        c.getFenetre().initFenetreTourneeCalcule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        c.getPlanning().calculerMeilleurTournee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajoutNouvelleRequete(Controleur c) {
        c.setEtatAjoutNouvelleRequete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerDemande(Controleur c, Demande demande) {
        if (demande == null) {
            Utils.afficherAlerte(this, "Mauvaise sélection", "Il faut d'abord sélectionner une demande.",
                    AlertType.ERROR);
            return;
        }

        if (c.getListOfCommands().ajouterCommande(new SupprimerDemandeCommand(c.getPlanning(), demande))) {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
        } else {
            Utils.afficherAlerte(this, "Information", "Au moins une de vos demandes est inaccessible.",
                    AlertType.INFORMATION);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerRequete(Controleur c, Requete requete) {
        if (requete == null) {
            Utils.afficherAlerte(this, "Mauvaise sélection", "Il faut d'abord sélectionner une requête.",
                    AlertType.ERROR);
            return;
        }

        if (c.getListOfCommands().ajouterCommande(new SupprimerRequeteCommand(c.getPlanning(), requete))) {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
        } else {
            Utils.afficherAlerte(this, "Information", "Au moins une de vos demandes est inaccessible.",
                    AlertType.INFORMATION);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierDemande(Controleur c, Demande demande) {
        if (demande == null) {
            Utils.afficherAlerte(this, "Mauvaise sélection", "Il faut sélectionner une demande à modifier",
                    AlertType.ERROR);
            return;
        }
        c.setEtatModifierDemande(demande.getDuree());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierPlanning(Controleur c, int i, int j) {
        if (!c.getListOfCommands().ajouterCommande(new ModifierPlanningCommand(c.getPlanning(), i, j))) {
            Utils.afficherAlerte(this, "Information", "Au moins une de vos demandes est inaccessible",
                    AlertType.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerDemande(Controleur c, Demande demandeSelectionnee) {
        if (c.getDemandeSelectionnee() == demandeSelectionnee)
            c.setDemandeSelectionnee(null);
        else
            c.setDemandeSelectionnee(demandeSelectionnee);
        c.rafraichirVues(false);
    }
}
