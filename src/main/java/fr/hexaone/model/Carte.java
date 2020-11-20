
package fr.hexaone.model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;
import org.javatuples.Pair;


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
     * Renvoie true si toutes les contraintes de précédences sont respectées, faux sinon
     * @param chromosome que l'on teste
     * @param requetes liste des requêtes
     */

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
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart en termes de couts
     * @param population à tester
     * @param ecart maximum
     */

    public Boolean espacePopulation(List<Pair<List<Intersection>, Double>> population, double ecart) {

        for (int i = 1; i < population.size(); i++) {

            if (population.get(i).getValue1() - population.get(i - 1).getValue1() < ecart) {
                return false;
            }

        }
        return true;

    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart en termes de couts avec l'ajout de l'enfant
     * @param population à tester
     * @param ecart maximum
     * @param valeurEnfant
     */

    public Boolean espacePopulation(List<Pair<List<Intersection>, Double>> population, double ecart, double valeurEnfant) {

        for (int i = 0; i < population.size(); i++) {

            if (Math.abs(population.get(i).getValue1() - valeurEnfant) > ecart) {
                return false;
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
