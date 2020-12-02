package fr.hexaone.controller.Command;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

import java.util.ArrayList;
import java.util.List;

/**
 * Commande d'ajout de demande en suivant le design pattern COMMAND.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AjouterDemandeCommand implements Command{

    private Planning planning;
    private Demande demande;

    /**
     * Constructeur de l'ajout de demande
     */
    public AjouterDemandeCommand(Planning planning, Demande demande){
        this.planning = planning;
        this.demande = demande;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doCommand() {
        planning.ajouterDemande(demande);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void undoCommand() {
        planning.supprimerDemande(demande);
    }
}
