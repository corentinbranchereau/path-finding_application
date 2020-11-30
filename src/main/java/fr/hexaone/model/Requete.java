package fr.hexaone.model;

import javafx.scene.paint.Color;

/**
 * Objet permettant de modéliser une requête, c'est à dire avec un point de
 * retrait (PICKUP) et un point de livraison (DELIVERY)
 *
 * @author HexaOne
 * @version 1.0
 */
public class Requete {

    /**
     * 
     */
    protected Demande pickup;

    /**
     * 
     */
    protected Demande delivery;

    /**
     * 
     */
    protected Color couleur;
    
    /**
     * Constructeur de Requete
     *
     * @param idPickup
     * @param dureePickup
     * @param idDelivery
     * @param dureeDelivery
     */
    public Requete(long idPickup, int dureePickup, long idDelivery, int dureeDelivery) {
        // this.idPickup = idPickup;
        // this.dureePickup = dureePickup;
        // this.idDelivery = idDelivery;
        // this.dureeDelivery = dureeDelivery;
    }

    public Demande getPickup() {
        return pickup;
    }

    public void setPickup(Demande pickup) {
        this.pickup = pickup;
    }

    public Demande getDelivery() {
        return delivery;
    }

    public void setDelivery(Demande delivery) {
        this.delivery = delivery;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }


}
