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
    private List<Segment> listeSegments;

    /**
     * pPids temporel total du trajet
     */
    private Double poids;

    /**
     * constructeur de Trajet
     *
     * @param listeSegments La liste de segments
     * @param poids Le poids du trajet
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
     * Getter
     * 
     * @return le poid total du trajet
     */
    public Double getPoids() {
        return poids;
    }


}
