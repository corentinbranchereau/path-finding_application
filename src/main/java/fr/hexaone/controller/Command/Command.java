package fr.hexaone.controller.Command;

/**
 * Interface implémentant le design pattern Command pour la gestion
 * du undo/redo dans le controleur.
 *
 * @author HexaOne
 * @version 1.0
 */
public interface Command {

    /**
     * Exécuter la commande.
     * @return True si la commande a été exécutée.
     */
    boolean doCommand();

    /**
     * Annuler la commande.
     */
    void undoCommand();
}
