package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ModifierDemandeCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.utils.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on
 * souhaite modifier une demande (lieu et/ou durée)
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatModifierDemande implements State {

    /**
     * L'id de l'intersection sélectionnée
     */
    private Long idIntersection = null;

    /**
     * La durée saisie (en secondes)
     */
    private int duree;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().initFenetreModifierDemande(this.duree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
        if (this.idIntersection != null && this.idIntersection.equals(idIntersection)) {
            c.getFenetre().getVueGraphique().deselectionneIntersection(this.idIntersection);
            this.idIntersection = null;
        } else if (this.idIntersection == null) {
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            this.idIntersection = idIntersection;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String... durees) {
        String champDuree = durees[0];

        // On vérifie s'il y a des erreurs dans la saisie et/ou la sélection
        if (idIntersection == null && champDuree.isEmpty()) {
            Utils.afficherAlerte(this, "Mauvaise sélection",
                    "Il faut sélectionner la nouvelle intersection ou modifier la durée.", Alert.AlertType.ERROR);
            return;
        } else {
            if (champDuree.isEmpty()) {
                Utils.afficherAlerte(this, "Mauvaise saisie de durée", "Le champ concernant la durée  est vide !",
                        Alert.AlertType.ERROR);
            }

        }

        if (!Utils.verifieDureeUtilisateur(champDuree)) {
            Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                    "La durée (en seconde) ne doit contenir que des chiffres !", Alert.AlertType.ERROR);
            return;
        }

        try {
            if (!c.getListOfCommands().ajouterCommande(new ModifierDemandeCommand(c.getPlanning(),
                    c.getDemandeSelectionnee(), Integer.parseInt(champDuree), idIntersection))) {
                Alert messageAlerte = new Alert(AlertType.INFORMATION);
                messageAlerte.setTitle("Information");
                messageAlerte.setHeaderText(null);
                messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                messageAlerte.showAndWait();
            }
        } catch (NumberFormatException e) {
            Utils.afficherAlerte(this, "Mauvaise saisie de durée", "Les durées (en seconde) saisies sont incorrectes !",
                    Alert.AlertType.ERROR);
            return;
        } finally {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
            this.annuler(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        idIntersection = null;
        c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatTourneeCalcule();
    }

    /**
     * Change la valeur de la durée
     * 
     * @param duree La nouvelle valeur de la durée (en secondes)
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

}
