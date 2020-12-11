package fr.hexaone.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe qui représente une demande (collecte ou livraison) d'un client au sein
 * d'une requête (combinaison de 2 demandes)
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Demande {

    /**
     * Attribut static qui permet d'incrémenter l'id unique de demande
     */
    private static Long increment = 0L;

    /**
     * Le type de demande : COLLECTE ou LIVRAISON
     */
    private TypeDemande typeDemande;

    /**
     * Id unique de la demande
     */
    private Long idDemande;

    /**
     * Id de l'intersection où se situe la demande
     */
    private Long idIntersection;

    /**
     * Propriété JavaFX symbolisant le nom de la rue associée à cette intersection
     */
    private StringProperty proprieteNomIntersection;

    /**
     * Propriété JavaFX de la date d'arrivée au point sous la forme "08h35"
     */
    private StringProperty proprieteDateArrivee;

    /**
     * Propriété JavaFX de la date de départ du point sous la forme "08h35"
     */
    private StringProperty proprieteDateDepart;

    /**
     * Propriété JavaFX indiquant si la demande est orpheline (cela signife pour une
     * demande de collecte qu'elle n'a pas de demande de livraison associée, et
     * inversement)
     */
    private StringProperty proprieteOrpheline;

    /**
     * Date d'arrivée au point
     */
    private Date dateArrivee;

    /**
     * Date de départ du point
     */
    private Date dateDepart;

    /**
     * Durée passée au point
     */
    private Integer duree;

    /**
     * Requête associée à cette demande
     */
    private Requete requete;

    /**
     * Constructeur de demande
     * 
     * @param type            Le type de demande
     * @param idIntersection  L'id de l'intersection où se situe la demande
     * @param nomIntersection Le nom de l'intersection où se situe la demande
     * @param duree           La durée de la demande
     * @param requete         La requête liée
     */
    public Demande(TypeDemande type, Long idIntersection, String nomIntersection, Integer duree, Requete requete) {
        this.idDemande = increment;
        increment++;
        this.idIntersection = idIntersection;
        this.typeDemande = type;
        this.duree = duree;
        this.requete = requete;
        this.proprieteDateArrivee = null;
        this.proprieteDateDepart = null;
        this.dateArrivee = null;
        this.dateDepart = null;
        this.proprieteNomIntersection = new SimpleStringProperty(nomIntersection);
    }

    /**
     * @return La propriété du nom de l'intersection
     */
    public StringProperty getProprieteNomIntersection() {
        return proprieteNomIntersection;
    }

    /**
     * @return La propriété de la date d'arrivée à demande
     */
    public StringProperty getProprieteDateArrivee() {
        return proprieteDateArrivee;
    }

    /**
     * @return La propriété de la date de départ de la demande
     */
    public StringProperty getProprieteDateDepart() {
        return proprieteDateDepart;
    }

    /**
     * @return Le type de la demande, formaté avec l'icone correspondante
     */
    public StringProperty getProprieteType() {
        if (typeDemande == TypeDemande.COLLECTE)
            return new SimpleStringProperty("■ Collecte");
        if (typeDemande == TypeDemande.LIVRAISON)
            return new SimpleStringProperty("● Livraison");
        return new SimpleStringProperty("?");
    }

    /**
     * @return La durée passée sur la demande sous forme de StringProperty
     */
    public StringProperty getProprieteDuree() {
        return new SimpleStringProperty(duree + "s");
    }

    /**
     * @return La date de départ de la demande
     */
    public Date getDateDepart() {
        return dateDepart;
    }

    /**
     * @return La date d'arrivée à la demande
     */
    public Date getDateArrivee() {
        return dateArrivee;
    }

    /**
     * @return La requête associée à la demande
     */
    public Requete getRequete() {
        return requete;
    }

    /**
     * @return La durée passée sur la demande
     */
    public Integer getDuree() {
        return duree;
    }

    /**
     * Change la valeur de la durée passée sur la demande
     * 
     * @param duree La nouvelle durée
     */
    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    /**
     * @return L'id de la demande
     */
    public Long getIdDemande() {
        return idDemande;
    }

    /**
     * @return L'id de l'intersection où se trouve la demande
     */
    public Long getIdIntersection() {
        return idIntersection;
    }

    /**
     * Change la valeur de la date de départ et de la propriété associée
     * 
     * @param date La nouvelle date au format Date
     */
    public void setDateDepart(Date date) {
        this.dateDepart = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = cal.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);
        this.proprieteDateDepart = new SimpleStringProperty(heureString + "h" + minutesString);
    }

    /**
     * Change la valeur de la date d'arrivée et de la propriété associée
     * 
     * @param date La nouvelle date au format Date
     */
    public void setDateArrivee(Date date) {
        this.dateArrivee = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = cal.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);
        this.proprieteDateArrivee = new SimpleStringProperty(heureString + "h" + minutesString);
    }

    /**
     * Change la valeur de l'id de l'intersection où se situe la demande
     * 
     * @param idIntersection Le nouvel id de l'intersection
     */
    public void setIdIntersection(Long idIntersection) {
        this.idIntersection = idIntersection;
    }

    /**
     * @return Le type de la demande
     */
    public TypeDemande getTypeDemande() {
        return typeDemande;
    }

    /**
     * @return La propriété orpheline
     */
    public StringProperty getProprieteOrpheline() {
        if (this.typeDemande == TypeDemande.COLLECTE) {
            this.proprieteOrpheline = requete.getDemandeLivraison() == null ? new SimpleStringProperty("⚠")
                    : new SimpleStringProperty();
        } else {
            this.proprieteOrpheline = requete.getDemandeCollecte() == null ? new SimpleStringProperty("⚠")
                    : new SimpleStringProperty();
        }
        return proprieteOrpheline;
    }

    /**
     * Change la valeur de la propriété du nom de l'intersection
     * 
     * @param proprieteNomIntersection Le nouveau nom de l'intersection
     */
    public void setProprieteNomIntersection(StringProperty proprieteNomIntersection) {
        this.proprieteNomIntersection = proprieteNomIntersection;
    }
}
