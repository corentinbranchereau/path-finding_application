package fr.hexaone.controller.Command;

import java.util.LinkedList;

/**
 * Liste des commandes du design pattern Command afin de gérer l'undo/redo
 *
 * @author HexaOne
 * @version 1.0
 */
public class ListOfCommands {

    /**
     * Liste des commandes
     */
    private LinkedList<Command> listeCommandes;

    /**
     * Index privé permettant de gérer l'ordres des commandes dans la liste des
     * commandes.
     */
    private int i;

    /**
     * Constructeur de ListOfCommands. Instancie l'index des commandes à -1 ainsi
     * que la liste vide de commandes.
     */
    public ListOfCommands() {
        i = -1;
        listeCommandes = new LinkedList<>();
    }

    /**
     * Ajoute une commande à la liste des commandes et réalise la commande.
     * 
     * @param commande La commande à ajouter.
     * @return True si on a pu ajouter la commande, false sinon.
     */
    public boolean ajouterCommande(Command commande) {
        i++;
        if (i < listeCommandes.size()) {
            int taille = listeCommandes.size();
            for (int j = i; j < taille; j++) {
                listeCommandes.remove(i);
            }
        }
        listeCommandes.add(commande);
        if (!commande.doCommand()) {
            listeCommandes.remove(i);
            i--;
            return false;
        }
        return true;
    }

    /**
     * Annule la dernière commande réalisée, si elle existe.
     */
    public void undo() {
        if (i >= 0) {
            listeCommandes.get(i).undoCommand();
            i--;
        }
    }

    /**
     * Rétablir la dernière commande réalisée, si elle existe.
     */
    public void redo() {
        if (i + 1 <= listeCommandes.size() - 1) {
            listeCommandes.get(++i).doCommand();
        }
    }

    /**
     * Réinitialiser toutes les commandes.
     */
    public void reinitialiserCommandes() {
        listeCommandes.clear();
        i = -1;
    }

}
