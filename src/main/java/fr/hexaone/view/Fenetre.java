package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Requete;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
     * Variable indiquant la vitesse de l'animation de zoom
     */
    protected final double VITESSE_ZOOM = 100;

    /**
     * Map qui contient pour chaque requête sa couleur d'affichage
     */
    protected Map<Requete, Color> mapCouleurRequete;

    /**
     * Coordonnée x du clic lors d'un "drag" pour déplacer la carte
     */
    protected double origineDragSceneX;

    /**
     * Coordonnée y du clic lors d'un "drag" pour déplacer la carte
     */
    protected double origineDragSceneY;

    /**
     * Translation x de la carte lors d'un "drag" pour la déplacer
     */
    protected double origineDragTranslateX;

    /**
     * Translation y de la carte lors d'un "drag" pour la déplacer
     */
    protected double origineDragTranslateY;

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
        this.mapCouleurRequete = new HashMap<>();
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
            this.stage.setTitle("いちONE - Application développée par l'HexaOne");
            stage.getIcons().add(new Image("file:src/main/resources/logo-hexa.png"));

            this.stage.show();

            // On met un clip sur le anchor pane pour ne pas que le contenu dépasse
            this.fenetreControleur.getAnchorPaneGraphique()
                    .setClip(new Rectangle(this.fenetreControleur.getAnchorPaneGraphique().getWidth(),
                            this.fenetreControleur.getAnchorPaneGraphique().getHeight()));

            // On affiche le canvas devant les autres composants graphiques
            this.fenetreControleur.getCanvas().setViewOrder(-1);

            // Ajoute une fonctionnalité de zoom sur la carte
            this.fenetreControleur.getCanvas().setOnScroll(new EventHandler<ScrollEvent>() {
                public void handle(ScrollEvent event) {
                    double facteurZoom = 0.0;
                    if (event.getTextDeltaY() > 0) {
                        // Zoom
                        facteurZoom = 2;
                    } else {
                        // Dézoom
                        facteurZoom = 0.5;
                    }

                    Timeline timeline = new Timeline(60);
                    double ancienScale = fenetreControleur.canvas.getScaleX();
                    double nouveauScale = ancienScale * facteurZoom;
                    double translationX = 0.0;
                    double translationY = 0.0;

                    if (nouveauScale <= 1) {
                        // On ne peut pas plus dézoomer
                        nouveauScale = 1;
                        // On remet la carte à sa place initiale
                        translationX = 0;
                        translationY = 0;

                    } else {
                        // On ajuste le facteur de zoom
                        facteurZoom = (nouveauScale / ancienScale) - 1;

                        // On calcule la translation nécessaire pour centrer la carte à l'endroit du
                        // zoom
                        double xCentre = event.getSceneX();
                        double yCentre = event.getSceneY();
                        Bounds bounds = fenetreControleur.canvas
                                .localToScene(fenetreControleur.canvas.getBoundsInLocal());
                        double deltaX = (xCentre - (bounds.getWidth() / 2 + bounds.getMinX()));
                        double deltaY = (yCentre - (bounds.getHeight() / 2 + bounds.getMinY()));
                        translationX = fenetreControleur.canvas.getTranslateX() - facteurZoom * deltaX;
                        translationY = fenetreControleur.canvas.getTranslateY() - facteurZoom * deltaY;
                    }

                    // On utilise une timeline pour faire une animation de zoom/dézoom
                    timeline.getKeyFrames().clear();
                    timeline.getKeyFrames()
                            .addAll(new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                    new KeyValue(fenetreControleur.canvas.translateXProperty(), translationX)),
                                    new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                            new KeyValue(fenetreControleur.canvas.translateYProperty(), translationY)),
                                    new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                            new KeyValue(fenetreControleur.canvas.scaleXProperty(), nouveauScale)),
                                    new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                            new KeyValue(fenetreControleur.canvas.scaleYProperty(), nouveauScale)));
                    timeline.play();
                    // On consomme l'événement
                    event.consume();
                }
            });

            // Ajoute une fonctionnalité pour déplacer la carte avec la souris
            this.fenetreControleur.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    // Lorsque la souris est pressée, on retient les coordonnées initiales et la
                    // translation initiale de la carte pour ensuite la déplacer dans le cas d'un
                    // "drag"
                    origineDragSceneX = event.getSceneX();
                    origineDragSceneY = event.getSceneY();
                    origineDragTranslateX = ((Canvas) (event.getSource())).getTranslateX();
                    origineDragTranslateY = ((Canvas) (event.getSource())).getTranslateY();
                }
            });

            this.fenetreControleur.getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    // On calcule la translation à appliquer pour bouger la carte lors du "drag"
                    double offsetX = event.getSceneX() - origineDragSceneX;
                    double offsetY = event.getSceneY() - origineDragSceneY;
                    double nouvelleTranslationX = origineDragTranslateX + offsetX;
                    double nouvelleTranslationY = origineDragTranslateY + offsetY;

                    // On calcule le delta x et le delta y que cela provoquerait
                    double deltaX = (nouvelleTranslationX - fenetreControleur.getCanvas().getTranslateX());
                    double deltaY = (nouvelleTranslationY - fenetreControleur.getCanvas().getTranslateY());

                    Bounds boundsInParent = ((Canvas) (event.getSource())).getBoundsInParent();

                    // On vérifie qu'après la translation la carte est toujours dans les limites de
                    // l'AnchorPane (sur l'axe x)
                    if (boundsInParent.getMaxX() + deltaX >= fenetreControleur.getAnchorPaneGraphique().getWidth()
                            && boundsInParent.getMinX() + deltaX <= 0) {
                        // On translate la carte selon l'axe x
                        ((Canvas) (event.getSource())).setTranslateX(nouvelleTranslationX);
                    } else {
                        // On applique la translation maximale sans dépasser la bordure
                        if (boundsInParent.getMaxX() + deltaX < fenetreControleur.getAnchorPaneGraphique().getWidth()) {
                            nouvelleTranslationX = fenetreControleur.getAnchorPaneGraphique().getWidth()
                                    - boundsInParent.getMaxX() + fenetreControleur.getCanvas().getTranslateX();
                        } else {
                            nouvelleTranslationX = fenetreControleur.getCanvas().getTranslateX()
                                    - boundsInParent.getMinX();
                        }
                        ((Canvas) (event.getSource())).setTranslateX(nouvelleTranslationX);
                    }

                    // On vérifie qu'après la translation la carte est toujours dans les limites de
                    // l'AnchorPane (sur l'axe y)
                    if (boundsInParent.getMaxY() + deltaY >= fenetreControleur.getAnchorPaneGraphique().getHeight()
                            && boundsInParent.getMinY() + deltaY <= 0) {
                        // On translate la carte selon l'axe y
                        ((Canvas) (event.getSource())).setTranslateY(nouvelleTranslationY);
                    } else {
                        // On applique la translation maximale sans dépasser la bordure
                        if (boundsInParent.getMaxY() + deltaY < fenetreControleur.getAnchorPaneGraphique()
                                .getHeight()) {
                            nouvelleTranslationY = fenetreControleur.getAnchorPaneGraphique().getHeight()
                                    - boundsInParent.getMaxY() + fenetreControleur.getCanvas().getTranslateY();
                        } else {
                            nouvelleTranslationY = fenetreControleur.getCanvas().getTranslateY()
                                    - boundsInParent.getMinY();
                        }
                        ((Canvas) (event.getSource())).setTranslateY(nouvelleTranslationY);
                    }
                }
            });

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

    /**
     * Renvoie la map d'association entre les requêtes et les couleurs
     * 
     * @return La map d'association entre les requêtes et les couleurs
     */
    public Map<Requete, Color> getMapCouleurRequete() {
        return mapCouleurRequete;
    }

    /**
     * Cette méthode permet de réinitialiser le zoom (et la translation liée)
     * appliqué sur la carte.
     */
    public void resetZoom() {
        this.getFenetreControleur().getCanvas().setTranslateX(0);
        this.getFenetreControleur().getCanvas().setTranslateY(0);
        this.getFenetreControleur().getCanvas().setScaleX(1);
        this.getFenetreControleur().getCanvas().setScaleY(1);
    }
}
