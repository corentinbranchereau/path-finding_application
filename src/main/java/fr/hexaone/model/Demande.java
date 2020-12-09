package fr.hexaone.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Calendar;
import java.util.Date;

public class Demande {

    /**
     * Attribut static qui permet d'incrémenter l'id unique de demande
     */
    private static Long increment = (long) 0;

    /**
     * Le type d'intersection: COLLECTE, DELIVERY ou DEPOT
     */
    private TypeIntersection typeIntersection;

    /**
     * ID unique de la demande
     */
    private Long idDemande;

    /**
     * ID de l'intersection où se situe
     */
    private Long idIntersection;


	/**
     * Propriété javafx symbolisant le nom de la rue associée à cette intersection
     */
    private StringProperty nomIntersectionProperty;

    /**
     * Propriété javafx de la date d'arrivée au point sous forme "08h35"
     */
    private StringProperty dateArriveeProperty;

    /**
     * Propriété javafx de la date de départ au point sous forme "08h35"
     */
    private StringProperty dateDepartProperty;

    /**
     * Propriété JavaFX indiquant si la demande est orpheline (cela signife pour une
     * demande de collecte qu'elle n'a pas de demande de livraison associée, et
     * inversement)
     */
    private StringProperty orphelineProperty;

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
     * @param type Le type de demande
     * @param idIntersection L'id de l'intersection
     * @param nomIntersection Le nom de l'intersection
     * @param duree La durée de la demande
     * @param requete La requête liée
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

    /**
     * Getter nomIntersection
     * 
     * @return StringProperty nomIntersectionProperty
     */
    public StringProperty getNomIntersectionProperty() {
        return nomIntersectionProperty;
    }

    /**
     * Getter
     * 
     * @return Stringproperty
     */
    public StringProperty getDateArriveeProperty() {
        return dateArriveeProperty;
    }

    /**
     * Getter
     * 
     * @return Stringproperty
     */
    public StringProperty getDateDepartProperty() {
        return dateDepartProperty;
    }

    /**
     * Getter du type de demande
     * 
     * @return renvoie le type formaté avec l'icone correspondant
     */
    public StringProperty getTypeProperty() {
        if (typeIntersection == TypeIntersection.COLLECTE)
            return new SimpleStringProperty("■ collecte");
        if (typeIntersection == TypeIntersection.LIVRAISON)
            return new SimpleStringProperty("● livraison");
        return new SimpleStringProperty("?");
    }

    /**
     * Getter
     * 
     * @return duree sous forme StringProperty
     */
    public StringProperty getDureeProperty() {
        return new SimpleStringProperty(duree + "s");
    }

    /**
     * Getter
     * 
     * @return Date de départ
     */
    public Date getDateDepart() {
        return dateDepart;
    }

    /**
     * Getter
     * 
     * @return Date d'arrivée
     */
    public Date getDateArrivee() {
        return dateArrivee;
    }

    /**
     * Getter
     * @return La requête
     */
    public Requete getRequete() {
        return requete;
    }

    /**
     * Getter
     * @return Integer La durée
     */
    public Integer getDuree() {
        return duree;
    }

    /**
     * Setter
     * @param duree La durée
     */
    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    /**
     * Getter
     * @return Long idDemande
     */
    public Long getIdDemande() {
        return idDemande;
    }

    /**
     * Getter
     * @return Long
     */
    public Long getIdIntersection() {
        return idIntersection;
    }

    /**
     * Setter de Date datedépart et de StringProperty dateDépartProperty
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
     * Setter de Date datearrivee et de StringProperty dateArriveeProperty
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
     * Setter
     * @param idIntersection l'id de l'intersection
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

    /**
     * Getter
     * @return La propriété orpheline
     */
    public StringProperty getOrphelineProperty() {
        if (this.typeIntersection == TypeIntersection.COLLECTE) {
            this.orphelineProperty = requete.getDemandeLivraison() == null ? new SimpleStringProperty("⚠")
                    : new SimpleStringProperty();
        } else {
            this.orphelineProperty = requete.getDemandeCollecte() == null ? new SimpleStringProperty("⚠")
                    : new SimpleStringProperty();
        }
        return orphelineProperty;
    }

    /**
     * Setter
     * @param nomIntersectionProperty le nom de l'intersection
     */
    public void setNomIntersectionProperty(StringProperty nomIntersectionProperty) {
		this.nomIntersectionProperty = nomIntersectionProperty;
	}
}
