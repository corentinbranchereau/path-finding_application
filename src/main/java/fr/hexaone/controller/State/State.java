package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ListOfCommands;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.utils.DTDType;
import fr.hexaone.utils.Utils;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Interface implémentant le design pattern STATE pour la gestion des évènements
 * du controleur
 * 
 * @author HexaOne
 * @version 1.0
 */
public interface State {

    /**
     * Cette méthode permet d'initaliser l'état
     */
    void init(Controleur c);

    /**
     * Annuler la dernière commande (design pattern COMMAND) via un undo
     */
    default void undo(ListOfCommands l) {
        l.undo();
        System.out.println("UNDO - [default state implementation]");
    }

    /**
     * Rétablir la dernière commande (design pattern COMMAND) via un redo
     */
    default void redo(ListOfCommands l) {
        l.redo();
        System.out.println("REDO - [default state implementation]");
    }

    /**
     * Cette méthode permet de charger et d'afficher une carte
     */
    default void chargerCarte(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath(), DTDType.CARTE);
                Carte nouvelleCarte = new Carte();
                Planning nouveauPlanning = new Planning(nouvelleCarte);
                c.setPlanning(nouveauPlanning);
                XMLDeserializer.loadCarte(c.getPlanning().getCarte(), xmlCarte);

                // On réinitialise le zoom et la taille de la zone de dessin avant d'afficher la
                // carte
                c.getFenetre().resetZoom();
                c.getFenetre().resetTailleFenetre();

                c.getFenetre().getVueGraphique().calculAdaptationCarte(c.getPlanning().getCarte());

                // On adapte la taille de la fenêtre en fonction de la taille finale de la carte
                c.getFenetre().adapterTailleFenetre();

                c.setEtatCarteChargee();
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier carte : " + e);
                Utils.alertHelper(this,"Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier carte, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
                Utils.alertHelper(this,"Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
                Utils.alertHelper(this,"Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
                Utils.alertHelper(this,"Erreur de DTD", "Le fichier XML ne respecte pas son DTD, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
                Utils.alertHelper(this,"Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper(this,"Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de dtd pour l'inclusion : " + e);
                Utils.alertHelper(this,"Erreur d'inclusion DTD",
                        "Inclusion du DTD de vérification dans le fichier XML, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            }
        } else {
            System.out.println("Aucun fichier n'a été sélectionné");
        }
    }

    /**
     * Cette méthode permet de charger et d'afficher des requêtes
     */
    default void chargerRequetes(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                if (c.getPlanning() != null)
                    c.getPlanning().reinitialiserPlanning();
                c.reinitialiserCommandes();

                Document xmlRequete = xmlFileOpener.open(fichier.getAbsolutePath(), DTDType.REQUETE);
                XMLDeserializer.loadRequete(xmlRequete, c.getPlanning());

                // On génère des couleurs pour les requêtes
                c.getFenetre().getVueGraphique().genererCouleursRequetes(c.getPlanning().getRequetes());

                // TODO : effacer les trajets (la tournée) s'ils existent (si jamais
                // précédemment calculé)

                c.rafraichirVues(false);

                c.setEtatRequetesChargees();
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de requêtes : " + e);
                Utils.alertHelper(this,"Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier de requêtes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
                Utils.alertHelper(this,"Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
                Utils.alertHelper(this,"Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
                Utils.alertHelper(this,"Erreur de DTD", "Le fichier XML ne respecte pas son DTD, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
                Utils.alertHelper(this,"Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper(this,"Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (RequestOutOfMapException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper(this,"Erreur requête hors de la carte",
                        "Une ou plusieurs requête(s) chargées ne sont pas dans la carte, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de dtd pour l'inclusion : " + e);
                Utils.alertHelper(this,"Erreur d'inclusion DTD",
                        "Inclusion du DTD de vérification dans le fichier XML, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            }
        } else {
            System.out.println("Aucun fichier n'a été sélectionné");
        }
    }

    /**
     * Cette méthode permet de calculer le planning pour les requêtes actuelles
     */
    default void lancerCalcul(Controleur c) {
        System.out.println("handleClicBoutonCalcul [default state implementation]");
    }

    default void supprimerDemande(Controleur c, Demande demande) {
        System.out.println("Il faut avoir calculé la tournée pour supprimer des demandes");
    }

    default void modifierDemande(Controleur c, Demande d) {
        System.out.println("Il faut avoir calculé la tournée et sélectionner une demande pour la modifier");

    }

    default void supprimerRequete(Controleur c, Requete requete) {
        System.out.println("Il faut avoir calculé la tournée pour supprimer des requetes");
    }

    /**
     * Cette méthode quitte l'application
     */
    default void quitterApplication(Controleur c) {
        Platform.exit();
    }

    /**
     * Cette méthode permet de passer en mode d'ajout d'une nouvelle requête
     */
    default void ajoutNouvelleRequete(Controleur c) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Demande nouvelle livraison");
        alert.setHeaderText(null);
        alert.setContentText("Vous devez avoir un planning determiné avant de pouvoir rajouter d'autres requêtes");
        alert.show();
    }

    /**
     * Cette méthode permet de sélectionner une intersection
     */
    default void selectionnerIntersection(Controleur c, Long idIntersection) {
        System.out.println("selectionnerIntersection [default state implementation]");
    }

    /**
     * Cette méthode permet de valider un choix
     */
    default void valider(Controleur c, String... durations) {
        System.out.println("valider [default state implementation]");
    }

    /**
     * Cette méthode permet d'annuler un choix
     */
    default void annuler(Controleur c) {
        System.out.println("annuler [default state implementation]");
    }

    /**
     * Cette méthode permet de modifier l'ordre des demandes dans le planning
     */
    default void modifierPlanning(Controleur c, int i, int j) {
        System.out.println("modifierPlanning [default state implementation]");
    }

    /**
     * Cette méthode permet d'afficher l'aide à l'utilisateur
     */
    default void aide(Controleur c){
        c.getFenetre().afficherAide();
    }

    default void selectionnerDemande(Controleur c, Demande demandeSelectionnee) {
        System.out.println("selectionnerDemande [default state implementation]");
    }
}
