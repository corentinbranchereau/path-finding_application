package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.AjouterDemandeCommand;
import fr.hexaone.controller.Command.AjouterRequeteCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.model.TypeDemande;
import fr.hexaone.utils.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque l'on
 * souhaite demander une nouvelle livraison et saisir les durées
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatAjoutNouvelleRequete implements State {

    /**
     * L'id de la première intersection sélectionnée
     */
    private Long idIntersection1 = null;

    /**
     * L'id de la deuxième intersection sélectionnée
     */
    private Long idIntersection2 = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().initFenetreAjoutNouvelleRequete();
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
    public void valider(Controleur c, String... durees) {

        String champsDureeCollecte = durees[0];
        String champsDureeLivraison = durees[1];

        // Erreurs de saisies et sélections
        TypeDemande typeDemande = null;
        if (idIntersection1 == null && idIntersection2 == null) {
            Utils.afficherAlerte(this, "Mauvaise sélection", "Il faut sélectionner au moins une intersection.",
                    Alert.AlertType.ERROR);
            return;
        } else if (idIntersection1 != null && idIntersection2 != null) {
            if (champsDureeCollecte.isEmpty()) {
                Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                        "Le champ concernant la durée de collecte est vide !", Alert.AlertType.ERROR);
                return;
            }
            if (champsDureeLivraison.isEmpty()) {
                Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                        "Le champ concernant la durée de livraison est vide !", Alert.AlertType.ERROR);
                return;
            }
        } else if (idIntersection2 == null) {
            if (!champsDureeCollecte.isEmpty() && !champsDureeLivraison.isEmpty()) {
                Utils.afficherAlerte(this, "Mauvaise sélection",
                        "Il faut sélectionner deux intersections si l'on renseigne deux durées.",
                        Alert.AlertType.ERROR);
                return;
            } else if (champsDureeLivraison.isEmpty()) {
                typeDemande = TypeDemande.COLLECTE;
                if (!Utils.verifieDureeUtilisateur(champsDureeCollecte)) {
                    Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                            "La durée de collecte (en secondes) ne doit contenir que des chiffres !",
                            Alert.AlertType.ERROR);
                    return;
                }
            } else {
                typeDemande = TypeDemande.LIVRAISON;
                if (!Utils.verifieDureeUtilisateur(champsDureeLivraison)) {
                    Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                            "La durée de livraison (en secondes) ne doit contenir que des chiffres !",
                            Alert.AlertType.ERROR);
                    return;
                }
            }
        }

        // Gestion de l'ajout
        try {
            if (typeDemande == null) {

                if (!Utils.verifieDureeUtilisateur(champsDureeCollecte)
                        || !Utils.verifieDureeUtilisateur(champsDureeLivraison)) {
                    Utils.afficherAlerte(this, "Mauvaise saisie de durée",
                            "Les durées (en secondes) ne doivent contenir que des chiffres !", Alert.AlertType.ERROR);
                    return;
                }

                // Collecte ET livraison
                String nomCollecte = "";
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection1)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomCollecte = s.getNom();
                        break;
                    }
                }
                String nomLivraison = "";
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection2)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nomLivraison = s.getNom();
                        break;
                    }
                }

                Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(champsDureeCollecte),
                        nomCollecte, idIntersection2, Integer.parseInt(champsDureeLivraison), nomLivraison);
                if (!c.getListOfCommands()
                        .ajouterCommande(new AjouterRequeteCommand(c.getPlanning(), nouvelleRequete))) {
                    Alert messageAlerte = new Alert(AlertType.INFORMATION);
                    messageAlerte.setTitle("Information");
                    messageAlerte.setHeaderText(null);
                    messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                    messageAlerte.showAndWait();
                }

            } else {
                // Collecte OU livraison
                String nom = null;
                for (Segment s : c.getPlanning().getCarte().getIntersections().get(idIntersection1)
                        .getSegmentsArrivants()) {
                    if (!s.getNom().isEmpty()) {
                        nom = s.getNom();
                        break;
                    }
                }

                Demande nouvelleDemande;
                if (typeDemande == TypeDemande.COLLECTE) {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(champsDureeCollecte), nom,
                            TypeDemande.COLLECTE);
                    nouvelleDemande = nouvelleRequete.getDemandeCollecte();
                } else {
                    Requete nouvelleRequete = new Requete(idIntersection1, Integer.parseInt(champsDureeLivraison), nom,
                            TypeDemande.LIVRAISON);
                    nouvelleDemande = nouvelleRequete.getDemandeLivraison();
                }
                if (!c.getListOfCommands()
                        .ajouterCommande(new AjouterDemandeCommand(c.getPlanning(), nouvelleDemande))) {
                    Alert messageAlerte = new Alert(AlertType.INFORMATION);
                    messageAlerte.setTitle("Information");
                    messageAlerte.setHeaderText(null);
                    messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
                    messageAlerte.showAndWait();
                }
            }

        } catch (NumberFormatException e) {
            Utils.afficherAlerte(this, "Mauvaise saisie de durée", "Les durées (en seconde) saisies sont incorrectes !",
                    Alert.AlertType.ERROR);
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

}
