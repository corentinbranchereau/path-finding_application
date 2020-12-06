package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.AjouterDemandeCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.controller.Command.AjouterRequeteCommand;
import fr.hexaone.controller.Command.ModifierDemandeCommand;
import fr.hexaone.model.*;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on
 * souhaite modifier une demande (lieu ou/et durée)
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatModifierDemande implements State {

    /**
     * L'id de l'intersection  sélectionné
     */
    private Long idIntersection = null;
    
    
    private int duree;


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {

        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();

        //c.getFenetre().getFenetreControleur().getPickUpDurationField().clear();
        
        //c.getFenetre().getFenetreControleur().getDeliveryDurationField().clear();
        c.getFenetre().getFenetreControleur().getDurationField().clear();
        c.getFenetre().getFenetreControleur().getDurationField().setText(String.valueOf(duree));
        c.getFenetre().getFenetreControleur().getDurationField().setVisible(true);
        c.getFenetre().getFenetreControleur().getDurationLabel().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setDisable(false);
        c.getFenetre().getFenetreControleur().getDurationField().setDisable(false);

        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);

        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);

        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);
    }


	/**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
    	
    	c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
    	this.idIntersection=idIntersection;
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String... durations) {
        // Erreurs de saisies et sélections
    	
    	String durationField=durations[0];
    	
        TypeIntersection typeIntersection = null;
        if (idIntersection == null && durationField.isEmpty() ) {
            System.out.println("Il faut sélectionner la nouvelle intersection ou modifier la durée");
            alertHelper("Mauvaise sélection", "Il faut selectionner la nouvelle intersection ou modifier la durée.", Alert.AlertType.ERROR);
            return;
        }
        else {
        	 if(durationField.isEmpty()) {
            	 System.out.println("Le champ concernant la durée est vide !");
                 alertHelper("Mauvaise saisie de durée", "Le champ concernant la durée  est vide !",
                         Alert.AlertType.ERROR);
            }
            
        }
        
        if (!verifieDureeUtilisateur(durationField)) {
            System.out.println("La durée ne doit contenir que des chiffres !");
            alertHelper("Mauvaise saisie de durée",
                    "La durée (en seconde) ne doit contenir que des chiffres !",
                    Alert.AlertType.ERROR);
            return;
        }
        
        c.getListOfCommands().add(new ModifierDemandeCommand(c.getPlanning(),c.getDemandeSelectionnee(),Integer.parseInt(durationField),idIntersection));
        
        
        c.resetDemandeSelectionnee();
        
        c.getFenetre().rafraichir(c.getPlanning(),c.getDemandeSelectionnee(),false);
        
        this.annuler(c);

     }

      

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        // idIntersection1 = null;
        // idIntersection2 = null;
        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
    	idIntersection=null;
        c.setEtatTourneeCalcule();
    }

    /**
     * Setter de l'idIntersection
     * 
     * @param idIntersection1
     */
    public void setIdIntersection(Long idIntersection) {
        this.idIntersection = idIntersection;
    }
    
    /**
     * Getter de la durée
     * @return la durée en sec
     */
    public int getDuree() {
		return duree;
	}

    /**
     * Setter de la durée
     * @param duree en sec
     */
	public void setDuree(int duree) {
		this.duree = duree;
	}



    /**
     * Gère l'affichage de messages d'alertes dans l'application
     * 
     * @param title     Le titre de l'alerte
     * @param message   Le message contenu dans l'alerte
     * @param alertType Le type d'alerte souhaité
     */
    public void alertHelper(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Vérification de l'entrée utilisateur sur les durées via une REGEX
     *
     */
    public boolean verifieDureeUtilisateur(String durationField) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(durationField);
        return matcher.matches();
    }

}
