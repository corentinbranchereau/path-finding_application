package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ModifierDemandeCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.TypeIntersection;
import fr.hexaone.utils.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
     * L'id de l'intersection sélectionné
     */
    private Long idIntersection = null;

    private int duree;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().initFenetreModifierDemande(this.duree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerIntersection(Controleur c, Long idIntersection) {
        if (this.idIntersection != null && this.idIntersection.equals(idIntersection)) {
            c.getFenetre().getVueGraphique().deselectionneIntersection(this.idIntersection);
            this.idIntersection = null;
        } else if (this.idIntersection == null) {
            c.getFenetre().getVueGraphique().selectionneIntersection(idIntersection);
            this.idIntersection = idIntersection;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valider(Controleur c, String... durations) {
        // Erreurs de saisies et sélections

        String durationField = durations[0];

        TypeIntersection typeIntersection = null;
        if (idIntersection == null && durationField.isEmpty()) {
            System.out.println("Il faut sélectionner la nouvelle intersection ou modifier la durée");
            alertHelper("Mauvaise sélection", "Il faut selectionner la nouvelle intersection ou modifier la durée.",
                    Alert.AlertType.ERROR);
            return;
        } else {
            if (durationField.isEmpty()) {
                System.out.println("Le champ concernant la durée est vide !");
                alertHelper("Mauvaise saisie de durée", "Le champ concernant la durée  est vide !",
                        Alert.AlertType.ERROR);
            }

        }

        if (!verifieDureeUtilisateur(durationField)) {
            System.out.println("La durée ne doit contenir que des chiffres !");
            alertHelper("Mauvaise saisie de durée", "La durée (en seconde) ne doit contenir que des chiffres !",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            if (!c.getListOfCommands().add(new ModifierDemandeCommand(c.getPlanning(), c.getDemandeSelectionnee(),
                    Integer.parseInt(durationField), idIntersection))) {
                Alert messageAlerte = new Alert(AlertType.INFORMATION);
                messageAlerte.setTitle("Information");
                messageAlerte.setHeaderText(null);
                messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                messageAlerte.showAndWait();
            }
        }

        catch (NumberFormatException e) {
            System.out.println("Les durées (en seconde) saisies sont incorrectes !");
            Utils.alertHelper(this, "Mauvaise saisie de durée", "Les durées (en seconde) saisies sont incorrectes !",
                    Alert.AlertType.ERROR);
            return;
        }

        finally {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
            this.annuler(c);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(Controleur c) {
        idIntersection = null;
        c.getFenetre().getVueGraphique().nettoyerIntersectionsSelectionnees();
        c.setEtatTourneeCalcule();
    }

    /**
     * Setter de l'idIntersection
     * 
     * @param idIntersection
     */
    public void setIdIntersection(Long idIntersection) {
        this.idIntersection = idIntersection;
    }

    /**
     * Getter de la durée
     * 
     * @return la durée en sec
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Setter de la durée
     * 
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
