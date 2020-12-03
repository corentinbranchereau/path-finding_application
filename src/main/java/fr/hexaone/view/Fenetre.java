package fr.hexaone.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Carte;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

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
     * Largeur initiale de la carte (utile pour redonner à la carte sa taille
     * initiale)
     */
    protected double largeurInitialeCarte;

    /**
     * Hauteur initiale de la carte (utile pour redonner à la carte sa taille
     * initiale)
     */
    protected double hauteurInitialeCarte;

    /**
     * Largeur initiale de la fenêtre (utile pour redonner à la fenêtre sa taille
     * initiale)
     */
    protected double largeurInitialeStage;

    /**
     * Hauteur initiale de la fenêtre (utile pour redonner à la fenêtre sa taille
     * initiale)
     */
    protected double hauteurInitialeStage;

    /**
     * Liste observable des demandes
     */
    protected ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();

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
        this.vueTextuelle = new VueTextuelle(this);
        this.vueGraphique = new VueGraphique(this);
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

            // on donne la zone de texte à la vue textuelle
            this.vueTextuelle.setZoneTexte(this.fenetreControleur.getZoneTexte());

            // On fait en sorte que le pane de dessin s'affiche au dessus des autres
            // éléments
            this.fenetreControleur.getPaneDessin().setViewOrder(-1);

            // On donne le pane de dessin à la vue graphique
            this.vueGraphique.setPaneDessin(this.fenetreControleur.getPaneDessin());

            // On récupère la taille initiale de la zone de dessin
            this.fenetreControleur.getAnchorPaneGraphique().autosize();
            this.largeurInitialeCarte = this.fenetreControleur.getAnchorPaneGraphique().getWidth();
            this.hauteurInitialeCarte = this.fenetreControleur.getAnchorPaneGraphique().getHeight();

            // Affichage de la scène
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.setResizable(false);
            this.stage.setTitle("いちONE - Application développée par l'HexaOne");
            stage.getIcons().add(new Image("file:src/main/resources/logo-hexa.png"));

            this.stage.show();

            this.largeurInitialeStage = this.stage.getWidth();
            this.hauteurInitialeStage = this.stage.getHeight();

            // On met un clip sur le anchor pane pour ne pas que le contenu dépasse
            this.fenetreControleur.getAnchorPaneGraphique()
                    .setClip(new Rectangle(this.fenetreControleur.getAnchorPaneGraphique().getWidth(),
                            this.fenetreControleur.getAnchorPaneGraphique().getHeight()));

            // On affiche le canvas devant les autres composants graphiques
            this.fenetreControleur.getPaneDessin().setViewOrder(-1);

            // Ajoute une fonctionnalité de zoom sur la carte
            this.fenetreControleur.getPaneDessin().setOnScroll(new EventHandler<ScrollEvent>() {
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
                    double ancienScale = fenetreControleur.getPaneDessin().getScaleX();
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
                        Bounds bounds = fenetreControleur.getPaneDessin()
                                .localToScene(fenetreControleur.getPaneDessin().getBoundsInLocal());
                        double deltaX = (xCentre - (bounds.getWidth() / 2 + bounds.getMinX()));
                        double deltaY = (yCentre - (bounds.getHeight() / 2 + bounds.getMinY()));
                        translationX = fenetreControleur.getPaneDessin().getTranslateX() - facteurZoom * deltaX;
                        translationY = fenetreControleur.getPaneDessin().getTranslateY() - facteurZoom * deltaY;
                    }

                    // On utilise une timeline pour faire une animation de zoom/dézoom
                    timeline.getKeyFrames().clear();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                    new KeyValue(fenetreControleur.getPaneDessin().translateXProperty(), translationX)),
                            new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                    new KeyValue(fenetreControleur.getPaneDessin().translateYProperty(), translationY)),
                            new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                    new KeyValue(fenetreControleur.getPaneDessin().scaleXProperty(), nouveauScale)),
                            new KeyFrame(Duration.millis(VITESSE_ZOOM),
                                    new KeyValue(fenetreControleur.getPaneDessin().scaleYProperty(), nouveauScale)));
                    timeline.play();
                    // On consomme l'événement
                    event.consume();
                }
            });

            // Ajoute une fonctionnalité pour déplacer la carte avec la souris
            this.fenetreControleur.getPaneDessin().setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    // Lorsque la souris est pressée, on retient les coordonnées initiales et la
                    // translation initiale de la carte pour ensuite la déplacer dans le cas d'un
                    // "drag"
                    origineDragSceneX = event.getSceneX();
                    origineDragSceneY = event.getSceneY();
                    origineDragTranslateX = ((Pane) (event.getSource())).getTranslateX();
                    origineDragTranslateY = ((Pane) (event.getSource())).getTranslateY();
                }
            });

            this.fenetreControleur.getPaneDessin().setOnMouseDragged(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    // On calcule la translation à appliquer pour bouger la carte lors du "drag"
                    double offsetX = event.getSceneX() - origineDragSceneX;
                    double offsetY = event.getSceneY() - origineDragSceneY;
                    double nouvelleTranslationX = origineDragTranslateX + offsetX;
                    double nouvelleTranslationY = origineDragTranslateY + offsetY;

                    // On calcule le delta x et le delta y que cela provoquerait
                    double deltaX = (nouvelleTranslationX - fenetreControleur.getPaneDessin().getTranslateX());
                    double deltaY = (nouvelleTranslationY - fenetreControleur.getPaneDessin().getTranslateY());

                    Bounds boundsInParent = ((Pane) (event.getSource())).getBoundsInParent();

                    // On vérifie qu'après la translation la carte est toujours dans les limites de
                    // l'AnchorPane (sur l'axe x)
                    if (boundsInParent.getMaxX() + deltaX >= fenetreControleur.getAnchorPaneGraphique().getWidth()
                            && boundsInParent.getMinX() + deltaX <= 0) {
                        // On translate la carte selon l'axe x
                        ((Pane) (event.getSource())).setTranslateX(nouvelleTranslationX);
                    } else {
                        // On applique la translation maximale sans dépasser la bordure
                        if (boundsInParent.getMaxX() + deltaX < fenetreControleur.getAnchorPaneGraphique().getWidth()) {
                            nouvelleTranslationX = fenetreControleur.getAnchorPaneGraphique().getWidth()
                                    - boundsInParent.getMaxX() + fenetreControleur.getPaneDessin().getTranslateX();
                        } else {
                            nouvelleTranslationX = fenetreControleur.getPaneDessin().getTranslateX()
                                    - boundsInParent.getMinX();
                        }
                        ((Pane) (event.getSource())).setTranslateX(nouvelleTranslationX);
                    }

                    // On vérifie qu'après la translation la carte est toujours dans les limites de
                    // l'AnchorPane (sur l'axe y)
                    if (boundsInParent.getMaxY() + deltaY >= fenetreControleur.getAnchorPaneGraphique().getHeight()
                            && boundsInParent.getMinY() + deltaY <= 0) {
                        // On translate la carte selon l'axe y
                        ((Pane) (event.getSource())).setTranslateY(nouvelleTranslationY);
                    } else {
                        // On applique la translation maximale sans dépasser la bordure
                        if (boundsInParent.getMaxY() + deltaY < fenetreControleur.getAnchorPaneGraphique()
                                .getHeight()) {
                            nouvelleTranslationY = fenetreControleur.getAnchorPaneGraphique().getHeight()
                                    - boundsInParent.getMaxY() + fenetreControleur.getPaneDessin().getTranslateY();
                        } else {
                            nouvelleTranslationY = fenetreControleur.getPaneDessin().getTranslateY()
                                    - boundsInParent.getMinY();
                        }
                        ((Pane) (event.getSource())).setTranslateY(nouvelleTranslationY);
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

            fenetreControleur.getBoutonNouvelleRequete().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.ajouterNouvelleRequete();
                }
            });

            fenetreControleur.getBoutonValider().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {

                    controleur.valider(fenetreControleur.getPickUpDurationField().getText(),
                            fenetreControleur.getDeliveryDurationField().getText());
                }
            });

            fenetreControleur.getBoutonAnnuler().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.annuler();
                }
            });

            /*fenetreControleur.getboutonModifierPlanning().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.modifierPlanning();
                }
            });
            */
            
            fenetreControleur.getUndoItem().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.undo();
                }
            });
            
            fenetreControleur.getRedoItem().setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    controleur.redo();
                }
            });

        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier FXML : " + e);
        }
    }

    /**
     * Permet de rafraichir la fenêtre
     * 
     * @param planning            Le planning actuel de l'application, contenant les
     *                            objets du modèle à dessiner
     * @param demandeSelectionnee La demande qui a été sélectionnée (s'il y en a
     *                            une)
     * @param dessinerCarte       Indique s'il y a besoin de redessiner les noeuds
     *                            de la carte (segments et intersections)
     */
    public void rafraichir(Planning planning, Demande demandeSelectionnee, boolean dessinerCarte) {
        vueGraphique.rafraichir(planning, demandeSelectionnee, dessinerCarte);
        vueTextuelle.rafraichir(planning, demandeSelectionnee);
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
        this.getFenetreControleur().getPaneDessin().setTranslateX(0);
        this.getFenetreControleur().getPaneDessin().setTranslateY(0);
        this.getFenetreControleur().getPaneDessin().setScaleX(1);
        this.getFenetreControleur().getPaneDessin().setScaleY(1);
    }

    /**
     * Méthode qui permet d'adapter la taille de la fenêtre en fonction de la taille
     * de la carte affichée
     */
    public void adapterTailleFenetre() {
        Point2D coordMin = this.vueGraphique.adapterCoordonnees(this.vueGraphique.getMinX(),
                this.vueGraphique.getMinY());
        Point2D coordMax = this.vueGraphique.adapterCoordonnees(
                (this.vueGraphique.getMaxX() + this.vueGraphique.getMinX()),
                (this.vueGraphique.getMaxY() + this.vueGraphique.getMinY()));

        double largeur = Math.abs(coordMax.getX() - coordMin.getX()) + this.vueGraphique.getPADDING_CARTE();
        double hauteur = Math.abs(coordMax.getY() - coordMin.getY()) + this.vueGraphique.getPADDING_CARTE();

        this.fenetreControleur.getBordureCarte().setWidth(largeur);
        this.fenetreControleur.getBordureCarte().setHeight(hauteur);

        this.fenetreControleur.getPaneDessin().setPrefSize(largeur, hauteur);

        this.fenetreControleur.getAnchorPaneGraphique().setPrefSize(largeur, hauteur);
        this.fenetreControleur.getAnchorPaneGraphique().setClip(new Rectangle(largeur, hauteur));

        this.stage.setWidth(this.stage.getWidth() - (this.largeurInitialeCarte - largeur));
        this.stage.setHeight(this.stage.getHeight() - (this.hauteurInitialeCarte - hauteur));
    }

    /**
     * Méthode permettant de remettre la taille initiale (taille au lancement) de la
     * fenêtre
     */
    public void resetTailleFenetre() {
        this.fenetreControleur.getBordureCarte().setWidth(largeurInitialeCarte);
        this.fenetreControleur.getBordureCarte().setHeight(hauteurInitialeCarte);

        this.fenetreControleur.getPaneDessin().setPrefSize(largeurInitialeCarte, hauteurInitialeCarte);

        this.fenetreControleur.getAnchorPaneGraphique().setPrefSize(largeurInitialeCarte, hauteurInitialeCarte);
        this.fenetreControleur.getAnchorPaneGraphique()
                .setClip(new Rectangle(largeurInitialeCarte, hauteurInitialeCarte));

        this.stage.setWidth(this.largeurInitialeStage);
        this.stage.setHeight(this.hauteurInitialeStage);
    }

    /**
     * @return ObservableList<Demande> return the listeDemandes
     */
    public ObservableList<Demande> getListeDemandes() {
        return listeDemandes;
    }

    /**
     * setter
     * 
     * @param list
     */
    public void setListeDemandes(ObservableList<Demande> list) {
        this.listeDemandes = list;
    }

    /**
     * Renvoie le controleur de l'application
     * 
     * @return Le controleur
     */
    public Controleur getControleur() {
        return controleur;
    }
}
