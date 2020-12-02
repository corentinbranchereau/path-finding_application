package fr.hexaone.controller.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Caretaker permettant de gérer les différents états selon le design pattern Memento
 * du undo/redo dans le controleur.
 *
 * @author HexaOne
 * @version 1.0
 */

class Caretaker {
	
	  /**
	   * Liste des états sauvegardés
	   */
	   private List savedStates;
	   
	   /**
	   * Index privé permettant de gérer l'ordres des commandes dans la liste des commandes.
	   */
	   private int i;
	   
	   /**
	    * Constructeur par défaut de Caretaker
	    */
	   public Caretaker() {
		   i=-1;
		   savedStates = new ArrayList();
	   }

	   /**
	    * Permet d'ajouter un memento dans la liste des états sauvegardés
	    * @param m
	    */
	   public void addMemento(Object m) { i++; savedStates.add(m); }
	   
	   /**
	    * Méthode undo
	    * @return
	    */
	   public Object undo() {
		   if(i>=1) {
			   i--;
		   }
		   return savedStates.get(i);  
	   }
	   
	   
	   /**
	    * Méthode Redo
	    * @return
	    */
	   public Object redo() {
		   
	        if(savedStates.get(++i) != null)
	        {
	        	return savedStates.get(i);  
	        }
	            
	        else --i;
	        
	        return savedStates.get(i);  
	   }        
	}   
