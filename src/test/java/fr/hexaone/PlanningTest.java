package fr.hexaone;

import fr.hexaone.algo.AlgoTSP;
import fr.hexaone.model.*;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Tests unitaires de l'algorithmie back-end
 */
public class PlanningTest {

    Carte carte;
    Planning planning;

    /**
     * Instancie une carte vide
     */
    @BeforeEach
    void init() {
        carte = new Carte();
        planning = new Planning(carte);
    }

    /**
     * Méthode de test pour vérifer les trajets créés pour un un graphe simple
     */
    @Test
    public void testRechercheDesPlusCourtsTrajetsSimple() {

        // Création d'un graphe
        creerGrapheSimple();

        // Création des intersections des plus courts chemins
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));

        // Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // Vérification des résultats
        Integer nbTrajet = planning.getTrajetsLesPlusCourts().size();
        assert (nbTrajet == 9);

        planning.getTrajetsLesPlusCourts().forEach((key, value) -> {
            switch (key) {
                case "0|0":
                    assert (value.getPoids() == 0.);
                    break;
                case "1|1":
                    assert (value.getPoids() == 0.);
                    break;
                case "3|3":
                    assert (value.getPoids() == 0.);
                    break;
                case "0|1":
                    assert (value.getPoids() == 1.);
                    break;
                case "0|3":
                    assert (value.getPoids() == 3.);
                    break;
                case "1|0":
                    assert (value.getPoids() == 1.);
                    break;
                case "1|3":
                    assert (value.getPoids() == 2.);
                    break;
                case "3|0":
                    assert (value.getPoids() == 2.);
                    break;
                case "3|1":
                    assert (value.getPoids() == 2.);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Méthode de test pour vérifer les trajets créés pour un un graphe avec une
     * intersection inaccessible
     */
    @Test
    public void testRechercheDesPlusCourtsCheminsInaccessible() {

        // Création d'un graphe
        creerGraphePonderer();

        // Création des requetes
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(9L)); // 9 est le point inaccessible

        // Calcul des chemins les plus courts
        assert (planning.calculerLesTrajetsLesPlusCourts(intersections) == false);

    }

    /**
     * Méthode de test de la recherche de plus courts chemins contenant des mêmes
     * intersections
     */
    @Test
    public void testRechercheDesPlusCourtsCheminsMemeIntersection() {

        // Création d'un graphe
        creerGraphe();

        // Création de la liste des intersections à accéder
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));
        intersections.add(carte.getIntersections().get(3L));
        intersections.add(carte.getIntersections().get(9L));

        // Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // Vérification des résultats
        assert (planning.getTrajetsLesPlusCourts().size() == 16);

        planning.getTrajetsLesPlusCourts().forEach((key, value) -> {
            switch (key) {
                case "0|0":
                    assert (value.getPoids() == 0.);
                    break;
                case "1|1":
                    assert (value.getPoids() == 0.);
                    break;
                case "9|9":
                    assert (value.getPoids() == 0.);
                    break;
                case "3|3":
                    assert (value.getPoids() == 0.);
                    break;
                case "0|1":
                    assert (value.getPoids() == 4.);
                    break;
                case "0|3":
                    assert (value.getPoids() == 16.);
                    break;
                case "0|9":
                    assert (value.getPoids() == 27.);
                    break;
                case "1|0":
                    assert (value.getPoids() == 4.);
                    break;
                case "1|3":
                    assert (value.getPoids() == 12.);
                    break;
                case "1|9":
                    assert (value.getPoids() == 23.);
                    break;
                case "3|0":
                    assert (value.getPoids() == 16.);
                    break;
                case "3|1":
                    assert (value.getPoids() == 12.);
                    break;
                case "3|9":
                    assert (value.getPoids() == 15.);
                    break;
                case "9|0":
                    assert (value.getPoids() == 27.);
                    break;
                case "9|1":
                    assert (value.getPoids() == 23.);
                    break;
                case "9|3":
                    assert (value.getPoids() == 15.);
                    break;
                default:
                    assert (false);
                    break;
            }
        });
    }

    /**
     * Méthode de test pour la vérification des contraintes sur une population
     *
     * Préconditions : - correctionCrossover
     */
    @Test
    public void testVerifierPop() {

        Requete r1 = new Requete(1, 0, "", 3, 0, "");
        Requete r2 = new Requete(4, 0, "", 9, 0, "");
        Requete r3 = new Requete(5, 0, "", 7, 0, "");

        List<Object> demandes1 = new ArrayList<Object>();
        List<Object> demandes2 = new ArrayList<Object>();
        List<Object> demandes3 = new ArrayList<Object>();

        demandes1.add(r1.getDemandeCollecte());
        demandes1.add(r2.getDemandeCollecte());
        demandes1.add(r3.getDemandeCollecte());
        demandes1.add(r1.getDemandeLivraison());
        demandes1.add(r2.getDemandeLivraison());
        demandes1.add(r3.getDemandeLivraison());

        demandes2.add(r1.getDemandeCollecte());
        demandes2.add(r2.getDemandeLivraison());
        demandes2.add(r3.getDemandeCollecte());
        demandes2.add(r1.getDemandeLivraison());
        demandes2.add(r2.getDemandeCollecte());
        demandes2.add(r3.getDemandeLivraison());

        demandes2.add(r1.getDemandeCollecte());
        demandes2.add(r1.getDemandeLivraison());
        demandes2.add(r2.getDemandeCollecte());
        demandes2.add(r2.getDemandeLivraison());
        demandes2.add(r3.getDemandeCollecte());
        demandes2.add(r3.getDemandeLivraison());

        AlgoTSP algo = new AlgoTSP(null, null);

        assert (algo.verifierChromosome(demandes1) == true);
        assert (algo.verifierChromosome(demandes2) == false);
        assert (algo.verifierChromosome(demandes3) == true);

    }

    /**
     * Méthode de test pour la vérification de l'espacement minimum des coûts pour
     * une population
     */
    @Test
    public void testEspacePopulation() {

        List<Object> listIntersections1 = new ArrayList<Object>();
        List<Object> listIntersections2 = new ArrayList<Object>();
        List<Object> listIntersections3 = new ArrayList<Object>();

        for (int i = 0; i < 10; i++) {
            listIntersections1.add(new Demande(TypeDemande.COLLECTE, (long) i, "", (Integer) 0, null));
            listIntersections1.add(new Demande(TypeDemande.COLLECTE, (long) i * i, "", (Integer) 0, null));
            listIntersections1.add(new Demande(TypeDemande.COLLECTE, (long) i * i * i, "", (Integer) 0, null));

        }

        List<Pair<List<Object>, Double>> pop = new ArrayList<Pair<List<Object>, Double>>();

        pop.add(new Pair<>(listIntersections1, 1.0));

        pop.add(new Pair<>(listIntersections2, 3.0));

        pop.add(new Pair<>(listIntersections3, 8.0));

        AlgoTSP algo = new AlgoTSP(null, null);

        assert (algo.espacePopulation(pop, 10.0) == false);
        assert (algo.espacePopulation(pop, 1.0) == true);

    }

    /**
     * Méthode de test de la vérification de la génération d'un chromosome enfant
     */
    @Test
    public void testCrossoverOX() {

        List<Object> P1 = new ArrayList<Object>();
        List<Object> P2 = new ArrayList<Object>();

        Demande d1 = new Demande(TypeDemande.COLLECTE, (long) 1, "", (Integer) 0, null);
        Demande d2 = new Demande(TypeDemande.COLLECTE, (long) 2, "", (Integer) 0, null);
        Demande d3 = new Demande(TypeDemande.COLLECTE, (long) 3, "", (Integer) 0, null);
        Demande d4 = new Demande(TypeDemande.COLLECTE, (long) 4, "", (Integer) 0, null);
        Demande d5 = new Demande(TypeDemande.COLLECTE, (long) 5, "", (Integer) 0, null);
        Demande d6 = new Demande(TypeDemande.COLLECTE, (long) 6, "", (Integer) 0, null);
        Demande d7 = new Demande(TypeDemande.COLLECTE, (long) 7, "", (Integer) 0, null);
        Demande d8 = new Demande(TypeDemande.COLLECTE, (long) 8, "", (Integer) 0, null);
        Demande d9 = new Demande(TypeDemande.COLLECTE, (long) 9, "", (Integer) 0, null);

        P1.add(d1);
        P1.add(d3);
        P1.add(d2);
        P1.add(d6);
        P1.add(d4);
        P1.add(d5);
        P1.add(d9);
        P1.add(d7);
        P1.add(d8);

        P2.add(d3);
        P2.add(d7);
        P2.add(d8);
        P2.add(d1);
        P2.add(d4);
        P2.add(d9);
        P2.add(d2);
        P2.add(d5);
        P2.add(d6);

        AlgoTSP algo = new AlgoTSP(null, null);

        List<Object> enfant = algo.crossoverOX(P1, P2, 3, 5);

        int[] res = { 8, 1, 9, 6, 4, 5, 2, 3, 7 };

        for (int i = 0; i < enfant.size(); i++) {
            assert ((long) res[i] == ((Demande) enfant.get(i)).getIdIntersection());
        }

    }

    /**
     * Méthode de test pour vérifier le calul du coup total d'une tournée
     *
     * Préconditions : - calculerLesCheminsLesPlusCourts
     */
    @Test
    public void testCoutSimple() {

        // Création d'un graphe simple
        creerGrapheSimple();

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1, 5, "", 3, 5, ""));

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));

        // Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        List<Object> chromosome = new ArrayList<Object>();
        chromosome.add(requetes.get(0).getDemandeCollecte());
        chromosome.add(requetes.get(0).getDemandeLivraison());

        AlgoTSP algo = new AlgoTSP(0L, planning.getTrajetsLesPlusCourts());

        Double cout = algo.cout(chromosome);

        assert (cout == 5);
    }

    /**
     * Méthode de test pour vérifier le calul du coup total d'une tournée lorsque
     * des intersections sont à l'infini
     *
     * Préconditions : - calculerLesCheminsLesPlusCourts
     */
    @Test
    public void testCoutInfini() {

        // Création d'un graphe simple
        creerGraphePonderer();

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1, 5, "", 9, 5, ""));

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(9L));

        // Calcul des chemins les plus courts
        boolean test = planning.calculerLesTrajetsLesPlusCourts(intersections);

        assert (test == false);

    }

    /**
     * Méthode de test de la mutation avec recherche locale
     *
     * Préconditions : - calculerLesCheminsLesPlusCourts
     */
    @Test
    public void testMutationLocalSearch() {

        // Création d'un graphe

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        Requete requete = new Requete(1, 5, "", 3, 5, "");
        requetes.add(requete);

        Planning planning2 = new Planning(carte);
        // Calcul des chemins les plus courts
        planning2.calculerLesTrajetsLesPlusCourts(creerGraphe());

        planning2.setIdDepot(0L);
        planning2.setRequetes(requetes);

        List<Object> P1 = new ArrayList<Object>();

        P1.add(new Demande(TypeDemande.COLLECTE, (long) 3, "", (Integer) 0, null));
        P1.add(new Demande(TypeDemande.LIVRAISON, (long) 5, "", (Integer) 0, null));

        AlgoTSP algo = new AlgoTSP(0L, planning2.getTrajetsLesPlusCourts());

        double cout1 = algo.cout(P1);

        List<Object> mutation = algo.mutationLocalSearch(P1, cout1);

        assert (algo.cout(mutation) <= cout1);

    }

    /**
     * Méthode de test pour la vérification de la génération d'un chromosome
     * aléatoire
     *
     * Préconditions : - genererChromosomeAleatoire - verifierPop
     */
    @Test
    public void testGenererChromosome() {

        List<Object> demandes = new ArrayList<Object>();

        Requete r12 = new Requete(1, 0, "", 3, 0, "");
        Demande d1 = r12.getDemandeCollecte();
        Demande d2 = r12.getDemandeLivraison();
        Requete r34 = new Requete(4, 0, "", 9, 0, "");
        Demande d3 = r34.getDemandeCollecte();
        Demande d4 = r34.getDemandeLivraison();
        Requete r56 = new Requete(5, 0, "", 7, 0, "");
        Demande d5 = r56.getDemandeCollecte();
        Demande d6 = r56.getDemandeLivraison();

        demandes.add(d1);
        demandes.add(d2);
        demandes.add(d3);
        demandes.add(d4);
        demandes.add(d5);
        demandes.add(d6);

        AlgoTSP algo = new AlgoTSP(0L, null);

        List<Object> chromosome = algo.genererChromosomeAleatoire(demandes);

        assert (algo.verifierChromosome(chromosome) == true);

    }

    /**
     * Méthode de test de la boucle principale de l'algorithme génétique
     *
     * Précondition : - calculerLesCheminsLesPlusCourts - cout - mutationLocalSearch
     * - genererChromosomeAleatoire - verifierPop - correctionCrossover
     */
    @Test
    public void testTrouverMeilleureTourneeSimple() {

        // Création d'un grap simple
        creerGrapheSimple();

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        Requete requete = new Requete(1, 5, "", 3, 5, "");
        requetes.add(requete);

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        planning.setDateDebut(new Date(0));

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));

        // Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // recherche de la meilleur solution
        planning.calculerMeilleurTournee();

        List<Demande> demandesOrdonnees = planning.getDemandesOrdonnees();

        assert (demandesOrdonnees.size() == 2);

        assert (demandesOrdonnees.get(0) == requete.getDemandeCollecte());
        assert (demandesOrdonnees.get(1) == requete.getDemandeLivraison());

    }

    /**
     * Méthode de test de la boucle principale de l'algorithme génétique
     *
     * Précondition : - calculerLesCheminsLesPlusCourts - cout - mutationLocalSearch
     * - genererChromosomeAleatoire - verifierPop - correctionCrossover
     */
    @Test
    public void testTrouverMeilleureTourneeNormal() {

        // Création d'un grap simple

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();

        Requete requete1 = new Requete(3, 5, "", 5, 5, "");
        Requete requete2 = new Requete(6, 5, "", 8, 5, "");
        Requete requete3 = new Requete(9, 5, "", 4, 5, "");

        requetes.add(requete1);
        requetes.add(requete2);
        requetes.add(requete3);

        // Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(creerGraphe());
        planning.setIdDepot(0L);
        planning.setDateDebut(new Date(0));
        planning.setRequetes(requetes);

        // recherche de la meilleur solution
        planning.calculerMeilleurTournee();

        int dureeTotal = (int) planning.getDureeTotale();

        assert (dureeTotal == 49);

    }

    /**
     * Méthode de test pour vérifier les horraires de la tournée calculés
     *
     * Préconditions : - calculerLesCheminsLesPlusCourts - cout -
     * mutationLocalSearch - genererChromosomeAleatoire - verifierPop -
     * correctionCrossover
     */
    @Test
    public void testCalculerTourneeSimple() {

        creerGrapheSimple();

        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1, 3, "", 3, 5, ""));

        Planning planning = new Planning(carte);

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);

        planning.calculerMeilleurTournee();

        Demande demande;
        Long d1, d2;

        List<Demande> demandes = planning.getDemandesOrdonnees();

        assert (demandes.size() == 2);

        assert (planning.getDateDebut().getTime() == 0);
        assert (planning.getDateFin().getTime() == (5 * 3600 / 15 + 8000));
        assert (planning.getDureeTotale() == (5 * 3600 / 15 + 8000) / 1000);

        demande = demandes.get(0);
        d1 = demande.getDateArrivee().getTime();
        d2 = demande.getDateDepart().getTime();
        assert (d1 == 1 * 3600 / 15);
        assert (d2 == 1 * 3600 / 15 + 3000);

        demande = demandes.get(1);
        d1 = demande.getDateArrivee().getTime();
        d2 = demande.getDateDepart().getTime();
        assert (d1 == 3 * 3600 / 15 + 3000);
        assert (d2 == 3 * 3600 / 15 + 8000);
    }

    /**
     * Méthode pour tester la modification d'une requête en remplaçant un point par
     * un nouveau
     */
    @Test
    public void testModifierRequetesIntersection() {
        // Création d'un graphe
        creerGraphe();

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();

        Requete r1 = new Requete(1, 5, "", 3, 5, "");
        requetes.add(r1);
        requetes.add(new Requete(4, 5, "", 9, 5, ""));
        requetes.add(new Requete(6, 5, "", 8, 5, ""));

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);

        planning.calculerMeilleurTournee();

        Integer dureeAvantChangement = planning.getDureeTotale();

        r1.getDemandeLivraison().setIdIntersection(7L);
        planning.recalculerTournee();

        // on vérifie si la durée totale du nouveau planning est cohérente avec celle
        // d'avant la modif
        Integer nouvelleDureeTotale = planning.getDureeTotale();
        assert (dureeAvantChangement == 49);
        assert (nouvelleDureeTotale == 49);

    }

    /**
     * Méthode pour tester l'ajout de requête après le calcul de la tournée
     */
    @Test
    public void testAjouterRequetes() {
        // Création d'un graphe

        creerGraphe();

        // Création des requetes de référence
        List<Requete> requetes = new ArrayList<Requete>();

        requetes.add(new Requete(1, 5, "", 3, 5, ""));
        requetes.add(new Requete(4, 5, "", 9, 5, ""));
        requetes.add(new Requete(6, 5, "", 8, 5, ""));

        Planning planning = new Planning(carte);

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);

        planning.calculerMeilleurTournee();

        double dureeReference = planning.getDureeTotale();

        // Création des requetes de test
        List<Requete> requetesTest = new ArrayList<Requete>();

        requetesTest.add(new Requete(1, 5, "", 3, 5, ""));
        requetesTest.add(new Requete(4, 5, "", 9, 5, ""));

        Planning planningTest = new Planning(carte);

        planningTest.setIdDepot(0L);
        planningTest.setRequetes(requetesTest);
        Date dTest = new Date(0);
        planningTest.setDateDebut(dTest);

        planningTest.calculerMeilleurTournee();

        // ajout d'une nouvelle requête

        planningTest.ajouterRequete(new Requete(6, 5, "", 8, 5, ""));

        // on vérifie si la durée totale du nouveau planning est cohérente

        assert (Math.abs(planningTest.getDureeTotale() - dureeReference) <= 1);

    }

    /**
     * Méthode pour créer un graphe orienté simple avec 4 intersections
     */
    private List<Intersection> creerGrapheSimple() {

        planning.setIdDepot(0L);

        ArrayList<Intersection> intersections = new ArrayList<Intersection>();

        Intersection i0 = new Intersection(0, 0, 0);
        Intersection i1 = new Intersection(1, 0, 0);
        Intersection i2 = new Intersection(2, 0, 0);
        Intersection i3 = new Intersection(3, 0, 0);

        Segment s01 = new Segment(1, "nom", 0, 1);
        Segment s02 = new Segment(3, "nom", 0, 2);
        Segment s03 = new Segment(4, "nom", 0, 3);
        Segment s10 = new Segment(1, "nom", 1, 0);
        Segment s12 = new Segment(1, "nom", 1, 2);
        Segment s20 = new Segment(1, "nom", 2, 0);
        Segment s21 = new Segment(1, "nom", 2, 1);
        Segment s23 = new Segment(1, "nom", 2, 3);
        Segment s30 = new Segment(4, "nom", 3, 0);
        Segment s32 = new Segment(1, "nom", 3, 2);

        Set<Segment> set0 = new HashSet<Segment>();
        Set<Segment> set1 = new HashSet<Segment>();
        Set<Segment> set2 = new HashSet<Segment>();
        Set<Segment> set3 = new HashSet<Segment>();

        set0.add(s01);
        set0.add(s02);
        set0.add(s03);
        set1.add(s10);
        set1.add(s12);
        set2.add(s20);
        set2.add(s21);
        set2.add(s23);
        set3.add(s30);
        set3.add(s32);

        i0.setSegmentsPartants(set0);
        i1.setSegmentsPartants(set1);
        i2.setSegmentsPartants(set2);
        i3.setSegmentsPartants(set3);

        carte.getIntersections().put(i0.getId(), i0);
        carte.getIntersections().put(i1.getId(), i1);
        carte.getIntersections().put(i2.getId(), i2);
        carte.getIntersections().put(i3.getId(), i3);

        intersections.add(i0);
        intersections.add(i1);
        intersections.add(i2);
        intersections.add(i3);

        return intersections;

    }

    /**
     * Méthode pour créer un graphe non orienté avec 10 intersections
     */
    private List<Intersection> creerGraphe() {
        // http://yallouz.arie.free.fr/terminale_cours/graphes/graphes.php?page=g3

        planning.setIdDepot(0L);

        Intersection i0 = new Intersection(0, 0, 0);
        Intersection i1 = new Intersection(1, 0, 0);
        Intersection i2 = new Intersection(2, 0, 0);
        Intersection i3 = new Intersection(3, 0, 0);
        Intersection i4 = new Intersection(4, 0, 0);
        Intersection i5 = new Intersection(5, 0, 0);
        Intersection i6 = new Intersection(6, 0, 0);
        Intersection i7 = new Intersection(7, 0, 0);
        Intersection i8 = new Intersection(8, 0, 0);
        Intersection i9 = new Intersection(9, 0, 0);

        Segment s01 = new Segment(4, "nom", 0, 1);
        Segment s10 = new Segment(4, "nom", 1, 0);

        Segment s12 = new Segment(10, "nom", 1, 2);
        Segment s21 = new Segment(10, "nom", 2, 1);

        Segment s13 = new Segment(17, "nom", 1, 3);
        Segment s31 = new Segment(17, "nom", 3, 1);

        Segment s16 = new Segment(7, "nom", 1, 6);
        Segment s61 = new Segment(7, "nom", 6, 1);

        Segment s14 = new Segment(18, "nom", 1, 4);
        Segment s41 = new Segment(18, "nom", 4, 1);

        Segment s23 = new Segment(8, "nom", 2, 3);
        Segment s32 = new Segment(8, "nom", 3, 2);

        Segment s29 = new Segment(13, "nom", 2, 9);
        Segment s92 = new Segment(13, "nom", 9, 2);

        Segment s39 = new Segment(15, "nom", 3, 9);
        Segment s93 = new Segment(15, "nom", 9, 3);

        Segment s37 = new Segment(9, "nom", 3, 7);
        Segment s73 = new Segment(9, "nom", 7, 3);

        Segment s79 = new Segment(8, "nom", 7, 9);
        Segment s97 = new Segment(8, "nom", 9, 7);

        Segment s67 = new Segment(16, "nom", 6, 7);
        Segment s76 = new Segment(16, "nom", 7, 6);

        Segment s36 = new Segment(5, "nom", 3, 6);
        Segment s63 = new Segment(5, "nom", 6, 3);

        Segment s04 = new Segment(3, "nom", 0, 4);
        Segment s40 = new Segment(3, "nom", 4, 0);

        Segment s48 = new Segment(19, "nom", 4, 8);
        Segment s84 = new Segment(19, "nom", 8, 4);

        Segment s08 = new Segment(11, "nom", 0, 8);
        Segment s80 = new Segment(11, "nom", 8, 0);

        Segment s58 = new Segment(6, "nom", 5, 8);
        Segment s85 = new Segment(6, "nom", 8, 5);

        Segment s56 = new Segment(11, "nom", 5, 6);
        Segment s65 = new Segment(11, "nom", 6, 5);

        Segment s45 = new Segment(8, "nom", 4, 5);
        Segment s54 = new Segment(8, "nom", 5, 4);

        Segment s64 = new Segment(17, "nom", 6, 4);
        Segment s46 = new Segment(17, "nom", 4, 6);

        Set<Segment> set0 = new HashSet<Segment>();
        Set<Segment> set1 = new HashSet<Segment>();
        Set<Segment> set2 = new HashSet<Segment>();
        Set<Segment> set3 = new HashSet<Segment>();
        Set<Segment> set4 = new HashSet<Segment>();
        Set<Segment> set5 = new HashSet<Segment>();
        Set<Segment> set6 = new HashSet<Segment>();
        Set<Segment> set7 = new HashSet<Segment>();
        Set<Segment> set8 = new HashSet<Segment>();
        Set<Segment> set9 = new HashSet<Segment>();

        set0.add(s01);
        set0.add(s04);
        set0.add(s08);

        set1.add(s10);
        set1.add(s12);
        set1.add(s13);
        set1.add(s14);
        set1.add(s16);

        set2.add(s21);
        set2.add(s23);
        set2.add(s29);

        set3.add(s31);
        set3.add(s32);
        set3.add(s36);
        set3.add(s37);
        set3.add(s39);

        set4.add(s40);
        set4.add(s41);
        set4.add(s45);
        set4.add(s46);
        set4.add(s48);

        set5.add(s54);
        set5.add(s56);
        set5.add(s58);

        set6.add(s61);
        set6.add(s63);
        set6.add(s64);
        set6.add(s65);
        set6.add(s67);

        set7.add(s73);
        set7.add(s76);
        set7.add(s79);

        set8.add(s80);
        set8.add(s84);
        set8.add(s85);

        set9.add(s92);
        set9.add(s93);
        set9.add(s97);

        i0.setSegmentsPartants(set0);
        i1.setSegmentsPartants(set1);
        i2.setSegmentsPartants(set2);
        i3.setSegmentsPartants(set3);
        i4.setSegmentsPartants(set4);
        i5.setSegmentsPartants(set5);
        i6.setSegmentsPartants(set6);
        i7.setSegmentsPartants(set7);
        i8.setSegmentsPartants(set8);
        i9.setSegmentsPartants(set9);

        carte.getIntersections().put(i0.getId(), i0);
        carte.getIntersections().put(i1.getId(), i1);
        carte.getIntersections().put(i2.getId(), i2);
        carte.getIntersections().put(i3.getId(), i3);
        carte.getIntersections().put(i4.getId(), i4);
        carte.getIntersections().put(i5.getId(), i5);
        carte.getIntersections().put(i6.getId(), i6);
        carte.getIntersections().put(i7.getId(), i7);
        carte.getIntersections().put(i8.getId(), i8);
        carte.getIntersections().put(i9.getId(), i9);

        ArrayList<Intersection> intersections = new ArrayList<Intersection>();

        intersections.add(i0);
        intersections.add(i1);
        intersections.add(i2);
        intersections.add(i3);
        intersections.add(i4);
        intersections.add(i5);
        intersections.add(i6);
        intersections.add(i7);
        intersections.add(i8);
        intersections.add(i9);

        return intersections;

    }

    /**
     * Méthode pour créer un graphe non orienté avec 10 intersections avec un cul de
     * sac et un point inaccessible
     */
    private void creerGraphePonderer() {
        // http://yallouz.arie.free.fr/terminale_cours/graphes/graphes.php?page=g3

        // Caractéristiques :
        // J est innacessible
        // H est un cul de sac

        planning.setIdDepot(0L);

        Intersection i0 = new Intersection(0, 0, 0); // A
        Intersection i1 = new Intersection(1, 0, 0); // B
        Intersection i2 = new Intersection(2, 0, 0); // C
        Intersection i3 = new Intersection(3, 0, 0); // D
        Intersection i4 = new Intersection(4, 0, 0); // E
        Intersection i5 = new Intersection(5, 0, 0); // F
        Intersection i6 = new Intersection(6, 0, 0); // G
        Intersection i7 = new Intersection(7, 0, 0); // H
        Intersection i8 = new Intersection(8, 0, 0); // I
        Intersection i9 = new Intersection(9, 0, 0); // J

        Segment s01 = new Segment(4, "nom", 0, 1);
        Segment s10 = new Segment(4, "nom", 1, 0);

        Segment s12 = new Segment(10, "nom", 1, 2);
        Segment s21 = new Segment(10, "nom", 2, 1);

        Segment s13 = new Segment(17, "nom", 1, 3);
        Segment s31 = new Segment(17, "nom", 3, 1);

        Segment s16 = new Segment(7, "nom", 1, 6);
        Segment s61 = new Segment(7, "nom", 6, 1);

        Segment s14 = new Segment(18, "nom", 1, 4);
        Segment s41 = new Segment(18, "nom", 4, 1);

        Segment s23 = new Segment(8, "nom", 2, 3);
        Segment s32 = new Segment(8, "nom", 3, 2);

        Segment s37 = new Segment(9, "nom", 3, 7);
        Segment s73 = new Segment(9, "nom", 7, 3);

        Segment s36 = new Segment(5, "nom", 3, 6);
        Segment s63 = new Segment(5, "nom", 6, 3);

        Segment s04 = new Segment(3, "nom", 0, 4);
        Segment s40 = new Segment(3, "nom", 4, 0);

        Segment s48 = new Segment(19, "nom", 4, 8);
        Segment s84 = new Segment(19, "nom", 8, 4);

        Segment s08 = new Segment(11, "nom", 0, 8);
        Segment s80 = new Segment(11, "nom", 8, 0);

        Segment s58 = new Segment(6, "nom", 5, 8);
        Segment s85 = new Segment(6, "nom", 8, 5);

        Segment s56 = new Segment(11, "nom", 5, 6);
        Segment s65 = new Segment(11, "nom", 6, 5);

        Segment s45 = new Segment(8, "nom", 4, 5);
        Segment s54 = new Segment(8, "nom", 5, 4);

        Segment s64 = new Segment(17, "nom", 6, 4);
        Segment s46 = new Segment(17, "nom", 4, 6);

        Set<Segment> set0 = new HashSet<Segment>();
        Set<Segment> set1 = new HashSet<Segment>();
        Set<Segment> set2 = new HashSet<Segment>();
        Set<Segment> set3 = new HashSet<Segment>();
        Set<Segment> set4 = new HashSet<Segment>();
        Set<Segment> set5 = new HashSet<Segment>();
        Set<Segment> set6 = new HashSet<Segment>();
        Set<Segment> set7 = new HashSet<Segment>();
        Set<Segment> set8 = new HashSet<Segment>();
        Set<Segment> set9 = new HashSet<Segment>();

        set0.add(s01);
        set0.add(s04);
        set0.add(s08);

        set1.add(s10);
        set1.add(s12);
        set1.add(s13);
        set1.add(s14);
        set1.add(s16);

        set2.add(s21);
        set2.add(s23);

        set3.add(s31);
        set3.add(s32);
        set3.add(s36);
        set3.add(s37);

        set4.add(s40);
        set4.add(s41);
        set4.add(s45);
        set4.add(s46);
        set4.add(s48);

        set5.add(s54);
        set5.add(s56);
        set5.add(s58);

        set6.add(s61);
        set6.add(s63);
        set6.add(s64);
        set6.add(s65);

        set7.add(s73);

        set8.add(s80);
        set8.add(s84);
        set8.add(s85);

        i0.setSegmentsPartants(set0);
        i1.setSegmentsPartants(set1);
        i2.setSegmentsPartants(set2);
        i3.setSegmentsPartants(set3);
        i4.setSegmentsPartants(set4);
        i5.setSegmentsPartants(set5);
        i6.setSegmentsPartants(set6);
        i7.setSegmentsPartants(set7);
        i8.setSegmentsPartants(set8);
        i9.setSegmentsPartants(set9);

        carte.getIntersections().put(i0.getId(), i0);
        carte.getIntersections().put(i1.getId(), i1);
        carte.getIntersections().put(i2.getId(), i2);
        carte.getIntersections().put(i3.getId(), i3);
        carte.getIntersections().put(i4.getId(), i4);
        carte.getIntersections().put(i5.getId(), i5);
        carte.getIntersections().put(i6.getId(), i6);
        carte.getIntersections().put(i7.getId(), i7);
        carte.getIntersections().put(i8.getId(), i8);
        carte.getIntersections().put(i9.getId(), i9);

    }

}