
package fr.hexaone.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Comparator;
import java.util.Date;

import org.javatuples.Pair;

/**
 * Objet contenant les structures de données relatives à la carte"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Carte {

    // Créer une nouvelle map entre les nouveaux points des requetes et les
    // intersections de la map
    // map<Long,Long> inter requete -> inter carte
    // map<Intersection,List<Intersection>> inter carte -> list inter requete
    // ou directement list dans une intersection

    /**
     * Map permettant d'identifier les chemins les plus courts à partir d'un
     * identifiant (String)
     */
    protected Map<String, Trajet> cheminsLesPlusCourts;
    /**
     * Map permettant d'identifier les intersections à partir de leur id
     */
    protected Map<Long, Intersection> intersections;
    /**
     * id du dépot
     */
    protected Long depotId;

    /**
     * Comparateur d'intersections en fonction de la distance
     */
    protected Comparator<Intersection> ComparatorChemin = new Comparator<Intersection>() {
        @Override
        public int compare(Intersection o1, Intersection o2) {
            if (o1.getDistance() > o2.getDistance()) {
                return 1;
            } else if (o1.getDistance() < o2.getDistance()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * Comparateur afin de classer les chromosomes au sein d'une population dans
     * l'ordre croissant des coûts
     *
     * @param depot
     * @param requetes
     */
    protected Comparator<Pair<List<Long>, Double>> ComparatorChromosome = new Comparator<Pair<List<Long>, Double>>() {

        @Override
        public int compare(Pair<List<Long>, Double> e1, Pair<List<Long>, Double> e2) {
            return (int) (e1.getValue1() - e2.getValue1());
        }
    };

    /**
     * Constructeur par défaut de Carte
     */
    public Carte() {
        intersections = new HashMap<>();
    }

    /**
     * Recherche des chemins les plus courts
     *
     * @param requetes
     */
    public void calculerLesCheminsLesPlusCourts(List<Requete> requetes) {

        // Préparation

        List<Intersection> specialIntersections = new ArrayList<Intersection>();
        specialIntersections.add(intersections.get(depotId));

        for (Requete r : requetes) {
            specialIntersections.add(intersections.get(r.getDemandeLivraison().getIdIntersection()));
            specialIntersections.add(intersections.get(r.getDemandeCollecte().getIdIntersection()));

        }

        this.cheminsLesPlusCourts = new HashMap<String, Trajet>();

        // Calcul de tous les chemins les plus courts n fois avec dijkstra

        for (Intersection source : specialIntersections) {

            source.setDistance(0.);

            Set<Intersection> settledIntersections = new HashSet<Intersection>();

            Set<Intersection> unsettledIntersections = new HashSet<Intersection>();

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
                    Intersection adjacentIntersection = intersections.get(segmentAdjacent.getArrivee());
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

            for (Intersection i : specialIntersections) {
                String key = sourceId + i.getId();
                this.cheminsLesPlusCourts.put(key, new Trajet(i.getCheminLePlusCourt(), i.getDistance()));
            }

            intersections.forEach((id, intersection) -> {
                intersection.resetIntersection();
            });
        }
    }

    /**
     * Retourne l'intersection avec la distance la plus faible
     * 
     * @param unsettledIntersections
     * @return
     */
    private Intersection getLowestDistanceIntersection(Set<Intersection> unsettledIntersections) {
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
     * Calcule les distances minimales
     * 
     * @param evaluationIntersection
     * @param edgeWeigh
     * @param sourceIntersection
     */
    private void CalculateMinimumDistance(Intersection evaluationIntersection, Double edgeWeigh,
            Intersection sourceIntersection, Segment seg) {
        Double sourceDistance = sourceIntersection.getDistance();
        if (sourceDistance + edgeWeigh < evaluationIntersection.getDistance()) {
            evaluationIntersection.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Segment> shortestPath = new LinkedList<>(sourceIntersection.getCheminLePlusCourt());
            shortestPath.add(seg);
            evaluationIntersection.setCheminLePlusCourt(shortestPath);
        }
    }

    /**
     * Algorithme génétique permettant de trouver le melleure tournée
     *
     * @param depot
     * @param requetes
     */
    public List<Long> trouverMeilleureTournee(List<Requete> requetes) {

        // initialisation des paramètres maximums de l'algo génétique

        int sigma = 30; // Nb chromosomes max dans une population
        double delta = 3;// Minimum d'ecart entre les valeurs dans la population
        double p = 0.1; // probabilité d'améliorer avec du Local Search un enfant
        int nMax = 10000;// Nb max d'itérations pour générer une pop initiale
        int alphamax = 30000;// Nb max de crossovers productifs
        int BetaMax = 10000;// Nb max de crossosovers sans améliorer la solution
        int maxIter = 1000;// Nb max d'iterations d'affilé à ne pas trouver de solution

        // création de la population
        List<Pair<List<Long>, Double>> population = new ArrayList<Pair<List<Long>, Double>>();

        // 1er élément de la population obtenu avec du 2-opt
        List<Long> chr1 = this.genererChromosomeAleatoire(requetes);
        chr1 = this.mutationLocalSearch(chr1, cout(chr1), requetes);
        population.add(new Pair<>(chr1, cout(chr1)));

        // initialisation des variables
        int k = 1;
        int nbEssais = 0;
        int iter = 0;

        double cost;
        Pair<List<Long>, Double> chrom;

        Random rand = new Random();

        // génération aléatoire de la population initiale
        while (k <= sigma && nbEssais <= nMax) {
            k++;
            nbEssais = 0;

            do {
                // on essaie de générer un chromosome aléatoire avec un nombre max d'essais
                nbEssais++;
                List<Long> chr = genererChromosomeAleatoire(requetes);
                cost = cout(chr);
                chrom = new Pair<>(chr, cost);
            } while (nbEssais <= nMax && !espacePopulation(population, delta, cost));
            if (nbEssais <= nMax) {
                // la génération a réussi
                population.add(chrom);
            }
        }
        if (nbEssais > nMax) {
            sigma = k - 1;
        }

        // tri par ordre coissant de cout la population
        Collections.sort(population, ComparatorChromosome);

        /*
         * for (Pair<List<Long>,Double> pair : population) {
         *
         * System.out.print(pair.getValue1() + " : "); List<Long> ll = pair.getValue0();
         * for (Long l : ll) { System.out.print(l+ ", "); } System.out.println(); }
         */

        int i1;
        int i2;
        int indexP1;
        int indexP2;

        int alpha = 0;
        int beta = 0;

        // boucle principale
        while (alpha < alphamax && beta < BetaMax && iter <= maxIter) {

            indexP1 = 0;
            indexP2 = 0;

            // génération aléatoire de 2 indices i et j < poulation.size()
            for (int i = 0; i < 2; i++) {

                i1 = rand.nextInt(population.size());
                i2 = rand.nextInt(population.size());

                if (i == 0) {
                    indexP1 = i2;
                    if (population.get(i1).getValue1() < population.get(i2).getValue1()) {
                        indexP1 = i1;
                    }
                } else {
                    indexP2 = i2;
                    if (population.get(i1).getValue1() < population.get(i2).getValue1()) {
                        indexP2 = i1;
                    }
                }
            }

            int choiceChild = rand.nextInt(2);

            // génération des enfants : un seul survit aléatoirement
            List<Long> child;

            if (choiceChild == 0) {
                child = crossoverOX(population.get(indexP1).getValue0(), population.get(indexP2).getValue0(),
                        rand.nextInt(population.get(0).getValue0().size()),
                        rand.nextInt(population.get(0).getValue0().size()));
            } else {
                child = crossoverOX(population.get(indexP2).getValue0(), population.get(indexP1).getValue0(),
                        rand.nextInt(population.get(0).getValue0().size()),
                        rand.nextInt(population.get(0).getValue0().size()));
            }

            // correction des contraintes de précédence sur l'enfant
            child = correctionCrossover(child, requetes);

            // nb aléatoire compris entre sigma/2 et sigma
            int kRand = rand.nextInt(sigma - sigma / 2) + (sigma / 2);

            double costChild = cout(child);

            if (Math.random() < p) {
                // mutation de l'enfant : on l'améliore avec de la recherche locale

                List<Long> M = this.mutationLocalSearch(child, cout(child), requetes);

                double ancienCout = population.get(kRand).getValue1();
                population.get(kRand).setAt1(Integer.MIN_VALUE);

                if (this.espacePopulation(population, delta, costChild)) {
                    // si les contraintes d'espacement sont vérifiés avec le nouvel enfant
                    child = M;
                    costChild = cout(child);
                }

                population.get(kRand).setAt1(ancienCout);

            }

            double ancienCout = population.get(kRand).getValue1();
            population.get(kRand).setAt1(Integer.MIN_VALUE);

            if (this.espacePopulation(population, delta, costChild)) {

                // itération productive
                alpha++;
                iter = 0;

                population.remove(kRand);
                population.add(new Pair<>(child, costChild));

                if (costChild >= population.get(0).getValue1()) {
                    // pas d'amélioration de la meilleure solution
                    beta++;

                } else {
                    beta = 0;
                }

                Collections.sort(population, ComparatorChromosome);
            }

            else {
                // l'enfant n'est pas acceptable dans la population
                population.get(kRand).setAt1(ancienCout);
                iter++;
            }
        }

        return population.get(0).getValue0();
    }

    /**
     * Calcule le cout d'un chromsomome (une solution à la tournée)
     *
     * @param chromosome que l'on teste
     */
    public double cout(List<Long> chromosome) {

        double somme = 0;
        Long prevIntersectionId = this.depotId;

        for (Long newId : chromosome) {
            newId = Planning.idUniqueTOIdIntersection.get(newId);
            somme += cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId).getPoids();
            prevIntersectionId = newId;
        }
        somme += cheminsLesPlusCourts.get(prevIntersectionId + "|" + depotId).getPoids();

        return somme;
    }

    /**
     * Mutation d'un chromosme grâce à un algorithme de recherche locale
     *
     * @param chromosome que l'on teste
     * @param requetes   liste des requêtes
     */

    public List<Long> mutationLocalSearch(List<Long> chromosome, double coutIni, List<Requete> requetes) {

        Boolean amelioration = true;
        double coutMin = coutIni;

        while (amelioration == true) {
            amelioration = false;

            // parcours des paires de chromosomes
            for (int i = 1; i < chromosome.size() - 2; i++) {
                for (int j = 1; j < chromosome.size() - 2; j++) {
                    if (i != j && j != i - 1 && j != i + 1) {
                        // recherche 2-opt
                        long u = chromosome.get(i);
                        long v = chromosome.get(j);
                        long x = chromosome.get(i + 1);
                        long y = chromosome.get(j + 1);

                        chromosome.set(i + 1, v);
                        chromosome.set(j, x);

                        if (this.verifierPop(chromosome, requetes)) {
                            double cout = cout(chromosome);

                            if (cout < coutMin) {
                                // solution avec meilleur coût : on la garde
                                coutMin = cout;
                                amelioration = true;
                                break;
                            }
                        }
                        chromosome.set(i + 1, x);
                        chromosome.set(j, v);
                    }
                }
                if (amelioration == true) {
                    // on recommence le processus
                    break;
                }
            }
        }
        return chromosome;
    }

    /**
     * Renvoie true si toutes les contraintes de précédences sont respectées, faux
     * sinon
     *
     * @param chromosome que l'on teste
     * @param requetes   liste des requêtes
     */

    public Boolean verifierPop(List<Long> chromosome, List<Requete> requetes) {

        for (Requete r : requetes) {
            Boolean livraison = false;
            Boolean collecte = false;
            for (Long c : chromosome) {
                // vérifie qu'un point de collecte n'est pas après sa livraison
                if (livraison == true && collecte == true) {
                    break;
                }
                if (c == r.getDemandeCollecte().getIdDemande()) {
                    collecte = true;
                    if (livraison == false) {
                        continue;
                    } else {
                        return false;
                    }
                }

                if (c == r.getDemandeLivraison().getIdDemande()) {
                    livraison = true;

                    if (collecte == false) {
                        return false;
                    } else {
                        continue;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart
     * en termes de couts
     *
     * @param population à tester
     * @param ecart      maximum
     */

    public Boolean espacePopulation(List<Pair<List<Long>, Double>> population, double ecart) {

        for (int i = 1; i < population.size(); i++) { // TODO foreach pour pas faire get()

            if (Math.abs(population.get(i).getValue1() - population.get(i - 1).getValue1()) < ecart) {
                return false;
            }

        }
        return true;

    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart
     * en termes de couts avec l'ajout de l'enfant
     *
     * @param population   à tester
     * @param ecart        maximum
     * @param valeurEnfant
     */

    public Boolean espacePopulation(List<Pair<List<Long>, Double>> population, double ecart, double valeurEnfant) {

        for (int i = 0; i < population.size(); i++) { // TODO foreach pour pas faire get()

            if (Math.abs(population.get(i).getValue1() - valeurEnfant) < ecart) {
                return false;
            }

        }
        return true;

    }

    /**
     * Renvoie une liste de chromosomes aléatoires (donc une liste d'intersections)
     *
     * @param depot
     * @param requetes
     */

    public List<Long> genererChromosomeAleatoire(List<Requete> requetes) {
        List<Long> shuffleList;

        do {
            shuffleList = new ArrayList<Long>();

            for (Requete r : requetes) {
                shuffleList.add(r.getDemandeCollecte().getIdDemande());
                shuffleList.add(r.getDemandeLivraison().getIdDemande());
            }

            // réarrangement aléatoire de la population
            Collections.shuffle(shuffleList);
        } while (!verifierPop(shuffleList, requetes));

        return shuffleList;

    }

    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     *
     * @param depot
     * @param requetes
     */
    public List<Long> crossoverOX(List<Long> P1, List<Long> P2, int i, int j) {

        List<Long> child = new ArrayList<Long>();

        for (int k = 0; k < P1.size(); k++) {
            child.add((long) 0);
        }

        int max = max(i, j);
        int min = min(i, j);

        Set<Long> intersectionsVus = new HashSet<Long>();

        for (int k = min; k <= max; k++) {

            child.set(k, P1.get(k));
            intersectionsVus.add(P1.get(k));
        }

        int k = max + 1;
        int p = max + 1;

        int c = 0;

        // suite d'opérations réalisées afin de faire le croisement
        while (c < P1.size()) {
            c++;
            if (k >= P1.size()) {
                k = 0;
            }
            if (p >= P1.size()) {
                p = 0;
            }
            if (intersectionsVus.contains(P2.get(k))) {
                k++;
                continue;
            }
            child.set(p, P2.get(k));
            p++;
            k++;
        }

        return child;
    }

    /**
     * Permet la correction d'un chromosome en intégrant les conditions de
     * précédence des requêtes Echange la place des couples <pickup,delivery> quand
     * ils sont inversés
     *
     * @param requetes
     * @param chromosome
     */
    public List<Long> correctionCrossover(List<Long> chromosome, List<Requete> requetes) {

        for (Requete r : requetes) {

            int indiceCollecte = chromosome.indexOf(r.getDemandeCollecte().getIdDemande());
            int indiceLivraison = chromosome.indexOf(r.getDemandeLivraison().getIdDemande());

            if (indiceLivraison < indiceCollecte) {
                // la livraison est après la collecte : on inverse les 2 points
                long id = chromosome.get(indiceCollecte);
                chromosome.set(indiceCollecte, r.getDemandeLivraison().getIdDemande());
                chromosome.set(indiceLivraison, id);
            }
        }

        return chromosome;
    }

    /**
     * Retourne le max entre a et b
     *
     * @param a
     * @param b
     */
    private int max(int a, int b) {

        if (a > b) {
            return a;
        }
        return b;

    }

    /**
     * Retourne le min entre a et b
     *
     * @param a
     * @param b
     */
    private int min(int a, int b) {

        if (a < b) {
            return a;
        }
        return b;
    }

    /**
     * Getter
     *
     * @return Les plus courts chemins
     */
    public Map<String, Trajet> getCheminsLesPlusCourts() {
        return cheminsLesPlusCourts;
    }

    /**
     * Getter
     *
     * @return Les intersections
     */
    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Getter
     *
     * @return l'id du dépot
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * Setter
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

}
