package fr.hexaone.controller.State;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import fr.hexaone.controller.Command.ListOfCommands;
import fr.hexaone.controller.Controleur;
import fr.hexaone.utils.exception.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

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
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath());
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
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
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
                Document xmlRequete = xmlFileOpener.open(fichier.getAbsolutePath());
                XMLDeserializer.loadRequete(xmlRequete, c.getPlanning());

                // On génère des couleurs pour les requêtes
                c.getFenetre().getVueGraphique().genererCouleursRequetes(c.getPlanning().getRequetes());

                // TODO : effacer les trajets (la tournée) s'ils existent (si jamais
                // précédemment calculé)

                // TODO : enlever la ligne du dessous quand la méthode rafraichir de la vue
                // textuelle sera prête
                c.getFenetre().getVueTextuelle().afficherRequetes(c.getPlanning(), c.getPlanning().getCarte(),
                        c.getFenetre().getMapCouleurRequete());

                c.setEtatRequetesChargees();
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de requêtes : " + e);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
            } catch (RequestOutOfMapException e) {
                System.out.println(e.getMessage());
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

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            // Rien
        } else {
            // Rien
        }
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
    default void valider(Controleur c, String pickUpDurationField, String deliveryDurationField) {
        System.out.println("valider [default state implementation]");
    }

    /**
     * Cette méthode permet d'annuler un choix
     */
    default void annuler(Controleur c) {
        System.out.println("annuler [default state implementation]");
    }

    /**
     * méthode qui permet de passer dans l'état de modification de planning
     */
    default void modifierPlanning(Controleur c,int i, int j) {
        System.out.println("modifierPlanning [default state implementation]");
    }
}
