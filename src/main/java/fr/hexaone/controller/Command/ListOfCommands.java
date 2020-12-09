package fr.hexaone.controller.Command;

import java.util.LinkedList;

/**
 * Liste des commandes du design pattern Command afin de
 * gérer l'undo/redo
 *
 * @author HexaOne
 * @version 1.0
 */
public class ListOfCommands {

    /**
     * Liste des commandes
     */
    private LinkedList<Command> l;

    /**
     * Index privé permettant de gérer l'ordres des commandes dans la liste des commandes.
     */
    private int i;

    /**
     * Constructeur de ListOfCommands.
     * Instancie l'index des commandes à -1 ainsi que la liste vide de commandes.
     */
    public ListOfCommands(){
        i=-1;
        l = new LinkedList<>();
    }

    /**
     * Ajoute une commande à la liste des commandes et réalise la commande.
     * @param c La commande à ajouter.
     * @return Whether we could add the command c or not.
     */
    public boolean add(Command c){
        i++;
        if(i<l.size()) {
        	int taille=l.size();
        	for(int j=i;j<taille;j++) {
        		l.remove(i);
        	}	
        }
        l.add(c);
        if ( !c.doCommand() ) {
            l.remove(i);
            i--;
            return false;
        }
        return true;
    }

    /**
     * Annule la dernière commande réalisée, si elle existe.
     */
    public void undo(){
        if(i>=0){
            l.get(i).undoCommand();
            i--;
        }
    }

    /**
     * Rétablir la dernière commande réalisée, si elle existe.
     */
    public void redo(){
    	if(i+1<=l.size()-1) {
    		  l.get(++i).doCommand();
    	}
    }  

    /**
     * Réinitialiser toutes les commandes.
     */
    public void reinitialiser() {
        l.clear();
        i=-1;
    }
    
}
