package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;
import fr.hexaone.controller.Command.SupprimerDemandeCommand;
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
        c.rafraichirVues();
        // TODO : enlever la ligne du dessous quand la méthode rafraichir de la vue
        // textuelle sera prête
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getPlanning().getCarte());
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
            System.out.println("Il faut sélectionner une requete avant.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une requete avant.");
            alert.show();
            return;
        }

        // TODO : Créer un marqeur car point orphelin

        c.getListOfCommands().add(new SupprimerDemandeCommand(c.getPlanning(), demande));
        c.rafraichirVues();

        // TODO : enlever la ligne du dessous quand la méthode rafraichir de la vue
        // textuelle sera prête
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getPlanning().getCarte());

        c.resetDemandeSelectionnee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierPlanning(Controleur c) {
        //! Etat Useless
        //c.setEtatModifierPlanning();
        //TODO : Mettre au propre le drag and drop selon MVC
        //c.getListOfCommands().add(new ModifierPlanningCommand(...));
    }
}
