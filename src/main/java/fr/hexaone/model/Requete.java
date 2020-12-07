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
     * Demande associée à la collecte
     */
    private Demande demandeCollecte;

    /**
     * Demande associée à la livraison
     */
    private Demande demandeLivraison;

    /**
     * Couleur utilisée
     */
    private Color couleur;

    /**
     * Constructeur de Requete
     *
     * @param idPickup
     * @param dureePickup
     * @param idDelivery
     * @param dureeDelivery
     */
    public Requete(long idPickup, int dureePickup, String nomPickup, long idDelivery, int dureeDelivery,
            String nomDelivery) {
        demandeCollecte = new Demande(TypeIntersection.COLLECTE, idPickup, nomPickup, dureePickup, this);
        demandeLivraison = new Demande(TypeIntersection.LIVRAISON, idDelivery, nomDelivery, dureeDelivery, this);
    }

    /**
     * Constructeur de Requete pour une unique demande
     *
     * @param id
     * @param duree
     * @param nom
     * @param typeIntersection
     */
    public Requete(long id, int duree, String nom, TypeIntersection typeIntersection) {
        if(typeIntersection==TypeIntersection.COLLECTE){
            demandeCollecte = new Demande(TypeIntersection.COLLECTE, id, nom, duree, this);
        } else if (typeIntersection==TypeIntersection.LIVRAISON){
            demandeLivraison = new Demande(TypeIntersection.LIVRAISON, id, nom, duree, this);
        }
    }

    /**
     * Getter
     * 
     * @return La demande de collecte
     */
    public Demande getDemandeCollecte() {
        return demandeCollecte;
    }

    /**
     * Setter
     * 
     * @param demandeCollecte La demande de collecte
     */
    public void setDemandeCollecte(Demande demandeCollecte) {
        this.demandeCollecte = demandeCollecte;
    }

    /**
     * Getter
     * 
     * @return La demande de livraison
     */
    public Demande getDemandeLivraison() {
        return demandeLivraison;
    }

    /**
     * Setter
     * 
     * @param demandeLivraison La demande de livraison
     */
    public void setDemandeLivraison(Demande demandeLivraison) {
        this.demandeLivraison = demandeLivraison;
    }
}
