package fr.hexaone.utils.exception;

/**
 * Exception provoquée lorsque le DTD d'un fichier n'est pas validé par le
 * parser.
 * 
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class DTDValidationException extends Exception {

    /**
     * Héritage d'une exception Java
     * 
     * @param messageErreur Le message d'erreur
     */
    public DTDValidationException(String messageErreur) {
        super(messageErreur);
    }

}
