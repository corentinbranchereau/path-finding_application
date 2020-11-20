package fr.hexaone.utils;

import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Carte;

import java.text.ParseException;
import java.util.ArrayList;
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
    public static void loadRequete(Planning planning, Document xml) {
        try {
            // On récupère le depot
            Element depotTag = (Element) xml.getElementsByTagName("depot");
            // !planning.setDepot(new Depot(depotTag.getAttribute("address"),
            // depotTag.getAttribute("departureTime")));
            // On construit la liste des requetes
            List<Requete> listRequetes = new ArrayList<>();
            NodeList requetesNode = xml.getElementsByTagName("request");
            for (int i = 0; i < requetesNode.getLength(); ++i) {
                Element requeteTag = (Element) requetesNode.item(i);
                // ! Problème : Quand on récupère une requete, on a l'id du pickup et du
                // delivery. Ils correspondent à des intersections existant déjà par ailleurs en
                // mémoire. Mais on y a pas accès (à moins de passer la Map en param) et si on y
                // a accès comment les modifier en intersectionSpeciale ? De plus on ne peut pas
                // créer les intersectionsSpeciales directement car on ne connait pas lat et
                // long et ça ferait des doublons d'id ...

                // listRequetes.add(new Requete(...));
            }
            planning.setRequetes(listRequetes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
