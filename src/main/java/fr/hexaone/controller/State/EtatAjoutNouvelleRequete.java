package fr.hexaone.controller.State;

import fr.hexaone.controller.Controleur;
import fr.hexaone.controller.Command.AjouterRequeteCommand;
import fr.hexaone.model.*;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on
 * souhaite demander une nouvelle livraison et saisir les durées
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatAjoutNouvelleRequete implements State {

    private Long idIntersection1 = null;
    private Long idIntersection2 = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {

        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();

        c.getFenetre().getFenetreControleur().getPickUpDurationField().clear();
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().clear();

        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(false);

        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonSupprimerRequete().setVisible(false);
        c.getFenetre().getFenetreControleur().getboutonModifierPlanning().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(true);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(true);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(true);
        
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
        if (idIntersection1 != null && idIntersection1.equals(idIntersection)) {
            c.getFenetre().getVueGraphique().deselectionneIntersection(idIntersection);
            idIntersection1 = null;
        } else if (idIntersection2 != null && idIntersection2.equals(idIntersection)) {
            c.getFenetre().getVueGraphique().deselectionneIntersection(idIntersection);
            idIntersection2 = null;
        } else if (idIntersection1 == null) {
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            idIntersection1 = idIntersection;
        } else if (idIntersection2 == null) {
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            idIntersection2 = idIntersection;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String pickUpDurationField, String deliveryDurationField) {
        //Erreurs de saisies et sélections
        TypeIntersection typeIntersection = null;
        if(idIntersection1 == null && idIntersection2 == null) {
            System.out.println("Il faut sélectionner au moins une intersection.");
            alertHelper("Mauvaise sélection", "Il faut selectionner au moins une intersection.", Alert.AlertType.ERROR);
            return;
        } else if(idIntersection1!=null && idIntersection2!=null) {
            if (pickUpDurationField.isEmpty()) {
                System.out.println("Le champ concernant la durée de Collecte est vide !");
                alertHelper("Mauvaise saisie de durée", "Le champ concernant la durée de Collecte est vide !", Alert.AlertType.ERROR);
                return;
            }
            if (deliveryDurationField.isEmpty()) {
                System.out.println("Le champ concernant la durée de Livraison est vide !");
                alertHelper("Mauvaise saisie de durée", "Le champ concernant la durée de Livraison est vide !", Alert.AlertType.ERROR);
                return;
            }
        } else if(idIntersection2==null){
            if(!pickUpDurationField.isEmpty() && !deliveryDurationField.isEmpty()){
                System.out.println("Il faut sélectionner deux intersections si l'on renseigne deux durées.");
                alertHelper("Mauvaise sélection", "Il faut selectionner deux intersections si l'on renseigne deux durées.", Alert.AlertType.ERROR);
                return;
            } else if(deliveryDurationField.isEmpty()){
                typeIntersection = TypeIntersection.COLLECTE;
                if(!verifieDureeUtilisateur(pickUpDurationField)){
                    System.out.println("La durée ne doit contenir que des chiffres !");
                    alertHelper("Mauvaise saisie de durée", "La durée de collecte (en seconde) ne doit contenir que des chiffres !", Alert.AlertType.ERROR);
                    return;
                }
            } else {
                typeIntersection = TypeIntersection.LIVRAISON;
                if(!verifieDureeUtilisateur(deliveryDurationField)){
                    System.out.println("La durée ne doit contenir que des chiffres !");
                    alertHelper("Mauvaise saisie de durée", "La durée de livraison (en seconde) ne doit contenir que des chiffres !", Alert.AlertType.ERROR);
                    return;
                }
            }
        }

        //Gestion de l'ajout
        try {
            if (typeIntersection == null) {

                if (!verifieDureeUtilisateur(pickUpDurationField) || !verifieDureeUtilisateur(deliveryDurationField)) {
                    System.out.println("Les durées ne doivent contenir que des chiffres !");
                    alertHelper("Mauvaise saisie de durée", "Les durées (en secondes) ne doivent contenir que des chiffres !", Alert.AlertType.ERROR);
                    return;
                }

                //Pickup & Delivery
                String nomPickup = "";
                for (Segment s : c.getCarte().getIntersections().get(idIntersection1).getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomPickup = s.getNom();
                        break;
                    }
                }
                String nomDelivery = "";
                for (Segment s : c.getCarte().getIntersections().get(idIntersection2).getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomDelivery = s.getNom();
                        break;
                    }
                }

                Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(pickUpDurationField), nomPickup, idIntersection2, Integer.parseInt(deliveryDurationField), nomDelivery);
                AjouterRequeteCommand ajouterRequeteCommand = new AjouterRequeteCommand(c.getPlanning(), nouvelleRequete);
                ajouterRequeteCommand.doCommand();
                //c.getFenetre().getVueGraphique().afficherNouvelleRequete(c.getCarte(), nouvelleRequete, c.getFenetre().getMapCouleurRequete());

            } else {
                //Pickup or Delivery
                String nom = null;
                for (Segment s : c.getCarte().getIntersections().get(idIntersection1).getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nom = s.getNom();
                        break;
                    }
                }

                Demande nouvelleDemande;
                if (typeIntersection == TypeIntersection.COLLECTE) {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(pickUpDurationField), nom, TypeIntersection.COLLECTE);
                    nouvelleDemande = nouvelleRequete.getDemandeCollecte();
                } else {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(deliveryDurationField), nom, TypeIntersection.LIVRAISON);
                    nouvelleDemande = nouvelleRequete.getDemandeLivraison();
                }
                c.getPlanning().ajouterDemande(nouvelleDemande);

                c.getFenetre().getVueGraphique().afficherNouvelleDemande(c.getCarte(), nouvelleDemande, c.getFenetre().getMapCouleurRequete());
            }

            c.getFenetre().getVueGraphique().effacerTrajets();
            for (Trajet trajet : c.getPlanning().getListeTrajets()) {
                Color couleur = Color.color(Math.random(), Math.random(), Math.random());
                c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
            }

            c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte());

        } catch (NumberFormatException e){
            System.out.println("Les durées (en seconde) saisies sont incorrectes !");
            alertHelper("Mauvaise saisie de durée", "Les durées (en seconde) saisies sont incorrectes !", Alert.AlertType.ERROR);
            return;
        } finally {
            this.annuler(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        // idIntersection1 = null;
        // idIntersection2 = null;
        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatTourneeCalcule();
    }

    /**
     * Setter de l'idIntersection1
     * 
     * @param idIntersection1
     */
    public void setIdIntersection1(Long idIntersection1) {
        this.idIntersection1 = idIntersection1;
    }

    /**
     * Setter de l'idIntersection2
     * 
     * @param idIntersection2
     */
    public void setIdIntersection2(Long idIntersection2) {
        this.idIntersection2 = idIntersection2;
    }

    /**
     * Gère l'affichage de messages d'alertes dans l'application
     * @param title Le titre de l'alerte
     * @param message Le message contenu dans l'alerte
     * @param alertType Le type d'alerte souhaité
     */
    public void alertHelper(String title, String message, Alert.AlertType alertType){
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
    public boolean verifieDureeUtilisateur(String durationField){
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(durationField);
        return matcher.matches();
    }


}
