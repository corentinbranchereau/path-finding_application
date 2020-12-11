package fr.hexaone.utils.exception;

/**
 * Exception provoquée lorsque le fichier XML provoque une erreur de parser dû à
 * un attribut de type illégal (ex : texte au lieu d'un double).
 * 
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class IllegalAttributException extends Exception {

    /**
     * Héritage d'une exception Java
     * 
     * @param messageErreur Le message d'erreur
     */
    public IllegalAttributException(String messageErreur) {
        super(messageErreur);
    }

}
