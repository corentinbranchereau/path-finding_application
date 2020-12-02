package fr.hexaone.controller.Command;

//TODO : Pas sûr de l'utilité, car supprimer déjà implémenté, à retirer au besoin.
/**
 * Commande inverse en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ReverseCommand implements Command{

    /**
     * Commande à reverse
     */
    private Command cmd;

    /**
     * Constructeur de la commande inverse
     */
    public ReverseCommand(Command cmd){
        this.cmd = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        cmd.undoCommand();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        cmd.doCommand();
    }
}
