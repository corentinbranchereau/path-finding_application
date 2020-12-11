package fr.hexaone.utils;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe Utils contenant des méthodes génériques pouvant servir dans toute
 * l'application.
 */
public class Utils {

    /**
     * Gère l'affichage de messages d'alertes dans l'application
     * 
     * @param objet      L'objet appelant la méthode
     * @param titre      Le titre de l'alerte
     * @param message    Le message contenu dans l'alerte
     * @param typeAlerte Le type d'alerte souhaitée
     */
    public static void afficherAlerte(Object objet, String titre, String message, Alert.AlertType typeAlerte) {
        Alert alert = new Alert(typeAlerte);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(Utils.obtenirInputStreamDepuisChemin(objet, "logo-hexa.png")));
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image("file:src/main/resources/logo-hexa.png"));
        alert.show();
    }

    /**
     * Permet d'obtenir un titre pour le menu d'aide utilisateur
     * 
     * @param titre Le titre souhaité
     * @return Le label à afficher
     */
    public static Label obtenirTitreAide(String titre) {
        Label label = new Label(titre);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15D));
        label.setPadding(new Insets(0D, 0D, 5D, 0D));
        return label;
    }

    /**
     * Permet d'obtenir un paragraphe pour le menu d'aide utilisateur
     * 
     * @param texte Le texte souhaité
     * @return Le label à afficher
     */
    public static Label obtenirParagrapheAide(String texte) {
        Label label = new Label(texte);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.ITALIC, 13D));
        label.setWrapText(true);
        label.setMaxWidth(520D);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        return label;
    }

    /**
     * Permet d'obtenir un interligne pour le menu d'aide utilisateur
     * 
     * @param valeur L'intervalle à ajouter
     * @return Le label à afficher
     */
    public static Label obtenirInterligne(double valeur) {
        Label label = new Label("");
        label.setPadding(new Insets(0D, 0D, valeur, 0D));
        return label;
    }

    /**
     * Permet d'obtenir un InputStream vers une ressource, que l'on soit dans un JAR
     * ou non.
     * 
     * @param objet      L'objet appelant la méthode
     * @param nomFichier le nom du fichier
     * @return L'input stream de la ressource pointée
     */
    public static InputStream obtenirInputStreamDepuisChemin(Object objet, String nomFichier) {
        ClassLoader classLoader = objet.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(nomFichier);
        if (inputStream == null) {
            throw new IllegalArgumentException("Fichier non trouvé " + nomFichier);
        } else {
            return inputStream;
        }
    }

    /**
     * Vérifie l'entrée utilisateur sur les durées via une regex
     * 
     * @param champDuree La saisie en entrée
     * @return True si l'entrée utilisateur est conforme (seulement des chiffres),
     *         false sinon.
     */
    public static boolean verifieDureeUtilisateur(String champDuree) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(champDuree);
        return matcher.matches();
    }
}
