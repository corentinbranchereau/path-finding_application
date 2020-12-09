package fr.hexaone.utils.exception;

/**
 * Exception provoqué lorsqu'une requête du fichier de requête possède
 * un point (collecte ou livraison) hors de la map actuellement chargée.
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class RequestOutOfMapException extends Exception{

    /**
     * Héritage d'une exception java
     * @param errorMessage Le message d'erreur
     */
    public RequestOutOfMapException(String errorMessage){
        super(errorMessage);
    }
    
}
