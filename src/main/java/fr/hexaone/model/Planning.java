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
     * Date de début de la tournée. 24 hours format - H:m:s
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
     * Constructeur par défaut de Planning
     */
    public Planning() {
        this.requetes = new ArrayList<>();
        this.datesPassage = new HashMap<>();
    }

    public List<Requete> getRequetes() {
        return this.requetes;
    }

    public Long getIdDepot() {
        return this.idDepot;
    }

    public void setIdDepot(Long newIdDepot) {
        this.idDepot = newIdDepot;
    }

    public Date getDateDebut() {
        return this.getDateDebut();
    }

    public void setDateDebut(Date newDateDebut) {
        this.dateDebut = newDateDebut;
    }

    public void setRequetes(List<Requete> newRequetes) {
        this.requetes = newRequetes;
    }
}
