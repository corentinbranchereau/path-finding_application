package fr.hexaone.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserErrorHandler implements ErrorHandler {

    private boolean isValid = true;

    public boolean isValid(){
        return this.isValid;
    }

    @Override
    public void warning(SAXParseException exception) {
        System.out.println("[HEXAONE PARSER WARNING] : "+exception.toString());
    }

    @Override
    public void error(SAXParseException exception) {
        if(this.isValid){
            System.out.println("[HEXAONE PARSER ERROR] : "+exception.toString());
            this.isValid = false;
        }

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("[HEXAONE PARSER FATAL ERROR] : "+exception.toString());
        this.isValid = false;
        throw exception;
    }
}
