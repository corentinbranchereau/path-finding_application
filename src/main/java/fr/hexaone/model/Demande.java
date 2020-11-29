package fr.hexaone.model;

public class Demande {

    protected Integer type; // 0 : dépot, 1: collecte, 2: livraison

    protected Long idIntersection;

    protected String nomIntersection;

    protected String dateArrivee;

    protected String dateDepart;
}
