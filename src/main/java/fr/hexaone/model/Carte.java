
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
    
    public Boolean verifierPop(List<Intersection> chromosome, List<Requete> requetes) {

        for (int i = 0; i < requetes.size(); i++) {
            Boolean livraison = false;
            Boolean collecte = false;
            for (int j = 0; j < chromosome.size(); j++) {
                if (chromosome.get(j).id == requetes.get(0).collecte.id) {
                    collecte = true;
                    if (livraison == false) {
                        continue;
                    } else {
                        return false;
                    }
                }

                if (chromosome.get(j).id == requetes.get(0).livraison.id) {
                    livraison = true;

                    if (collecte == false) {
                        return false;
                    } else {
                        continue;
                    }
                }

            }
        }
        return true;
    }


    /**
     * Getter
     * @return Les plus courts chemins
     */
    public List<Trajet> getCheminsLesPlusCourts() {
        return cheminsLesPlusCourts;
    }

    /**
     * Getter
     * @return Les intersections
     */
    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }
}
