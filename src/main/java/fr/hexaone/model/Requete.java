package fr.hexaone.model;

/**
 * Objet permettant de modéliser une requête, c'est à dire un point de collecte
 * et un point de livraison.
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
     * Constructeur de Requete
     *
     * @param idCollecte     L'id de l'intersection de la collecte
     * @param dureeCollecte  La durée de la collecte
     * @param nomCollecte    Le nom de l'intersection de la collecte
     * @param idLivraison    L'id de l'intersection de la livraison
     * @param dureeLivraison La durée de la livraison
     * @param nomLivraison   Le nom de l'intersection de la livraison
     */
    public Requete(long idCollecte, int dureeCollecte, String nomCollecte, long idLivraison, int dureeLivraison,
            String nomLivraison) {
        demandeCollecte = new Demande(TypeDemande.COLLECTE, idCollecte, nomCollecte, dureeCollecte, this);
        demandeLivraison = new Demande(TypeDemande.LIVRAISON, idLivraison, nomLivraison, dureeLivraison, this);
    }

    /**
     * Constructeur de Requete pour une unique demande
     *
     * @param idIntersection L'id de l'intersection de la demande
     * @param duree          La durée de la demande
     * @param nom            Le nom de l'intersection de la demande
     * @param typeDemande    Le type de la demande
     */
    public Requete(long idIntersection, int duree, String nom, TypeDemande typeDemande) {
        if (typeDemande == TypeDemande.COLLECTE) {
            demandeCollecte = new Demande(TypeDemande.COLLECTE, idIntersection, nom, duree, this);
        } else if (typeDemande == TypeDemande.LIVRAISON) {
            demandeLivraison = new Demande(TypeDemande.LIVRAISON, idIntersection, nom, duree, this);
        }
    }

    /**
     * @return La demande de collecte
     */
    public Demande getDemandeCollecte() {
        return demandeCollecte;
    }

    /**
     * Change la demande de collecte
     * 
     * @param demandeCollecte La nouvelle demande de collecte
     */
    public void setDemandeCollecte(Demande demandeCollecte) {
        this.demandeCollecte = demandeCollecte;
    }

    /**
     * @return La demande de livraison
     */
    public Demande getDemandeLivraison() {
        return demandeLivraison;
    }

    /**
     * Change la demande de livraison
     * 
     * @param demandeLivraison La nouvelle demande de livraison
     */
    public void setDemandeLivraison(Demande demandeLivraison) {
        this.demandeLivraison = demandeLivraison;
    }
}
