package fr.hexaone.utils.exception;

/**
 * Exception provoquée lorsque le fichier sélectionné n'est pas au bon format.
 * 
 * @see Exception
 * @author HexaOne
 * @version 1.0
 */
public class FileBadExtensionException extends Exception {

    /**
     * Héritage d'une exception Java
     * 
     * @param messageErreur Le message d'erreur
     */
    public FileBadExtensionException(String messageErreur) {
        super(messageErreur);
    }

}
