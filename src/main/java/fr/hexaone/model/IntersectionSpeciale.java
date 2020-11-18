package fr.hexaone.model;

import java.util.List;

/**
 * Spécialisation de la classe intersection"
 *
 * @author HexaOne
 * @version 1.0
 */
public class IntersectionSpeciale extends Intersection {
    /**
     * duréee de la tâche à l'intersection spéciale
     */

    protected double duree;

    /**
     * type de l'intersection
     */
    protected EnumIntersection typeIntersection;

    /**
     * constructeur d'intersectionSpeciale
     *
     * @param latitude
     * @param longitude
     * @param id
     * @param segmentsArrivants
     * @param segmentsPartants
     * @param duree
     * @param typeIntersection
     */
    public IntersectionSpeciale(double latitude, double longitude, int id, List<Segment> segmentsArrivants,
            List<Segment> segmentsPartants, double duree, EnumIntersection typeIntersection) {
        super(latitude, longitude, id, segmentsArrivants, segmentsPartants);
        this.duree = duree;
        this.typeIntersection = typeIntersection;
    }

}
