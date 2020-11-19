package fr.hexaone.model;

import java.util.List;

/**
 * Objet contenant les structures de données relatives à une intersection"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Intersection {

    /**
     * identifiant unique
     */
    protected int id;

    /**
     * latitude et longitude de l'intersection
     */
    protected double latitude;
    protected double longitude;

    /**
     * liste des segments arrivants sur l'intersection : utile pour le calcul de
     * tournée
     */

    protected List<Segment> segmentsArrivants;

    /**
     * liste des segments partants depuis l'intersection : utile pour le calcul de
     * tournée
     */

    protected List<Segment> segmentsPartants;

    /**
     * constructeur d'Intersection
     *
     * @param id
     * @param latitude
     * @param longitude
     * @param segmentsArrivants
     * @param segmentsPartants
     */
    public Intersection(int id, double latitude, double longitude, List<Segment> segmentsArrivants,
            List<Segment> segmentsPartants) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.segmentsArrivants = segmentsArrivants;
        this.segmentsPartants = segmentsPartants;
    }

    /**
     * constructeur d'Intersection
     *
     * @param id
     * @param latitude
     * @param longitude
     */
    public Intersection(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
