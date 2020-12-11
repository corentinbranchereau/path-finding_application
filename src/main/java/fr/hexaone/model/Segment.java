package fr.hexaone.model;

/**
 * Objet modélisant un segment (portion de route atomique reliant 2
 * intersections)
 *
 * @author HexaOne
 * @version 1.0
 */
public class Segment {

    /**
     * Longueur du segment
     */
    private double longueur;

    /**
     * Nom du segment
     */
    private String nom;

    /**
     * Id de l'intersection de départ
     */
    private Long idDepart;

    /**
     * Id de l'intersection d'arrivée
     */
    private Long idArrivee;

    /**
     * Constructeur de Segment
     *
     * @param longueur  La longueur du segment
     * @param nom       Le nom du segment
     * @param idDepart  L'id de départ
     * @param idArrivee L'id d'arrivée
     */
    public Segment(double longueur, String nom, long idDepart, long idArrivee) {
        this.longueur = longueur;
        this.nom = nom;
        this.idDepart = idDepart;
        this.idArrivee = idArrivee;
    }

    /**
     * @return La longeur du segment
     */
    public double getLongueur() {
        return longueur;
    }

    /**
     * @return Le nom du segment
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return L'id de l'intersection au départ du segment
     */
    public long getDepart() {
        return idDepart;
    }

    /**
     * @return L'id de l'intersection à l'arrivée du segment
     */
    public long getArrivee() {
        return idArrivee;
    }
}
