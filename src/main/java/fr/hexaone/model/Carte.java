package fr.hexaone.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Objet contenant les structures de données relatives à la carte"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Carte {
	
    /**
     * Map permettant d'identifier les intersections à partir de leur id
     */
    protected Map<Long, Intersection> intersections;

    /**
     * Constructeur par défaut de Carte
     */
    public Carte() {
        intersections = new HashMap<>();
    }

    /**
     * Getter
     * @return
     */
    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Setter
     * @param intersections
     */
    public void setIntersections(Map<Long, Intersection> intersections) {
        this.intersections = intersections;
    }

}
