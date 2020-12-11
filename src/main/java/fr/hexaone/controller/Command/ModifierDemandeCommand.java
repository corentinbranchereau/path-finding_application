package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;

/**
 * Commande d'ajout de demande en suivant le design pattern Command.
 *
 * @author HexaOne
 * @version 1.0
 */
public class ModifierDemandeCommand implements Command {

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La demande à ajouter
     */
    private Demande demande;

    /**
     * Durée avant la modification
     */
    private int dureePrecedente;

    /**
     * Id de l'intersection avant la modification
     */
    private Long idIntersectionPrecedente;

    /**
     * Nouvelle durée
     */
    private int nouvelleDuree;

    /**
     * Nouvel id de l'intersection
     */
    private Long idNouvelleIntersection;

    /**
     * Constructeur de la modification de demande.
     * 
     * @param planning       Le planning contenant la demande que l'on veut
     *                       modifier.
     * @param demande        La demande que l'on veut modifier.
     * @param duree          La nouvelle durée.
     * @param idIntersection Le nouvel id de l'intersection de la demande.
     */
    public ModifierDemandeCommand(Planning planning, Demande demande, int duree, Long idIntersection) {
        this.planning = planning;
        this.demande = demande;
        this.dureePrecedente = demande.getDuree();
        this.idIntersectionPrecedente = demande.getIdIntersection();

        if (idIntersection == null) {
            this.idNouvelleIntersection = idIntersectionPrecedente;
        } else {
            this.idNouvelleIntersection = idIntersection;
        }

        this.nouvelleDuree = duree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {
        return planning.modifierDemande(demande, nouvelleDuree, idNouvelleIntersection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoCommand() {
        planning.modifierDemande(demande, dureePrecedente, idIntersectionPrecedente);
    }
}
