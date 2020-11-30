package fr.hexaone.model;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Demande {

    private static Long increment = (long) 0;

    protected TypeIntersection typeIntersection;

    protected Long idDemande;

    protected Long idIntersection;

    protected StringProperty nomIntersectionProperty;

    protected StringProperty dateArriveeProperty;

    protected StringProperty dateDepartProperty;

    protected Date dateArrivee;

    protected Date dateDepart;

    protected Integer duree;

    protected Color couleur;

    public Demande(TypeIntersection type, Long idIntersection, String nomIntersection, Integer duree) {
        this.idDemande = increment;
        increment++;
        this.idIntersection = idIntersection;
        this.typeIntersection = type;
        this.duree = duree;
        this.dateArriveeProperty = null;
        this.dateDepartProperty = null;
        this.dateArrivee = null;
        this.dateDepart = null;
        this.nomIntersectionProperty = new SimpleStringProperty(nomIntersection);

    }

    public StringProperty getNomIntersectionProperty() {
        return nomIntersectionProperty;
    }

    public StringProperty getDateArriveeProperty() {
        return dateArriveeProperty;
    }

    public StringProperty getDateDepartProperty() {
        return dateDepartProperty;
    }

    public StringProperty getTypeProperty() {
        if (typeIntersection == TypeIntersection.DEPOT)
            return new SimpleStringProperty("★ dépot");
        if (typeIntersection == TypeIntersection.COLLECTE)
            return new SimpleStringProperty("■ collecte");
        if (typeIntersection == TypeIntersection.LIVRAISON)
            return new SimpleStringProperty("● livraison");

        return new SimpleStringProperty("");
    }

    public String getNomIntersection() {
        return nomIntersectionProperty.getValue();
    }

}
