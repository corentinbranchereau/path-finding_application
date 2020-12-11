package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.TypeDemande;

import java.util.List;

/**
 * Commande de suppression de demande en suivant le design pattern Command.
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
    private int indexDemande;

    /**
     * Constructeur de la suppression de demande
     * 
     * @param planning Le planning que l'on souhaite modifier.
     * @param demande  La demande que l'on souhaite supprimer.
     */
    public SupprimerDemandeCommand(Planning planning, Demande demande) {
        this.planning = planning;
        this.demande = demande;
        this.indexDemande = planning.getDemandesOrdonnees().indexOf(demande);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doCommand() {
        planning.supprimerDemande(demande);

        // On fait passer la valeur à null dans la requête associée
        if (demande.getTypeDemande() == TypeDemande.COLLECTE) {
            demande.getRequete().setDemandeCollecte(null);
        } else if (demande.getTypeDemande() == TypeDemande.LIVRAISON) {
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
        demandes.add(indexDemande, demande);

        // On change la valeur dans la requête associée
        if (demande.getTypeDemande() == TypeDemande.COLLECTE) {
            demande.getRequete().setDemandeCollecte(demande);
        } else if (demande.getTypeDemande() == TypeDemande.LIVRAISON) {
            demande.getRequete().setDemandeLivraison(demande);
        }

        planning.recalculerTournee();

    }
}
