package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class CarteTest {
	
	Carte carte;
	

    /**
     * Instancie une carte vide
     */
    @BeforeEach
    void init() {
        carte = new Carte();
    }
    

    /**
     * Test de vérification de contraintes des requêtes sur une solution de tournée
     */

    @Test
    public void verifierPopTest() {

        List<Long> listIntersections = new ArrayList<Long>();
        List<Long> listIntersections2 = new ArrayList<Long>();
        for (int i = 0; i < 10; i++) {
            listIntersections.add((long)i);
        }
        
        listIntersections2.add((long)1);
        listIntersections2.add((long)7);
        listIntersections2.add((long)9);
        listIntersections2.add((long)3);
        listIntersections2.add((long)4);
        listIntersections2.add((long)5);


        List<Requete> requetes = new ArrayList<Requete>();
        List<Requete> requetes2 = new ArrayList<Requete>();
        List<Requete> requetes3 = new ArrayList<Requete>();
        
        requetes3.add(new Requete((long)1,0,(long)3,0));
	    requetes3.add(new Requete((long)4,0,(long)9,0));
	    requetes3.add(new Requete((long)5,0,(long)7,0));
        requetes.add(new Requete((long) 8,0,(long) 3,0));
        requetes2.add(new Requete((long) 3,0,(long) 8,0));
        
        assert (carte.verifierPop(listIntersections, requetes) == false);

        assert (carte.verifierPop(listIntersections, requetes2) == true);
        
        assert (carte.verifierPop(listIntersections2, requetes3) == false);
        
        assert(carte.verifierPop(carte.correctionCrossover(listIntersections2, requetes3),requetes3)==true);
        
        
    }

    /**
     * Test de vérification de l'espacement minimum des coûts pour une population
     */

    @Test
    public void espacePopulationTest() {

    	List<Long> listIntersections1 = new ArrayList<Long>();
        List<Long> listIntersections2 = new ArrayList<Long>();
        List<Long> listIntersections3 = new ArrayList<Long>();

        for (int i = 0; i < 10; i++) {
            listIntersections1.add((long)i);
            listIntersections2.add((long)i*i);
            listIntersections3.add((long)i*i*i);
        }

        List<Pair<List<Long>, Double>> pop = new ArrayList<Pair<List<Long>, Double>>();

        pop.add(new Pair<>(listIntersections1, 1.0));

        pop.add(new Pair<>(listIntersections2, 3.0));

        pop.add(new Pair<>(listIntersections3, 8.0));

        assert (carte.espacePopulation(pop, 10) == false);

        assert (carte.espacePopulation(pop, 1) == true);

    }
    
    /**
     * Test de vérification de la génération d'un chromosome enfant
     */

    @Test
    public void crossoverOXTest() {

    	List<Long> P1 = new ArrayList<Long>();
    	List<Long> P2 = new ArrayList<Long>();
    	
    	P1.add((long)1);
    	P1.add((long)3);
    	P1.add((long)2);
    	P1.add((long)6);
    	P1.add((long)4);
    	P1.add((long)5);
    	P1.add((long)9);
    	P1.add((long)7);
    	P1.add((long)8);
    	
    	P2.add((long)3);
    	P2.add((long)7);
    	P2.add((long)8);
    	P2.add((long)1);
    	P2.add((long)4);
    	P2.add((long)9);
    	P2.add((long)2);
    	P2.add((long)5);
    	P2.add((long)6);
    	
    	
        List<Long> enfant=carte.crossoverOX(P1,P2,3,5); 
     
        int[]res= {8,1,9,6,4,5,2,3,7};
        
        for(int i=0;i<enfant.size();i++) {
        	assert((long)res[i]==enfant.get(i));
        }

    }
    
    /**
     * Test de vérification de la génération d'un chromosome aléatoire
     */

    @Test
    public void genererChromosomeTest() {

    	List<Requete> requetes = new ArrayList<Requete>();

	    requetes.add(new Requete((long)1,0,(long)3,0));
	    requetes.add(new Requete((long)4,0,(long)9,0));
	    requetes.add(new Requete((long)5,0,(long)7,0));
    	
        List<Long> chromosome=carte.genererChromosomeAleatoire(requetes) ;
        
        assert (carte.verifierPop(chromosome, requetes) == true);
        
    }
    
    /**
     * Test de la boucle principale de l'algorithme génétique
     */

    @Test
    public void GaTest() {

        // Création d'un grap simple
        createSimpleGraph();

        // Création des requetes
    	List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,3,5));
        
        //Calcul des chemins les plus courts
        carte.calculerLesCheminsLesPlusCourts(requetes);

        //recherche de la meilleur solution
        List<Long> bestSolution=carte.trouverMeilleureTournee(requetes);
        
        assert(bestSolution.size()==2);
        
        assert(bestSolution.get(0)==1);
        assert(bestSolution.get(1)==3);
        
        
    }

    @Test
    public void coutTest() {

        //Création d'un graphe simple
        createSimpleGraph();
        
        //Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,3,5));
         
        //Calcul des chemins les plus courts
        carte.calculerLesCheminsLesPlusCourts(requetes);

        List<Long> chromosome = new ArrayList<Long>();
    	chromosome.add((long)1);
        chromosome.add((long)3);
        
        Double cout = carte.cout(chromosome);

        assert(cout == 5);
    }
    
    /**
     * Test de la mutation avec recherche locale
     */

    @Test
    public void MutationLocalSearchTest() {
    	 
    	//Création d'un graphe
        createSimpleGraph();

        //Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,3,5));
        
        //Calcul des chemins les plus courts
        carte.calculerLesCheminsLesPlusCourts(requetes);

	    List<Long> P1 = new ArrayList<Long>();

    	P1.add((long)1);
    	P1.add((long)3);

	    List<Long> mutation=carte.mutationLocalSearch(P1, carte.cout(P1), requetes);

	    
	    assert(carte.cout(mutation)<=carte.cout(P1));
        
    }

    @Test
    public void rechercheDesPlusCourtsChemins(){

        //Création d'un graphe
        createSimpleGraph();

        //Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,3,5));
        
        //Calcul des chemins les plus courts
        carte.calculerLesCheminsLesPlusCourts(requetes);

        // Vérification des résultats
        assert(carte.getCheminsLesPlusCourts().size() == 9);

        carte.getCheminsLesPlusCourts().forEach( (key,value) -> {
            switch (key) {
	            case "0|0":
	                assert(value.getPoids() == 0.);
	                break;
	            case "1|1":
	                assert(value.getPoids() == 0.);
	                break;
	                
	            case "3|3":
	                assert(value.getPoids() == 0.);
	                break;
	                
                case "0|1":
                    assert(value.getPoids() == 1.);
                    break;
                case "0|3":
                    assert(value.getPoids() == 3.);
                    break;
                case "1|0":
                    assert(value.getPoids() == 1.);
                    break;
                case "1|3":
                    assert(value.getPoids() == 2.);
                    break;
                case "3|0":
                    assert(value.getPoids() == 2.);
                    break;
                case "3|1":
                    assert(value.getPoids() == 2.);
                    break;
                default:
                    break;
            }
        });
    }

    private void createSimpleGraph() {
        
        carte.setDepotId((long)0);

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

        
    }
    
}