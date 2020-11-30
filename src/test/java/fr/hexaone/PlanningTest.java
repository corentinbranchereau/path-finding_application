package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class PlanningTest {
	
    Carte carte;
    Planning planning;
	

    /**
     * Instancie une carte vide
     */
    @BeforeEach
    void init() {
        carte = new Carte();
        planning = new Planning(carte);
    }
    
    /**
     * Méthode de test pour vérifer les trajets créés pour un
     * un graphe simple
     */
    @Test
    public void test_RechercheDesPlusCourtsTrajets_simple(){

        //Création d'un graphe
        createSimpleGraph();

        //Création des intersections des plus courts chemins        
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));

        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // Vérification des résultats
        Integer nbTrajet = planning.getTrajetsLesPlusCourts().size();
        assert( nbTrajet == 9);

        planning.getTrajetsLesPlusCourts().forEach( (key,value) -> {
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
    
    /**
     * Méthode de test pour vérifer les trajets créés pour un
     * un graphe avec une intersection inaccessible
     */
    @Test
    public void test_RechercheDesPlusCourtsChemins_inaccessible(){

        //Création d'un graphe
        createWeirdGraph();

        //Création des requetes
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(9L)); //9 est le point inaccessible


        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // Vérification des résultats
        assert(planning.getTrajetsLesPlusCourts().size() == 9);

        planning.getTrajetsLesPlusCourts().forEach( (key,value) -> {
            switch (key) {
	            case "0|0":
	                assert(value.getPoids() == 0.);
	                break;
	            case "1|1":
	                assert(value.getPoids() == 0.);
	                break;
	            case "9|9":
	                assert(value.getPoids() == 0.);
	                break;
                case "0|1":
                    assert(value.getPoids() == 4.);
                    break;
                case "0|9":
                    assert(value.getPoids() == Double.MAX_VALUE);
                    break;
                case "1|0":
                    assert(value.getPoids() == 4.);
                    break;
                case "1|9":
                    assert(value.getPoids() == Double.MAX_VALUE);
                    break;
                case "9|0":
                    assert(value.getPoids() == Double.MAX_VALUE);
                    break;
                case "9|1":
                    assert(value.getPoids() == Double.MAX_VALUE);
                    break;
                default:
                    assert(false);
                    break;
            }
        });
    }

    /**
     * Méthode de test de la recherche de plus courts chemins contenant
     * des mêmes intersections
     */
    @Test
    public void test_RechercheDesPlusCourtsChemins_memeIntersection(){

        //Création d'un graphe
        createGraph();

        //Création de la liste des intersections à accéder
        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));
        intersections.add(carte.getIntersections().get(3L));
        intersections.add(carte.getIntersections().get(9L));
        
        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        // Vérification des résultats
        assert(planning.getTrajetsLesPlusCourts().size() == 16);

        planning.getTrajetsLesPlusCourts().forEach( (key,value) -> {
            switch (key) {
	            case "0|0":
	                assert(value.getPoids() == 0.);
	                break;
	            case "1|1":
	                assert(value.getPoids() == 0.);
	                break;
	            case "9|9":
	                assert(value.getPoids() == 0.);
                    break;
                case "3|3":
	                assert(value.getPoids() == 0.);
	                break;
                case "0|1":
                    assert(value.getPoids() == 4.);
                    break;
                case "0|3":
                    assert(value.getPoids() == 16.);
                    break;
                case "0|9":
                    assert(value.getPoids() == 27.);
                    break;
                case "1|0":
                    assert(value.getPoids() == 4.);
                    break;
                case "1|3":
                    assert(value.getPoids() == 12.);
                    break;
                case "1|9":
                    assert(value.getPoids() == 23.);
                    break;
                case "3|0":
                    assert(value.getPoids() == 16.);
                    break;
                case "3|1":
                    assert(value.getPoids() == 12.);
                    break;
                case "3|9":
                    assert(value.getPoids() == 15.);
                    break;
                case "9|0":
                    assert(value.getPoids() == 27.);
                    break;
                case "9|1":
                    assert(value.getPoids() == 23.);
                    break;
                case "9|3":
                    assert(value.getPoids() == 15.);
                    break;
                default:
                    assert(false);
                    break;
            }
        });
    }

    /**
     * Méthode de test pour la vérification des contraintes sur une population
     * 
     * Préconditions :
     *   - correctionCrossover
     */
    @Test
    public void test_verifierPop() {

        Requete r1 = new Requete(1,0,"",3,0,"");
        Requete r2 = new Requete(4,0,"",9,0,"");
        Requete r3 = new Requete(5,0,"",7,0,"");

        List<Demande> demandes1 = new ArrayList<Demande>();
        List<Demande> demandes2 = new ArrayList<Demande>();
        List<Demande> demandes3 = new ArrayList<Demande>();

        demandes1.add(r1.getDemandeCollecte());
        demandes1.add(r2.getDemandeCollecte());
        demandes1.add(r3.getDemandeCollecte());
        demandes1.add(r1.getDemandeLivraison());
        demandes1.add(r2.getDemandeLivraison());
        demandes1.add(r3.getDemandeLivraison());

        demandes2.add(r1.getDemandeCollecte());
        demandes2.add(r2.getDemandeLivraison());
        demandes2.add(r3.getDemandeCollecte());
        demandes2.add(r1.getDemandeLivraison());
        demandes2.add(r2.getDemandeCollecte());
        demandes2.add(r3.getDemandeLivraison());

        demandes2.add(r1.getDemandeCollecte());
        demandes2.add(r1.getDemandeLivraison());
        demandes2.add(r2.getDemandeCollecte());
        demandes2.add(r2.getDemandeLivraison());
        demandes2.add(r3.getDemandeCollecte());
        demandes2.add(r3.getDemandeLivraison());

        
        assert (planning.verifierPop(demandes1) == true);
        assert (planning.verifierPop(demandes2) == false);
        assert (planning.verifierPop(demandes3) == true);
        
    }

    // /**
    //  * Méthode de test pour la vérification de l'espacement minimum des coûts pour une population
    //  */
    // @Test
    // public void test_espacePopulation() {

        

    // 	List<Long> listIntersections1 = new ArrayList<Long>();
    //     List<Long> listIntersections2 = new ArrayList<Long>();
    //     List<Long> listIntersections3 = new ArrayList<Long>();

    //     for (int i = 0; i < 10; i++) {
    //         listIntersections1.add((long)i);
    //         listIntersections2.add((long)i*i);
    //         listIntersections3.add((long)i*i*i);
    //     }

    //     List<Pair<List<Long>, Double>> pop = new ArrayList<Pair<List<Long>, Double>>();

    //     pop.add(new Pair<>(listIntersections1, 1.0));

    //     pop.add(new Pair<>(listIntersections2, 3.0));

    //     pop.add(new Pair<>(listIntersections3, 8.0));

    //     assert (carte.espacePopulation(pop, 10) == false);

    //     assert (carte.espacePopulation(pop, 1) == true);

    // }
    
    // /**
    //  * Méthode de test de la vérification de la génération d'un chromosome enfant
    //  */
    // @Test
    // public void test_crossoverOX() {

    // 	List<Long> P1 = new ArrayList<Long>();
    // 	List<Long> P2 = new ArrayList<Long>();
    	
    // 	P1.add((long)1);
    // 	P1.add((long)3);
    // 	P1.add((long)2);
    // 	P1.add((long)6);
    // 	P1.add((long)4);
    // 	P1.add((long)5);
    // 	P1.add((long)9);
    // 	P1.add((long)7);
    // 	P1.add((long)8);
    	
    // 	P2.add((long)3);
    // 	P2.add((long)7);
    // 	P2.add((long)8);
    // 	P2.add((long)1);
    // 	P2.add((long)4);
    // 	P2.add((long)9);
    // 	P2.add((long)2);
    // 	P2.add((long)5);
    // 	P2.add((long)6);
    	
    	
    //     List<Long> enfant=carte.crossoverOX(P1,P2,3,5); 
     
    //     int[]res= {8,1,9,6,4,5,2,3,7};
        
    //     for(int i=0;i<enfant.size();i++) {
    //     	assert((long)res[i]==enfant.get(i));
    //     }

    // }
    

    /**
     * Méthode de test pour vérifier le calul du coup total d'une tournée
     * 
     * Préconditions :
     *   - calculerLesCheminsLesPlusCourts
     */
    @Test
    public void test_cout_simple() {

        //Création d'un graphe simple
        createSimpleGraph();
        
        //Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,"",3,5,""));

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));
        

        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);

        List<Demande> chromosome = new ArrayList<Demande>();
    	chromosome.add(requetes.get(0).getDemandeCollecte());
        chromosome.add(requetes.get(0).getDemandeLivraison());
        
        Double cout = planning.cout(chromosome);

        assert(cout == 5);
    }

     /**
     * Méthode de test pour vérifier le calul du coup total d'une tournée
     * lorsque des intersections sont à l'infini
     * 
     * Préconditions :
     *   - calculerLesCheminsLesPlusCourts
     */
    @Test
    public void test_cout_infini() {

        //Création d'un graphe simple
        createWeirdGraph();
        
        //Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,5,"",9,5,""));

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(9L));


        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);
        

        List<Demande> chromosome = new ArrayList<Demande>();
    	chromosome.add(requetes.get(0).getDemandeCollecte());
        chromosome.add(requetes.get(0).getDemandeLivraison());
        
        Double cout = planning.cout(chromosome);

        assert(cout >= Double.MAX_VALUE);
    }
    
    // /**
    //  * Méthode de test de la mutation avec recherche locale
    //  * 
    //  * Préconditions :
    //  *   - calculerLesCheminsLesPlusCourts
    //  */
    // @Test
    // public void test_MutationLocalSearch() {
    	 
    // 	//Création d'un graphe
    //     createSimpleGraph();

    //     //Création des requetes
    //     List<Requete> requetes = new ArrayList<Requete>();
    //     requetes.add(new Requete(1,5,3,5));
        
    //     //Calcul des chemins les plus courts
    //     carte.calculerLesTrajetsLesPlusCourts(requetes);

    //     Planning planning = new Planning(carte);
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);

    //     planning.generateNewId();

	//     List<Long> P1 = new ArrayList<Long>();

    // 	P1.add(0L);
    // 	P1.add(1L);

	//     List<Long> mutation=carte.mutationLocalSearch(P1, carte.cout(P1), requetes);

	    
	//     assert(carte.cout(mutation)<=carte.cout(P1));
        
    // }

    // /**
    //  * Méthode de test pour la vérification de la génération d'un chromosome aléatoire
    //  * 
    //  * Préconditions :
    //  *   - genererChromosomeAleatoire
    //  *   - verifierPop
    //  */
    // @Test
    // public void test_genererChromosome() {

    // 	List<Requete> requetes = new ArrayList<Requete>();

	//     requetes.add(new Requete((long)1,0,(long)3,0));
	//     requetes.add(new Requete((long)4,0,(long)9,0));
	//     requetes.add(new Requete((long)5,0,(long)7,0));
    	
    //     List<Long> chromosome=carte.genererChromosomeAleatoire(requetes) ;
        
    //     assert (carte.verifierPop(chromosome, requetes) == true);
        
    // }
    
    /**
     * Méthode de test de la boucle principale de l'algorithme génétique
     * 
     * Précondition :
     *   - calculerLesCheminsLesPlusCourts
     *   - cout
     *   - mutationLocalSearch
     *   - genererChromosomeAleatoire
     *   - verifierPop
     *   - correctionCrossover
     */
    @Test
    public void test_trouverMeilleureTournee_simple() {

        // Création d'un grap simple
        createSimpleGraph();

        // Création des requetes
        List<Requete> requetes = new ArrayList<Requete>();
        Requete requete = new Requete(1,5,"",3,5,"");
        requetes.add(requete);

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        planning.setDateDebut(new Date(0));

        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(carte.getIntersections().get(0L));
        intersections.add(carte.getIntersections().get(1L));
        intersections.add(carte.getIntersections().get(3L));
        
        //Calcul des chemins les plus courts
        planning.calculerLesTrajetsLesPlusCourts(intersections);


        //recherche de la meilleur solution
        planning.calculerMeilleurTournee();

        List<Demande> demandesOrdonnees = planning.getDemandesOrdonnees();
        
        assert(demandesOrdonnees.size()==2);
        
        assert(demandesOrdonnees.get(0) == requete.getDemandeCollecte());
        assert(demandesOrdonnees.get(1) == requete.getDemandeLivraison());
        
        
    }
    
    // /**
    //  * Méthode de test de la boucle principale de l'algorithme génétique
    //  * 
    //  * Précondition :
    //  *   - calculerLesCheminsLesPlusCourts
    //  *   - cout
    //  *   - mutationLocalSearch
    //  *   - genererChromosomeAleatoire
    //  *   - verifierPop
    //  *   - correctionCrossover
    //  */
    // @Test
    // public void test_trouverMeilleureTournee_normal() {

    //     // Création d'un grap simple
    //     createGraph();

    //     // Création des requetes
    // 	List<Requete> requetes = new ArrayList<Requete>();
    //     requetes.add(new Requete(3,5,5,5));
    //     requetes.add(new Requete(6,5,8,5));
    //     requetes.add(new Requete(9,5,4,5));
        
    //     //Calcul des chemins les plus courts
    //     carte.calculerLesTrajetsLesPlusCourts(requetes);

    //     Planning planning = new Planning(carte);
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);

    //     planning.generateNewId();

    //     //recherche de la meilleur solution
    //     List<Long> bestSolution=carte.trouverMeilleureTournee(requetes);

    //     assert(carte.cout(bestSolution)<=81.0);
        
    // }

    /**
     * Méthode de test pour vérifier les horraires de la tournée calculés
     * 
     * Préconditions :
     *   - calculerLesCheminsLesPlusCourts
     *   - cout
     *   - mutationLocalSearch
     *   - genererChromosomeAleatoire
     *   - verifierPop
     *   - correctionCrossover
     */
    @Test
    public void test_calculerTournee_simple() {
        
        createSimpleGraph();

        List<Requete> requetes = new ArrayList<Requete>();
        requetes.add(new Requete(1,3,"",3,5,""));
        
        Planning planning = new Planning(carte);

        planning.setIdDepot(0L);
        planning.setRequetes(requetes);
        Date d = new Date(0);
        planning.setDateDebut(d);
        
        planning.calculerMeilleurTournee();

        Demande demande;
        Long d1, d2;

        List<Demande> demandes = planning.getDemandesOrdonnees();

        assert(demandes.size() == 2);

        assert(planning.getDateDebut().getTime() == 0);
        assert(planning.getDateFin().getTime() == (5*3600/15 + 8000));
        assert(planning.getDureeTotale() == (5*3600/15 + 8000));

        demande = demandes.get(0);
        d1 = demande.getDateArrivee().getTime();
        d2 = demande.getDateDepart().getTime();
        assert(d1 == 1*3600/15);
        assert(d2 == 1*3600/15 + 3000);

        demande = demandes.get(1);
        d1 = demande.getDateArrivee().getTime();
        d2 = demande.getDateDepart().getTime();
        assert(d1 == 3*3600/15 + 3000 );
        assert(d2 == 3*3600/15 + 8000 );
    }

    // }
    
    // /**
    //  * Méthode pour tester la création des tournée avec des mêmes
    //  * intersections dans les requêtes
    //  */
    // @Test
    // public void test_calculerTournee_memeIntersection() {
    // 	 // Création d'un grap simple
    //     createGraph();

    //     // Création des requetes
    // 	List<Requete> requetes = new ArrayList<Requete>();
     
    // 	requetes.add(new Requete(1,5,3,5));
    //     requetes.add(new Requete(3,5,9,5));
        
    //     Planning planning = new Planning(carte);
        
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);
    //     Date d = new Date(0);
    //     planning.setDateDebut(d);
        
    //     planning.calculerTournee();


    //     /*System.out.println("size :" + planning.getListeTrajets().size());

    //     for (Trajet t : planning.getListeTrajets()) {
    //         if (t.getListeSegments().size()!=0) {
    //             System.out.print(t.getListeSegments().get(0).getDepart() + " ");
    //             System.out.println(t.getListeSegments().get(t.getListeSegments().size()-1).getArrivee());
    //         } else System.out.println("?");
    //     }
    //     */

    //     Map<Long,Date> datesPassages = planning.getDatesPassage();
    //     Map<Long,Date> datesSorties = planning.getDatesSorties();

    //     Long d1_depot = datesPassages.get(planning.getIdUniqueDepot()).getTime();
    //     Long d2_depot = datesSorties.get(planning.getIdUniqueDepot()).getTime();
    //     assert(d1_depot == 58*3600/15 + 20*1000);
    //     assert(d2_depot == 0);

    //     for (Requete r : requetes) {
    //         Long d1 = datesPassages.get(r.getIdUniquePickup()).getTime();
    //         Long d2 = datesSorties.get(r.getIdUniquePickup()).getTime();
    //         Long d3 = datesPassages.get(r.getIdUniqueDelivery()).getTime();
    //         Long d4 = datesSorties.get(r.getIdUniqueDelivery()).getTime();

    //         switch (r.getIdPickup() + "|" +r.getIdDelivery() ) {
    //             case "1|3":
    //                 assert(d1 == 4*3600/15);
    //                 assert(d2 == 4*3600/15 + 5*1000);
    //                 assert( (d3 == 16*3600/15 + 5*1000) || (d3 == 16*3600/15 + 10*1000) );
    //                 assert( (d4 == 16*3600/15 + 10*1000) || (d4 == 16*3600/15 + 15*1000) ) ;
    //                 break;
    //             case "3|9":
    //                 assert( (d1 == 16*3600/15 + 5*1000) || (d1 == 16*3600/15 + 10*1000) );
    //                 assert( (d2 == 16*3600/15 + 10*1000) || (d2 == 16*3600/15 + 15*1000) ) ;
    //                 assert(d3 == 31*3600/15 + 15*1000);
    //                 assert(d4 == 31*3600/15 + 20*1000);
    //                 break;
    //             default:
    //                 assert(false);
    //                 break;
    //         }

    //     }

    // }
    
    // /**
    //  * Méthode pour tester la modification d'une requête en remplaçant un point par un nouveau
    //  */
    // @Test
    // public void test_modifier_requetes_intersection() {
    // 	 // Création d'un graphe
    //     createGraph();

    //     // Création des requetes
    // 	List<Requete> requetes = new ArrayList<Requete>();
     
    // 	requetes.add(new Requete(1,5,3,5));
    //     requetes.add(new Requete(4,5,9,5));
    //     requetes.add(new Requete(6,5,8,5));
       
    //     Planning planning = new Planning(carte);
        
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);
    //     Date d = new Date(0);
    //     planning.setDateDebut(d);

    //     planning.calculerTournee();

    //     double dureeAvantChangement=planning.getDureeTotale();

    //     //modfication du du point de livraison de la 1 ère requête
    //     Requete r1=requetes.get(0);
    //     planning.modifierRequeteLieu(r1.getIdUniqueDelivery(),7);

    //     //on vérifie si la durée totale du nouveau planning est cohérente avec celle d'avant la modif
    //     assert(Math.abs(planning.getDureeTotale()-dureeAvantChangement-0.48)<0.001);

    // }
    
    // /**
    //  * Méthode pour tester la modification d'une requête en changeant le durée de livraison
    //  */
    // @Test
    // public void test_modifier_requetes_duree() {
    // 	 // Création d'un graphe
    //     createGraph();

    //     // Création des requetes
    // 	List<Requete> requetes = new ArrayList<Requete>();
     
    // 	requetes.add(new Requete(1,5,3,5));
    //     requetes.add(new Requete(4,5,9,5));
    //     requetes.add(new Requete(6,5,8,5));
       
    //     Planning planning = new Planning(carte);
        
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);
    //     Date d = new Date(0);
    //     planning.setDateDebut(d);

    //     planning.calculerTournee();
        
    //     double dureeAvantChangement=planning.getDureeTotale();

    //     //modfication de la durée de livraison
    //     Requete r1=requetes.get(0);
        
    //     r1.setDureePickup(15);
        
    //     planning.setRequetes(requetes);
        
    //     planning.calculTempsDePassage();

    //     //on vérifie si la durée totale du nouveau planning est cohérente avec celle d'avant la modif
    //     assert(Math.abs(planning.getDureeTotale()-dureeAvantChangement)==10.0);

    // }
    
    // /**
    //  * Méthode pour tester la modification d'une requête en inversant 2 points de livraison ou collecte
    //  */
    // @Test
    // public void test_modifier_requetes_ordre() {
    // 	 // Création d'un graphe
    //     createGraph();

    //     // Création des requetes
    // 	List<Requete> requetes = new ArrayList<Requete>();
     
    // 	requetes.add(new Requete(1,5,3,5));
    //     requetes.add(new Requete(4,5,9,5));
    //     requetes.add(new Requete(6,5,8,5));
       
    //     Planning planning = new Planning(carte);
        
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);
    //     Date d = new Date(0);
    //     planning.setDateDebut(d);

    //     planning.calculerTournee();
        
    //     double dureeAvantChangement=planning.getDureeTotale();

    //     //modfication de l'ordre de passage

    //     planning.modifierOrdreRequetes(0,4);
      
    //     //on vérifie si la durée totale du nouveau planning est cohérente avec celle d'avant la modif
    //     assert(Math.abs(planning.getDureeTotale()-dureeAvantChangement-2.16)<0.01);

    // }
    
    // /**
    //  * Méthode pour tester l'ajout de requête après le calcul de la tournée
    //  */
    // @Test
    // public void test_ajouter_requetes() {
    // 	 // Création d'un graphe
    //     createGraph();
        
    //     // Création des requetes de référence
    // 	List<Requete> requetes = new ArrayList<Requete>();
     
    // 	requetes.add(new Requete(1,5,3,5));
    //     requetes.add(new Requete(4,5,9,5));
    //     requetes.add(new Requete(6,5,8,5));
       
    //     Planning planning = new Planning(carte);
        
    //     planning.setIdDepot(0L);
    //     planning.setRequetes(requetes);
    //     Date d = new Date(0);
    //     planning.setDateDebut(d);

    //     planning.calculerTournee();
        
    //     double dureeReference=planning.getDureeTotale();
        
    //     // Création des requetes de test
    // 	List<Requete> requetesTest = new ArrayList<Requete>();
     
    // 	requetesTest.add(new Requete(1,5,3,5));
    //     requetesTest.add(new Requete(4,5,9,5));
       
    //     Planning planningTest = new Planning(carte);
        
    //     planningTest.setIdDepot(0L);
    //     planningTest.setRequetes(requetesTest);
    //     Date dTest = new Date(0);
    //     planningTest.setDateDebut(dTest);
        
    //     planningTest.calculerTournee();

    //     //ajout d'une nouvelle requête

    //     planningTest.ajouterRequete(new Requete(6,5,8,5));
 
    //     //on vérifie si la durée totale du nouveau planning est cohérente
 
    //     assert(Math.abs(planningTest.getDureeTotale()-dureeReference)<=1);

    // }


    /**
     * Méthode pour créer un graphe orienté simple avec 4 intersections
     */
    private void createSimpleGraph() {
        
        planning.setIdDepot(0L);

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
    
    /**
     * Méthode pour créer un graphe non orienté avec 10 intersections
     */
    private void createGraph() {
        //http://yallouz.arie.free.fr/terminale_cours/graphes/graphes.php?page=g3
        
        planning.setIdDepot(0L);

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

    /**
     * Méthode pour créer un graphe non orienté avec 10 intersections
     * avec un cul de sac et un point inaccessible
     */
    private void createWeirdGraph() {
        //http://yallouz.arie.free.fr/terminale_cours/graphes/graphes.php?page=g3

        // Caractéristiques : 
        // J est innacessible
        // H est un cul de sac
        
        planning.setIdDepot(0L);

        Intersection i0 = new Intersection(0, 0, 0); //A
        Intersection i1 = new Intersection(1, 0, 0); //B
        Intersection i2 = new Intersection(2, 0, 0); //C
        Intersection i3 = new Intersection(3, 0, 0); //D
        Intersection i4 = new Intersection(4, 0, 0); //E
        Intersection i5 = new Intersection(5, 0, 0); //F
        Intersection i6 = new Intersection(6, 0, 0); //G
        Intersection i7 = new Intersection(7, 0, 0); //H
        Intersection i8 = new Intersection(8, 0, 0); //I
        Intersection i9 = new Intersection(9, 0, 0); //J


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
        
        Segment s37 = new Segment(9, "nom", 3, 7);
        Segment s73 = new Segment(9, "nom", 7, 3);
        
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
        
        set3.add(s31);
        set3.add(s32);
        set3.add(s36);
        set3.add(s37);
        
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
        
        set7.add(s73);
        
        set8.add(s80);
        set8.add(s84);
        set8.add(s85);
        

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