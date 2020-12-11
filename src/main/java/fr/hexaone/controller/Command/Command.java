package fr.hexaone.controller.Command;

/**
 * Interface implémentant le design pattern Command pour la gestion du undo/redo
 * dans le controleur.
 *
 * @author HexaOne
 * @version 1.0
 */
public interface Command {

    /**
     * Permet d'exécuter la commande.
     * 
     * @return True si la commande a été exécutée, false sinon.
     */
    boolean doCommand();

    /**
     * Annule la commande.
     */
    void undoCommand();
}
