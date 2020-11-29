package fr.hexaone.model;

import java.util.*;

/**
 * Objet contenant toutes les informations relatives au planning d'une tournée "
 *
 * @author HexaOne
 * @version 1.0
 */
public class Planning {

    /**
     * L'id du Dépôt associé au planning
     */
    protected Long idDepot;
    /**
     * L'id unqiue du Dépôt associé au planning
     */
    protected Long idUniqueDepot;
    /**
     * Date de début de la tournée. 24 hours format - H:m:s
     */
    protected Date dateDebut;
    /**
     * Liste des requêtes en rapport avec la demande client
     */
    protected List<Requete> requetes;
    
    /**
     * Liste des ids uniques d'intersections constituant la tournée
     */
    protected List<Long> tournee;
    
    /**
     * carte associée au planning
     */
    protected Carte carte;


	/**
     * liste de tous les trajets composant la tournée
     */
    protected List<Trajet> listeTrajets;

    /**
     * Dates des passages des points spéciaux
     */
    protected Map<Long, Date> datesPassage;
    /**
     * Duree totale de la duree
     */

    protected Map<Long, Date> datesSorties;

    /**
     * Duree totale de la duree
     */
    protected double dureeTotale;
    
    protected static Map<Long,Long> idUniqueTOIdIntersection;
    
    protected Long idUniqueMax;

    /**
     * Constructeur par défaut de Planning
     */
    public Planning(Carte carte) {
        this.requetes = new ArrayList<>();
        this.datesPassage = new HashMap<>();
        this.carte=carte;
    }
    
    /**
     * Calcul du temps d'arrivé et de sortie sur les points spéciaux
     */
    public void calculTempsDePassage() {

        Map<Long,Date> datesPassage = new HashMap<Long,Date>();
        Map<Long,Date> datesSorties = new HashMap<Long,Date>();

        Long tempDebut = dateDebut.getTime();
        double duree = 0.;
        
        datesSorties.put(idUniqueDepot, new Date(tempDebut));
        Long previd = idDepot;
        for (Long id : tournee) {
            Long idInter = Planning.idUniqueTOIdIntersection.get(id);
            Trajet t = carte.cheminsLesPlusCourts.get(previd + "|" + idInter);
            duree += t.getPoids() * 3600. / 15.;
            datesPassage.put(id, new Date(tempDebut + (long)duree ));
            for (Requete r : requetes) {
                if (r.getIdUniqueDelivery() == id) {
                    duree += r.getDureeDelivery() * 1000 ;
                    break;
                }
                
                if (r.getIdUniquePickup() == id) {
                    duree += r.getDureePickup() * 1000 ;
                    break;
                }
                
            }

            datesSorties.put(id, new Date(tempDebut + (long)duree ));
            previd = idInter;
        }

        Trajet t = carte.cheminsLesPlusCourts.get(previd + "|" + idDepot);
        duree += t.getPoids() * 3600. / 15.;
        datesPassage.put(idUniqueDepot, new Date(tempDebut + (long)duree ));

        setDureeTotale(duree/1000.0);
        setDatesSorties(datesSorties);
        setDatesPassage(datesPassage);

    }
    
    
    public Carte getCarte() {
		return carte;
	}


	/**
     * Génère des ids uniques pour chaque point de collecte ou de livraison d'une requête
     * @param planning
     */
    public void generateNewId() {
        Long id = 0L;

        idUniqueTOIdIntersection = new HashMap<Long,Long>();

        for (Requete requete : requetes) {
            idUniqueTOIdIntersection.put(id, requete.getIdPickup());
            requete.setIdUniquePickup(id);
            id += 1;
            idUniqueTOIdIntersection.put(id, requete.getIdDelivery());
            requete.setIdUniqueDelivery(id);
            id += 1;
        }

        idUniqueMax=id-1;
        this.idUniqueDepot=id;

    }
    
    /**
     * Permet de faire une modification de requête : changement d'un point de pickup ou delivery 
     * @param planning planning en cours de modification
     * @param idUnique de l'intersection de la requête à changer
     * @param idIntersection de la nouvelle intersection (géographique)
     * @return
     */
    public Planning modifierRequeteLieu(long idUnique,long idIntersection) {

    	  //recherche de l'intersection dans les requêtes
    	  for(Requete r : requetes) {
    		  if(r.getIdUniqueDelivery()==idUnique) {
    			  r.setIdDelivery(idIntersection);
    		  }
    		  
    		  if(r.getIdUniquePickup()==idUnique) {
    			  r.setIdPickup(idIntersection);
    			  
    		  }
    	  }
          
    	  //maj de la map des correspondances d'ids
    	  idUniqueTOIdIntersection.put(idUnique,idIntersection);
    	  
          //recalcul des chemins
    	  carte.calculerLesCheminsLesPlusCourts(requetes);
    	
    	  Long prevIntersectionId = idDepot;
    	  
          List<Trajet> listTrajets = new LinkedList<Trajet>();

          for (Long newId : tournee) {
              newId = idUniqueTOIdIntersection.get(newId);
             
              listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId));

              prevIntersectionId = newId;
          }

          listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + idDepot));
          
          this.listeTrajets=listTrajets;
          
          this.calculTempsDePassage();
          
          return this;
    	  
    }
    
    /**
     * Permet de faire une modification de requête : changement d'un point de pickup ou delivery 
     * @param planning
     * @param idUnique1
     * @param idUnique2
     * @return
     */
    public Planning modifierOrdreRequetes(long idUnique1,long idUnique2) {
    	
			/*for(Long l : this.idUniqueTOIdIntersection.keySet())
			{
				System.out.println(l + ":" +this.idUniqueTOIdIntersection.get(l));
			}
			*/
		
    	  int i1=0;
    	  int i2=0;
    	  
    	  i1=tournee.indexOf(idUnique1);
    	  i2=tournee.indexOf(idUnique2);
   
    	  Collections.swap(tournee,i1,i2);
    	  
    	  Long prevIntersectionId = idDepot;
    	  
          List<Trajet> listTrajets = new LinkedList<Trajet>();
          
          for (Long newId : tournee) {
              newId = idUniqueTOIdIntersection.get(newId);
             
              listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId));

              prevIntersectionId = newId;
          }

          listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + idDepot));
          
          listeTrajets=listTrajets;

          //nouveaux temps de passage
          this.calculTempsDePassage();
          
          return this;
        
    }
   
    /**
     * Permet d'ajouter une requête après le calcul
     * @param planning
     * @param newRequete
     * @return
     */
    public Planning ajouterRequete(Requete newRequete) {

    	  List<Trajet> listTrajets = new LinkedList<Trajet>();
    	 
    	  tournee.add(idUniqueMax+1);
    	  tournee.add(idUniqueMax+2);
    	  
    	  //maj de la map des correspondances d'ids
    	  idUniqueTOIdIntersection.put(idUniqueMax+1,newRequete.getIdPickup());
    	  idUniqueTOIdIntersection.put(idUniqueMax+2,newRequete.getIdDelivery());
    	  
    	  newRequete.setIdUniquePickup(idUniqueMax+1);
    	  newRequete.setIdUniqueDelivery(idUniqueMax+2);
    	  
    	  idUniqueMax+=2;
    	  
    	  requetes.add(newRequete);
        
          //recalcul des chemins
    	  carte.calculerLesCheminsLesPlusCourts(requetes);
    	
    	  Long prevIntersectionId = idDepot;

          for (Long newId : tournee) {
              newId = idUniqueTOIdIntersection.get(newId);
             
              listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId));

              prevIntersectionId = newId;
          }

          listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + idDepot));

          listeTrajets=listTrajets;

          //nouveaux temps de passage
          this.calculTempsDePassage();
          
          return this;

    }

    /**
     * Recherche de la tournée la plus rapide
     *
     * @param planning
     * @return
     */
    public Planning calculerTournee() {

        // Recherche des chemins des plus courts entre tous les points (dépots,
        // livraisons et dépot)
        carte.calculerLesCheminsLesPlusCourts(requetes);
        
        generateNewId();
        
        //recherche de la melleure tournéee
        tournee = carte.trouverMeilleureTournee(requetes);
        
        Long prevIntersectionId =idDepot;
        List<Trajet> listTrajets = new LinkedList<Trajet>();

        for (Long newId : tournee) {
            newId = idUniqueTOIdIntersection.get(newId);
            listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + newId));
            prevIntersectionId = newId;
        }

        listTrajets.add(carte.cheminsLesPlusCourts.get(prevIntersectionId + "|" + idDepot));
        
        listeTrajets=listTrajets;

        //nouveaux temps de passage
        this.calculTempsDePassage();
        
        return this;

    }


    public List<Trajet> getListeTrajets() {
        return listeTrajets;
    }

    public void setListeTrajets(List<Trajet> listeTrajets) {
        this.listeTrajets = listeTrajets;
    }

    public Map<Long, Date> getDatesPassage() {
        return datesPassage;
    }

    public void setDatesPassage(Map<Long, Date> datesPassage) {
        this.datesPassage = datesPassage;
    }

    public Map<Long, Date> getDatesSorties() {
        return datesSorties;
    }

    public void setDatesSorties(Map<Long, Date> datesSorties) {
        this.datesSorties = datesSorties;
    }

    public double getDureeTotale() {
        return dureeTotale;
    }

    public void setDureeTotale(double dureeTotale) {
        this.dureeTotale = dureeTotale;
    }

    public List<Requete> getRequetes() {
        return this.requetes;
    }

    public Long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(Long newIdDepot) {
    	carte.setDepotId(newIdDepot);
        this.idDepot = newIdDepot;
    }

    public Long getIdUniqueDepot() {
        return this.idUniqueDepot;
    }

    public void setIdUniqueDepot(Long newIdUniqueDepot) {
        this.idUniqueDepot = newIdUniqueDepot;
    }

    public Date getDateDebut() {
        return this.dateDebut;
    }

    public void setDateDebut(Date newDateDebut) {
        this.dateDebut = newDateDebut;
    }

    public void setRequetes(List<Requete> newRequetes) {
        this.requetes = newRequetes;
    }

    public List<Long> getTournee() {
		return tournee;
	}

	public void setTournee(List<Long> tournee) {
		this.tournee = tournee;
	}
	public void setCarte(Carte carte) {
		this.carte = carte;
	}

	public static Map<Long, Long> getIdUniqueTOIdIntersection() {
		return idUniqueTOIdIntersection;
	}

	public static void setIdUniqueTOIdIntersection(Map<Long, Long> idUniqueTOIdIntersection) {
		Planning.idUniqueTOIdIntersection = idUniqueTOIdIntersection;
	}

	public Long getIdUniqueMax() {
		return idUniqueMax;
	}

	public void setIdUniqueMax(Long idUniqueMax) {
		this.idUniqueMax = idUniqueMax;
	}
}
