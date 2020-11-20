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
     * L'id du Dépôt associé au planning
     */
    protected Long idDepot;
    /**
     * Date de début de la tournée
     */
    protected Date dateDebut;
    /**
     * Liste des requêtes en rapport avec la demande client
     */
    protected List<Requete> requetes;
    /**
     * Tournée relative au planning
     */
    protected Tournee tournee;
    /**
     * Dates des passages des points spéciaux
     */
    protected Map<Intersection, Date> datesPassage;
    /**
     * Duree totale de la duree
     */
    protected double dureeTotale;

    /**
     * Constructeur de Planning
     *
     * @param idDepot
     * @param dateDebut
     * @param requetes
     */
    public Planning(Long idDepot, Date dateDebut, List<Requete> requetes) {
        this.idDepot = idDepot;
        this.dateDebut = dateDebut;
        this.requetes = requetes;
        this.datesPassage = new HashMap<>();
    }

    /**
     * Constructeur par défaut de Planning
     */
    public Planning() {
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
