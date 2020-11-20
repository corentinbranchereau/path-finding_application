package fr.hexaone.model;

/**
 * Objet permettant de modéliser une requête, c'est à dire avec un point de
 * retrait un point de livraison"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Requete {
    /**
     * intersection spécaiale de type collecte
     */
    protected long idPickup;

    /**
     * intersection spéciale de type point de livraison
     */
    protected long idDelivery;

    /**
     * constructeur de Requete
     *
     * @param idPickup
     * @param idDelivery
     */
    public Requete(long idPickup, long idDelivery) {
        this.idPickup = idPickup;
        this.idDelivery = idDelivery;
    }

}
