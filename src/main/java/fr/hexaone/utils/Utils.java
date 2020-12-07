package fr.hexaone.utils;

import fr.hexaone.controller.Controleur;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Classe Utils contenant des méthodes génériques pouvant servir
 * dans tout l'application.
 */
public class Utils {

    /**
     * Gère l'affichage de messages d'alertes dans l'application
     *
     * @param title     Le titre de l'alerte
     * @param message   Le message contenu dans l'alerte
     * @param alertType Le type d'alerte souhaité
     */
    public static void alertHelper(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/main/resources/logo-hexa.png"));
        alert.show();
    }

    /**
     * Obtenir un titre pour le menu d'aide utilisateur
     * @param titre Le titre souhaité
     * @return Le label à afficher
     */
    public static Label obtenirTitreAide(String titre){
        Label label = new Label(titre);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD,15D));
        label.setPadding(new Insets(0D,0D,5D,0D));
        return label;
    }

    /**
     * Obtenir un paragraphe pour le menu d'aide utilisateur
     * @param texte Le texte souhaité
     * @return Le label à afficher
     */
    public static Label obtenirParagrapheAide(String texte){
        Label label = new Label(texte);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC,13D));
        label.setWrapText(true);
        label.setMaxWidth(520D);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        return label;
    }

    /**
     * Obtenir un interligne pour le menu d'aide utilisateur
     * @param valeur L'intervalle à ajouter
     * @return Le label à afficher
     */
    public static Label obtenirInterligne(double valeur){
        Label label = new Label("");
        label.setPadding(new Insets(0D,0D,valeur,0D));
        return label;
    }

    /**
     * Permet d'obtenir une URL vers une ressource que l'on soit dans un JAR ou non
     * @return L'URL de la ressource, null si l'URL est mal formé
     */
    public static URL obtenirURLRessource(Object o, String path) {
        URL url = o.getClass().getResource(path);
        try {
            if (url == null) {
                url = new URL("file:src/main/resources/" + path);
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
            url = null;
        }
        return url;
    }
}
