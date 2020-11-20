package fr.hexaone.utils;

import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Depot;
import fr.hexaone.model.IntersectionSpeciale;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de déserialiser un fichier au format XML.
 * Classe statique.
 * @author HexaOne
 * @version 1.0
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDeserializer {

    /**
     * Charge les données d'un fichier XML bien formé contenant des intersections et
     * des segments formant la carte
     * 
     * @param carte La carte où charger les données.
     * @param file  Le fichier XML bien formé contenant les données.
     */
    public static void loadCarte(Carte carte, Document xml) {
        // TODO
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
