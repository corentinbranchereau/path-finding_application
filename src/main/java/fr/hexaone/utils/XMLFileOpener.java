package fr.hexaone.utils;

import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;

/**
 * Permet d'ouvrir un fichier de type XML. Implémente FileFilter du package
 * java.io
 * 
 * @see FileFilter
 * @author HexaOne
 * @version 1.0
 */
public class XMLFileOpener implements FileFilter {

    /**
     * L'instance Singleton de la classe.
     */
    private static XMLFileOpener instance = null;

    /**
     * Constructeur par défaut privé.
     */
    private XMLFileOpener() {
    }

    /**
     * Implémentation du design pattern Singleton afin d'implémenter l'interface
     * FileFilter du package java.io en gardant un comportement static pour la
     * classe
     * 
     * @return L'unique instance de la classe XMLFileOpener. Si elle n'existe pas,
     *         l'instancie.
     */
    public static XMLFileOpener getInstance() {
        if (instance == null)
            instance = new XMLFileOpener();
        return instance;
    }

    /**
     * Ouverture d'un fichier XML dont l'URI est spécifié en paramètre
     * 
     * @param chemin  L'URI vers le fichier à ouvrir
     * @param typeDtd Le type de DTD à appliquer
     * @throws IOException               Lorsque le fichier spécifié n'existe pas
     * @throws FileBadExtensionException Lorsque l'extension du fichier n'est pas
     *                                   XML
     * @throws SAXException              Lorsque le fichier possède des erreurs de
     *                                   formatage
     * @throws DTDValidationException    Lorsque le fichier XML ne valide pas son
     *                                   DTD
     * @throws URISyntaxException        Lorsque il y a une erreur de syntaxe dans
     *                                   l'URI
     * @return Le document XML correspondant au format org.w3c.dom.Document
     */
    public Document ouvrirXml(String chemin, TypeDTD typeDtd)
            throws IOException, FileBadExtensionException, SAXException, DTDValidationException, URISyntaxException {
        File fichier = creerXMLTemporaireAvecDTD(chemin, typeDtd);
        if (!accept(fichier))
            throw new FileBadExtensionException("Extension de fichier incorrete, il faut du .XML.");
        Document xml = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true); // Active la vérification par le DTD
        ParserErrorHandler errorHandler = new ParserErrorHandler();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(errorHandler);
            xml = documentBuilder.parse(fichier);
            if (!errorHandler.getEstValide()) {
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
     * @param fichier Le fichier à vérifier
     * @return true si le fichier est au format XML, false sinon.
     */
    @Override
    public boolean accept(File fichier) {
        return fichier.getName().endsWith(".xml");
    }

    /**
     * Crée un fichier temporaire comportant le DTD correspondant au type passé en
     * paramètre. De cette façon, nous pouvons vérifier la bonne formation d'un
     * fichier XML non doté d'un DTD au départ.
     * 
     * @param cheminReel Le chemin du fichier réel de l'utilisateur
     * @param typeDtd    Le type de DTD à appliquer
     * @return Le fichier temporaire créé
     * @throws IOException        si problème d'I/O
     * @throws URISyntaxException si problème de syntaxe URI
     */
    private File creerXMLTemporaireAvecDTD(String cheminReel, TypeDTD typeDtd) throws IOException, URISyntaxException {
        // Charge le DTD du dossier ressource
        File fichierDtd = File.createTempFile("tmp_hexaone", ".dtd");
        FileUtils.copyInputStreamToFile(Utils.obtenirInputStreamDepuisChemin(this, typeDtd.getChemin()), fichierDtd);

        // Créer un fichier temporaire qui est une copie de celui passé en paramètre et
        // y ajoute le DTD.
        String[] splits = cheminReel.split("\\.");
        File fichierTemporaire = File.createTempFile("tmp_hexaone", "." + splits[splits.length - 1]);

        // Il sera supprimé à la fin de l'exécution de l'application
        fichierTemporaire.deleteOnExit();

        BufferedReader reader = new BufferedReader(new FileReader(cheminReel));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fichierTemporaire));

        String ligne = reader.readLine();
        EtatEcritureDTD etat = EtatEcritureDTD.AUCUN;
        while (ligne != null) {
            writer.write(ligne);
            writer.newLine();
            if (etat != EtatEcritureDTD.ECRIT) {
                if (etat == EtatEcritureDTD.AUCUN && ligne.startsWith("<?xml"))
                    etat = EtatEcritureDTD.DEBUT_BALISE;
                if (etat == EtatEcritureDTD.DEBUT_BALISE && ligne.endsWith("?>"))
                    etat = EtatEcritureDTD.FIN_BALISE;
                if (etat == EtatEcritureDTD.FIN_BALISE) {
                    etat = EtatEcritureDTD.ECRIT;
                    // Ecrit le DTD
                    BufferedReader dtdReader = new BufferedReader(new FileReader(fichierDtd));
                    ligne = dtdReader.readLine();
                    while (ligne != null) {
                        writer.write(ligne);
                        writer.newLine();
                        ligne = dtdReader.readLine();
                    }
                    dtdReader.close();
                }
            }
            ligne = reader.readLine();
        }

        writer.flush();
        writer.close();
        reader.close();

        return fichierTemporaire;
    }
}
