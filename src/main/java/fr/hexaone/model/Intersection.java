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
     * @param segmentsArrivants
     * @param segmentsPartants
     */
    public Intersection(int id, List<Segment> segmentsArrivants, List<Segment> segmentsPartants) {
        this.id = id;
        this.segmentsArrivants = segmentsArrivants;
        this.segmentsPartants = segmentsPartants;
    }

    /**
     * constructeur d'Intersection
     *
     * @param id
     */
    public Intersection(int id) {
        this.id = id;
    }
}
