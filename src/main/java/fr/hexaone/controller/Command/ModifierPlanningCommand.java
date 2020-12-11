package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

import java.util.Collections;
import java.util.List;

/**
 * Commande de modification du planning avec le drag and drop en suivant le
 * design pattern Command.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierPlanningCommand implements Command {

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * L'index dans la liste de demandes au départ du drag and drop
     */
    private int indexDepart;

    /**
     * L'index dans la liste de demandes à l'arrivée du drag and drop
     */
    private int indexArrivee;

    /**
     * Constructeur de la modificationn de planning
     * 
     * @param planning     Le planning que l'on souhaite modifier
     * @param indexDepart  index dans la liste de demandes du départ du drag and
     *                     drop
     * @param indexArrivee nouvel index dans la liste de demandes à l'arrivée du
     *                     drag and drop
     */
    public ModifierPlanningCommand(Planning planning, int indexDepart, int indexArrivee) {
        this.planning = planning;
        this.indexDepart = indexDepart;
        this.indexArrivee = indexArrivee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {

        List<Demande> demandes = planning.getDemandesOrdonnees();

        if (indexDepart <= indexArrivee) {
            for (int k = indexDepart; k < indexArrivee; k++)
                Collections.swap(demandes, k, k + 1);
        } else {
            for (int k = indexDepart; k > indexArrivee; k--)
                Collections.swap(demandes, k, k - 1);
        }

        planning.ordonnerLesTrajetsEtLesDates();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoCommand() {
        List<Demande> demandes = planning.getDemandesOrdonnees();
        if (indexDepart <= indexArrivee) {
            for (int k = indexArrivee; k > indexDepart; k--)
                Collections.swap(demandes, k, k - 1);
        } else {
            for (int k = indexArrivee; k < indexDepart; k++)
                Collections.swap(demandes, k, k + 1);
        }
        planning.ordonnerLesTrajetsEtLesDates();
    }
}
