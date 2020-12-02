package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

/**
 * Commande d'ajout de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterRequeteCommand implements Command{

    private Planning planning;
    private Requete requete;

    /**
     * Constructeur de l'ajout de requête
     */
    public AjouterRequeteCommand(Planning planning, Requete requete){
        this.planning = planning;
        this.requete = requete;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        planning.ajouterRequete(requete);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        planning.supprimerRequete(requete);
    }
}
