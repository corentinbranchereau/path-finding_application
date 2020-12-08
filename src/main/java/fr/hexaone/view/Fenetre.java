package fr.hexaone.view;

import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    private VueGraphique vueGraphique;

    /**
     * Vue textuelle de la fenêtre
     */
    private VueTextuelle vueTextuelle;

    /**
     * Controleur JavaFX servant à récupérer les références des éléments du fichier
     * FXML
     */
    private FenetreControleurFXML fenetreControleur;

    /**
     * Conteneur principal des éléments graphiques
     */
    private Stage stage;

    /**
     * Controleur gérant la logique de l'application
     */
    private Controleur controleur;

    /**
     * Variable indiquant la vitesse de l'animation de zoom
     */
    private final double VITESSE_ZOOM = 100;

    /**
     * Map qui contient pour chaque requête sa couleur d'affichage
     */
    private Map<Requete, Color> mapCouleurRequete;

    /**
     * Coordonnée x du clic lors d'un "drag" pour déplacer la carte
     */
    private double origineDragSceneX;

    /**
     * Coordonnée y du clic lors d'un "drag" pour déplacer la carte
     */
    private double origineDragSceneY;

    /**
     * Translation x de la carte lors d'un "drag" pour la déplacer
     */
    private double origineDragTranslateX;

    /**
     * Translation y de la carte lors d'un "drag" pour la déplacer
     */
    private double origineDragTranslateY;

    /**
     * Largeur initiale de la carte (utile pour redonner à la carte sa taille
     * initiale)
     */
    private double largeurInitialeCarte;

    /**
     * Hauteur initiale de la carte (utile pour redonner à la carte sa taille
     * initiale)
     */
    private double hauteurInitialeCarte;

    /**
     * Largeur initiale de la fenêtre (utile pour redonner à la fenêtre sa taille
     * initiale)
     */
    private double largeurInitialeStage;

    /**
     * Hauteur initiale de la fenêtre (utile pour redonner à la fenêtre sa taille
     * initiale)
     */
    private double hauteurInitialeStage;

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
            InputStream inputFichierFxml = Utils.obtenirInputStreamDepuisPath(this, "fenetre.fxml");
            Parent root = loader.load(inputFichierFxml);

            // Récupération du controleur FXML
            fenetreControleur = loader.getController();

            // On fait en sorte que le pane de dessin s'affiche au dessus des autres
            // éléments
            this.fenetreControleur.getPaneDessin().setViewOrder(-1);

            // On donne le pane de dessin à la vue graphique
            this.vueGraphique.setPaneDessin(this.fenetreControleur.getPaneDessin());

            // On récupère la taille initiale de la zone de dessin
            this.fenetreControleur.getAnchorPaneGraphique().autosize();
            this.largeurInitialeCarte = this.fenetreControleur.getAnchorPaneGraphique().getWidth();
            this.hauteurInitialeCarte = this.fenetreControleur.getAnchorPaneGraphique().getHeight();

            // On lie les propriétés "visible" et "managed" des noeuds dont la visibilité
            // change au cours de l'application
            lierVisibleEtManaged();

            // Affichage de la scène
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.setResizable(false);
            this.stage.setTitle("いちONE - Application développée par l'HexaOne");
            stage.getIcons().add(new Image(Utils.obtenirInputStreamDepuisPath(this, "logo-hexa.png")));
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
            this.fenetreControleur.getPaneDessin().setOnScroll(event -> { // scrollEvent linux
                event.consume();
                double facteurZoom = 1.;
                double scrollAmout = event.getDeltaY();
                if (scrollAmout > 0) {
                    // Zoom
                    facteurZoom = 2.;
                } else if (scrollAmout < 0) {
                    // Dézoom
                    facteurZoom = 0.5;
                }

                Timeline timeline = new Timeline(60);
                double ancienScale = fenetreControleur.getPaneDessin().getScaleX();
                double nouveauScale = ancienScale * facteurZoom;
                double translationX;
                double translationY;

                if (nouveauScale <= 1) {
                    // On ne peut pas plus dézoomer
                    nouveauScale = 1D;
                    // On remet la carte à sa place initiale
                    translationX = 0D;
                    translationY = 0D;

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
            });

            // Ajoute une fonctionnalité pour déplacer la carte avec la souris
            this.fenetreControleur.getPaneDessin().setOnMousePressed(event -> {
                // Lorsque la souris est pressée, on retient les coordonnées initiales et la
                // translation initiale de la carte pour ensuite la déplacer dans le cas d'un
                // "drag"
                origineDragSceneX = event.getSceneX();
                origineDragSceneY = event.getSceneY();
                origineDragTranslateX = ((Pane) (event.getSource())).getTranslateX();
                origineDragTranslateY = ((Pane) (event.getSource())).getTranslateY();
            });

            this.fenetreControleur.getPaneDessin().setOnMouseDragged(event -> {
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
                    if (boundsInParent.getMaxY() + deltaY < fenetreControleur.getAnchorPaneGraphique().getHeight()) {
                        nouvelleTranslationY = fenetreControleur.getAnchorPaneGraphique().getHeight()
                                - boundsInParent.getMaxY() + fenetreControleur.getPaneDessin().getTranslateY();
                    } else {
                        nouvelleTranslationY = fenetreControleur.getPaneDessin().getTranslateY()
                                - boundsInParent.getMinY();
                    }
                    ((Pane) (event.getSource())).setTranslateY(nouvelleTranslationY);
                }
            });

            // Définition des handlers sur les éléments du menu
            fenetreControleur.getChargerCarteItem().setOnAction(event -> controleur.chargerCarte());

            fenetreControleur.getChargerRequetesItem().setOnAction(event -> controleur.chargerRequetes());

            fenetreControleur.getQuitterItem().setOnAction(event -> controleur.quitterApplication());

            fenetreControleur.getBoutonLancer().setOnAction(event -> controleur.lancerCalcul());

            fenetreControleur.getBoutonNouvelleRequete().setOnAction(event -> controleur.ajouterNouvelleRequete());

            fenetreControleur.getBoutonValider()
                    .setOnAction(event -> controleur.valider(fenetreControleur.getPickUpDurationField().getText(),
                            fenetreControleur.getDeliveryDurationField().getText()));

            fenetreControleur.getBoutonValiderModificationDemande()
                    .setOnAction(event -> controleur.valider(fenetreControleur.getDurationField().getText()));

            fenetreControleur.getAide().setOnAction(event -> controleur.aide());

            fenetreControleur.getBoutonAnnuler().setOnAction(event -> controleur.annuler());

            fenetreControleur.getUndoItem().setOnAction(event -> controleur.undo());

            fenetreControleur.getRedoItem().setOnAction(event -> controleur.redo());

            // Définition des handlers pour le undo/redo (CTRL + Z, CTRL + Y)
            scene.getAccelerators().put(KeyCombination.keyCombination("CTRL+Z"), new Runnable() {
                @Override
                public void run() {
                    controleur.undo();
                }
            });

            scene.getAccelerators().put(KeyCombination.keyCombination("CTRL+Y"), new Runnable() {
                @Override
                public void run() {
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
     * Renvoie le controleur de l'application
     *
     * @return Le controleur
     */
    public Controleur getControleur() {
        return controleur;
    }

    /**
     * Affiche l'aide utilisateur
     */
    public void afficherAide() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(Utils.obtenirInputStreamDepuisPath(this, "logo-hexa.png")));
        alert.getDialogPane().setMaxWidth(600D);
        alert.getDialogPane().setMaxHeight(700D);
        alert.setHeight(700D);
        alert.setY(100D);
        alert.setTitle("Aide de l'application");
        alert.setHeaderText("Bienvenue sur l'aide de l'application いちONE, développée par l'HexaOne !");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Chargement des fichiers XML"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Afin de charger une carte et des demandes de livraison, il faut se rendre dans le menu Fichier en haut à gauche de l'application et choisir l'option adaptée."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Signification des symboles"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque des demandes de livraison ont été chargées, des carrés et des ronds colorés apparaissent dans la vue graphique : les carrés représentent les différents points de collecte des paquets, tandis que les ronds représentent les points de livraison des paquets. Il est à noter que les points de collecte et de livraison qui vont de pair s'affichent de la même couleur."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Calculer une tournée"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Une fois une carte et des demandes de livraison chargées, il est possible de calculer une tournée en utilisant le bouton \"Lancer le calcul\" disponible sur la vue textuelle de l'application."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Couleurs utilisées pour le dessin d'une tournée"));
        TextFlow texteCouleursUtilisees = new TextFlow();
        Color couleurTexte = Color.gray(0.20);
        Text debutTexteCouleurs = new Text("Lorsqu'une tournée est affichée, le sens du parcours va du ");
        debutTexteCouleurs.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        debutTexteCouleurs.setFill(couleurTexte);
        Text texteCouleur1 = new Text("rouge");
        texteCouleur1.setUnderline(true);
        texteCouleur1.setFill(Color.hsb(0, 1.0, 1.0));
        texteCouleur1.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        Text milieuTexteCouleurs = new Text(" au ");
        milieuTexteCouleurs.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        milieuTexteCouleurs.setFill(couleurTexte);
        milieuTexteCouleurs.setWrappingWidth(600D);
        Text texteCouleur2 = new Text("vert");
        texteCouleur2.setUnderline(true);
        texteCouleur2.setFill(Color.hsb(120, 1.0, 1.0));
        texteCouleur2.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        Text finTexteCouleurs = new Text(
                " (c'est-à-dire que le livreur part du dépôt du côté du rouge et revient à ce dernier du côté du vert)");
        finTexteCouleurs.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        finTexteCouleurs.setFill(couleurTexte);
        texteCouleursUtilisees.getChildren().addAll(debutTexteCouleurs, texteCouleur1, milieuTexteCouleurs,
                texteCouleur2, finTexteCouleurs);
        vBox.getChildren().add(texteCouleursUtilisees);
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Sélection d'une demande"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Il est possible de sélectionner une demande pour la mettre en valeur en cliquant dessus (dans la vue graphique ou textuelle). Pour la désélectionner, il suffit de recliquer dessus. Lorsqu'une demande est sélectionnée, les trajets situés avant et après cette dernière sont également mis en valeur, en suivant la même logique de couleur que pour la tournée (le sens va du rouge au vert)."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Modifier l'ordre d'une tournée"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque la tournée est calculée, il est possible de modifier l'ordre d'une tournée en modifiant l'ordre de passage à un point de collecte ou de livraison. Pour ce faire, il suffit de faire un drag'n'drop (sélection du point à déplacer avec la souris en maintenant le clic, tout en le déplaçant vers le bon emplacement) depuis la vue textuelle."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Modifier le lieu d'un évènement"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque la tournée est calculée, il est possible de modifier le lieu d'un évènement sur un point de collecte ou de livraison en cliquant droit sur le point depuis la vue textuelle, en choisissant l'option adéquate puis en choisissant une intersection sur la carte."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Modifier la durée d'un évènement"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque la tournée est calculée, il est possible de modifier la durée d'un évènement sur un point de collecte ou de livraison en cliquant droit sur le point depuis la vue textuelle et en choisissant l'option adéquate."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Ajouter une demande de livraison"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque la tournée est calculée, il est possible d'ajouter une demande de livraison, partielle ou non, grâce au bouton \"Ajouter une demande de livraison\" depuis la vue textuelle. Ensuite, sélectionner une ou deux intersection(s) sur la carte et remplissez le ou les champ(s) de durée qu'il vous faut. Enfin, valider ou annuler votre saisie."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Supprimer une demande de livraison"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Lorsque la tournée est calculée, il est possible de supprimer une demande de livraison, de façon partielle ou non, en cliquant droit sur la collecte ou la livraison depuis la vue textuelle et en choisissant l'option adéquate."));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        vBox.getChildren().add(Utils.obtenirTitreAide("Annuler ou rejouer une action"));
        vBox.getChildren().add(Utils.obtenirParagrapheAide(
                "Vous avez commis une erreur lors de l'amélioration d'une tournée calculée ? Aucun problème ! Il est possible d'annuler et de rejouer chaque action avec les raccourcis clavier CTRL+Z (undo) et CTRL+Y (redo), ou en passant par le menu \"Edition\""));
        vBox.getChildren().add(Utils.obtenirInterligne(3D));

        scrollPane.setContent(vBox);

        alert.getDialogPane().setContent(scrollPane);

        alert.show();
    }

    /**
     * Méthode qui permet de lier les propriétés "visible" et "managed" des noeuds
     * dont la visibilité change au cours de l'application, afin que les autres
     * éléments s'adaptent pour prendre la place libérée
     */
    public void lierVisibleEtManaged() {
        this.fenetreControleur.getBoutonNouvelleRequete().managedProperty()
                .bind(this.fenetreControleur.getBoutonNouvelleRequete().visibleProperty());
        this.fenetreControleur.getBoutonLancer().managedProperty()
                .bind(this.fenetreControleur.getBoutonLancer().visibleProperty());
        this.fenetreControleur.getBoutonValider().managedProperty()
                .bind(this.fenetreControleur.getBoutonValider().visibleProperty());
        this.fenetreControleur.getDurationField().managedProperty()
                .bind(this.fenetreControleur.getDurationField().visibleProperty());
        this.fenetreControleur.getDurationLabel().managedProperty()
                .bind(this.fenetreControleur.getDurationLabel().visibleProperty());
        this.fenetreControleur.getBoutonValiderModificationDemande().managedProperty()
                .bind(this.fenetreControleur.getBoutonValiderModificationDemande().visibleProperty());
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().managedProperty()
                .bind(this.fenetreControleur.getBoxBoutonsValiderAnnuler().visibleProperty());
        this.fenetreControleur.getBoutonAnnuler().managedProperty()
                .bind(this.fenetreControleur.getBoutonAnnuler().visibleProperty());
        this.fenetreControleur.getDeliveryDurationField().managedProperty()
                .bind(this.fenetreControleur.getDeliveryDurationField().visibleProperty());
        this.fenetreControleur.getDeliveryDurationLabel().managedProperty()
                .bind(this.fenetreControleur.getDeliveryDurationLabel().visibleProperty());
        this.fenetreControleur.getPickUpDurationField().managedProperty()
                .bind(this.fenetreControleur.getPickUpDurationField().visibleProperty());
        this.fenetreControleur.getPickUpDurationLabel().managedProperty()
                .bind(this.fenetreControleur.getBoutonValider().visibleProperty());
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatAjoutNouvelleRequete
     */
    public void initFenetreAjoutNouvelleRequete() {
        this.fenetreControleur.getPickUpDurationField().clear();
        this.fenetreControleur.getDeliveryDurationField().clear();
        this.fenetreControleur.getDurationField().setVisible(false);
        this.fenetreControleur.getDurationLabel().setVisible(false);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(true);
        this.fenetreControleur.getBoutonAnnuler().setVisible(true);
        this.fenetreControleur.getBoutonAnnuler().setDisable(false);
        this.fenetreControleur.getBoutonValider().setVisible(true);
        this.fenetreControleur.getBoutonValider().setDisable(false);
        this.fenetreControleur.getBoutonLancer().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(true);
        this.fenetreControleur.getPickUpDurationField().setVisible(true);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(true);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(true);
        this.vueTextuelle.getRequetesControleur().setDraggable(false);
        this.vueTextuelle.showContextualMenu(false);
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatCarteChargee
     */
    public void initFenetreCarteChargee() {
        this.fenetreControleur.getDurationField().setVisible(false);
        this.fenetreControleur.getDurationLabel().setVisible(false);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(false);
        this.fenetreControleur.getBoutonAnnuler().setVisible(false);
        this.fenetreControleur.getBoutonLancer().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(false);
        this.fenetreControleur.getBoutonValider().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(false);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().clear();
        this.fenetreControleur.getDeliveryDurationField().clear();
        this.fenetreControleur.getScrollPane().setContent(null);
        this.vueTextuelle.showContextualMenu(false);
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatInitial
     */
    public void initFenetreInitial() {
        this.fenetreControleur.getDurationField().setVisible(false);
        this.fenetreControleur.getDurationLabel().setVisible(false);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(false);
        this.fenetreControleur.getBoutonAnnuler().setVisible(false);
        this.fenetreControleur.getBoutonLancer().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(false);
        this.fenetreControleur.getBoutonValider().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(false);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setDisable(false);
        this.fenetreControleur.getDeliveryDurationField().setDisable(false);
        this.fenetreControleur.getPickUpDurationField().setDisable(false);
        this.vueTextuelle.showContextualMenu(false);
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatModifierDemande
     * 
     * @param duree La durée à mettre par défaut dans le champs dédié à la durée de
     *              la demande
     */
    public void initFenetreModifierDemande(int duree) {
        this.fenetreControleur.getDurationField().clear();
        this.fenetreControleur.getDurationField().setText(String.valueOf(duree));
        this.fenetreControleur.getDurationField().setVisible(true);
        this.fenetreControleur.getDurationLabel().setVisible(true);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(true);
        this.fenetreControleur.getBoutonValiderModificationDemande().setDisable(false);
        this.fenetreControleur.getDurationField().setDisable(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(true);
        this.fenetreControleur.getBoutonAnnuler().setVisible(true);
        this.fenetreControleur.getBoutonAnnuler().setDisable(false);
        this.fenetreControleur.getBoutonValider().setVisible(false);
        this.fenetreControleur.getBoutonValider().setDisable(true);
        this.fenetreControleur.getBoutonLancer().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(false);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(false);
        this.vueTextuelle.getRequetesControleur().setDraggable(false);
        this.vueTextuelle.showContextualMenu(false);
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatRequetesChargees
     */
    public void initFenetreRequetesChargees() {
        this.fenetreControleur.getDurationField().setVisible(false);
        this.fenetreControleur.getDurationLabel().setVisible(false);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(false);
        this.fenetreControleur.getBoutonAnnuler().setVisible(false);
        this.fenetreControleur.getBoutonLancer().setVisible(true);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(false);
        this.fenetreControleur.getBoutonValider().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(false);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(false);
        this.vueTextuelle.chargerFXML();
        this.vueTextuelle.getRequetesControleur().setDraggable(false);
        this.vueTextuelle.showContextualMenu(false);
    }

    /**
     * Méthode qui permet d'adapter les éléments de la fenêtre pour l'état
     * EtatTourneeCalcule
     */
    public void initFenetreTourneeCalcule() {
        this.fenetreControleur.getDurationField().setVisible(false);
        this.fenetreControleur.getDurationLabel().setVisible(false);
        this.fenetreControleur.getBoutonValiderModificationDemande().setVisible(false);
        this.fenetreControleur.getBoutonAnnuler().setVisible(false);
        this.fenetreControleur.getBoutonLancer().setVisible(false);
        this.fenetreControleur.getBoutonNouvelleRequete().setVisible(true);
        this.fenetreControleur.getBoutonValider().setVisible(false);
        this.fenetreControleur.getDeliveryDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationField().setVisible(false);
        this.fenetreControleur.getPickUpDurationLabel().setVisible(false);
        this.fenetreControleur.getDeliveryDurationLabel().setVisible(false);
        this.fenetreControleur.getBoxBoutonsValiderAnnuler().setVisible(true);
        this.vueTextuelle.getRequetesControleur().setDraggable(true);
        this.vueTextuelle.showContextualMenu(true);
    }
}
