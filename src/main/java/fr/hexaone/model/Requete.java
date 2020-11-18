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
    protected IntersectionSpeciale collecte;

    /**
     * intersection spéciale de type point de livraison
     */
    protected IntersectionSpeciale livraison;

    /**
     * constructeur de Requete
     *
     * @param collecte
     * @param livraison
     */
    public Requete(IntersectionSpeciale collecte, IntersectionSpeciale livraison) {
        this.collecte = collecte;
        this.livraison = livraison;
    }

}
