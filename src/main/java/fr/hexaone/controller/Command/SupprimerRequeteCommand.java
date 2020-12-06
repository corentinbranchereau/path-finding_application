package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

import java.util.ArrayList;
import java.util.List;

/**
 * Commande de suppression de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class SupprimerRequeteCommand implements Command{

    /**
     * Le planning associé
     */
    private Planning planning;

    /**
     * La requête à supprimer
     */
    private Requete requete;

    /**
     * Les positions des requêtes associées aux demandes dans la liste
     * des demandes
     */
    private List<Integer> positions;

    /**
     * Constructeur de la suppression de requête
     */
    public SupprimerRequeteCommand(Planning planning, Requete requete) {
        this.planning = planning;
        this.requete = requete;
        positions = new ArrayList<>();
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
