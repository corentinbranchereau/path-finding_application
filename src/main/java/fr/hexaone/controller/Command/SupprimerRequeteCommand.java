package fr.hexaone.controller.Command;

/**
 * Commande de suppression de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerRequeteCommand implements Command{

    /**
     * Constructeur de la suppression de requête
     */
    public SupprimerRequeteCommand(){
        //TODO --> Préparer la commande (les demandes à retirer etc...)
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
