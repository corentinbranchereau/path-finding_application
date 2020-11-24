package fr.hexaone.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.Trajet;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Permet d'afficher la partie graphique de l'IHM.
 * 
 * @author HexaOne
 * @version 1.0
 */

public class VueGraphique {

    /**
     * Canvas dans lequel sont dessinés les différents éléments (carte, segments,
     * etc.)
     */
    protected Canvas canvas;

    /**
     * Longitude minimale dans la carte
     */
    protected double minLongitude = Double.MAX_VALUE;

    /**
     * Longitude maximale dans la carte
     */
    protected double maxLongitude = Double.MIN_VALUE;

    /**
     * Latitude minimale dans la carte
     */
    protected double minLatitude = Double.MAX_VALUE;

    /**
     * Latitude maximale dans la carte
     */
    protected double maxLatitude = Double.MIN_VALUE;

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
     * Constructeur de VueGraphique.
     */
    public VueGraphique() {

    }

    /**
     * Renvoie le canvas dans lequel on dessine les éléments.
     * 
     * @return Le canvas de la vue graphique
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Change la valeur du canvas.
     * 
     * @param canvas Le nouveau canvas.
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Cette méthode permet de dessiner la carte dans le canvas, en adaptant les
     * coordonnées des éléments en fonction de la taille du canvas.
     * 
     * @param carte      La carte à dessiner
     * @param redessiner Paramètre indiquant si la carte passée en paramètre a déjà
     *                   été affichée précédemment, dans le cas où la première étape
     *                   de calcul n'est pas nécessaire
     */
    public void afficherCarte(Carte carte, boolean redessiner) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setLineWidth(1.0);

        // On efface le canvas au cas où une carte est déjà affichée.
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        if (!redessiner) {
            // Tout d'abord, on recherche le min et le max des longitudes et latitudes pour
            // pouvoir adapter l'affichage en conséquence
            this.minLongitude = Double.MAX_VALUE;
            this.maxLongitude = Double.MIN_VALUE;
            this.minLatitude = Double.MAX_VALUE;
            this.maxLatitude = Double.MIN_VALUE;
            for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
                if (entry.getValue().getLongitude() > this.maxLongitude) {
                    this.maxLongitude = entry.getValue().getLongitude();
                } else if (entry.getValue().getLongitude() < this.minLongitude) {
                    this.minLongitude = entry.getValue().getLongitude();
                }

                if (entry.getValue().getLatitude() > this.maxLatitude) {
                    this.maxLatitude = entry.getValue().getLatitude();
                } else if (entry.getValue().getLatitude() < this.minLatitude) {
                    this.minLatitude = entry.getValue().getLatitude();
                }
            }
        }

        // On parcourt ensuite tous les segments (via les intersections) pour les
        // afficher en adaptant les coordonnées
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            double xPos = (entry.getValue().getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yPos = (entry.getValue().getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);
            // On traite les coordonnées y pour enlever l'effet "miroir"
            yPos = -yPos + this.canvas.getHeight();

            // On dessine l'intersection
            gc.setFill(Color.GRAY);
            gc.fillOval(xPos - 2, yPos - 2, 4, 4);

            for (Segment s : entry.getValue().getSegmentsPartants()) {
                Intersection arrivee = carte.getIntersections().get(s.getArrivee());
                double xPos2 = (arrivee.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                        / (this.maxLongitude - this.minLongitude);
                double yPos2 = (arrivee.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                        / (this.maxLatitude - this.minLatitude);
                // On traite les coordonnées y pour enlever l'effet "miroir"
                yPos2 = -yPos2 + this.canvas.getHeight();

                // On dessine le segment
                gc.setFill(Color.BLACK);
                gc.strokeLine(xPos, yPos, xPos2, yPos2);
            }
        }
    }

    /**
     * Cette méthode permet de dessiner les requêtes dans le canvas.
     * 
     * @param planning Le planning actuel de l'application, contenant les requêtes
     * @param carte    La carte actuelle de l'application
     */
    public void afficherRequetes(Planning planning, Carte carte, Map<Requete, Color> mapCouleurRequete) {
        // On réinitialise la map d'association Requete <-> Couleur
        mapCouleurRequete.clear();

        GraphicsContext gc = this.canvas.getGraphicsContext2D();

        // Dessin du dépôt (sous la forme d'une étoile)
        Intersection depot = carte.getIntersections().get(planning.getIdDepot());
        double xDepot = (depot.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                / (this.maxLongitude - this.minLongitude);
        double yDepot = (depot.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                / (this.maxLatitude - this.minLatitude);
        // On traite les coordonnées y pour enlever l'effet "miroir"
        yDepot = -yDepot + this.canvas.getHeight();

        double rayonLarge = 8.0;
        double rayonCourt = 3.0;

        double[] positionsX = new double[10];
        double[] positionsY = new double[10];

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
            positionsX[i] = xTemp + xDepot;
            positionsY[i] = yTemp + yDepot;
        }

        gc.setFill(Color.RED);
        gc.fillPolygon(positionsX, positionsY, 10);

        // Liste des couleurs qui auront été générées aléatoirement
        List<Color> couleursDejaPresentes = new LinkedList<Color>();

        for (Requete requete : planning.getRequetes()) {
            Intersection collecte = carte.getIntersections().get(requete.getIdPickup());
            Intersection livraison = carte.getIntersections().get(requete.getIdDelivery());

            double xCollecte = (collecte.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yCollecte = (collecte.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);
            // On traite les coordonnées y pour enlever l'effet "miroir"
            yCollecte = -yCollecte + this.canvas.getHeight();

            double xLivraison = (livraison.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yLivraison = (livraison.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);
            // On traite les coordonnées y pour enlever l'effet "miroir"
            yLivraison = -yLivraison + this.canvas.getHeight();

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
            gc.setFill(couleur);

            // On ajoute l'association Requete <-> Couleur dans la map
            mapCouleurRequete.put(requete, couleur);

            // Pour le point de collecte, on dessine un carré
            gc.fillRect(xCollecte - 5, yCollecte - 5, 10, 10);
            // Pour le point de livraison on dessine un rond
            gc.fillOval(xLivraison - 5, yLivraison - 5, 10, 10);
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
        GraphicsContext gc = this.canvas.getGraphicsContext2D();

        // On parcourt tous les segments composant le trajet
        for (Segment segment : trajet.getListeSegments()) {
            // On calcule les coordonnées du départ et de l'arrivée
            Intersection depart = carte.getIntersections().get(segment.getDepart());
            double xDepart = (depart.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yDepart = (depart.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);
            yDepart = -yDepart + this.canvas.getHeight();

            Intersection arrivee = carte.getIntersections().get(segment.getArrivee());
            double xArrivee = (arrivee.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yArrivee = (arrivee.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);
            yArrivee = -yArrivee + this.canvas.getHeight();

            // On affiche le segment
            gc.setStroke(couleur);
            gc.setLineWidth(3.0);
            gc.strokeLine(xDepart, yDepart, xArrivee, yArrivee);

            // On redessine par dessus les intersections correspondantes
            gc.setFill(Color.GRAY);
            gc.fillOval(xDepart - 2, yDepart - 2, 4, 4);
            gc.fillOval(xArrivee - 2, yArrivee - 2, 4, 4);

            gc.setFill(couleur);
            gc.fillOval(xDepart - 1, yDepart - 1, 2, 2);
            gc.fillOval(xArrivee - 1, yArrivee - 1, 2, 2);
        }
    }
}
