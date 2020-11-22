package fr.hexaone.view;

import java.util.Map;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
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
     * Canvas de sauvegarde qui n'a que la carte d'affichée. Cela permet, en cas de
     * changement de fichier de requêtes, d'effacer les précédentes requêtes.
     */
    protected Canvas canvasSauvegardeCarte;

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

        // On efface le canvas au cas où une carte est déjà affichée.
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

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

        // On parcourt ensuite tous les segments (via les intersections) pour les
        // afficher en adaptant les coordonnées
        for (Map.Entry<Long, Intersection> entry : carte.getIntersections().entrySet()) {
            double xPos = (entry.getValue().getLongitude() - this.minLongitude) * this.canvas.getWidth()
                    / (this.maxLongitude - this.minLongitude);
            double yPos = (entry.getValue().getLatitude() - this.minLatitude) * this.canvas.getHeight()
                    / (this.maxLatitude - this.minLatitude);

            for (Segment s : entry.getValue().getSegmentsPartants()) {
                Intersection arrivee = carte.getIntersections().get(s.getArrivee());
                double xPos2 = (arrivee.getLongitude() - this.minLongitude) * this.canvas.getWidth()
                        / (this.maxLongitude - this.minLongitude);
                double yPos2 = (arrivee.getLatitude() - this.minLatitude) * this.canvas.getHeight()
                        / (this.maxLatitude - this.minLatitude);

                // On dessine le segment
                gc.strokeLine(xPos, yPos, xPos2, yPos2);
            }
        }

        this.canvasSauvegardeCarte = canvas;
    }
}
