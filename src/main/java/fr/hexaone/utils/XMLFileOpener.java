package fr.hexaone.utils;

import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Permet d'ouvrir un fichier et de version type (XML). Implémente FileFilter du
 * package java.io
 * 
 * @see FileFilter
 * @author HexaOne
 * @version 1.0
 */
public class XMLFileOpener implements FileFilter {

    /**
     * L'instance SINGLETON de la classe.
     */
    private static XMLFileOpener instance = null;

    /**
     * Constructeur par défaut privé.
     */
    private XMLFileOpener() {
    }

    /**
     * Implémentation du design pattern SINGLETON afin d'implémenter l'interface
     * FileFilter du package java.io en gardant un comportement static pour la
     * classe
     * 
     * @return l'unique instance de la classe XMLFileOpener. Si elle n'existe pas,
     *         l'instancie.
     */
    public static XMLFileOpener getInstance() {
        if (instance == null)
            instance = new XMLFileOpener();
        return instance;
    }

    /**
     * Ouverture d'un fichier XML dont l'URI est est spécifié en paramètre
     * 
     * @param path L'URI vers le fichier à ouvrir
     * @throws IOException               Lorsque le fichier spécifié n'existe pas
     * @throws FileBadExtensionException Lorsque l'extension du fichier n'est pas
     *                                   XML
     * @throws SAXException              Lors que le fichier possède des erreurs de
     *                                   formatage
     * @return Le document XML correspondant au format org.w3c.dom.Document
     */
    public Document open(String path) throws IOException, FileBadExtensionException, SAXException, DTDValidationException {
        File file = new File(path);
        if (!accept(file))
            throw new FileBadExtensionException("Incorrect file extension, XML is needed.");
        Document xml = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true); // Activer la vérification par le DTD
            ParserErrorHandler errorHandler = new ParserErrorHandler();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(errorHandler);
            xml = documentBuilder.parse(file);
            if(!errorHandler.isValid()){
                throw new DTDValidationException("DTD Error, XML file does not validate it.");
            }
            xml.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Vérifie l'extension du fichier ouvert
     * 
     * @param file Le fichier à vérifier
     * @return true si le fichier est au format XML, false sinon.
     */
    @Override
    public boolean accept(File file) {
        return file.getName().endsWith(".xml");
    }
}
