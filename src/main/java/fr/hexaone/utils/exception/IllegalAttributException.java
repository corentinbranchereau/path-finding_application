package fr.hexaone.utils.exception;

/**
 * Exception provoqué lorsque le fichier XML provoque une erreur de PARSER
 * dû à un attribut de type illégal (ex : texte au lieu d'un double).
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class IllegalAttributException extends Exception{

    /**
     * Héritage d'une exception java
     * @param errorMessage Le message d'erreur
     */
    public IllegalAttributException(String errorMessage){
        super(errorMessage);
    }
    
}
