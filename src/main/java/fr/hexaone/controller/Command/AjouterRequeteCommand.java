package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

/**
 * Commande d'ajout de requête en suivant le design pattern Command.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterRequeteCommand implements Command {

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La requête à ajouter
     */
    private Requete requete;

    /**
     * Constructeur de l'ajout de requête
     * 
     * @param planning Le planning que l'on veut modifier
     * @param requete  La requete que l'on veut ajouter au planning
     */
    public AjouterRequeteCommand(Planning planning, Requete requete) {
        this.planning = planning;
        this.requete = requete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {
        return planning.ajouterRequete(requete);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoCommand() {
        planning.supprimerRequete(requete);
    }
}
