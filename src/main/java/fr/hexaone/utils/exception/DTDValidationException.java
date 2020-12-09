package fr.hexaone.utils.exception;

/**
 * Exception provoqué lorsque le DTD d'un fichier n'est pas validé par le parser.
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class DTDValidationException extends Exception{

    /**
     * Héritage d'une exception java
     * @param errorMessage Le message d'erreur
     */
    public DTDValidationException(String errorMessage){
        super(errorMessage);
    }
    
}
