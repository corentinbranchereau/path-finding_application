package fr.hexaone;

/**
 * Classe qui permet simplement de lancer l'application d'une manière indirecte
 * afin de contourner les erreurs de dépendances
 */
public class LanceurApp {

    /**
     * Méthode main qui va simplement lancer à son tour la méthode main de la classe
     * App
     * 
     * @param args Les arguments passés lors du lancement
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
