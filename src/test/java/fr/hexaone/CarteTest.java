package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.Trajet;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
     * Test simple de la boucle principale de l'algorithme génétique
     */

    @Test
    public void GaTestSimple() {

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
    
    
    /**
     * Tes de la boucle principale de l'algorithme génétique
     */

    @Test
    public void GaTest() {

        // Création d'un grap simple
        createGraph();

        // Création des requetes
    	List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(3,5,5,5));
        requetes.add(new Requete(6,5,8,5));
        requetes.add(new Requete(9,5,4,5));
        
        //Calcul des chemins les plus courts
        carte.calculerLesCheminsLesPlusCourts(requetes);
        
        //recherche de la meilleur solution
        List<Long> bestSolution=carte.trouverMeilleureTournee(requetes);

        assert(carte.cout(bestSolution)<=81.0);
        
    }

    @Test
    public void calculerTourneeSimpleTest() {
        
        createSimpleGraph();

        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,3,3,5));
        
        Planning planning = new Planning();

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);

        planning = carte.calculerTournee(planning);

        Map<Intersection,Date> datesPassages = planning.getDatesPassage();
        Map<Intersection,Date> datesSorties = planning.getDatesSorties();

        Map<Long,Intersection> intersections = carte.getIntersections();
        Long d1;
        Long d2;

        d1 = datesPassages.get(intersections.get(0L)).getTime();
        d2 = datesSorties.get(intersections.get(0L)).getTime();
        assert(d1 == 1*15000000/3600 + 3 + 2*15000000/3600 + 5 + 2*15000000/3600);
        assert(d2 == 0);

        d1 = datesPassages.get(intersections.get(1L)).getTime();
        d2 = datesSorties.get(intersections.get(1L)).getTime();
        assert(d1 == 1*15000000/3600);
        assert(d2 == 1*15000000/3600 + 3);

        d1 = datesPassages.get(intersections.get(3L)).getTime();
        d2 = datesSorties.get(intersections.get(3L)).getTime();
        assert(d1 == 1*15000000/3600 + 3 + 2*15000000/3600);
        assert(d2 == 1*15000000/3600 + 3 + 2*15000000/3600 + 5);

    }
    
    @Test
    public void calculerTourneeTest() {
    	 // Création d'un grap simple
        createGraph();

        // Création des requetes
    	List<Requete> requetes = new ArrayList<Requete>();
     
    	requetes.add(new Requete(3,5,5,5));
        requetes.add(new Requete(6,5,8,5));
        requetes.add(new Requete(7,5,4,5));
        requetes.add(new Requete(2,5,9,5));
        
        Planning planning = new Planning();
        
        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);

        planning = carte.calculerTournee(planning);

        Map<Intersection,Date> datesPassages = planning.getDatesPassage();
        Map<Intersection,Date> datesSorties = planning.getDatesSorties();

        Map<Long,Intersection> intersections = carte.getIntersections();

        Long d1;
        Long d2;

        d1 = datesPassages.get(intersections.get(0L)).getTime();
        d2 = datesSorties.get(intersections.get(0L)).getTime();
        assert(d2 == 0);

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
    
    private void createGraph() {
        
        carte.setDepotId((long)0);

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

        
    }
    
}