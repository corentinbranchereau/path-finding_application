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
     * latitude et longitude de l'intersection spéciale
     */
    protected double latitude;
    protected double longitude;

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
    public IntersectionSpeciale(int id, List<Segment> segmentsArrivants, List<Segment> segmentsPartants,
            double latitude, double longitude, double duree, EnumIntersection typeIntersection) {
        super(id, segmentsArrivants, segmentsPartants);
        this.latitude = latitude;
        this.longitude = longitude;
        this.duree = duree;
        this.typeIntersection = typeIntersection;
    }

    /**
     * constructeur d'intersectionSpeciale
     *
     * @param latitude
     * @param longitude
     * @param id
     * @param duree
     * @param typeIntersection
     */
    public IntersectionSpeciale(int id, double latitude, double longitude, double duree,
            EnumIntersection typeIntersection) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
        this.duree = duree;
        this.typeIntersection = typeIntersection;
    }

}
