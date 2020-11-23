package fr.hexaone.view;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    protected TextFlow zoneTexte;

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
    public void afficherPlanning(Planning planning, Carte carte, Map<Requete, Color> mapCouleurRequete) {
        // On vide le zone de texte au cas où des choses sont déjà affichées dedans
        this.zoneTexte.getChildren().clear();

        // récupération du nom du dépot
        Intersection depot = carte.getIntersections().get(planning.getIdDepot());
        Set<Segment> segmentsDepot = depot.getSegmentsPartants();
        String depotName = "";
        if (!segmentsDepot.isEmpty()) {
            depotName = segmentsDepot.iterator().next().getNom();
        }

        // récupération de l'heure de départ
        Date dateDebut = planning.getDateDebut();
        Calendar date = Calendar.getInstance();
        date.setTime(dateDebut);
        int heure = date.get(Calendar.HOUR_OF_DAY);
        String heureString = heure < 10 ? ("0" + heure) : String.valueOf(heure);
        int minutes = date.get(Calendar.MINUTE);
        String minutesString = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);

        // écriture du point et de l'heure de départ
        Text texteDepot = new Text(
                " ★ Départ de " + depotName + " à " + heureString + 'h' + minutesString + "\r\n\r\n");
        this.zoneTexte.getChildren().add(texteDepot);
        int i = 1;

        // parcours de map
        for (Map.Entry<Requete, Color> requete : mapCouleurRequete.entrySet()) {
            Set<Segment> segmentsCollecte = carte.getIntersections().get(requete.getKey().getIdPickup())
                    .getSegmentsPartants();
            Set<Segment> segmentsLivraison = carte.getIntersections().get(requete.getKey().getIdDelivery())
                    .getSegmentsPartants();
            String nomCollecte = "";
            String nomLivraison = "";
            if (!segmentsCollecte.isEmpty()) {
                nomCollecte = segmentsCollecte.iterator().next().getNom();
            }
            if (!segmentsLivraison.isEmpty()) {
                nomLivraison = segmentsLivraison.iterator().next().getNom();
            }
            Text titreText = new Text("Requête " + i + ": \r\n");
            Text collecteText = new Text("     ■ Collecte : " + nomCollecte + " - "
                    + String.valueOf(requete.getKey().getDureePickup()) + "s" + "\r\n");
            Text livraisonText = new Text("     ● Livraison : " + nomLivraison + " - "
                    + String.valueOf(requete.getKey().getDureeDelivery()) + "s" + "\r\n\n");
            i++;

            titreText.setFill(requete.getValue());
            collecteText.setFill(requete.getValue());
            livraisonText.setFill(requete.getValue());

            this.zoneTexte.getChildren().addAll(titreText, collecteText, livraisonText);
        }
    }

    /**
     * setter permettant de définir la zone de texte de la fenêtre
     * 
     * @param zoneTexte
     */
    public void setZoneTexte(TextFlow zoneTexte) {
        this.zoneTexte = zoneTexte;
    }
}
