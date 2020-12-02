package fr.hexaone.model;

import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Demande {

    /**
     * attribut static qui permet d'incrémenter l'id unique de demande
     */
    private static Long increment = (long) 0;

    /**
     * le type d'intersection: COLLECTE, DELIVERY ou DEPOT
     */
    protected TypeIntersection typeIntersection;

    /**
     * ID unique de la demande
     */
    protected Long idDemande;

    /**
     * id de l'intersection où se situe
     */
    protected Long idIntersection;

    /**
     * propriété javafx symbolisant le nom de la rue associée à cette intersection
     */
    protected StringProperty nomIntersectionProperty;

    /**
     * propriété javafx de la date d'arrivée au point sous forme "08h35"
     */
    protected StringProperty dateArriveeProperty;

    /**
     * propriété javafx de la date de départ au point sous forme "08h35"
     */
    protected StringProperty dateDepartProperty;

    /**
     * Date d'arrivée au point
     */
    protected Date dateArrivee;

    /**
     * date de départ du point
     */
    protected Date dateDepart;

    /**
     * Durée passée au point
     */
    protected Integer duree;

    /**
     * Requête associée à cette demande
     */
    protected Requete requete;

    /**
     * Constructeur de demande
     * 
     * @param type
     * @param idIntersection
     * @param nomIntersection
     * @param duree
     * @param requete
     */
    public Demande(TypeIntersection type, Long idIntersection, String nomIntersection, Integer duree, Requete requete) {
        this.idDemande = increment;
        increment++;
        this.idIntersection = idIntersection;
        this.typeIntersection = type;
        this.duree = duree;
        this.requete = requete;
        this.dateArriveeProperty = null;
        this.dateDepartProperty = null;
        this.dateArrivee = null;
        this.dateDepart = null;
        this.nomIntersectionProperty = new SimpleStringProperty(nomIntersection);

    }

    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * getter nomIntersection
     * 
     * @return StringProperty nomIntersectionProperty
     */
    public StringProperty getNomIntersectionProperty() {
        return nomIntersectionProperty;
    }

    /**
     * getter
     * 
     * @return Stringproperty
     */
    public StringProperty getDateArriveeProperty() {
        return dateArriveeProperty;
    }

    /**
     * getter
     * 
     * @return Stringproperty
     */
    public StringProperty getDateDepartProperty() {
        return dateDepartProperty;
    }

    /**
     * getter du type de demande
     * 
     * @return renvoie le type formaté avec l'icone correspondant
     */
    public StringProperty getTypeProperty() {
        if (typeIntersection == TypeIntersection.DEPOT)
            return new SimpleStringProperty("★ dépot");
        if (typeIntersection == TypeIntersection.COLLECTE)
            return new SimpleStringProperty("■ collecte");
        if (typeIntersection == TypeIntersection.LIVRAISON)
            return new SimpleStringProperty("● livraison");
        return new SimpleStringProperty("?");
    }

    /**
     * getter
     * 
     * @return String
     */
    public String getNomIntersection() {
        return nomIntersectionProperty.get();
    }

    /**
     * getter
     * 
     * @return duree sous forme StringProperty
     */
    public StringProperty getDureeProperty() {
        return new SimpleStringProperty(duree + "s");
    }

    /**
     * getter
     * 
     * @return Date
     */
    public Date getDateDepart() {
        return dateDepart;
    }

    /**
     * getter
     * 
     * @return Date
     */
    public Date getDateArrivee() {
        return dateArrivee;
    }

    /**
     * getter
     * 
     */
    public Requete getRequete() {
        return requete;
    }

    /**
     * getter
     * 
     * @return Integer
     */
    public Integer getDuree() {
        return duree;
    }

    /**
     * setter
     * 
     * @return Integer
     */
    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    /**
     * getter
     * 
     * @return Long idDemande
     */
    public Long getIdDemande() {
        return idDemande;
    }

    /**
     * getter
     * 
     * @return Long
     */
    public Long getIdIntersection() {
        return idIntersection;
    }

    /**
     * setter de Date datedépart et de StringProperty dateDépartProperty
     * 
     * @param date la date au format Date
     */
    public void setDateDepart(Date date) {
        this.dateDepart = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = cal.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);
        this.dateDepartProperty = new SimpleStringProperty(heureString + "h" + minutesString);
    }

    /**
     * setter de Date datearrivee et de StringProperty dateArriveeProperty
     * 
     * @param date la date au format Date
     */
    public void setDateArrivee(Date date) {
        this.dateArrivee = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = cal.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);
        this.dateArriveeProperty = new SimpleStringProperty(heureString + "h" + minutesString);
    }

    /**
     * setter
     * 
     * @param idIntersection
     */
    public void setIdIntersection(Long idIntersection) {
        this.idIntersection = idIntersection;
    }

    /**
     * Renvoie le type de l'intersection
     * 
     * @return Le type de l'intersection
     */
    public TypeIntersection getTypeIntersection() {
        return typeIntersection;
    }

    public void setTypeIntersection(TypeIntersection typeIntersection) {
        this.typeIntersection = typeIntersection;
    }
}
