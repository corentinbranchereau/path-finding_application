package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

import java.util.Collections;
import java.util.List;

/**
 * Commande de modification du planning avec le drag n drop
 * en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierPlanningCommand implements Command {

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * L'index dans la liste de demandes au départ du drag n drop
     */
    private int i;

    /**
     * L'index dans la liste de demandes à l'arrivée du drag n drop
     */
    private int j;

   /**
    * Constructeur de la modificationn de planning
    * @param planning
    * @param i index dans la liste de demandes du départ du drag/drop
    * @param j nouvel index dans la liste de demandes à l'arrivée du drag/drop
    */
    public ModifierPlanningCommand(Planning planning, int i, int j){
        this.planning = planning;
        this.i = i;
        this.j = j;
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean doCommand() {

    	List<Demande> demandes = planning.getDemandesOrdonnees();
    	
    	if(i <= j) {
    		for(int k=i;k<j;k++)
        		Collections.swap(demandes, k, k + 1);
    	} else {
    		for(int k = i; k > j; k--)
        		Collections.swap(demandes, k, k - 1);	
        }
        
        planning.ordonnerLesTrajetsEtLesDates();
        
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
    	List<Demande> demandes=planning.getDemandesOrdonnees();
    	if(i <= j) {
        	for(int k = j; k > i; k--)
        		Collections.swap(demandes, k, k - 1);
    	}
    	else {
    		for(int k = j; k < i; k++)
        		Collections.swap(demandes, k, k + 1);
    	}
    	planning.ordonnerLesTrajetsEtLesDates();
    }
}
