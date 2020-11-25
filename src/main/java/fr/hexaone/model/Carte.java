
package fr.hexaone.model;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;
import org.javatuples.Pair;


/**
 * Objet contenant les structures de données relatives à la carte"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Carte {

    protected List<Trajet> cheminsLesPlusCourts;
    protected Map<Long, Intersection> intersections;

    /**
     * Constructeur par défaut de Carte
     */
    public Carte() {
        intersections = new HashMap<>();
    }

    /**
     * Calculer une tournée et l'inscrit dans le planning
     *
     * @param planning
     * @return
     */
    public Planning calculerTournee(Planning planning) {
        // TODO
        return planning;

    }

    private void calculerLesCheminsLesPlusCourts() {
        // TODO

    }

    private void trouverMeilleureTournee() {
        // TODO

    }

    /**
     * Comparateur afin de classer les chromosomes au sein d'une population
     * dans l'ordre croissant des coûts
     * @param depot 
     * @param requetes
     */
    public static Comparator<Pair<List<Long>, Double>> ComparatorChromosome = new Comparator<Pair<List<Long>, Double>>() {

        @Override
        public int compare(Pair<List<Long>, Double> e1, Pair<List<Long>, Double> e2) {
            return (int) (e1.getValue1() - e2.getValue1());
        }
    };

    /**
     * Algorithme génétique permettant de trouver le melleure tournée
     * @param depot 
     * @param requetes
     */
    public List<Long> trouverMeilleureTournee(long idDepot , List<Requete> requetes) {
    	//TO DO

        /*int sigma = 10; // Nb chromosomes
        double delta = 1;// Minimum d'ecart entre les valeurs
        double p = 0.1; // probabilité d'améliorer avec du Local Search un enfant
        int nMax = 10000;// Nb max d'itérations pour générer une pop initiale
        int alphamax = 30000;// Nb max de crossovers productifs
        int BetaMax = 10000;// Nb max de crossosovers sans améliorer la solution
        */
    	
    	int sigma = 6; // Nb chromosomes
        double delta = 1;// Minimum d'ecart entre les valeurs
        double p = 0.1; // probabilité d'améliorer avec du Local Search un enfant
        int nMax = 10000;// Nb max d'itérations pour générer une pop initiale
        int alphamax = 3000;// Nb max de crossovers productifs
        int BetaMax = 1000;// Nb max de crossosovers sans améliorer la solution

        List<Pair<List<Long>, Double>> population = new ArrayList<Pair<List<Long>, Double>>();

        int k = 0;
        int nbEssais = 0;
        Random rand = new Random();

        while (k <= sigma && nbEssais <= nMax) {
        
            k++;
            nbEssais = 0;
            Pair<List<Long>, Double> chrom;
            
            do {
            	 nbEssais++;
            	 chrom=  new Pair<>(genererChromosomeAleatoire(idDepot, requetes), (double)(rand.nextInt(100)+10));
            }
            while (nbEssais <= nMax && !espacePopulation(population, delta)) ;
            if(nbEssais<=nMax) {
            	population.add(chrom);
            }    
        }
        if (nbEssais > nMax) {
            sigma = k - 1;
        }

        Collections.sort(population, ComparatorChromosome);

        int alpha = 0;
        int beta = 0;

        // main loop GA
        while (alpha < alphamax && beta < BetaMax) {
          
            int indexP1 = 0;
            int indexP2 = 0;

            int i1;
            int i2;

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

            List<Long> C1 = crossoverOX(population.get(indexP1).getValue0(),
                    population.get(indexP2).getValue0(), rand.nextInt(population.get(0).getValue0().size()),
                    rand.nextInt(population.get(0).getValue0().size()));
           
            List<Long> C2 = crossoverOX(population.get(indexP2).getValue0(),
                    population.get(indexP1).getValue0(), rand.nextInt(population.get(0).getValue0().size()),
                    rand.nextInt(population.get(0).getValue0().size()));
          
            int choiceChild = rand.nextInt(2);

            List<Long> child = C2;

            if (choiceChild == 0) {
                child = C1;
            }
            
            child=correctionCrossover(child,requetes);

            int kRand = rand.nextInt(sigma - sigma / 2) + (sigma / 2);

           /* if (Math.random() < p) {
                // mutation with LS to improve C
                continue;
            }
            */

            double costChild = (double)(rand.nextInt(100)+10); // to compute
            
            List<Pair<List<Long>, Double>> copiePopulation=new ArrayList<Pair<List<Long>, Double>>();
            
            for(int i=0;i<population.size();i++) {
            	
            	copiePopulation.add(population.get(i));
            }
            
            population.remove(kRand);

            if (this.espacePopulation(population, delta, costChild)) {
                // productive iteration
                population.add(new Pair<>(child, costChild));
                alpha++;

                if (costChild < population.get(0).getValue1()) {
                    beta++;
                }

                Collections.sort(population, ComparatorChromosome);
            }

            else {
                population = copiePopulation;
            }
        }
        
        return population.get(0).getValue0();
    }
    
    
    /**
     * Calcule le cout d'un chromsomome (une solution à la tournée)
     * @param chromosome que l'on teste
     */
    public double cout(List<Long> chromosome) {
    	
    	double somme=0;
    	
    	for(int i=0;i<chromosome.size();i++) {
    		somme+=chromosome.get(i)*i*i*i;
    	}
    	
    	return somme;
    }
    
    /**
     * Mutation d'un chromosme grâce à un algorithme de recherche locale
     * @param chromosome que l'on teste
     * @param requetes liste des requêtes
     */

    public List<Long> mutationLocalSearch(List<Long> chromosome, double coutIni, List<Requete> requetes) {
    	
    	Boolean amelioration=true;
    	double coutMin=coutIni;
    	
    	while(amelioration==true)
    	{
    		amelioration=false;
    		
    		for(int i=1;i<chromosome.size()-2;i++) {
    			for(int j=1;j<chromosome.size()-2;j++) {
    				if(i!=j) {
    					
    					long u=chromosome.get(i);
    					long v=chromosome.get(j);
    					long x=chromosome.get(i+1);
    					long y=chromosome.get(j+1);
    					
    					chromosome.set(i+1,v);
    					chromosome.set(j,x);
    					
    					if(this.verifierPop(chromosome, requetes))
    					{
    						double cout=cout(chromosome);
    						
        					if(cout<coutMin) {
        						coutMin=cout;
        						amelioration=true;
        						break;
        					}
    					}
    					
    					chromosome.set(i+1,x);
    					chromosome.set(j,v);	
    				}
    			}
    			if(amelioration==true) {
    				break;
    			}
    		}
    	}
    	return chromosome;
    }
    
    
    
    /**
     * Renvoie true si toutes les contraintes de précédences sont respectées, faux sinon
     * @param chromosome que l'on teste
     * @param requetes liste des requêtes
     */

    public Boolean verifierPop(List<Long> chromosome, List<Requete> requetes) {

        for (int i = 0; i < requetes.size(); i++) {
            Boolean livraison = false;
            Boolean collecte = false;
            for (int j = 0; j < chromosome.size(); j++) {
            	
            	if(livraison==true && collecte==true) {
            		break;
            	}
                if (chromosome.get(j) == requetes.get(i).getIdPickup()) {
                    collecte = true;
                    if (livraison == false) {
                        continue;
                    } else {
                        return false;
                    }
                }

                if (chromosome.get(j) == requetes.get(i).getIdDelivery()) {
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
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart en termes de couts
     * @param population à tester
     * @param ecart maximum
     */

    public Boolean espacePopulation(List<Pair<List<Long>, Double>> population, double ecart) {

        for (int i = 1; i < population.size(); i++) {

            if (population.get(i).getValue1() - population.get(i - 1).getValue1() < ecart) {
                return false;
            }

        }
        return true;

    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart en termes de couts avec l'ajout de l'enfant
     * @param population à tester
     * @param ecart maximum
     * @param valeurEnfant
     */

    public Boolean espacePopulation(List<Pair<List<Long>, Double>> population, double ecart, double valeurEnfant) {

        for (int i = 0; i < population.size(); i++) {

            if (Math.abs(population.get(i).getValue1() - valeurEnfant) < ecart) {
                return false;
            }

        }
        return true;

    }
    
    
    /**
     * Renvoie une liste de chromosomes aléatoires (donc une liste d'intersections) 
     * @param depot 
     * @param requetes 
     */

    public List<Long> genererChromosomeAleatoire(Long idDepot, List<Requete> requetes) {
        List<Long> shuffleList;

        do {
            shuffleList = new ArrayList<Long>();

            for (int i = 0; i < requetes.size(); i++) {
                shuffleList.add(requetes.get(i).getIdPickup());
                shuffleList.add(requetes.get(i).getIdDelivery());
            }

            Collections.shuffle(shuffleList);
        } while (!verifierPop(shuffleList, requetes));

        shuffleList.add(0, idDepot);
        shuffleList.add(idDepot);

        return shuffleList;

    }

    
    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     * @param depot 
     * @param requetes 
     */
    public List<Long> crossoverOX(List<Long> P1, List<Long> P2, int i, int j) {

        List<Long> child = new ArrayList<Long>();
        
        for(int k=0;k<P1.size();k++) {
        	child.add((long)0);
        }

        int max = max(i, j);
        int min = min(i, j);

        List<Long> intersectionsVus = new ArrayList<Long>();

        for (int k = min; k <= max; k++) {
        	
            child.set(k, P1.get(k));
            intersectionsVus.add(P1.get(k));
        }

        int k = max + 1;
        int p = max + 1;

        // use some set or map to improve (unique values)

        int c = 0;
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
	 * Permet la correction d'un chromosome en intégrant les conditions de précédence des requêtes
	 * Echange la place des couples <pickup,delivery> quand ils sont inversés
	 * @param requetes 
	 * @param chromosome
	 */
    public List<Long> correctionCrossover(List<Long> chromosome, List<Requete> requetes) {
    	
        for (int i = 0; i < requetes.size(); i++) {
        	
        	int indiceCollecte=chromosome.indexOf(requetes.get(i).getIdPickup());
        	int indiceLivraison=chromosome.indexOf(requetes.get(i).getIdDelivery());
        	
        	if(indiceLivraison<indiceCollecte) {
        		
        		long id=chromosome.get(indiceCollecte);
        		chromosome.set(indiceCollecte,requetes.get(i).getIdDelivery());
            	chromosome.set(indiceLivraison,id);
        	}
        }
        
        return chromosome;   	

    }

	/**
	 * Retourne le max entre a et b
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
    public List<Trajet> getCheminsLesPlusCourts() {
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
}
