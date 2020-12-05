package fr.hexaone.utils;

import javafx.scene.control.Alert;

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
        alert.show();
    }

}
