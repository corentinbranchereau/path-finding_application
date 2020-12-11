package fr.hexaone.utils;

import fr.hexaone.model.*;
import fr.hexaone.utils.exception.BadFileTypeException;
import fr.hexaone.utils.exception.IllegalAttributException;
import fr.hexaone.utils.exception.RequestOutOfMapException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
     * @throws IllegalAttributException Si mauvais attribut lors du parsage.
     * @throws BadFileTypeException     Si mauvais type de fichier.
     */
    public static void chargerCarte(Carte carte, Document xml) throws IllegalAttributException, BadFileTypeException {
        Map<Long, Intersection> intersections = carte.getIntersections();
        if (xml.getElementsByTagName("map").getLength() == 0)
            throw new BadFileTypeException("Le fichier XML chargé n'est pas de type map.");
        try {
            // Charger les intersections
            NodeList noeuds = xml.getElementsByTagName("intersection");
            for (int i = 0; i < noeuds.getLength(); i++) {
                Element element = (Element) noeuds.item(i);
                long id = Long.parseLong(element.getAttribute("id"));
                double latitude = Double.parseDouble(element.getAttribute("latitude"));
                double longitude = Double.parseDouble(element.getAttribute("longitude"));
                intersections.put(id, new Intersection(id, latitude, longitude));
            }

            // Charger les segments
            noeuds = xml.getElementsByTagName("segment");
            for (int i = 0; i < noeuds.getLength(); i++) {
                Element element = (Element) noeuds.item(i);
                long depart = Long.parseLong(element.getAttribute("origin"));
                long destination = Long.parseLong(element.getAttribute("destination"));
                double longueur = Double.parseDouble(element.getAttribute("length"));
                if (longueur < 0)
                    throw new IllegalAttributException("Une longueur de segment est négative, veuillez-recommencer !");
                String nom = element.getAttribute("name");
                Segment segment = new Segment(longueur, nom, depart, destination);
                intersections.get(depart).getSegmentsPartants().add(segment);
                intersections.get(destination).getSegmentsArrivants().add(segment);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalAttributException(
                    "Le fichier XML chargé contient un attribut avec un type incohérent / illégal");
        }
    }

    /**
     * Charge les données d'un fichier XML bien formé contenant les requêtes formant
     * le planning
     * 
     * @param planning Le planning où charger les données.
     * @param xml      Le fichier XML bien formé contenant les données.
     * 
     * @throws IllegalAttributException Lorsque mauvais attribut
     * @throws BadFileTypeException     Lorsque mauvais type de fichier
     * @throws RequestOutOfMapException Requête hors de la map
     */
    public static void chargerRequetes(Document xml, Planning planning)
            throws IllegalAttributException, BadFileTypeException, RequestOutOfMapException {
        if (xml.getElementsByTagName("planningRequest").getLength() == 0)
            throw new BadFileTypeException("Le fichier XML chargé n'est pas de type request.");
        try {
            // Récupère le dépôt
            Element tagDepot = (Element) xml.getElementsByTagName("depot").item(0);
            Long idDepot = Long.parseLong(tagDepot.getAttribute("address"));
            Date dateDebut = new SimpleDateFormat("H:m:s").parse(tagDepot.getAttribute("departureTime"));

            // Construit la liste des requêtes
            List<Requete> listeRequetes = new ArrayList<>();
            NodeList noeudsRequetes = xml.getElementsByTagName("request");
            for (int i = 0; i < noeudsRequetes.getLength(); ++i) {
                Element tagRequete = (Element) noeudsRequetes.item(i);
                long idCollecte = Long.parseLong(tagRequete.getAttribute("pickupAddress")),
                        idLivraison = Long.parseLong(tagRequete.getAttribute("deliveryAddress"));
                int dureeCollecte = Integer.parseInt(tagRequete.getAttribute("pickupDuration")),
                        dureeLivraison = Integer.parseInt(tagRequete.getAttribute("deliveryDuration"));
                String nomCollecte = "";
                if (!(planning.getCarte().getIntersections().containsKey(idCollecte)
                        && planning.getCarte().getIntersections().containsKey(idLivraison)))
                    throw new RequestOutOfMapException(
                            "Une requête du fichier XML n'est pas disponible dans la carte chargée.");

                for (Segment segment : planning.getCarte().getIntersections().get(idCollecte).getSegmentsArrivants()) {
                    if (!segment.getNom().isEmpty()) {
                        nomCollecte = segment.getNom();
                        break;
                    }
                }
                String nomLivraison = null;
                for (Segment segment : planning.getCarte().getIntersections().get(idLivraison).getSegmentsArrivants()) {
                    if (!segment.getNom().isEmpty()) {
                        nomLivraison = segment.getNom();
                        break;
                    }
                }

                if ((dureeCollecte < 0) || (dureeLivraison < 0))
                    throw new IllegalAttributException("Une durée est négative, veuillez-recommencer !");

                listeRequetes.add(
                        new Requete(idCollecte, dureeCollecte, nomCollecte, idLivraison, dureeLivraison, nomLivraison));
            }
            planning.setIdDepot(idDepot);
            planning.setDateDebut(dateDebut);
            planning.setRequetes(listeRequetes);
        } catch (IllegalArgumentException | ParseException e) {
            throw new IllegalAttributException(
                    "Le fichier XML chargé contient un attribut avec un type incohérent / illégal");
        }
    }
}
