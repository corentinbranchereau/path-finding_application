package fr.hexaone.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import javafx.scene.control.TextArea;

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
    protected TextArea zoneTexte;

    /**
     * constructeur
     */
    public VueTextuelle() {

    }

    /**
     * Méthode qui permet d'afficher le planning dans la vue textuelle
     * 
     * @param planning liste des segments à parcourir
     */
    public void afficherPlanning(Planning planning, Carte carte) {
        Intersection depot = carte.getIntersections().get(planning.getIdDepot());
        Set<Segment> segmentsDepot = depot.getSegmentsPartants();
        String depotName = "";
        if (!segmentsDepot.isEmpty()) {
            depotName = segmentsDepot.iterator().next().getNom();
        }
        Date dateDebut = planning.getDateDebut();
        Calendar date = Calendar.getInstance();
        date.setTime(dateDebut);
        int heure = date.get(Calendar.HOUR_OF_DAY);
        int minutes = date.get(Calendar.MINUTE);
        this.zoneTexte.setText("Départ de " + depotName + " à " + heure + 'h' + minutes + "\r\n");

        for (Requete requete : planning.getRequetes()) {
            this.zoneTexte.appendText("Point de Collecte : " + requete.getIdPickup() + " - "
                    + String.valueOf(requete.getDureePickup()) + "\r\n");
            this.zoneTexte.appendText("Point de Livraison : " + requete.getIdDelivery() + " - "
                    + String.valueOf(requete.getDureeDelivery()) + "\r\n");
        }
    }

    /**
     * setter permettant de définir la zone de texte de la fenêtre
     * 
     * @param zoneTexte
     */
    public void setZoneTexte(TextArea zoneTexte) {
        this.zoneTexte = zoneTexte;
    }
}
