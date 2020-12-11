package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ListOfCommands;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.utils.TypeDTD;
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
 * Interface implémentant le design pattern State pour la gestion des évènements
 * du controleur
 * 
 * @author HexaOne
 * @version 1.0
 */
public interface State {

    /**
     * Cette méthode permet d'initaliser l'état
     * 
     * @param c Le controleur MVC
     */
    void init(Controleur c);

    /**
     * Annule la dernière commande (design pattern Command) via un undo
     * 
     * @param listeCommandes La liste de commandes
     */
    default void undo(ListOfCommands listeCommandes) {
        listeCommandes.undo();
    }

    /**
     * Rétablit la dernière commande (design pattern Command) via un redo
     * 
     * @param listeCommandes la liste de commandes
     */
    default void redo(ListOfCommands listeCommandes) {
        listeCommandes.redo();
    }

    /**
     * Cette méthode permet de charger et d'afficher une carte
     * 
     * @param c Le controleur MVC
     */
    default void chargerCarte(Controleur c) {
        FileChooser selecteurFichier = new FileChooser();
        File fichier = selecteurFichier.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document documentXmlCarte = xmlFileOpener.ouvrirXml(fichier.getAbsolutePath(), TypeDTD.CARTE);
                c.setPlanning(new Planning(new Carte()));
                XMLDeserializer.chargerCarte(c.getPlanning().getCarte(), documentXmlCarte);

                // On réinitialise le zoom et la taille de la zone de dessin avant d'afficher la
                // carte
                c.getFenetre().resetZoom();
                c.getFenetre().resetTailleFenetre();

                // On calcule l'adaptation à appliquer aux coordonnées
                c.getFenetre().getVueGraphique().calculAdaptationCarte(c.getPlanning().getCarte());

                // On adapte la taille de la fenêtre en fonction de la taille finale de la carte
                c.getFenetre().adapterTailleFenetre();

                c.setEtatCarteChargee();
            } catch (IOException e) {
                Utils.afficherAlerte(this, "Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier carte, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                Utils.afficherAlerte(this, "Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                Utils.afficherAlerte(this, "Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                Utils.afficherAlerte(this, "Erreur de DTD",
                        "Le fichier XML ne respecte pas son DTD, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                Utils.afficherAlerte(this, "Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                Utils.afficherAlerte(this, "Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                Utils.afficherAlerte(this, "Erreur d'inclusion DTD",
                        "Inclusion du DTD de vérification dans le fichier XML, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Cette méthode permet de charger et d'afficher des requêtes
     * 
     * @param c Le controleur MVC
     */
    default void chargerRequetes(Controleur c) {
        FileChooser selecteurFichier = new FileChooser();
        File fichier = selecteurFichier.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                // On réinitialise le planning et les commandes
                if (c.getPlanning() != null)
                    c.getPlanning().reinitialiserPlanning();
                c.reinitialiserCommandes();

                Document documentXmlRequete = xmlFileOpener.ouvrirXml(fichier.getAbsolutePath(), TypeDTD.REQUETE);
                XMLDeserializer.chargerRequetes(documentXmlRequete, c.getPlanning());

                // On génère des couleurs pour les requêtes
                c.getFenetre().getVueGraphique().genererCouleursRequetes(c.getPlanning().getRequetes());

                c.rafraichirVues(false);

                c.setEtatRequetesChargees();
            } catch (IOException e) {
                Utils.afficherAlerte(this, "Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier de requêtes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                Utils.afficherAlerte(this, "Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                Utils.afficherAlerte(this, "Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                Utils.afficherAlerte(this, "Erreur de DTD",
                        "Le fichier XML ne respecte pas son DTD, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                Utils.afficherAlerte(this, "Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                Utils.afficherAlerte(this, "Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (RequestOutOfMapException e) {
                Utils.afficherAlerte(this, "Erreur requête hors de la carte",
                        "Une ou plusieurs requête(s) chargées ne sont pas dans la carte, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                Utils.afficherAlerte(this, "Erreur d'inclusion DTD",
                        "Inclusion du DTD de vérification dans le fichier XML, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Cette méthode permet de calculer le planning pour les requêtes actuelles
     * 
     * @param c Le controleur MVC
     */
    default void lancerCalcul(Controleur c) {
        // Aucune action par défaut
    }

    /**
     * Cette méthode supprime une demande
     * 
     * @param c       Le controleur MVC
     * @param demande La demande à supprimer
     */
    default void supprimerDemande(Controleur c, Demande demande) {
        Utils.afficherAlerte(this, "Information",
                "Il faut d'abord avoir calculé la tournée pour supprimer des demandes.", AlertType.INFORMATION);
    }

    /**
     * Cette méthode modifie une demande
     * 
     * @param c Le controleur MVC
     * @param d La demande à modifier
     */
    default void modifierDemande(Controleur c, Demande d) {
        Utils.afficherAlerte(this, "Information",
                "Il faut d'abord avoir calculé la tournée et sélectionner une demande pour la modifier.",
                AlertType.INFORMATION);
    }

    /**
     * Cette méthode supprime une requête
     * 
     * @param c       Le controleur MVC
     * @param requete La requête à supprimer
     */
    default void supprimerRequete(Controleur c, Requete requete) {
        Utils.afficherAlerte(this, "Information", "Il faut avoir calculé la tournée pour supprimer des requêtes.",
                AlertType.INFORMATION);
    }

    /**
     * Cette méthode quitte l'application
     * 
     * @param c Le controleur MVC
     */
    default void quitterApplication(Controleur c) {
        Platform.exit();
    }

    /**
     * Cette méthode permet de passer en mode d'ajout d'une nouvelle requête
     * 
     * @param c Le controleur MVC
     */
    default void ajoutNouvelleRequete(Controleur c) {
        Utils.afficherAlerte(this, "Demande nouvelle livraison",
                "Il faut d'abord avoir calculé la tournée avant de pouvoir rajouter d'autres requêtes.",
                AlertType.INFORMATION);
    }

    /**
     * Cette méthode permet de sélectionner une intersection
     * 
     * @param c              Le controleur MVC
     * @param idIntersection L'id de l'intersection à sélectionner
     */
    default void selectionnerIntersection(Controleur c, Long idIntersection) {
        // Aucune action par défaut
    }

    /**
     * Cette méthode permet de valider un choix
     * 
     * @param c      Le controleur MVC
     * @param durees Les durées à valider
     */
    default void valider(Controleur c, String... durees) {
        // Aucune action par défaut
    }

    /**
     * Cette méthode permet d'annuler un choix
     * 
     * @param c Le controleur MVC
     */
    default void annuler(Controleur c) {
        // Aucune action par défaut
    }

    /**
     * Cette méthode permet de modifier l'ordre des demandes dans le planning
     * 
     * @param c Le controleur MVC
     * @param i L'index de départ de la demande
     * @param j L'index d'arrivée de la demande
     */
    default void modifierPlanning(Controleur c, int i, int j) {
        // Aucune action par défaut
    }

    /**
     * Cette méthode permet d'afficher l'aide à l'utilisateur
     * 
     * @param c Le controleur MVC
     */
    default void aide(Controleur c) {
        c.getFenetre().afficherAide();
    }

    /**
     * Sélection d'une demande
     * 
     * @param c                   Le controleur MVC
     * @param demandeSelectionnee La demande à sélectionner
     */
    default void selectionnerDemande(Controleur c, Demande demandeSelectionnee) {
        // Aucune action par défaut
    }
}
