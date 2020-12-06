package fr.hexaone.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

}
