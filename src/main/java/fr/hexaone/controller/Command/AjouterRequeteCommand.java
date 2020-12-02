package fr.hexaone.controller.Command;

/**
 * Commande d'ajout de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterRequeteCommand implements Command{

    /**
     * Constructeur de l'ajout de requête
     */
    public AjouterRequeteCommand(){
        //TODO --> Préparer la commande (les données à ajouter etc...)
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
