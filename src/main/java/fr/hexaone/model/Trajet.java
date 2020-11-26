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
    protected List<Segment> listeSegments;

    /**
     * poids temporel total du trajet
     */
    protected Double poids;

    /**
     * constructeur de Trajet
     *
     * @param listeSegments
     * @param poids
     */
    public Trajet(List<Segment> listeSegments, Double poids) {
        this.listeSegments = listeSegments;
        this.poids = poids;
    }

    /**
     * Getter
     * 
     * @return la liste des segments
     */
    public List<Segment> getListeSegments() {
        return listeSegments;
    }

    /**
     * Setter
     * 
     * @param listeSegments
     */
    public void setListeSegments(List<Segment> listeSegments) {
        this.listeSegments = listeSegments;
    }

    /**
     * Getter
     * 
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
