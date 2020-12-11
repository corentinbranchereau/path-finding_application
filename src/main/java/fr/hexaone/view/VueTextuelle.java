package fr.hexaone.view;

import fr.hexaone.model.*;
import fr.hexaone.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
     * La fenêtre de l'application
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

    /**
     * constructeur
     * 
     * @param fenetre La fenêtre de l'application
     */
    public VueTextuelle(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    /**
     * chargement du fichier FXML permettant d'afficher le tableau
     */
    public void chargerFXML() {
        try {
            FXMLLoader loader = new FXMLLoader();
            InputStream inputFichierFxml = Utils.obtenirInputStreamDepuisChemin(this, "requetes.fxml");
            AnchorPane conteneurTableauDemande = loader.load(inputFichierFxml);

            this.fenetre.getFenetreControleur().getScrollPaneTexte().setContent(conteneurTableauDemande);
            this.requetesControleur = loader.getController();
            this.requetesControleur.setFenetre(fenetre);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de réactualiser la vue textuelle après une modification du modèle
     * 
     * @param planning            Le planning actuel de l'application
     * @param demandeSelectionnee La demande qui a été sélectionnée par
     *                            l'utilisateur
     */
    public void rafraichir(Planning planning, Demande demandeSelectionnee) {

        if (planning == null || this.requetesControleur == null)
            return;

        // Affichage des demandes (ordonnées) dans le tableau
        if (planning.getDemandesOrdonnees() != null) {
            enleverHighlightDemande();
            afficherPlanning(planning, planning.getCarte());
            if (demandeSelectionnee != null)
                highlightDemande(demandeSelectionnee);

            // Affichage des requêtes si planning non calculé
        } else if (!planning.getRequetes().isEmpty()) {
            reinitialiser();
            afficherRequetes(planning, planning.getCarte());
            enleverHighlightDemande();
        } else {
            reinitialiser();
        }
    }

    /**
     * Méthode qui réinitialise la vue textuelle et le tableau comme au lancement de
     * l'application.
     */
    public void reinitialiser() {
        fenetre.getFenetreControleur().getTexteInformationDepot().getChildren().clear();
        fenetre.getFenetreControleur().getTexteInformationDepot().getChildren().add(new Text(
                "Pour charger une Carte ou des Requêtes, rendez-vous dans 'Fichier', en haut à gauche de l'application. \r\n\r\n"));
        requetesControleur.getListeDemandes().removeAll(requetesControleur.getListeDemandes());
    }

    /**
     * Méthode qui permet d'afficher les requêtes dans la vue textuelle lors du
     * chargement d'un fichier de requêtes (quand le planning n'est pas encore
     * calculé)
     * 
     * @param planning Le planning actuel de l'application
     * @param carte    La carte qui a été chargée
     */
    public void afficherRequetes(Planning planning, Carte carte) {

        // paramétrage des colonnes du tableau
        requetesControleur.getColonneDepart().setCellValueFactory(cellData -> cellData.getValue().getProprieteDuree());
        requetesControleur.getColonneDepart().setText("Durée");
        requetesControleur.getColonneArrivee().setVisible(false);
        requetesControleur.getColonneOrpheline().setVisible(false);

        // récupération du nom du dépot
        this.nomDepot = getNomIntersection(planning, carte.getIntersections().get(planning.getIdDepot()));

        // écriture du point et de l'heure de départ
        Text texteDepot = new Text(
                " ★ Départ : " + nomDepot + " à " + getStringAPartirDate(planning.getDateDebut()) + "\r\n\r\n");
        texteDepot.setFill(Color.RED);
        ObservableList<Node> noeudsInformationDepot = fenetre.getFenetreControleur().getTexteInformationDepot()
                .getChildren();
        noeudsInformationDepot.remove(0, noeudsInformationDepot.size());
        noeudsInformationDepot.add(texteDepot);

        // parcours des requêtes
        for (Requete requete : planning.getRequetes()) {
            if (requete.getDemandeCollecte() != null)
                requetesControleur.ajouterDemande(requete.getDemandeCollecte());

            if (requete.getDemandeLivraison() != null)
                requetesControleur.ajouterDemande(requete.getDemandeLivraison());

        }
    }

    /**
     * Méthode qui permet d'afficher le planning dans la vue textuelle quand
     * celui-ci est calculé
     * 
     * @param planning Le planning à afficher
     * @param carte    La carte permettant de récupérer le nom des intersections
     *                 (pour le dépôt)
     */
    public void afficherPlanning(Planning planning, Carte carte) {

        // paramétrage des colonnes du tableau
        requetesControleur.getColonneDepart()
                .setCellValueFactory(cellData -> cellData.getValue().getProprieteDateDepart());
        requetesControleur.getColonneDepart().setText("Repart à");
        requetesControleur.getColonneArrivee().setVisible(true);
        requetesControleur.getColonneOrpheline().setVisible(true);

        ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();
        listeDemandes.addAll(planning.getDemandesOrdonnees());
        requetesControleur.setListeDemandes(listeDemandes);
        this.requetesControleur.getTableauDemandes().setItems(listeDemandes);

        String nomDepot = getNomIntersection(planning, carte.getIntersections().get(planning.getIdDepot()));
        if (this.nomDepot.isEmpty()) {
            this.nomDepot = nomDepot;
        }
        String depotString = "★ Dépot : " + nomDepot + "\r\n";
        Text texteDepot = new Text(depotString);
        texteDepot.setFill(Color.RED);
        String heureDepartString = "Heure de départ : " + getStringAPartirDate(planning.getDateDebut()) + "\r\n";
        Text texteHeureDepart = new Text(heureDepartString);
        String heureRetourString = "Heure de retour : " + getStringAPartirDate(planning.getDateFin());
        Text texteHeureRetour = new Text(heureRetourString);

        ObservableList<Node> noeudsInformationDepot = fenetre.getFenetreControleur().getTexteInformationDepot()
                .getChildren();
        noeudsInformationDepot.remove(0, noeudsInformationDepot.size());
        noeudsInformationDepot.add(texteDepot);
        noeudsInformationDepot.add(texteHeureDepart);
        noeudsInformationDepot.add(texteHeureRetour);

    }

    /**
     * Méthode permettant d'associer un nom à une intersection à partir des noms de
     * rues qui lui sont adjacentes
     * 
     * @param planning     Le planning
     * @param intersection L'intersection dont on cherche le nom
     * @return Le nom de l'intersection
     */
    private String getNomIntersection(Planning planning, Intersection intersection) {

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
     * Méthode permettant de récupérer l'heure sous forme de String au format
     * "08h35"
     *
     * @param horaire La date dans laquelle on récupère l'heure
     * @return L'heure sous forme de String au format "08h35"
     */
    private String getStringAPartirDate(Date horaire) {
        Calendar date = Calendar.getInstance();
        date.setTime(horaire);
        int heure = date.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = date.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);

        return heureString + "h" + minutesString;
    }

    /**
     * @return Le controleur requêtes FXML
     */
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
        if (demande.getTypeDemande() == TypeDemande.COLLECTE) {
            demandeLiee = demande.getRequete().getDemandeLivraison();
        } else if (demande.getTypeDemande() == TypeDemande.LIVRAISON) {
            demandeLiee = demande.getRequete().getDemandeCollecte();
        }

        int indexDemande = this.requetesControleur.getTableauDemandes().getItems().indexOf(demande);
        this.requetesControleur.getMapIndexLignes().get(indexDemande)
                .setStyle("-fx-background-color: " + this.COULEUR_HIGHLIGHT_LIGNE);

        if (demandeLiee != null) {
            int indexDemandeLiee = this.requetesControleur.getTableauDemandes().getItems().indexOf(demandeLiee);
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
     * Permet de réaliser le drag and drop dans le modèle
     * 
     * @param indexDepart  Index de départ du drag
     * @param indexArrivee Index d'arrivée du drag
     */
    public void modifierPlanning(int indexDepart, int indexArrivee) {
        this.fenetre.getControleur().modifierPlanning(indexDepart, indexArrivee);
    }

    /**
     * Définit si le menu contextuel est visible ou non
     * 
     * @param visible Indique si le menu contextuel est visible ou non
     */
    public void setMenuContextuelAffichable(boolean visible) {
        if (this.requetesControleur != null) {
            this.requetesControleur.setMenuContextuelAffichable(visible);
        }
    }
}
