package fr.hexaone.model;

import fr.hexaone.algo.AlgoTSP;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;

/**
 * Objet contenant toutes les informations relatives au planning d'une tournée
 *
 * @author HexaOne
 * @version 1.0
 */
public class Planning {

    /**
     * L'id du dépôt associé au planning
     */
    protected Long idDepot;

    /**
     * Date de début de la tournée
     */
    protected Date dateDebut;

    /**
     * Date de la fin de la tournée / du retour au dépot
     */
    protected Date dateFin;

    /**
     * Liste des requêtes qui ont été chargées
     */
    protected List<Requete> requetes;

    /**
     * Liste ordonnée des demandes, constituant leur ordre de passage dans la
     * tournée
     */
    protected List<Demande> demandesOrdonnees;

    /**
     * Carte associée au planning
     */
    protected Carte carte;

    /**
     * Liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * Durée totale de la tournée en secondes
     */
    protected Integer dureeTotale;

    /**
     * Map permettant d'identifier les chemins les plus courts à partir d'un
     * identifiant (String)
     */
    protected Map<String, Trajet> trajetsLesPlusCourts;

    /**
     * Constructeur du planning
     * 
     * @param carte La carte du planning
     */
    public Planning(Carte carte) {
        this.requetes = new ArrayList<>();
        this.carte = carte;
    }

    /**
     * Constructeur du planning
     * 
     * @param carte    La carte du planning
     * @param requetes Les requêtes que l'on intègre au planning
     */
    public Planning(Carte carte, List<Requete> requetes) {
        this.requetes = requetes;
        this.carte = carte;
    }

    /**
     * Recherche de la tournée la plus rapide : Crée la liste ordonnée de demandes
     * demandesOrdonnees ; crée la liste des trajets et calcul les durées de passage
     * et de sorties dans les demandes.
     * 
     * Méthode à appeler une seule fois pour générer le premier ordonnancement.
     * 
     * Prérequis : La liste des requetes ; la date de début de la tournée ;
     * l'identifiant du dépôt
     * 
     * @return True en cas de succès du calcul, false sinon
     */
    public boolean calculerMeilleurTournee() {
        // Recherche des chemins les plus courts entre toutes les
        // intersections spéciales (collectes, livraisons et dépôt)
        List<Intersection> intersectionsSpeciales = new ArrayList<>();
        intersectionsSpeciales.add(carte.getIntersections().get(idDepot));

        for (Requete requete : requetes) {
            intersectionsSpeciales.add(carte.getIntersections().get(requete.getDemandeCollecte().getIdIntersection()));
            intersectionsSpeciales.add(carte.getIntersections().get(requete.getDemandeLivraison().getIdIntersection()));
        }

        boolean succes = calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);

        if (!succes) {
            return false;
        }

        // Recherche de la meilleure tournée
        List<Object> demandes = new ArrayList<>();
        for (Requete requete : requetes) {
            demandes.add(requete.getDemandeCollecte());
            demandes.add(requete.getDemandeLivraison());
        }

        AlgoTSP algo = new AlgoTSP(this.idDepot, this.trajetsLesPlusCourts);

        List<Object> resultat = algo.algoGenetique(demandes);

        this.demandesOrdonnees = new ArrayList<>();

        for (Object objet : resultat) {
            this.demandesOrdonnees.add((Demande) objet);
        }

        // Création de la listes des trajets à suivre et calcul des temps
        ordonnerLesTrajetsEtLesDates();

        return true;
    }

    /**
     * Recrée une liste de trajets à partir des demandes ordonnées et calcule les
     * dates de passage dans les demandes.
     * 
     * Méthode à utiliser après un changement d'ordonnancement des demandes.
     * 
     * Prérequis : Avoir les demandes ordonnées.
     */
    public void ordonnerLesTrajetsEtLesDates() {
        long precedentIdIntersection = idDepot;
        listeTrajets = new LinkedList<>();
        double duree = 0D;
        long tempsDebut = dateDebut.getTime();

        for (Demande demande : demandesOrdonnees) {
            Long nouvelId = demande.getIdIntersection();
            Trajet trajet = trajetsLesPlusCourts.get(precedentIdIntersection + "|" + nouvelId);
            listeTrajets.add(trajet);
            precedentIdIntersection = nouvelId;

            duree += trajet.getPoids() * 3600D / 15D;
            demande.setDateArrivee(new Date(tempsDebut + (long) duree));
            duree += demande.getDuree() * 1000;
            demande.setDateDepart(new Date(tempsDebut + (long) duree));
        }

        Trajet trajet = trajetsLesPlusCourts.get(precedentIdIntersection + "|" + idDepot);
        listeTrajets.add(trajet);
        duree += trajet.getPoids() * 3600D / 15D;
        dateFin = new Date(tempsDebut + (long) duree);
        dureeTotale = (int) duree / 1000;
    }

    /**
     * Recalcule tous les plus courts trajets entre toutes les demandes. Crée une
     * nouvelle liste de trajets ordonnées et calcule les durées de passage et de
     * sorties dans les demandes.
     * 
     * Méthode à utiliser après l'ajout ou la supression de demandes ou la
     * modification des temps et lieux de demandes.
     * 
     * Prérequis : Avoir les demandes ordonnées.
     * 
     * @return True si succès, false sinon.
     */
    public boolean recalculerTournee() {
        // Recalcule tous les plus courts trajets des demandes
        List<Intersection> intersectionsSpeciales = new ArrayList<>();
        intersectionsSpeciales.add(carte.getIntersections().get(idDepot));
        for (Demande demande : demandesOrdonnees) {
            intersectionsSpeciales.add(carte.getIntersections().get(demande.getIdIntersection()));
        }
        boolean succes = calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);
        if (!succes) {
            return false;
        }

        // Recrée une liste de trajets et recalcule les dates des demandes
        ordonnerLesTrajetsEtLesDates();
        return true;
    }

    /**
     * Ajoute une demande seule après avoir déjà calculé la meilleure tournée.
     * 
     * @param demande La demande que l'on souhaite ajouter.
     * @return True si succès, false sinon.
     */
    public boolean ajouterDemande(Demande demande) {
        demandesOrdonnees.add(demande);

        boolean succes = recalculerTournee();

        if (!succes)
            demandesOrdonnees.remove(demande);

        return succes;
    }

    /**
     * Modifie une demande seule après avoir déjà calculé la meilleure tournée.
     * 
     * @param demande        La demande que l'on souhaite modifier
     * @param duree          La nouvelle durée
     * @param idIntersection L'id de l'intersection
     * @return True si succès, false sinon.
     */
    public boolean modifierDemande(Demande demande, int duree, Long idIntersection) {
        int dureePrecedente = demande.getDuree();
        Long precedentIdIntersection = demande.getIdIntersection();

        String nom = null;
        for (Segment segment : carte.getIntersections().get(idIntersection).getSegmentsArrivants()) {
            if (!segment.getNom().isEmpty()) {
                nom = segment.getNom();
                break;
            }
        }

        demande.setDuree(duree);
        demande.setIdIntersection(idIntersection);
        demande.setProprieteNomIntersection(new SimpleStringProperty(nom));

        boolean succes = recalculerTournee();
        if (!succes) {
            demande.setDuree(dureePrecedente);
            demande.setIdIntersection(precedentIdIntersection);
        }

        return succes;
    }

    /**
     * Ajoute une requête après avoir déjà calculé la meilleure tournée.
     * 
     * @param requete La requête que l'on souhaite ajouter
     * @return True si succès, false sinon.
     */
    public boolean ajouterRequete(Requete requete) {
        requetes.add(requete);

        demandesOrdonnees.add(requete.getDemandeCollecte());
        demandesOrdonnees.add(requete.getDemandeLivraison());

        boolean succes = recalculerTournee();

        if (!succes) {
            demandesOrdonnees.remove(requete.getDemandeCollecte());
            demandesOrdonnees.remove(requete.getDemandeLivraison());
        }

        return succes;
    }

    /**
     * Ajoute une requête après avoir déjà calculé la meilleure tournée.
     * 
     * @param requete   La requête que l'on souhaite ajouter
     * @param positions Les positions de la livraison et de la collecte dans le
     *                  planning établi
     * @return True si succès, false sinon.
     */
    public boolean ajouterRequete(Requete requete, List<Integer> positions) {
        requetes.add(requete);

        demandesOrdonnees.add(positions.get(0), requete.getDemandeCollecte());
        demandesOrdonnees.add(positions.get(1), requete.getDemandeLivraison());

        return recalculerTournee();
    }

    /**
     * Supprime une demande de la tournée et regénère les trajets ordonés
     * 
     * @param demande La demande que l'on souhaite supprimer
     */
    public void supprimerDemande(Demande demande) {
        demandesOrdonnees.remove(demande);
        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Supprime une requête de la tournée et regénère les trajets ordonés
     * 
     * @param requete La requête que l'on souhaite supprimer
     * @return La position de la collecte et de la livraison
     */
    public List<Integer> supprimerRequete(Requete requete) {
        requetes.remove(requete);

        List<Integer> positions = new ArrayList<>();

        positions.add(demandesOrdonnees.indexOf(requete.getDemandeCollecte()));
        positions.add(demandesOrdonnees.indexOf(requete.getDemandeLivraison()));

        demandesOrdonnees.remove(requete.getDemandeCollecte());
        demandesOrdonnees.remove(requete.getDemandeLivraison());

        ordonnerLesTrajetsEtLesDates();

        return positions;
    }

    /**
     * Modifie la durée d'une demande
     * 
     * @param demande La demande que l'on souhaite modifier.
     * @param duree   La nouvelle durée de la demande.
     */
    public void modifierDemande(Demande demande, Integer duree) {
        demande.setDuree(duree);
        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Modifie l'intersection d'une demande
     * 
     * @param demande        La demande que l'on souhaite modifier
     * @param idIntersection L'id de la nouvelle intersection
     * @return True si succès, false sinon
     */
    public boolean modifierDemande(Demande demande, Long idIntersection) {
        Long precedentIdIntersection = demande.getIdIntersection();

        demande.setIdIntersection(idIntersection);

        boolean succes = recalculerTournee();
        if (!succes) {
            demande.setIdIntersection(precedentIdIntersection);
        }

        return succes;
    }

    /**
     * Modifie la durée et l'intersection d'une demande
     * 
     * @param demande        La demande que l'on souhaite modifier
     * @param idIntersection L'id de la nouvelle intersection
     * @param duree          La nouvelle durée de la demande
     * @return True si succès, false sinon.
     */
    public boolean modifierDemande(Demande demande, Long idIntersection, Integer duree) {
        Long precedentIdIntersection = demande.getIdIntersection();

        demande.setIdIntersection(idIntersection);
        demande.setDuree(duree);

        boolean succes = recalculerTournee();

        if (!succes) {
            demande.setIdIntersection(precedentIdIntersection);
        }

        return succes;
    }

    /**
     * Permet de réinitialiser le planning
     */
    public void reinitialiserPlanning() {
        this.demandesOrdonnees = null;
        this.dateDebut = null;
        this.dateFin = null;
        this.dureeTotale = null;
        this.idDepot = null;
        this.listeTrajets = null;
        this.requetes.clear();
    }

    /**
     * Calcule tous les trajets les plus courts entre toutes les intersections
     * passées en paramètre
     * 
     * @param intersections La liste des intersections
     * 
     * @return True si succès, false sinon.
     */
    public boolean calculerLesTrajetsLesPlusCourts(List<Intersection> intersections) {

        // Préparation
        Map<Long, Intersection> toutesLesIntersections = carte.getIntersections();

        trajetsLesPlusCourts = new HashMap<>();

        // Calcul de tous les chemins les plus courts n fois avec Dijkstra
        for (Intersection source : intersections) {
            source.setDistance(0D);
            Set<Intersection> intersectionsVisitees = new HashSet<>();
            Set<Intersection> intersectionsNonVisitees = new HashSet<>();

            intersectionsNonVisitees.add(source);

            while (intersectionsNonVisitees.size() != 0) {
                Intersection intersectionActuelle = obtenirIntersectionLaPlusProche(intersectionsNonVisitees);
                intersectionsNonVisitees.remove(intersectionActuelle);

                for (Segment segmentAdjacent : intersectionActuelle.getSegmentsPartants()) {
                    Intersection intersectionAdjacente = toutesLesIntersections.get(segmentAdjacent.getArrivee());
                    Double longueurSegment = segmentAdjacent.getLongueur();
                    if (!intersectionsVisitees.contains(intersectionAdjacente)) {
                        calculDistanceMinimum(intersectionAdjacente, longueurSegment, intersectionActuelle,
                                segmentAdjacent);
                        intersectionsNonVisitees.add(intersectionAdjacente);
                    }
                }
                intersectionsVisitees.add(intersectionActuelle);
            }

            String idSource = source.getId() + "|";

            for (Intersection intersection : intersections) {
                String cle = idSource + intersection.getId();
                if (intersection.getDistance() >= Double.MAX_VALUE) {
                    return false;
                }
                trajetsLesPlusCourts.put(cle,
                        new Trajet(intersection.getCheminLePlusCourt(), intersection.getDistance()));
            }
            toutesLesIntersections.forEach((id, intersection) -> intersection.resetIntersection());
        }

        return true;
    }

    /**
     * Retourne l'intersection avec la distance la plus faible
     * 
     * @param intersectionsNonVisitees Les intersections qui n'ont pas encore été
     *                                 parcourues lors de l'algorithme
     * @return L'intersection la plus proche
     */
    public Intersection obtenirIntersectionLaPlusProche(Set<Intersection> intersectionsNonVisitees) {
        Intersection intersectionLaPlusProche = null;
        double plusCourteDistance = Double.MAX_VALUE;
        for (Intersection intersection : intersectionsNonVisitees) {
            double intersectionDistance = intersection.getDistance();
            if (intersectionDistance < plusCourteDistance) {
                plusCourteDistance = intersectionDistance;
                intersectionLaPlusProche = intersection;
            }
        }
        return intersectionLaPlusProche;
    }

    /**
     * Enregistre la distance minimale pour accéder à une intersection
     * 
     * @param intersectionAEvaluer L'intersection à évaluer
     * @param longueurSegment      Le poids de l'arrête
     * @param intersectionSource   L'intersection source
     * @param segment              Le segment associé
     */
    public void calculDistanceMinimum(Intersection intersectionAEvaluer, Double longueurSegment,
            Intersection intersectionSource, Segment segment) {
        Double distanceSource = intersectionSource.getDistance();
        if (distanceSource + longueurSegment < intersectionAEvaluer.getDistance()) {
            intersectionAEvaluer.setDistance(distanceSource + longueurSegment);
            LinkedList<Segment> cheminLePlusCourt = new LinkedList<>(intersectionSource.getCheminLePlusCourt());
            cheminLePlusCourt.add(segment);
            intersectionAEvaluer.setCheminLePlusCourt(cheminLePlusCourt);
        }
    }

    /**
     * @return L'id du dépôt
     */
    public Long getIdDepot() {
        return idDepot;
    }

    /**
     * Change la valeur de l'id du dépôt
     * 
     * @param idDepot Le nouvel id du dépôt
     */
    public void setIdDepot(Long idDepot) {
        this.idDepot = idDepot;
    }

    /**
     * @return La date de début
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Change la valeur de la date de début
     * 
     * @param dateDebut La nouvelle date de début
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return La liste des requêtes
     */
    public List<Requete> getRequetes() {
        return requetes;
    }

    /**
     * Change la valeur de la liste des requêtes
     * 
     * @param requetes La nouvelle liste des requêtes
     */
    public void setRequetes(List<Requete> requetes) {
        this.requetes = requetes;
    }

    /**
     * @return La liste des demandes ordonnées
     */
    public List<Demande> getDemandesOrdonnees() {
        return demandesOrdonnees;
    }

    /**
     * @return La carte
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     * Change la valeur de la carte
     * 
     * @param carte La nouvelle carte
     */
    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    /**
     * @return La liste des trajets
     */
    public List<Trajet> getListeTrajets() {
        return listeTrajets;
    }

    /**
     * @return La durée totale
     */
    public Integer getDureeTotale() {
        return dureeTotale;
    }

    /**
     * @return La liste des plus courts trajets
     */
    public Map<String, Trajet> getTrajetsLesPlusCourts() {
        return trajetsLesPlusCourts;
    }

    /**
     * @return La date de fin
     */
    public Date getDateFin() {
        return dateFin;
    }
}
