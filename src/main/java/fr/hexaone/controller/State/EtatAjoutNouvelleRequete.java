package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.AjouterDemandeCommand;
import fr.hexaone.controller.Command.AjouterRequeteCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.TypeIntersection;
import fr.hexaone.utils.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

    /**
     * L'id de l'intersection premièrement sélectionné
     */
    private Long idIntersection1 = null;

    /**
     * L'id de l'intersection deuxièmement sélectionné
     */
    private Long idIntersection2 = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {

        // c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();

        c.getFenetre().getFenetreControleur().getPickUpDurationField().clear();
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().clear();

        c.getFenetre().getFenetreControleur().getDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setVisible(false);

        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(false);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(false);

        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(false);
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
    public void valider(Controleur c, String... durations) {

        String pickUpDurationField = durations[0];
        String deliveryDurationField = durations[1];

        /*
         * for(String d : durations) {
         * 
         * if(compteur==0) { pickUpDurationField=d;
         * 
         * }
         * 
         * if(compteur==1) { deliveryDurationField=d; }
         * 
         * }
         */

        // Erreurs de saisies et sélections
        TypeIntersection typeIntersection = null;
        if (idIntersection1 == null && idIntersection2 == null) {
            System.out.println("Il faut sélectionner au moins une intersection.");
            Utils.alertHelper(this,"Mauvaise sélection", "Il faut selectionner au moins une intersection.",
                    Alert.AlertType.ERROR);
            return;
        } else if (idIntersection1 != null && idIntersection2 != null) {
            if (pickUpDurationField.isEmpty()) {
                System.out.println("Le champ concernant la durée de Collecte est vide !");
                Utils.alertHelper(this,"Mauvaise saisie de durée", "Le champ concernant la durée de Collecte est vide !",
                        Alert.AlertType.ERROR);
                return;
            }
            if (deliveryDurationField.isEmpty()) {
                System.out.println("Le champ concernant la durée de Livraison est vide !");
                Utils.alertHelper(this,"Mauvaise saisie de durée", "Le champ concernant la durée de Livraison est vide !",
                        Alert.AlertType.ERROR);
                return;
            }
        } else if (idIntersection2 == null) {
            if (!pickUpDurationField.isEmpty() && !deliveryDurationField.isEmpty()) {
                System.out.println("Il faut sélectionner deux intersections si l'on renseigne deux durées.");
                Utils.alertHelper(this,"Mauvaise sélection",
                        "Il faut selectionner deux intersections si l'on renseigne deux durées.",
                        Alert.AlertType.ERROR);
                return;
            } else if (deliveryDurationField.isEmpty()) {
                typeIntersection = TypeIntersection.COLLECTE;
                if (!verifieDureeUtilisateur(pickUpDurationField)) {
                    System.out.println("La durée ne doit contenir que des chiffres !");
                    Utils.alertHelper(this,"Mauvaise saisie de durée",
                            "La durée de collecte (en seconde) ne doit contenir que des chiffres !",
                            Alert.AlertType.ERROR);
                    return;
                }
            } else {
                typeIntersection = TypeIntersection.LIVRAISON;
                if (!verifieDureeUtilisateur(deliveryDurationField)) {
                    System.out.println("La durée ne doit contenir que des chiffres !");
                    Utils.alertHelper(this,"Mauvaise saisie de durée",
                            "La durée de livraison (en seconde) ne doit contenir que des chiffres !",
                            Alert.AlertType.ERROR);
                    return;
                }
            }
        }

        // Gestion de l'ajout
        try {
            if (typeIntersection == null) {

                if (!verifieDureeUtilisateur(pickUpDurationField) || !verifieDureeUtilisateur(deliveryDurationField)) {
                    System.out.println("Les durées ne doivent contenir que des chiffres !");
                    Utils.alertHelper(this,"Mauvaise saisie de durée",
                            "Les durées (en secondes) ne doivent contenir que des chiffres !", Alert.AlertType.ERROR);
                    return;
                }

                // Pickup & Delivery
                String nomPickup = "";
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection1)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomPickup = s.getNom();
                        break;
                    }
                }
                String nomDelivery = "";
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection2)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomDelivery = s.getNom();
                        break;
                    }
                }

                Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(pickUpDurationField), nomPickup,
                        idIntersection2, Integer.parseInt(deliveryDurationField), nomDelivery);
                if ( !c.getListOfCommands().add(new AjouterRequeteCommand(c.getPlanning(), nouvelleRequete))) {
                    Alert messageAlerte = new Alert(AlertType.INFORMATION);
                    messageAlerte.setTitle("Information");
                    messageAlerte.setHeaderText(null);
                    messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                    messageAlerte.showAndWait();
                }

            } else {
                // Pickup or Delivery
                String nom = null;
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection1)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nom = s.getNom();
                        break;
                    }
                }

                Demande nouvelleDemande;
                if (typeIntersection == TypeIntersection.COLLECTE) {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(pickUpDurationField), nom,
                            TypeIntersection.COLLECTE);
                    nouvelleDemande = nouvelleRequete.getDemandeCollecte();
                } else {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(deliveryDurationField), nom,
                            TypeIntersection.LIVRAISON);
                    nouvelleDemande = nouvelleRequete.getDemandeLivraison();
                }
                if ( !c.getListOfCommands().add(new AjouterDemandeCommand(c.getPlanning(), nouvelleDemande))) {
                    Alert messageAlerte = new Alert(AlertType.INFORMATION);
                    messageAlerte.setTitle("Information");
                    messageAlerte.setHeaderText(null);
                    messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                    messageAlerte.showAndWait();
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Les durées (en seconde) saisies sont incorrectes !");
            Utils.alertHelper(this,"Mauvaise saisie de durée", "Les durées (en seconde) saisies sont incorrectes !", Alert.AlertType.ERROR);
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
        idIntersection1 = null;
        idIntersection2 = null;
        c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatTourneeCalcule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerDemande(Controleur c, Demande demandeSelectionnee) {
        if (c.getDemandeSelectionnee() == demandeSelectionnee)
            c.setDemandeSelectionnee(null);
        else
            c.setDemandeSelectionnee(demandeSelectionnee);
        c.rafraichirVues(false);
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
