package fr.hexaone.utils.exception;

/**
 * Exception provoquée lorsque le fichier XML chargé n'est pas du bon type. Par
 * exemple, si l'on charge un fichier requête au lieu d'un fichier carte.
 * 
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class BadFileTypeException extends Exception {

    /**
     * Héritage d'une exception Java
     * 
     * @param messageErreur le message d'erreur
     */
    public BadFileTypeException(String messageErreur) {
        super(messageErreur);
    }

}
