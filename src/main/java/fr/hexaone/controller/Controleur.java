package fr.hexaone.controller;

import fr.hexaone.view.Fenetre;
import fr.hexaone.view.RequetesControleurFXML;
import javafx.stage.Stage;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle. Inclue le design pattern STATE.
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    /**
     * Gère l'affichage de l'application (Vue du MVC)
     */
    protected Fenetre fenetre;

    /**
     * Carte actuelle de l'application
     */
    protected Carte carte;

    /**
     * Planning actuel de l'application
     */
    protected Planning planning;

    /**
     * Demande seclectionné par l'utilisateur après un clic sur la vue graphique ou
     * textuelle
     */
    protected Demande demandeSelectionnee;

    /**
     * Etat courant du design pattern STATE
     */
    protected State etatCourant;

    /**
     * Etat initial de l'application du design pattern STATE
     */
    public EtatInitial etatInitial;

    /**
     * Etat carte chargée du design pattern STATE
     */
    public EtatCarteChargee etatCarteChargee;

    /**
     * Etat requête chargées du design pattern STATE
     */
    public EtatRequetesChargees etatRequetesChargees;

    /**
     * Etat tournée calculée du design pattern STATE
     */
    public EtatTourneeCalcule etatTourneeCalcule;

    /**
     * Etat sélection de nouveaux points et des durées pour une ajouter une nouvelle
     * requête du design pattern STATE
     */
    public EtatAjoutNouvelleRequete etatAjoutNouvelleRequete;

    /**
     * Etat ou on peut modifier l'ordre des requêtes dans le planning
     */
    public EtatModifierPlanning etatModifierPlanning;

    /**
     * Constructeur de Controleur. Instancie la fenêtre de l'application, l'affiche
     * à l'écran et met l'application à son état initial
     * 
     * @param stage Conteneur principal des éléments graphiques de la fenêtre
     */
    public Controleur(Stage stage) {
        this.fenetre = new Fenetre(stage, this);
        this.fenetre.dessinerFenetre();
        this.etatInitial = new EtatInitial();
        this.etatCarteChargee = new EtatCarteChargee();
        this.etatRequetesChargees = new EtatRequetesChargees();
        this.etatTourneeCalcule = new EtatTourneeCalcule();
        this.etatAjoutNouvelleRequete = new EtatAjoutNouvelleRequete();
        this.etatModifierPlanning = new EtatModifierPlanning();
        setEtatCourant(etatInitial);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger une carte" du menu. La méthode
     * permet de choisir un fichier carte au format XML et l'affiche dans la vue
     * graphique de l'application
     */
    public void chargerCarte() {
        etatCourant.chargerCarte(this);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger des requêtes" du menu. La méthode
     * permet de choisir un fichier de requêtes au format XML et affiche les
     * requêtes dans la vue graphique de l'application
     */
    public void chargerRequetes() {
        etatCourant.chargerRequetes(this);
    }

    /**
     * Méthode gérant le clic sur l'item "Quitter" du menu
     */
    public void quitterApplication() {
        etatCourant.quitterApplication(this);
    }

    /**
     * Méthode gérant le clic sur le bouton lançant le calcul du planning
     */
    public void lancerCalcul() {
        etatCourant.lancerCalcul(this);
    }

    /**
     * Méthode gérant le clic sur le bouton lançant le calcul du planning
     */
    public void supprimerDemande() {
        // etatCourant.supprimerDemande(this, demandeSelectionnee);
    }

    /**
     * Méthode gérant le clic sur le bouton lançant le calcul du planning
     */
    public void supprimerRequete() {
        etatCourant.supprimerDemande(this, demandeSelectionnee);
    }

    /**
     * Méthode permettant la sélection de deux intersections pour une nouvelle
     * requête en fin de trajet
     */
    public void ajouterNouvelleRequete() {
        etatCourant.ajoutNouvelleRequete(this);
    }

    /**
     * Méthode permettant la sélection d'une intersection
     */
    public void selectionnerIntersection(Long idIntersection) {
        etatCourant.selectionnerIntersection(this, idIntersection);
    }

    /**
     * Valider l'action en cours
     */
    public void valider(String pickUpDurationField, String deliveryDurationField) {
        etatCourant.valider(this, pickUpDurationField, deliveryDurationField);
    }

    /**
     * Annuler l'action en cours
     */
    public void annuler() {
        etatCourant.annuler(this);
    }

    public void modifierPlanning() {
        etatCourant.modifierPlanning(this);
    }

    /**
     * Réinitialise la saisie de l'utilisateur.
     */
    public void resetDemandeSelectionnee() {
        demandeSelectionnee = null;
    }

    /**
     * Sélectionne une demande dans la vue textuelle de l'application.
     * @param demandeSelectionnee La demande sélectionnée par l'utilisateur
     */
    public void setDemandeSelectionnee(Demande demandeSelectionnee) {
        this.demandeSelectionnee = demandeSelectionnee;
        // TODO afficher la demande selectionnee sur la vue textuelle et la vue graphique
    }

    /**
     * Renvoie la fenêtre de l'application.
     * 
     * @return La fenêtre de l'application
     */
    public Fenetre getFenetre() {
        return fenetre;
    }

    /**
     * Change l'état courant par un SETTER du design pattern STATE
     * 
     * @param etatCourant L'état courant de l'application
     */
    public void setEtatCourant(State etatCourant) {
        this.etatCourant = etatCourant;
    }

    /**
     * Renvoie le planning.
     * 
     * @return Le planning
     */
    public Planning getPlanning() {
        return planning;
    }

    /**
     * Change la valeur du planning actuel.
     * 
     * @param planning Le nouveau planning
     */
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    /**
     * Renvoie la carte.
     * 
     * @return La carte
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     * Change la valeur de la carte actuelle.
     * 
     * @param carte La nouvelle carte
     */
    public void setCarte(Carte carte) {
        this.carte = carte;
    }
}
