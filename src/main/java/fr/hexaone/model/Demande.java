package fr.hexaone.model;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Demande {

    public static Integer TYPE_DEPOT = 0;
    public static Integer TYPE_COLLECTE = 1;
    public static Integer TYPE_LIVRAISON = 2;
    
    protected Long idDemande;

    protected Integer type; // 0 : dépot, 1: collecte, 2: livraison

    protected Long idIntersection;

    protected StringProperty nomIntersection;

    protected StringProperty dateArriveeString;

    protected StringProperty dateDepartString;

    protected Date dateArrivee;

    protected Date dateDepart;

    protected Color couleur;

    protected Integer duree;

    protected Requete requete;

    public Demande(Integer type, Long idIntersection, String nomIntersection, String dateArrivee, String dateDepart) {
        this.type = type;
        this.idIntersection = idIntersection;
        this.dateArriveeString = new SimpleStringProperty(dateArrivee);
        this.dateDepartString = new SimpleStringProperty(dateDepart);
        this.nomIntersection = new SimpleStringProperty(nomIntersection);

    }

    public Integer getType() {
        return type;
    }

    public StringProperty getTypeString() {
        if (type == 0)
            return new SimpleStringProperty("★ dépot");
        if (type == 1)
            return new SimpleStringProperty("■ collecte");
        if (type == 2)
            return new SimpleStringProperty("● livraison");

        return new SimpleStringProperty("");
    }


    // Getter and setter

    public Long getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getIdIntersection() {
        return idIntersection;
    }

    public void setIdIntersection(Long idIntersection) {
        this.idIntersection = idIntersection;
    }

    public StringProperty getNomIntersection() {
        return nomIntersection;
    }

    public void setNomIntersection(StringProperty nomIntersection) {
        this.nomIntersection = nomIntersection;
    }

    public StringProperty getDateArriveeString() {
        return dateArriveeString;
    }

    public void setDateArriveeString(StringProperty dateArriveeString) {
        this.dateArriveeString = dateArriveeString;
    }

    public StringProperty getDateDepartString() {
        return dateDepartString;
    }

    public void setDateDepartString(StringProperty dateDepartString) {
        this.dateDepartString = dateDepartString;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Requete getRequete() {
        return requete;
    }

    public void setRequete(Requete requete) {
        this.requete = requete;
    }
    
}
