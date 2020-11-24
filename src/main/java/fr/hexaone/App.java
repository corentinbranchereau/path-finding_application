package fr.hexaone;

import fr.hexaone.controller.Controleur;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Permet de lancer l'application et de créer le controleur.
 * 
 * @author HexaOne
 * @version 1.0
 */

public class App extends Application {

    /**
     * Le controleur de l'application
     */
    static Controleur controleur;

    /**
     * Cette méthode démarre l'application et instancie le controleur.
     * 
     * @param stage Conteneur principal des éléments graphiques de l'application
     */
    @Override
    public void start(Stage stage) {
        controleur = new Controleur(stage);
    }

    public static void main(String[] args) {
        launch(App.class);
    }
}
