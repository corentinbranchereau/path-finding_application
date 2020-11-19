package fr.hexaone.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * @param requetes
     * @param depot
     * @param tournee
     * @param datesPassage
     */
    public Planning(double dureeTotale, Date dateDebut, List<Requete> requetes, Intersection depot, Tournee tournee,
            Map<Intersection, Date> datesPassage) {
        this.dureeTotale = dureeTotale;
        this.dateDebut = dateDebut;
        this.requetes = requetes;
        this.depot = depot;
        this.tournee = tournee;
        this.datesPassage = datesPassage;
    }

    /**
     * constructeur de Planning
     *
     * @param dateDebut
     * @param requetes
     * @param depot
     */
    public Planning(Date dateDebut, List<Requete> requetes, Intersection depot) {
        this.dateDebut = dateDebut;
        this.requetes = requetes;
        this.depot = depot;
    }

    /**
     * constructeur par défaut de Planning
     */
    public Planning() {
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
