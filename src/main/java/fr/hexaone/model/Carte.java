package fr.hexaone.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Objet contenant les structures de données relatives à la carte
 *
 * @author HexaOne
 * @version 1.0
 */
public class Carte {

    /**
     * Map permettant d'identifier les intersections à partir de leur id
     */
    private Map<Long, Intersection> intersections;

    /**
     * Constructeur par défaut de Carte
     */
    public Carte() {
        intersections = new HashMap<>();
    }

    /**
     * Renvoie la valeur de la map identifiant les intersections à partir de leur id
     * 
     * @return La map d'intersections
     */
    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Change la valeur de la map identifiant les intersections à partir de leur id
     * 
     * @param intersections La nouvelle map contenant les ids et leur intersection
     *                      associée
     */
    public void setIntersections(Map<Long, Intersection> intersections) {
        this.intersections = intersections;
    }
}
