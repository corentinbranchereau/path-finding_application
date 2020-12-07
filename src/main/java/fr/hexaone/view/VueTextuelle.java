package fr.hexaone.view;

import fr.hexaone.model.*;
import fr.hexaone.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Permet d'afficher la partie textuelle de l'IHM.
 *
 * @author HexaOne
 * @version 1.0
 */

public class VueTextuelle {

    /**
     * Item de la fenêtre où s'affiche la vue textuelle
     */
    private Fenetre fenetre;

    /**
     * Couleur de l'highlight des lignes dans le tableau
     */
    private final String COULEUR_HIGHLIGHT_LIGNE = "yellow";

    /**
     * Variable définissant l'opacité appliquée lors de l'highlight d'une ligne
     * secondaire (ligne en lien avec la ligne actuellement sélectionnée)
     */
    private final double OPACITE_DEMANDE_LIEE = 0.3;

    /**
     * Variable qui est initialisé au premier parcours du planning: représente la
     * nom de la rue du dépot
     */
    private String nomDepot;

    /**
     * Controleur FXML pour le tableau affichant les requêtes dans la vue textuelle
     */
    private RequetesControleurFXML requetesControleur;

    private Boolean afficherPremierPlanning = true;

    /**
     * constructeur
     */
    public VueTextuelle(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    /**
     * Permet de réactualiser la vue textuelle après une modification du model
     */
    public void rafraichir(Planning planning, Demande demandeSelectionnee) {
        // Reinitialisation de la vue

        if (planning == null)
            return;

        if (planning.getDemandesOrdonnees() != null) {
            // Affichage des demandes (ordonnées) dans le tableau
            if (afficherPremierPlanning) {
                afficherPlanning(planning, planning.getCarte());
            } else {
                rafraichirVuePlanning(planning);
            }
            enleverHighlightDemande();
            if (demandeSelectionnee != null)
                highlightDemande(demandeSelectionnee);

        } else if (!planning.getRequetes().isEmpty()) {
            // Affichage des requêtes
            init();
            afficherRequetes(planning, planning.getCarte());
        } else {
            init();
        }
    }

    public void init() {
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().clear();
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().add(new Text(
                "Pour charger une Carte ou des Requêtes, rendez-vous dans 'Fichier', en haut à gauche de l'application. \r\n\r\n"));
        this.fenetre.getFenetreControleur().getScrollPane().setContent(null);
        fenetre.getListeDemandes().clear();
    }

    /**
     * Méthode qui permet d'afficher les requetes dans la vue textuelle
     * 
     * @param planning liste des segments à parcourir
     */
    public void afficherRequetes(Planning planning, Carte carte) {

        // récupération du nom du dépot
        this.nomDepot = getNomIntersection(planning, carte, carte.getIntersections().get(planning.getIdDepot()));

        // écriture du point et de l'heure de départ
        Text texteDepot = new Text(
                " ★ Départ : " + nomDepot + " à " + getStringFromDate(planning.getDateDebut()) + "\r\n\r\n");
        texteDepot.setFill(Color.RED);
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().clear();
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().add(texteDepot);

        ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();

        // parcours des requêtes
        for (Requete requete : planning.getRequetes()) {

            if (requete.getDemandeCollecte() != null) {
                listeDemandes.add(requete.getDemandeCollecte());
            }

            if (requete.getDemandeLivraison() != null) {
                listeDemandes.add(requete.getDemandeLivraison());
            }
        }
        fenetre.setListeDemandes(listeDemandes);

        try {
            // Load textual tab.
            FXMLLoader loader = new FXMLLoader();
            InputStream inputFichierFxml = Utils.getFileFromResourceAsStream(this,"requetes.fxml");
            AnchorPane personOverview = loader.load(inputFichierFxml);

            // Set person overview into the center of root layout.
            this.fenetre.getFenetreControleur().getScrollPane().setContent(personOverview);

            this.requetesControleur = loader.getController();
            requetesControleur.getDepartColumn()
                    .setCellValueFactory(cellData -> cellData.getValue().getDureeProperty());
            requetesControleur.getDepartColumn().setText("Durée");
            requetesControleur.getArriveeColumn().setVisible(false);
            requetesControleur.getOrphelineColumn().setVisible(false);
            requetesControleur.setFenetre(fenetre);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void afficherPlanning(Planning planning, Carte carte) {
        // TODO : Passer la logique applicative dans planning,e t récupérer la
        // listeDemande dans planning
        ObservableList<Demande> listeDemandes = creerListeDemandes(planning, carte);
        fenetre.setListeDemandes(listeDemandes);
        this.requetesControleur.getDemandeTable().setItems(listeDemandes);

        String depotName = getNomIntersection(planning, carte, carte.getIntersections().get(planning.getIdDepot()));
        if (this.nomDepot.isEmpty()) {
            this.nomDepot = depotName;
        }
        String depotString = "★ Dépot : " + nomDepot + "\r\n";
        Text texteDepot = new Text(depotString);
        texteDepot.setFill(Color.RED);
        String heureDepartString = "Heure de départ : " + getStringFromDate(planning.getDateDebut()) + "\r\n";
        Text texteHeureDepart = new Text(heureDepartString);
        String heureRetourString = "Heure de retour : " + getStringFromDate(planning.getDateFin());
        Text texteHeureRetour = new Text(heureRetourString);

        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().clear();
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().add(texteDepot);
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().add(texteHeureDepart);
        fenetre.getFenetreControleur().getDepotTextInformation().getChildren().add(texteHeureRetour);

        requetesControleur.getDepartColumn()
                .setCellValueFactory(cellData -> cellData.getValue().getDateDepartProperty());
        requetesControleur.getDepartColumn().setText("Repart à");
        requetesControleur.getArriveeColumn().setVisible(true);
        requetesControleur.getOrphelineColumn().setVisible(true);
    }

    public void rafraichirVuePlanning(Planning planning) {
        fenetre.getListeDemandes().clear();
        fenetre.getListeDemandes().addAll(planning.getDemandesOrdonnees());
        // this.requetesControleur.getDemandeTable().setItems(fenetre.getListeDemandes());
    }

    /**
     * Méthode qui crée les objets demande à la réception d'un planning
     *
     * @param planning le planning
     * @param carte    la carte
     * @return une liste observable de demandes
     */
    public ObservableList<Demande> creerListeDemandes(Planning planning, Carte carte) {

        ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();

        listeDemandes.addAll(planning.getDemandesOrdonnees());

        return listeDemandes;
    }

    /**
     * Méthode qui permet d'afficher le popup demande nouvelle livraison dans la vue
     * textuelle
     */
    public void afficherPopUpNouvelleDemandeLivraison() {
        // TODO
    }

    /**
     * Méthode qui permet de supprimer la demande de livraison sur la vue textuelle
     */
    public void afficherSuppressionRequeteVueTextuelle() {
        // TODO
    }

    /**
     * Méthode permettant d'associer un nom à une intersection à partir des noms de
     * rues qui lui sont adjacentes
     * 
     * @param planning     le planning contenant le dépot
     * @param carte        la carte contenant le nom des intersections
     * @param intersection l'intersection dont on cherche le nom
     * @return String: le nom de la rue du dépot
     */
    private String getNomIntersection(Planning planning, Carte carte, Intersection intersection) {

        Set<Segment> segments = intersection.getSegmentsPartants();
        String nomIntersection = "";
        if (!segments.isEmpty()) {
            Iterator<Segment> iterateurSegments = segments.iterator();
            nomIntersection = iterateurSegments.next().getNom();
            while ((nomIntersection.isBlank() || nomIntersection.isEmpty()) && iterateurSegments.hasNext()) {
                nomIntersection = iterateurSegments.next().getNom();
            }
        }
        return nomIntersection;
    }

    /**
     * Méthode permettant récupérer l'heure de sous forme de String au format
     * Pair<Heure, Minute>
     *
     * @param horaire la date
     * @return une pair contenant l'heure et les minutes sous forme de String
     */
    private String getStringFromDate(Date horaire) {

        Calendar date = Calendar.getInstance();
        date.setTime(horaire);
        int heure = date.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = date.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);

        return heureString + "h" + minutesString;
    }

    public RequetesControleurFXML getRequetesControleur() {
        return requetesControleur;
    }

    /**
     * Méthode permettant de désélectionner la demande actuellement sélectionnée
     */
    public void enleverHighlightDemande() {
        for (TableRow<Demande> ligne : this.requetesControleur.getMapIndexLignes().values()) {
            if (ligne != null) {
                ligne.setStyle("");
            }
        }
    }

    /**
     * Méthode permettant de sélectionner (highlight) une demande dans le tableau
     * 
     * @param demande La demande à sélectionner
     */
    public void highlightDemande(Demande demande) {
        Demande demandeLiee = null;
        if (demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
            demandeLiee = demande.getRequete().getDemandeLivraison();
        } else if (demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
            demandeLiee = demande.getRequete().getDemandeCollecte();
        }

        int indexDemande = this.requetesControleur.getDemandeTable().getItems().indexOf(demande);
        this.requetesControleur.getMapIndexLignes().get(indexDemande)
                .setStyle("-fx-background-color: " + this.COULEUR_HIGHLIGHT_LIGNE);

        if (demandeLiee != null) {
            int indexDemandeLiee = this.requetesControleur.getDemandeTable().getItems().indexOf(demandeLiee);
            Color couleur = Color.valueOf(this.COULEUR_HIGHLIGHT_LIGNE);
            this.requetesControleur.getMapIndexLignes().get(indexDemandeLiee)
                    .setStyle("-fx-background-color: rgba(" + 255 * couleur.getRed() + "," + 255 * couleur.getGreen()
                            + "," + 255 * couleur.getBlue() + ", " + this.OPACITE_DEMANDE_LIEE + ")");
        }
    }

    /**
     * Permet de remettre le highlight au bon endroit (dans la vue textuelle) lors
     * du drag and drop
     */
    public void rechargerHighlight() {
        if (this.fenetre.getControleur().getDemandeSelectionnee() != null) {
            enleverHighlightDemande();
            highlightDemande(this.fenetre.getControleur().getDemandeSelectionnee());

        }
    }

    /**
     * Permet de réaliser le drag/drop dans le modèle
     * 
     * @param draggIndex index de départ du dragg
     * @param dropIndex  index d'arrivée du dragg
     */
    public void modifierPlanning(int draggIndex, int dropIndex) {
        this.fenetre.getControleur().modifierPlanning(draggIndex, dropIndex);

    }
}
