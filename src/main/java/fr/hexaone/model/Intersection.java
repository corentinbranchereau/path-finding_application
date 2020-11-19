package fr.hexaone.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    protected long id;

    /**
     * latitude de l'intersection
     */
    protected double latitude;

    /**
     * longitude de l'intersection
     */
    protected double longitude;

    /**
     * Set des segments arrivants sur l'intersection : utile pour le calcul de
     * tournée
     */
    protected Set<Segment> segmentsArrivants;

    /**
     * Set des segments partants depuis l'intersection : utile pour le calcul de
     * tournée
     */
    protected Set<Segment> segmentsPartants;

    /**
     * constructeur d'Intersection
     *
     * @param id
     * @param latitude
     * @param longitude
     */
    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.segmentsArrivants = new HashSet<>();
        this.segmentsPartants = new HashSet<>();
    }

    /**
     * Getter
     * @return La latitude de l'intersection
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter
     * @return La longitude de l'intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter
     * @return Le set des segments arrivant sur l'intersection
     */
    public Set<Segment> getSegmentsArrivants() {
        return segmentsArrivants;
    }

    /**
     * Getter
     * @return Le set des segments partants sur l'intersection
     */
    public Set<Segment> getSegmentsPartants() {
        return segmentsPartants;
    }
}
