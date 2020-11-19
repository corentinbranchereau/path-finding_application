package fr.hexaone.controller;

import fr.hexaone.view.Fenetre;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {
    public static void main(String[] args) {
        Fenetre f = new Fenetre();
        f.dessinerFenetre(args);
    }
}
