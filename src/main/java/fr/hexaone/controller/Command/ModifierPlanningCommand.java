package fr.hexaone.controller.Command;

/**
 * Commande de modification du planning avec le drag n drop
 * en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierPlanningCommand implements Command{

    /**
     * Constructeur de la modificationn de planning
     */
    public ModifierPlanningCommand(){
        //TODO --> Préparer la commande (les demandes à déplacer etc...)
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
