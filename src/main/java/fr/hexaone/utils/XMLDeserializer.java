package fr.hexaone.utils;

import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Carte;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.hexaone.model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
 * Permet de déserialiser un fichier au format XML. Classe statique.
 * 
 * @author HexaOne
 * @version 1.0
 */
public class XMLDeserializer {

    /**
     * Charge les données d'un fichier XML bien formé contenant des intersections et
     * des segments formant la carte
     * 
     * @param carte La carte où charger les données.
     * @param xml   Le document XML bien formé contenant les données.
     */
    public static void loadCarte(Carte carte, Document xml) {
        Map<Long, Intersection> intersections = carte.getIntersections();

        // Charger les intersections
        NodeList ns = xml.getElementsByTagName("intersection");
        for (int i = 0; i < ns.getLength(); i++) {
            Element element = (Element) ns.item(i);
            long id = Long.parseLong(element.getAttribute("id"));
            double latitude = Double.parseDouble(element.getAttribute("latitude"));
            double longitude = Double.parseDouble(element.getAttribute("longitude"));
            intersections.put(id, new Intersection(id, latitude, longitude));
        }

        // Charger les segments
        ns = xml.getElementsByTagName("segment");
        for (int i = 0; i < ns.getLength(); i++) {
            Element element = (Element) ns.item(i);
            long depart = Long.parseLong(element.getAttribute("origin"));
            long destination = Long.parseLong(element.getAttribute("destination"));
            double longueur = Double.parseDouble(element.getAttribute("length"));
            String nom = element.getAttribute("name");
            Segment segment = new Segment(longueur, nom, depart, destination);
            intersections.get(depart).getSegmentsPartants().add(segment);
            intersections.get(destination).getSegmentsArrivants().add(segment); // TODO : A vérifier
        }
    }

    /**
     * Charge les données d'un fichier XML bien formé contenant les requêtes formant
     * le planning
     * 
     * @param planning Le planning où charger les données.
     * @param xml      Le fichier XML bien formé contenant les données.
     */
    public static void loadRequete(Document xml, Carte carte, Planning planning) {
        try {
            // Récupèrer le depot
            Element depotTag = (Element) xml.getElementsByTagName("depot").item(0);
            Long idDepot = Long.parseLong(depotTag.getAttribute("address"));
            Date dateDebut = new SimpleDateFormat("H:m:s").parse(depotTag.getAttribute("departureTime"));
            // Construire la liste des requetes
            List<Requete> listeRequetes = new ArrayList<>();
            NodeList requetesNode = xml.getElementsByTagName("request");
            for (int i = 0; i < requetesNode.getLength(); ++i) {
                Element requeteTag = (Element) requetesNode.item(i);
                Long idPickup = Long.parseLong(requeteTag.getAttribute("pickupAddress")),
                        idDelivery = Long.parseLong(requeteTag.getAttribute("deliveryAddress"));
                int pickupDuration = Integer.parseInt(requeteTag.getAttribute("pickupDuration")),
                        deliveryDuration = Integer.parseInt(requeteTag.getAttribute("deliveryDuration"));
                listeRequetes.add(new Requete(idPickup, pickupDuration, idDelivery, deliveryDuration));
            }
            planning.setIdDepot(idDepot);
            planning.setDateDebut(dateDebut);
            planning.setRequetes(listeRequetes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
