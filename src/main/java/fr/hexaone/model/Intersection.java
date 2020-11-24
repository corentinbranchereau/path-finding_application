package fr.hexaone.model;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
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
     * Liste des segments pour les calculs de chemins les plus courts
     */
    private List<Segment> cheminLePlusCourt = new LinkedList<Segment>();
    
    /**
     * Distance entre l'intersection source et ce point. Utilisé pour le calcul
     * de chemins les plus courts
     */
    private Double distance = Double.MAX_VALUE;

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
        this.distance = Double.MAX_VALUE;
    }
    
    /**
     * Getter
     * @return l'identifiant
     */
    public long getId() {
        return id;
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

    /**
     * Setter
     */
    public void setSegmentsPartants(Set<Segment> segmentsPartants) {
        this.segmentsPartants = segmentsPartants;
    }

    /**
     * Getter
     * @return les intersections du chemin le plus court 
     * pendant le calcul du chemin le plus court
     */
    public List<Segment> getCheminLePlusCourt() {
        return cheminLePlusCourt;
    }

    /**
     * Setter
     */
    public void setCheminLePlusCourt(List<Segment> cheminLePlusCourt) {
        this.cheminLePlusCourt = cheminLePlusCourt;
    }

    /**
     * Getter
     * @return la distance à la source pendant le calcul du
     * chemin le plus court 
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Setter
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    /**
     * Remet à zero les variables utilisées pour calculer les 
     * chemins les plus courts.
     */
    public void resetIntersection() {
        distance = Double.MAX_VALUE;
        cheminLePlusCourt = new LinkedList<Segment>();
    }



}
