package fr.hexaone.controller;

/**
 * Interface implémentant le design pattern STATE pour la gestion des évènements du controleur
 * @author HexaOne
 * @version 1.0
 */
public interface State {

    /**
     * Chargement de la carte
     */
    default void handleClicChargerCarte(Controleur c){
        System.out.println("handleClicChargerCarte [default state implementation]");
    }

    /**
     * Chargement des requêtes
     */
    default void handleClicChargerRequetes(Controleur c){
        System.out.println("handleClicChargerRequetes [default state implementation]");
    }

    /**
     * Calcul du planning
     */
    default void handleClicBoutonCalcul(Controleur c){
        System.out.println("handleClicBoutonCalcul [default state implementation]");
    }

    /**
     * Quitter l'application
     */
    default void handleClicQuitter(Controleur c){
        System.out.println("handleClicQuitter [default state implementation]");
    }


}
