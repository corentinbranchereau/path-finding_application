package fr.hexaone.controller.Command;

import java.util.List;

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
    private int index;

    /**
     * Constructeur de la suppression de demande
     * @param planning
     * @param demande
     * @param index de l'ajout dans la liste
     */
    public SupprimerDemandeCommand(Planning planning, Demande demande, int index){
        this.planning = planning;
        this.demande = demande;
        this.index=index;
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
    	List<Demande>demandes=planning.getDemandesOrdonnees();
    	demandes.add(index,demande);

    }
}
