package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;

/**
 * Commande de suppression de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerDemandeCommand implements Command{

    private Demande demande;

    /**
     * Constructeur de la suppression de demande
     */
    public SupprimerDemandeCommand(Demande demande){
        //TODO --> Préparer la commande (la demande à retirer etc...)
        this.demande = demande;
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
