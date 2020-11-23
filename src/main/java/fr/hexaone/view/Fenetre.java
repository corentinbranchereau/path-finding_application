package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;

import fr.hexaone.controller.Controleur;
import fr.hexaone.view.VueGraphique;
import fr.hexaone.view.VueTextuelle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Permet d'afficher la fenêtre d'IHM.
 * 
 * @author HexaOne
 * @version 1.0
 */

public class Fenetre {

    /**
     * Vue graphique de la fenêtre
     */
    protected VueGraphique vueGraphique;

    /**
     * Vue textuelle de la fenêtre
     */
    protected VueTextuelle vueTextuelle;

    /**
     * Controleur JavaFX servant à récupérer les références des éléments du fichier
     * FXML
     */
    protected FenetreControleurFXML fenetreControleur;

    /**
     * Conteneur principal des éléments graphiques
     */
    protected Stage stage;

    /**
     * Controleur gérant la logique de l'application
     */
    protected Controleur controleur;

    /**
     * Constructeur de Fenetre
     * 
     * @param stage      le conteneur principal des éléments graphiques de
     *                   l'application
     * @param controleur le controleur de l'application
     */
    public Fenetre(Stage stage, Controleur controleur) {
        this.stage = stage;
        this.controleur = controleur;
        this.vueGraphique = new VueGraphique();
        this.vueTextuelle = new VueTextuelle();
    }

    /**
     * Méthode qui permet d'afficher une fenêtre dans l'OS à l'aide de JavaFX
     */
    public void dessinerFenetre() {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            FileInputStream inputFichierFxml = new FileInputStream("src/main/java/fr/hexaone/view/fenetre.fxml");
            Parent root = (Parent) loader.load(inputFichierFxml);

            // Récupération du controleur FXML
            fenetreControleur = (FenetreControleurFXML) loader.getController();

            // On donne le canvas à la vue graphique
            this.vueGraphique.setCanvas(this.fenetreControleur.getCanvas());

            // on donne la zone de texte à la vue textuelle
            this.vueTextuelle.setZoneTexte(this.fenetreControleur.getZoneTexte());

            // Affichage de la scène
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.setResizable(false);
            this.stage.setTitle("TITRE A DEFINIR");

            this.stage.show();

            // Définition des handlers sur les éléments du menu
            fenetreControleur.getChargerCarteItem().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.chargerCarte();
                }
            });

            fenetreControleur.getChargerRequetesItem().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.chargerRequetes();
                }
            });

            fenetreControleur.getQuitterItem().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.quitterApplication();
                }
            });

            fenetreControleur.getBoutonLancer().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.lancerCalcul();
                }
            });

        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier FXML : " + e);
        }
    }

    /**
     * Renvoie le controleur JavaFX de la fenêtre
     * 
     * @return Le controleur JavaFX de la fenêtre
     */
    public FenetreControleurFXML getFenetreControleur() {
        return fenetreControleur;
    }

    /**
     * Renvoie le conteneur principal des éléments graphiques de la fenêtre
     * 
     * @return Le conteneur graphique principal
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Renvoie la vue graphique de l'application.
     * 
     * @return La vue graphique
     */
    public VueGraphique getVueGraphique() {
        return vueGraphique;
    }

    /**
     * Renvoie la vue textuelle de l'application.
     * 
     * @return La vue textuelle
     */
    public VueTextuelle getVueTextuelle() {
        return vueTextuelle;
    }
}
