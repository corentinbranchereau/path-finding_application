package fr.hexaone.utils.exception;

/**
 * Exception provoquée lorsqu'une requête du fichier de requêtes possède un
 * point (collecte ou livraison) hors de la map actuellement chargée.
 * 
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class RequestOutOfMapException extends Exception {

    /**
     * Héritage d'une exception Java
     * 
     * @param messageErreur Le message d'erreur
     */
    public RequestOutOfMapException(String messageErreur) {
        super(messageErreur);
    }

}
