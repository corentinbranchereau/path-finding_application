package fr.hexaone.controller.Command;

import fr.hexaone.controller.Command.Command;

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
        l = new LinkedList<Command>();
    }

    /**
     * Ajoute une commande à la liste des commandes et réalise la commande
     * @param c La commande à ajouter.
     */
    public void add(Command c){
        i++;
        l.add(i,c);
        c.doCommand();
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
        //TODO : Check if there's a command to redo ?
        if(l.get(++i) != null)
            l.get(i).doCommand();
        else --i;
    }
}
