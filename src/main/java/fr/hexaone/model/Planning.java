package fr.hexaone.model;

import java.lang.reflect.Array;
import java.util.*;

import org.javatuples.Pair;

// TODO
// Set pour les intersections dans le calcul des plus courts trajets
// générer chromosomes aléa, mieux qu'un do while ?
// treeset pour générer les plus courts trajets

/**
 * Objet contenant toutes les informations relatives au planning d'une tournée "
 *
 * @author HexaOne
 * @version 1.0
 */
public class Planning {

    /**
     * L'id du Dépôt associé au planning
     */
    protected Long idDepot;

    /**
     * Date de début de la tournée. 24 hours format - H:m:s
     */
    protected Date dateDebut;

    /**
     * Date de la fin de la tournée / du retour au dépot
     */
    protected Date dateFin;

    /**
     * Liste des requêtes en rapport avec la demande client
     */
    protected List<Requete> requetes;

    /**
     * Liste des ids uniques d'intersections constituant la tournée
     */
    protected List<Demande> demandesOrdonnees;

    /**
     * Carte associée au planning
     */
    protected Carte carte;

    /**
     * liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * Duree totale de la tournée en secondes
     */
    protected Integer dureeTotale;

    /**
     * Map permettant d'identifier les chemins les plus courts à partir d'un
     * identifiant (String)
     */
    protected Map<String, Trajet> TrajetsLesPlusCourts;

    /**
     * Comparateur afin de classer les chromosomes au sein d'une population dans
     * l'ordre croissant des coûts
     */
    public Comparator<Pair<List<Demande>, Double>> ComparatorChromosome = (e1,
            e2) -> (int) (e1.getValue1() - e2.getValue1());

    ///////////////////////////////
    // Méthode de plannification //
    ///////////////////////////////

    /**
     * Constructeur du planning
     * 
     * @param carte
     */
    public Planning(Carte carte) {
        this.requetes = new ArrayList<>();
        this.carte = carte;
    }

    /**
     * Constructeur du planning
     * 
     * @param carte
     */
    public Planning(Carte carte, List<Requete> requetes) {
        this.requetes = requetes;
        this.carte = carte;
    }

    /**
     * Recherche de la tournée la plus rapide : - Crée une la liste ordonnée de
     * demandes demandesOrdonnées - Crée la liste des trajets et calcul les durées
     * de passage et de sorties dans les demandes
     * 
     * A appeler qu'une fois pour générer le premier ordonnancement
     * 
     * Prérequis : - La liste des requetes - La date de début de la tournée -
     * idDépot
     */
    public void calculerMeilleurTournee() {

        // Recherche des chemins des plus courts entre toutes les
        // intersections spéciales (dépots, livraisons et dépot)
        List<Intersection> intersectionsSpeciales = new ArrayList<>();
        intersectionsSpeciales.add(carte.getIntersections().get(idDepot));
        for (Requete r : requetes) {
            intersectionsSpeciales.add(carte.getIntersections().get(r.getDemandeCollecte().getIdIntersection()));
            intersectionsSpeciales.add(carte.getIntersections().get(r.getDemandeLivraison().getIdIntersection()));

        }
        calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);

        // recherche de la melleure tournéee
        List<Object> demandes = new ArrayList<>();
        for (Requete requete : requetes) {
            demandes.add(requete.getDemandeCollecte());
            demandes.add(requete.getDemandeLivraison());
        }

        AlgoTSP algo = new AlgoTSP(this.idDepot, this.TrajetsLesPlusCourts);

        List<Object> result = algo.algoGenetique(demandes);

        this.demandesOrdonnees = new ArrayList<>();

        for (Object obj : result) {
            this.demandesOrdonnees.add((Demande) obj);
        }

        // demandesOrdonnees = ordonnerLesDemandes(demandes); //ordonnerLesDemandes

        // Création de la listes des trajets à suivre et calcul des temps
        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Recrée une liste de trajets à partir des demandes ordonnées et calcul des
     * dates de passage dans les demandes.
     * 
     * A utiliser après un changement d'ordonnancement des demandes.
     * 
     * Prérequis : - Avoir les demandes ordonnées
     */
    public void ordonnerLesTrajetsEtLesDates() {
        long prevIntersectionId = idDepot;
        listeTrajets = new LinkedList<>();
        double duree = 0.;
        long tempsDebut = dateDebut.getTime();

        for (Demande demande : demandesOrdonnees) {
            Long newId = demande.getIdIntersection();
            Trajet trajet = TrajetsLesPlusCourts.get(prevIntersectionId + "|" + newId);
            listeTrajets.add(trajet);
            prevIntersectionId = newId;

            duree += trajet.getPoids() * 3600. / 15.;
            demande.setDateArrivee(new Date(tempsDebut + (long) duree));
            duree += demande.getDuree() * 1000;
            demande.setDateDepart(new Date(tempsDebut + (long) duree));
        }

        Trajet trajet = TrajetsLesPlusCourts.get(prevIntersectionId + "|" + idDepot);
        listeTrajets.add(trajet);
        duree += trajet.getPoids() * 3600. / 15.;
        dateFin = new Date(tempsDebut + (long) duree);
        dureeTotale = (int) duree / 1000;
    }

    /**
     * Recalcule tous les plus courts trajets entre toutes demandes. - Crée un
     * nouvelle liste de trajets ordonnées et calcul les durées de passage et de
     * sorties dans les demandes.
     * 
     * A utiliser après l'ajout ou la supression de demandes ou la modification des
     * temps et lieux de demandes.
     * 
     * Prérequis : - Avoir les demandes ordonnées.
     */
    public void recalculerTournee() {
        // Recalcule tous les plus courts trajets des demandes
        List<Intersection> intersectionsSpeciales = new ArrayList<>();
        intersectionsSpeciales.add(carte.getIntersections().get(idDepot));
        for (Demande demande : demandesOrdonnees) {
            intersectionsSpeciales.add(carte.getIntersections().get(demande.getIdIntersection()));
        }
        calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);

        // Recréer une liste de trajet et recalcule les dates des demandes
        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Ajouter une demande seule après avoir déja calculer la meilleure tournée.
     */
    public void ajouterDemande(Demande demande) {
        demandesOrdonnees.add(demande);

        recalculerTournee();
    }

    /**
     * Modifier une demande seule après avoir déja calculer la meilleure tournée.
     */
    public void modifierDemande(Demande demande, int duree, Long idIntersection) {
        demande.setDuree(duree);
        demande.setIdIntersection(idIntersection);
        recalculerTournee();
    }

    /**
     * Ajouter une requete après avoir déja calculé la meilleure tournée.
     */
    public void ajouterRequete(Requete requete) {
        requetes.add(requete);

        demandesOrdonnees.add(requete.getDemandeCollecte());
        demandesOrdonnees.add(requete.getDemandeLivraison());

        recalculerTournee();
    }

    /**
     * Ajouter une requete après avoir déja calculer la meilleure tournée.
     */
    public void ajouterRequete(Requete requete, List<Integer> positions) {
        requetes.add(requete);

        demandesOrdonnees.add(positions.get(0), requete.getDemandeCollecte());
        demandesOrdonnees.add(positions.get(1), requete.getDemandeLivraison());

        recalculerTournee();
    }

    /**
     * Supprimer une requete de la tournée et regénère les trajets ordonées
     */
    public void supprimerDemande(Demande demande) {
        demandesOrdonnees.remove(demande);

        ordonnerLesTrajetsEtLesDates();

        System.out.println(demandesOrdonnees.size());
    }

    /**
     * Supprimer une requete de la tournée et regénère les trajets ordonées
     * 
     * @return la position de la collecte et de la livraison
     */
    public List<Integer> supprimerRequete(Requete requete) {
        requetes.remove(requete);

        List<Integer> positions = new ArrayList<>();

        positions.add(demandesOrdonnees.indexOf(requete.getDemandeCollecte()));
        positions.add(demandesOrdonnees.indexOf(requete.getDemandeLivraison()));

        demandesOrdonnees.remove(requete.getDemandeCollecte());
        demandesOrdonnees.remove(requete.getDemandeLivraison());

        ordonnerLesTrajetsEtLesDates();

        System.out.println(demandesOrdonnees.size());

        return positions;
    }

    /**
     * Modifer la durée d'une demande
     */
    public void modifierDemande(Demande demande, Integer duree) {
        demande.setDuree(duree);

        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Modifer la durée d'une demande
     */
    public void modifierDemande(Demande demande, Long idIntersection) {
        demande.setIdIntersection(idIntersection);

        recalculerTournee();
    }

    /**
     * Modifer la durée et l'intersection d'une demande
     */
    public void modifierDemande(Demande demande, Long idIntersection, Integer duree) {
        demande.setIdIntersection(idIntersection);
        demande.setDuree(duree);

        recalculerTournee();
    }

    ///////////////////////////////////////////////
    // Algo de recherche des plus courts trajets //
    ///////////////////////////////////////////////

    /**
     * Calculer tous les trajets les plus courts entre toutes les intersections en
     * paramètre
     * 
     * @param intersections
     */
    public void calculerLesTrajetsLesPlusCourts(List<Intersection> intersections) {

        // Préparation

        Map<Long, Intersection> allIntersections = carte.getIntersections();

        TrajetsLesPlusCourts = new HashMap<>();

        // Calcul de tous les chemins les plus courts n fois avec dijkstra

        for (Intersection source : intersections) {

            source.setDistance(0.);

            Set<Intersection> settledIntersections = new HashSet<>();

            Set<Intersection> unsettledIntersections = new HashSet<>();

            /*
             * SortedSet<Intersection> unsettledIntersections = new
             * TreeSet<Intersection>(new Comparator<Intersection>() { // TODO Vérifier si
             * c'est le set le plus efficace pour récupérer le premier
             *
             * @Override public int compare(Intersection o1, Intersection o2) { return
             * (int)(o1.getDistance()-o2.getDistance()); } });
             */

            unsettledIntersections.add(source);

            while (unsettledIntersections.size() != 0) {

                // Intersection currentIntersection = unsettledIntersections.first();

                Intersection currentIntersection = getLowestDistanceIntersection(unsettledIntersections);
                unsettledIntersections.remove(currentIntersection);

                for (Segment segmentAdjacent : currentIntersection.getSegmentsPartants()) {
                    Intersection adjacentIntersection = allIntersections.get(segmentAdjacent.getArrivee());
                    Double edgeWeight = segmentAdjacent.getLongueur();
                    if (!settledIntersections.contains(adjacentIntersection)) {
                        CalculateMinimumDistance(adjacentIntersection, edgeWeight, currentIntersection,
                                segmentAdjacent);
                        unsettledIntersections.add(adjacentIntersection);
                    }
                }
                settledIntersections.add(currentIntersection);
            }

            String sourceId = source.getId() + "|";

            for (Intersection i : intersections) {
                String key = sourceId + i.getId();
                TrajetsLesPlusCourts.put(key, new Trajet(i.getCheminLePlusCourt(), i.getDistance()));
            }

            allIntersections.forEach((id, intersection) -> intersection.resetIntersection());
        }
    }

    /**
     * Retourne l'intersection avec la distance la plus faible
     * 
     * @param unsettledIntersections
     * @return lowestDistanceIntersection : l'intersection la plus proche
     */
    public Intersection getLowestDistanceIntersection(Set<Intersection> unsettledIntersections) {
        Intersection lowestDistanceIntersection = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Intersection intersection : unsettledIntersections) {
            double intersectionDistance = intersection.getDistance();
            if (intersectionDistance < lowestDistance) {
                lowestDistance = intersectionDistance;
                lowestDistanceIntersection = intersection;
            }
        }
        return lowestDistanceIntersection;
    }

    /**
     * Enregistre la distance minimale pour accéder à une intersection
     * 
     * @param evaluationIntersection
     * @param edgeWeigh
     * @param sourceIntersection
     * @param seg
     */
    public void CalculateMinimumDistance(Intersection evaluationIntersection, Double edgeWeigh,
            Intersection sourceIntersection, Segment seg) {
        Double sourceDistance = sourceIntersection.getDistance();
        if (sourceDistance + edgeWeigh < evaluationIntersection.getDistance()) {
            evaluationIntersection.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Segment> shortestPath = new LinkedList<>(sourceIntersection.getCheminLePlusCourt());
            shortestPath.add(seg);
            evaluationIntersection.setCheminLePlusCourt(shortestPath);
        }
    }

    ///////////////////////
    // GETTER AND SETTER //
    ///////////////////////

    public Long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(Long idDepot) {
        this.idDepot = idDepot;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public List<Requete> getRequetes() {
        return requetes;
    }

    public void setRequetes(List<Requete> requetes) {
        this.requetes = requetes;
    }

    public List<Demande> getDemandesOrdonnees() {
        return demandesOrdonnees;
    }

    public void setDemandesOrdonnees(List<Demande> demandesOrdonnees) {
        this.demandesOrdonnees = demandesOrdonnees;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public List<Trajet> getListeTrajets() {
        return listeTrajets;
    }

    public void setListeTrajets(List<Trajet> listeTrajets) {
        this.listeTrajets = listeTrajets;
    }

    public Integer getDureeTotale() {
        return dureeTotale;
    }

    public void setDureeTotale(Integer dureeTotale) {
        this.dureeTotale = dureeTotale;
    }

    public Map<String, Trajet> getTrajetsLesPlusCourts() {
        return TrajetsLesPlusCourts;
    }

    public void setTrajetsLesPlusCourts(Map<String, Trajet> TrajetsLesPlusCourts) {
        this.TrajetsLesPlusCourts = TrajetsLesPlusCourts;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

}
