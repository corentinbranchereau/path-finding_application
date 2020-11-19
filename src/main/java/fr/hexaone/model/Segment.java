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
    protected String nom;

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
    public Segment(double longueur, String nom, Intersection depart, Intersection arrivee) {
        this.longueur = longueur;
        this.nom = nom;
        this.depart = depart;
        this.arrivee = arrivee;
    }

    /**
     * Getter
     * @return La longeur du segment
     */
    public double getLongueur() {
        return longueur;
    }

    /**
     * Getter
     * @return Le nom du segment
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter
     * @return L'intersection au départ du segment
     */
    public Intersection getDepart() {
        return depart;
    }

    /**
     * Getter
     * @return L'intersection à l'arrivée du segment
     */
    public Intersection getArrivee() {
        return arrivee;
    }
}
