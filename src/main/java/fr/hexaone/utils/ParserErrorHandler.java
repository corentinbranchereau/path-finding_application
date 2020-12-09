package fr.hexaone.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserErrorHandler implements ErrorHandler {

    /**
     * Permet de s'assurer de la validité du document parsé.
     */
    private boolean isValid = true;

    /**
     * Retourne l'attribut isValid.
     * @return isValid (true si le fichier parsé est valide).
     */
    public boolean isValid(){
        return this.isValid;
    }

    /**
     * Annonce l'erreur sur la console.
     */
    @Override
    public void warning(SAXParseException exception) {
        System.out.println("[HEXAONE PARSER WARNING] : "+exception.toString());
    }

    /**
     * Annonce l'erreur sur la console.
     */
    @Override
    public void error(SAXParseException exception) {
        if(this.isValid){
            System.out.println("[HEXAONE PARSER ERROR] : "+exception.toString());
            this.isValid = false;
        }

    }

    /**
     * Annonce l'erreur sur la console.
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("[HEXAONE PARSER FATAL ERROR] : "+exception.toString());
        this.isValid = false;
        throw exception;
    }
}
