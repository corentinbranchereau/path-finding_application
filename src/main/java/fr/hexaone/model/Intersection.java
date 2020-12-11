package fr.hexaone.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Objet contenant les structures de données relatives à une intersection
 *
 * @author HexaOne
 * @version 1.0
 */
public class Intersection {

    /**
     * Identifiant unique de l'intersection
     */
    private long id;

    /**
     * Latitude de l'intersection
     */
    private double latitude;

    /**
     * Longitude de l'intersection
     */
    private double longitude;

    /**
     * Set des segments arrivants sur l'intersection
     */
    private Set<Segment> segmentsArrivants;

    /**
     * Set des segments partants depuis l'intersection
     */
    private Set<Segment> segmentsPartants;

    /**
     * Liste des segments pour les calculs des chemins les plus courts
     */
    private List<Segment> cheminLePlusCourt = new LinkedList<>();

    /**
     * Distance entre l'intersection source et ce point. Utilisé pour le calcul des
     * chemins les plus courts
     */
    private Double distance = Double.MAX_VALUE;

    /**
     * Constructeur d'Intersection
     *
     * @param id        L'id de l'intersection
     * @param latitude  La latitude de l'intersection
     * @param longitude La longitude de l'intersection
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
     * @return L'identifiant de l'intersection
     */
    public long getId() {
        return id;
    }

    /**
     * @return La latitude de l'intersection
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return La longitude de l'intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return Le set des segments arrivant sur l'intersection
     */
    public Set<Segment> getSegmentsArrivants() {
        return segmentsArrivants;
    }

    /**
     * @return Le set des segments partants depuis l'intersection
     */
    public Set<Segment> getSegmentsPartants() {
        return segmentsPartants;
    }

    /**
     * Change la valeur du set des segments partants depuis l'intersection
     * 
     * @param segmentsPartants Le nouveau set des segments partants
     */
    public void setSegmentsPartants(Set<Segment> segmentsPartants) {
        this.segmentsPartants = segmentsPartants;
    }

    /**
     * @return La liste des segments du chemin le plus court
     */
    public List<Segment> getCheminLePlusCourt() {
        return cheminLePlusCourt;
    }

    /**
     * Change la valeur de la liste des segments du chemin le plus court
     * 
     * @param cheminLePlusCourt Le nouveau chemin le plus court
     */
    public void setCheminLePlusCourt(List<Segment> cheminLePlusCourt) {
        this.cheminLePlusCourt = cheminLePlusCourt;
    }

    /**
     * @return La distance à la source pendant le calcul du chemin le plus court.
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Change la valeur de la distance à la source
     * 
     * @param distance La nouvelle distance à la source
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * Remet à zero les variables utilisées pour calculer les chemins les plus
     * courts.
     */
    public void resetIntersection() {
        distance = Double.MAX_VALUE;
        cheminLePlusCourt = new LinkedList<>();
    }
}
