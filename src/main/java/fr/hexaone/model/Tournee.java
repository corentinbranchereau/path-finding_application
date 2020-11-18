package fr.hexaone.model;

import java.util.List;

/**
 * Objet modélisant une tournee"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Tournee {
    /**
     * pois temporel total d'une tournée
     */
    protected int poidsTotal;

    /**
     * liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * constructeur de Tournee
     *
     * @param poidsTotal
     * @param listeTrajets
     */
    public Tournee(int poidsTotal, List<Trajet> listeTrajets) {
        this.poidsTotal = poidsTotal;
        this.listeTrajets = listeTrajets;
    }

    /**
     * constructeur de Tournee
     *
     * @param listeTrajets
     */
    public Tournee(List<Trajet> listeTrajets) {
        this.listeTrajets = listeTrajets;
    }

}
