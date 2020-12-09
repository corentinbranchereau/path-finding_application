package fr.hexaone.model;

/**
 * Objet modélisant un segment"
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
     * ID de l'intersection de départ
     */
    private Long idDepart;

    /**
     * ID de l'intersection d'arrivée
     */
    private Long idArrivee;

    /**
     * Constructeur de Segment
     *
     * @param longueur La longueur du segment
     * @param nom Le nom du segment
     * @param idDepart L'id de départ
     * @param idArrivee L'id d'arrivée
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
