package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque les
 * requetes sont chargées dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatRequetesChargees implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().initFenetreRequetesChargees();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        if (c.getPlanning().calculerMeilleurTournee()) {
            c.setEtatTourneeCalcule();
            c.resetDemandeSelectionnee();
        } else {
            Alert messageAlerte = new Alert(AlertType.INFORMATION);
            messageAlerte.setTitle("Information");
            messageAlerte.setHeaderText(null);
            messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
            messageAlerte.showAndWait();
        }
    }
}
