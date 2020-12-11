package fr.hexaone.controller;

import fr.hexaone.controller.Command.ListOfCommands;
import fr.hexaone.controller.State.*;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.view.Fenetre;
import javafx.stage.Stage;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec la vue et le modèle. Inclut le design pattern State et Command.
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    /**
     * Liste de commandes conforme au design pattern Command pour l'implémentation
     * de l'undo/redo
     */
    private ListOfCommands listeCommandes;

    /**
     * Gère l'affichage de l'application (Vue du MVC)
     */
    private Fenetre fenetre;

    /**
     * Planning actuel de l'application
     */
    private Planning planning;

    /**
     * Demande sélectionnée par l'utilisateur après un clic sur la vue graphique ou
     * textuelle
     */
    private Demande demandeSelectionnee;

    /**
     * Etat (design pattern State) courant
     */
    private State etatCourant;

    /**
     * Etat (design pattern State) initial de l'application
     */
    private EtatInitial etatInitial;

    /**
     * Etat (design pattern State) carte chargée
     */
    private EtatCarteChargee etatCarteChargee;

    /**
     * Etat (design pattern State) requête chargées
     */
    private EtatRequetesChargees etatRequetesChargees;

    /**
     * Etat (design pattern State) tournée calculée
     */
    private EtatTourneeCalcule etatTourneeCalcule;

    /**
     * Etat (design pattern State) sélection de nouveaux points et des durées pour
     * ajouter une nouvelle requête
     */
    private EtatAjoutNouvelleRequete etatAjoutNouvelleRequete;

    /**
     * Etat (design pattern State) sélection d'un nouveau point et d'une durée pour
     * modifier une demande du design pattern State
     */
    private EtatModifierDemande etatModifierDemande;

    /**
     * Constructeur de Controleur. Instancie la fenêtre de l'application, l'affiche
     * à l'écran et met l'application dans son état initial
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
        this.etatModifierDemande = new EtatModifierDemande();
        this.listeCommandes = new ListOfCommands();
        setEtatInitial();
    }

    /**
     * Annule la dernière commande (design pattern Command) via un undo
     */
    public void undo() {
        etatCourant.undo(listeCommandes);
        rafraichirVues(false);
    }

    /**
     * Rétablit la dernière commande (design pattern Command) via un redo
     */
    public void redo() {
        etatCourant.redo(listeCommandes);
        rafraichirVues(false);
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
     * Méthode permettant la sélection d'une nouvelle intersection pour modifier le
     * lieu de collecte ou livraison
     */
    public void modifierDemande() {
        etatCourant.modifierDemande(this, demandeSelectionnee);
        rafraichirVues(false);
    }

    /**
     * Méthode gérant le clic sur le bouton permettant de supprimer une requête
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
     * 
     * @param idIntersection L'id de l'intersection selectionnée
     */
    public void selectionnerIntersection(Long idIntersection) {
        etatCourant.selectionnerIntersection(this, idIntersection);
        rafraichirVues(false);
    }

    /**
     * Méthode permettant d'afficher l'aide à l'utilisateur
     */
    public void aide() {
        etatCourant.aide(this);
    }

    /**
     * Valide l'action en cours
     * 
     * @param durees Le/les nouvelle(s) durée(s).
     */
    public void valider(String... durees) {

        int taille = durees.length;

        if (taille == 2) {
            etatCourant.valider(this, durees[0], durees[1]);
        }
        if (taille == 1) {
            etatCourant.valider(this, durees[0]);
        }

        rafraichirVues(false);
    }

    /**
     * Annule l'action en cours
     */
    public void annuler() {
        etatCourant.annuler(this);
        rafraichirVues(false);
    }

    /***
     * Modifie le planning en modifiant la position d'une demande
     * 
     * @param i Correspond à l'index de la demande dans la liste des demandes avant
     *          la modification
     * @param j Correspond à l'index de la demande dans la liste des demandes après
     *          la modification
     */
    public void modifierPlanning(int i, int j) {
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

    /**
     * Réinitialise les commandes actuellement en mémoire.
     */
    public void reinitialiserCommandes() {
        listeCommandes.reinitialiserCommandes();
    }

    /**
     * Sélectionne une demande
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
     * Renvoie la demande actuellement sélectionnée
     * 
     * @return La demande qui est sélectionnée
     */
    public Demande getDemandeSelectionnee() {
        return demandeSelectionnee;
    }

    /**
     * Renvoie la liste des commandes du design pattern Command.
     * 
     * @return La liste des commandes
     */
    public ListOfCommands getListOfCommands() {
        return listeCommandes;
    }

    /**
     * Passe l'application dans l'état initial (design pattern State)
     */
    public void setEtatInitial() {
        etatInitial.init(this);
        etatCourant = etatInitial;
    }

    /**
     * Passe l'application dans l'état carte chargée (design pattern State)
     */
    public void setEtatCarteChargee() {
        etatCarteChargee.init(this);
        etatCourant = etatCarteChargee;
    }

    /**
     * Passe l'application dans l'état requêtes chargées (design pattern State)
     */
    public void setEtatRequetesChargees() {
        etatRequetesChargees.init(this);
        etatCourant = etatRequetesChargees;
    }

    /**
     * Passe l'application dans l'état tournée calculée (design pattern State)
     */
    public void setEtatTourneeCalcule() {
        etatTourneeCalcule.init(this);
        etatCourant = etatTourneeCalcule;
    }

    /**
     * Passe l'application dans l'état ajout d'une nouvelle requête (design pattern
     * State)
     */
    public void setEtatAjoutNouvelleRequete() {
        etatAjoutNouvelleRequete.init(this);
        etatCourant = etatAjoutNouvelleRequete;
    }

    /**
     * Passe l'application dans l'état modifier une demande (design pattern State)
     * 
     * @param duree La durée de la demande à modifier
     */
    public void setEtatModifierDemande(int duree) {
        etatModifierDemande.setDuree(duree);
        etatModifierDemande.init(this);
        etatCourant = etatModifierDemande;
    }

    /**
     * Change la valeur de la demande sélectionnée
     * 
     * @param demandeSelectionnee La demande qui a été sélectionnée
     */
    public void setDemandeSelectionnee(Demande demandeSelectionnee) {
        this.demandeSelectionnee = demandeSelectionnee;
    }
}
