package fr.hexaone.view;

import java.util.Map;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Segment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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
     * @param carte La carte à dessiner
     */
    public void afficherCarte(Carte carte) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setLineWidth(1.0);

        // Tout d'abord, on recherche le min et le max des longitudes et latitudes pour
        // pouvoir adapter l'affichage en conséquence
        double minLongitude = Double.MAX_VALUE;
        double maxLongitude = Double.MIN_VALUE;
        double minLatitude = Double.MAX_VALUE;
        double maxLatitude = Double.MIN_VALUE;
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            if (entry.getValue().getLongitude() > maxLongitude) {
                maxLongitude = entry.getValue().getLongitude();
            } else if (entry.getValue().getLongitude() < minLongitude) {
                minLongitude = entry.getValue().getLongitude();
            }

            if (entry.getValue().getLatitude() > maxLatitude) {
                maxLatitude = entry.getValue().getLatitude();
            } else if (entry.getValue().getLatitude() < minLatitude) {
                minLatitude = entry.getValue().getLatitude();
            }
        }

        // On parcourt ensuite tous les segments (via les intersections) pour les
        // afficher en adaptant les coordonnées
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            double xPos = (entry.getValue().getLongitude() - minLongitude) * this.canvas.getWidth()
                    / (maxLongitude - minLongitude);
            double yPos = (entry.getValue().getLatitude() - minLatitude) * this.canvas.getHeight()
                    / (maxLatitude - minLatitude);

            for (Segment s : entry.getValue().getSegmentsPartants()) {
                Intersection arrivee = carte.getIntersections().get(s.getArrivee());
                double xPos2 = (arrivee.getLongitude() - minLongitude) * this.canvas.getWidth()
                        / (maxLongitude - minLongitude);
                double yPos2 = (arrivee.getLatitude() - minLatitude) * this.canvas.getHeight()
                        / (maxLatitude - minLatitude);
                gc.moveTo(xPos, yPos);
                gc.lineTo(xPos2, yPos2);
                gc.stroke();
            }
        }
    }
}
