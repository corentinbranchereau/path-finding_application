package fr.hexaone.model;

import java.util.*;

import org.javatuples.Pair;

// TODO
// Set pour les intersections dans le calcul des plus courts trajets
// générer chromosomes aléa, mieux qu'un do while ?
// treeset pour générer les plus courts trajets

/**
 * Objet contenant toutes les informations relatives au planning d'une tournée "
 *
 * @author HexaOne
 * @version 1.0
 */
public class Planning{

    /**
     * L'id du Dépôt associé au planning
     */
    protected Long idDepot;

    /**
     * Date de début de la tournée. 24 hours format - H:m:s
     */
    protected Date dateDebut;

    /**
     * Date de la fin de la tournée / du retour au dépot
     */
    protected Date dateFin;

    /**
     * Liste des requêtes en rapport avec la demande client
     */
    protected List<Requete> requetes;

    /**
     * Liste des ids uniques d'intersections constituant la tournée
     */
    protected List<Demande> demandesOrdonnees;
    
    /**
     * Carte associée au planning
     */
    protected Carte carte;

    /**
     * liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * Duree totale de la tournée
     */
    protected double dureeTotale;

    /**
	 * Map permettant d'identifier les chemins les plus courts à partir d'un identifiant (String) 
	 */
    protected Map<String, Trajet> TrajetsLesPlusCourts;

    /**
     * Comparateur afin de classer les chromosomes au sein d'une population dans
     * l'ordre croissant des coûts
     *
     * @param depot
     * @param requetes
     */
    public Comparator<Pair<List<Demande>, Double>> ComparatorChromosome = new Comparator<Pair<List<Demande>, Double>>() {

        @Override
        public int compare(Pair<List<Demande>, Double> e1, Pair<List<Demande>, Double> e2) {
            return (int) (e1.getValue1() - e2.getValue1());
        }
    };
    
    

    ///////////////////////////////
    // Méthode de plannification //
    ///////////////////////////////

    /**
     * Constructeur du planning
     * 
     * @param carte
     */
    public Planning(Carte carte) {
        this.requetes = new ArrayList<>();
        this.carte=carte;
    }
    
    /**
     * Constructeur du planning
     * 
     * @param carte
     */
    public Planning(Carte carte, List<Requete> requetes) {
        this.requetes = requetes;
        this.carte=carte;
    }

    /**
     * Recherche de la tournée la plus rapide :
     *  - Crée une la liste ordonnée de demandes demandesOrdonnées
     *  - Crée la liste des trajets et calcul les durées de passage
     *    et de sorties dans les demandes
     * 
     * A appeler qu'une fois pour générer le premier ordonnancement
     * 
     * Prérequis : 
     *  - La liste des requetes
     *  - La date de début de la tournée
     *  - idDépot
     *
     * @param planning
     * @return
     */
    public void calculerMeilleurTournee() {

        // Recherche des chemins des plus courts entre toutes les 
        // intersections spéciales (dépots, livraisons et dépot)
        List<Intersection> intersectionsSpeciales = new ArrayList<Intersection>();
        intersectionsSpeciales.add(carte.intersections.get(idDepot));
        for (Requete r : requetes) {
            intersectionsSpeciales.add(carte.intersections.get(r.getDemandeCollecte().getIdIntersection()));
            intersectionsSpeciales.add(carte.intersections.get(r.getDemandeLivraison().getIdIntersection()));
            
        }
        calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);
        
        //recherche de la melleure tournéee
        List<Demande> demandes = new ArrayList<Demande>();
        for (Requete requete : requetes) {
            demandes.add(requete.getDemandeCollecte());
            demandes.add(requete.getDemandeLivraison());
        }
        demandesOrdonnees = ordonnerLesDemandes(demandes); //ordonnerLesDemandes


        //Création de la listes des trajets à suivre et calcul des temps
        ordonnerLesTrajetsEtLesDates();

    }

    /**
     * Recrée une liste de trajets à partir des demandes ordonnées
     * et calcul des dates de passage dans les demandes.
     * 
     * A utiliser après un changement d'ordonnancement des demandes.
     * 
     * Prérequis :
     *     - Avoir les demandes ordonnées
     */
    public void ordonnerLesTrajetsEtLesDates() {
        Long prevIntersectionId =idDepot;
        listeTrajets = new LinkedList<Trajet>();
        double duree = 0.;
        Long tempsDebut = dateDebut.getTime();

        for (Demande demande : demandesOrdonnees) {
            Long newId = demande.idIntersection;
            Trajet trajet = TrajetsLesPlusCourts.get(prevIntersectionId + "|" + newId);
            listeTrajets.add(trajet);
            prevIntersectionId = newId;

            duree += trajet.poids*3600. / 15.;
            demande.setDateArrivee(new Date(tempsDebut + (long)duree));
            duree += demande.duree*1000;
            demande.setDateDepart(new Date(tempsDebut + (long)duree));
        }

        Trajet trajet = TrajetsLesPlusCourts.get(prevIntersectionId + "|" + idDepot);
        listeTrajets.add(trajet);
        duree += trajet.getPoids()*3600. / 15.;
        dateFin = new Date(tempsDebut + (long)duree);
        dureeTotale = duree;
    }
    
    /**
     * Recalcule tous les plus courts trajets entre toutes demandes.
     *  - Crée un nouvelle liste de trajets ordonnées et calcul les durées de passage
     *    et de sorties dans les demandes.
     * 
     * A utiliser après l'ajout ou la supression de demandes
     * ou la modification des temps et lieux de demandes.
     * 
     * Prérequis :
     *  - Avoir les demandes ordonnées.
     */
    public void recalculerTournee() {
        //Recalcule tous les plus courts trajets des demandes
        List<Intersection> intersectionsSpeciales = new ArrayList<Intersection>();
        intersectionsSpeciales.add(carte.intersections.get(idDepot));
        for (Demande demande : demandesOrdonnees) {
            intersectionsSpeciales.add(carte.intersections.get(demande.getIdIntersection()));
        }
        calculerLesTrajetsLesPlusCourts(intersectionsSpeciales);

        //Recréer une liste de trajet et recalcule les dates des demandes
        ordonnerLesTrajetsEtLesDates();
    }

    /**
     * Ajouter une requete après avoir déja calculer la 
     * meilleure tournée.
     */
    public void ajouterRequete(Requete requete) {

        requetes.add(requete);

        demandesOrdonnees.add(requete.demandeCollecte);
        demandesOrdonnees.add(requete.demandeLivraison);

        recalculerTournee();
    }

    /**
     * Supprimer une requete de la tournée et regénère les trajets ordonées
     */
    public void supprimerRequete(Requete requete) {
        requetes.remove(requete);
        demandesOrdonnees.remove(requete.getDemandeCollecte());
        demandesOrdonnees.remove(requete.getDemandeLivraison());

        ordonnerLesTrajetsEtLesDates();

        System.out.println(demandesOrdonnees.size());
    }

    
    
    ///////////////////////////////////////////////
    // Algo de recherche des plus courts trajets //
    ///////////////////////////////////////////////

    /**
     * Calculer tous les trajets les plus courts entre toutes les
     * intersections en paramètre
     * @param intersections
     */
    public void calculerLesTrajetsLesPlusCourts(List<Intersection> intersections) {

        // Préparation

        Map<Long,Intersection> allIntersections = carte.getIntersections();

        TrajetsLesPlusCourts = new HashMap<String, Trajet>();

        // Calcul de tous les chemins les plus courts n fois avec dijkstra

        for (Intersection source : intersections) {
        	
            source.setDistance(0.);

            Set<Intersection> settledIntersections = new HashSet<Intersection>();

            Set<Intersection> unsettledIntersections = new HashSet<Intersection>();

            /*
             * SortedSet<Intersection> unsettledIntersections = new
             * TreeSet<Intersection>(new Comparator<Intersection>() { // TODO Vérifier si
             * c'est le set le plus efficace pour récupérer le premier
             *
             * @Override public int compare(Intersection o1, Intersection o2) { return
             * (int)(o1.getDistance()-o2.getDistance()); } });
             */

            unsettledIntersections.add(source);

            while (unsettledIntersections.size() != 0) {

                // Intersection currentIntersection = unsettledIntersections.first();

                Intersection currentIntersection = getLowestDistanceIntersection(unsettledIntersections);
                unsettledIntersections.remove(currentIntersection);

                for (Segment segmentAdjacent : currentIntersection.getSegmentsPartants()) {
                    Intersection adjacentIntersection = allIntersections.get(segmentAdjacent.getArrivee());
                    Double edgeWeight = segmentAdjacent.getLongueur();
                    if (!settledIntersections.contains(adjacentIntersection)) {
                        CalculateMinimumDistance(adjacentIntersection, edgeWeight, currentIntersection,
                                segmentAdjacent);
                        unsettledIntersections.add(adjacentIntersection);
                    }
                }
                settledIntersections.add(currentIntersection);
            }

            String sourceId = source.getId() + "|";

            for (Intersection i : intersections) {
                String key = sourceId + i.getId();
                TrajetsLesPlusCourts.put(key, new Trajet(i.getCheminLePlusCourt(), i.getDistance()));
            }

            allIntersections.forEach((id, intersection) -> {
                intersection.resetIntersection();
            });
        }
    }

    /**
     * Retourne l'intersection avec la distance la plus faible
     * @param unsettledIntersections
     * @return lowestDistanceIntersection : l'intersection la plus proche
     */
    public Intersection getLowestDistanceIntersection(Set<Intersection> unsettledIntersections) {
        Intersection lowestDistanceIntersection = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Intersection intersection : unsettledIntersections) {
            double intersectionDistance = intersection.getDistance();
            if (intersectionDistance < lowestDistance) {
                lowestDistance = intersectionDistance;
                lowestDistanceIntersection = intersection;
            }
        }
        return lowestDistanceIntersection;
    }

    /**
     * Enregistre la distance minimale pour accéder à une intersection
     * @param evaluationIntersection
     * @param edgeWeigh
     * @param sourceIntersection
     * @param seg
     */
    public void CalculateMinimumDistance(Intersection evaluationIntersection, Double edgeWeigh,
            Intersection sourceIntersection, Segment seg) {
        Double sourceDistance = sourceIntersection.getDistance();
        if (sourceDistance + edgeWeigh < evaluationIntersection.getDistance()) {
            evaluationIntersection.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Segment> shortestPath = new LinkedList<>(sourceIntersection.getCheminLePlusCourt());
            shortestPath.add(seg);
            evaluationIntersection.setCheminLePlusCourt(shortestPath);
        }
    }



    //////////////////////////////////
    // Algo de recherche de tournée //
    //////////////////////////////////

    /**
     * Algorithme génétique permettant de trouver la meilleure tournée
     * à partir des demandes.
     *
     * @param demandes
     */
    public List<Demande> ordonnerLesDemandes(List<Demande> demandes) {

    	//initialisation des constantes de l'algo génétique

        int sigma = 30; // Nb chromosomes max dans une population
        double delta = 3;// Minimum d'ecart entre les valeurs dans la population
        double p = 0.1; // probabilité d'améliorer avec du Local Search un enfant
        int nMax = 10000;// Nb max d'itérations pour générer une pop initiale
        int alphamax = 30000;// Nb max de crossovers productifs
        int BetaMax = 10000;// Nb max de crossosovers sans améliorer la solution
        int maxIter = 1000;//Nb max d'iterations d'affilé à ne pas trouver de solution


        //création de la population
        List<Pair<List<Demande>, Double>> population = new ArrayList<Pair<List<Demande>, Double>>();

        //1er élément de la population obtenu avec du 2-opt
        List<Demande> chr1 = this.genererChromosomeAleatoire(demandes);
        chr1 = this.mutationLocalSearch(chr1, cout(chr1));
        population.add(new Pair<>(chr1, cout(chr1)));

        //initialisation des variables
        int k = 1;
        int nbEssais = 0;
        int iter = 0;

        double cost;
        Pair<List<Demande>, Double> chrom;

        Random rand = new Random();

        //génération aléatoire de la population initiale
        while (k <= sigma && nbEssais <= nMax) {
            k++;
            nbEssais = 0;

            do {
            	//on essaie de générer un chromosome aléatoire avec un nombre max d'essais
                nbEssais++;
                List<Demande> chr = genererChromosomeAleatoire(demandes);
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

        /*
         * for (Pair<List<Long>,Double> pair : population) {
         *
         * System.out.print(pair.getValue1() + " : "); List<Long> ll = pair.getValue0();
         * for (Long l : ll) { System.out.print(l+ ", "); } System.out.println(); }
         */

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
            List<Demande> child;

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

                List<Demande> M = mutationLocalSearch(child, cout(child));

                double ancienCout = population.get(kRand).getValue1();
                population.get(kRand).setAt1(Integer.MIN_VALUE);

                if (espacePopulation(population, delta, costChild)) {
                	//si les contraintes d'espacement sont vérifiés avec le nouvel enfant
                    child = M;
                    costChild = cout(child);
                }

                population.get(kRand).setAt1(ancienCout);

            }

            double ancienCout = population.get(kRand).getValue1();
            population.get(kRand).setAt1(Integer.MIN_VALUE);

            if (espacePopulation(population, delta, costChild)) {

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

        return population.get(0).getValue0();
    }

    /**
     * Calcule le cout d'un chromsomome
     *
     * @param chromosome que l'on teste
     */
    public double cout(List<Demande> chromosome) {

        double somme = 0;
        Long prevIntersectionId = idDepot;

        for (Demande demande: chromosome) {
            Long newId = demande.getIdIntersection();
            somme += TrajetsLesPlusCourts.get(prevIntersectionId + "|" + newId).getPoids();
            prevIntersectionId = newId;
        }
        somme += TrajetsLesPlusCourts.get(prevIntersectionId + "|" + idDepot).getPoids();

        return somme;
    }

    /**
     * Renvoie une liste de chromosomes aléatoires (donc une liste d'intersections)
     *
     * @param demandes
     */
    public List<Demande> genererChromosomeAleatoire(List<Demande> demandes) {
        // Copie de la liste des demandes
        List<Demande> shuffleList = new ArrayList<Demande>(demandes);

        do { //réarrangement aléatoire de la population
            Collections.shuffle(shuffleList);
        } while (!verifierPop(shuffleList)); // Sérieux ? XD

        return shuffleList;

    }

    /**
     * Renvoie true si toutes les contraintes de précédences sont respectées, faux
     * sinon
     *
     * @param chromosome que l'on teste
     * @param requetes   liste des requêtes
     */
    public Boolean verifierPop(List<Demande> chromosome) {

        ArrayList<Demande> collecteRealisee = new ArrayList<Demande>();

        for (Demande demande : chromosome) {
            if ( demande.getTypeIntersection() == TypeIntersection.COLLECTE) {
                collecteRealisee.add(demande);
            } else if ( demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
                Demande collecte = demande.getRequete().getDemandeCollecte();
                if ( collecteRealisee.contains(collecte) ){
                    collecteRealisee.remove(collecte);
                } else {
                    return false;
                }
            } else {
                System.out.println("Erreur dans le type de la demande n°" + demande.idDemande);
                return false;
            }
        }
        return true;

    }

    /**
     * Mutation d'un chromosme grâce à un algorithme de recherche locale
     *
     * @param chromosome que l'on teste
     * @param requetes   liste des requêtes
     */
    public List<Demande> mutationLocalSearch(List<Demande> chromosome, double coutIni) {

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
                        Demande v = chromosome.get(j);
                        Demande x = chromosome.get(i + 1);
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
     * Permet la correction d'un chromosome en intégrant les conditions de
     * précédence des requêtes Echange la place des couples <pickup,delivery> quand
     * ils sont inversés
     *
     * @param requetes
     * @param chromosome
     */
    public List<Demande> correctionCrossover(List<Demande> chromosome) {
        List<Pair<Integer,Integer>> aSwap  = new ArrayList<Pair<Integer,Integer>>();

        Integer posLivraison = 0;
        for (Demande demande : chromosome) {
            if ( demande.getTypeIntersection() == TypeIntersection.LIVRAISON) {
                Integer posCollecte = chromosome.indexOf(demande.getRequete().getDemandeCollecte());
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
     * en termes de couts
     *
     * @param population à tester
     * @param ecart      maximum
     */
    public Boolean espacePopulation(List<Pair<List<Demande>, Double>> population, double ecart) {
        Pair<List<Demande>, Double> chromPres = null;
        for (Pair<List<Demande>,Double> chrom : population) {
            if (chromPres != null && Math.abs(chrom.getValue1() - chromPres.getValue1()) < ecart) {
                return false;
            }
        }
        return true;
    }

    /**
     * Renvoie true si l'espacement minimum dans la population est supérieur à ecart
     * en termes de couts avec l'ajout de l'enfant
     *
     * @param population   à tester
     * @param ecart        maximum
     * @param valeurEnfant
     */
    public Boolean espacePopulation(List<Pair<List<Demande>, Double>> population, double ecart, double valeurEnfant) {
        for (Pair<List<Demande>,Double> chrom : population) {
            if (Math.abs(chrom.getValue1() - valeurEnfant) < ecart) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Réalise le crossover de chromosomes afin d'obtenir un enfant
     *
     * @param depot
     * @param requetes
     */
    public List<Demande> crossoverOX(List<Demande> P1, List<Demande> P2, int i, int j) {

        List<Demande> child = new ArrayList<Demande>();

        for (int k = 0; k < P1.size(); k++) {
            child.add(null);
        }

        int max = max(i, j);
        int min = min(i, j);

        Set<Demande> intersectionsVus = new HashSet<Demande>();

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
     * Retourne le max entre a et b
     *
     * @param a
     * @param b
     */
    public int max(int a, int b) {

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
     */
    public int min(int a, int b) {

        if (a < b) {
            return a;
        }
        return b;
    }



    ///////////////////////
    // GETTER AND SETTER //
    ///////////////////////

    public Long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(Long idDepot) {
        this.idDepot = idDepot;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public List<Requete> getRequetes() {
        return requetes;
    }

    public void setRequetes(List<Requete> requetes) {
        this.requetes = requetes;
    }

    public List<Demande> getDemandesOrdonnees() {
        return demandesOrdonnees;
    }

    public void setDemandesOrdonnees(List<Demande> demandesOrdonnees) {
        this.demandesOrdonnees = demandesOrdonnees;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public List<Trajet> getListeTrajets() {
        return listeTrajets;
    }

    public void setListeTrajets(List<Trajet> listeTrajets) {
        this.listeTrajets = listeTrajets;
    }

    public double getDureeTotale() {
        return dureeTotale;
    }

    public void setDureeTotale(double dureeTotale) {
        this.dureeTotale = dureeTotale;
    }

    public Map<String, Trajet> getTrajetsLesPlusCourts() {
        return TrajetsLesPlusCourts;
    }

    public void setTrajetsLesPlusCourts(Map<String, Trajet> TrajetsLesPlusCourts) {
        this.TrajetsLesPlusCourts = TrajetsLesPlusCourts;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

}
