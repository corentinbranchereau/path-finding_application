package fr.hexaone.controller.Command;

import java.util.ArrayList;
import java.util.List;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

/**
 * Commande de suppression de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerRequeteCommand implements Command{

    private Requete requete;
    private Planning planning;
    List<Integer> positions;

    /**
     * Constructeur de la suppression de requête
     */
    public SupprimerRequeteCommand(Requete requete, Planning planning) {
        this.requete = requete;
        this.planning = planning;
        positions = new ArrayList<>();
        //TODO --> Préparer la commande (les demandes à retirer etc...)
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        positions = planning.supprimerRequete(requete);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        planning.ajouterRequete(requete, positions);
    }
}
