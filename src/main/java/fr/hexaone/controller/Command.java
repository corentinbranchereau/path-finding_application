package fr.hexaone.controller;

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
     */
    void doCommand();

    /**
     * Annuler la commande.
     */
    void undoCommand();
}
