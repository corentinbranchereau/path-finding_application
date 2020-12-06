package fr.hexaone.model;

import org.javatuples.Pair;

import java.util.*;


/**
 * Classe abstraite définissant le squelette dd'un algorithme génétique
 *  Inclue le design pattern TEMPLATE.
 * 
 * @author HexaOne
 * @version 1.0
 */
public abstract class AlgoGenetique {
	
	  int sigma ; // Nb chromosomes max dans une population
      double delta ;// Minimum d'ecart entre les valeurs dans la population
      double p ; // probabilité d'améliorer avec du Local Search un enfant
      int nMax ;// Nb max d'itérations pour générer une pop initiale
      int alphamax ;// Nb max de crossovers productifsList<Pair<List<Object>, Object>>
      int BetaMax ;// Nb max de crossosovers sans améliorer la solution
      int maxIter ;//Nb max d'iterations d'affilé à ne pas trouver de solution
      
      /**
       * population constituée d'une liste d'objets associée à un coût
       */
      List<Pair<List<Object>, Double>> population;
      
      /**
       * Comparateur afin de classer les chromosomes au sein d'une population dans
       * l'ordre croissant des coûts
       *
       * @param depot
       * @param requetes
       */
      protected final Comparator<Pair<List<Object>, Double>> ComparatorChromosome = new Comparator<Pair<List<Object>, Double>>() {

          @Override
          public int compare(Pair<List<Object>, Double> e1, Pair<List<Object>, Double> e2) {
              return (int) (e1.getValue1() - e2.getValue1());
          }
      };
      
      /**
       * Constructeur abstrait de AlgoGentique
       * @param sigma Nb chromosomes max dans une population
       * @param delta Minimum d'ecart entre les valeurs dans la population
       * @param p  probabilité d'améliorer avec du Local Search un enfant
       * @param nMax Nb max d'itérations pour générer une pop initiale
       * @param alphaMax  Nb max de crossovers productifs
       * @param betaMax Nb max de crossosovers sans améliorer la solution
       * @param maxIter Nb max d'iterations d'affilé à ne pas trouver de solution
       */
    public AlgoGenetique(int sigma, int delta,int p,int nMax,int alphaMax,int betaMax,int maxIter) {
    	
    	this.sigma=sigma;
    	this.delta=delta;
    	this.p=p;
    	this.nMax=nMax;
    	this.alphamax=alphaMax;
    	this.BetaMax=betaMax;
    	this.maxIter=maxIter;
    }
    
    
    /**
     * Constructeur par défaut de AlgoGenetique
     */
    public AlgoGenetique() {
    	this.sigma=30;
    	this.delta=3;
    	this.p=0.2;
    	this.nMax=10000;
    	this.alphamax=2000;
    	this.BetaMax=2000;
    	this.maxIter=5000;
    }
    
    
    /**
     * Boucle principale de l'algo génétique : à ne jamais modifier
     * @param objets
     * @return
     */
	public final List<Object> algoGenetique(List<Object> objets) {
		
		List<List<Pair<List<Object>, Double>>> listePopulations= new ArrayList<List<Pair<List<Object>, Double>>>();
		
		double bestCost=Integer.MAX_VALUE;
		
		for(int nbGa=0;nbGa<10;nbGa++)
		{
			
	        //création de la population
			population=new ArrayList<Pair<List<Object>, Double>>();
			
	        //1er élément de la population obtenu avec du 2-opt
			List<Object> chr1 =this.genererChromosomeAleatoire(objets);
			
	        chr1 = this.mutationLocalSearch(chr1, cout(chr1));
	        
	        population.add(new Pair<>(chr1, cout(chr1)));
	
	        //initialisation des variables
	        int k = 1;
	        int nbEssais = 0;
	        int iter = 0;
	
	        Double cost;
	        
	        Pair<List<Object>, Double> chrom;
	
	        Random rand = new Random();
	
	        //génération aléatoire de la population initiale
	        while (k <= sigma && nbEssais <= nMax) {
	            k++;
	            nbEssais = 0;
	
	            do {
	            	//on essaie de générer un chromosome aléatoire avec un nombre max d'essais
	                nbEssais++;
	                List<Object> chr = genererChromosomeAleatoire(objets);
	                cost = cout(chr);
	                chrom = new Pair<>(chr, cost);
	            } while (nbEssais <= nMax && !espacePopulation(population, delta, cost));
	            if (nbEssais <= nMax) {
	            	//la génération a réussi
	                population.add(chrom);
	            }
	        }
	        if (nbEssais > nMax) {
	            sigma = k - 1;
	        }
	
	        //tri par ordre coissant de cout la population
	        Collections.sort(population, ComparatorChromosome);
	
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
	
	            //génération aléatoire de 2 indices i et j < poulation.size()
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
	
	            //génération des enfants : un seul survit aléatoirement
	            List<Object> child;
	
	            if (choiceChild == 0) {
	                child = crossoverOX(population.get(indexP1).getValue0(), population.get(indexP2).getValue0(),
	                        rand.nextInt(population.get(0).getValue0().size()),
	                        rand.nextInt(population.get(0).getValue0().size()));
	            } else {
	                child = crossoverOX(population.get(indexP2).getValue0(), population.get(indexP1).getValue0(),
	                        rand.nextInt(population.get(0).getValue0().size()),
	                        rand.nextInt(population.get(0).getValue0().size()));
	            }
	
	            //correction des contraintes de précédence sur l'enfant
	            child = correctionCrossover(child);
	
	            //nb aléatoire compris entre sigma/2 et sigma
	            int kRand = rand.nextInt(sigma - sigma / 2) + (sigma / 2);
	
	            double costChild = cout(child);
	
	            if (Math.random() < p) {
	                // mutation de l'enfant : on l'améliore avec de la recherche locale
	
	                List<Object> M = this.mutationLocalSearch(child, cout(child));
	
	                double ancienCout = population.get(kRand).getValue1();
	                population.get(kRand).setAt1(Integer.MIN_VALUE);
	
	                if (this.espacePopulation(population, delta, costChild)) {
	                	//si les contraintes d'espacement sont vérifiés avec le nouvel enfant
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
	                	//pas d'amélioration de la meilleure solution
	                    beta++;
	
	                } else {
	                    beta = 0;
	                }
	
	                Collections.sort(population, ComparatorChromosome);
	            }
	
	            else {
	            	//l'enfant n'est pas acceptable dans la population
	                population.get(kRand).setAt1(ancienCout);
	                iter++;
	            }
	        }
	        	
	        	listePopulations.add(population);
		}
		
		List<Object> resultat=new ArrayList<Object>();
		
		for(int m=0;m<listePopulations.size();m++) {
			if(listePopulations.get(m).get(0).getValue1()<bestCost) {
				bestCost=listePopulations.get(m).get(0).getValue1();
				resultat=listePopulations.get(m).get(0).getValue0();
			}
		}
        return resultat;
	}
	

    /**
     * Renvoie une liste de chromosomes aléatoires
     *
     * @param objets
     */
	public abstract List<Object> genererChromosomeAleatoire(List<Object> objets);
	
	
    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     *
     * @param P1 parent1
     * @param P2 parent2
     * @param i indice 1
     * @param j indice 2
     */
	public abstract List<Object> crossoverOX(List<Object> P1, List<Object> P2, int i, int j);
	
	
	/**
	 * Permet de corriger éventuellement un chromosome qui ne satisfait pas des contraintes
	 * @param chromosome
	 * @return
	 */
	public List<Object> correctionCrossover(List<Object> chromosome){
		return chromosome;
	};
	
	
	/**
	 * Renvoie true si c'est possible d'ajouter l'enfant en gardant l'espacement de la population
	 * @param population
	 * @param ecart
	 * @param valeurEnfant
	 * @return
	 */
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart, Double valeurEnfant) {
		return true;
	};
	
	
	/**
	 *Renvoie true si la population est assez "espacée", c'est à dire avec une 
	 * valeur minimum d'écart entre les couts
	 * @param population
	 * @param ecart
	 * @return
	 */
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart) {
		return true;
	};
	
	
	/**
	 * Renvoie true si certaines contraintes (à définir) dans la population sont satisfaites
	 * @param chromosome
	 * @return
	 */
	public Boolean verifierPop(List<Object> chromosome) {
		return true;
	}
	
	
	/**
	 * Algorithme permettant de réaliser la mutation sur un chromosome 
	 * @param chromosome
	 * @param coutIni
	 * @return
	 */
	public List<Object> mutationLocalSearch(List<Object> chromosome, Double coutIni){
		return chromosome;
	}
	
	
	/**
	 * Méthode de calcul de cout d'un chromosome
	 * @param chromosome
	 * @return
	 */
	public abstract Double cout(List<Object> chromosome);	
	
}
