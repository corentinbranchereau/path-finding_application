package fr.hexaone.model;

/**
 * Objet permettant de modéliser une requête, c'est à dire avec un point de
 * retrait (PICKUP) et un point de livraison (DELIVERY)
 *
 * @author HexaOne
 * @version 1.0
 */
public class Requete {
    /**
     * Id de l'intersection spéciale de type collecte
     */
    protected long idPickup;

    /**
     * Id l'intersection spéciale de type point de livraison
     */
    protected long idDelivery;

    /**
     * Constructeur de Requete
     *
     * @param idPickup
     * @param idDelivery
     */
    public Requete(long idPickup, long idDelivery) {
        this.idPickup = idPickup;
        this.idDelivery = idDelivery;
    }

}
