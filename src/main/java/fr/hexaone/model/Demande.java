package fr.hexaone.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Demande {

    protected Integer type; // 0 : dépot, 1: collecte, 2: livraison

    protected Long idIntersection;

    protected StringProperty nomIntersection;

    protected StringProperty dateArrivee;

    protected StringProperty dateDepart;

    protected Color couleur;

    public Demande(Integer type, Long idIntersection, String nomIntersection, String dateArrivee, String dateDepart) {
        this.type = type;
        this.idIntersection = idIntersection;
        this.dateArrivee = new SimpleStringProperty(dateArrivee);
        this.dateDepart = new SimpleStringProperty(dateDepart);
        this.nomIntersection = new SimpleStringProperty(nomIntersection);

    }

    public StringProperty getNomIntersection() {
        return nomIntersection;
    }

    public StringProperty getDateArrivee() {
        return dateArrivee;
    }

    public StringProperty getDateDepart() {
        return dateDepart;
    }

    public StringProperty getType() {
        if (type == 0)
            return new SimpleStringProperty("★ dépot");
        if (type == 1)
            return new SimpleStringProperty("■ collecte");
        if (type == 2)
            return new SimpleStringProperty("● livraison");

        return new SimpleStringProperty("");
    }

}
