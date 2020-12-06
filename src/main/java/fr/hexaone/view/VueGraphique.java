package fr.hexaone.view;

import fr.hexaone.model.*;
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

import java.util.*;

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
    private Pane paneDessin;

    /**
     * Coordonnée x minimale de la carte
     */
    private double minX = Double.MAX_VALUE;

    /**
     * Coordonnée x maximale de la carte
     */
    private double maxX = Double.MIN_VALUE;

    /**
     * Coordonnée y minimale de la carte
     */
    private double minY = Double.MAX_VALUE;

    /**
     * Coordonnée y maximale de la carte
     */
    private double maxY = Double.MIN_VALUE;

    /**
     * Padding utilisé pour le calcul de la largeur et de la longueur max de la
     * carte
     */
    private final double PADDING_CARTE = 10;

    /**
     * Padding utilisé lors du dessin de la carte
     */
    private double paddingGlobal;

    /**
     * Variable qui permet d'adapter les coordonnées en fonction du ratio de la
     * carte. Il sert notamment à ne pas avoir un effet "aplati" sur la carte
     */
    private double ratioGlobal;

    /**
     * Constante qui permet de définir la valeur seuil que l'on souhaite comme
     * différence entre les couleurs générées aléatoirement pour les points de
     * collecte et de livraison
     */
    private final double VALEUR_SEUIL_DIFF_COULEUR = 0.9;

    /**
     * Constante qui permet de définir la valeur seuil permettant de déterminer si
     * une couleur est trop claire ou non
     */
    private final double VALEUR_SEUIL_COULEUR_CLAIRE = 0.7;

    /**
     * Map contenant les intersections de la carte chargée mappée avec son ID. Cela
     * est utile afin de pouvoir sélectionner une intersection précise lors de la
     * modification de requêtes.
     */
    private Map<Long, Circle> mapIntersections;

    /**
     * Liste contenant l'id des intersections sélectionnées sur la carte
     */
    private List<Long> listIntersectionsSelectionnees;

    /**
     * La fenêtre de l'application à laquelle est reliée la vue graphique
     */
    private Fenetre fenetre;

    /**
     * Map qui permet de relier un objet Demande à sa représentation graphique
     * (carré ou rond)
     */
    private Map<Demande, Node> mapDemandeNoeud;

    /**
     * Variable définissant la taille d'un noeud (élément graphique) de demande
     * (collecte ou livraison)
     */
    private final double TAILLE_NOEUD_DEMANDE = 5;

    /**
     * Variable définissant la taille d'un noeud (élément graphique) de demande
     * (collecte ou livraison) qui a été sélectionné/highlight
     */
    private final double TAILLE_NOEUD_HIGHLIGHT = 10;

    /**
     * Variable définissant la taille d'un noeud (élément graphique) secondaire,
     * c'est-à-dire le point de collecte pour une livraison et inversement, dont
     * l'autre noeud a été highlight
     */
    private final double TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT = 8;

    /**
     * Taille de liste des noeuds correspondant au dessin de la carte (segments et
     * intersections)
     */
    private int tailleListeNoeudsCarte;

    /**
     * Map qui contient pour chaque trajet sa couleur d'affichage
     */
    private Map<Trajet, Color> mapCouleurTrajet;

    /**
     * Angle Hue (modèle HSB) du début de l'intervalle des couleurs de trajets
     */
    private final double DEBUT_INTERVALLE_COULEURS_TRAJETS = 60;

    /**
     * Angle Hue (modèle HSB) de la fin de l'intervalle des couleurs de trajets
     */
    private final double FIN_INTERVALLE_COULEURS_TRAJETS = 180;

    /**
     * Constructeur de VueGraphique
     * 
     * @param fenetre La fenêtre de l'application à laquelle est reliée la vue
     *                graphique
     */
    public VueGraphique(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    /**
     * Permet de redessiner la vue graphique en prenant en compte tous les éléments
     * du modèles qui existent
     * 
     * @param planning            Le planning actuel, dans lequel on retrouve les
     *                            autres variables (carte, requêtes, etc.)
     * @param demandeSelectionnee La demande qui a été sélectionnée par
     *                            l'utilisateur
     * @param dessinerCarte       Indique s'il y a besoin de redessiner les noeuds
     *                            de la carte (segments et intersections)
     */
    public void rafraichir(Planning planning, Demande demandeSelectionnee, boolean dessinerCarte) {
        if (planning == null)
            return;

        if (planning.getCarte() != null) {
            if (dessinerCarte) {
                // Réinitialisation de la vue
                this.paneDessin.getChildren().clear();
                this.tailleListeNoeudsCarte = 0;
                // Affichage de la carte
                afficherCarte(planning.getCarte());
            } else {
                // On efface les noeuds qui ne font pas partie de Carte (ceux qui ne sont pas
                // des segments ou intersections)
                this.paneDessin.getChildren().remove(this.tailleListeNoeudsCarte, this.paneDessin.getChildren().size());
            }
        }

        // Affichage des demandes sur la carte
        if (planning.getDemandesOrdonnees() != null) {
            // Affichage des demandes sur la carte
            afficherDemandes(planning.getDemandesOrdonnees(), planning.getCarte(), planning.getIdDepot());

            if (planning.getListeTrajets() != null) {
                // Affichage de la demande sélectionnée et de la demande associée
                // TODO : actuellement ne marche pas avant d'avoir calculé --> à voir
                if (demandeSelectionnee != null) {
                    Requete requeteAssociee = demandeSelectionnee.getRequete();

                    if (demandeSelectionnee.getTypeIntersection() == TypeIntersection.COLLECTE) {
                        // Mise en valeur forte de la collecte
                        highlightDemande(demandeSelectionnee, false);

                        if (requeteAssociee != null && requeteAssociee.getDemandeLivraison() != null) {
                            Demande demandeAssociee = requeteAssociee.getDemandeLivraison();
                            // Mise en valeur faible de la livraison
                            highlightDemande(demandeAssociee, true);

                        }
                    } else if (demandeSelectionnee.getTypeIntersection() == TypeIntersection.LIVRAISON) {
                        // Mise en valeur forte de la livraison
                        highlightDemande(demandeSelectionnee, false);

                        if (requeteAssociee != null && requeteAssociee.getDemandeCollecte() != null) {
                            Demande demandeAssociee = requeteAssociee.getDemandeCollecte();
                            // Mise en valeur faible de la collecte
                            highlightDemande(demandeAssociee, true);
                        }
                    } else {
                        // TODO : Mise en valeur du dépot
                    }

                    List<Trajet> trajetsAvantApresDemande = new ArrayList<>();
                    int index = planning.getDemandesOrdonnees().indexOf(demandeSelectionnee);
                    if (index != -1) {
                        // L'index a été trouvé
                        trajetsAvantApresDemande.add(planning.getListeTrajets().get(index));

                        if (index + 1 < planning.getListeTrajets().size()) {
                            trajetsAvantApresDemande.add(planning.getListeTrajets().get(index + 1));
                        }

                        // Génération des couleurs pour les trajets
                        genererCouleursTrajets(planning.getListeTrajets());

                        // Affichage des trajets
                        for (Trajet trajet : trajetsAvantApresDemande) {
                            afficherTrajet(planning.getCarte(), trajet);
                        }
                    }
                } else {
                    // Pas de demande sélectionnée : on affiche tous les trajets
                    // Génération des couleurs pour les trajets
                    genererCouleursTrajets(planning.getListeTrajets());

                    // Affichage des trajets
                    for (Trajet trajet : planning.getListeTrajets()) {
                        afficherTrajet(planning.getCarte(), trajet);
                    }
                }
            }
        } else if (!planning.getRequetes().isEmpty()) {
            // Si les demandes n'ont pas encore été calculées, on affiche les requetes.
            List<Demande> demandes = new ArrayList<Demande>();
            for (Requete requete : planning.getRequetes()) {
                if (requete.getDemandeCollecte() != null) {
                    demandes.add(requete.getDemandeCollecte());
                }
                if (requete.getDemandeLivraison() != null) {
                    demandes.add(requete.getDemandeLivraison());
                }
            }
            // Affichage des demandes
            afficherDemandes(demandes, planning.getCarte(), planning.getIdDepot());
        }
    }

    /**
     * Permet de convertir des coordonnées (longitude, latitude) en coordonnées (x,
     * y) dans le pane de dessin
     * 
     * @param longitude La longitude du point à convertir
     * @param latitude  La latitude du point à convertir
     * @return Un point contenant la paire de coordonnées (x, y)
     */
    private Point2D longLatToXY(double longitude, double latitude) {
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
     * Méthode permettant de calculer les paramètres servant à adapter l'affichage
     * de la carte dans la fenêtre et la taille de la fenêtre
     * 
     * @param carte
     */
    public void calculAdaptationCarte(Carte carte) {
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
    }

    /**
     * Méthode qui permet de générer des couleurs pour chaque requête de la liste
     * passée en paramètre. Les couleurs générées seront les plus différentes
     * possibles les unes des autres et ne seront pas trop claires (meilleure
     * visibilité)
     * 
     * @param requetes La liste des requêtes pour lesquelles il faut générer des
     *                 couleurs
     */
    public void genererCouleursRequetes(List<Requete> requetes) {
        // On réinitialise la map d'association Requete <-> Couleur
        this.fenetre.getMapCouleurRequete().clear();

        Set<Color> couleursDejaPresentes = new HashSet<>();

        for (Requete requete : requetes) {
            Color couleur = genereCouleurAleatoire(couleursDejaPresentes);

            // On ajoute l'association Requete <-> Couleur dans la map
            this.fenetre.getMapCouleurRequete().put(requete, couleur);

            couleursDejaPresentes.add(couleur);
        }
    }

    /**
     * Permet de générer une couleur par trajet en réalisant un dégradé entre
     * l'angle (angle hue du modèle de couleurs HSB) de début et de fin définis
     * 
     * @param trajets La liste des trajets pour lesquels il faut générer des
     *                couleurs
     */
    public void genererCouleursTrajets(List<Trajet> trajets) {
        this.mapCouleurTrajet = new HashMap<>();

        double intervalleAngle = this.FIN_INTERVALLE_COULEURS_TRAJETS - this.DEBUT_INTERVALLE_COULEURS_TRAJETS;
        double pasIntervalle = intervalleAngle / trajets.size();

        for (int i = 0; i < trajets.size(); i++) {
            double angle = this.DEBUT_INTERVALLE_COULEURS_TRAJETS + i * pasIntervalle;
            Color couleurTrajet = Color.hsb(angle, 1.0, 1.0);
            this.mapCouleurTrajet.put(trajets.get(i), couleurTrajet);
        }
    }

    /**
     * Génère une couleur aléatoire assez différente de celles présentes dans la
     * liste passée en paramètre
     * 
     * @param couleursDejaPresentes La liste des couleurs déjà utilisées
     * @return La couleur générée
     */
    private Color genereCouleurAleatoire(Collection<Color> couleursDejaPresentes) {
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
        return couleur;
    }

    /**
     * Cette méthode permet de dessiner la carte dans le pane de la vue graphique,
     * en adaptant les coordonnées des éléments en fonction de la taille de ce pane,
     * en respectant toutefois le ratio longitude/latitude des coordonnées pour ne
     * pas avoir un effet "aplati"
     * 
     * @param carte La carte à dessiner
     */
    private void afficherCarte(Carte carte) {
        // this.listeNoeudsCarte = new LinkedList<>();
        this.mapIntersections = new HashMap<>();
        this.listIntersectionsSelectionnees = new LinkedList<>();

        // On parcourt la carte dans le but de dessiner les intersections
        // et les segments
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            // On convertit la longitude et latitude en x et y
            Point2D coordXY = longLatToXY(entry.getValue().getLongitude(), entry.getValue().getLatitude());

            // On adapte le x et y en fonction de la taille de la carte établie avant
            coordXY = adapterCoordonnees(coordXY.getX(), coordXY.getY());

            Circle cercleIntersection = new Circle(coordXY.getX(), coordXY.getY(), 2);
            cercleIntersection.setFill(Color.GRAY);

            // Ajout des handlers sur l'intersection
            // Handler pour sélectionner l'intersection
            cercleIntersection.setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    fenetre.getControleur().selectionnerIntersection(entry.getKey());
                }
            });

            // Ajoute un handler pour augmenter la taille de l'intersection lors du survol
            cercleIntersection.setOnMouseEntered(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (!listIntersectionsSelectionnees.contains(entry.getKey())) {
                        cercleIntersection.setRadius(3.5D);
                        cercleIntersection.setViewOrder(-1.0);
                        cercleIntersection.setFill(Color.INDIANRED);
                    }
                }
            });

            cercleIntersection.setOnMouseExited(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (!listIntersectionsSelectionnees.contains(entry.getKey())) {
                        cercleIntersection.setRadius(2D);
                        cercleIntersection.setViewOrder(0.0);
                        cercleIntersection.setFill(Color.GRAY);
                    }
                }
            });

            // On ajoute l'élément au dessin
            this.mapIntersections.put(entry.getKey(), cercleIntersection);
            this.paneDessin.getChildren().add(cercleIntersection);
            this.tailleListeNoeudsCarte++;

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

                this.paneDessin.getChildren().add(ligneSegment);
                this.tailleListeNoeudsCarte++;
            }
        }
    }

    /**
     * Affiche la liste des demandes passées en paramètres sur le pane de dessin
     * 
     * @param demandes La liste des demandes à afficher
     * @param carte    La carte actuellement chargée dans l'application
     * @param idDepot  L'identifiant du dépôt
     */
    private void afficherDemandes(List<Demande> demandes, Carte carte, Long idDepot) {
        // Initialisation de la map reliant les demandes aux objets graphiques
        this.mapDemandeNoeud = new HashMap<>();

        // Dessin du dépôt (sous la forme d'une étoile)
        Intersection depot = carte.getIntersections().get(idDepot);
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

        for (Demande demande : demandes) {
            afficherDemande(carte, demande);
        }
    }

    /**
     * Cette méthode permet de dessiner une demande dans le pane de la vue
     * graphique.
     * 
     * @param carte   La carte actuelle de l'application
     * @param demande La demande devant être dessinée
     */
    private void afficherDemande(Carte carte, Demande demande) {
        Intersection intersection = carte.getIntersections().get(demande.getIdIntersection());

        Point2D coord = longLatToXY(intersection.getLongitude(), intersection.getLatitude());
        coord = adapterCoordonnees(coord.getX(), coord.getY());

        Color couleur = this.fenetre.getMapCouleurRequete().get(demande.getRequete());

        if (couleur == null) {
            // La couleur n'a pas été trouvée (par exemple c'est une requête que
            // l'utilisateur a ajouté)
            Collection<Color> couleursDejaPresentes = this.fenetre.getMapCouleurRequete().values();
            couleur = genereCouleurAleatoire(couleursDejaPresentes);

            // On ajoute l'association Requete <-> Couleur dans la map
            this.fenetre.getMapCouleurRequete().put(demande.getRequete(), couleur);
        }

        if (demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
            // Collecte
            // Pour le point de collecte, on crée un carré
            Rectangle rectangleCollecte = new Rectangle(coord.getX() - 5, coord.getY() - 5, 10, 10);
            rectangleCollecte.setFill(couleur);

            // On ajoute l'association Requete <-> Couleur dans la map
            this.fenetre.getMapCouleurRequete().put(demande.getRequete(), couleur);

            // On ajoute un handler pour sélectionner la demande
            rectangleCollecte.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    fenetre.getControleur().setDemandeSelectionnee(demande);
                }
            });

            // On ajoute l'association Demande <-> Noeud à la map
            this.mapDemandeNoeud.put(demande, rectangleCollecte);

            // On ajoute le point de collecte au pane de dessin
            this.paneDessin.getChildren().addAll(rectangleCollecte);

        } else {
            // Livraison
            // Pour le point de livraison on crée un rond
            Circle cercleLivraison = new Circle(coord.getX(), coord.getY(), 5);
            cercleLivraison.setFill(couleur);

            // On ajoute l'association Requete <-> Couleur dans la map
            this.fenetre.getMapCouleurRequete().put(demande.getRequete(), couleur);

            // On ajoute un handler pour sélectionner la demande
            cercleLivraison.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    fenetre.getControleur().setDemandeSelectionnee(demande);
                }
            });

            // On ajoute l'association Demande <-> Noeud à la map
            this.mapDemandeNoeud.put(demande, cercleLivraison);

            // On ajoute le point de livraison au pane de dessin
            this.paneDessin.getChildren().addAll(cercleLivraison);
        }
    }

    /**
     * Cette méthode permet de dessiner le trajet passé en paramètre avec la couleur
     * choisie.
     * 
     * @param carte   La carte actuelle de l'application, contenant les
     *                intersections
     * @param trajet  Le trajet à dessiner
     */
    private void afficherTrajet(Carte carte, Trajet trajet) {
        // On récupère la couleur du trajet
        Color couleur = this.mapCouleurTrajet.get(trajet);

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
            ligneSegment.setStrokeWidth(6.0);

            // On change le "viewOrder" pour que les trajets apparaissent derrière les
            // intersections
            ligneSegment.setViewOrder(2);

            // Ajoute un tooltip pour afficher le nom de la rue au survol de la souris
            Tooltip tooltipNomRue = new Tooltip(segment.getNom());
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

            this.paneDessin.getChildren().add(ligneSegment);
        }
    }

    /**
     * Sélectionne une intersection
     * 
     * @param idIntersection L'identifiant de l'intersection à sélectionner
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
     * 
     * @param idIntersection L'identifiant de l'intersection à sélectionner
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

    /**
     * Méthode permettant de sélectionner (highlight) une demande sur la carte
     * 
     * @param demande La demande à sélectionner
     */
    private void highlightDemande(Demande demande, boolean attenuation) {
        Node n = this.mapDemandeNoeud.get(demande);

        if (n instanceof Rectangle) {
            // Demande de collecte
            Rectangle rectangleCollecte = (Rectangle) n;

            if (attenuation) {
                // Demande associée à celle sélectionnée
                rectangleCollecte.setWidth(this.TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT * 2);
                rectangleCollecte.setHeight(this.TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT * 2);
                rectangleCollecte.setX(
                        rectangleCollecte.getX() + this.TAILLE_NOEUD_DEMANDE - this.TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT);
                rectangleCollecte.setY(
                        rectangleCollecte.getY() + this.TAILLE_NOEUD_DEMANDE - this.TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT);

            } else {
                // Demande sélectionnée
                rectangleCollecte.setWidth(this.TAILLE_NOEUD_HIGHLIGHT * 2);
                rectangleCollecte.setHeight(this.TAILLE_NOEUD_HIGHLIGHT * 2);
                rectangleCollecte
                        .setX(rectangleCollecte.getX() + this.TAILLE_NOEUD_DEMANDE - this.TAILLE_NOEUD_HIGHLIGHT);
                rectangleCollecte
                        .setY(rectangleCollecte.getY() + this.TAILLE_NOEUD_DEMANDE - this.TAILLE_NOEUD_HIGHLIGHT);
            }
        } else if (n instanceof Circle) {
            // Demande de livraison
            Circle cercleLivraison = (Circle) n;

            if (attenuation) {
                // Demande associée à celle sélectionnée
                cercleLivraison.setRadius(this.TAILLE_NOEUD_SECONDAIRE_HIGHLIGHT);
            } else {
                // Demande sélectionnée
                cercleLivraison.setRadius(this.TAILLE_NOEUD_HIGHLIGHT);
            }
        }
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
     * Getter
     * 
     * @return Le minX
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Getter
     * 
     * @return Le maxX
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Getter
     * 
     * @return Le minY
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Getter
     * 
     * @return Le maxY
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * Getter
     * 
     * @return Le padding de la carte
     */
    public double getPADDING_CARTE() {
        return PADDING_CARTE;
    }
}