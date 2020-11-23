package fr.hexaone.model;

import java.util.List;

/**
 * Objet modélisant un trajet
 *
 * @author HexaOne
 * @version 1.0
 */
public class Trajet {
    /**
     * liste de toutes les intersections représenant les chemins les plus courts
     */
    protected List<Intersection> listeIntersections;

    /**
     * poids temporel total du trajet
     */
    protected Double poids;

    /**
     * constructeur de Trajet
     *
     * @param listeIntersections
     * @param poids
     */
    public Trajet(List<Intersection> listeIntersections, Double poids) {
        this.listeIntersections = listeIntersections;
        this.poids = poids;
    }

}
