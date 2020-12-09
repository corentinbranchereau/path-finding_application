package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque la
 * carte est chargée dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatCarteChargee implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().initFenetreCarteChargee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        Alert messageAlerte = new Alert(AlertType.INFORMATION);
        messageAlerte.setTitle("Information");
        messageAlerte.setHeaderText(null);
        messageAlerte.setContentText("Vous devez charger des requêtes avant de pouvoir lancer le calcul !");
        messageAlerte.showAndWait();
    }
}
