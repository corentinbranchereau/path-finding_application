package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import java.util.List;
import java.util.ArrayList;

/**
 * Commande d'ajout de requête en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterRequeteCommand implements Command{

    private Planning planning;
    private Requete requete;
    List<Integer> positions;

    /**
     * Constructeur de l'ajout de requête
     */
    public AjouterRequeteCommand(Planning planning, Requete requete){
        this.planning = planning;
        this.requete = requete;
        positions = new ArrayList<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        planning.ajouterRequete(requete);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        positions = planning.supprimerRequete(requete);
    }
}
