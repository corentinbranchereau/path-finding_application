package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

/**
 * Commande de suppression de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerDemandeCommand implements Command{

    private Planning planning;
    private Demande demande;

    /**
     * Constructeur de la suppression de demande
     */
    public SupprimerDemandeCommand(Planning planning, Demande demande){
        this.planning = planning;
        this.demande = demande;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        planning.supprimerDemande(demande);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {

    }
}
