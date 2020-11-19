package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;

import fr.hexaone.view.VueGraphique;
import fr.hexaone.view.VueTextuelle;
import javafx.application.Application;
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

public class Fenetre extends Application {

    /**
     * Vue graphique de la fenêtre
     */
    protected VueGraphique vueGraphique;

    /**
     * Vue textuelle de la fenêtre
     */
    protected VueTextuelle vueTextuelle;

    /**
     * Controleur nécessaire pour le fonctionnement de JavaFX
     */
    protected FenetreControleurFXML fxControleur;

    /**
     * Cette méthode charge le fichier FXML (JavaFX) contenant les différents objets
     * graphiques, ajoute les handlers à ces derniers puis affiche la fenêtre de
     * l'application
     * 
     * @param stage Variable propre à JavaFX permettant d'afficher les éléments à
     *              l'écran
     */
    @Override
    public void start(Stage stage) {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            FileInputStream inputFichierFxml = new FileInputStream("src/main/java/fr/hexaone/view/fenetre.fxml");
            Parent root = (Parent) loader.load(inputFichierFxml);

            // Récupération du controleur FXML
            fxControleur = (FenetreControleurFXML) loader.getController();

            // Affichage de la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Définition des handlers sur les éléments du menu
            fxControleur.chargerMapItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicChargerCarte();
                }
            });

            fxControleur.chargerRequetesItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicChargerRequetes();
                }
            });

            fxControleur.quitterItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicQuitter();
                }
            });
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier FXML : " + e);
        }
    }

    /**
     * Constructeur de la classe Fenetre
     */
    public Fenetre() {

    }

    /**
     * Cette méthode est le handler permettant de gérer les clics sur l'élément du
     * menu "Charger une carte"
     */
    protected void handleClicChargerCarte() {
        System.out.println("Charger carte");
    }

    /**
     * Cette méthode est le handler permettant de gérer les clics sur l'élément du
     * menu "Charger des requêtes"
     */
    protected void handleClicChargerRequetes() {
        System.out.println("Charger requêtes");
    }

    /**
     * Cette méthode est le handler permettant de gérer les clics sur l'élément du
     * menu "Quitter"
     */
    protected void handleClicQuitter() {
        System.out.println("Quitter");
    }

    /**
     * Méthode qui permet d'afficher la fenêtre JavaFX en exécutant la méthode start
     * (via l'appel à launch de JavaFX)
     * 
     * @param args Arguments obtenus lors de l'exécution de la méthode main
     */
    public void dessinerFenetre(String[] args) {
        launch(Fenetre.class, args);
    }

}
