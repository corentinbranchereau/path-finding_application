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
    private double longueur;

    /**
     * nom du segment
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
     * constructeur de Segment
     *
     * @param longueur
     * @param nom
     * @param idDepart
     * @param idArrivee
     */
    public Segment(double longueur, String nom, long idDepart, long idArrivee) {
        this.longueur = longueur;
        this.nom = nom;
        this.idDepart = idDepart;
        this.idArrivee = idArrivee;
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
     * @return L'id de l'intersection au départ du segment
     */
    public long getDepart() {
        return idDepart;
    }

    /**
     * Getter
     * @return L'id de l'intersection à l'arrivée du segment
     */
    public long getArrivee() {
        return idArrivee;
    }
}
