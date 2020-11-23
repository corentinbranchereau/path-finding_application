package fr.hexaone.model;

import java.util.List;

/**
 * Objet modélisant un trajet
 *
 * @author HexaOne
 * @version 1.0
 */
public class Trajet {
    /**
     * liste de toutes les intersections représenant les chemins les plus courts
     */
    protected List<Intersection> listeIntersections;

    /**
     * poids temporel total du trajet
     */
    protected Double poids;

    /**
     * constructeur de Trajet
     *
     * @param listeIntersections
     * @param poids
     */
    public Trajet(List<Intersection> listeIntersections, Double poids) {
        this.listeIntersections = listeIntersections;
        this.poids = poids;
    }

    /**
     * Getter
     * @return la liste des intersections
     */
    public List<Intersection> getListeIntersections() {
        return listeIntersections;
    }

    /**
     * Setter
     * @param listeIntersections
     */
    public void setListeIntersections(List<Intersection> listeIntersections) {
        this.listeIntersections = listeIntersections;
    }

    /**
     * Getter
     * @return le poid total du trajet
     */
    public Double getPoids() {
        return poids;
    }

    /**
     * Setter
     */
    public void setPoids(Double poids) {
        this.poids = poids;
    }
    

}
