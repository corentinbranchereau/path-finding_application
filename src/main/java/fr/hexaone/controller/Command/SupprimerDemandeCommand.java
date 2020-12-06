package fr.hexaone.controller.Command;

import java.util.List;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.TypeIntersection;

/**
 * Commande de suppression de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerDemandeCommand implements Command{

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
     * @param planning
     * @param demande
     * @param index de l'ajout dans la liste
     */
    public SupprimerDemandeCommand(Planning planning, Demande demande){
        this.planning = planning;
        this.demande = demande;
        this.index=planning.getDemandesOrdonnees().indexOf(demande);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        planning.supprimerDemande(demande);

        // On fait passer la valeur à null dans la requête associée
        if (demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
            demande.getRequete().setDemandeCollecte(null);
        } else if (demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
            demande.getRequete().setDemandeLivraison(null);
        }
    }

    /**
     * @inheritDoc
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
