package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

/**
 * Commande d'ajout de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterDemandeCommand implements Command{

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La demande à ajouter
     */
    private Demande demande;

    /**
     * Constructeur de l'ajout de demande
     * @param planning Le planning que l'on veut modifier
     * @param demande La demande que l'on veut ajouter au planning
     */
    public AjouterDemandeCommand(Planning planning, Demande demande){
        this.planning = planning;
        this.demande = demande;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {
        return planning.ajouterDemande(demande);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoCommand() {
        planning.supprimerDemande(demande);
    }
}
