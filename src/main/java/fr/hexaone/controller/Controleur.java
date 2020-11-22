package fr.hexaone.controller;
import fr.hexaone.view.Fenetre;
import javafx.stage.Stage;

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
     * Constructeur de Controleur. Instancie la fenêtre de l'application,
     * l'affiche à l'écran et met l'application à son état initial
     * 
     * @param stage Conteneur principal des éléments graphiques de la fenêtre
     */
    public Controleur(Stage stage) {
        this.fenetre = new Fenetre(stage, this);
        this.fenetre.dessinerFenetre();
        this.etatInitial = new EtatInitial();
        this.etatCarteChargee = new EtatCarteChargee();
        this.etatRequetesChargees = new EtatRequetesChargees();
        setEtatCourant(etatInitial);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger une carte" du menu. La méthode
     * permet de choisir un fichier carte au format XML et l'affiche dans la vue
     * graphique de l'application
     */
    public void handleClicChargerCarte() {
        etatCourant.handleClicChargerCarte(this);
    }

    /**
     * Méthode gérant le clic sur l'item "Charger des requêtes" du menu
     */
    public void handleClicChargerRequetes() {
        etatCourant.handleClicChargerRequetes(this);
    }

    /**
     * Méthode gérant le clic sur l'item "Quitter" du menu
     */
    public void handleClicQuitter() {
        etatCourant.handleClicQuitter(this);
    }

    /**
     * Méthode gérant le clic sur le bouton lançant le calcul du planning
     */
    public void handleClicBoutonCalcul() {
        etatCourant.handleClicBoutonCalcul(this);
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
     * @param etatCourant L'état courant de l'application
     */
    public void setEtatCourant(State etatCourant) {
        this.etatCourant = etatCourant;
    }
}
