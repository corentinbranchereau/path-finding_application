package fr.hexaone.controller;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.hexaone.model.Carte;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import fr.hexaone.view.Fenetre;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controleur du modèle MVC, centralisant les différents éléments d'interactions
 * avec vue et modèle
 * 
 * @author HexaOne
 * @version 1.0
 */
public class Controleur {

    /**
     * Gère l'affichage de l'application (Vue du MVC)
     */
    protected Fenetre fenetre;

    /**
     * Constructeur de Controleur. Instancie la fenêtre de l'application et
     * l'affiche à l'écran.
     * 
     * @param stage Conteneur principal des éléments graphiques de la fenêtre
     */
    public Controleur(Stage stage) {
        this.fenetre = new Fenetre(stage, this);
        this.fenetre.dessinerFenetre();
    }

    /**
     * Méthode gérant le clic sur l'item "Charger une carte" du menu. La méthode
     * permet de choisir un fichier carte au format XML et l'affiche dans la vue
     * graphique de l'application
     */
    public void handleClicChargerCarte() {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(this.fenetre.getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath());
                Carte carte = new Carte();
                XMLDeserializer.loadCarte(carte, xmlCarte);

                this.fenetre.getVueGraphique().afficherCarte(carte);
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
     * Méthode gérant le clic sur l'item "Charger des requêtes" du menu
     */
    public void handleClicChargerRequetes() {
        System.out.println("Charger requêtes");
    }

    /**
     * Méthode gérant le clic sur l'item "Quitter" du menu
     */
    public void handleClicQuitter() {
        System.out.println("Quitter");
    }

    /**
     * Méthode gérant le clic sur le bouton lançant le calcul du planning
     */
    public void handleClicBoutonCalcul() {
        System.out.println("Lancement du calcul");
    }

    /**
     * Renvoie la fenêtre de l'application.
     * 
     * @return La fenêtre de l'application
     */
    public Fenetre getFenetre() {
        return fenetre;
    }
}
