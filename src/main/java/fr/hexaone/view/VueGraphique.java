package fr.hexaone.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.Trajet;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Permet d'afficher la partie graphique de l'IHM.
 * 
 * @author HexaOne
 * @version 1.0
 */

public class VueGraphique {

    /**
     * Pane dans lequel sont dessinés les différents éléments (carte, segments,
     * etc.)
     */
    protected Pane paneDessin;

    /**
     * Coordonnée x minimale de la carte
     */
    protected double minX = Double.MAX_VALUE;

    /**
     * Coordonnée x maximale de la carte
     */
    protected double maxX = Double.MIN_VALUE;

    /**
     * Coordonnée y minimale de la carte
     */
    protected double minY = Double.MAX_VALUE;

    /**
     * Coordonnée y maximale de la carte
     */
    protected double maxY = Double.MIN_VALUE;

    /**
     * Padding utilisé pour le calcul de la largeur et de la longueur max de la
     * carte
     */
    protected final double PADDING_CARTE = 10;

    /**
     * Padding utilisé lors du dessin de la carte
     */
    protected double paddingGlobal;

    /**
     * Variable qui permet d'adapter les coordonnées en fonction du ratio de la
     * carte. Il sert notamment à ne pas avoir un effet "aplati" sur la carte
     */
    protected double ratioGlobal;

    /**
     * Constante qui permet de définir la valeur seuil que l'on souhaite comme
     * différence entre les couleurs générées aléatoirement pour les points de
     * collecte et de livraison
     */
    protected final double VALEUR_SEUIL_DIFF_COULEUR = 0.9;

    /**
     * Constante qui permet de définir la valeur seuil permettant de déterminer si
     * une couleur est trop claire ou non
     */
    protected final double VALEUR_SEUIL_COULEUR_CLAIRE = 0.7;

    /**
     * Liste contenant les intersections et les segments de la carte chargées sous
     * forme d'élements graphiques. Cela est utile pour redessiner la carte en cas
     * de changement de fichier de requêtes
     */
    protected List<Node> listeNoeudsCarte;

    /**
     * Map contenant les intersections de la carte chargée mappée avec son ID. Cela
     * est utile afin de pouvoir sélectionner une intersection précise lors de la
     * modification de requêtes.
     */
    protected Map<Long, Circle> mapIntersections;

    /**
     * Liste contenant l'id des intersections sélectionnées sur la carte
     */
    protected List<Long> listIntersectionsSelectionnees;

    /**
     * Liste qui contient les lignes (élément graphique) composant les trajets
     * affichés sur la carte
     */
    protected List<Line> listeLignesTrajets = new ArrayList<>();

    /**
     * Constructeur de VueGraphique.
     */
    public VueGraphique() {

    }

    /**
     * Renvoie le pane de dessin de la vue graphique.
     * 
     * @return Le pane de dessin
     */
    public Pane getPaneDessin() {
        return paneDessin;
    }

    /**
     * Permet de définir un nouveau pane de dessin pour la vue graphique.
     * 
     * @param paneDessin Le nouveau pane de dessin
     */
    public void setPaneDessin(Pane paneDessin) {
        this.paneDessin = paneDessin;
    }

    /**
     * Permet de convertir des coordonnées (longitude, latitude) en coordonnées (x,
     * y) dans le pane de dessin
     * 
     * @param longitude La longitude du point à convertir
     * @param latitude  La latitude du point à convertir
     * @return Un point contenant la paire de coordonnées (x, y)
     */
    public Point2D longLatToXY(double longitude, double latitude) {
        // Conversion de la longitude et latitude en radian
        longitude = longitude * Math.PI / 180;
        latitude = latitude * Math.PI / 180;

        double x = longitude;
        double y = Math.log(Math.tan((Math.PI / 4) + (latitude / 2)));

        return new Point2D(x, y);
    }

    /**
     * Permet d'adapter les coordonnées (x, y) obtenues afin que le dessin de la
     * carte s'ajuste le plus possible à la taille de la zone de dessin
     * 
     * @param x La coordonnée x à adapter
     * @param y La coordonnée y à adapter
     * @return Un point contenant la paire de coordonnées (x, y) adaptées
     */
    public Point2D adapterCoordonnees(double x, double y) {
        double adaptationX = this.paddingGlobal + ((x - this.minX) * this.ratioGlobal);
        double adaptationY = this.paneDessin.getHeight() - this.paddingGlobal - ((y - this.minY) * this.ratioGlobal);
        return new Point2D(adaptationX, adaptationY);
    }

    /**
     * Méthode permettant de n'affichant que la carte en enlevant toutes les
     * requêtes dessinées dessus
     */
    public void nettoyerCarte() {
        this.paneDessin.getChildren().setAll(this.listeNoeudsCarte);
    }

    /**
     * Méthode qui permet d'effacer les trajets affichés dans la vue graphique
     */
    public void effacerTrajets() {
        for (Line l : this.listeLignesTrajets) {
            this.paneDessin.getChildren().remove(l);
        }
        this.listeLignesTrajets.clear();
    }

    /**
     * Cette méthode permet de dessiner la carte dans le pane de la vue graphique,
     * en adaptant les coordonnées des éléments en fonction de la taille de ce pane,
     * en respectant toutefois le ratio longitude/latitude des coordonnées pour ne
     * pas avoir un effet "aplati"
     * 
     * @param carte La carte à dessiner
     */
    public void afficherCarte(Carte carte) {
        this.listeNoeudsCarte = new LinkedList<>();
        this.mapIntersections = new HashMap<>();
        this.listIntersectionsSelectionnees = new LinkedList<>();

        // On enlève tous les éléments graphiques déjà affichés (s'il y en a)
        this.paneDessin.getChildren().clear();

        // On va chercher les coordonnées (x, y) minimales et maximales
        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;

        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            Point2D coordXY = longLatToXY(entry.getValue().getLongitude(), entry.getValue().getLatitude());

            if (coordXY.getX() > this.maxX) {
                this.maxX = coordXY.getX();
            } else if (coordXY.getX() < this.minX) {
                this.minX = coordXY.getX();
            }

            if (coordXY.getY() > this.maxY) {
                this.maxY = coordXY.getY();
            } else if (coordXY.getY() < this.minY) {
                this.minY = coordXY.getY();
            }
        }

        // Offset qui sera appliqué à toutes les coordonnées
        this.maxX -= this.minX;
        this.maxY -= this.minY;

        // On cherche maintenant la largeur et la hauteur maximales que l'on peut avoir
        // en respectant la ratio de la carte
        double largeurCarte = this.paneDessin.getWidth() - this.PADDING_CARTE;
        double hauteurCarte = this.paneDessin.getHeight() - this.PADDING_CARTE;

        double ratioLargeur = largeurCarte / this.maxX;
        double ratioHauteur = hauteurCarte / this.maxY;

        // On prend le ratio le plus petit pour que la carte puisse rentrer dans la zone
        // de dessin
        this.ratioGlobal = Math.min(ratioLargeur, ratioHauteur);

        double paddingLargeur = (this.paneDessin.getWidth() - (this.ratioGlobal * this.maxX)) / 2;
        double paddingHauteur = (this.paneDessin.getHeight() - (this.ratioGlobal * this.maxY)) / 2;

        this.paddingGlobal = Math.min(paddingLargeur, paddingHauteur);

        // On va maintenant parcourir la carte dans le but de dessiner les intersections
        // et les segments
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            // On convertit la longitude et latitude en x et y
            Point2D coordXY = longLatToXY(entry.getValue().getLongitude(), entry.getValue().getLatitude());

            // On adapte le x et y en fonction de la taille de la carte établie avant
            coordXY = adapterCoordonnees(coordXY.getX(), coordXY.getY());

            Circle cercleIntersection = new Circle(coordXY.getX(), coordXY.getY(), 2);
            cercleIntersection.setFill(Color.GRAY);

            // On ajoute l'élément au dessin
            this.listeNoeudsCarte.add(cercleIntersection);
            this.mapIntersections.put(entry.getKey(), cercleIntersection);
            this.paneDessin.getChildren().add(cercleIntersection);

            for (Segment s : entry.getValue().getSegmentsPartants()) {
                Intersection arrivee = carte.getIntersections().get(s.getArrivee());

                // On convertit la longitude et latitude en x et y
                Point2D coordXYArrivee = longLatToXY(arrivee.getLongitude(), arrivee.getLatitude());

                // On adapte le x et y en fonction de la taille de la carte établie avant
                coordXYArrivee = adapterCoordonnees(coordXYArrivee.getX(), coordXYArrivee.getY());

                Line ligneSegment = new Line(coordXY.getX(), coordXY.getY(), coordXYArrivee.getX(),
                        coordXYArrivee.getY());
                ligneSegment.setStroke(Color.BLACK);

                // On change le "viewOrder" pour que les segments apparaissent derrière les
                // intersections
                ligneSegment.setViewOrder(2);

                // Ajoute un tooltip pour afficher le nom de la rue au survol de la souris
                Tooltip tooltipNomRue = new Tooltip(s.getNom());
                tooltipNomRue.setShowDelay(new Duration(0));
                tooltipNomRue.setHideDelay(new Duration(0));
                Tooltip.install(ligneSegment, tooltipNomRue);

                // Ajoute un handler pour augmenter la taille de la route lors du survol
                ligneSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        ligneSegment.setStrokeWidth(ligneSegment.getStrokeWidth() + 3);
                    }
                });

                ligneSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        ligneSegment.setStrokeWidth(ligneSegment.getStrokeWidth() - 3);
                    }
                });

                this.listeNoeudsCarte.add(ligneSegment);
                this.paneDessin.getChildren().add(ligneSegment);
            }
        }
    }

    /**
     * Cette méthode permet de dessiner les requêtes dans le pane de la vue
     * graphique.
     * 
     * @param planning Le planning actuel de l'application, contenant les requêtes
     * @param carte    La carte actuelle de l'application
     */
    public void afficherRequetes(Planning planning, Carte carte, Map<Requete, Color> mapCouleurRequete) {
        // On réinitialise la map d'association Requete <-> Couleur
        mapCouleurRequete.clear();

        nettoyerCarte();

        // Dessin du dépôt (sous la forme d'une étoile)
        Intersection depot = carte.getIntersections().get(planning.getIdDepot());
        Point2D coordDepot = longLatToXY(depot.getLongitude(), depot.getLatitude());
        coordDepot = adapterCoordonnees(coordDepot.getX(), coordDepot.getY());

        double rayonLarge = 8.0;
        double rayonCourt = 3.0;
        Polygon etoileDepot = new Polygon();

        for (int i = 0; i < 10; i++) {
            double xTemp;
            double yTemp;
            if (i % 2 == 0) {
                xTemp = rayonLarge * Math.cos(2 * Math.PI * (i / 2) / 5 - Math.PI / 2);
                yTemp = rayonLarge * Math.sin(2 * Math.PI * (i / 2) / 5 - Math.PI / 2);
            } else {
                xTemp = rayonCourt * Math.cos(2 * Math.PI * (i / 2) / 5 - Math.PI / 2 + Math.PI / 5);
                yTemp = rayonCourt * Math.sin(2 * Math.PI * (i / 2) / 5 - Math.PI / 2 + Math.PI / 5);
            }
            etoileDepot.getPoints().addAll(xTemp + coordDepot.getX(), yTemp + coordDepot.getY());
        }

        etoileDepot.setFill(Color.RED);
        this.paneDessin.getChildren().add(etoileDepot);

        // Liste des couleurs qui auront été générées aléatoirement
        List<Color> couleursDejaPresentes = new LinkedList<Color>();

        for (Requete requete : planning.getRequetes()) {
            Intersection collecte = carte.getIntersections().get(requete.getDemandeCollecte().getIdIntersection());
            Intersection livraison = carte.getIntersections().get(requete.getDemandeLivraison().getIdIntersection());

            Point2D coordCollecte = longLatToXY(collecte.getLongitude(), collecte.getLatitude());
            coordCollecte = adapterCoordonnees(coordCollecte.getX(), coordCollecte.getY());

            Point2D coordLivraison = longLatToXY(livraison.getLongitude(), livraison.getLatitude());
            coordLivraison = adapterCoordonnees(coordLivraison.getX(), coordLivraison.getY());

            // On va générer une couleur aléatoire qui est suffisament différente des
            // couleurs déjà présentes (cela est déterminé grâce à la constante
            // VALEUR_SEUIL_DIFF_COULEUR). On regarde également si la couleur n'est pas trop
            // claire.
            boolean couleurSimilairePresente;
            int maxIterations = 1000;
            int nbIterations = 0;
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            do {
                nbIterations++;
                couleurSimilairePresente = false;
                // On vérifie que la couleur ne soit pas trop claire
                if (couleur.getRed() > VALEUR_SEUIL_COULEUR_CLAIRE && couleur.getGreen() > VALEUR_SEUIL_COULEUR_CLAIRE
                        && couleur.getBlue() > VALEUR_SEUIL_COULEUR_CLAIRE) {
                    couleur = Color.color(couleur.getRed() - 0.2, couleur.getGreen() - 0.2, couleur.getBlue() - 0.2);
                }

                for (Color c : couleursDejaPresentes) {
                    // Pour déterminer si une couleur est proche d'une autre, on calcule la somme
                    // des valeurs absolues des différences entre les 3 composantes RVB des couleurs
                    double sommeDiffComposantes = Math.abs(c.getRed() - couleur.getRed())
                            + Math.abs(c.getGreen() - couleur.getGreen()) + Math.abs(c.getBlue() - couleur.getBlue());
                    if (sommeDiffComposantes < VALEUR_SEUIL_DIFF_COULEUR) {
                        couleur = Color.color(Math.random(), Math.random(), Math.random());
                        couleurSimilairePresente = true;
                        break;
                    }
                }
            } while (couleurSimilairePresente && nbIterations < maxIterations);

            couleursDejaPresentes.add(couleur);

            // On ajoute l'association Requete <-> Couleur dans la map
            mapCouleurRequete.put(requete, couleur);

            // Pour le point de collecte, on crée un carré
            Rectangle rectangleCollecte = new Rectangle(coordCollecte.getX() - 5, coordCollecte.getY() - 5, 10, 10);
            rectangleCollecte.setFill(couleur);

            // Pour le point de livraison on crée un rond
            Circle cercleLivraison = new Circle(coordLivraison.getX(), coordLivraison.getY(), 5);
            cercleLivraison.setFill(couleur);

            this.paneDessin.getChildren().addAll(rectangleCollecte, cercleLivraison);
        }
    }

    /**
     * Cette méthode permet de dessiner le trajet passé en paramètre avec la couleur
     * choisie.
     * 
     * @param carte   La carte actuelle de l'application, contenant les
     *                intersections
     * @param trajet  Le trajet à dessiner
     * @param couleur La couleur du trajet
     */
    public void afficherTrajet(Carte carte, Trajet trajet, Color couleur) {
        // On parcourt tous les segments composant le trajet
        for (Segment segment : trajet.getListeSegments()) {
            // On calcule les coordonnées du départ et de l'arrivée
            Intersection depart = carte.getIntersections().get(segment.getDepart());

            Point2D coordDepart = longLatToXY(depart.getLongitude(), depart.getLatitude());
            coordDepart = adapterCoordonnees(coordDepart.getX(), coordDepart.getY());

            Intersection arrivee = carte.getIntersections().get(segment.getArrivee());

            Point2D coordArrivee = longLatToXY(arrivee.getLongitude(), arrivee.getLatitude());
            coordArrivee = adapterCoordonnees(coordArrivee.getX(), coordArrivee.getY());

            // On crée une ligne pour le segment
            Line ligneSegment = new Line(coordDepart.getX(), coordDepart.getY(), coordArrivee.getX(),
                    coordArrivee.getY());
            ligneSegment.setStroke(couleur);
            ligneSegment.setStrokeWidth(3.0);

            // On change le "viewOrder" pour que les trajets apparaissent derrière les
            // intersections
            ligneSegment.setViewOrder(2);

            this.paneDessin.getChildren().add(ligneSegment);
            this.listeLignesTrajets.add(ligneSegment);
        }

        // TODO : méthode ajouterRequete (permet l'ajout sur la vueGraphique)
    }

    /**
     * Attache les EventHandler aux intersections de la carte chargée afin de
     * préparer la sélection d'intersection
     *
     * @param controleur Le controleur de l'application
     */
    public void attacherHandlerIntersection(Controleur controleur) {
        for (Map.Entry<Long, Circle> entry : this.mapIntersections.entrySet()) {
            entry.getValue().setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    controleur.selectionnerIntersection(entry.getKey());
                }
            });

            // Ajoute un handler pour augmenter la taille de l'intersection lors du survol
            entry.getValue().setOnMouseEntered(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (!listIntersectionsSelectionnees.contains(entry.getKey())) {
                        entry.getValue().setRadius(3.5D);
                        entry.getValue().setFill(Color.INDIANRED);
                    }
                }
            });

            entry.getValue().setOnMouseExited(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (!listIntersectionsSelectionnees.contains(entry.getKey())) {
                        entry.getValue().setRadius(2D);
                        entry.getValue().setFill(Color.GRAY);
                    }
                }
            });
        }
    }

    /**
     * Sélectionne une intersection
     */
    public void selectionneIntersection(Long idIntersection) {
        if (!listIntersectionsSelectionnees.contains(idIntersection)) {
            listIntersectionsSelectionnees.add(idIntersection);
            mapIntersections.get(idIntersection).setFill(Color.RED);
            mapIntersections.get(idIntersection).setRadius(4D);
        }
    }

    /**
     * Déselectionne une intersection
     */
    public void deselectionneIntersection(Long idIntersection) {
        if (listIntersectionsSelectionnees.contains(idIntersection)) {
            listIntersectionsSelectionnees.remove(idIntersection);
            mapIntersections.get(idIntersection).setFill(Color.GRAY);
            mapIntersections.get(idIntersection).setRadius(2D);
        }
    }

    /**
     * Déselectionne la totalité des intersections sélectionnées
     */
    public void nettoyerIntersectionsSelectionnees() {
        for (Long l : listIntersectionsSelectionnees) {
            this.deselectionneIntersection(l);
        }
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getPADDING_CARTE() {
        return PADDING_CARTE;
    }
}
