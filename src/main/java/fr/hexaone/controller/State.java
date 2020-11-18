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
    void chargerCarte();

    /**
     * Chargement de la requête
     */
    void chargerRequete();

    /**
     * Calcul du planning
     */
    void calculerPlanning();
}
