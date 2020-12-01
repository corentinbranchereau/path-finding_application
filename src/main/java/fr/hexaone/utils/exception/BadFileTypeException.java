package fr.hexaone.utils.exception;

/**
 * Exception provoqué lorsque le fichier XML chargé n'est pas du bon type.
 * Par exemple, si l'on charge un fichier requête au lieu d'un fichier map.
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class BadFileTypeException extends Exception{

    /**
     * Héritage d'une exception java
     */
    public BadFileTypeException(String errorMessage){
        super(errorMessage);
    }
    
}
