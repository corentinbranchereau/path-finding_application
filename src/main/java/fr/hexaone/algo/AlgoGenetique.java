package fr.hexaone.algo;

import org.javatuples.Pair;

import java.util.*;

/**
 * Classe abstraite définissant le squelette d'un algorithme génétique. Inclut
 * le design pattern Template.
 * 
 * @author HexaOne
 * @version 1.0
 */
public abstract class AlgoGenetique {

	/**
	 * Nombre de chromosomes max dans une population
	 */
	int sigma;

	/**
	 * Ecart minimum entre les valeurs dans la population
	 */
	double delta;

	/**
	 * Probabilité d'améliorer un enfant avec du Local Search
	 */
	double p;

	/**
	 * Nombre max d'itérations pour générer une population initiale
	 */
	int nMax;

	/**
	 * Nombre max de crossovers productifs
	 */
	int alphamax;

	/**
	 * Nombre max de crossosovers sans améliorer la solution
	 */
	int betaMax;

	/**
	 * Nombre max d'itérations d'affilée à ne pas trouver de solution
	 */
	int maxIter;

	/**
	 * Population constituée d'une liste d'objets associée à un coût
	 */
	List<Pair<List<Object>, Double>> population;

	/**
	 * Comparateur afin de classer les chromosomes au sein d'une population dans
	 * l'ordre croissant des coûts
	 */
	protected final Comparator<Pair<List<Object>, Double>> COMPARATEUR_CHROMOSOME = new Comparator<Pair<List<Object>, Double>>() {
		@Override
		public int compare(Pair<List<Object>, Double> e1, Pair<List<Object>, Double> e2) {
			return (int) (e1.getValue1() - e2.getValue1());
		}
	};

	/**
	 * Constructeur abstrait de AlgoGenetique
	 * 
	 * @param sigma    Nombre de chromosomes max dans une population
	 * @param delta    Ecart minimum entre les valeurs dans la population
	 * @param p        Probabilité d'améliorer un enfant avec du Local Search
	 * @param nMax     Nombre max d'itérations pour générer une population initiale
	 * @param alphaMax Nombre max de crossovers productifs
	 * @param betaMax  Nombre max de crossosovers sans améliorer la solution
	 * @param maxIter  Nombre max d'itérations d'affilée à ne pas trouver de
	 *                 solution
	 */
	public AlgoGenetique(int sigma, int delta, int p, int nMax, int alphaMax, int betaMax, int maxIter) {
		this.sigma = sigma;
		this.delta = delta;
		this.p = p;
		this.nMax = nMax;
		this.alphamax = alphaMax;
		this.betaMax = betaMax;
		this.maxIter = maxIter;
	}

	/**
	 * Constructeur par défaut de AlgoGenetique
	 */
	public AlgoGenetique() {
		this.sigma = 30;
		this.delta = 3;
		this.p = 0.2;
		this.nMax = 10000;
		this.alphamax = 2000;
		this.betaMax = 2000;
		this.maxIter = 5000;
	}

	/**
	 * Boucle principale de l'algorithme génétique : à ne jamais modifier
	 * 
	 * @param objets Une liste d'objets représentant un chromosome (une liste non
	 *               ordonnée)
	 * @return La meilleure liste d'objets ordonnée
	 */
	public final List<Object> algoGenetique(List<Object> objets) {

		List<List<Pair<List<Object>, Double>>> listePopulations = new ArrayList<List<Pair<List<Object>, Double>>>();

		double meilleurCout = Integer.MAX_VALUE;

		for (int nbGa = 0; nbGa < 10; nbGa++) {

			// création de la population
			population = new ArrayList<Pair<List<Object>, Double>>();

			// 1er élément de la population obtenu avec du 2-opt
			List<Object> chromosome1 = this.genererChromosomeAleatoire(objets);

			chromosome1 = this.mutationLocalSearch(chromosome1, cout(chromosome1));

			population.add(new Pair<>(chromosome1, cout(chromosome1)));

			// initialisation des variables
			int k = 1;
			int nbEssais = 0;
			int iter = 0;

			Double cout;

			Pair<List<Object>, Double> chromosome;

			Random rand = new Random();

			// génération aléatoire de la population initiale
			while (k <= sigma && nbEssais <= nMax) {
				k++;
				nbEssais = 0;

				do {
					// on essaie de générer un chromosome aléatoire avec un nombre max d'essais
					nbEssais++;
					List<Object> chromosomeAleatoire = genererChromosomeAleatoire(objets);
					cout = cout(chromosomeAleatoire);
					chromosome = new Pair<>(chromosomeAleatoire, cout);
				} while (nbEssais <= nMax && !espacePopulation(population, delta, cout));
				if (nbEssais <= nMax) {
					// la génération a réussi
					population.add(chromosome);
				}
			}
			if (nbEssais > nMax) {
				sigma = k - 1;
			}

			// tri la population par ordre coissant de cout
			Collections.sort(population, COMPARATEUR_CHROMOSOME);

			int i1;
			int i2;
			int indexParent1;
			int indexParent2;

			int alpha = 0;
			int beta = 0;

			// boucle principale
			while (alpha < alphamax && beta < betaMax && iter <= maxIter) {

				indexParent1 = 0;
				indexParent2 = 0;

				// génération aléatoire de 2 indices i et j inférieurs à population.size()
				for (int i = 0; i < 2; i++) {
					i1 = rand.nextInt(population.size());
					i2 = rand.nextInt(population.size());

					if (i == 0) {
						indexParent1 = i2;
						if (population.get(i1).getValue1() < population.get(i2).getValue1()) {
							indexParent1 = i1;
						}
					} else {
						indexParent2 = i2;
						if (population.get(i1).getValue1() < population.get(i2).getValue1()) {
							indexParent2 = i1;
						}
					}
				}

				int choixEnfant = rand.nextInt(2);

				// génération des enfants : un seul survit aléatoirement
				List<Object> enfant;

				if (choixEnfant == 0) {
					enfant = crossoverOX(population.get(indexParent1).getValue0(),
							population.get(indexParent2).getValue0(),
							rand.nextInt(population.get(0).getValue0().size()),
							rand.nextInt(population.get(0).getValue0().size()));
				} else {
					enfant = crossoverOX(population.get(indexParent2).getValue0(),
							population.get(indexParent1).getValue0(),
							rand.nextInt(population.get(0).getValue0().size()),
							rand.nextInt(population.get(0).getValue0().size()));
				}

				// correction des contraintes de précédence sur l'enfant
				enfant = correctionCrossover(enfant);

				// nombre aléatoire compris entre sigma/2 et sigma
				int kRand = rand.nextInt(sigma - sigma / 2) + (sigma / 2);

				double coutEnfant = cout(enfant);

				if (Math.random() < p) {
					// mutation de l'enfant : on l'améliore avec de la recherche locale
					List<Object> mutation = this.mutationLocalSearch(enfant, cout(enfant));

					double ancienCout = population.get(kRand).getValue1();
					population.get(kRand).setAt1(Integer.MIN_VALUE);

					if (this.espacePopulation(population, delta, coutEnfant)) {
						// si les contraintes d'espacement sont vérifiées avec le nouvel enfant
						enfant = mutation;
						coutEnfant = cout(enfant);
					}

					population.get(kRand).setAt1(ancienCout);

				}

				double ancienCout = population.get(kRand).getValue1();
				population.get(kRand).setAt1(Integer.MIN_VALUE);

				if (this.espacePopulation(population, delta, coutEnfant)) {
					// itération productive
					alpha++;
					iter = 0;

					population.remove(kRand);
					population.add(new Pair<>(enfant, coutEnfant));

					if (coutEnfant >= population.get(0).getValue1()) {
						// pas d'amélioration de la meilleure solution
						beta++;

					} else {
						beta = 0;
					}

					Collections.sort(population, COMPARATEUR_CHROMOSOME);
				}

				else {
					// l'enfant n'est pas acceptable dans la population
					population.get(kRand).setAt1(ancienCout);
					iter++;
				}
			}

			listePopulations.add(population);
		}

		List<Object> resultat = new ArrayList<Object>();

		for (int m = 0; m < listePopulations.size(); m++) {
			if (listePopulations.get(m).get(0).getValue1() < meilleurCout) {
				meilleurCout = listePopulations.get(m).get(0).getValue1();
				resultat = listePopulations.get(m).get(0).getValue0();
			}
		}
		return resultat;
	}

	/**
	 * Renvoie un chromosome aléatoire
	 *
	 * @param objets Chromosome servant à générer le nouveau chromosome aléatoire
	 * @return Un chromosome aléatoire
	 */
	public abstract List<Object> genererChromosomeAleatoire(List<Object> objets);

	/**
	 * Réalise le crossover de chromosomes afin d'obtenir un enfant
	 *
	 * @param P1 Premier parent
	 * @param P2 Deuxième parent
	 * @param i  Indice 1
	 * @param j  Indice 2
	 * @return Le chromosome enfant
	 */
	public abstract List<Object> crossoverOX(List<Object> P1, List<Object> P2, int i, int j);

	/**
	 * Permet de corriger éventuellement un chromosome qui ne satisfait pas des
	 * contraintes
	 * 
	 * @param chromosome Chromosome à corriger
	 * @return La correction du chromosome
	 */
	public List<Object> correctionCrossover(List<Object> chromosome) {
		return chromosome;
	};

	/**
	 * Renvoie true si c'est possible d'ajouter l'enfant en gardant l'espacement de
	 * la population, false sinon
	 * 
	 * @param population   La population dans laquelle on souhaite vérifier
	 *                     l'espacement
	 * @param ecart        Représente l'espacement minimum
	 * @param valeurEnfant Le coût de l'enfant
	 * @return true si c'est possible d'ajouter l'enfant en gardant l'espacement de
	 *         la population, false sinon
	 */
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart, Double valeurEnfant) {
		return true;
	};

	/**
	 * Renvoie true si la population est assez "espacée", c'est à dire avec une
	 * valeur minimum d'écart entre les couts
	 * 
	 * @param population La population dans laquelle on souhaite vérifier
	 *                   l'espacement
	 * @param ecart      Représente l'espacement minimum
	 * @return true si la population est assez "espacée", false sinon
	 */
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart) {
		return true;
	};

	/**
	 * Renvoie true si certaines contraintes (à définir) dans le chromosome sont
	 * satisfaites
	 * 
	 * @param chromosome Le chromosome à vérifier
	 * @return true si les contraintes sont respectées, false sinon
	 */
	public Boolean verifierChromosome(List<Object> chromosome) {
		return true;
	}

	/**
	 * Algorithme permettant de réaliser la mutation sur un chromosome
	 * 
	 * @param chromosome  Le chromosome à partir duquel on réalise la mutation
	 * @param coutInitial Le coût initial du chromosome
	 * @return Le chromosome muté
	 */
	public List<Object> mutationLocalSearch(List<Object> chromosome, Double coutInitial) {
		return chromosome;
	}

	/**
	 * Méthode de calcul du coût d'un chromosome
	 * 
	 * @param chromosome Le chromosome dont on souhaite calculer le coût
	 * @return Le coût d'un chromosome
	 */
	public abstract Double cout(List<Object> chromosome);

}
