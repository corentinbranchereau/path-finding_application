package fr.hexaone.controller;

import fr.hexaone.view.Fenetre;
import javafx.stage.Stage;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    protected Fenetre fenetre; 

    public Controleur(Stage stage){
        this.fenetre = new Fenetre(stage,this);
        this.fenetre.dessinerFenetre(stage);
    }

    public void handleClicChargerCarte() {
        System.out.println("Charger carte");
    }

    public void handleClicChargerRequetes() {
        System.out.println("Charger requêtes");
    }

    public void handleClicQuitter() {
        System.out.println("Quitter");
    }
}
