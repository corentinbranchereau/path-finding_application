package fr.hexaone.controller;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import javafx.application.Platform;
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

                c.getFenetre().getVueGraphique().afficherCarte(c.getCarte());
                c.setEtatCourant(c.etatCarteChargee);
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier carte : " + e);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
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
                XMLDeserializer.loadRequete(xmlRequete, null, c.getPlanning());

                c.getFenetre().getVueGraphique().afficherRequetes(c.getPlanning(), c.getCarte());
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de requêtes : " + e);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
            }
        } else {
            System.out.println("Aucun fichier n'a été sélectionné");
        }

        c.setEtatCourant(c.etatRequetesChargees);
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
        Platform.exit();
    }

}
