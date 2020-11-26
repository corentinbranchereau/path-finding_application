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
     * Durée du pickup
     */
    protected Integer dureePickup;

    /**
     * Id l'intersection spéciale de type point de livraison
     */
    protected long idDelivery;

    /**
     * Durée du delivery
     */
    protected Integer dureeDelivery;

    /**
     * Id l'intersection spéciale de type point de livraison
     */
    protected long idUniquePickup;

    /**
     * Id l'intersection spéciale de type point de livraison
     */
    protected long idUniqueDelivery;

    /**
     * Constructeur de Requete
     *
     * @param idPickup
     * @param dureePickup
     * @param idDelivery
     * @param dureeDelivery
     */
    public Requete(long idPickup, int dureePickup, long idDelivery, int dureeDelivery) {
        this.idPickup = idPickup;
        this.dureePickup = dureePickup;
        this.idDelivery = idDelivery;
        this.dureeDelivery = dureeDelivery;
    }

    /**
     * Getter
     * 
     * @return L'id de l'intersection de Pickup
     */
    public long getIdPickup() {
        return idPickup;
    }

    /**
     * Getter
     * 
     * @return Durée du pickup
     */
    public Integer getDureePickup() {
        return dureePickup;
    }

    /**
     * Getter
     * 
     * @return L'id de l'intersection de delivery
     */
    public long getIdDelivery() {
        return idDelivery;
    }

    /**
     * Getter
     * 
     * @return Durée du delivery
     */
    public Integer getDureeDelivery() {
        return dureeDelivery;
    }

    /**
     * Getter
     */
    public long getIdUniquePickup() {
        return idUniquePickup;
    }

    /**
     * Setter
     */
    public void setIdUniquePickup(long idUniquePickup) {
        this.idUniquePickup = idUniquePickup;
    }

    /**
     * Getter
     */
    public long getIdUniqueDelivery() {
        return idUniqueDelivery;
    }

    /**
     * Setter
     */
    public void setIdUniqueDelivery(long idUniqueDelivery) {
        this.idUniqueDelivery = idUniqueDelivery;
    }

}
