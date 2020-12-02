package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.TypeIntersection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

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
        protected Fenetre fenetre;

        protected TextFlow zoneTexte;

        /**
         * Controleur FXML pour le tableau affichant les requêtes dans la vue textuelle
         */
        protected RequetesControleurFXML requetesControleurFXML;

        /**
         * Représente la paire de lignes (collecte et livraison) qui est
         * sélectionnée/highlight
         */
        protected Pair<TableRow<Demande>, TableRow<Demande>> lignesHighlight;

        /**
         * Couleur de l'highlight des lignes dans le tableau
         */
        protected final String COULEUR_HIGHLIGHT_LIGNE = "yellow";

        /**
         * Variable définissant l'opacité appliquée lors de l'highlight d'une ligne
         * secondaire (ligne en lien avec la ligne actuellement sélectionnée)
         */
        protected final double OPACITE_DEMANDE_LIEE = 0.3;

        /**
         * constructeur
         */
        public VueTextuelle(Fenetre fenetre) {
                this.fenetre = fenetre;
        }

        /**
         * Méthode qui permet d'afficher les requetes dans la vue textuelle
         * 
         * @param planning liste des segments à parcourir
         */
        public void afficherRequetes(Planning planning, Carte carte, Map<Requete, Color> mapCouleurRequete) {

                // On vide le zone de texte au cas où des choses sont déjà affichées dedans
                this.zoneTexte.getChildren().clear();

                // récupération du nom du dépot
                String depotName = getNomIntersection(planning, carte,
                                carte.getIntersections().get(planning.getIdDepot()));

                // récupération de l'heure de départ
                Pair<String, String> horaire = getStringFromDate(planning, planning.getDateDebut());
                String heure = horaire.getKey();
                String minutes = horaire.getValue();

                // écriture du point et de l'heure de départ
                Text texteDepot = new Text(" ★ Départ : " + depotName + " à " + heure + 'h' + minutes + "\r\n\r\n");
                texteDepot.setFill(Color.RED);
                this.zoneTexte.getChildren().add(texteDepot);
                int i = 1;

                // parcours des requêtes
                for (Requete requete : planning.getRequetes()) {

                        String nomCollecte = requete.getDemandeCollecte().getNomIntersection();
                        String nomLivraison = requete.getDemandeLivraison().getNomIntersection();

                        Text titreText = new Text("Requête " + i + ": \r\n");
                        Text collecteIcon = new Text("     ■ ");
                        Text collecteText = new Text("Collecte : " + nomCollecte + " - "
                                        + String.valueOf(requete.getDemandeCollecte().getDuree()) + "s" + "\r\n");
                        Text livraisonIcon = new Text("     ● ");
                        Text livraisonText = new Text("Livraison : " + nomLivraison + " - "
                                        + String.valueOf(requete.getDemandeLivraison().getDuree()) + "s" + "\r\n\n");
                        i++;

                        collecteIcon.setFill(mapCouleurRequete.get(requete));
                        livraisonIcon.setFill(mapCouleurRequete.get(requete));

                        this.zoneTexte.getChildren().addAll(titreText, collecteIcon, collecteText, livraisonIcon,
                                        livraisonText);
                }
        }

        public void afficherPlanning(Planning planning, Carte carte) {
                ObservableList<Demande> listeDemandes = creerListeDemandes(planning, carte);
                fenetre.setListeDemandes(listeDemandes);
                try {
                        // Load textual tab.
                        FXMLLoader loader = new FXMLLoader();
                        FileInputStream inputFichierFxml = new FileInputStream(
                                        "src/main/java/fr/hexaone/view/requetes.fxml");
                        AnchorPane personOverview = (AnchorPane) loader.load(inputFichierFxml);

                        // Set person overview into the center of root layout.
                        this.fenetre.getFenetreControleur().getScrollPane().setContent(personOverview);

                        this.requetesControleurFXML = loader.getController();
                        this.requetesControleurFXML.setFenetre(fenetre);

                } catch (IOException e) {
                        e.printStackTrace();
                }

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

                String depotName = getNomIntersection(planning, carte,
                                carte.getIntersections().get(planning.getIdDepot()));

                Demande depot = new Demande(TypeIntersection.DEPOT, planning.getIdDepot(), depotName, null, null);
                depot.setDateDepart(planning.getDateDebut());
                depot.setDateArrivee(planning.getDateFin());

                // listeDemandes.add(depot);

                for (Demande demande : planning.getDemandesOrdonnees()) {
                        listeDemandes.add(demande);
                }

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
         * Méthode qui permet d'afficher la nouvelle demande de livraison sur la vue
         * textuelle
         */
        public void afficherNouvelleRequeteVueTextuelle(Requete nouvelleRequete,
                        Map<Requete, Color> mapCouleurRequete) {
                // TODO
                // String nomCollecte =
                // nouvelleRequete.getDemandeCollecte().getNomIntersection();
                // String nomLivraison =
                // nouvelleRequete.getDemandeLivraison().getNomIntersection();

                // Text titreText = new Text("Requête " + i + ": \r\n");
                // Text collecteIcon = new Text(" ■ ");
                // Text collecteText = new Text("Collecte : " + nomCollecte + " - "
                // + String.valueOf(nouvelleRequete.getDemandeCollecte().getDuree()) + "s" +
                // "\r\n");
                // Text livraisonIcon = new Text(" ● ");
                // Text livraisonText = new Text("Livraison : " + nomLivraison + " - "
                // + String.valueOf(nouvelleRequete.getDemandeLivraison().getDuree()) + "s" +
                // "\r\n\n");

                // collecteIcon.setFill(mapCouleurRequete.get(nouvelleRequete));
                // livraisonIcon.setFill(mapCouleurRequete.get(nouvelleRequete));

                // this.zoneTexte.getChildren().addAll(titreText, collecteIcon, collecteText,
                // livraisonIcon,
                // livraisonText);
        }

        /**
         * Méthode qui permet de supprimer la demande de livraison sur la vue textuelle
         */
        public void afficherSuppressionRequeteVueTextuelle() {
                // TODO
        }

        /**
         * setter permettant de définir la zone de texte de la fenêtre
         * 
         * @param zoneTexte
         */
        public void setZoneTexte(TextFlow zoneTexte) {
                this.zoneTexte = zoneTexte;
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
                        while ((nomIntersection.isBlank() || nomIntersection.isEmpty())
                                        && iterateurSegments.hasNext()) {
                                nomIntersection = iterateurSegments.next().getNom();
                        }
                }
                return nomIntersection;
        }

        /**
         * Méthode permettant récupérer l'heure de sous forme de String au format
         * Pair<Heure, Minute>
         * 
         * @param planning le planning
         * @param horaire  la date
         * @return une pair contenant l'heure et les minutes sous forme de String
         */
        private Pair<String, String> getStringFromDate(Planning planning, Date horaire) {

                Calendar date = Calendar.getInstance();
                date.setTime(horaire);
                int heure = date.get(Calendar.HOUR_OF_DAY);
                String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
                int minutes = date.get(Calendar.MINUTE);
                String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);

                return new Pair<String, String>(heureString, minutesString);
        }

        /**
         * Méthode qui permet de trier une Map selon la date passée en valeur
         * 
         * @param unSortedMap
         * @return
         */
        private LinkedHashMap<Text, Date> getRequetesTrieesParDatePassage(Map<Text, Date> unSortedMap) {

                // LinkedHashMap preserve the ordering of elements in which they are inserted
                LinkedHashMap<Text, Date> sortedMap = new LinkedHashMap<>();

                unSortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

                return sortedMap;
        }

        /**
         * Méthode permettant de désélectionner la demande actuellement sélectionnée
         */
        public void enleverHighlightDemande() {
                for (TableRow<Demande> row : this.requetesControleurFXML.getListeLignes()) {
                        row.setStyle("");
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

                int indexDemande = this.requetesControleurFXML.getDemandeTable().getItems().indexOf(demande) + 1;
                int indexDemandeLiee = this.requetesControleurFXML.getDemandeTable().getItems().indexOf(demandeLiee)
                                + 1;

                this.requetesControleurFXML.getListeLignes().get(indexDemande)
                                .setStyle("-fx-background-color: " + this.COULEUR_HIGHLIGHT_LIGNE);

                Color couleur = Color.valueOf(this.COULEUR_HIGHLIGHT_LIGNE);
                this.requetesControleurFXML.getListeLignes().get(indexDemandeLiee)
                                .setStyle("-fx-background-color: rgba(" + 255 * couleur.getRed() + ","
                                                + 255 * couleur.getGreen() + "," + 255 * couleur.getBlue() + ", "
                                                + this.OPACITE_DEMANDE_LIEE + ")");
        }

        public void rechargerHighlight() {
                if (this.fenetre.getControleur().getDemandeSelectionnee() != null) {
                        enleverHighlightDemande();
                        highlightDemande(this.fenetre.getControleur().getDemandeSelectionnee());
                }
        }
}
