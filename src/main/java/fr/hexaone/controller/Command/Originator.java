package fr.hexaone.controller.Command;

import fr.hexaone.model.Planning;

/**
 * Classe Originator permettant de gérer l'état courant selon le design pattern Memento
 * du undo/redo dans le controleur.
 *
 * @author HexaOne
 * @version 1.0
 */

class Originator {
	/**
	 * Etat courant
	 */
   private Planning state;

   /**
    * Met à jour l'état
    * @param state
    */
   public void set(Planning state) { 
       System.out.println("Originator: etat affecte a: "+state);
       this.state=new Planning(state);
   }

   /**
    * Permet de sauvegarder le Memento
    * @return
    */
   public Object saveToMemento() { 
       System.out.println("Originator: sauvegarde dans le memento.");
       return new Memento(state); 
   }
   
   
   /**
    * Permet de restaurer l'état à partir d'un Memento
    * @param m
    */
   public void restoreFromMemento(Object m) {
       if (m instanceof Memento) {
           Memento memento = (Memento)m; 
           state = memento.getSavedState(); 
           System.out.println("Originator: Etat après restauration: "+state);
       }
   }

}


