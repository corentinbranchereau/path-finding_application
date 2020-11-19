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
     * Cette méthode démarre l'application et instancie le controleur.
     * 
     * @param stage Variable propre à JavaFX permettant d'afficher les éléments à
     *              l'écran
     */
    @Override
    public void start(Stage stage) {
        Controleur controleur = new Controleur(stage);
    }

    public static void main(String[] args) {
        launch(App.class, args);
    }
}
