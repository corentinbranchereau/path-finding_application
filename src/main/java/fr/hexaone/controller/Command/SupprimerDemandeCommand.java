package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.TypeIntersection;

import java.util.List;

/**
 * Commande de suppression de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerDemandeCommand implements Command {

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La demande à supprimer
     */
    private Demande demande;

    /**
     * La position de la demande dans la liste des demandes
     */
    private int index;

    /**
     * Constructeur de la suppression de demande
     * 
     * @param planning Le planning que l'on souhaite modifier.
     * @param demande La demande que l'on souhaite supprimer.
     */
    public SupprimerDemandeCommand(Planning planning, Demande demande) {
        this.planning = planning;
        this.demande = demande;
        this.index = planning.getDemandesOrdonnees().indexOf(demande);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {
        planning.supprimerDemande(demande);

        // On fait passer la valeur à null dans la requête associée
        if (demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
            demande.getRequete().setDemandeCollecte(null);
        } else if (demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
            demande.getRequete().setDemandeLivraison(null);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoCommand() {
        List<Demande> demandes = planning.getDemandesOrdonnees();
        demandes.add(index, demande);

        // On change la valeur dans la requête associée
        if (demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
            demande.getRequete().setDemandeCollecte(demande);
        } else if (demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
            demande.getRequete().setDemandeLivraison(demande);
        }

        planning.recalculerTournee();

    }
}
