package fr.hexaone.model;

import java.util.List;

/**
 * Objet modélisant un trajet (suite de segments)
 *
 * @author HexaOne
 * @version 1.0
 */
public class Trajet {

    /**
     * Liste de tous les segments constituant le trajet
     */
    private List<Segment> listeSegments;

    /**
     * Poids temporel total du trajet
     */
    private Double poids;

    /**
     * Constructeur de Trajet
     *
     * @param listeSegments La liste des segments du trajet
     * @param poids         Le poids du trajet
     */
    public Trajet(List<Segment> listeSegments, Double poids) {
        this.listeSegments = listeSegments;
        this.poids = poids;
    }

    /**
     * @return La liste des segments du trajet
     */
    public List<Segment> getListeSegments() {
        return listeSegments;
    }

    /**
     * @return Le poids total du trajet
     */
    public Double getPoids() {
        return poids;
    }
}
