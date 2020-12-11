package fr.hexaone.algo;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Trajet;
import fr.hexaone.model.TypeDemande;
import org.javatuples.Pair;

import java.util.*;

/**
 * Classe définissant les méthodes spécifiques utilisées pour résoudre le
 * problème du voyageur de commerce avec contraintes de précédences. Utilise le
 * design pattern Template.
 *
 * @author HexaOne
 * @version 1.0
 */
public class AlgoTSP extends AlgoGenetique {

    /**
     * Map permettant d'identifier les chemins les plus courts à partir d'un
     * identifiant (String)
     */
    private final Map<String, Trajet> CHEMINS_LES_PLUS_COURTS;

    /**
     * id du dépot
     */
    private final Long ID_DEPOT;

    /**
     * Constructeur de AlgoTSP
     *
     * @param sigma                Nombre de chromosomes max dans une population
     * @param delta                Ecart minimum entre les valeurs dans la
     *                             population
     * @param p                    Probabilité d'améliorer un enfant avec du Local
     *                             Search
     * @param nMax                 Nombre max d'itérations pour générer une
     *                             population initiale
     * @param alphaMax             Nombre max de crossovers productifs
     * @param betaMax              Nombre max de crossosovers sans améliorer la
     *                             solution
     * @param maxIter              Nombre max d'itérations d'affilée à ne pas
     *                             trouver de solution
     * @param depotId              Id du dépot
     * @param cheminsLesPlusCourts Liste des chemins les plus courts
     */
    public AlgoTSP(int sigma, int delta, int p, int nMax, int alphaMax, int betaMax, int maxIter, Long depotId,
            Map<String, Trajet> cheminsLesPlusCourts) {
        super(sigma, delta, p, nMax, alphaMax, betaMax, maxIter);
        this.ID_DEPOT = depotId;
        this.CHEMINS_LES_PLUS_COURTS = cheminsLesPlusCourts;
    }

    /**
     * Constructeur de AlgoTSP utilisant les paramètres par défaut de l'algorithme
     * génétique
     *
     * @param depotId              Id du dépot
     * @param cheminsLesPlusCourts Liste des chemins les plus courts
     */
    public AlgoTSP(Long depotId, Map<String, Trajet> cheminsLesPlusCourts) {
        super();
        this.ID_DEPOT = depotId;
        this.CHEMINS_LES_PLUS_COURTS = cheminsLesPlusCourts;
    }

    /**
     * Renvoie un chromosome aléatoire (donc une liste de Demande ici)
     *
     * @param chromosome Chromosome servant à générer le nouveau chromosome
     *                   aléatoire
     * @return Un chromosome aléatoire
     */
    @Override
    public List<Object> genererChromosomeAleatoire(List<Object> chromosome) {
        List<Object> shuffleList = new ArrayList<Object>(chromosome);

        do { // réarrangement aléatoire de la population
            Collections.shuffle(shuffleList);
        } while (!verifierChromosome(shuffleList));

        return shuffleList;
    }

    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     *
     * @param P1 Premier parent
     * @param P2 Deuxième parent
     * @param i  Indice 1
     * @param j  Indice 2
     * @return Le chromosome enfant
     */
    @Override
    public List<Object> crossoverOX(List<Object> P1, List<Object> P2, int i, int j) {

        List<Object> enfant = new ArrayList<Object>();

        for (int k = 0; k < P1.size(); k++) {
            enfant.add(null);
        }

        int max = Math.max(i, j);
        int min = Math.min(i, j);

        Set<Object> intersectionsVues = new HashSet<Object>();

        for (int k = min; k <= max; k++) {
            enfant.set(k, P1.get(k));
            intersectionsVues.add(P1.get(k));
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
            if (intersectionsVues.contains(P2.get(k))) {
                k++;
                continue;
            }
            enfant.set(p, P2.get(k));
            p++;
            k++;
        }

        return enfant;
    }

    /**
     * Permet la correction d'un chromosome en intégrant les conditions de
     * précédence des requêtes. Echange la place des couples collecte-livraison
     * quand ils sont inversés
     *
     * @param chromosome Le chromosome à corriger
     * @return Le nouveau chromosome corrigé
     */
    @Override
    public List<Object> correctionCrossover(List<Object> chromosome) {
        List<Pair<Integer, Integer>> listeSwap = new ArrayList<Pair<Integer, Integer>>();

        Integer indexLivraison = 0;
        for (Object demande : chromosome) {
            if (((Demande) demande).getTypeDemande() == TypeDemande.LIVRAISON) {
                Integer indexCollecte = chromosome.indexOf(((Demande) demande).getRequete().getDemandeCollecte());
                if (indexLivraison < indexCollecte) {
                    listeSwap.add(new Pair<Integer, Integer>(indexCollecte, indexLivraison));
                }
            }
            indexLivraison += 1;
        }

        for (Pair<Integer, Integer> pair : listeSwap) {
            Collections.swap(chromosome, pair.getValue0(), pair.getValue1());
        }

        return chromosome;
    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à
     * l'écart précisé, en termes de coûts avec l'ajout de l'enfant. Renvoie false
     * sinon
     *
     * @param population   La population dans laquelle on souhaite vérifier
     *                     l'espacement
     * @param ecart        Représente l'espacement minimum
     * @param valeurEnfant Le coût de l'enfant
     * @return true si la population est assez "espacée", false sinon
     */
    @Override
    public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart, Double valeurEnfant) {
        for (Pair<List<Object>, Double> chromosome : population) {
            if (Math.abs(chromosome.getValue1() - valeurEnfant) < ecart) {
                return false;
            }
        }
        return true;
    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart
     * en termes de coûts
     *
     * @param population à tester
     * @param ecart      maximum
     * @return true si l'espacement minimum dans la population est supérieur à ecart
     */
    @Override
    public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart) {
        Pair<List<Object>, Double> chromosomePrecedent = null;
        for (Pair<List<Object>, Double> chromosome : population) {
            if (chromosomePrecedent != null
                    && Math.abs(chromosome.getValue1() - chromosomePrecedent.getValue1()) < ecart) {
                return false;
            }
            chromosomePrecedent = chromosome;
        }
        return true;
    }

    /**
     * Renvoie true si certaines contraintes (à définir) dans le chromosome sont
     * satisfaites
     * 
     * @param chromosome Le chromosome à vérifier
     * @return true si les contraintes sont respectées, false sinon
     */
    @Override
    public Boolean verifierChromosome(List<Object> chromosome) {

        ArrayList<Object> collecteRealisee = new ArrayList<Object>();

        for (Object demande : chromosome) {
            if (((Demande) (demande)).getTypeDemande() == TypeDemande.COLLECTE) {
                collecteRealisee.add(demande);
            } else if (((Demande) (demande)).getTypeDemande() == TypeDemande.LIVRAISON) {
                Demande collecte = ((Demande) (demande)).getRequete().getDemandeCollecte();
                if (collecteRealisee.contains(collecte)) {
                    collecteRealisee.remove(collecte);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Algorithme permettant de réaliser la mutation sur un chromosome
     * 
     * @param chromosome  Le chromosome à partir duquel on réalise la mutation
     * @param coutInitial Le coût initial du chromosome
     * @return Le chromosome muté
     */
    @Override
    public List<Object> mutationLocalSearch(List<Object> chromosome, Double coutInitial) {

        Boolean amelioration = true;
        double coutMin = coutInitial;

        while (amelioration == true) {
            amelioration = false;

            // parcours des paires de chromosomes
            for (int i = 1; i < chromosome.size() - 2; i++) {
                for (int j = 1; j < chromosome.size() - 2; j++) {
                    if (i != j && j != i - 1 && j != i + 1) {
                        // recherche 2-opt
                        Demande demande1 = (Demande) chromosome.get(j);
                        Demande demande2 = (Demande) chromosome.get(i + 1);

                        chromosome.set(i + 1, demande1);
                        chromosome.set(j, demande2);

                        if (this.verifierChromosome(chromosome)) {
                            double cout = cout(chromosome);

                            if (cout < coutMin) {
                                // solution avec meilleur coût : on la garde
                                coutMin = cout;
                                amelioration = true;
                                break;
                            }
                        }
                        chromosome.set(i + 1, demande2);
                        chromosome.set(j, demande1);
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
     * Méthode de calcul du coût d'un chromosome
     * 
     * @param chromosome Le chromosome dont on souhaite calculer le coût
     * @return Le coût d'un chromosome
     */
    @Override
    public Double cout(List<Object> chromosome) {
        double somme = 0;
        Long idIntersectionPrecedente = ID_DEPOT;

        for (Object demande : chromosome) {
            Long nouvelId = ((Demande) demande).getIdIntersection();
            somme += CHEMINS_LES_PLUS_COURTS.get(idIntersectionPrecedente + "|" + nouvelId).getPoids();
            idIntersectionPrecedente = nouvelId;
        }
        somme += CHEMINS_LES_PLUS_COURTS.get(idIntersectionPrecedente + "|" + ID_DEPOT).getPoids();

        return somme;
    }
}
