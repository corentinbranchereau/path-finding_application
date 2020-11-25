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

    public List<Trajet> getListeTrajets() {
        return listeTrajets;
    }

    public void setListeTrajets(List<Trajet> listeTrajets) {
        this.listeTrajets = listeTrajets;
    }

    public Map<Intersection, Date> getDatesPassage() {
        return datesPassage;
    }

    public void setDatesPassage(Map<Intersection, Date> datesPassage) {
        this.datesPassage = datesPassage;
    }

    public Map<Intersection, Date> getDatesSorties() {
        return datesSorties;
    }

    public void setDatesSorties(Map<Intersection, Date> datesSorties) {
        this.datesSorties = datesSorties;
    }

    public double getDureeTotale() {
        return dureeTotale;
    }

    public void setDureeTotale(double dureeTotale) {
        this.dureeTotale = dureeTotale;
    }

    /**
     * liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * Dates des passages des points spéciaux
     */
    protected Map<Intersection, Date> datesPassage;
    /**
     * Duree totale de la duree
     */

    protected Map<Intersection, Date> datesSorties;
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
        return this.dateDebut;
<<<<<<< HEAD
    }

    public Map<Intersection, Date> getDatesPassage() {
        return this.datesPassage;
=======
>>>>>>> ab77cfc11b40d00d40f325f397a8c653112ac1dd
    }

    public void setDateDebut(Date newDateDebut) {
        this.dateDebut = newDateDebut;
    }

    public void setRequetes(List<Requete> newRequetes) {
        this.requetes = newRequetes;
    }
}
