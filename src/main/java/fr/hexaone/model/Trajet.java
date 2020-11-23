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
     * liste de tous les segments représenant les chemins les plus courts
     */
    protected List<Segment> listeSegments;

    /**
     * poids temporel total du trajet
     */
    protected int poids;

    /**
     * constructeur de Trajet
     *
     * @param listeSegments
     * @param poids
     */
    public Trajet(List<Segment> listeSegments, int poids) {
        this.listeSegments = listeSegments;
        this.poids = poids;
    }

    public List<Segment> getListeSegments() {
        return listeSegments;
    }
}
