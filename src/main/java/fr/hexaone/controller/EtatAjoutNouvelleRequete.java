package fr.hexaone.controller;

import fr.hexaone.model.Requete;
import fr.hexaone.model.Trajet;
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

    private Long idPickup = null;
    private Long idDelivery = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
        if(idPickup!=null && idPickup.equals(idIntersection)){
            c.getFenetre().getVueGraphique().deselectionneIntersection(idIntersection);
            idPickup = null;
        } else if (idDelivery!=null && idDelivery.equals(idIntersection)){
            c.getFenetre().getVueGraphique().deselectionneIntersection(idIntersection);
            idDelivery = null;
        } else if(idPickup == null){
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            idPickup = idIntersection;
        } else if(idDelivery == null){
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            idDelivery = idIntersection;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String pickUpDurationField, String deliveryDurationField) {
        if (pickUpDurationField.isEmpty()) {
            System.out.println("Le champ concernant la durée de Collecte est vide !");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise saisie de durée");
            alert.setHeaderText(null);
            alert.setContentText("Le champ concernant la durée de Collecte est vide !");
            alert.show();
            return;
        }
        if (deliveryDurationField.isEmpty()) {
            System.out.println("Le champ concernant la durée de Livraison est vide !");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise saisie de durée");
            alert.setHeaderText(null);
            alert.setContentText("Le champ concernant la durée de Livraison est vide !");
            alert.show();
            return;
        }
        // Regex to check string contains only digits
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        Matcher matcherPickUp = p.matcher(pickUpDurationField);
        Matcher matcherDelivery = p.matcher(deliveryDurationField);
        // If the input doesn't contain ONLY digits then we alert the user
        if (!matcherPickUp.matches() || !matcherDelivery.matches()) {
            System.out.println("Les durées ne doivent contenir que des chiffres !");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise saisie de durée");
            alert.setHeaderText(null);
            alert.setContentText("Les durées (en secondes) ne doivent contenir que des chiffres !");
            alert.show();
            return;
        }

        if(idPickup==null || idDelivery ==null){
            System.out.println("Il faut sélectionner deux intersections.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner deux intersections.");
            alert.show();
            return;
        }

        Requete nouvelleRequete = new Requete(idPickup, Integer.parseInt(pickUpDurationField), idDelivery, Integer.parseInt(deliveryDurationField));
        c.getPlanning().ajouterRequete(nouvelleRequete);
        for (Trajet trajet : c.getPlanning().getListeTrajets()) {
            Color couleur = Color.color(Math.random(), Math.random(), Math.random());
            c.getFenetre().getVueGraphique().afficherTrajet(c.getCarte(), trajet, couleur);
        }
        c.getFenetre().getVueTextuelle().afficherPlanning(c.getPlanning(), c.getCarte(),
                c.getFenetre().getMapCouleurRequete());

        this.annuler(c);

        //TODO : Ajouter notre nouvelle requête à l'observable liste de Corentin
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setDisable(true);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setDisable(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setDisable(true);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setDisable(true);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().clear();
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().clear();
        idPickup = null;
        idDelivery = null;
        c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatCourant(c.etatTourneeCalcule);
    }

    /**
     * Setter de l'idPickup
     * @param idPickup
     */
    public void setIdPickup(Long idPickup) {
        this.idPickup = idPickup;
    }

    /**
     * Setter de l'idDelivery
     * @param idDelivery
     */
    public void setIdDelivery(Long idDelivery) {
        this.idDelivery = idDelivery;
    }
}
