package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;

import fr.hexaone.App;
import fr.hexaone.view.VueGraphique;
import fr.hexaone.view.VueTextuelle;
import fr.hexaone.view.ButtonListener;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Permet d'afficher la fenêtre d'IHM.
 * 
 * @author HexaOne
 * @version 1.0
 */

public class Fenetre extends Application {

    protected VueGraphique vueGraphique;
    protected VueTextuelle vueTextuelle;
    protected FenetreControleurFXML controleur;

    @Override
    public void start(Stage stage) {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            FileInputStream inputFichierFxml = new FileInputStream("src/main/java/fr/hexaone/view/fenetre.fxml");
            Parent root = (Parent) loader.load(inputFichierFxml);

            // Récupération du controleur FXML
            controleur = (FenetreControleurFXML) loader.getController();

            // Affichage de la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Définition des handlers sur les éléments du menu
            controleur.chargerMapItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicChargerCarte();
                }
            });

            controleur.chargerRequetesItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicChargerRequetes();
                }
            });

            controleur.quitterItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    handleClicQuitter();
                }
            });
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier FXML : " + e);
        }
    }

    public Fenetre() {

    }

    protected void handleClicChargerCarte() {
        System.out.println("Charger carte");
    }

    protected void handleClicChargerRequetes() {
        System.out.println("Charger requêtes");
    }

    protected void handleClicQuitter() {
        System.out.println("Quitter");
    }

    /**
     * Méthode qui permet d'afficher une fenêtre dans l'os à l'aide de javaFX
     */
    public void dessinerFenetre(String[] args) {
        launch(Fenetre.class, args);
    }

}
