package fr.hexaone.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Permet de gérer les erreurs du parser
 * 
 * @author HexaOne
 * @version 1.0
 */
public class ParserErrorHandler implements ErrorHandler {

    /**
     * Permet de s'assurer de la validité du document parsé.
     */
    private boolean estValide = true;

    /**
     * @return L'attribut estValide
     */
    public boolean getEstValide() {
        return this.estValide;
    }

    /**
     * Annonce l'avertissement sur la console.
     * 
     * @param exception L'exception levée
     */
    @Override
    public void warning(SAXParseException exception) {
        System.out.println("[HEXAONE PARSER WARNING] : " + exception.toString());
    }

    /**
     * Annonce l'erreur sur la console.
     * 
     * @param exception L'exception levée
     */
    @Override
    public void error(SAXParseException exception) {
        if (this.estValide) {
            System.out.println("[HEXAONE PARSER ERROR] : " + exception.toString());
            this.estValide = false;
        }
    }

    /**
     * Annonce l'erreur fatale sur la console.
     * 
     * @param exception L'exception levée
     * @throws SAXException Si erreur parsage DTD.
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("[HEXAONE PARSER FATAL ERROR] : " + exception.toString());
        this.estValide = false;
        throw exception;
    }
}
