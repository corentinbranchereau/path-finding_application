
package fr.hexaone.model;

import java.util.HashMap;
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
    protected Map<Long, Intersection> intersections;

    /**
     * Constructeur par défaut de Carte
     */
    public Carte() {
        intersections = new HashMap<>();
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

    /**
     * Getter
     * 
     * @return Les plus courts chemins
     */
    public List<Trajet> getCheminsLesPlusCourts() {
        return cheminsLesPlusCourts;
    }

    /**
     * Getter
     * 
     * @return Les intersections
     */
    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }
}
