
package fr.hexaone.model;

import java.util.List;
import java.util.Map;

/**
 * Objet contenant les structures de données relatives à la carte"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Carte {

    protected List<Trajet> cheminsLesPlusCourts;
    protected Map<Integer, Intersection> intersections;

    /**
     * constructeur de Carte
     *
     * @param intersections
     */
    public Carte(Map<Integer, Intersection> intersections) {
        this.intersections = intersections;
    }

    /**
     * constructeur par défaut de Carte
     */
    public Carte() {
    }

    /**
     * Créer une intersection
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public Intersection creerIntersection(double longitude, double latitude) {

        return null;
        // TODO
    }

    /**
     * Créer une intersection spéciale
     *
     * @param longitude
     * @param latitude
     * @param duree
     * @param enumIntersection
     * @return
     */
    public IntersectionSpeciale creerIntersectionSpeciale(double longitude, double latitude, double duree,
            EnumIntersection enumIntersection) {
        return null;
        // TODO

    }

    /**
     * Calculer une tournée et l'inscrit dans le planning
     *
     * @param planning
     * @return
     */
    public Planning calculerTournee(Planning planning) {
        // TODO
        return planning;

    }

    private void calculerLesCheminsLesPlusCourts() {
        // TODO

    }

    private void trouverMeilleureTournee() {
        // TODO

    }

}
