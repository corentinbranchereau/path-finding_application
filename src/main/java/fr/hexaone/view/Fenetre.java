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

            // Affichage de la scène
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();

            // Définition des handlers sur les éléments du menu
            fenetreControleur.chargerMapItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.handleClicChargerCarte();
                }
            });

            fenetreControleur.chargerRequetesItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.handleClicChargerRequetes();
                }
            });

            fenetreControleur.quitterItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.handleClicQuitter();
                }
            });
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier FXML : " + e);
        }
    }
}
