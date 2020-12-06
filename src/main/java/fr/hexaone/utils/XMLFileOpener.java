package fr.hexaone.utils;

import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
     * @param dtdType Le type de dtd à appliquer
     * @throws IOException               Lorsque le fichier spécifié n'existe pas
     * @throws FileBadExtensionException Lorsque l'extension du fichier n'est pas
     *                                   XML
     * @throws SAXException              Lors que le fichier possède des erreurs de
     *                                   formatage
     * @throws DTDValidationException    Lorsque le fichier XML ne valide pas son DTD
     * @return Le document XML correspondant au format org.w3c.dom.Document
     */
    public Document open(String path, DTDType dtdType) throws IOException, FileBadExtensionException, SAXException, DTDValidationException, URISyntaxException {
        File file = creerXMLTemporaireAvecDTD(path, dtdType);
        if (!accept(file))
            throw new FileBadExtensionException("Extension de fichier incorrete, il faut du .XML.");
        Document xml = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true); // Activer la vérification par le DTD
        ParserErrorHandler errorHandler = new ParserErrorHandler();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(errorHandler);
            xml = documentBuilder.parse(file);
            if(!errorHandler.isValid()){
                throw new DTDValidationException("Erreur de DTD, le fichier XML ne le valide pas.");
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

    /**
     * Crée un fichier temporaire comportant la DTD correspondante au type passé en paramètre.
     * De cette façon, nous pouvons vérifier la bonne formation d'un fichier XML non doté d'un
     * dtd au départ.
     * @param realPath Le chemin du fichier réel de l'utilisateur
     * @param dtdType Le type de dtd à appliquer
     * @return Le fichier temporaire crée
     */
    private File creerXMLTemporaireAvecDTD(String realPath, DTDType dtdType) throws IOException, URISyntaxException {
        //Charge le dtd du dossier ressource
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(dtdType.getPath());
        File dtdFile;
        if (resource == null) {
            throw new IllegalArgumentException("Fichier XML non trouvé dans le dossier ressource" + realPath);
        } else {
            dtdFile = new File(resource.toURI());
        }

        //Créer un fichier temporaire copie de celui passé en paramètre et y ajoute le DTD.
        String[] splits = realPath.split("\\.");
        File tempFile = File.createTempFile("tmp_hexaone","."+splits[splits.length-1]);

        //Il sera supprimé à la fin de l'exécution de l'application
        tempFile.deleteOnExit();

        BufferedReader reader = new BufferedReader(new FileReader(realPath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line = reader.readLine();
        DTDWritingStage stage = DTDWritingStage.NONE;
        while(line!=null){
            writer.write(line);
            writer.newLine();
            if(stage!=DTDWritingStage.WRITED){
                if(stage == DTDWritingStage.NONE && line.startsWith("<?xml")) stage = DTDWritingStage.START_BALISE;
                if(stage == DTDWritingStage.START_BALISE && line.endsWith("?>")) stage = DTDWritingStage.END_BALISE;
                if(stage == DTDWritingStage.END_BALISE){
                    stage = DTDWritingStage.WRITED;
                    //Ecrire le DTD
                    BufferedReader dtdReader = new BufferedReader(new FileReader(dtdFile));
                    line = dtdReader.readLine();
                    while(line!=null){
                        writer.write(line);
                        writer.newLine();
                        line = dtdReader.readLine();
                    }
                    dtdReader.close();
                }
            }
            line = reader.readLine();
        }

        writer.flush();
        writer.close();
        reader.close();

        return tempFile;
    }
}
