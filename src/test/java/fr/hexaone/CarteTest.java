package fr.hexaone;

import fr.hexaone.model.Carte;

import fr.hexaone.model.Intersection;
import fr.hexaone.model.IntersectionSpeciale;
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
        carte = new Carte(null);

    }
    

    /**
     * Test de vérification de contraintes des requêtes  sur une solution de tournée
     */

    @Test
    public void verifierPopTest() {

        List<Intersection> listIntersections = new ArrayList<Intersection>();
        for (int i = 0; i < 10; i++) {
            listIntersections.add(new Intersection(0, 0, i, null, null));
        }

        List<Requete> requetes = new ArrayList<Requete>();

        requetes.add(new Requete(new IntersectionSpeciale(0, 0, 8, null, null, 0, null),
                new IntersectionSpeciale(0, 0, 3, null, null, 0, null)));

        List<Requete> requetes2 = new ArrayList<Requete>();

        requetes.add(new Requete(new IntersectionSpeciale(0, 0, 3, null, null, 0, null),
                new IntersectionSpeciale(0, 0, 4, null, null, 0, null)));

        assert (carte.verifierPop(listIntersections, requetes) == false);

        assert (carte.verifierPop(listIntersections, requetes2) == true);

    }
    
    /**
     * Test de vérification de l'espacement minimum des coûts pour une population
     */

    
    @Test
    public void espacePopulationTest() {

        List<Intersection> listIntersections1 = new ArrayList<Intersection>();
        List<Intersection> listIntersections2 = new ArrayList<Intersection>();
        List<Intersection> listIntersections3 = new ArrayList<Intersection>();

        for (int i = 0; i < 10; i++) {
            listIntersections1.add(new Intersection(0, 0, i, null, null));
            listIntersections2.add(new Intersection(0, 0, 10 - i, null, null));
            listIntersections3.add(new Intersection(0, 0, i * i, null, null));
        }

        List<Pair<List<Intersection>, Double>> pop = new ArrayList<Pair<List<Intersection>, Double>>();

        pop.add(new Pair<>(listIntersections1, 1.0));

        pop.add(new Pair<>(listIntersections2, 3.0));

        pop.add(new Pair<>(listIntersections3, 8.0));

        assert (carte.espacePopulation(pop, 10) == false);

        assert (carte.espacePopulation(pop, 1) == true);

    }


}