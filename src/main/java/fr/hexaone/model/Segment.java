package fr.hexaone.model;

/**
 * Objet modélisant un segment"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Segment {
    /**
     * longueur du segment
     */
    protected double longueur;

    /**
     * nom du segment
     */
    protected double nom;

    /**
     * intersection de départ
     */
    protected Intersection depart;
    /**
     * intersection d'arrivée
     */
    protected Intersection arrivee;

    /**
     * constructeur de Segment
     *
     * @param longueur
     * @param nom
     * @param depart
     * @param arrivee
     */
    public Segment(double longueur, double nom, Intersection depart, Intersection arrivee) {
        this.longueur = longueur;
        this.nom = nom;
        this.depart = depart;
        this.arrivee = arrivee;
    }

}
