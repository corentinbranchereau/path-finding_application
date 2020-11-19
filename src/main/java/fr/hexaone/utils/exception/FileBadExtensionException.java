package fr.hexaone.utils.exception;

/**
 * Exception provoqué lorsque le fichier sélectionné n'est pas au bon format.
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class FileBadExtensionException extends Exception{

    /**
     * Héritage d'une exception java
     */
    public FileBadExtensionException(String errorMessage){
        super(errorMessage);
    }
    
}
