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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
                Utils.alertHelper("Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier carte, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
                Utils.alertHelper("Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
                Utils.alertHelper("Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
                Utils.alertHelper("Erreur de DTD", "Le fichier XML ne respecte pas son DTD, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
                Utils.alertHelper("Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper("Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de dtd pour l'inclusion : " + e);
                Utils.alertHelper("Erreur d'inclusion DTD",
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
                Utils.alertHelper("Erreur d'ouverture",
                        "Erreur lors de l'ouverture du fichier de requêtes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
                Utils.alertHelper("Erreur d'extension",
                        "Le fichier sélectionné n'est pas de type XML, veuillez recommencer !", Alert.AlertType.ERROR);
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
                Utils.alertHelper("Erreur de fichier XML",
                        "Le fichier sélectionné possèdes erreurs internes, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (DTDValidationException e) {
                System.out.println("Le fichier XML ne respecte pas son DTD");
                Utils.alertHelper("Erreur de DTD", "Le fichier XML ne respecte pas son DTD, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (IllegalAttributException e) {
                System.out.println("Le fichier XML contient un attribut de type incohérent");
                Utils.alertHelper("Erreur de type",
                        "Le fichier XML contient un attribut de type incohérent, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (BadFileTypeException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper("Erreur de type",
                        "Le fichier XML n'est pas cohérent avec le type demandé, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (RequestOutOfMapException e) {
                System.out.println(e.getMessage());
                Utils.alertHelper("Erreur requête hors de la carte",
                        "Une ou plusieurs requête(s) chargées ne sont pas dans la carte, veuillez recommencer !",
                        Alert.AlertType.ERROR);
            } catch (URISyntaxException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de dtd pour l'inclusion : " + e);
                Utils.alertHelper("Erreur d'inclusion DTD",
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
    default void aide(){
        Alert alert = new Alert(AlertType.INFORMATION);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/main/resources/logo-hexa.png"));
        alert.getDialogPane().setMaxWidth(550D);
        alert.setTitle("Aide de l'application");
        alert.setHeaderText("Bienvenue sur l'aide de l'application いちONE, développée par l'HexaOne !");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Chargement des fichiers XML"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Afin de charger une carte et des demandes de livraison, il faut se rendre dans le menu Fichier en haut à gauche de l'application et choisir l'option adaptée."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Calculer une tournée"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Une fois une carte et des demandes de livraison chargées, il est possible de calculer une tournée en utilisant le bouton \"Lancer le calcul\" disponible sur la vue textuelle de l'application."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Modifier l'ordre d'une tournée"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Lorsque la tournée est calculée, il est possible de modifier l'ordre d'une tournée en modifiant l'ordre de passage à un point de collecte ou de livraison. Pour ce faire, il suffit de faire un drag'n'drop (sélection du point à déplacer avec la souris en maintenant le clic, tout en le déplaçant vers le bon emplacement) depuis la vue textuelle."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Modifier la durée d'un évènement"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Lorsque que la tournée est calculée, il est possible de modifier la durée d'un évènement sur un point de collecte ou de livraison en cliquant droit sur le point depuis la vue textuelle et en choisissant l'option adéquate."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Ajouter une demande de livraison"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Lorsque la tournée est calculée, il est possible d'ajouter une demande de livraison, partielle ou non, grâce au bouton \"Ajouter une demande de livraison\" depuis la vue textuelle. Ensuite, sélectionner une ou deux intersection(s) sur la carte et remplissez le ou les champ(s) de durée qu'il vous faut. Enfin, valider ou annuler votre saisie."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Supprimer une demande de livraison"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Lorsque que la tournée est calculée, il est possible de supprimer une demande de livraison, de façon partielle ou non, en cliquant droit sur la collecte ou la livraison depuis la vue textuelle et en choisissant l'option adéquate."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Annuler ou rejouer une action"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide("Vous avez commis une erreur lors de l'amélioration d'une tournée calculée ? Aucun problème ! Il est possible d'annuler et de rejouer chaque action avec les raccourcis clavier CTRL+Z (undo) et CTRL+Y (redo), ou en passant par le menu \"Edition\""));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        alert.getDialogPane().setContent(vBox);

        alert.show();
    }
}
