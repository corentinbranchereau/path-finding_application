package fr.hexaone.controller;

import fr.hexaone.controller.Command.ListOfCommands;
import fr.hexaone.controller.State.*;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.view.Fenetre;
import javafx.stage.Stage;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle. Inclue le design pattern STATE et COMMAND.
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    /**
     * Liste de commandes conforme au design pattern COMMAND pour l'implémentation
     * de l'undo/redo
     */
    private ListOfCommands l;

    /**
     * Gère l'affichage de l'application (Vue du MVC)
     */
    private Fenetre fenetre;

    /**
     * Carte actuelle de l'application
     */
    private Carte carte;

    /**
     * Planning actuel de l'application
     */
    private Planning planning;

    /**
     * Demande selectionnée par l'utilisateur après un clic sur la vue graphique ou
     * textuelle
     */
    private Demande demandeSelectionnee;

    /**
     * Etat courant du design pattern STATE
     */
    private State etatCourant;

    /**
     * Etat initial de l'application du design pattern STATE
     */
    private EtatInitial etatInitial;

    /**
     * Etat carte chargée du design pattern STATE
     */
    private EtatCarteChargee etatCarteChargee;

    /**
     * Etat requête chargées du design pattern STATE
     */
    private EtatRequetesChargees etatRequetesChargees;

    /**
     * Etat tournée calculée du design pattern STATE
     */
    private EtatTourneeCalcule etatTourneeCalcule;

    /**
     * Etat sélection de nouveaux points et des durées pour une ajouter une nouvelle
     * requête du design pattern STATE
     */
    private EtatAjoutNouvelleRequete etatAjoutNouvelleRequete;
    
    /**
     * Etat sélection de nouveaux points et des durées pour une ajouter une nouvelle
     * requête du design pattern STATE
     */
    private EtatModifierDemande etatModifierDemande;

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
        this.etatModifierDemande= new EtatModifierDemande();
        this.l = new ListOfCommands();
        setEtatInitial();
    }

    /**
     * Annuler la dernière commande (design pattern COMMAND) via un undo
     */
    public void undo() {
        etatCourant.undo(l);
        rafraichirVues(true);
    }

    /**
     * Rétablir la dernière commande (design pattern COMMAND) via un redo
     */
    public void redo() {
        etatCourant.redo(l);
        rafraichirVues(true);
    }

    /**
     * Méthode permettant de rafraichir les vues après des modifications dans le
     * modèle
     * 
     * @param dessinerCarte Indique s'il y a besoin de redessiner les noeuds de la
     *                      carte (segments et intersections)
     */
    public void rafraichirVues(boolean dessinerCarte) {
        fenetre.rafraichir(planning, demandeSelectionnee, dessinerCarte);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger une carte" du menu. La méthode
     * permet de choisir un fichier carte au format XML et l'affiche dans la vue
     * graphique de l'application
     */
    public void chargerCarte() {
        etatCourant.chargerCarte(this);
        rafraichirVues(true);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger des requêtes" du menu. La méthode
     * permet de choisir un fichier de requêtes au format XML et affiche les
     * requêtes dans la vue graphique de l'application
     */
    public void chargerRequetes() {
        etatCourant.chargerRequetes(this);
        rafraichirVues(false);
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
        rafraichirVues(false);
    }

    /**
     * Méthode gérant le clic sur le bouton permettant de supprimer une demande
     */
    public void supprimerDemande() {
        etatCourant.supprimerDemande(this, demandeSelectionnee);
        rafraichirVues(false);
    }
    
    /**
     * Méthode permettant la sélection d'une nouvelle intersection pour modifier 
     *le lieu de collecte ou livraison
     */
    public void modifierDemande() {
        etatCourant.modifierDemande(this,demandeSelectionnee);
        rafraichirVues(false);
    }

    /**
     * Méthode gérant le clic sur le bouton permettant de supprimer une requete
     */
    public void supprimerRequete() {
        etatCourant.supprimerRequete(this, demandeSelectionnee.getRequete());
        rafraichirVues(false);
    }

    /**
     * Méthode permettant la sélection de deux intersections pour une nouvelle
     * requête en fin de trajet
     */
    public void ajouterNouvelleRequete() {
        etatCourant.ajoutNouvelleRequete(this);
        rafraichirVues(false);
    }

    /**
     * Méthode permettant la sélection d'une intersection
     */
    public void selectionnerIntersection(Long idIntersection) {
        etatCourant.selectionnerIntersection(this, idIntersection);
        rafraichirVues(false);
    }

    /**
     * Méthode permettant d'afficher l'aide à l'utilisateur
     */
    public void aide(){
        etatCourant.aide(this);
    }

    /**
     * Valider l'action en cours
     */
    public void valider(String... durations) {
    	
    	int taille=durations.length;
    	
    	if (taille==2) {
    		etatCourant.valider(this, durations[0], durations[1]);
    		
    	}
    	if(taille==1) {
    		etatCourant.valider(this, durations[0]);
    	}
        
        rafraichirVues(false);
    }

    /**
     * Annuler l'action en cours
     */
    public void annuler() {
        etatCourant.annuler(this);
        rafraichirVues(false);
    }

    /***
     * Modifier le planning
     * @param i correspondant à l'index dans la liste des demandes avant la modification
     * @param j correspondant à l'index dans la liste des demandes après la modification
     */
    public void modifierPlanning(int i , int j) {
        etatCourant.modifierPlanning(this, i, j);
        rafraichirVues(false);
    }

    /**
     * Réinitialise la saisie de l'utilisateur.
     */
    public void resetDemandeSelectionnee() {
        this.demandeSelectionnee = null;
        rafraichirVues(false);
    }

    public void reinitialiserCommandes() {
        l.reinitialiser();
    }

    /**
     * Sélectionne une demande dans la vue textuelle de l'application.
     * 
     * @param demandeSelectionnee La demande sélectionnée par l'utilisateur
     */
    public void selectionnerDemande(Demande demandeSelectionnee) {
        this.etatCourant.selectionnerDemande(this, demandeSelectionnee);
    }

    /**
     * Renvoie la fenêtre de l'application.
     * 
     * @return La fenêtre de l'application
     */
    public Fenetre getFenetre() {
        return fenetre;
    }

    // /**
    // * Change l'état courant par un SETTER du design pattern STATE
    // *
    // * @param etatCourant L'état courant de l'application
    // */
    // public void setEtatCourant(State etatCourant) {
    // this.etatCourant = etatCourant;
    // }

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

    /**
     * Renvoie la demande actuellement sélectionnée
     * 
     * @return La demande qui est sélectionnée
     */
    public Demande getDemandeSelectionnee() {
        return demandeSelectionnee;
    }

    /**
     * Renvoie la liste des commandes du design pattern COMMAND.
     * 
     * @return
     */
    public ListOfCommands getListOfCommands() {
        return l;
    }

    public void setEtatInitial() {
        etatInitial.init(this);
        etatCourant = etatInitial;
    }

    public void setEtatCarteChargee() {
        etatCarteChargee.init(this);
        etatCourant = etatCarteChargee;
    }

    public void setEtatRequetesChargees() {
        etatRequetesChargees.init(this);
        etatCourant = etatRequetesChargees;
    }

    public void setEtatTourneeCalcule() {
        etatTourneeCalcule.init(this);
        etatCourant = etatTourneeCalcule;
    }

    public void setEtatAjoutNouvelleRequete() {
        etatAjoutNouvelleRequete.init(this);
        etatCourant = etatAjoutNouvelleRequete;
    }
    
    public void setEtatModifierDemande(int duree) {
    	etatModifierDemande.setDuree(duree);
        etatModifierDemande.init(this);
        etatCourant = etatModifierDemande;
    }

    public void setDemandeSelectionnee(Demande demandeSelectionnee) {
        this.demandeSelectionnee = demandeSelectionnee;
    }
}
