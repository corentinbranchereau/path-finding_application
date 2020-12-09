package fr.hexaone.algo;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Trajet;
import fr.hexaone.model.TypeIntersection;
import org.javatuples.Pair;

import java.util.*;

/**
 * Classe définissant les méthodes spécifiques utilisées pour résoudre 
 * le problème du voyageur de commerce avec contraintes de précédences
 *  Utilise le design pattern TEMPLATE.
 * 
 * @author HexaOne
 * @version 1.0
 */
public class AlgoTSP extends AlgoGenetique {
	
	/**
	 * Map permettant d'identifier les chemins les plus courts à partir d'un identifiant (String) 
	 */
    private Map<String, Trajet> cheminsLesPlusCourts;
    
    /**
     * id du dépot
     */
    private Long depotId;

    /**
     * Constructeur de AlgoTSP
     * @param sigma Nb chromosomes max dans une population
     * @param delta Minimum d'ecart entre les valeurs dans la population
     * @param p  probabilité d'améliorer avec du Local Search un enfant
     * @param nMax Nb max d'itérations pour générer une pop initiale
     * @param alphaMax  Nb max de crossovers productifs
     * @param betaMax Nb max de crossosovers sans améliorer la solution
     * @param maxIter Nb max d'iterations d'affilé à ne pas trouver de solution
     * @param depotId id du dépot
     * @param cheminsLesPlusCourts liste des chemins les plus liste des chemins les plus courtscourts
     */
    public AlgoTSP(int sigma, int delta,int p,int nMax,int alphaMax,int betaMax,int maxIter,Long depotId, Map<String, Trajet> cheminsLesPlusCourts) {
    	super(sigma,  delta, p, nMax, alphaMax, betaMax, maxIter);
    	this.depotId=depotId;
    	this.cheminsLesPlusCourts=cheminsLesPlusCourts;
    }
    
    /**
     * Constructeur de AlgoTSP utilisant les paramètres par défaut de l'algo génétique
     * @param depotId depotId id du dépot
     * @param cheminsLesPlusCourts liste des chemins les plus courts
     */
    public AlgoTSP(Long depotId, Map<String, Trajet> cheminsLesPlusCourts) {
    	super();
    	this.depotId=depotId;
    	this.cheminsLesPlusCourts=cheminsLesPlusCourts;
    }

    /**
     * Renvoie une liste de chromosomes aléatoires (donc une liste d'intersections)
     *
     * @param objets
     */
    @Override
	public List<Object> genererChromosomeAleatoire(List<Object> objets){
		 List<Object> shuffleList=new ArrayList<Object>(objets);
		 
		  do { //réarrangement aléatoire de la population
	            Collections.shuffle(shuffleList);
	        } while (!verifierPop(shuffleList)); // Sérieux ? XD

	        return shuffleList;	
	}

    
    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     * 
     * @param P1 parent1
     * @param P2 parent2
     * @param i indice 1
     * @param j indice 2
     * @return le chromosome enfant
     */
    @Override
	public List<Object> crossoverOX(List<Object> P1, List<Object> P2, int i, int j) {

        List<Object> child = new ArrayList<Object>();

        for (int k = 0; k < P1.size(); k++) {
        	 child.add(null);
        }

        int max = max(i, j);
        int min = min(i, j);

        Set<Object> intersectionsVus = new HashSet<Object>();

        for (int k = min; k <= max; k++) {

            child.set(k, P1.get(k));
            intersectionsVus.add(P1.get(k));
        }

        int k = max + 1;
        int p = max + 1;

        int c = 0;

        //suite d'opérations réalisées afin de faire le croisement
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
     * précédence des requêtes Echange la place des couples pickup-delivery quand
     * ils sont inversés
     *
     * @param chromosome
     * @return le nouveau chromosome corrigé
     */
    @Override
	public List<Object> correctionCrossover(List<Object> chromosome){
    	List<Pair<Integer,Integer>> aSwap  = new ArrayList<Pair<Integer,Integer>>();

        Integer posLivraison = 0;
        for (Object demande : chromosome) {
            if (((Demande) demande).getTypeIntersection() == TypeIntersection.LIVRAISON) {
                Integer posCollecte = chromosome.indexOf(((Demande) demande).getRequete().getDemandeCollecte());
                if (posLivraison < posCollecte ) {
                    aSwap.add( new Pair<Integer,Integer>(posCollecte,posLivraison) );
                }
            }
            posLivraison += 1;
        }

        for (Pair<Integer,Integer> pair : aSwap) {
            Collections.swap(chromosome, pair.getValue0(), pair.getValue1());
        }

        return chromosome;
    }
    

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart
     * en termes de couts avec l'ajout de l'enfant
     *
     * @param population   à tester
     * @param ecart        maximum
     * @param valeurEnfant
     * @return true si l'espacement minimum dans la population est supérieur à ecart 
     */
    @Override
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart, Double valeurEnfant) {
    	for (Pair<List<Object>,Double> chrom : population) {
            if (Math.abs(chrom.getValue1() - valeurEnfant) < ecart) {
                return false;
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
     * @return true si l'espacement minimum dans la population est supérieur à ecart 
     */
    @Override
	public Boolean espacePopulation(List<Pair<List<Object>, Double>> population, Double ecart) {
    	 Pair<List<Object>, Double> chromPres = null;
         for (Pair<List<Object>,Double> chrom : population) {
             if (chromPres != null && Math.abs(chrom.getValue1() - chromPres.getValue1()) < ecart) {
                 return false;
             }
             chromPres=chrom;
         }
         return true;	 	
	}
	
	
    /**
     * Renvoie true si toutes les contraintes de précédences sont respectées, faux
     * sinon
     *
     * @param chromosome que l'on teste
     * @return true si toutes les contraintes de précédences sont respectées
     */
    @Override
	public Boolean verifierPop(List<Object> chromosome) {
    	
        ArrayList<Object> collecteRealisee = new ArrayList<Object>();

        for (Object demande : chromosome) {
            if (((Demande)(demande)).getTypeIntersection() == TypeIntersection.COLLECTE) {
                collecteRealisee.add(demande);
            } else if ( ((Demande)(demande)).getTypeIntersection() == TypeIntersection.LIVRAISON) {
                Demande collecte = ((Demande)(demande)).getRequete().getDemandeCollecte();
                if ( collecteRealisee.contains(collecte) ){
                    collecteRealisee.remove(collecte);
                } else {
                    return false;
                }
            } else {
                System.out.println("Erreur dans le type de la demande n°" + ((Demande)(demande)).getIdDemande());
                return false;
            }
        }
        return true;	
	}
    
    /**
     * Mutation d'un chromosme grâce à un algorithme de recherche locale
     *
     * @param chromosome que l'on teste
     * @param coutIni coût initial
     * @return le chromosome muté
     */
    @Override
	public List<Object> mutationLocalSearch(List<Object> chromosome, Double coutIni){
		
    	  Boolean amelioration = true;
          double coutMin = coutIni;

          while (amelioration == true) {
              amelioration = false;

              //parcours des paires de chromosomes
              for (int i = 1; i < chromosome.size() - 2; i++) {
                  for (int j = 1; j < chromosome.size() - 2; j++) {
                      if (i != j && j != i - 1 && j != i + 1) {
                      	//recherche 2-opt
                          //Demande u = chromosome.get(i);
                          Demande v = (Demande)chromosome.get(j);
                          Demande x = (Demande)chromosome.get(i + 1);
                          //Demande y = chromosome.get(j + 1);

                          chromosome.set(i + 1, v);
                          chromosome.set(j, x);

                          if (this.verifierPop(chromosome)) {
                              double cout = cout(chromosome);

                              if (cout < coutMin) {
                              	//solution avec meilleur coût : on la garde
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
                  	//on recommence le processus
                      break;
                  }
              }
          }
          return chromosome;
	}
    
    /**
     * Calcule le cout d'un chromsomome
     *
     * @param chromosome que l'on teste
     * @return le cout d'un chromosome
     */
    @Override
	public Double cout(List<Object> chromosome) {
    	 double somme = 0;
         Long prevIntersectionId = depotId;

         for (Object demande: chromosome) {
             Long newId = ((Demande)demande).getIdIntersection();
             somme += cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId).getPoids();
             prevIntersectionId = newId;
         }
         somme += cheminsLesPlusCourts.get(prevIntersectionId + "|" + depotId).getPoids();

         return somme;
	}
    
    /**
     * Retourne le max entre a et b
     *
     * @param a
     * @param b
     * @return le max entre a et b
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
     * @return le min entre a et b
     */
    private int min(int a, int b) {

        if (a < b) {
            return a;
        }
        return b;
    }
}
