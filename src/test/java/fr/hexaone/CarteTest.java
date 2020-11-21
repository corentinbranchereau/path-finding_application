package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Requete;

import java.util.ArrayList;
import java.util.Collections;

import fr.hexaone.model.Requete;

import java.util.ArrayList;
import java.util.List;

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
     * Test de vérification de contraintes des requêtes  sur une solution de tournée
     */

    @Test
    public void verifierPopTest() {

        List<Long> listIntersections = new ArrayList<Long>();
        for (int i = 0; i < 10; i++) {
            listIntersections.add((long)i);
        }

        List<Requete> requetes = new ArrayList<Requete>();
        List<Requete> requetes2 = new ArrayList<Requete>();
        
        long huit=(long) 8;
        
        long trois=(long) 3;
        
        requetes.add(new Requete(huit,0,trois,0));
        requetes2.add(new Requete(trois,0,huit,0));
        
        assert (carte.verifierPop(listIntersections, requetes) == false);

        assert (carte.verifierPop(listIntersections, requetes2) == true);
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
    
}