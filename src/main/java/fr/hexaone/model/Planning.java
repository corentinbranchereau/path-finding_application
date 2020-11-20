package fr.hexaone.model;

import java.util.*;

/**
 * Objet contenant toutes les informations relatives au planning d'une tournée "
 *
 * @author HexaOne
 * @version 1.0
 */
public class Planning {

    /**
     * duree totale de la duree
     */
    protected double dureeTotale;
    /**
     * date de début de la tournée
     */
    protected Date dateDebut;
    /**
     * liste des requêtes en rapport avec la demande client
     */
    protected List<Requete> requetes;
    /**
     * intersection spéciale de type dépôt
     */
    protected Intersection depot;
    /**
     * tournée relative au planning
     */
    protected Tournee tournee;
    /**
     * dates des passages des points spéciaux
     */
    protected Map<Intersection, Date> datesPassage;

    /**
     * constructeur de Planning
     *
     * @param dureeTotale
     * @param dateDebut
     * @param depot
     * @param tournee
     */
    public Planning(double dureeTotale, Date dateDebut, Intersection depot, Tournee tournee) {
        this.dureeTotale = dureeTotale;
        this.dateDebut = dateDebut;
        this.requetes = new ArrayList<>();
        this.depot = depot;
        this.tournee = tournee;
        this.datesPassage = new HashMap<>();
    }

    /**
     * constructeur de Planning
     *
     * @param dateDebut
     * @param depot
     */
    public Planning(Date dateDebut, Intersection depot) {
        this.dateDebut = dateDebut;
        this.requetes = new ArrayList<>();
        this.datesPassage = new HashMap<>();
        this.depot = depot;
    }

    /**
     * Constructeur par défaut de Planning
     */
    public Planning(){
        this.requetes = new ArrayList<>();
        this.datesPassage = new HashMap<>();
    }

    public List<Requete> getRequetes() {
        return this.requetes;
    }

    public Intersection getDepot() {
        return this.depot;
    }

    public void setDepot(Depot newDepot) {
        this.depot = newDepot;
    }

    public void setRequetes(List<Requete> newRequetes) {
        this.requetes = newRequetes;
    }
}
