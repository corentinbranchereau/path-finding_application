package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;

/**
 * Commande de modification du planning avec le drag n drop
 * en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierPlanningCommand implements Command {

    private Planning planning;

    /**
     * Constructeur de la modificationn de planning
     */
    public ModifierPlanningCommand(Planning planning){
        //TODO --> Préparer la commande (les demandes à déplacer etc...)
        this.planning = planning;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {

    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {

    }
}
