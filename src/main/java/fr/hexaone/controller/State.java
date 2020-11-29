package fr.hexaone.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.IllegalAttributException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
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
     * Cette méthode permet de charger et d'afficher une carte
     */
    default void chargerCarte(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath());
                c.setCarte(new Carte());
                XMLDeserializer.loadCarte(c.getCarte(), xmlCarte);

                // On réinitialise le zoom et la taille de la zone de dessin avant d'afficher la
                // carte
                c.getFenetre().resetZoom();
                c.getFenetre().resetTailleFenetre();

                c.getFenetre().getVueGraphique().afficherCarte(c.getCarte());

                // On adapte la taille de la fenêtre en fonction de la taille finale de la carte
                c.getFenetre().adapterTailleFenetre();

                c.setEtatCourant(c.etatCarteChargee);
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
                c.setPlanning(new Planning());
                XMLDeserializer.loadRequete(xmlRequete, c.getPlanning());

                // On affiche requêtes chargées dans la vue graphique et la vue textuelle
                c.getFenetre().getVueGraphique().afficherRequetes(c.getPlanning(), c.getCarte(),
                        c.getFenetre().getMapCouleurRequete());
                c.getFenetre().getVueTextuelle().afficherRequetes(c.getPlanning(), c.getCarte(),
                        c.getFenetre().getMapCouleurRequete());

                c.setEtatCourant(c.etatRequetesChargees);
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

    /**
     * Cette méthode quitte l'application
     */
    default void quitterApplication(Controleur c) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quitter l'application ?");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir quitter l'application ?");

        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK) {
            Platform.exit();
        } else {
            // Rien
        }
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
    default void selectionnerIntersection(Controleur c) {
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
}
