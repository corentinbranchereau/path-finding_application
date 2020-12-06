package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

/**
 * Commande d'ajout de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierDemandeCommand implements Command{

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La demande à ajouter
     */
    private Demande demande;
    
    /**
     * durée avant la modification
     */
    private int dureeAvant;
    
    /**
     * id de l'intersection avant la modification
     */
    private Long idIntersectionGeoAvant;
    
    /**
     * Nouvelle durée
     */
    private int dureeNouvelle;
    
    /**
     * Nouvel id de l'intersection
     */
    private Long idIntersectionGeoNouveau;
    

   /**
    *  Constructeur de la modification de demande
    * @param planning
    * @param demande
    * @param duree
    * @param idIntersection
    */
    public ModifierDemandeCommand(Planning planning, Demande demande, int duree, Long idIntersection){
        this.planning = planning;
        this.demande = demande;
        this.dureeAvant=demande.getDuree();
        this.idIntersectionGeoAvant = demande.getIdIntersection();
        
        if(idIntersection==null) {
        	this.idIntersectionGeoNouveau=idIntersectionGeoAvant;
        }
        else {
        	this.idIntersectionGeoNouveau=idIntersection;
        }
        
        this.dureeNouvelle=duree;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
    	
        planning.modifierDemande(demande, dureeNouvelle,idIntersectionGeoNouveau);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        planning.modifierDemande(demande,dureeAvant,idIntersectionGeoAvant);
    }
}
