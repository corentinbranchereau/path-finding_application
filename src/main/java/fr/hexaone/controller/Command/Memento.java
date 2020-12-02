package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;

/**
 * Classe Memento permettant de stocker un état selon le design pattern Memento
 * du undo/redo dans le controleur.
 *
 * @author HexaOne
 * @version 1.0
 */
class Memento {
	
	/**
	 * Etat à sauvegarder : un planning
	 */
    private Planning state;

    /**
     * Constructeur de Memento
     * @param stateToSave
     */
    public Memento(Planning stateToSave) { state = new Planning(stateToSave); }
    
    
    /**
     * Retourne l'état
     * @return
     */
    public Planning getSavedState() { return state; }
}
