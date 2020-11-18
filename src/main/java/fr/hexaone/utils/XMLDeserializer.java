package fr.hexaone.utils;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Carte;

/**
 * Permet de déserialiser un fichier au format XML.
 * Classe statique.
 * @author HexaOne
 * @version 1.0
 */

import java.io.File;

public class XMLDeserializer {

    /**
     * Charge les données d'un fichier XML bien formé contenant des intersections et des segments formant la carte
     * @param carte La carte où charger les données.
     * @param file Le fichier XML bien formé contenant les données.
     */
    public static void loadMap(Carte carte, File file){
        //TODO
    }

    /**
     * Charge les données d'un fichier XML bien formé contenant les requêtes formant le planning
     * @param planning Le planning où charger les données.
     * @param file Le fichier XML bien formé contenant les données.
     */
    public static void loadRequete(Planning planning, File file){
        //TODO
    }

}
